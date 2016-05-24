package scheduler;

import java.util.ArrayList;

import transacion.Operation;

public class Scheduler {

	/**
	 * TODO: 1) uma transação T deve garantir a operação readLock(x) ou
	 * writeLock(x) antes de qualquer operação READ(x) ser executada em T 2) uma
	 * transação T deve garantir a operação writeLock(x) antes de qualquer
	 * operação WRITE(x) ser executada em T 3) uma transação T deve garantir a
	 * operação unlock(x) depois que todas as operação READ(x) e WRITE(x) são
	 * completadas em T 4) uma transação T não vai gerar uma operação
	 * readLock(x) se ela já controlar um bloqueio de leitura ou um bloqueio de
	 * escrita no item x. 5) uma transação T não resultará uma operação
	 * writeLock(x) se ela já controlar um bloqueio de leitura ou um bloqueio de
	 * escrita no item x. 6) uma transação T não resultará uma operação
	 * unlock(x) a menos que ela já controle um bloqueio de leitura ou um
	 * bloqueio de escrita em um item x.
	 */
	// TODO: granularidade do bloqueio: objeto(item)

	// TODO: uma vez que uma transação T libere algum bloqueio, T não pode mais
	// obter nenhum bloqueio
	// TODO: o strict 2PL uma transação T não libera nenhum de seus bloqueios de
	// escrita até que ela efetive ou aborte. Portanto,
	// nenhuma outra transação pode ler ou escrever um item que seja ecrito por
	// T, a menos que T tenha efetivado/abortado.

	// TODO: usar serializable

	// TODO: Perguntar ao professor se as operações de readLock e writeLock são
	// colocadas na descrição da transação
	// ou se o Scheduler que gerencia isso: NÃO SÃO!!!!!
	// TODO: pegar uma operação de T1 e colocar no scheduler depois uma operação
	// de T2 e assim sucessivamente (respeitando o protocolo) ? NÃOOOO!

	private ArrayList<Operation> schedule;
	private LockManager lockManager;

	public Scheduler() {
		this.schedule = new ArrayList<Operation>();
		this.lockManager = new LockManager();
	}

	public void schedule(Operation op) {
		/**
		 * If the operation transaction is already on a waiting list, DO NOT do
		 * anything with it
		 */
		System.out.println("verifica se a transação " + op.getTransaction().getId() + " está esperando algo");
		if (lockManager.isOnWaitingList(op.getTransaction())) {
			return;
		}

		switch (op.getType()) {
		case READ:
			System.out.println("request a read lock for the item " + op.getOperationItem().getName());
			if (lockManager.readLock(op.getTransaction(), op.getOperationItem())) {
				getSchedule().add(op);
			}
			break;
		case WRITE:
			System.out.println("request a write lock for the item " + op.getOperationItem().getName());
			if (lockManager.writeLock(op.getTransaction(), op.getOperationItem())) {
				getSchedule().add(op);
			}
			break;
		case ABORT:
		case COMMIT:
			System.out.println("release all the locks of the transaction  " + op.getTransaction().getId());
			this.lockManager.unlockAllByTransaction(op.getTransaction());
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
