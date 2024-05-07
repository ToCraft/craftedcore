package tocraft.craftedcore.event;

import org.jetbrains.annotations.ApiStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@SuppressWarnings({"UnusedReturnValue", "unused"})
public class Event<T> {
    private final Function<List<T>, T> invoker;
    private final List<T> handlers = new ArrayList<>();

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
