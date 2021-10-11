/**
 * Das Hauptmenue wird erstellt
 * Hier wird entschieden ob das Quiz bearbeitet werden soll oder gespielt werden soll
 * @file QuizGUI.java
 * @brief GUI fuer den Startbildschirm
 * @author Joel Maximilian Seidel
 */

package quizPacket;

import java.awt.Desktop;
import java.awt.GridLayout;
import java.io.File;
import java.io.IOException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class QuizGUI extends JFrame{
	private static final long serialVersionUID = -7523253076958567471L;
	
	//Buttons die auf dem Startbildschirm zu sehen sind
	private JButton bearbeitenButton, spielenButton, anleitungButton;
	
	private static QuizBearbeiten bearbeiten;
	private static QuizSpielen spielen;
	private static QuizDaten daten;
	
	/**
	 * Der Konstruktor fuer den Startbildschirm
	 * @fn QuizGUI(QuizDaten, QuizBearbeiten, QuizSpielen)
	 * @param d alle Funktionen und Daten die fuer das Quiz benoetigt werden
	 * @param b alle Funktionen und Daten die zum bearbeiten des Quizzes benoetigt werden
	 * @param s alle Funktionen und Daten die zum Spielen des Quizzes benoetigt werden
	 */
	public QuizGUI(QuizDaten d, QuizBearbeiten b, QuizSpielen s) {
		bearbeiten = b;
		spielen = s;
		daten = d;
		
		//erstellt ein neues Panel
		JPanel panel = new JPanel();
		
		//Grid-Layout und Grenzen fuer das Panel
		panel.setLayout(new GridLayout(1, 3, 20, 20));
		panel.setBorder(new EmptyBorder(5, 5, 5, 5));
		
		//Der Titel des Startbildschirms
		this.setTitle("Quiz");
		
		//alle Buttons werden erstellt, den Buttons werden Funktionen zugeteilt und sie werden zum Panel hinzugefuegt
		bearbeitenButton = new JButton("Quiz bearbeiten");
		spielenButton = new JButton("Quiz spielen");
		anleitungButton = new JButton("Anleitung");
		bearbeitenButton.addActionListener(e -> bearbeiten());
		spielenButton.addActionListener(e -> spielen());
		anleitungButton.addActionListener(e -> anleitung());
		panel.add(bearbeitenButton);
		panel.add(spielenButton);
		panel.add(anleitungButton);
		
		//Panel wird zum Frame hinzugefuegt, position wird bestimmt, Aktion beim schliessen wird festgelegt
		this.add(panel);
		this.pack();
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
	}
	
	/**
	 * Die Funktion leitet zum Teil des Programms weiter das fuer das Bearbeiten von Kategorien und Fragen zustaendig ist
	 * @fn bearbeiten()
	 * @brief weiterleiten zum bearbeiten
	 * @see bearbeitenMenu() in QuizDaten
	 */
	public void bearbeiten() {
		try {
			this.setVisible(false);
			bearbeiten.bearbeitenMenu();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	
	/**
	 * Die Funktion leitet zum Teil des Programms weiter in dem das Quiz gespielt wird
	 * @fn spielen()
	 * @brief weiterleiten zum spielen
	 * @see dateiueberpruefung() in QuizSpielen
	 */
	public void spielen() {
		try {
			this.setVisible(false);
			spielen.dateiueberpruefung();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	
	/**
	 * @fn anleitung()
	 * @brief öffnet die Anleitung die als PDF gespeichert ist
	 */
	public void anleitung() {
		if(Desktop.isDesktopSupported()) {
			try {
				File anleitung = new File("./src/Anleitung.pdf");
				Desktop.getDesktop().open(anleitung);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 *Die Funktion macht das Hauptauswahlmenue wieder sichtbar wenn zu ihm zurueckgekehrt wird
	 *@fn sichtbar()
	 *@brief wieder sichtbar machen der GUI 
	 */
	public static void sichtbar() {
		QuizGUI gui = new QuizGUI(daten, bearbeiten, spielen);
		gui.setVisible(true);
	}
}
