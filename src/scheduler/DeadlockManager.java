package scheduler;

public class DeadlockManager {

	public enum PreventionType {WAIT_DIE, WOUND_WAIT};
	
	/**
	 * TODO: 
	 * 	A gente vai precisar de uma precisar de um campo com o grafos nessa classe.
	 *  Algo como:
	 *  	public Graph watingGraph;
	 */
	
	private PreventionType preventionType;
	
	public DeadlockManager(final PreventionType preventionType) {
		this.preventionType = preventionType;
	}
	
	/**
	 * TODO: Esse método aqui vai ser o método que a gente vai adicionar as transaçãoes no grafo.
	 * Basta colocar os int's source e target como nós.
	 * 
	 * A direção vai ser: source -----> target
	 *  
	 * @param source
	 * @param target
	 */
	public void addEdgeIntoGraph(final int source, final int target) {
		/**
		 * TODO:
		 * 	nesse método aqui tu vai ter que ver se já existem os nós source e target
		 *  se eles existirem:
		 *  	cria uma aresta source ----> target
		 *  se algum deles não existir:
		 *  	cria ele(s), e depois cria a aresta direcionada source -----> target
		 */
	}
	
	/**
	 * TODO: Esse método aqui var ser o método que a gente vai remover uma aresta.
	 * @param source
	 * @param target
	 */
	public void removeEdgeFromGraph(final int source, final int target) {
		/**
		 * TODO:
		 * nesse método tu vai remover a aresta direcionada de source para target: source ---/---> target
		 * 
		 * depois de remover, se no nó source ou target não sairem ou entrarem aresta, por removê-los 
		 */
	}
	
	/**
	 * TODO: retorna um booleano dizendo se o grafo é cíclico
	 * @return
	 */
	public boolean graphIsCyclic() {
		return false;
	}
	
}
