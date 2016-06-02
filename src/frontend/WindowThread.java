package frontend;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JOptionPane;

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
				System.out.println("Ler todas as transações");
				transactions = Reader.readTransactionFiles();
			} catch (IOException e) {
				java.util.logging.Logger.getLogger(ResultsWindow.class.getName()).log(java.util.logging.Level.SEVERE,
						null, e);
			}

			/**
			 * Create the scheduler that implements the strict 2PL protocol
			 */
			Scheduler scheduler = new Scheduler();
			
			/**
			 * Set scheduler into the controller
			 */
			Controller.scheduler = scheduler;

			/**
			 * Create a randomizer to request transactions randomly
			 */
			TransactionRandomizer randomizer = new TransactionRandomizer(transactions);

			ArrayList<Transaction> alreadyChosenTransacions = new ArrayList<Transaction>();

			while (transactions.size() > 0) {
				
				while (Controller.paused.get()) {
					Thread.sleep(500);
				}

				Transaction chosenTransaction = randomizer.getRandomTransaction();
				
				/**
				 * If the transaction has no more operations, remove it
				 */
				if (chosenTransaction.getOperations().isEmpty()) {
					transactions.remove(chosenTransaction);
					continue;
				}

				Controller.resultsWindow.insertIntoRandomizerTable(chosenTransaction.getId());

				if (!alreadyChosenTransacions.contains(chosenTransaction)) {
					chosenTransaction.setTimestamp(new Date());

					alreadyChosenTransacions.add(chosenTransaction);

					Controller.resultsWindow.insertIntoTransactionTable(chosenTransaction.getId(),
							chosenTransaction.getOperations().toString(), chosenTransaction.getTimestamp());
				}

				Operation nextOperation = chosenTransaction.getOperations().get(0);

				/**
				 * If it is possible to schedule the operation, remove it from
				 * the chosen transaction operations list
				 */
				if (scheduler.schedule(nextOperation)) {
					chosenTransaction.getOperations().remove(0);
					Controller.paused.set(true);
				}
			}
			
			JOptionPane.showMessageDialog(null, "Schedule done!");
			
			System.out.println(scheduler.toString());

			scheduler.getSchedule().clear();
		} catch (InterruptedException ex) {
			Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

}
