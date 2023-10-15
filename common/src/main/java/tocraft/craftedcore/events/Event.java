package tocraft.craftedcore.events;

public interface Event<T> {
    T invoker();
    
    void register(T listener);
    
    void unregister(T listener);
    
    boolean isRegistered(T listener);
    
    void clearListeners();
    
    public class Result {
    	/**
    	 * Continue executing this event
    	 */
        private static final Result PASS = new Result(false);
        /**
         * Stop the event without specific result
         */
        private static final Result STOP = new Result(true);

        
        private final boolean interruptsFurtherEvaluation;
        
        Result(boolean interruptsFurtherEvaluation) {
            this.interruptsFurtherEvaluation = interruptsFurtherEvaluation;
        }
        
        public static Result pass() {
            return PASS;
        }
        
    	public static Result interrupt() {
            return STOP;
        }

		public boolean interruptsFurtherEvaluation() {
			return interruptsFurtherEvaluation;
		}
    }
}
