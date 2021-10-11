/**
 * Alle noetigen Daten werden geholt und geeignet gespeichert
 * @file QuizDaten.java
 * @brief Datenmanagement fuer das Quiz
 * @author Joel Maximilian Seidel
 */

package quizPacket;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class QuizDaten {
	//enthaelt die Kategorien als Textdatei
	private ArrayList<File> kategorienAlsFile;
	//enthaelt die Dateinamen der Kategorien
	private ArrayList<String> kategorienAlsString;
	//enthaelt alle Fragen einer Kategorie
	private ArrayList<String> fragen;
	//enthaelt alle Antworten einer Kategorie
	private ArrayList<String> antworten;
	//enthaelt alle Verweise auf richtige Antworten einer Frage einer Kategorie
	private ArrayList<String> richtigeAntworten;
	//enthaelt die Dateinamen alle fehlerhaften Dateien
	private ArrayList<String> fehlerhafteDateien;
	
	/**
	 * Konstruktor fuer die Klasse QuizDaten
	 * @fn QuizDaten()
	 */
	public QuizDaten() {
		kategorienAlsFile = new ArrayList<File>();
		kategorienAlsString = new ArrayList<String>();
		fragen = new ArrayList<String>();
		antworten = new ArrayList<String>();
		richtigeAntworten = new ArrayList<String>();
		fehlerhafteDateien = new ArrayList<String>();
	}
	
	/**
	 * Liest alle Kategorien ein und speichert die Dateien in "kategorienAlsFile".
	 * Zudem werden die Kategorienamen in "kategorienAlsString" gespeichert.
	 * @fn leseKategorien()
	 * @brief speichern der Kategorien
	 * @see resetKategorien
	 */
	public void leseKategorien() {
		resetKategorien();
		File ordner = new File("./src");
		File[] listeDerDateien = ordner.listFiles();
		
		for(int i = 0; i < listeDerDateien.length; i++) {
			if (listeDerDateien[i].isFile() && listeDerDateien[i].toString().contains(".txt")) {
				kategorienAlsFile.add(listeDerDateien[i]);
			}
		}
		for (int i = 0; i < kategorienAlsFile.size(); i++) {
			kategorienAlsString.add(kategorienAlsFile.get(i).getName());
		}
	}
	
	/**
	 * Setzt die Kategorien zurueck
	 * @fn resetKategorien()
	 */
	public void resetKategorien() {
		kategorienAlsFile.clear();
		kategorienAlsString.clear();
	}
	
	/**
	 * Loescht Kategorien aus dem Quiz und auf dem PC
	 * @fn loescheKategorie(String)
	 * @brief loescht eine Kategorie
	 * @param kategorie ist der Name der Kategorie die geloescht werden soll
	 * @pre kategorie muss ein Dateiname einer vorhandenen Datei sein
	 */
	public void loescheKategorie(String kategorie) {
		for (int i = 0; i < kategorienAlsFile.size(); i++) {
			if (kategorie.equals(kategorienAlsFile.get(i).getName()))
				kategorienAlsFile.get(i).delete();
		}
	}
	
	/**
	 * Eine Kategorie wird als Textdatei auf dem PC und somit dem Quiz hinzugefuegt
	 * @fn fuegeKategorieHinzu(String)
	 * @brief fuegt Kategorie hinzu
	 * @param kategorie ist der Name wie die Kategorie heissen soll
	 */
	public void fuegeKategorieHinzu(String kategorie) {
		File datei = new File("./src/" + kategorie + ".txt");
		try {
			datei.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Eine Kategorie-Dateien wird ausgelesen.
	 * Aus den ausgelesenen Daten werden die Fragen, die Antworten und die richtigen Antworten 
	 * als String  in den dafuer vorgesehenen Array-Listen gespeichert.
	 * @fn erstelleFragenUndAntworten(String)
	 * @brief fragen und Antworten einer Kategorie herauslesen
	 * @param kategorie ist der Kategoriename als String
	 * @pre der Kategoriename bzw. die Kategorie muss als Datei verfuegbar sein
	 */
	public void erstelleFragenUndAntworten(String kategorie) {
		fragen.clear();
		antworten.clear();
		richtigeAntworten.clear();
		File kategorieFile = new File("./src/" + kategorie);
		try {
			Scanner leser = new Scanner(kategorieFile);
			for (int i = 0; leser.hasNextLine(); i++) {
				String zeile = leser.nextLine();
				if (i % 7 == 0) {
					fragen.add(zeile);
				}
				else if (i % 7 >= 1 && i % 7 <= 4) {
					antworten.add(zeile);
				}
				else if(i % 7 == 5) {
					richtigeAntworten.add(zeile);
				}
			}
			leser.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Wenn eine Kategorie geaendert wurde werden alle Fragen, Antworten und richtigen Antworten
	 * neu in die vorhandene Datei geschrieben
	 * @fn speichereBearbeiteteKategorie(String)
	 * @brief aenderungen werden gespeichert
	 * @param kategorie ist der Kategoriename als String
	 * @pre der Kategoriename bzw. die Kategorie muss als Datei verfuegbar sein
	 * @throws IOException
	 */
	public void speichereBearbeiteteKategorie(String kategorie) throws IOException {
		new FileWriter("./src/" + kategorie, false).close();
		FileWriter writer = new FileWriter("./src/" + kategorie);
		for (int i = 0; i < fragen.size(); i++) {
			writer.write(fragen.get(i) + "\n");
			for (int j = 0; j < 4; j++) {
				writer.write(antworten.get(4 * i + j) + "\n");
			}
			writer.write(richtigeAntworten.get(i) + "\n\n");
		}
		writer.close();
	}
	
	/**
	 * Alle Dateien werden auf Fehler untersucht.
	 * @fn ueberpruefeDateien()
	 * @return wenn "true" zurueckgegeben wird sind alle Dateien in Ordnung
	 * @return wenn "false" zurueckgegeben wird ist ein Fehler in mindestens einer Datei aufgetreten
	 * @see leseKategorien()
	 * @see erstelleFragenUndAntworten(String)
	 */
	public boolean ueberpruefeDateien() {
		boolean richtig = true;
		leseKategorien();
		//zu wenig Kategorien
		if (kategorienAlsString.size() < 6)
			richtig = false;
		//alle Kategorien durcharbeiten
		for (int i = 0; i < kategorienAlsString.size(); i++) {
			erstelleFragenUndAntworten(kategorienAlsString.get(i));
			//eine Frage ist leer
			for (int j = 0; j < fragen.size(); j++) {
				if (fragen.get(j).isBlank()) {
					richtig = false;
					//if(!fehlerhafteDateien.contains(kategorienAlsString.get(i)))
						fehlerhafteDateien.add(kategorienAlsString.get(i));
				}
			}
			//eine Antwort ist leer
			for (int j = 0; j < antworten.size(); j++) {
				if (antworten.get(j).isBlank()) {
					richtig = false;
					//if(!fehlerhafteDateien.contains(kategorienAlsString.get(i)))
						fehlerhafteDateien.add(kategorienAlsString.get(i));
				}
			}
			//eine richtige Antwort ist leer
			for (int j = 0; j < richtigeAntworten.size(); j++) {
				if (richtigeAntworten.get(j).isBlank()) {
					richtig = false;
					//if(!fehlerhafteDateien.contains(kategorienAlsString.get(i)))
						fehlerhafteDateien.add(kategorienAlsString.get(i));
				}
			}
			//zu wenig fragen
			if (fragen.size() < 3) {
				//if(!fehlerhafteDateien.contains(kategorienAlsString.get(i)))
					fehlerhafteDateien.add(kategorienAlsString.get(i));
				richtig = false;
			}
			//Fragen- und Antwortenzahl stimmen nicht überein
			if (antworten.size() != fragen.size()*4) {
				//if(!fehlerhafteDateien.contains(kategorienAlsString.get(i)))
					fehlerhafteDateien.add(kategorienAlsString.get(i));
				richtig = false;
			}
			//Fragen- und richtige Antwortenzahl stimmten nicht überein
			if (richtigeAntworten.size() != fragen.size()) {
				//if(!fehlerhafteDateien.contains(kategorienAlsString.get(i)))
					fehlerhafteDateien.add(kategorienAlsString.get(i));
				richtig = false;
			}
		}
		return richtig;
	}

	public ArrayList<String> getFehlerhafteDateien() {
		return fehlerhafteDateien;
	}

	public void setFehlerhafteDateien(ArrayList<String> fehlerhafteDateien) {
		this.fehlerhafteDateien = fehlerhafteDateien;
	}

	public ArrayList<File> getKategorienAlsFile() {
		return kategorienAlsFile;
	}

	public void setKategorienAlsFile(ArrayList<File> kategorienAlsFile) {
		this.kategorienAlsFile = kategorienAlsFile;
	}

	public ArrayList<String> getKategorienAlsString() {
		return kategorienAlsString;
	}

	public void setKategorienAlsString(ArrayList<String> kategorienAlsString) {
		this.kategorienAlsString = kategorienAlsString;
	}

	public ArrayList<String> getFragen() {
		return fragen;
	}

	public void setFragen(ArrayList<String> fragen) {
		this.fragen = fragen;
	}

	public ArrayList<String> getAntworten() {
		return antworten;
	}

	public void setAntworten(ArrayList<String> antworten) {
		this.antworten = antworten;
	}

	public ArrayList<String> getRichtigeAntworten() {
		return richtigeAntworten;
	}

	public void setRichtigeAntworten(ArrayList<String> richtigeAntworten) {
		this.richtigeAntworten = richtigeAntworten;
	}
	
}
