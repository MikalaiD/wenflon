package io.github.mikalaid.wenflon.core;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BooleanSupplier;

class WenflonCondition implements BooleanSupplier {
    private final List<BooleanSupplier> suppliers = new ArrayList<>();

    public WenflonCondition(final BooleanSupplier initial) {
        suppliers.add(initial);
    }

    public WenflonCondition addAnd(final BooleanSupplier andCondition) {
        suppliers.add(andCondition);
        return this;
    }

    public WenflonCondition addOr(final BooleanSupplier orCondition){
        final BooleanSupplier current = ()->suppliers.stream()
                .allMatch(BooleanSupplier::getAsBoolean);
        suppliers.clear();
        suppliers.add(()-> current.getAsBoolean() || orCondition.getAsBoolean());
        return this;
    }

    @Override
    public boolean getAsBoolean() {
        return suppliers.stream()
                .allMatch(BooleanSupplier::getAsBoolean);
    }

}
