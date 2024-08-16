package com.github.mikalaid.wenflon.core;

/**
 * Interface to return a pivot - a runtime value which will be tested against conditions; based on the outcome one or another
 * implementation of {@link Wenflon} annotated interface will be used to run called method.
 * Implementations of PivotProvider must be declared as spring beans. Multiple implementations are possible - then, to bind particular
 * {@link Wenflon} with particular PivotProvider use bean names of the latter.
 * @param <T> currently only String is supported
 */
@FunctionalInterface
public interface PivotProvider<T>{
  T getPivot();
}
