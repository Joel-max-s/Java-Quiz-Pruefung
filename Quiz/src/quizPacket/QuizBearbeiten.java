/**
 * Das Quiz kann vollständig bearbeitet werden.
 * Es koennen neue Kategorien und Fragen erstellt werden, bei bedarf koennen Fragen und Kategorien auch geloescht werden 
 * @file QuizBearbeiten.java
 * @brief Alle Funktionen um das Quiz zu bearbeiten
 * @author Joel Maximilian Seidel
 */

package quizPacket;

import java.io.IOException;
import java.util.ArrayList;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class QuizBearbeiten {
	//erstellt neues QuizDaten()
	private QuizDaten daten = new QuizDaten();
	//Speichert den Namen der ausgewaehlten Kategorie
	private String kategorie = "";
	//Speichert den Index der Frage die bearbeitet werden soll
	private int fragenIndex = -1;
	//Speichert den Wortlaut der neuen oder bearbeiteten Frage
	private String neueFrage = "";
	//Speichert alle neuen bzw. bearbeiteten Antworten
	private String[] neueAntworten = new String[4];
	//Speichert den Index der neuen bzw. bearbeiteten richtigen Antwort
	int neueRichtigeAntwort = -1;
	
	/**
	 * In diesem Menue kann Ausgewaehlt werden ob eine Kategorie geloescht oder Bearbeitet werden soll.
	 * Zusaetzlich kann ausgewaehlt werden ob man eine neue Kategorie erstellen moechte,
	 * oder ob man zurueck ind Hauptmenue moechte.
	 * @fn bearbeitenMenu()
	 * @brief auswahlmoeglichkeiten fuer Kategorien
	 * @throws IOException
	 * @see resetKategorien() der Klasse QuizDaten
	 * @see leseKategorien() der Klasse QuizDaten
	 * @see fragenBearbeiten()
	 * @see kategorieLoeschen()
	 * @see kategorieHinzufuegen()
	 * @see QuizGUI.sichtbar()
	 */
	public void bearbeitenMenu() throws IOException {
		daten.resetKategorien();
		daten.leseKategorien();
		Object[] kategorien = daten.getKategorienAlsString().toArray();
		final JComboBox<Object> combo = new JComboBox<>(kategorien);
		Object[] antworten = {"Kategorie bearbeiten", "Kategorie loeschen", "neue Kategorie erstellen", "zurueck"};
		int weiter = JOptionPane.showOptionDialog(null, combo, "Kategorieoptionen", JOptionPane.DEFAULT_OPTION, 
				JOptionPane.QUESTION_MESSAGE, null, antworten, null);
		kategorie = (String) combo.getSelectedItem();
		
		//wenn "Kategorie bearbeiten" ausgewaehlt wurde und keine Kategorien vorhanden sind
		if (weiter == 0 && daten.getKategorienAlsString().isEmpty())
			bearbeitenMenu();
		
		//wenn "Kategorie bearbeiten" ausgewaehlt wurde, es ist mindestens eine Kategorie vorhanden
		else if (weiter == 0)
			fragenBearbeiten();
		
		//wenn "Kategorie loeschen" ausgewaehlt wurde und keine Kategorie vorhanden ist
		else if (weiter == 1 && daten.getKategorienAlsString().isEmpty())
			bearbeitenMenu();
		
		//wenn "Kategorie loeschen" ausgewaehlt wurde, es ist mindestens eine Kategorie vorhanden
		else if (weiter == 1)
			kategorieLoeschen();
		
		//wenn "neue Kategorie erstellen" ausgewaehlt wurde
		else if (weiter == 2)
			kategorieHinzufuegen();
		
		else
			QuizGUI.sichtbar();	
	}
	
	/**
	 * Die in "bearbeitenMenu() ausgewaehlte Kategorie wird entweder geloescht oder der Loeschvorgang wird abgebrochen.
	 * @fn kategorieLoeschen()
	 * @brief loeschen der ausgewaehlten Kategorie
	 * @throws IOException
	 * @see loescheKategorie(String) der Klasse QuizDaten
	 * @see bearbeitenMenu()
	 */
	public void kategorieLoeschen() throws IOException {
		Object[] content = {"Moechten sie die Kategorie", kategorie, "wirklich loeschen?"};
		String[] auswahlmoeglichkeiten = {"Kategorie loeschen", "Kategorie nicht Loeschen"};
		int weiter = JOptionPane.showOptionDialog(null, content, "Wirklich Loeschen?", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, 
				null, auswahlmoeglichkeiten, null);
		
		//wenn "Kategorie loeschen" ausgewaehlt wurde
		if (weiter == 0) {
			daten.loescheKategorie(kategorie);
			bearbeitenMenu();
		}
		else
			bearbeitenMenu();
	}
	
	/**
	 * @fn kategorieHinzufuegen()
	 * @brief es wird eine neue Kategorie hinzugefuegt
	 * @throws IOException
	 * @see fuegeKategorieHinzu(String) der Klasse QuizDaten
	 * @see bearbeitenMenu()
	 */
	public void kategorieHinzufuegen() throws IOException {
		String kategorieName = (String)JOptionPane.showInputDialog(null, "Bitte geben sie einen Kategorienamen ein", "Kategorie hinzufuegen", 
				JOptionPane.PLAIN_MESSAGE);
		if (kategorieName != null) {
			//wenn nichts eingegeben wurde
			if (kategorieName.isBlank() || kategorieName.isEmpty())
				bearbeitenMenu();
			//wenn etwas eingegeben wurde
			else {
				daten.fuegeKategorieHinzu(kategorieName);
				bearbeitenMenu();
			}
		}
		else
			bearbeitenMenu();
	}
	
	/**
	 * Hier kann ausgesucht werden welche Frage der ausgewaehlten Kategorie bearbeitet werden soll.
	 * Zudem koennen Fragen geloescht werden oder neue Fragen erstellt werden.
	 * @fn fragenBearbeiten()
	 * @brief bearbeiten von Fragen
	 * @throws IOException
	 * @see erstelleFragenUndAntworten(String) der Klasse QuizDaten
	 * @see eineFrageBearbeiten()
	 * @see frageWirklichLoeschen()
	 * @see frageErstellen()
	 * @see bearbeitenMenu()
	 */
	public void fragenBearbeiten() throws IOException {
		daten.erstelleFragenUndAntworten(kategorie);
		String[] fragen = new String[daten.getFragen().size()];
		for (int i = 0; i < daten.getFragen().size(); i++) {
			fragen[i] = daten.getFragen().get(i);
		}
		final JComboBox<String> combo = new JComboBox<>(fragen);
		
		String[] auswahlMoeglichkeiten = {"Frage bearbeiten", "Frage loeschen", "neue Frage erstellen", "zurueck"};
		
		int weiter = JOptionPane.showOptionDialog(null, combo, "Fragenmenue", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, 
				null, auswahlMoeglichkeiten, auswahlMoeglichkeiten[0]);
		fragenIndex = combo.getSelectedIndex();
		//"Frage bearbeiten" wurde ausgewaehlt, es sind aber keine Fragen vorhanden
		if (weiter == 0 && daten.getFragen().isEmpty())
			fragenBearbeiten();
		
		//"Frage bearbeiten" wurde ausgewaehlt, es ist mindestens eine Frage vorhanden
		else if (weiter == 0)
			eineFrageBearbeiten();
		
		//"Frage loeschen" wurde ausgewaehlt, es sind aber keine Fragen vorhanden
		else if (weiter == 1 && daten.getFragen().isEmpty())
			fragenBearbeiten();
		
		//"Frage loeschen" wurde ausgewaehlt, es ist mindestens eine Frage vorhanden
		else if (weiter == 1)
			frageWirklichLoeschen();
		
		//"neue Frage" wurde ausgewaehlt
		else if (weiter == 2)
			frageErstellen();
		
		else
			bearbeitenMenu();
	}
	
	/**
	 * Die in fragenBearbeiten() ausgewaehlte Frage kann hier bearbeitet werden.
	 * Es kann die Frage, alle Antworten und die richtige Antwort bearbeitet werden.
	 * @fn eineFrageBearbeiten
	 * @brief eine Frage kann bearbeitet werden
	 * @throws IOException
	 * @see ueberpruefen()
	 * @see warnung(int)
	 * @see frageSpeichern()
	 * @see bearbeitenMenu()
	 * @see fragenBearbeiten()
	 */
	public void eineFrageBearbeiten() throws IOException {
		String[] antworten = {"Antwort 1", "Antwort 2", "Antwort 3", "Antwort 4"};
		String[] abcd = {"a", "b", "c", "d"};
		
		JTextField frage = new JTextField();
		JTextField antwort1 = new JTextField();
		JTextField antwort2 = new JTextField();
		JTextField antwort3 = new JTextField();
		JTextField antwort4 = new JTextField();
		
		frage.setText(daten.getFragen().get(fragenIndex));
		antwort1.setText(daten.getAntworten().get(fragenIndex * 4));
		antwort2.setText(daten.getAntworten().get(fragenIndex * 4 + 1));
		antwort3.setText(daten.getAntworten().get(fragenIndex * 4 + 2));
		antwort4.setText(daten.getAntworten().get(fragenIndex * 4 + 3));
		
		int voreingestellteRichtigeAntwort = -1;
		if (daten.getRichtigeAntworten().get(fragenIndex).toUpperCase().equals(abcd[0].toUpperCase()) || 
				daten.getRichtigeAntworten().get(fragenIndex).equals("1"))
			voreingestellteRichtigeAntwort = 0;
		else if (daten.getRichtigeAntworten().get(fragenIndex).toUpperCase().equals(abcd[1].toUpperCase()) || 
				daten.getRichtigeAntworten().get(fragenIndex).equals("2"))
			voreingestellteRichtigeAntwort = 1;
		else if (daten.getRichtigeAntworten().get(fragenIndex).toUpperCase().equals(abcd[2].toUpperCase()) || 
				daten.getRichtigeAntworten().get(fragenIndex).equals("3"))
			voreingestellteRichtigeAntwort = 2;
		else if (daten.getRichtigeAntworten().get(fragenIndex).toUpperCase().equals(abcd[3].toUpperCase()) || 
				daten.getRichtigeAntworten().get(fragenIndex).equals("4"))
			voreingestellteRichtigeAntwort = 3;
		
		JComboBox<String> richtigeAntwort = new JComboBox<>(antworten);
		richtigeAntwort.setSelectedIndex(voreingestellteRichtigeAntwort);
		
		
		Object[] fragenUndAntworten = {"Frage:", frage, "Antwortmoeglichkeit 1:", antwort1, "Antowortmoeglichkeit 2:", antwort2,
				"Antwortmoeglichkeit 3:", antwort3, "Antwortmoeglichkeit 4:", antwort4, "Bitte waehlen sie die richtige Antwort aus:", richtigeAntwort};
		String[] auswahlMoeglichkeiten = {"Speichern", "weitere Frage bearbeiten", "Abbrechen"};
		
		int j = JOptionPane.showOptionDialog(null, fragenUndAntworten, "Frage bearbeiten", JOptionPane.DEFAULT_OPTION,
				JOptionPane.PLAIN_MESSAGE, null, auswahlMoeglichkeiten, auswahlMoeglichkeiten[1]);
		neueFrage = frage.getText();
		neueAntworten[0] = antwort1.getText();
		neueAntworten[1] = antwort2.getText();
		neueAntworten[2] = antwort3.getText();
		neueAntworten[3] = antwort4.getText();
		neueRichtigeAntwort = richtigeAntwort.getSelectedIndex() + 1;
		
		//wenn nicht alle Felder ausgefuellt worden sind und "Speichern" oder "weitere Frage bearbeiten" ausgewaehlt wurde
		if (!ueberpruefen() && (j == 0 || j == 1))
			warnung(0);
		
		else {
			//wenn "Speichern" ausgewaehlt wurde und alle Felder ausgefuellt sind
			if (j == 0) {
				frageSpeichern();
				bearbeitenMenu();
			}
			//wenn "weitere Frage bearbeiten" ausgewaehlt wurde und alle Felder ausgefuellt sind
			else if (j == 1) {
				frageSpeichern();
				fragenBearbeiten();
			}
			else
				fragenBearbeiten();
		}
	}
	
	/**
	 * @fn frageSpeichern()
	 * @brief die bearbeitete Frage wird gespeichert
	 * @throws IOException
	 * @see speichereBearbeiteteKategorie(String) der Klasse QuizDaten
	 */
	public void frageSpeichern() throws IOException {
		Integer j2 = neueRichtigeAntwort;
		ArrayList<String> temp = new ArrayList<String>();
		
		//temporaere ArrayList mit bearbeiteter Frage
		temp = daten.getFragen();
		temp.set(fragenIndex, neueFrage);
		daten.setFragen(temp);
		
		//temporaere ArrayList mit bearbeiteten Antworten
		temp = daten.getAntworten();
		for (int k = 0; k < 4; k++) {
			temp.set(4*fragenIndex + k, neueAntworten[k]);
		}
		daten.setAntworten(temp);
		
		//temporaere ArrayList mit bearbeiteter richtiger Antwort
		temp = daten.getRichtigeAntworten();
		temp.set(fragenIndex, j2.toString());
		daten.setRichtigeAntworten(temp);
		daten.speichereBearbeiteteKategorie(kategorie);
	}
	
	/**
	 * @fn frageWirklichLoeschen()
	 * @brief sicherstellen das Frage geloescht werden soll
	 * @throws IOException
	 * @see eineFrageLoeschen()
	 * @see fragenBearbeiten()
	 */
	public void frageWirklichLoeschen() throws IOException {
		Object[] optionen = {"Ja, Frage loeschen", "Abbrechen"};
		int weiter = JOptionPane.showOptionDialog(null, "Wollen sie die Frage wirklich loeschen?", "Frage loeschen", 
				JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, optionen, optionen[1]);
		if (weiter == 0)
			eineFrageLoeschen();
		else
			fragenBearbeiten();
	}
	
	/**
	 * @fn eineFrageLoeschen()
	 * @brief Frage wird geloescht
	 * @throws IOException
	 * @see fragenBearbeiten()
	 * @see speichereBearbeiteteKategorie(String) der Klasse QuizDaten
	 */
	public void eineFrageLoeschen() throws IOException {
		ArrayList<String> temp = new ArrayList<String>();
		
		//temporaere ArrayList wo Frage geloescht wird
		temp = daten.getFragen();
		temp.remove(fragenIndex);
		daten.setFragen(temp);
		
		//temporaere ArrayList wo Antworten geloescht werden
		temp = daten.getAntworten();
		for (int i = 3; i >= 0; i--) {
			temp.remove(4 * fragenIndex + i);
		}
		daten.setAntworten(temp);
		
		//temporaere ArrayList wo richtige Antwort Antwort geloescht wird
		temp = daten.getRichtigeAntworten();
		temp.remove(fragenIndex);
		daten.setRichtigeAntworten(temp);
		daten.speichereBearbeiteteKategorie(kategorie);
		fragenBearbeiten();
	}
	
	/**
	 * Hier kann eine neue Frage erstellt werden.
	 * Zusaetzlich muessen die Antworten und die richte Antwort eingestellt werden.
	 * @fn frageErstellen()
	 * @brief neue Frage erstellen
	 * @throws IOException
	 * @see ueberpruefen()
	 * @see warnung(int)
	 * @see fragenBearbeiten()
	 * @see speichereBearbeiteteKategorie(String) der Klasse QuizDaten
	 * @see bearbeitenMenu()
	 */
	public void frageErstellen() throws IOException {
		String[] antworten = {"Antwort 1", "Antwort 2", "Antwort 3", "Antwort 4"};
		
		JTextField frage = new JTextField();
		JTextField antwort1 = new JTextField();
		JTextField antwort2 = new JTextField();
		JTextField antwort3 = new JTextField();
		JTextField antwort4 = new JTextField();
		
		JComboBox<String> richtigeAntwort = new JComboBox<>(antworten);
		
		Object[] fragenUndAntworten = {"Frage:", frage, "Antwortmoeglichkeit 1:", antwort1, "Antowortmoeglichkeit 2:", antwort2,
				"Antwortmoeglichkeit 3:", antwort3, "Antwortmoeglichkeit 4:", antwort4, "Bitte waehlen sie die richtige Antwort aus:", richtigeAntwort};
		String[] auswahlMoeglichkeiten = {"Speichern", "weitere Frage bearbeiten", "Abbrechen"};
		
		int weiter = JOptionPane.showOptionDialog(null, fragenUndAntworten, "Frage bearbeiten", JOptionPane.DEFAULT_OPTION,
				JOptionPane.PLAIN_MESSAGE, null, auswahlMoeglichkeiten, auswahlMoeglichkeiten[1]);
		neueFrage = frage.getText();
		neueAntworten[0] = antwort1.getText();
		neueAntworten[1] = antwort2.getText();
		neueAntworten[2] = antwort3.getText();
		neueAntworten[3] = antwort4.getText();
		neueRichtigeAntwort = richtigeAntwort.getSelectedIndex() + 1;
		
		//wenn nicht alles ausgefuellt ist und "Speichern" oder "weitere Frage bearbeiten" ausgewaehlt wurde 
		if(!ueberpruefen() && (weiter == 0 || weiter == 1))
			warnung(1);
		
		else {
			// wenn "Abbrechen" gewaehlt wurde
			if (weiter == 2)
				fragenBearbeiten();
			
			else {
				Integer nra = neueRichtigeAntwort;
				ArrayList<String> temp = new ArrayList<String>();
				temp = daten.getFragen();
				temp.add(neueFrage);
				daten.setFragen(temp);
				temp = daten.getAntworten();
				for (int i = 0; i < 4; i++) {
					temp.add(neueAntworten[i]);
				}
				daten.setAntworten(temp);
				temp = daten.getRichtigeAntworten();
				temp.add(nra.toString());
				daten.setRichtigeAntworten(temp);
				daten.speichereBearbeiteteKategorie(kategorie);
				
				//wenn "Speichern" ausgewaehlt wurde
				if (weiter == 0)
					bearbeitenMenu();
				else
					fragenBearbeiten();
			}
		}
	}
	
	/**
	 * @fn warnung(int)
	 * @brief zeigt Fehlermeldung
	 * @param fehler bestimmt welche Fehlermeldung angezeigt wird
	 * @pre fehler muss entweder 0 oder 1 sein
	 * @see eineFrageBearbeiten()
	 * @see frageErstellen()
	 * @throws IOException
	 */
	public void warnung(int fehler) throws IOException {
		switch (fehler) {
		case 0:
			JOptionPane.showMessageDialog(null, "Bitte fuellen sie alle Felder aus");
			eineFrageBearbeiten();
			break;
		case 1:
			JOptionPane.showMessageDialog(null, "Bitte fuellen sie alle Felder aus");
			frageErstellen();
			break;
		}
	}
	
	/**
	 * @fn ueberpruefen()
	 * @brief prueft ob alle Eingabefelder ausgefuellt sind
	 * @return true wenn alle Feld ausgefuellt sind
	 * @return false wenn mindestens ein Feld nicht ausgefuellt wurde
	 */
	public boolean ueberpruefen() {
		//wenn Fragenfeld leer ist
		if (neueFrage.isEmpty() || neueFrage.isBlank())
			return false;
		
		//wenn nicht alle Antworten ausgefuellt sind
		for(int i = 0; i < 4; i++) {
			if(neueAntworten[i].isBlank() || neueAntworten[i].isEmpty())
				return false;
		}
		
		//wenn keine richtige Antwort ausgewaehlt wurde
		if (neueRichtigeAntwort == -1)
			return false;
		else
			return true;
	}
}