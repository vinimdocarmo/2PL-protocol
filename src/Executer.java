import java.io.IOException;

import transacion.Reader;

public class Executer {
	public static void main(String[] args) throws IOException {
		System.out.println("Trabalho - Protocolo 2PL");
		
		Reader.readTransactionFiles();
	}
}
