package com.yosik.wenflon;

import com.yosik.wenflon.exceptions.WenflonException;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.beans.factory.support.BeanDefinitionValidationException;

class WenflonDynamicProxy<T> {

  static final String DEFAULT_KEYWORD = "default";
  private static final int MAX_DEFAULT_IMPL_ALLOWED = 1;
  public static final String CANNOT_DEFINE_IMPL =
      "Neither can find conditional implementation, nor can find the default one. Please check wenflon.conditions.* properties, or beans declarations, or 'soleConditionalImplAsImplicitDefault' property on @Wenflon";

  private final Set<Implementation> declaredImplementations;

  @Getter(AccessLevel.PACKAGE)
  private final Map<Implementation, Predicate<String>> conditionalImplementations;

  @Getter private final Set<Implementation> defaultImplementations;
  @Getter private final Class<T> representedInterface;
  private PivotProvider<?> pivotProvider;

  @Getter private final T wenflonProxy;
  private final boolean soleConditionalImplAsImplicitDefault;

  WenflonDynamicProxy(final Class<T> representedInterface) {
    this.representedInterface = representedInterface;
    this.declaredImplementations = new HashSet<>();
    this.conditionalImplementations = new HashMap<>();
    this.defaultImplementations = new HashSet<>();
    this.wenflonProxy = createProxy();
    this.soleConditionalImplAsImplicitDefault =
        representedInterface.getAnnotation(Wenflon.class).soleConditionalImplAsImplicitDefault();
  }

  private T createProxy() {
    return representedInterface.cast(
        Proxy.newProxyInstance(
            WenflonDynamicProxy.class.getClassLoader(),
            new Class<?>[] {representedInterface, com.yosik.wenflon.Proxy.class},
            (proxy, method, args) -> method.invoke(defineImplementation(), args)));
  }

  WenflonDynamicProxy<T> addImplementation(final Object bean, final String beanName) {
    final var implementation = new Implementation(bean, beanName);
    declaredImplementations.add(implementation);
    return this;
  }

  private Object defineImplementation() {
    return conditionalImplementations.entrySet().stream()
        .filter(
            impl -> impl.getValue().test((String) pivotProvider.getPivot())) // todo ugly temp cast
        .map(Map.Entry::getKey)
        .map(Implementation::getBean)
        .findFirst()
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

  void addConditions(final WenflonProperties properties) {
    declaredImplementations.stream()
        .filter(impl -> conditionRepresentedInProperties(properties, impl))
        .forEach(impl -> addCondition(properties, impl));
  }

  void trySetImplicitDefault() {
    final var soleImplWithNoConditions =
        declaredImplementations.size() == 1
            && conditionalImplementations.isEmpty()
            && defaultImplementations.isEmpty();
    final var soleConditionalImplImplicit = soleConditionalImplAsImplicitDefault
            && defaultImplementations.isEmpty()
            && conditionalImplementations.size() == 1;
    if (soleImplWithNoConditions) {
      defaultImplementations.addAll(declaredImplementations);
    } else if (soleConditionalImplImplicit) {
      defaultImplementations.addAll(conditionalImplementations.keySet());
    }
  }

  private void addCondition(final WenflonProperties properties, final Implementation impl) {
    final var isDefaultImpl =
        properties.getConditions().get(impl.getBeanName()).contains(DEFAULT_KEYWORD);
    if (isDefaultImpl) {
      validateNumberOfDefaultImpls(impl);
      this.defaultImplementations.add(impl);
    } else {
      conditionalImplementations.put(
          impl, s -> properties.getConditions().get(impl.getBeanName()).contains(s));
    }
  }

  private void validateNumberOfDefaultImpls(final Implementation impl) {
    if (this.defaultImplementations.size() >= MAX_DEFAULT_IMPL_ALLOWED) {
      throw new BeanDefinitionValidationException(
          "Too many default default implementations declared. Current maximum per wenflon is %s. %s is declared as %s default implementation for %s"
              .formatted(
                  MAX_DEFAULT_IMPL_ALLOWED,
                  impl.getBeanName(),
                  MAX_DEFAULT_IMPL_ALLOWED + 1,
                  this.getRepresentedInterfaceName()));
    }
  }

  private static Boolean conditionRepresentedInProperties(
      final WenflonProperties properties, final Implementation impl) {
    return Optional.ofNullable(properties.getConditions())
        .map(
            conditions ->
                conditions.keySet().stream().anyMatch(name -> name.equals(impl.getBeanName())))
        .orElse(false);
  }

  void addPivotProvider(final List<PivotProvider<?>> pivotProviders) {
    // todo only one - first best - is supported now
    this.pivotProvider = pivotProviders.stream().findFirst().orElseThrow();
  }

  @AllArgsConstructor
  @EqualsAndHashCode(of = "bean")
  @Getter
  static class Implementation {
    private final Object bean;
    private final String beanName;
  }
}
