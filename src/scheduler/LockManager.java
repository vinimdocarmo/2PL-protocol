package scheduler;

import java.util.ArrayList;
import java.util.LinkedList;

import transacion.Operation.OperationItem;
import transacion.Transaction;

public class LockManager {
	/**
	 * 
	 * Gerenciador de bloqueios Executa as operações de bloqueios e liberações
	 * Gerencia duas estruturas de dados Arquivo de bloqueios Contém informações
	 * sobre os bloqueios <transação, tipo de bloqueio, operação> Lista de
	 * espera Contém os dados das transações bloqueadas
	 */

	public enum LockType {
		WRITE, READ, UNLOCK
	};

	class Lock {

		private ArrayList<Transaction> blockingTransactions = new ArrayList<Transaction>();
		private OperationItem item;
		private LockType type;
		private int numberOfReads = 0;
		private LinkedList<Transaction> waitingTrasactions = new LinkedList<Transaction>();

		public Lock(final Transaction transaction, final LockType lockType, final OperationItem item, final int numberOfReads) {
			this.blockingTransactions.add(transaction);
			this.type = lockType;
			this.item = item;
			this.numberOfReads = numberOfReads;
		}
		
		public Lock(final Transaction transaction, final LockType lockType, final OperationItem item) {
			this.blockingTransactions.add(transaction);
			this.type = lockType;
			this.item = item;
		}
	}

	private ArrayList<Lock> locks;

	public LockManager() {
		this.locks = new ArrayList<Lock>();
	}

	public ArrayList<Lock> getLocks() {
		return this.locks;
	}
	
	/**
	 * Returns whether a transaction is waiting for a specific lock
	 * 
	 * @param transaction
	 * @return
	 */
	public boolean isOnWaitingList(Transaction transaction) {
		for (Lock lock : this.locks) {
			if (lock.waitingTrasactions == null) {
				System.out.println("Com a fila nula é foda né");
			}
			if (lock.waitingTrasactions.contains(transaction)) {
				return true;
			}
		}
		return false;
	}
	
	public boolean readLock(final Transaction requestingTransaction, final OperationItem item) {
		Lock lock = getLockByItem(item);
		
		if (lock == null || lock.type.equals(LockType.UNLOCK)) {
			int numberOfReads = 1;
			Lock newReadLock = new Lock(requestingTransaction, LockType.READ, item, numberOfReads);
			this.locks.add(newReadLock);
			
			return true;
		} else if (lock.type.equals(LockType.READ)) {
			lock.numberOfReads++;
			
			return true;
		} else if (lock.type.equals(LockType.WRITE)) {
			lock.waitingTrasactions.add(requestingTransaction);
			
			return false;
		}
		
		return false;
	}
	
	public boolean writeLock(final Transaction requestingTransaction, final OperationItem item) {
		Lock lock = getLockByItem(item);
		
		if (lock == null || lock.type.equals(LockType.UNLOCK)) {
			Lock newWriteLock = new Lock(requestingTransaction, LockType.WRITE, item);
			this.locks.add(newWriteLock);
			
			return true;
		}
		
		lock.waitingTrasactions.add(requestingTransaction);
		
		return false;
	}
	
	public void unlockAllByTransaction(final Transaction transaction) {
		for (int i = 0; i < this.locks.size(); i++) {
			Lock lock = this.locks.get(i);
			if (lock.blockingTransactions.contains(transaction)) {
				this.unlock(lock);
			}
		}
	}

	private void unlock(final Lock lock) {
		if (lock.type.equals(LockType.WRITE)) {
			lock.type = LockType.UNLOCK;
			
			Transaction nextTransaction = lock.waitingTrasactions.poll();
			
			if (nextTransaction == null) {
				this.locks.remove(lock);
				return;
			}
			
			this.writeLock(nextTransaction, lock.item);
		} else if (lock.type.equals(LockType.READ)) {
			lock.numberOfReads--;
			
			if (lock.numberOfReads == 0) {
				lock.type = LockType.UNLOCK;
				
				Transaction nextTransaction = lock.waitingTrasactions.poll();
				
				if (nextTransaction == null) {
					this.locks.remove(lock);
					return;
				}
				
				readLock(nextTransaction, lock.item);
			}
		}
	}

	private Lock getLockByItem(OperationItem item) {
		for (Lock lock : locks) {
			if (lock.item.equals(item)) {
				return lock;
			}
		}

		return null;
	}
}
