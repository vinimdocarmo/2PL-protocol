package transacion;

import java.util.Random;

public final class TransactionRandomizer {

	private Random rand;
	private TransactionSet transactions;

	public TransactionRandomizer(final TransactionSet transactions) {
		this.rand = new Random();
		this.transactions = transactions;
	}

	public Transaction getRandomTransaction() {
		int randomNum = rand.nextInt(((transactions.size() - 1) - 0) + 1) + 0;

		return transactions.get(randomNum);
	}

}