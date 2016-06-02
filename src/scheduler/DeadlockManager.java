package scheduler;

import java.util.List;
import java.util.Scanner;

import xml.Aresta;
import xml.GeradorXML;
import xml.Vertice;

public class DeadlockManager {

	public enum PreventionType {WAIT_DIE, WOUND_WAIT};
	public static GeradorXML gerador = new GeradorXML();
	
	
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
	
	public void addEdgeIntoGraph(Aresta aresta) {
		gerador.addAresta(aresta);
	}
	
	public void removeEdgeFromGraph(Aresta aresta) {
		gerador.removerAresta(aresta);
	}
	
	public void addNodeIntoGraph(Vertice vertice){
		gerador.addVertice(vertice);	
	}
	
	public void removeNodeFromGraph(Vertice vertice){
		gerador.removerVertice(vertice);
	}
	
	public boolean graphIsCyclic() {
		return false;
	}
	
	public static void main(String[] args) {
		DeadlockManager deadlockManager = new DeadlockManager(DeadlockManager.PreventionType.WAIT_DIE);
		Integer id = 0;
		boolean continua = true;
		while (continua) {
			id++;
			Scanner read = new Scanner(System.in);
			Scanner r = new Scanner(System.in);

			System.out.println("OPERACAO 1 - VERTICE | 2 - ARESTA");
			Integer operacao = r.nextInt();
			if (operacao.equals(1)) {

				System.out.println("Digite o nome");
				String nome = read.nextLine();
				Vertice vertice = new Vertice(id.toString(), nome, id.toString());
				deadlockManager.addNodeIntoGraph(vertice);

			}else if(operacao.equals(2)){
				System.out.println("ARESTA - ADD - 1 REMOVE - 2");
				Integer respos = r.nextInt();

				System.out.println("VERTICE A");
				String a = read.nextLine();

				System.out.println("VERTICE B");
				String b = read.nextLine();
				
				Vertice verticeA = new Vertice(a, "", a);
				Vertice verticeB = new Vertice(b, "", a);
				Aresta aresta = new Aresta("1", verticeA, verticeB, "10");

				
				
						if(respos.equals(1)){
							deadlockManager.addEdgeIntoGraph(aresta);

						}else if(respos.equals(2)){
							deadlockManager.removeEdgeFromGraph(aresta);
						}
						
				
				
			}
		}
	}
}
