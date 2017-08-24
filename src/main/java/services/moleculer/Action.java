package services.moleculer;

@FunctionalInterface
public interface Action {
	
	Object handler(Context ctx) throws Exception;

	// --- GETTERS ---
	
	public default String name() {
		return null;
	}
	
	public default String nodeID() {
		return null;
	}
	
	public default boolean local() {
		return true;
	}
	
}