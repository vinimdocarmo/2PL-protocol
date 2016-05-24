package transacion;

import java.util.Date;
import java.util.LinkedList;
import java.util.Queue;

import transacion.Operation.OperationItem;

import java.sql.Timestamp;

public class Transaction {
	private final Integer id;
	private final Timestamp timestamp;
	private final Queue<Operation> operations;

	public Transaction(final LinkedList<Operation> operations) {
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

	public Queue<Operation> getOperations() {
		return operations;
	}

}
