package io.github.mikalaid.wenflon.core;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BooleanSupplier;

class LazySuppliersBucket implements BooleanSupplier {

    final List<BooleanSupplier> suppliers = new ArrayList<>();
    final Type type;

    private LazySuppliersBucket(final Type type) {
        this.type = type;
    }

    static LazySuppliersBucket createAndBucket(final List<BooleanSupplier> suppliers){
        final var lazySuppliersBucket = new LazySuppliersBucket(Type.AND);
        lazySuppliersBucket.suppliers.addAll(suppliers);
        return lazySuppliersBucket;
    }
    static LazySuppliersBucket createOrBucket(final List<BooleanSupplier> suppliers){
        final var lazySuppliersBucket = new LazySuppliersBucket(Type.OR);
        lazySuppliersBucket.suppliers.addAll(suppliers);
        return lazySuppliersBucket;
    }

    LazySuppliersBucket add(final BooleanSupplier andCondition) {
        suppliers.add(andCondition);
        return this;
    }

    @Override
    public boolean getAsBoolean() {
        return switch (this.type ) {
            case AND -> suppliers.stream()
                    .allMatch(BooleanSupplier::getAsBoolean);
            case OR -> suppliers.stream().anyMatch(BooleanSupplier::getAsBoolean);
        };
    }

    private enum Type {
        AND, OR
    }
}
