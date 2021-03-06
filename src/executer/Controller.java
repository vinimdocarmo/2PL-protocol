package executer;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

import frontend.ParamsWindow;
import frontend.ResultsWindow;
import frontend.WindowThread;
import scheduler.Scheduler;
import transacion.Transaction;
import transacion.TransactionSet;

public class Controller {
	
	/**
	 * TODO: 
	 * 1) Prevenção de deadlock
	 * 2) Detecção de deadlock
	 * 3) Melhorar randomização:
	 * 	3.1) Escolher um melhor algoritmo.
	 * 	3.2) Tirar a transação que ta esperando bloqueio da lista de randomização
	 * 4) Ver a questão dos bloquios (?)
	 */

	public static AtomicBoolean paused = new AtomicBoolean(true);

	public static ParamsWindow paramsWindow = null;
	public static ResultsWindow resultsWindow = null;

	public static Scheduler scheduler = null;
	
	public static TransactionSet transactionSet;

	public static String path = null;
	public static int strategy = -1;
	public static int interval = -1;

	public static int deadlockDetectionType;

	public static void main(String[] args) throws IOException {
		try {
			for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					javax.swing.UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (ClassNotFoundException ex) {
			java.util.logging.Logger.getLogger(ResultsWindow.class.getName()).log(java.util.logging.Level.SEVERE, null,
					ex);
		} catch (InstantiationException ex) {
			java.util.logging.Logger.getLogger(ResultsWindow.class.getName()).log(java.util.logging.Level.SEVERE, null,
					ex);
		} catch (IllegalAccessException ex) {
			java.util.logging.Logger.getLogger(ResultsWindow.class.getName()).log(java.util.logging.Level.SEVERE, null,
					ex);
		} catch (javax.swing.UnsupportedLookAndFeelException ex) {
			java.util.logging.Logger.getLogger(ResultsWindow.class.getName()).log(java.util.logging.Level.SEVERE, null,
					ex);
		}

		Runnable execute = new Runnable() {
			public void run() {
				paramsWindow = new ParamsWindow();
				resultsWindow = new ResultsWindow();
				paramsWindow.setVisible(true);
				resultsWindow.setVisible(false);
			}
		};

		/* Create and display the form */
		java.awt.EventQueue.invokeLater(execute);
	}

	private static void changeWindow() {
		paramsWindow.setVisible(!paramsWindow.isVisible());
		resultsWindow.setVisible(!resultsWindow.isVisible());
		resultsWindow.requestFocus();
	}

	public static void init(String path, int strategy, int deadlockDetectionType) {
		Controller.path = path;
		Controller.strategy = strategy;
		Controller.deadlockDetectionType = deadlockDetectionType;

		changeWindow();

		new WindowThread().start();
	}
}
