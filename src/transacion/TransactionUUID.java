package transacion;

public class TransactionUUID {
	private static int uuid = 0;
	
	public static int generateUUID() {
		return ++uuid;
	}
}
