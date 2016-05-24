package transacion;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.DirectoryStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class Reader {

	private static String opRead = "read";
	private static String opWrite = "write";
	private static String opCommit = "commit";
	private static String opAbort = "abort";

	/**
	 * read the transactions files and return a set of all transactions in the
	 * directory
	 * 
	 * @return
	 * @throws IOException
	 */
	public static TransactionSet readTransactionFiles() throws IOException {
		TransactionSet transactions = new TransactionSet();

		Path transactionFilesPath = FileSystems.getDefault().getPath("transaction-files");

		DirectoryStream<Path> dir = Files.newDirectoryStream(transactionFilesPath);

		for (Iterator<Path> iterator = dir.iterator(); iterator.hasNext();) {
			String transactionFile = iterator.next().getFileName().toString();
			Path transactionPath = transactionFilesPath.resolve(transactionFile);
			LinkedList<Operation> operations = new LinkedList<Operation>();
			List<String> ops = Files.readAllLines(transactionPath, Charset.forName("UTF-8"));

			for (String op : ops) {
				String objName = Reader.getObjectNameFromOperation(op);
				Operation operation = null;
				Operation.OperationItem item = new Operation.OperationItem(objName);

				if (op.contains(opRead)) {
					operation = new Operation(Operation.Type.READ, item);
				} else if (op.contains(opWrite)) {
					operation = new Operation(Operation.Type.WRITE, item);
				} else if (op.contains(opCommit)) {
					operation = new Operation(Operation.Type.COMMIT);
				} else if (op.contains(opAbort)) {
					operation = new Operation(Operation.Type.ABORT);
				}

				operations.add(operation);
			}

			transactions.add(new Transaction(operations));
		}

		return transactions;
	}

	private static String getObjectNameFromOperation(String operation) {
		int firstIndexOf = operation.indexOf("("), secondIndexOf = operation.indexOf(")");

		if (firstIndexOf == -1 || secondIndexOf == -1) {
			return operation;
		}

		return operation.substring(firstIndexOf + 1, secondIndexOf);
	}
}
