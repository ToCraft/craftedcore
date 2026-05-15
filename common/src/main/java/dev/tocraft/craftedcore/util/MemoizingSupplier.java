package dev.tocraft.craftedcore.util;

import java.util.function.Supplier;

public class MemoizingSupplier<T> implements Supplier<T> {
    private final Supplier<T> delegate;
    private volatile boolean initialized = false;
    private T value;

    public MemoizingSupplier(Supplier<T> delegate) {
        this.delegate = delegate;
    }

    @Override
    public T get() {
        if (!initialized) {
            synchronized (this) {
                if (!initialized) {
                    T t = delegate.get();
                    value = t;
                    initialized = true;
                    return t;
                }
            }
        }
        return value;
    }
}