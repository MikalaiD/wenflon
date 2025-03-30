package io.github.mikalaid.wenflon.core;

import io.github.mikalaid.wenflon.exceptions.WenflonException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;


@ConfigurationProperties(prefix = "wenflon")
@Getter
class WenflonProperties {
    @Setter
    private Map<String, List<String>> conditions = new HashMap<>();
    private Map<String, Map<MatchType, Map<String, Condition>>> complexConditions = new HashMap<>();

    public void setComplexConditions(final Map<String, Map<MatchType, Map<String, Map<String, List<String>>>>> complexConditions) {
        this.complexConditions = complexConditions.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, entryA->entryA.getValue().entrySet().stream()
                        .collect(Collectors.toMap(Map.Entry::getKey, entryB->entryB.getValue().entrySet().stream()
                                .collect(Collectors.toMap(Map.Entry::getKey, entryC-> getLimit(entryC.getValue())))))));
    }

    private static Condition getLimit(final  Map<String, List<String>> conditionMap) {
        final var condition = conditionMap.entrySet();
        //todo write test for this condition
        if(condition.size()>1){
            throw new WenflonException("Only single condition type is allowed! " + conditionMap);
        }
        return condition.stream().map(c->new Condition(c.getKey(), c.getValue())).findFirst().orElseThrow(()-> new WenflonException("Condition is empty!"));
    }

    @AllArgsConstructor
    enum MatchType {
        ANY_OF,
        ALL_OF
    }

    static class Condition {
        private final Type type;
        private final Predicate<? super Object> predicate;

        public Condition(final String type, final List<String> inputValues) {
            final var args = new ArrayList<>(inputValues);
            this.type = Type.valueOfLabel(type);
            this.predicate =  switch (this.type) {
                case VALUES -> target -> args.contains(target.toString());
                case LESS_THAN, LESS_THAN_CLOSED, MORE_THAN, MORE_THAN_CLOSED -> target -> {
                    if(target instanceof Number number){
                        final var bigDecimalTarget = new BigDecimal(String.valueOf(number));
                        final var limit = new BigDecimal(args.get(0));
                        return switch (this.type){
                            case LESS_THAN -> bigDecimalTarget.compareTo(limit) < 0;
                            case LESS_THAN_CLOSED -> bigDecimalTarget.compareTo(limit) <=0;
                            case MORE_THAN -> bigDecimalTarget.compareTo(limit) > 0;
                            case MORE_THAN_CLOSED -> bigDecimalTarget.compareTo(limit) >=0;
                            default -> throw new UnsupportedOperationException();
                        };
                    }
                    throw new WenflonException(Messages.wrongClass(BigDecimal.class.getSimpleName(), target.getClass().getSimpleName())); //Finished here conditions test failing
                };
                case RANGE, RANGE_CLOSED -> target -> {
                    if(target instanceof Number number){
                        final var bigDecimalTarget = new BigDecimal(String.valueOf(number));
                        final var limitDown = new BigDecimal(args.get(0));
                        final var limitUp = new BigDecimal(args.get(1));
                        return switch (this.type){
                            case RANGE -> bigDecimalTarget.compareTo(limitUp) < 0 && bigDecimalTarget.compareTo(limitDown)>=0;
                            case RANGE_CLOSED -> bigDecimalTarget.compareTo(limitUp) <= 0 && bigDecimalTarget.compareTo(limitDown)>=0;
                            default -> throw new UnsupportedOperationException();
                        };
                    }
                    throw new WenflonException(Messages.wrongClass(BigDecimal.class.getSimpleName(), target.getClass().getSimpleName()));
                } ;
            };
        }

        public boolean test(final Object input){
            return this.predicate.test(input);
        }

        @AllArgsConstructor
        enum Type{
            //todo write tests for each case
            VALUES("values"),
            MORE_THAN("moreThan"),
            MORE_THAN_CLOSED("moreThanClosed"),
            LESS_THAN("lessThan"),
            LESS_THAN_CLOSED("lessThanClosed"),
            RANGE("range"),
            RANGE_CLOSED("rangeClosed");

            private String label;
            private static final Map<String, Type> BY_LABEL = new HashMap<>();

            static {
                for (Type e: values()) {
                    BY_LABEL.put(e.label, e);
                }
            }
            static Type valueOfLabel(final String label) {
                final var type = BY_LABEL.get(label);
                if (type==null){
                    throw new WenflonException(Messages.getConditionTypeNotRecognized(label));
                }
                return type;
            }
        }
//        Condition normalised() {
//            return new Condition(values().stream().map(Object::toString).map(value -> {
//                //todo maybe to add decimal as well
//                try {
//                    return Integer.valueOf(value);
//                } catch (NumberFormatException e) {
//                    return value;
//                }
//            }).toList(), moreThan());//todo temp
//        }
    }
}

