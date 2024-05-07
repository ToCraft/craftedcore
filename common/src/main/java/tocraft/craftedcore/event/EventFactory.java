package tocraft.craftedcore.event;

import net.minecraft.world.InteractionResult;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.Proxy;
import java.util.List;
import java.util.function.Function;

@SuppressWarnings("unused")
public final class EventFactory {
    /**
     * @param invoker check each callback, wherever it stops the event
     * @param <T>     a functional interface used for invocation
     * @return the created event you can use for invocation
     */
    @Contract(value = "_ -> new", pure = true)
    public static <T> @NotNull Event<T> createWithCallback(Function<List<T>, T> invoker) {
        return new Event<>(invoker);
    }

    /**
     * {@link #createWithInteractionResult(Class)}
     *
     * @param typeGetter this is ignored and only works to get the event class
     */
    @SuppressWarnings("unchecked")
    @SafeVarargs
    public static <T> @NotNull Event<T> createWithInteractionResult(T... typeGetter) {
        return (Event<T>) createWithInteractionResult(typeGetter.getClass().getComponentType());
    }

    /**
     * This creates an event that automatically stops when an {@link InteractionResult} that isn't a {@link InteractionResult#PASS} is passed
     * your functional interface will return this {@link InteractionResult}
     *
     * @param clazz your event handler class
     * @param <T>   a functional interface used for invocation
     * @return the created event you can use for invocation
     */
    @SuppressWarnings({"unchecked"})
    @Contract(value = "_ -> new", pure = true)
    public static <T> @NotNull Event<T> createWithInteractionResult(Class<T> clazz) {
        return new Event<>(listeners -> (T) Proxy.newProxyInstance(EventFactory.class.getClassLoader(), new Class[]{clazz}, (proxy, method, args) -> {
            for (T listener : listeners) {
                Object result = MethodHandles.lookup().unreflect(method)
                        .bindTo(listener).invokeWithArguments(args);

                if (result != InteractionResult.PASS) {
                    return result;
                }
            }
            return InteractionResult.PASS;
        }));
    }

    /**
     * {@link #createWithVoid(Class)}
     *
     * @param typeGetter this is ignored and only works to get the event class
     */
    @SuppressWarnings("unchecked")
    @SafeVarargs
    public static <T> @NotNull Event<T> createWithVoid(T... typeGetter) {
        return (Event<T>) createWithVoid(typeGetter.getClass().getComponentType());
    }

    /**
     * @param clazz your event handler class
     * @param <T>   a functional interface used for invocation
     * @return the created event you can use for invocation
     */
    @SuppressWarnings({"unchecked", "SuspiciousInvocationHandlerImplementation"})
    @Contract(value = "_ -> new", pure = true)
    public static <T> @NotNull Event<T> createWithVoid(Class<T> clazz) {
        return new Event<>(listeners -> (T) Proxy.newProxyInstance(EventFactory.class.getClassLoader(), new Class[]{clazz}, (proxy, method, args) -> {
            for (T listener : listeners) {
                MethodHandles.lookup().unreflect(method)
                        .bindTo(listener).invokeWithArguments(args);
            }
            return null;
        }));
    }
}
