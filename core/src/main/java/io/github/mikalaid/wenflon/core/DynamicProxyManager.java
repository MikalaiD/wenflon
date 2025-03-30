package io.github.mikalaid.wenflon.core;

import io.github.mikalaid.wenflon.exceptions.WenflonException;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.beans.factory.BeanCreationException;

import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.BooleanSupplier;
import java.util.stream.Stream;

class DynamicProxyManager<T> {

    static final String DEFAULT_KEYWORD = "default";
    private static final int MAX_DEFAULT_IMPL_ALLOWED = 1;

    private final Set<Implementation> declaredImplementations;

    @Getter(AccessLevel.PACKAGE)
    private final Map<Implementation, LazyConditionsBucket> conditionalImplementations;

    @Getter
    private final Set<Implementation> defaultImplementations;
    @Getter
    private final Class<T> representedInterface;
    private final Map<String, PivotProvider<?>> pivotProviders;

    @Getter
    private final T dynamicProxy;
    private final boolean soleConditionalImplAsImplicitDefault;
    private final String[] pivotProviderBeanNames;

    DynamicProxyManager(final Class<T> representedInterface) {
        this.representedInterface = representedInterface;
        this.declaredImplementations = new HashSet<>();
        this.conditionalImplementations = new HashMap<>();
        this.pivotProviders = new HashMap<>();
        this.defaultImplementations = new HashSet<>();
        this.dynamicProxy = createProxy();
        this.soleConditionalImplAsImplicitDefault =
                representedInterface.getAnnotation(Wenflon.class).soleConditionalImplAsImplicitDefault();
        this.pivotProviderBeanNames =
                representedInterface.getAnnotation(Wenflon.class).pivotProviderBeanNames();
    }

    private T createProxy() {
        return representedInterface.cast(
                Proxy.newProxyInstance(
                        DynamicProxyManager.class.getClassLoader(),
                        new Class<?>[]{representedInterface, io.github.mikalaid.wenflon.core.Proxy.class},
                        (proxy, method, args) -> method.invoke(defineImplementation(), args)));
    }

    DynamicProxyManager<T> addImplementation(final Object bean, final String beanName) {
        final var implementation = new Implementation(bean, beanName);
        declaredImplementations.add(implementation);
        return this;
    }

    private Object defineImplementation() {
        return conditionalImplementations.entrySet().stream()
                .filter(implCase -> implCase.getValue().getAsBoolean())
                .findFirst()
                .map(Map.Entry::getKey)
                .map(Implementation::getBean)
                .orElseGet(
                        () ->
                                this.getDefaultImplementations().stream()
                                        .findAny()
                                        .map(Implementation::getBean)
                                        .orElseThrow(() -> new WenflonException(Messages.CANNOT_DEFINE_IMPL)));
    }

    String getName() {
        return String.format("wenflon-%s", this.representedInterface.getSimpleName());
    }

    String getProxyName() {
        return String.format("wproxy-%s", this.representedInterface.getSimpleName());
    }

    String getRepresentedInterfaceName() {
        return this.representedInterface.getSimpleName();
    }

    void trySetImplicitDefault() {
        final var soleImplWithNoConditions =
                declaredImplementations.size() == 1
                        && conditionalImplementations.isEmpty()
                        && defaultImplementations.isEmpty();
        final var soleConditionalImplImplicit =
                soleConditionalImplAsImplicitDefault
                        && defaultImplementations.isEmpty()
                        && conditionalImplementations.size() == 1;
        if (soleImplWithNoConditions) {
            defaultImplementations.addAll(declaredImplementations);
        } else if (soleConditionalImplImplicit) {
            defaultImplementations.addAll(conditionalImplementations.keySet());
        }
    }

    void addConditions(final WenflonProperties properties) {
        declaredImplementations.stream()
                .filter(impl -> conditionRepresentedInProperties(properties, impl))
                .forEach(impl -> addConditions(properties, impl));
    }

    private void addConditions(final WenflonProperties properties, final Implementation impl) {
        final var simpleConditionAdded = addSimpleCondition(properties, impl);
        if (!simpleConditionAdded) {
            addComplexCondition(properties, impl); //todo add tests and doc - simple condition trumps complex condition.
        }
    }

    private boolean addSimpleCondition(final WenflonProperties properties, final Implementation impl) {
        final var listOfSimpleConditionValues = properties.getConditions().get(impl.getBeanName());
        if (Objects.isNull(listOfSimpleConditionValues) || listOfSimpleConditionValues.isEmpty()) {
            return false;
        }
        final var isDefaultImpl =
                listOfSimpleConditionValues.contains(DEFAULT_KEYWORD);
        if (isDefaultImpl) {
            validateNumberOfDefaultImpls(impl);
            this.defaultImplementations.add(impl);
        } else {
            if (pivotProviders.size() > 1) {
                throw new WenflonException(Messages.ONLY_SINGLE_PROVIDER_FOR_SIMPLE); //todo test & doc what is written here :D
            }
            final var pivotProvider = pivotProviders.values().stream().findFirst().orElseThrow();
            final var implCondition = LazyConditionsBucket.createAndBucket(List.of(() -> listOfSimpleConditionValues.contains(pivotProvider.getPivot().toString())));
            conditionalImplementations.put(impl, implCondition);
        }
        return true;
    }

    //So. this anyOf or allOff is related to providers, not to values they provide. Since each can provide only one value
    //todo move this to documentation
    private void addComplexCondition(final WenflonProperties properties, final Implementation impl) {
        final var complexCondition = properties.getComplexConditions().get(impl.getBeanName());
        if (Objects.isNull(complexCondition) || complexCondition.isEmpty()) {
            return; //todo shouldn't exception be thrown here since it seems that at this point no condition is provided
        }
        if (complexCondition.size() > 1) {
            //todo add test
            throw new WenflonException("Only one complex condition type is allowed per implementation");
        }

        final var conditionCase = complexCondition.entrySet().stream().findAny().orElseThrow();
        addCondition(conditionCase, impl);
    }

    private void addCondition(final Map.Entry<WenflonProperties.MatchType, Map<String, WenflonProperties.Condition>> conditionCase,
                              final Implementation impl) {
        final var matchType = conditionCase.getKey();
        final var conditionPerProviderName = conditionCase.getValue();
        final var conditionsAsBooleanSupplierList = conditionPerProviderName.entrySet().stream()
                .map(conditionPerProvider -> {
                    final PivotProvider<?> pivotProvider = this.pivotProviders.get(conditionPerProvider.getKey());
                    if(pivotProvider==null){
                        throw new WenflonException("Provider specified in conditions cannot be found among registered providers");
                    }
                    final var condition = conditionPerProvider.getValue();
                    return (BooleanSupplier) () -> condition.test(pivotProvider.getPivot());
                })
                .toList();
        final var lazyConditionsBucket =
                switch (matchType) {
                    case ALL_OF -> LazyConditionsBucket.createAndBucket(conditionsAsBooleanSupplierList);
                    case ANY_OF -> LazyConditionsBucket.createOrBucket(conditionsAsBooleanSupplierList);
                };
        conditionalImplementations.put(impl, lazyConditionsBucket); //todo temp - wait till merge with simple condition
    }

    private void validateNumberOfDefaultImpls(final Implementation impl) {
        if (this.defaultImplementations.size() >= MAX_DEFAULT_IMPL_ALLOWED) {
            throw new BeanCreationException(
                    Messages.getTooManyDefaultImplsMessage(MAX_DEFAULT_IMPL_ALLOWED, impl.getBeanName(), this.getRepresentedInterfaceName()));
        }
    }

    private static Boolean conditionRepresentedInProperties(
            final WenflonProperties properties, final Implementation impl) {
        return Stream.concat(properties.getConditions().keySet().stream(), properties.getComplexConditions().keySet().stream())
                .anyMatch(name -> name.equals(impl.getBeanName()));
    }

    void addPivotProvider(final List<PivotProviderWrapper<?>> pivotProviders) {
        if (pivotProviders.size() == 1 && pivotProviderBeanNames.length == 0) {
            this.pivotProviders.put(pivotProviders.get(0).getBeanName(), pivotProviders.get(0));
            return;
        }
        pivotProviders.stream()
                .filter(provider -> Arrays.stream(pivotProviderBeanNames).anyMatch(name -> name.equals(provider.getBeanName())))
                .forEach(provider -> this.pivotProviders.put(provider.getBeanName(), provider));
        if (this.pivotProviders.isEmpty()) {
            throw new BeanCreationException(Messages.MISSING_PIVOT_PROVIDER);
        }
    }

    @AllArgsConstructor
    @EqualsAndHashCode(of = "bean")
    @Getter
    static class Implementation {
        private final Object bean;
        private final String beanName;
    }
}
