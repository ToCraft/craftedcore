package tocraft.craftedcore.events;

import org.apache.commons.lang3.BooleanUtils;

import net.minecraft.world.InteractionResult;

public interface Event<T> {
    T invoker();
    
    void register(T listener);
    
    void unregister(T listener);
    
    boolean isRegistered(T listener);
    
    void clearListeners();
    
    public final class Result {
        private static final Result TRUE = new Result(true, true);
        private static final Result STOP = new Result(true, null);
        private static final Result PASS = new Result(false, null);
        private static final Result FALSE = new Result(true, false);
        
        public static Result pass() {
            return PASS;
        }
        
        public static Result interrupt(Boolean value) {
            if (value == null) return STOP;
            if (value) return TRUE;
            return FALSE;
        }
        
        public static Result interruptTrue() {
            return TRUE;
        }
        
        public static Result interruptDefault() {
            return STOP;
        }
        
        public static Result interruptFalse() {
            return FALSE;
        }
        
        private final boolean interruptsFurtherEvaluation;
        
        private final Boolean value;
        
        Result(boolean interruptsFurtherEvaluation, Boolean value) {
            this.interruptsFurtherEvaluation = interruptsFurtherEvaluation;
            this.value = value;
        }
        
        public boolean interruptsFurtherEvaluation() {
            return interruptsFurtherEvaluation;
        }
        
        public Boolean value() {
            return value;
        }
        
        public boolean isEmpty() {
            return value == null;
        }
        
        public boolean isPresent() {
            return value != null;
        }
        
        public boolean isTrue() {
            return BooleanUtils.isTrue(value);
        }
        
        public boolean isFalse() {
            return BooleanUtils.isFalse(value);
        }
        
        public InteractionResult asMinecraft() {
            if (isPresent()) {
                return value() ? InteractionResult.SUCCESS : InteractionResult.FAIL;
            }
            return InteractionResult.PASS;
        }
    }

}
