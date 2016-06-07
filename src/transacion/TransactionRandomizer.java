package transacion;

import java.util.Random;

public final class TransactionRandomizer {

	private Random rand;
	private TransactionSet transactions;
	
	private int lastNumber = -1;

	public TransactionRandomizer(final TransactionSet transactions) {
		this.rand = new Random();
		this.transactions = transactions;
	}

	public Transaction getRandomTransaction() {
		int randomNum = rand.nextInt(((transactions.size() - 1) - 0) + 1) + 0;
		
		if (randomNum == lastNumber && transactions.size() > 1) {
			return getRandomTransaction();
		}
		
		lastNumber = randomNum;

		return transactions.get(randomNum);
	}

}