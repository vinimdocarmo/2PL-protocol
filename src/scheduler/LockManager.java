package scheduler;

import java.util.ArrayList;
import java.util.LinkedList;

import scheduler.DeadlockManager.PreventionType;
import scheduler.DeadlockManager.StrategyType;
import transacion.Operation;
import transacion.Transaction;
import transacion.Operation.OperationItem;
import executer.Controller;

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
	private DeadlockManager deadlockManager;

	public LockManager() {
		this.locks = new ArrayList<Lock>();
		
		/**
		 * Initialize a deadlock manager with the strategy set on the interface
		 */
		if (Controller.strategy == 0) {
			this.deadlockManager = new DeadlockManager(StrategyType.DETECTION, null);
		} else if (Controller.strategy == 1) {
			if (Controller.deadlockDetectionType == 0) {
				this.deadlockManager = new DeadlockManager(StrategyType.PREVENTION, PreventionType.WOUND_WAIT);
			} else {
				this.deadlockManager = new DeadlockManager(StrategyType.PREVENTION, PreventionType.WAIT_DIE);
			}
		}
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
	
	public void readLock(final Operation operation) {
		Transaction requestingTransaction = operation.getTransaction();
		OperationItem item = operation.getItem();

		Lock lock = getLockByItem(item);
		
		if (lock == null) {
			int numberOfReads = 1;
			Lock newReadLock = new Lock(requestingTransaction, LockType.READ, item, numberOfReads);
			this.locks.add(newReadLock);
			
			Controller.resultsWindow.insertIntoBlockTable(item.getName(), LockType.READ.toString(), requestingTransaction.getId());
			Controller.scheduler.addOperation(operation);
			
			return;
		} 
		else if (lock.type.equals(LockType.UNLOCKED)) {
			lock.type = LockType.READ;
			lock.blockingTransactions.add(requestingTransaction);
			lock.numberOfReads++;
			
			Controller.resultsWindow.insertIntoBlockTable(item.getName(), LockType.READ.toString(), requestingTransaction.getId());
			Controller.scheduler.addOperation(operation);
			
			return;
		}
		
		/**
		 * If the transaction already has a read block for these item, do nothing
		 */
		for (Transaction transaction : lock.blockingTransactions) {
			if (transaction.equals(requestingTransaction) && lock.type.equals(LockType.READ)) {
				return;
			}
		}
		
		if (lock.type.equals(LockType.READ)) {
			lock.numberOfReads++;
			lock.blockingTransactions.add(requestingTransaction);
			
			Controller.resultsWindow.insertIntoBlockTable(item.getName(), LockType.READ.toString(), requestingTransaction.getId());
			Controller.scheduler.addOperation(operation);
		} else if (lock.type.equals(LockType.WRITE)) {
			ArrayList<Integer> ids = getBlockingTransactionsIds(lock);
			
			Controller.resultsWindow.insertIntoQueue("ADD", requestingTransaction.getId(), operation.toString(), ids.toString());
			lock.waitingTrasactions.add(new TransactionLockTypePair(requestingTransaction, LockType.READ));
			
			for (Transaction transaction : lock.blockingTransactions) {
				addEdgeIntoGraph(requestingTransaction, transaction);
				if (deadlockManager.graphIsCyclic()) {
					Transaction transactionCandidate = null;
					if (deadlockManager.strategyType == StrategyType.DETECTION) {
						transactionCandidate = transaction;
					} else if (deadlockManager.strategyType == StrategyType.PREVENTION) {
						if (deadlockManager.preventionType == PreventionType.WOUND_WAIT) {
							if (requestingTransaction.getTimestamp().before(transaction.getTimestamp())) {
								continue;
							} else {
								transactionCandidate = requestingTransaction;
							}
						} else if (deadlockManager.preventionType == PreventionType.WAIT_DIE) {
							if (requestingTransaction.getTimestamp().before(transaction.getTimestamp())) {
								transactionCandidate = requestingTransaction;
							} else {
								continue;
							}
						}
					}
					
					for (Lock currLock : locks) {
						currLock.waitingTrasactions.remove(transactionCandidate);
						for (TransactionLockTypePair transactionLockTypePair : currLock.waitingTrasactions) {
							if (transactionLockTypePair.transaction == transactionCandidate) {
								currLock.waitingTrasactions.remove(transactionLockTypePair);
							}
						}
					}
					
					Controller.scheduler.schedule(new Operation(Operation.Type.ABORT, lock.item, transactionCandidate), true);
				}
			}
		}
	}
	
	public void writeLock(final Operation operation) {
		
		final Transaction requestingTransaction = operation.getTransaction();
		final OperationItem item = operation.getItem();
		
		Lock lock = getLockByItem(item);
		
		/**
		 * If the block is unlock or does not exist, create a new one
		 */
		if (lock == null) {
			Lock newWriteLock = new Lock(requestingTransaction, LockType.WRITE, item);
			this.locks.add(newWriteLock);
			
			Controller.resultsWindow.insertIntoBlockTable(item.getName(), LockType.WRITE.toString(), requestingTransaction.getId());
			Controller.scheduler.addOperation(operation);
			
			return;
		} else if (lock.type.equals(LockType.UNLOCKED)) {
			lock.type = LockType.WRITE;
			lock.blockingTransactions.add(requestingTransaction);
			
			Controller.resultsWindow.insertIntoBlockTable(item.getName(), LockType.WRITE.toString(), requestingTransaction.getId());
			Controller.scheduler.addOperation(operation);
			
			return;
		}
		
		/**
		 * If the transaction already has the block for these item, do nothing
		 */
		for (Transaction transaction : lock.blockingTransactions) {
			if (transaction.equals(requestingTransaction) && lock.type.equals(LockType.WRITE)) {
				return;
			}
		}
		
		lock.waitingTrasactions.add(new TransactionLockTypePair(requestingTransaction, LockType.WRITE));
		
		for (Transaction transaction : lock.blockingTransactions) {
			addEdgeIntoGraph(requestingTransaction, transaction);
			if (deadlockManager.graphIsCyclic()) {
				Transaction transactionCandidate = null;
				if (deadlockManager.strategyType == StrategyType.DETECTION) {
					transactionCandidate = transaction;
				} else if (deadlockManager.strategyType == StrategyType.PREVENTION) {
					if (deadlockManager.preventionType == PreventionType.WOUND_WAIT) {
						if (requestingTransaction.getTimestamp().before(transaction.getTimestamp())) {
							continue;
						} else {
							transactionCandidate = requestingTransaction;
						}
					} else if (deadlockManager.preventionType == PreventionType.WAIT_DIE) {
						if (requestingTransaction.getTimestamp().before(transaction.getTimestamp())) {
							transactionCandidate = requestingTransaction;
						} else {
							continue;
						}
					}
				}
				
				for (Lock currLock : locks) {
					currLock.waitingTrasactions.remove(transactionCandidate);
					for (TransactionLockTypePair transactionLockTypePair : currLock.waitingTrasactions) {
						if (transactionLockTypePair.transaction == transactionCandidate) {
							currLock.waitingTrasactions.remove(transactionLockTypePair);
						}
					}
				}
				
				Controller.scheduler.schedule(new Operation(Operation.Type.ABORT, lock.item, transactionCandidate), true);
			}
		}
		
		ArrayList<Integer> ids = getBlockingTransactionsIds(lock);
		
		Controller.resultsWindow.insertIntoQueue("ADD", requestingTransaction.getId(), operation.toString(), ids.toString());
	}
	
	public void unlockAllByTransactionOperation(final Operation operation) {
		/**
		 * TODO:
		 * 	rever este m�todo. Comportamento bizarro. As vezes de 3 bloqueios ele só libera o primeiro e o ultimo.
		 */
		Controller.scheduler.addOperation(operation);
		
		Transaction transaction = operation.getTransaction();
		
		for (int i = 0; i < this.locks.size(); i++) {
			Lock lock = this.locks.get(i);
			
			if (lock.type.equals(LockType.UNLOCKED)) {
				continue;
			}
			
			if (lock.blockingTransactions.contains(transaction)) {
				this.unlock(lock, transaction);
			}
		}
	}

	private void unlock(final Lock lock, final Transaction blockingTrasaction) {
		lock.blockingTransactions.remove(blockingTrasaction);

		Controller.resultsWindow.insertIntoBlockTable(lock.item.getName(), LockType.UNLOCKED.toString() + " - " + lock.type.toString(), blockingTrasaction.getId());
		
		if (!lock.blockingTransactions.isEmpty()) {
			return;
		}
		
		if (lock.type.equals(LockType.WRITE)) {
			lock.type = LockType.UNLOCKED;
			
			TransactionLockTypePair transactionLockTypePair = lock.waitingTrasactions.poll();
			
			if (transactionLockTypePair == null) {
				return;
			}
			
			Transaction nextTransaction = transactionLockTypePair.transaction;
			
			removeEdgeFromGraph(nextTransaction, blockingTrasaction);
			

			if (transactionLockTypePair.lockType.equals(LockType.READ)) {
				Controller.resultsWindow.insertIntoQueue("ADD", nextTransaction.getId(), LockType.READ.toString(), ((Integer) blockingTrasaction.getId()).toString());
				readLock(new Operation(Operation.Type.READ, lock.item, nextTransaction));
			} else if (transactionLockTypePair.lockType.equals(LockType.WRITE)) {
				Controller.resultsWindow.insertIntoQueue("POP", nextTransaction.getId(), LockType.WRITE.toString(), ((Integer) blockingTrasaction.getId()).toString());
				writeLock(new Operation(Operation.Type.WRITE, lock.item, nextTransaction));
			}
		} else if (lock.type.equals(LockType.READ)) {
			lock.numberOfReads--;
			
			if (lock.numberOfReads == 0) {
				lock.type = LockType.UNLOCKED;
				
				TransactionLockTypePair transactionLockTypePair = lock.waitingTrasactions.poll();
				
				if (transactionLockTypePair == null) {
					return;
				}
				
				Transaction nextTransaction = transactionLockTypePair.transaction;
				
				
				if (transactionLockTypePair.lockType.equals(LockType.READ)) {
					Controller.resultsWindow.insertIntoQueue("POP", nextTransaction.getId(), LockType.READ.toString(), ((Integer) blockingTrasaction.getId()).toString());
					readLock(new Operation(Operation.Type.READ, lock.item, nextTransaction));
				} else if (transactionLockTypePair.lockType.equals(LockType.WRITE)) {
					Controller.resultsWindow.insertIntoQueue("REMOVE", nextTransaction.getId(), LockType.WRITE.toString(), ((Integer) blockingTrasaction.getId()).toString());
					writeLock(new Operation(Operation.Type.WRITE, lock.item, nextTransaction));
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
	
	private void addEdgeIntoGraph(Transaction source, Transaction target) {
		deadlockManager.addEdgeIntoGraph(source.getId(), target.getId());
		Controller.resultsWindow.insertIntoGraphTable("ADD", source.getId(), target.getId());
	}
	
	private void removeEdgeFromGraph(Transaction source, Transaction target) {
		deadlockManager.removeEdgeFromGraph(source.getId(), target.getId());
		Controller.resultsWindow.insertIntoGraphTable("REMOVE", source.getId(), target.getId());
	}
	
	private ArrayList<Integer> getBlockingTransactionsIds(final Lock lock) {
		ArrayList<Integer> ids = new ArrayList<Integer>();
		
		for (Transaction transaction : lock.blockingTransactions) {
			ids.add((Integer) transaction.getId());
		}
		
		return ids;
	}
}
