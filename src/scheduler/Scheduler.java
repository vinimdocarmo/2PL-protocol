package scheduler;

import java.util.ArrayList;

import executer.Controller;
import transacion.Operation;

public class Scheduler {

	/**
	 * DONE: 1) uma transação T deve garantir a operação readLock(x) ou
	 * writeLock(x) antes de qualquer operação READ(x) ser executada em T DONE:
	 * 2) uma transação T deve garantir a operação writeLock(x) antes de
	 * qualquer operação WRITE(x) ser executada em T DONE: 3) uma transação T
	 * deve garantir a operação unlock(x) depois que todas as operação READ(x) e
	 * WRITE(x) são completadas em T DONE: 4) uma transação T não vai gerar uma
	 * operação readLock(x) se ela já controlar um bloqueio de leitura ou um
	 * bloqueio de escrita no item x. DONE: 5) uma transação T não resultará uma
	 * operação writeLock(x) se ela já controlar um bloqueio de leitura ou um
	 * bloqueio de escrita no item x. DONE: 6) uma transação T não resultará uma
	 * operação unlock(x) a menos que ela já controle um bloqueio de leitura ou
	 * um bloqueio de escrita em um item x.
	 */

	// TODO: usar serializable!

	private ArrayList<Operation> schedule;
	private LockManager lockManager;
	private DeadlockManager deadlockManager;

	public Scheduler() {
		this.schedule = new ArrayList<Operation>();
		this.lockManager = new LockManager();
		// TODO: mudar o tipo de prevensão de acordo com a interface
		this.deadlockManager = new DeadlockManager(DeadlockManager.PreventionType.WAIT_DIE);
	}

	public void schedule(final Operation op) {
		/**
		 * If the operation transaction is already on a waiting list, DO NOT do
		 * anything with it
		 */
		if (lockManager.isOnWaitingList(op.getTransaction())) {
			System.out.println("T " + op.getTransaction().getId() + " está na fila de espera");
			return;
		}

		switch (op.getType()) {
		case READ:
			System.out.println("T " + op.getTransaction().getId() + " está pedindo um bloqueio de leitura para "
					+ op.getOperationItem().getName());
			if (lockManager.readLock(op.getTransaction(), op.getOperationItem())) {
				System.out.println("\tconseguiu o bloqueio");
				getSchedule().add(op);
				Controller.resultsWindow.insertIntoSchedulerTale(op.getTransaction().getId(), op.toString());
			} else
				System.out.println("\tnão conseguiu o bloqueio");
			break;
		case WRITE:
			System.out.println("T " + op.getTransaction().getId() + " está pedindo um bloqueio de escrita para "
					+ op.getOperationItem().getName());
			if (lockManager.writeLock(op.getTransaction(), op.getOperationItem())) {
				System.out.println("\tconseguiu o bloqueio");
				getSchedule().add(op);
				Controller.resultsWindow.insertIntoSchedulerTale(op.getTransaction().getId(), op.toString());
			} else
				System.out.println("\tnão conseguiu o bloqueio");
			break;
		case ABORT:
		case COMMIT:
			System.out.println("T " + op.getTransaction().getId() + " terminou. Liberar todos os bloqueios!");
			this.lockManager.unlockAllByTransaction(op.getTransaction());
			getSchedule().add(op);
			Controller.resultsWindow.insertIntoSchedulerTale(op.getTransaction().getId(), op.toString());
			break;
		}

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
