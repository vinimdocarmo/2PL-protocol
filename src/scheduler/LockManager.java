package scheduler;

import java.util.ArrayList;
import java.util.LinkedList;

import executer.Controller;
import transacion.Operation;
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
		WRITE, READ, UNLOCKED
	};
	
	class TransactionLockTypePair {
		private Transaction transaction;
		private LockType lockType;
		
		public TransactionLockTypePair(final Transaction transaction, final LockType lockType) {
			this.transaction = transaction;
			this.lockType = lockType;
		}
	}
	
	class Lock {

		private ArrayList<Transaction> blockingTransactions = new ArrayList<Transaction>();
		private OperationItem item;
		private LockType type;
		private int numberOfReads = 0;
		//TODO: ta certo isso daqui ??????
		private LinkedList<TransactionLockTypePair> waitingTrasactions = new LinkedList<TransactionLockTypePair>();

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
			for (TransactionLockTypePair transactionLockPair : lock.waitingTrasactions) {
				if (transactionLockPair.transaction.equals(transaction)) {
					return true;
				}
			}
		}
		return false;
	}
	
	public boolean readLock(final Transaction requestingTransaction, final OperationItem item) {
		Lock lock = getLockByItem(item);
		
		/**
		 * If the block is unlock or does not exist, create a new one
		 */
		if (lock == null) {
			int numberOfReads = 1;
			Lock newReadLock = new Lock(requestingTransaction, LockType.READ, item, numberOfReads);
			this.locks.add(newReadLock);
			
			Controller.resultsWindow.insertIntoBlockTable(item.getName(), LockType.READ.toString(), requestingTransaction.getId());
			
			return true;
		} else if (lock.type.equals(LockType.UNLOCKED)) {
			lock.type = LockType.READ;
			lock.blockingTransactions.add(requestingTransaction);
			lock.numberOfReads++;
			
			Controller.resultsWindow.insertIntoBlockTable(item.getName(), LockType.READ.toString(), requestingTransaction.getId());
			
			return true;
		}
		
		/**
		 * If the transaction already has the block for these item, do nothing
		 */
		if (lock.blockingTransactions.contains(requestingTransaction)) {
			return true;
		}
		
		if (lock.type.equals(LockType.READ)) {
			lock.numberOfReads++;
			lock.blockingTransactions.add(requestingTransaction);
			
			Controller.resultsWindow.insertIntoBlockTable(item.getName(), LockType.READ.toString(), requestingTransaction.getId());
			
			return true;
		} else if (lock.type.equals(LockType.WRITE)) {
			lock.waitingTrasactions.add(new TransactionLockTypePair(requestingTransaction, LockType.READ));
			
			return false;
		}
		
		return false;
	}
	
	public boolean writeLock(final Transaction requestingTransaction, final OperationItem item) {
		Lock lock = getLockByItem(item);
		
		/**
		 * If the block is unlock or does not exist, create a new one
		 */
		if (lock == null) {
			Lock newWriteLock = new Lock(requestingTransaction, LockType.WRITE, item);
			this.locks.add(newWriteLock);
			
			Controller.resultsWindow.insertIntoBlockTable(item.getName(), LockType.WRITE.toString(), requestingTransaction.getId());
			
			return true;
		} else if (lock.type.equals(LockType.UNLOCKED)) {
			lock.type = LockType.WRITE;
			lock.blockingTransactions.add(requestingTransaction);
			
			Controller.resultsWindow.insertIntoBlockTable(item.getName(), LockType.WRITE.toString(), requestingTransaction.getId());
			
			return true;
		}
		
		/**
		 * If the transaction already has the block for these item, do nothing
		 */
		if (lock.blockingTransactions.contains(requestingTransaction)) {
			return true;
		}
		
		lock.waitingTrasactions.add(new TransactionLockTypePair(requestingTransaction, LockType.WRITE));
		
		return false;
	}
	
	public void unlockAllByTransaction(final Transaction transaction) {
		for (int i = 0; i < this.locks.size(); i++) {
			Lock lock = this.locks.get(i);
			if (lock.blockingTransactions.contains(transaction)) {
				this.unlock(lock, transaction);
			}
		}
	}

	private void unlock(final Lock lock, final Transaction blockingTrasaction) {
		lock.blockingTransactions.remove(blockingTrasaction);

		Controller.resultsWindow.insertIntoBlockTable(lock.item.getName(), LockType.UNLOCKED.toString() + " - " + lock.type.toString(), blockingTrasaction.getId());
		
		if (lock.type.equals(LockType.WRITE)) {
			lock.type = LockType.UNLOCKED;
			
			TransactionLockTypePair transactionLockTypePair = lock.waitingTrasactions.poll();
			
			if (transactionLockTypePair == null) {
				this.locks.remove(lock);
				return;
			}
			
			Transaction nextTransaction = transactionLockTypePair.transaction;
			
			
			/**
			 * TODO: BELEZA, mas aqui eu tenho que adicionar a operação no scheduler
			 */
			Controller.resultsWindow.insertIntoSchedulerTable(1, "preciso adicionar no schule");
			if (transactionLockTypePair.lockType.equals(LockType.READ)) {
				readLock(nextTransaction, lock.item);
			} else if (transactionLockTypePair.lockType.equals(LockType.WRITE)) {
				writeLock(nextTransaction, lock.item);
			}
		} else if (lock.type.equals(LockType.READ)) {
			lock.numberOfReads--;
			
			if (lock.numberOfReads == 0) {
				lock.type = LockType.UNLOCKED;
				
				TransactionLockTypePair transactionLockTypePair = lock.waitingTrasactions.poll();
				
				if (transactionLockTypePair == null) {
					this.locks.remove(lock);
					return;
				}
				
				Transaction nextTransaction = transactionLockTypePair.transaction;
				
				/**
				 * TODO: BELEZA, mas aqui eu tenho que adicionar a operação no scheduler
				 */
				Controller.resultsWindow.insertIntoSchedulerTable(2, "preciso adicionar no schule");
				if (transactionLockTypePair.lockType.equals(LockType.READ)) {
					readLock(nextTransaction, lock.item);
				} else if (transactionLockTypePair.lockType.equals(LockType.WRITE)) {
					writeLock(nextTransaction, lock.item);
				}
			}
		}
	}

	private Lock getLockByItem(OperationItem item) {
		for (Lock lock : locks) {
			if (lock.item.getName().equals(item.getName())) {
				return lock;
			}
		}

		return null;
	}
}
