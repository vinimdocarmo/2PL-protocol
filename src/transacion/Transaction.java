package transacion;

import java.util.ArrayList;
import java.util.Date;

import transacion.Operation.OperationItem;

public class Transaction {
	private final Integer id;
	private Date timestamp;
	private final ArrayList<Operation> operations;

	public Transaction(final ArrayList<Operation> operations) {
		this.id = TransactionUUID.generateUUID();
		this.operations = operations;
		
		for (Operation operation : this.operations) {
			operation.setTransaction(this);
		}
	}

	@Override
	public String toString() {
		String transaction = "";
		
		for (Operation operation : operations) {
			OperationItem item = operation.getItem();
			
			if (item == null) {
				transaction += operation.getType() + "_" + this.id.toString();
			} else {
				transaction += operation.getType() + "_" + this.id.toString() + "(" + operation.getItem().getName() + ") "; 
			}
		}
		
		return "T_" + this.id.toString() + " = " + transaction;
	}

	public int getId() {
		return id;
	}

	public Date getTimestamp() {
		return timestamp;
	}
	
	public void setTimestamp(final Date timestramp) {
		this.timestamp = timestramp;
	}

	public ArrayList<Operation> getOperations() {
		return operations;
	}

}
