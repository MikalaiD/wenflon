package io.github.mikalaid.wenflon.core;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ConfigurationProperties(prefix = "wenflon")
@Getter
class WenflonProperties {
    @Setter
    private Map<String, List<String>> conditions = new HashMap<>();
    private Map<String, Map<MatchType, Map<String, Condition>>> complexConditions = new HashMap<>();

    public void setComplexConditions(final Map<String, Map<MatchType, Map<String, Condition>>> complexConditions) {
        complexConditions
                .values()
                .stream()
                .flatMap(it -> it.values().stream())
                .flatMap(it -> it.entrySet().stream())
                .forEach(it -> it.setValue(it.getValue().withDefinedValueType()));
        this.complexConditions = complexConditions;
    }

    @AllArgsConstructor
    enum MatchType {
        ANY_OF,
        ALL_OF
    }


    record Condition(List<?> values) {
        Condition withDefinedValueType() {
            return new Condition(values().stream().map(Object::toString).map(value -> {
                //todo maybe to add decimal as well
                try {
                    return Integer.valueOf(value);
                } catch (NumberFormatException e) {
                    return value;
                }
            }).toList());
        }
    }
}

