package frontend;


import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import executer.Controller;
import scheduler.Scheduler;
import transacion.Operation;
import transacion.Reader;
import transacion.Transaction;
import transacion.TransactionRandomizer;
import transacion.TransactionSet;

public class WindowThread extends Thread {

	public void run() {
		try {
			/**
			 * Load all the transaction into the memory. This way is easier to
			 * play around with these transactions.
			 */
			TransactionSet transactions = null;

			try {
				transactions = Reader.readTransactionFiles();
				
				for (Transaction transaction : transactions) {
					Controller.resultsWindow.insertIntoTransactionTable(transaction.getId(), transaction.getOperations().toString());
				}
				
			} catch (IOException e) {
				java.util.logging.Logger.getLogger(ResultsWindow.class.getName()).log(java.util.logging.Level.SEVERE,
						null, e);
			}

			/**
			 * Create the scheduler that implements the strict 2PL protocol
			 */
			Scheduler scheduler = new Scheduler();

			/**
			 * Create a randomizer to request transactions randomly
			 */
			TransactionRandomizer randomizer = new TransactionRandomizer(transactions);

			while (transactions.size() > 0) {
				int interval = 1; // TODO: pegar o intervalo setado na interface

				if (interval > 0) {
					Thread.sleep(interval * 1000);
				}

				Transaction chosenTransaction = randomizer.getRandomTransaction();
				
				Controller.resultsWindow.insertIntoRandomizerTable(chosenTransaction.getId());

				/**
				 * If the transaction has no more operations, remove it
				 */
				if (chosenTransaction.getOperations().isEmpty()) {
					transactions.remove(chosenTransaction);
					continue;
				}

				Operation nextOperation = chosenTransaction.getOperations().get(0);

				/**
				 * If it is possible to schedule the operation, remove it from the chosen transaction operations list
				 */
				if (scheduler.schedule(nextOperation)) {
					chosenTransaction.getOperations().remove(0);
				}
			}

			System.out.println(scheduler.toString());

			scheduler.getSchedule().clear();
		} catch (InterruptedException ex) {
			Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

}
