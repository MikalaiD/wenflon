package io.github.mikalaid.wenflon.core;

import io.github.mikalaid.wenflon.exceptions.WenflonException;

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

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.beans.factory.BeanCreationException;

class DynamicProxyManager<T> {

  static final String DEFAULT_KEYWORD = "default";
  private static final int MAX_DEFAULT_IMPL_ALLOWED = 1;
  public static final String CANNOT_DEFINE_IMPL = // todo move to a separate util class
      "Neither can find conditional implementation, nor can find the default one. Please check wenflon.conditions.* properties, or beans declarations, or 'soleConditionalImplAsImplicitDefault' property on @Wenflon";
  public static final String MISSING_PIVOT_PROVIDER = "Cannot find pivot provider. Either none was declared. Or pivot provider name used in @Wenflon cannot be match with any bean.";

  private final Set<Implementation> declaredImplementations;

  @Getter(AccessLevel.PACKAGE)
  private final Map<Implementation, WenflonCondition> conditionalImplementations;

  @Getter private final Set<Implementation> defaultImplementations;
  @Getter private final Class<T> representedInterface;
  private final Map<String, PivotProvider<?>> pivotProviders;

  @Getter private final T dynamicProxy;
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
            new Class<?>[] {representedInterface, io.github.mikalaid.wenflon.core.Proxy.class},
            (proxy, method, args) -> method.invoke(defineImplementation(), args)));
  }

  DynamicProxyManager<T> addImplementation(final Object bean, final String beanName) {
    final var implementation = new Implementation(bean, beanName);
    declaredImplementations.add(implementation);
    return this;
  }

  private Object defineImplementation() {
    return conditionalImplementations.entrySet().stream()
        .filter(implCase->implCase.getValue().getAsBoolean())
        .map(Map.Entry::getKey)
        .map(Implementation::getBean)
        .findFirst()//todo move 2 levels up?
        .orElseGet(
            () ->
                this.getDefaultImplementations().stream()
                    .findAny()
                    .map(Implementation::getBean)
                    .orElseThrow(() -> new WenflonException(CANNOT_DEFINE_IMPL)));
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
    final var simpleConditionAdded = addSimpleCondition(properties, impl);//todo convert it eplicitely to any of
    if(!simpleConditionAdded){
      addComplexCondition(properties, impl); //todo add tests and doc - simple condition trumps complex condition.
    }
  }

  private boolean addSimpleCondition(final WenflonProperties properties, final Implementation impl) {
    final var listOfSimpleConditionValues = properties.getConditions().get(impl.getBeanName());
    if(Objects.isNull(listOfSimpleConditionValues) || listOfSimpleConditionValues.isEmpty()){
      return false;
    }
    final var isDefaultImpl =
        listOfSimpleConditionValues.contains(DEFAULT_KEYWORD);
    if (isDefaultImpl) {
      validateNumberOfDefaultImpls(impl);
      this.defaultImplementations.add(impl);
    } else {
      if (pivotProviders.size()>1) {
        throw new WenflonException("Only single pivot provider is allowed when simple condition is used. Please verify if you do not have complex condition declared for the same implementation. Only one type of condition should be used per implementation"); //todo test & doc what is written here :D also move the string to some utils class
      }
      final var pivotProvider = pivotProviders.values().stream().findFirst().orElseThrow();
      final var implCondition = new WenflonCondition(() -> listOfSimpleConditionValues.contains(pivotProvider.getPivot().toString()));
      conditionalImplementations.put(impl, implCondition); //FINISHED HERE - to write tests
    }
    return true;
  }
  //So. this anyOf or allOff is related to providers, not to values they provide. Since each can provide only one value
  //todo move this to documentation
  private void addComplexCondition(final WenflonProperties properties, final Implementation impl){
    final var complexCondition = properties.getComplexConditions().get(impl.getBeanName());
    if(Objects.isNull(complexCondition) || complexCondition.isEmpty()){
      return;
    }
    if(complexCondition.size()>1){
      //todo add test
      throw new WenflonException("Only one complex condition type is allowed per implementation");
    }
    final var matchType = complexCondition.keySet().stream().findFirst().orElseThrow();
    final var providers = complexCondition.get(matchType);
    switch (matchType){
      case ALL_OF -> addAllOfProvidersCondition(providers, impl);
      case ANY_OF -> {}//todo
    }
  }

  private void addAllOfProvidersCondition(final Map<String, WenflonProperties.Condition> conditionPerProviderName, final Implementation impl) {

    final var implCondition = conditionPerProviderName.entrySet().stream()
            .map(entry -> {
              final var pivotProvider = this.pivotProviders.entrySet().stream()
                      .filter(providerEntry -> providerEntry.getKey().equals(entry.getKey()))
                      .map(Map.Entry::getValue)
                      .findFirst().orElseThrow(); //todo check this line if it works as intended, I think bean name would be better here, so will have to switch from set of providers to map
              final var conditionValues = entry.getValue().values(); //todo replace with set?
              return (BooleanSupplier) () -> conditionValues.contains(pivotProvider.getPivot());
            })
            .map(WenflonCondition::new)
            .reduce(WenflonCondition::addAnd);

    conditionalImplementations.put(impl, implCondition.get()); //todo temp - wait till merge with simple condition
  }

  private void validateNumberOfDefaultImpls(final Implementation impl) {
    if (this.defaultImplementations.size() >= MAX_DEFAULT_IMPL_ALLOWED) {
      throw new BeanCreationException(
          "Too many default default implementations declared. Current maximum per wenflon is %s. %s is declared as %s default implementation for %s" // todo move to separate util class
              .formatted(
                  MAX_DEFAULT_IMPL_ALLOWED,
                  impl.getBeanName(),
                  MAX_DEFAULT_IMPL_ALLOWED + 1,
                  this.getRepresentedInterfaceName()));
    }
  }

  private static Boolean conditionRepresentedInProperties(
      final WenflonProperties properties, final Implementation impl) {
    return Stream.concat(properties.getConditions().keySet().stream(), properties.getComplexConditions().keySet().stream())
            .anyMatch(name -> name.equals(impl.getBeanName()));
  }

  void addPivotProvider(final List<PivotProviderWrapper<?>> pivotProviders) {
    if (pivotProviders.size() == 1 && pivotProviderBeanNames.length==0) {
      this.pivotProviders.put(pivotProviders.get(0).getBeanName(), pivotProviders.get(0));
      return;
    }
    pivotProviders.stream()
            .filter(provider -> Arrays.stream(pivotProviderBeanNames).anyMatch(name -> name.equals(provider.getBeanName())))
            .forEach(provider->this.pivotProviders.put(provider.getBeanName(), provider));
    if(this.pivotProviders.isEmpty()){
      throw new BeanCreationException(MISSING_PIVOT_PROVIDER);
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
