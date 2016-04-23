package transacion;

public class Operation {
	public enum Type {WRITE, READ};
	private Type type;
	private ObjectOp obj;
	
	public static class ObjectOp {
		private String name;
		
		ObjectOp (String name) {
			this.name = name;
		}

		public String getName() {
			return name;
		}
	}
	
	Operation(Type type, ObjectOp obj) {
		this.type = type;
		this.setObj(obj);
	}

	public Type getType() {
		return type;
	}

	public ObjectOp getObj() {
		return obj;
	}

	public void setObj(ObjectOp obj) {
		this.obj = obj;
	}
	
}
