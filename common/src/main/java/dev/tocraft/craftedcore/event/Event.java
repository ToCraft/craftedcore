package dev.tocraft.craftedcore.event;

import org.jetbrains.annotations.ApiStatus;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Function;

@SuppressWarnings({"UnusedReturnValue", "unused"})
public class Event<T> {
    private final Function<List<T>, T> invoker;
    private final List<T> handlers = new CopyOnWriteArrayList<>();

    @ApiStatus.Internal
    protected Event(Function<List<T>, T> invoker) {
        this.invoker = invoker;
    }

    public void register(T handler) {
        handlers.add(handler);
    }

    public T invoke() {
        return invoker.apply(handlers);
    }
}
