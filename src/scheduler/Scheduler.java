package scheduler;

import java.util.ArrayList;

import executer.Controller;
import transacion.Operation;

public class Scheduler {

	private ArrayList<Operation> schedule;
	private LockManager lockManager;
	private DeadlockManager deadlockManager;

	public Scheduler() {
		this.schedule = new ArrayList<Operation>();
		this.lockManager = new LockManager();

		/**
		 * Initialize a deadlock manager with the strategy set on the interface
		 */
		DeadlockManager.PreventionType strategy = null;

		if (Controller.strategy == 0) {
			strategy = DeadlockManager.PreventionType.WAIT_DIE;
		} else if (Controller.strategy == 1) {
			strategy = DeadlockManager.PreventionType.WOUND_WAIT;
		}

		this.deadlockManager = new DeadlockManager(strategy);
	}

	public boolean schedule(final Operation op) {

		/**
		 * If the operation transaction is already on a waiting list, DO NOT do
		 * anything with it
		 */
		if (lockManager.isOnWaitingList(op.getTransaction())) {
			return false;
		}

		if (op.getType() == Operation.Type.READ) {
			if (lockManager.readLock(op.getTransaction(), op.getOperationItem())) {
				getSchedule().add(op);
				Controller.resultsWindow.insertIntoSchedulerTable(op.getTransaction().getId(), op.toString());
			}
		} else if (op.getType() == Operation.Type.WRITE) {
			if (lockManager.writeLock(op.getTransaction(), op.getOperationItem())) {
				getSchedule().add(op);
				Controller.resultsWindow.insertIntoSchedulerTable(op.getTransaction().getId(), op.toString());
			}
		} else if (op.getType() == Operation.Type.COMMIT || op.getType() == Operation.Type.ABORT) {
			this.lockManager.unlockAllByTransaction(op.getTransaction());
			getSchedule().add(op);
			Controller.resultsWindow.insertIntoSchedulerTable(op.getTransaction().getId(), op.toString());
		}
		return true;

	}

	@Override
	public String toString() {
		String schedule = "S =";

		for (Operation operation : this.getSchedule()) {
			schedule += " " + operation.toString() + "_" + operation.getTransaction().getId();
		}

		return schedule;
	}

	public ArrayList<Operation> getSchedule() {
		return schedule;
	}

}
