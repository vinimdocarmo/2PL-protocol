package scheduler;

import graph.Digraph;
import graph.DirectedCycle;

public class DeadlockManager {

	public enum StrategyType {DETECTION, PREVENTION};
	public enum PreventionType {WOUND_WAIT, WAIT_DIE};
	
	private Digraph waitingGraph;
	
	public PreventionType preventionType;
	public StrategyType strategyType;
	
	public DeadlockManager(final StrategyType strategyType, final PreventionType preventionType) {
		this.strategyType = strategyType;
		this.preventionType = preventionType;
		this.waitingGraph = new Digraph(10);
	}
	
	public void addEdgeIntoGraph(int source, int target) {
		waitingGraph.addEdge(source, target);
	}
	
	public void removeEdgeFromGraph(int source, int target) {
		waitingGraph.removeEdge(source, target);
	}
	
	public boolean graphIsCyclic() {
		return new DirectedCycle(waitingGraph).hasCycle();
	}
}
