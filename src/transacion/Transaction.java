package transacion;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

import transacion.Operation.OperationItem;

public class Transaction {
	private final Integer id;
	private final Timestamp timestamp;
	private final ArrayList<Operation> operations;

	public Transaction(final ArrayList<Operation> operations) {
		this.id = TransactionUUID.generateUUID();
		this.timestamp = new Timestamp(new Date().getTime());
		this.operations = operations;
		
		for (Operation operation : this.operations) {
			operation.setTransaction(this);
		}
	}

	@Override
	public String toString() {
		String transaction = "";
		
		for (Operation operation : operations) {
			OperationItem item = operation.getOperationItem();
			
			if (item == null) {
				transaction += operation.getType() + "_" + this.id.toString();
			} else {
				transaction += operation.getType() + "_" + this.id.toString() + "(" + operation.getOperationItem().getName() + ") "; 
			}
		}
		
		return "T_" + this.id.toString() + " = " + transaction;
	}

	public int getId() {
		return id;
	}

	public Timestamp getTimestamp() {
		return timestamp;
	}

	public ArrayList<Operation> getOperations() {
		return operations;
	}

}
