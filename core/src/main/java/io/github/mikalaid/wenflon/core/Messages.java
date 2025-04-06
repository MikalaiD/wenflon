package io.github.mikalaid.wenflon.core;

import lombok.experimental.UtilityClass;

import java.util.Arrays;
import java.util.Map;

@UtilityClass
class Messages {
    String CANNOT_DEFINE_IMPL = """
    Neither can find conditional implementation, nor can find the default one. Please check wenflon.conditions.* \
    properties, or beans declarations, or 'soleConditionalImplAsImplicitDefault' property on @Wenflon
    """;
    String MISSING_PIVOT_PROVIDER = """
    Cannot find pivot provider. Either none was declared. \
    Or pivot provider name used in @Wenflon cannot be match with any bean.
    """;

    String ONLY_SINGLE_PROVIDER_FOR_SIMPLE = """
            Only single pivot provider is allowed when simple condition is used. \
            Please verify if you do not have complex condition declared for the same implementation. \
            Only one type of condition should be used per implementation
            """;
    private final String TOO_MANY_DEFAULT_IMPLEMENTATIONS = """
            Too many default implementations declared. Current maximum per wenflon is %s. \
            %s is declared as %s default implementation for %s
            """;
    static String getTooManyDefaultImplsMessage(final int maxDefaultImplAllowed, final String beanName, final String representedInterfaceName) {
        return TOO_MANY_DEFAULT_IMPLEMENTATIONS.formatted(
                        maxDefaultImplAllowed,
                        beanName,
                        maxDefaultImplAllowed + 1,
                        representedInterfaceName);
    }

    private static String CONDITION_UNEXPECTED_CLASS = "Unexpected class in condition, expected %s, but got %s";

    static String wrongClass(final String expectedClassName, final String actualClassName ){
        return CONDITION_UNEXPECTED_CLASS.formatted(expectedClassName, actualClassName);
    }

    private static String CONDITION_TYPE_NOT_RECOGNIZED = "Condition type not recognised: %s";
    public static String getConditionTypeNotRecognized(final String type) {
        return CONDITION_TYPE_NOT_RECOGNIZED.formatted(type);
    }

    private static String OLNY_ONE_COMPLEX_CONDITION_TYPE_ALLOWED = "Only one complex condition type of %s is allowed per implementation";

    public static String getOnlyOneComplexConditionTypeAllowed(){
        return OLNY_ONE_COMPLEX_CONDITION_TYPE_ALLOWED.formatted(Arrays.stream(WenflonProperties.MatchType.values()).toList());
    }

    private static String ONLY_SINGLE_CONDITION_IS_ALLOWED = "Only single condition is allowed! Got the following conditions map though: %s";

    public static String getOnlySingleConditionIsAllowed(final Map<String, ?> map){
        return ONLY_SINGLE_CONDITION_IS_ALLOWED.formatted(map);
    }
}
