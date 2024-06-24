package com.yosik.wenflon;

import com.yosik.wenflon.exceptions.WenflonException;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

public class WenflonDynamicProxy<T> {

  private static final String DEFAULT_KEY_WORD = "default";
  public static final String CANNOT_DEFINE_IMPL =
      "Neither can define implementation, nor can find the default one. Please check wenflon.conditions.* properties or beans declarations";
  private final Map<Implementation, Predicate<String>> conditionalImplementationCases;
  @Getter private Optional<Implementation> defaultImplementation;
  @Getter private final Class<T> representedInterface;
  private PivotProvider<?> pivotProvider;

  @Getter private final T wenflonProxy;

  WenflonDynamicProxy(final Class<T> representedInterface) {
    this.representedInterface = representedInterface;
    conditionalImplementationCases = new HashMap<>();
    wenflonProxy = createProxy();
    defaultImplementation = Optional.empty();
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
    conditionalImplementationCases.put(implementation, c -> false);
    return this;
  }

  private Object defineImplementation() {
    return conditionalImplementationCases.entrySet().stream()
        .filter(
            impl -> impl.getValue().test((String) pivotProvider.getPivot())) // todo ugly temp cast
        .map(Map.Entry::getKey)
        .map(Implementation::getBean)
        .findFirst()
        .orElseGet(
            () ->
                this.getDefaultImplementation()
                    .map(Implementation::getBean)
                    .orElseThrow(() -> new WenflonException(CANNOT_DEFINE_IMPL)));
    // todo #15 cover in tests
  }

  public String getName() {
    return String.format("wenflon-%s", this.representedInterface.getSimpleName());
  }

  public String getProxyName() {
    return String.format("wproxy-%s", this.representedInterface.getSimpleName());
  }

  void addConditions(final WenflonProperties properties) {
    conditionalImplementationCases.entrySet().stream()
        .filter(entry -> conditionRepresentedInProperties(properties, entry))
        .forEach(entry -> addCondition(properties, entry));
  }

  private void addCondition(
      final WenflonProperties properties,
      final Map.Entry<Implementation, Predicate<String>> entry) {
    final var impl = entry.getKey();
    final var isDefaultImpl =
        properties
            .getConditions()
            .get(impl.getBeanName())
            .contains(DEFAULT_KEY_WORD); // todo #15 write test so default can be present with other
    // todo #15 write test & validation that only one implementation can be default; in final
    // assembler
    if (isDefaultImpl) {
      this.defaultImplementation = Optional.of(impl);
    } else {
      conditionalImplementationCases.put(
          impl, s -> properties.getConditions().get(impl.getBeanName()).contains(s));
    }
  }

  private static Boolean conditionRepresentedInProperties(
      final WenflonProperties properties,
      final Map.Entry<Implementation, Predicate<String>> entry) {
    return Optional.ofNullable(properties.getConditions())
        .map(
            conditions ->
                conditions.keySet().stream()
                    .anyMatch(name -> name.equals(entry.getKey().getBeanName())))
        .orElse(false);
  }

  void addPivotProvider(final List<PivotProvider<?>> pivotProviders) {
    // todo only one - first best - is supported now
    this.pivotProvider = pivotProviders.stream().findFirst().orElseThrow();
  }

  @AllArgsConstructor
  @EqualsAndHashCode(of = "bean")
  @Getter
  private static class Implementation {
    private final Object bean;
    private final String beanName;
  }
}
