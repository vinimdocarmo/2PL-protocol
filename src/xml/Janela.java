package xml;
import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Panel;
import java.io.FileNotFoundException;

import javax.swing.JFrame;
import javax.swing.JPanel;

import prefuse.Display;
//class Janela herdando caracteristica do JFrame (titulo,borda,abrir,fechar,maximizar)
@SuppressWarnings("serial")
public class Janela extends JFrame {
			//Atributos
			public Label  lb_Objetivo; //Label ou seja a parte aonde tá escrita o objetivo do jogo
			public Panel  pn_Jogo;  //Painel aonde vai tá o jogo em si
			//Metódo construtor
			public Janela (Display display,String pesos){
				//Criação de todos os componentes da interface
				this.setSize(550, 550); //altura,largura
				lb_Objetivo = new Label  (pesos);//objetivos do jogo
				lb_Objetivo.setPreferredSize(new Dimension(0, 50));//
				pn_Jogo		= new Panel	 ();
				pn_Jogo.setSize(400, 400);
				pn_Jogo.add(display);
				Panel painelNorte = new Panel();
				painelNorte.setLayout(new GridLayout(2,3));
				painelNorte.setBackground(Color.white);
				painelNorte.add(lb_Objetivo);
				painelNorte.add(pn_Jogo);
				lb_Objetivo.setBackground(Color.white);  
				lb_Objetivo.setForeground(Color.red);
				this.setLayout(new BorderLayout());
				this.add("North", painelNorte);
				this.add("Center",pn_Jogo);
				this.add("South", lb_Objetivo);
			}
	@SuppressWarnings("deprecation")
	public static void main(String[] args) throws FileNotFoundException {
		
	}
}