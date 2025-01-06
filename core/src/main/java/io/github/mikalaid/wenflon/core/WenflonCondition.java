package io.github.mikalaid.wenflon.core;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import java.util.function.BooleanSupplier;

@AllArgsConstructor(access = AccessLevel.PACKAGE)
class WenflonCondition{

    private BooleanSupplier condition;
    public WenflonCondition addAnd(final WenflonCondition wenflonCondition){
        condition=()->this.condition.getAsBoolean() && wenflonCondition.condition.getAsBoolean();
        return this;
    }

    public boolean isMatch() {
        return condition.getAsBoolean();
    }
}
