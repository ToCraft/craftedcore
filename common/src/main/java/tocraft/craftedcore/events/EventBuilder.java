package tocraft.craftedcore.events;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import com.google.common.reflect.AbstractInvocationHandler;

@SuppressWarnings("unchecked")
public class EventBuilder {
	
	
	@SafeVarargs
    public static <T> Event<T> createLoop(T... typeGetter) {
        if (typeGetter.length != 0) throw new IllegalStateException("array must be empty!");
        return createLoop((Class<T>) typeGetter.getClass().getComponentType());
    }
	
	public static <T> Event<T> createLoop(Class<T> clazz) {
    	// make an event
        return new EventImpl<>(listeners -> (T) Proxy.newProxyInstance(EventBuilder.class.getClassLoader(), new Class[]{clazz}, new AbstractInvocationHandler() {
            @Override
            protected Object handleInvocation(Object proxy, Method method, Object[] args) throws Throwable {
                for (var listener : listeners) {
                	// invoke method per listener
                	MethodHandles.lookup().unreflect(method).bindTo(listener).invokeWithArguments(args);
                }
                return null;
            }
        }));
    }
	
	@SafeVarargs
    public static <T> Event<T> createEventResult(T... typeGetter) {
        if (typeGetter.length != 0) throw new IllegalStateException("array must be empty!");
        return createEventResult((Class<T>) typeGetter.getClass().getComponentType());
    }
	
    public static <T> Event<T> createEventResult(Class<T> clazz) {
        return new EventImpl<>(listeners -> (T) Proxy.newProxyInstance(EventBuilder.class.getClassLoader(), new Class[]{clazz}, new AbstractInvocationHandler() {
            @Override
            protected Object handleInvocation(Object proxy, Method method, Object[] args) throws Throwable {
                for (var listener : listeners) {
                    var result = (Event.Result) MethodHandles.lookup().unreflect(method).bindTo(listener).invokeWithArguments(args);
                    if (result.interruptsFurtherEvaluation()) {
                        return result;
                    }
                }
                return Event.Result.pass();
            }
        }));
    }
	
    private static class EventImpl<T> implements Event<T> {
        private final Function<List<T>, T> function;
        private T invoker = null;
        private ArrayList<T> listeners;
        
        public EventImpl(Function<List<T>, T> function) {
            this.function = function;
            this.listeners = new ArrayList<>();
        }
        
        @Override
        public T invoker() {
            if (invoker == null) {
                update();
            }
            return invoker;
        }
        
        @Override
        public void register(T listener) {
            listeners.add(listener);
            invoker = null;
        }
        
        @Override
        public void unregister(T listener) {
            listeners.remove(listener);
            listeners.trimToSize();
            invoker = null;
        }
        
        @Override
        public boolean isRegistered(T listener) {
            return listeners.contains(listener);
        }
        
        @Override
        public void clearListeners() {
            listeners.clear();
            listeners.trimToSize();
            invoker = null;
        }
        
        public void update() {
            if (listeners.size() == 1) {
                invoker = listeners.get(0);
            } else {
                invoker = function.apply(listeners);
            }
        }
    }
}
