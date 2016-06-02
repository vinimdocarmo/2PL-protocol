package transacion;

public class Operation {
	public static enum Type {WRITE, READ, COMMIT, ABORT};
	private final Type type;
	private OperationItem item;
	private Transaction transaction;
	
	public static class OperationItem {
		private String name;
		
		public OperationItem (String name) {
			this.name = name;
		}

		public String getName() {
			return name;
		}
	}
	
	public Operation(final Type type,final OperationItem item) {
		this.type = type;
		this.setOperationItem(item);
	}
	
	public Operation(final Type type,final OperationItem item, final Transaction transaction) {
		this.type = type;
		this.setOperationItem(item);
		this.setTransaction(transaction);
	}
	
	public Operation(Type type) {
		this.type = type;
	}

	public Type getType() {
		return type;
	}

	public OperationItem getItem() {
		return item;
	}

	public void setOperationItem(final OperationItem item) {
		this.item = item;
	}
	
	public String toString() {
		if (this.item == null) {
			return this.type.toString();
		}
		return this.type + "(" + this.item.getName() + ")";
	}

	public Transaction getTransaction() {
		return transaction;
	}

	public void setTransaction(Transaction transaction) {
		this.transaction = transaction;
	}
	
}
