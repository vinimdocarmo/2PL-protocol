package scheduler;

public class DeadlockManager {

	public enum PreventionType {WAIT_DIE, WOUND_WAIT};
	
	private PreventionType preventionType;
	
	public DeadlockManager(final PreventionType preventinType) {
		this.preventionType = preventinType;
	}
	
}
