import java.io.IOException;

import scheduler.Scheduler;
import transacion.*;

public class Executer {
	public static void main(String[] args) throws IOException {
		System.out.println("Trabalho - Protocolo 2PL");

		/**
		 * Load all the transaction into the memory. This way is easier to play
		 * around with these transactions.
		 */
		TransactionSet transactions = Reader.readTransactionFiles();

		/**
		 * Create the scheduler that implements the strict 2PL protocol
		 */
		Scheduler scheduler = new Scheduler();

		/**
		 * Create a randomizer to request transactions randomly
		 */
		TransactionRandomizer randomizer = new TransactionRandomizer(transactions);

		while (transactions.size() > 0) {
			Transaction chosenTransaction = randomizer.getRandomTransaction();

			/**
			 * If the transaction has no more operations, remove it
			 */
			if (chosenTransaction.getOperations().isEmpty()) {
				transactions.remove(chosenTransaction);
				continue;
			}

			Operation nextOperation = chosenTransaction.getOperations().remove();

			scheduler.schedule(nextOperation);
		}

		System.out.println(scheduler.toString());

		scheduler.getSchedule().clear();
	}
}
