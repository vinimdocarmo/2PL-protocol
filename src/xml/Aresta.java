package xml;

public class Aresta {
	private String id;
	private Vertice verticeA;
	private Vertice verticeB;
	private String peso;

	public Aresta(String id, Vertice a, Vertice b, String peso) {
		this.id = id;
		verticeA = a;
		verticeB = b;
		this.peso = peso;
	}

	public Vertice getVerticeA() {
		return verticeA;
	}

	public void setVerticeA(Vertice verticeA) {
		this.verticeA = verticeA;
	}

	public Vertice getVerticeB() {
		return verticeB;
	}

	public void setVerticeB(Vertice verticeB) {
		this.verticeB = verticeB;
	}

	public String getPeso() {
		return peso;
	}

	public void setPeso(String peso) {
		this.peso = peso;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((peso == null) ? 0 : peso.hashCode());
		result = prime * result + ((verticeA == null) ? 0 : verticeA.hashCode());
		result = prime * result + ((verticeB == null) ? 0 : verticeB.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Aresta other = (Aresta) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (peso == null) {
			if (other.peso != null)
				return false;
		} else if (!peso.equals(other.peso))
			return false;
		if (verticeA == null) {
			if (other.verticeA != null)
				return false;
		} else if (!verticeA.equals(other.verticeA))
			return false;
		if (verticeB == null) {
			if (other.verticeB != null)
				return false;
		} else if (!verticeB.equals(other.verticeB))
			return false;
		return true;
	}

}
