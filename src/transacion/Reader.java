package transacion;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.DirectoryStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Reader {

	private static String opRead = "read";
	private static String opWrite = "write";

	/**
	 * read the transactions files and return an array list of transaction
	 * elements
	 * 
	 * @return
	 * @throws IOException
	 */
	public static ArrayList<Transaction> readTransactionFiles() throws IOException {
		// TODO: read the files randomly

		ArrayList<Transaction> transactions = new ArrayList<Transaction>();

		Path transactionFilesPath = FileSystems.getDefault().getPath("transaction-files");

		DirectoryStream<Path> dir = Files.newDirectoryStream(transactionFilesPath);

		for (Iterator<Path> iterator = dir.iterator(); iterator.hasNext();) {
			String transactionFile = iterator.next().getFileName().toString();
			Path transactionPath = transactionFilesPath.resolve(transactionFile);
			ArrayList<Operation> operations = new ArrayList<Operation>();
			List<String> ops = Files.readAllLines(transactionPath, Charset.forName("UTF-8"));
			
			System.out.println("Lendo " + transactionFile);

			for (String op : ops) {
				String objName = Reader.getObjectNameFromOperation(op);
				Operation operation = null;
				Operation.ObjectOp operationObject = new Operation.ObjectOp(objName);
				
				if (op.contains(opRead)) {
					operation = new Operation(Operation.Type.READ, operationObject);
					System.out.println("A opearação " + op + " é de leitura");
				} else if (op.contains(opWrite)) {
					operation = new Operation(Operation.Type.WRITE, operationObject);
					System.out.println("A opearação " + op + " é de escrita");
				}
				
				operations.add(operation);
			}

			transactions.add(new Transaction(operations));
		}

		return transactions;
	}

	private static String getObjectNameFromOperation(String operation) {
		return operation.substring(operation.indexOf("("), operation.indexOf(")"));
	}
}
