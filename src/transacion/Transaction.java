package transacion;

import java.util.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.UUID;

public class Transaction {
	private String id;
	private Timestamp timestamp;
	private ArrayList<Operation> operations;
	
	Transaction(ArrayList<Operation> operations) {
		this.id = UUID.randomUUID().toString();
		this.timestamp = new Timestamp(new Date().getTime());
		this.setOperations(operations);
	}
	
	@Override
	public String toString() {
		return "Transaction [id=" + id + ", timestamp=" + timestamp + "]";
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Timestamp getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Timestamp timestamp) {
		this.timestamp = timestamp;
	}

	public ArrayList<Operation> getOperations() {
		return operations;
	}

	public void setOperations(ArrayList<Operation> operations) {
		this.operations = operations;
	}
	
	
}
