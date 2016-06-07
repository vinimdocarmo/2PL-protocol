package scheduler;

import graph.*;

public class DeadlockManager {

	public enum PreventionType {WAIT_DIE, WOUND_WAIT};
	
	private Digraph waitingGraph;
	private DirectedCycle directedGraph;
	
	/**
	 * TODO: 
	 * 	A gente vai precisar de uma precisar de um campo com o grafos nessa classe.
	 *  Algo como:
	 *  	public Graph watingGraph;
	 */
	
	private PreventionType preventionType;
	
	public DeadlockManager(final PreventionType preventionType) {
		this.preventionType = preventionType;
		this.waitingGraph = new Digraph(10);
		this.directedGraph = new DirectedCycle(this.waitingGraph);
	}
	
	public void addEdgeIntoGraph(int source, int target) {
		waitingGraph.addEdge(source, target);
	}
	
	public void removeEdgeFromGraph() {
		//TODO
	}
	
	public boolean graphIsCyclic() {
		return directedGraph.hasCycle();
	}
}
