package io.github.mikalaid.wenflon.core;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "wenflon")
@Getter

class WenflonProperties {
  @Setter
  private Map<String, List<String>> conditions;

  private Map<String, Map<MatchType, Map<String, Condition>>> complexConditions;

  public void setComplexConditions(final Map<String, Map<MatchType, Map<String, Condition>>> complexConditions) {
    complexConditions
            .values()
            .stream()
            .flatMap(it->it.values().stream())
            .flatMap(it->it.entrySet().stream())
            .forEach(it->it.setValue(it.getValue().withDefinedValueType()));
    this.complexConditions = complexConditions;
  }

  @AllArgsConstructor
  enum MatchType {
    ANY_OF,
    ALL_OF
  }

  @AllArgsConstructor
  @Getter
  enum ValueType {
    STR(Function.identity()),
    INT((Function<String, Integer>)Integer::valueOf);
    final Function<String, ?> converter;
  }


  record Condition(ValueType type, List<?> values){
    Condition withDefinedValueType(){
      return new Condition(type, values().stream().map(Object::toString).map(value->type.getConverter().apply(value)).toList());
    }
  }
}

