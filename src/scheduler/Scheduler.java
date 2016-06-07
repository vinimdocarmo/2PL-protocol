package scheduler;

import java.util.ArrayList;

import transacion.Operation;
import executer.Controller;

public class Scheduler {

	private ArrayList<Operation> schedule;
	private LockManager lockManager;

	public Scheduler() {
		this.schedule = new ArrayList<Operation>();
		this.lockManager = new LockManager();
	}

	public boolean schedule(final Operation op, final boolean forceAbort) {
		
		if (forceAbort) {
			//TODO: quando aborta, remove transação do conjunto de transações
			Operation newOp = new Operation(Operation.Type.ABORT, null, op.getTransaction()); 
			this.lockManager.unlockAllByTransactionOperation(newOp);
			Controller.transactionSet.remove(op.getTransaction());
			return true;
		}

		/**
		 * If the operation transaction is already on a waiting list, DO NOT do
		 * anything with it
		 */
		if (lockManager.isOnWaitingList(op.getTransaction())) {
			return false;
		}
		
		if (op.getType() == Operation.Type.READ) {
			lockManager.readLock(op);
		} else if (op.getType() == Operation.Type.WRITE) {
			lockManager.writeLock(op);
		} else if (op.getType() == Operation.Type.COMMIT || op.getType() == Operation.Type.ABORT) {
			this.lockManager.unlockAllByTransactionOperation(op);
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
	
	public void addOperation(final Operation op) {
		getSchedule().add(op);
		Controller.resultsWindow.insertIntoSchedulerTable(op.getTransaction().getId(), op.toString());
	}

	public ArrayList<Operation> getSchedule() {
		return schedule;
	}

}
