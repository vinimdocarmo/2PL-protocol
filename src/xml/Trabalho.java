package xml;

import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JFrame;

import prefuse.Constants;
import prefuse.Display;
import prefuse.Visualization;
import prefuse.action.ActionList;
import prefuse.action.RepaintAction;
import prefuse.action.assignment.ColorAction;
import prefuse.action.assignment.DataColorAction;
import prefuse.action.layout.graph.ForceDirectedLayout;
import prefuse.activity.Activity;
import prefuse.controls.DragControl;
import prefuse.controls.NeighborHighlightControl;
import prefuse.controls.PanControl;
import prefuse.controls.WheelZoomControl;
import prefuse.controls.ZoomControl;
import prefuse.controls.ZoomToFitControl;
import prefuse.data.Graph;
import prefuse.data.io.DataIOException;
import prefuse.data.io.GraphMLReader;
import prefuse.render.AbstractShapeRenderer;
import prefuse.render.DefaultRendererFactory;
import prefuse.render.LabelRenderer;
import prefuse.util.ColorLib;
import prefuse.visual.VisualItem;

public class Trabalho {
	private Graph graph;
	private Display display;

	public Trabalho() throws FileNotFoundException {
		try {
			// Obtendo o Caminho do Arquivo
			File file = new File("D:\\arquivo.xml");
			graph = new GraphMLReader().readGraph(file);
		} catch (DataIOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	// Método para Visualizar o Grafo
	public void visualizar() {

		// Criando a Visualização
		Visualization vis = new Visualization();
		vis.add("graph", graph);

		LabelRenderer r = new LabelRenderer("name");
		r.setRoundedCorner(1, 1); // round the corners
		r.setRenderType(AbstractShapeRenderer.RENDER_TYPE_FILL);
		r.setHorizontalAlignment(Constants.CENTER);

		// Criando as Cores
		vis.setRendererFactory(new DefaultRendererFactory(r));
		int vermelho = ColorLib.rgb(255, 0, 0);
		int verde = ColorLib.rgb(0, 100, 0);
		int azul = ColorLib.rgb(70, 130, 180);
		int amarelo = ColorLib.rgb(255, 255, 0);
		int roxo = ColorLib.rgb(70, 130, 180);
		;
		int preto = ColorLib.rgb(47, 79, 79);
		;
		int branco = ColorLib.rgb(0, 139, 139);
		;
		int laranja = ColorLib.rgb(255, 165, 0);
		int rosa = ColorLib.rgb(255, 105, 180);
		;
		int marron = ColorLib.rgb(181, 181, 181);
		;

		// Criando a Paleta de Cores
		int[] palette = new int[] { vermelho, verde, azul, amarelo, roxo, preto, branco, laranja, rosa, marron };

		DataColorAction fill = new DataColorAction("graph.nodes", "gender", Constants.ORDINAL, VisualItem.FILLCOLOR,
				palette);

		// use black for node text
		ColorAction text = new ColorAction("graph.nodes", VisualItem.TEXTCOLOR, ColorLib.gray(0));
		// use light grey for edges
		ColorAction edges = new ColorAction("graph.edges", VisualItem.STROKECOLOR, ColorLib.gray(100));

		ActionList color = new ActionList();
		color.add(fill);
		color.add(text);
		color.add(edges);

		// add the actions to the visualization
		vis.putAction("color", color);

		ActionList layout = new ActionList(Activity.DEFAULT_STEP_TIME);
		layout.add(new ForceDirectedLayout("graph", true));
		layout.add(new RepaintAction());
		vis.putAction("layout", layout);

		Display display = new Display(vis);
		display.setSize(550, 400); // Tamanho do display
		display.pan(275, 60); // Configura o centro do Display.

		display.addControlListener(new DragControl());
		display.addControlListener(new PanControl());
		display.addControlListener(new ZoomControl());
		display.addControlListener(new WheelZoomControl());
		display.addControlListener(new ZoomToFitControl());
		display.addControlListener(new NeighborHighlightControl());

		
		vis.run("color"); // assign the colors
		vis.run("layout"); // start up the animated layout
		this.display = display;

	}

	public void gerar() {

		visualizar();

	}

	public static void main(String[] args) throws FileNotFoundException {
		Trabalho t = new Trabalho();
		t.gerar();
	}


	public Graph getGraph() {
		return graph;
	}

	public void setGraph(Graph graph) {
		this.graph = graph;
	}

	public Display getDisplay() {
		return display;
	}

	public void setDisplay(Display display) {
		this.display = display;
	}

}
