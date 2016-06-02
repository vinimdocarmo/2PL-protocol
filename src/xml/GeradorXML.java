package xml;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.XMLOutputter;

import prefuse.Display;


public class GeradorXML {

	private static List<Aresta> arestas = new ArrayList<Aresta>();
	private static List<Vertice> vertices = new ArrayList<Vertice>();
	private Display display;
	private String pesos;
	private Janela j;
	
	public void criarXml() {
		fecharJanela();
		pesos = "";
		Element graphml = new Element("graphml");
		Document myDocument = new Document(graphml);

		Element graph = new Element("graph");
		graph.setAttribute("edgedefault", "undirected");

		Element key = new Element("key");
		key.setAttribute("id", "name");
		key.setAttribute("for", "node");
		key.setAttribute("attr.name", "name");
		key.setAttribute("attr.type", "string");

		Element key2 = new Element("key");
		key2.setAttribute("id", "gender");
		key2.setAttribute("for", "node");
		key2.setAttribute("attr.name", "gender");
		key2.setAttribute("attr.type", "string");
		
	//  <key id="d1" for="edge" attr.name="weight" attr.type="double"/>

		Element key3 = new Element("key");
		key3.setAttribute("id", "d1");
		key3.setAttribute("for", "edge");
		key3.setAttribute("attr.name", "weight");
		key3.setAttribute("attr.type", "string");

		graph.addContent(key);
		graph.addContent(key2);
		graph.addContent(key3);

		
		
		for (Vertice vertice : vertices) {
			Element elementA = createNode(vertice.getId(), vertice.getNome(), vertice.getId());
			graph.addContent(elementA);

		}
		for (Aresta aresta : arestas) {
			Element ligacao = createEdge(aresta.getId(),aresta.getVerticeA().getId(), aresta.getVerticeB().getId(),aresta.getPeso());
			pesos = pesos + aresta.getVerticeA().getNome() + "-> "+ aresta.getVerticeB().getNome() +" =" + aresta.getPeso() + "";
			graph.addContent(ligacao);
		}

		graphml.addContent(graph);
		XMLOutputter xout = new XMLOutputter();

		try {

			xout.output(myDocument, System.out);

		} catch (IOException e) {

			e.printStackTrace();

		}

		try {

			FileWriter arquivo = new FileWriter(new File("D:\\arquivo.xml"));

			xout.output(myDocument, arquivo);

		} catch (IOException e) {

			e.printStackTrace();

		}

		gerarInterface();
		
	}

	public Element createNode(String idNode, String name, String gender) {
		Element node = new Element("node");
		node.setAttribute("id", idNode);

		Element data = new Element("data");
		data.setAttribute("key", "name");
		data.setText(name);

		Element data2 = new Element("data");
		data2.setAttribute("key", "gender");
		data2.setText(gender);

		
		
		node.addContent(data);
		node.addContent(data2);
		return node;
	}

	public Element createEdge(String id,String source, String target,String peso) {
		Element node = new Element("edge");
		node.setAttribute("id", id);

		node.setAttribute("source", source);
		node.setAttribute("target", target);

		Element data = new Element("data");
		data.setAttribute("key", "d1");
		data.setText(peso);

		node.addContent(data);
		
		return node;
	}

	public void gerarInterface() {
	//	criarXml(vertices, arestas);
		Trabalho t;
		try {
			t = new Trabalho();
			t.visualizar();
			display = t.getDisplay();
			j = new Janela(display, pesos);
			j.show();
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public void fecharJanela(){
		if(j!= null){
			
		
		this.j.hide();
		}
		}

	public static void main(String[] args) throws InterruptedException {
		GeradorXML gerador = new GeradorXML();
		for (int i = 0; i <4; i++) {
			gerador.fecharJanela();
			Vertice v = new Vertice(String.valueOf(i), "Transaction  - "+String.valueOf(i), String.valueOf(i));
		gerador.addVertice(v);
		
		gerador.gerarInterface();
		Thread.sleep(2000);
		}
	}

	public void addAresta(Aresta aresta){
		
		this.arestas.add(aresta);
		criarXml();
	}
	public void addVertice(Vertice vertice){
		this.vertices.add(vertice);
		criarXml();
	}
	
	
	public void removerAresta(Aresta aresta){
	arestas.remove(buscarPosicaoAresta(aresta.getId()));
	criarXml();
	
	}
	public void removerVertice(Vertice vertice){
		vertices.remove(buscarPosicaoVertice(vertice.getId()));
	criarXml();
	}
	
	
	
	public Integer buscarPosicaoAresta(String id) {
		int i = 0;

		for (Aresta aresta : arestas) {
			if (aresta.getId().equals(id)) {
				return i;
			}
			i++;
		}
		return null;
	}

	public Integer buscarPosicaoVertice(String id) {
		int i = 0;

		for (Vertice vertice : vertices) {
			if (vertice.getId().equals(id)) {
				return i;
			}
			i++;
		}
		return null;
	}

	public Display getDisplay() {
		return display;
	}

	public void setDisplay(Display display) {
		this.display = display;
	}

	public String getPesos() {
		return pesos;
	}

	public void setPesos(String pesos) {
		this.pesos = pesos;
	}

	public static List<Aresta> getArestas() {
		return arestas;
	}

	public static void setArestas(List<Aresta> arestas) {
		GeradorXML.arestas = arestas;
	}

	public static List<Vertice> getVertices() {
		return vertices;
	}

	public static void setVertices(List<Vertice> vertices) {
		GeradorXML.vertices = vertices;
	}

	public Janela getJ() {
		return j;
	}

	public void setJ(Janela j) {
		this.j = j;
	}

}
