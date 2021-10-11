/**
 * Diese Klasse enthält nur die noetigsten Funktionen um die GUI zu starten
 * @file Quiz.java
 * @brief Main Datei die alles startet
 * @author Joel Maximilian Seidel
 */

package quizPacket;

public class Quiz {
	private static QuizGUI view;
	private QuizDaten daten;
	private QuizBearbeiten bearbeiten;
	private QuizSpielen spielen;
	/**
	 * @fn Quiz()
	 * @brief der Konstruktor
	 */
	public Quiz() {
		daten = new QuizDaten();
		bearbeiten = new QuizBearbeiten();
		spielen = new QuizSpielen();
		view = new QuizGUI(daten, bearbeiten, spielen);
		
	}
	
	/**
	 * @fn main(String[])
	 * @brief main-Funktion die alles startet
	 * @param args
	 */
	public static void main(String[] args) {
		new Quiz();
		view.setVisible(true);
	}
}
