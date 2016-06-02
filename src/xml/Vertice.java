package xml;

public class Vertice {

	private String id;
	private String nome;
	private String gender;

	public Vertice(String id, String nome, String gender) {
		super();
		this.id = id;
		this.nome = nome;
		this.gender = gender;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

}
