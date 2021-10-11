/**	
 * @file QuizSpielen.java
 * @brief Alle Funktionen um das Quiz zu spielen
 * @author Joel Maximilian Seidel
 */

package quizPacket;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import javax.swing.ButtonGroup;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

public class QuizSpielen {
	
	//Die Punkte vom "echten" Spieler
	private Integer punkteMenschInt = 0;
	//Die Punkte vom simulierten Gegener
	private Integer punkteBotInt = 0;
	//Punkte von Spieler und Gegner
	private String punkte = "Ihre Punktzahl: " + punkteMenschInt.toString() + "\nPuktzahl Gegner: " + punkteBotInt.toString() + "\n\n";
	//Speichern der ausgewaehlten Kategorie
	private String kategorie = "";
	//Speichern aller gespielten Kategorien
	private ArrayList<String> gespielteKategorien = new ArrayList<String>();
	//Speichern aller gespielten Fragen
	private ArrayList<String> gespielteFragen = new ArrayList<String>();
	//Speichern aller Antworten vom "echten" Spieler
	private ArrayList<String> alleAntwortenMensch = new ArrayList<String>();
	//Speichern alle Antworten vom simulieren Gegner
	private ArrayList<String> alleAntwortenBot = new ArrayList<String>();
	//Alle richtigen Antworten auf die gestellten Fragen als String
	private ArrayList<String> alleRichtigenAntworten = new ArrayList<String>();
	//Speichern ob die Fragen vom "echten" Spieler richtig oder falsch beantwortet wurden
	private ArrayList<Boolean> richtigkeitAntwortenMensch = new ArrayList<Boolean>();
	//Speichern ob die Fragen vom simulierten Gegener richtig oder falsch beantwortet wurden
	private ArrayList<Boolean> richtigkeitAntwortenBot = new ArrayList<Boolean>();
	//Anzahl der gespielten Kategorien
	private int gespielteKategorienInt = 0;
	//Anzahl der gespielten Fragen
	private int gespielteFragenInt = 0;
	//Die Nummer der Frage die gerade gespielt wird
	private int fragenNummer = 0;
	//Die Nummer der Antwort die der "echte" Spieler ausgewaehlt hat
	private int ausgewaehlteAntwortMensch = 0;
	//Die Nummer der Antwort die der simulierte Gegner ausgewaehlt hat
	private int ausgewaehlteAntwortBot = 0;
	//Speichern ob die ausgwaehlte Antwort vom "echten" Spieler richtig oder falsch war
	private boolean richtigkeitMensch = false;
	//Speicher ob die ausgewaehlte Antwort vom simulierten Gegner richtig oder falsch war
	private boolean richtigkeitBot = false;
	//Speichern der Schwierigkeitsstufe
	private double schwierigkeit = 0.5;
	//Speichern aller nicht gueltigen Kategorien + aller gespielten Kategorien
	private ArrayList<String> ausgeschlosseneKategorien = new ArrayList<String>();
	private QuizDaten daten = new QuizDaten();
	
	/**
	 * Diese Funktion ueberprueft ob das Spiel ueberhaupt gespielt werden kann.
	 * Wenn dies nicht der Fall kann das Quiz nicht gespielt werden.
	 * @fn dateiueberpruefung()
	 * @brief Pruefen ob spielen moeglich ist
	 * @throws IOException
	 * @see uebrepreufeDateien() der Klasse QuizDaten
	 * @see QuizGUI.sichtbar() der Klasse QuizGUI
	 * @see schwierigkeit()
	 */
	public void dateiueberpruefung() throws IOException {
		reset();
		//wenn fehler auftreten
		if(!daten.ueberpruefeDateien()) {
			ausgeschlosseneKategorien = daten.getFehlerhafteDateien();
			String[] optionen = {"OK"};
			//Wenn nicht genuegend fehlerfreie Kategorien vorhanden sind
			if (daten.getKategorienAlsString().size() - daten.getFehlerhafteDateien().size() < 6) {
				Object[] content = {"Es sind zu viele Fehlerhaften Kategorien enthalten, sodass das Spiel nicht gespielt werden kann!",
						"Fehlerhafte Kategorien:", daten.getFehlerhafteDateien().toString()};
				JOptionPane.showOptionDialog(null, content, "Fehlerhafte Dateien", JOptionPane.OK_OPTION, JOptionPane.PLAIN_MESSAGE, 
						null, optionen, optionen[0]);
				QuizGUI.sichtbar();
				return;
			}
			//Wenn genug fehlerfreie Kategorien vorhanden sind
			else {
				Object[] content = {"Es sind Fehlerhafte Dateien vorhanden.", "Diese Dateien werden im Spiel nicht beruecksichtigt.",
						"Sie koennen das Spiel trotzdem ganz normal spielen.", "Die fehlerhaften Dateien sind:", 
						daten.getFehlerhafteDateien().toString()};
				JOptionPane.showOptionDialog(null, content, "Fehlerhafte Dateien", JOptionPane.OK_OPTION, JOptionPane.PLAIN_MESSAGE, 
						null, optionen, optionen[0]);
				schwierigkeit();
			}
		}
		else
			schwierigkeit();
	}
	
	/**
	 * @fn schwierigkeit()
	 * @brief die Schwierigkeit des Gegners wird ausgewaehlt
	 * @throws IOException
	 * @see menuVorbereitung()
	 * @see QuizGUI.sichtbar() der Klasse QuizGUI
	 */
	public void schwierigkeit() throws IOException {
		int weiter = 1;
		String[] optionen = {"OK"};
		final int leichtesteStufe = 0;
		final int schwersteStufe = 100;
		final int standard = 50;
		
		JSlider schwierigkeitsstufe = new JSlider(JSlider.HORIZONTAL, leichtesteStufe, schwersteStufe, standard);
		schwierigkeitsstufe.setMajorTickSpacing(25);
		schwierigkeitsstufe.setMinorTickSpacing(5);
		schwierigkeitsstufe.setPaintTicks(true);
		schwierigkeitsstufe.setPaintLabels(true);
		
		Object[] content = {"Bitte waehlen sie ihr Schwierigkeitsstufe:", schwierigkeitsstufe}; 
		weiter = JOptionPane.showOptionDialog(null, content, "Schwierigkeitsstufe", JOptionPane.OK_OPTION, JOptionPane.PLAIN_MESSAGE,
				null, optionen, optionen[0]);
		double ausgewaehlt = schwierigkeitsstufe.getValue();
		schwierigkeit = ausgewaehlt / 100;
		
		//"OK" wurde gedrueckt
		if (weiter == 0)
			menuVorbereitung();

		else
			QuizGUI.sichtbar();	
	}
	
	/**
	 * Hier wird das Quiz-Spiel beendet, ausserdem werden hier immmer noetige Schritte getaetigt um das Kategorie-Menue anzeigen zu koennen
	 * @fn menuVorbereitung()
	 * @brief Vorbereitung fuer das Kategorie-waehl-Menue
	 * @throws IOException
	 * @see endstand()
	 * @see QuizGUI.sichtbar()
	 * @see leseKategorien() der Klasse QuizDaten
	 * @see quizMenu(String[])
	 */
	public void menuVorbereitung() throws IOException {
		gespielteKategorienInt++;
		
		//wenn 6 Kategorien gespielt wurden
		if (gespielteKategorienInt > 6) {
			endstand();
			QuizGUI.sichtbar();
			return;
		}
		
		//entfernen der schon gespielten Kategorien
		daten.leseKategorien();
		ArrayList<String> temp = daten.getKategorienAlsString();
		for (int i = 0; i < ausgeschlosseneKategorien.size(); i++) {
			if (temp.contains(ausgeschlosseneKategorien.get(i)))
				temp.remove(ausgeschlosseneKategorien.get(i));
		}
		
		String[] kategorien = new String[temp.size()];
		for (int i = 0; i < temp.size(); i++) {
			kategorien[i] = temp.get(i).substring(0, temp.get(i).length() - 4);
		}
		quizMenu(kategorien);
	}
	
	/**
	 * Hier waehlt entweder der echte Spieler oder simulierte Gegner die Kategorie aus.
	 * @fn quizMenu(String[])
	 * @brief auswaehlen der zu spielenden Kategorie
	 * @param kategorien, Array in der alle noch verfuegbaren Kategorien stehen
	 * @pre String-Array, die Kategorien muessen fehlerfrei als Dateien exisitieren
	 * @throws IOException
	 * @see menuNachbereitung()
	 * @see beenden(int, String[])
	 */
	public void quizMenu(String[] kategorien) throws IOException {
		Random zufallsGenerator = new Random();
		int zufaelligeKategorie;
		int weiter = 1;
		String[] auswahlMoeglichkeiten = {"Kategorie waehlen"};
		String[] optionen = {"OK"};
		
		JComboBox<String> kategorieAuswahl = new JComboBox<String>(kategorien);
		Object[] auswahlDerKategorie = {punkte, "Bitte waehlen sie ihre Kategorie:", kategorieAuswahl};
		
		//Der "echte Spieler waehlt sich eine Kategorie aus
		if (gespielteKategorienInt % 2 == 1) {
			weiter = JOptionPane.showOptionDialog(null, auswahlDerKategorie, "Kategorieauswahl", JOptionPane.OK_OPTION, JOptionPane.PLAIN_MESSAGE,
					null, auswahlMoeglichkeiten, auswahlMoeglichkeiten[0]);
			//wenn "Kategorie waehlen" gedrueckt wurde
			if (weiter == 0) {
				kategorie = kategorieAuswahl.getSelectedItem().toString();
				//3 mal fuer Endauswertung
				gespielteKategorien.add(kategorie);
				gespielteKategorien.add(kategorie);
				gespielteKategorien.add(kategorie);
				kategorie += ".txt";
				menuNachbereitung();
			}
			else {
				beenden(0, kategorien);
			}
		}
		
		//der simulierte Gegener waehlt eine Kategorie aus
		else {
			zufaelligeKategorie = zufallsGenerator.nextInt(kategorien.length);
			kategorie = kategorien[zufaelligeKategorie];
			gespielteKategorien.add(kategorie);
			gespielteKategorien.add(kategorie);
			gespielteKategorien.add(kategorie);
			kategorie += ".txt";
			String nachricht = "Der Gegner hat die Kategorie " + kategorie.substring(0, kategorie.length() - 4) + " gewaehlt.";
			weiter = JOptionPane.showOptionDialog(null, nachricht, "Vom Gegner gewaehlte Kategorie", JOptionPane.OK_OPTION, 
					JOptionPane.PLAIN_MESSAGE, null, optionen, optionen[0]);
			if (weiter != 0)
				beenden(1, null);
			else
				menuNachbereitung();
		}
	}
	
	/**
	 * @fn menuNachbereitung()
	 * @brief kategorie abschliessen auswaehlen
	 * @throws IOException
	 * @see erstelleFragenUndAntworten(String) der Klasse QuizDaten
	 * @see fragenAuswahl()
	 */
	public void menuNachbereitung() throws IOException {
		daten.erstelleFragenUndAntworten(kategorie);
		ausgeschlosseneKategorien.add(kategorie);
		gespielteFragenInt = 0;
		fragenAuswahl();
	}
	
	/**
	 * @fn kategorieVorbereitung()
	 * @brief waehlt zu beantwortende Frage aus
	 * @throws IOException
	 * @see frageSpielen()
	 */
	public void fragenAuswahl() throws IOException {
		Random zufallsGenerator = new Random();
		int anzahlUebrigerFragen = daten.getFragen().size();
		fragenNummer = zufallsGenerator.nextInt(anzahlUebrigerFragen);
		gespielteFragen.add(daten.getFragen().get(fragenNummer));
		frageSpielen();
	}
	
	/**
	 * @fn kategorieSpielen()
	 * @brief beantworten der gestellten Frage
	 * @throws IOException
	 * @see richtigeAntwort(int)
	 * @see pruefung()
	 * @see punkteAddieren()
	 * @see beenden(int, String[])
	 */
	public void frageSpielen() throws IOException {
		Random zufallsGenerator = new Random();
		String[] optionen = {"naechste Frage"};
		ButtonGroup antwortGruppe = new ButtonGroup();
		JRadioButton antwort1 = new JRadioButton(daten.getAntworten().get(4 * fragenNummer));
		JRadioButton antwort2 = new JRadioButton(daten.getAntworten().get(4 * fragenNummer + 1));
		JRadioButton antwort3 = new JRadioButton(daten.getAntworten().get(4 * fragenNummer + 2));
		JRadioButton antwort4 = new JRadioButton(daten.getAntworten().get(4 * fragenNummer + 3));
		
		antwortGruppe.add(antwort1);
		antwortGruppe.add(antwort2);
		antwortGruppe.add(antwort3);
		antwortGruppe.add(antwort4);
		
		JRadioButton[] antworten = {antwort1, antwort2, antwort3, antwort4};
		List<Object> antwortenListe = Arrays.asList(antworten);
		Collections.shuffle(antwortenListe);
		antwortenListe.toArray(antworten);
		Object[] antwortAuswahl = {punkte, daten.getFragen().get(fragenNummer), antworten[0], antworten[1], antworten[2], antworten[3]};
		int weiter = JOptionPane.showOptionDialog(null, antwortAuswahl, "Frage", JOptionPane.OK_OPTION, JOptionPane.PLAIN_MESSAGE, null, optionen, optionen[0]);
		
		double zufallsZahlBot = zufallsGenerator.nextDouble();
		
		if(antwort1.isSelected())	
			ausgewaehlteAntwortMensch = 1;
		else if(antwort2.isSelected())
			ausgewaehlteAntwortMensch = 2;
		else if(antwort3.isSelected())
			ausgewaehlteAntwortMensch = 3;
		else if(antwort4.isSelected())
			ausgewaehlteAntwortMensch = 4;
		//wenn nichts ausgewaehlt ist
		else
			ausgewaehlteAntwortMensch = 5;
		
		//wenn nichts ausgewaehlt ist
		if (ausgewaehlteAntwortMensch == 5)
			alleAntwortenMensch.add(" ");
		else
			alleAntwortenMensch.add(daten.getAntworten().get(fragenNummer * 4 + (ausgewaehlteAntwortMensch - 1)));
		
		if(zufallsZahlBot <= schwierigkeit)
			richtigkeitBot = true;
		else
			richtigkeitBot = false;
		
		if (richtigkeitBot)
			ausgewaehlteAntwortBot = richtigeAntwort(fragenNummer);
		else {
			do {
				ausgewaehlteAntwortBot = zufallsGenerator.nextInt(4);
			}
			while (ausgewaehlteAntwortBot == richtigeAntwort(fragenNummer));
		}
		richtigkeitMensch = pruefung();
		
		alleAntwortenBot.add(daten.getAntworten().get(fragenNummer * 4 + (ausgewaehlteAntwortBot)));
		alleRichtigenAntworten.add(daten.getAntworten().get(fragenNummer * 4 + richtigeAntwort(fragenNummer)));
		richtigkeitAntwortenMensch.add(richtigkeitMensch);
		richtigkeitAntwortenBot.add(richtigkeitBot);
		
		if (weiter == 0)
			punkteAddieren();
		else
			beenden(2, null);
	}
	
	/**
	 * @fn informationVorbereitung()
	 * @brief erhoeht Punkte
	 * @throws IOException
	 * @see aktualisierePunkte()
	 * @see information()
	 */
	public void punkteAddieren() throws IOException {
		if (richtigkeitMensch) {
			punkteMenschInt++;
			aktualisierePunkte();
		}
		if (richtigkeitBot) {
			punkteBotInt++;
			aktualisierePunkte();
		}
		information();
	}
	
	/**
	 * Diese Funktion gibt die Punkte, die Information wer die Frage richtig bzw. falsch beantwortet hat und was die
	 * richtige Antwort gewesen waere aus.
	 * @fn information()
	 * @brief gibt Punkte aus
	 * @throws IOException
	 * @see frageLoeschen()
	 * @see fragenAuswahl()
	 * @see menuVorbereitung()
	 * @see beenden(int, String[])
	 */
	public void information() throws IOException {
		int weiter = 1;
		String[] optionen = {"OK"};
		String beideRichtig = "Glueckwunsch Sie und ihr Gegner haben die Frage";
		String menschRichtigBotFalsch = "Glueckwunsch Sie haben die Frage";
		String botRichtigMenschFalsch = "Schade Sie haben die Frage";
		String beideFalsch = "Schade Sie und ihr Gegner haben die Frage";
		
		String beideRichtig2 = "richtig beantwortet.";
		String menschRichtigBotFalsch2 = "richtig beantwortet, ihr Gegner lag falsch.";
		String botRichtigMenschFalsch2 = "falsch beantwortet, ihr Gegner lag richtig";
		String beideFalsch2 = "falsch beantwortet";
				
		String beideRichtig3 = "Ihre richtigen Antworten lauteten:";
		String menschRichtigeAntwort = "Ihre richtige Antwort war:";
		String botRichtigeAntwort = "Die richtige Antwort des Gegners lautete:";
		String menschFalscheAntwort = "Ihre falsche Antwort lautete:";
		String botFalscheAntwort = "Die Falsche Antwort ihres Gegners lautete:";
		String richtigeAntwort = "Diese Antwort waere die richtige gewesen:";
		
		String nichtBeantwortet = "Schade, Sie haben die Frage";
		String nichtBeantwortet2 = "leider nicht beantwortet.";
		
		
		Object[] nichtsAusgewaehlt = new Object[8];
		
		//"echter" Spieler hat nichts ausgewaehlt, der simulierte Spieler lag richtig
		if (ausgewaehlteAntwortMensch == 5 && richtigkeitBot) {
			Object[] temp = {punkte, nichtBeantwortet, daten.getFragen().get(fragenNummer), nichtBeantwortet2,
					botRichtigeAntwort, daten.getAntworten().get(4 * fragenNummer + ausgewaehlteAntwortBot)};
			nichtsAusgewaehlt = temp;
			weiter = JOptionPane.showOptionDialog(null, nichtsAusgewaehlt, "nichts ausgewaehlt", JOptionPane.OK_OPTION, 
					JOptionPane.PLAIN_MESSAGE, null, optionen, optionen[0]);
		}
		//"echter" Spieler hat nichts ausgewaehlt, der simulierte Spieler lag falsch
		else if (ausgewaehlteAntwortMensch == 5 && !richtigkeitBot) {
			Object[] temp = {punkte, nichtBeantwortet, daten.getFragen().get(fragenNummer), nichtBeantwortet2,
					botFalscheAntwort, daten.getAntworten().get(4 * fragenNummer + ausgewaehlteAntwortBot)};
			nichtsAusgewaehlt = temp;
			weiter = JOptionPane.showOptionDialog(null, nichtsAusgewaehlt, "nichts ausgewaehlt", JOptionPane.OK_OPTION,
					JOptionPane.PLAIN_MESSAGE, null, optionen, optionen[0]);
		}
		//der "echte" Spieler hat eine Antwort ausgewaehlt
		if (ausgewaehlteAntwortMensch != 5) {
			//beide Spieler richtig
			if (richtigkeitMensch && richtigkeitBot) {
				Object[] rightRight = {punkte, beideRichtig, daten.getFragen().get(fragenNummer), beideRichtig2, beideRichtig3,
						daten.getAntworten().get(4 * fragenNummer + richtigeAntwort(fragenNummer))};
				weiter = JOptionPane.showOptionDialog(null, rightRight, "beide richtig", JOptionPane.OK_OPTION, 
						JOptionPane.PLAIN_MESSAGE, null, optionen, optionen[0]);
			}
			//"echter" Spieler richtig, der simulierte Spieler lag falsch
			else if (richtigkeitMensch && !richtigkeitBot) {
				Object[] rightWrong = {punkte, menschRichtigBotFalsch, daten.getFragen().get(fragenNummer), menschRichtigBotFalsch2,
						menschRichtigeAntwort, daten.getAntworten().get(4 * fragenNummer + ausgewaehlteAntwortMensch - 1),
						botFalscheAntwort, daten.getAntworten().get(4 * fragenNummer + ausgewaehlteAntwortBot)};
				weiter = JOptionPane.showOptionDialog(null, rightWrong, "Gegner falsch", JOptionPane.OK_OPTION, 
						JOptionPane.PLAIN_MESSAGE, null, optionen, optionen[0]);
			}
			//"echter" Spieler lag falsch, der simulierte Spieler lag richtig
			else if (!richtigkeitMensch && richtigkeitBot) {
				Object[] wrongRight = {punkte, botRichtigMenschFalsch, daten.getFragen().get(fragenNummer), botRichtigMenschFalsch2,
						menschFalscheAntwort, daten.getAntworten().get(4 * fragenNummer + ausgewaehlteAntwortMensch - 1),
						botRichtigeAntwort, daten.getAntworten().get(4 * fragenNummer + ausgewaehlteAntwortBot)};
				weiter = JOptionPane.showOptionDialog(null, wrongRight, "falsche Antwort", JOptionPane.OK_OPTION, 
						JOptionPane.PLAIN_MESSAGE, null, optionen, optionen[0]);
			}
			//beide Spieler lagen richtig
			else {
				Object[] wrongWrong = {punkte, beideFalsch, daten.getFragen().get(fragenNummer), beideFalsch2,
						menschFalscheAntwort, daten.getAntworten().get(4 * fragenNummer + ausgewaehlteAntwortMensch - 1),
						botFalscheAntwort, daten.getAntworten().get(4 * fragenNummer + ausgewaehlteAntwortBot),
						richtigeAntwort, daten.getAntworten().get(4 * fragenNummer + richtigeAntwort(fragenNummer))};
				weiter = JOptionPane.showOptionDialog(null, wrongWrong, "beide Antworten falsch", JOptionPane.OK_OPTION, 
						JOptionPane.PLAIN_MESSAGE, null, optionen, optionen[0]);
			}
		}
		
		gespielteFragenInt++;
		
		//wenn unter 3 Fragen der Kategorie gespielt wurden
		if (gespielteFragenInt < 3 && weiter == 0) {
			frageLoeschen();
			fragenAuswahl();
		}
		//3 Fragen der Kategorie wurden gespielt
		else if (weiter == 0 && gespielteFragenInt >= 3) {
			menuVorbereitung();
		}
		else {
			gespielteFragenInt--;
			beenden(3, null);
		}
	}
	
	/**
	 * @fn pruefung()
	 * @brief ueberprueft ob die ausgewaehlte Antwort richtig oder falsch war
	 * @return true, wenn die ausgewaehlte Antwort richtig war
	 * @return false, wenn die ausgewaehlte Antwort falsch war
	 */
	public boolean pruefung() {
		Integer aA = ausgewaehlteAntwortMensch;
		String temp;
		if(ausgewaehlteAntwortMensch == 1)
			temp = "A";
		else if (ausgewaehlteAntwortMensch == 2)
			temp = "B";
		else if (ausgewaehlteAntwortMensch == 3)
			temp = "C";
		else if (ausgewaehlteAntwortMensch == 4)
			temp = "D";
		else
			temp = "falsch";
		
		if (daten.getRichtigeAntworten().get(fragenNummer).equals(aA.toString()) || daten.getRichtigeAntworten().get(fragenNummer).equals(temp))
			return true;
		else
			return false;
	}
	
	/**
	 * @fn richtigeAntwort(int)
	 * @param fragenNummer
	 * @return int, die Zahl welche Antwort richtig ist
	 */
	public int richtigeAntwort(int fragenNummer) {
		if(daten.getRichtigeAntworten().get(fragenNummer).equals("1") || daten.getRichtigeAntworten().get(fragenNummer).equals("A"))
			return 0;
		else if (daten.getRichtigeAntworten().get(fragenNummer).equals("2") || daten.getRichtigeAntworten().get(fragenNummer).equals("B"))
			return 1;
		else if (daten.getRichtigeAntworten().get(fragenNummer).equals("3") || daten.getRichtigeAntworten().get(fragenNummer).equals("C"))
			return 2;
		else
			return 3;
	}
	
	/**
	 * @fn aktualisierePunkte()
	 * @brief aktualisiert den String "punkte" auf den aktuellen Punktestand
	 */
	public void aktualisierePunkte() {
		punkte = "Ihre Punktzahl: " + punkteMenschInt.toString() + "\nPuktzahl Gegner: " + punkteBotInt.toString() + "\n\n";
	}
	
	/**
	 * @fn frageLoeschen()
	 * @brief Loescht die Frage damit sie nicht noch einmal gespielt werden kann
	 * @throws IOException
	 */
	public void frageLoeschen() throws IOException {
		//temporaere ArrayList zum Frage loeschen
		ArrayList<String> temp = new ArrayList<String>();
		temp = daten.getFragen();
		temp.remove(fragenNummer);
		daten.setFragen(temp);
		//temporaere ArrayList zum Antworten loeschen
		temp = daten.getAntworten();
		for (int i = 3; i >= 0; i--) {
			temp.remove(4 * fragenNummer + i);
		}
		daten.setAntworten(temp);
		//temporaere ArrayList zum loeschen der richtigen Antwort
		temp = daten.getRichtigeAntworten();
		temp.remove(fragenNummer);
		daten.setRichtigeAntworten(temp);
	}
	
	/**
	 * @fn reset()
	 * @brief setzt alles zurueck
	 * @see aktualisierePunkte()
	 */
	public void reset() {
		punkteMenschInt = 0;
		punkteBotInt = 0;
		aktualisierePunkte();
		gespielteKategorienInt = 0;
		ausgeschlosseneKategorien.clear();
	}
	
	/**
	 * @fn beenden(int, String[])
	 * @brief sicherstellen ob das Quiz wirklich beendet werden soll
	 * @param zurueck, angabe wohin zurueck gesprungen werden soll
	 * @pre zurueck muss 0, 1, 2 oder 3 sein
	 * @param kategorien, wird nur fuer zurueck = 0 benoetigt, enthaelt alle Kategorien
	 * @throws IOException
	 * @see QuizGUI.sichtbar() der Klasse QuizGUI
	 * @see quizMenu(String[])
	 * @see menuNachbereitung()
	 * @see frageSpielen()
	 * @see information()
	 */
	public void beenden(int zurueck, String[] kategorien) throws IOException {
		String[] optionen = {"JA", "NEIN"};
		String content = "Wollen sie das Spiel wirklich beenden?\nDer Spielforschritt geht komplett verloren!";
		int n = JOptionPane.showOptionDialog(null, content, "wirklich beenden?", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, 
				null, optionen, null);
		if (n == 0) {
			reset();
			QuizGUI.sichtbar();
			return;
		}
		else {
			switch (zurueck) {
			case 0:
				quizMenu(kategorien);
				break;
			case 1:
				menuNachbereitung();
				break;
			case 2:
				frageSpielen();
				break;
			case 3:
				information();
			}
		}
	}
	
	/**
	 * @fn endstand()
	 * @brief ausgeben des Enstandes
	 */
	public void endstand() {
		//Nachricht über Ergebnis
		String nachricht = "";
		if (punkteMenschInt > punkteBotInt) {
			nachricht = "Herzlichen Glueckwunsch sie haben gewonnen!";
		}
		else if (punkteMenschInt == punkteBotInt) {
			nachricht = "Sie und ihr Gegener haben gleich viele Punkte.";
		}
		else
			nachricht = "Sie haben leider gegen ihren Gegener verloren!";
		//Panel für Tabelle
		JPanel panel = new JPanel();
		panel.setOpaque(true);
		panel.setLayout(new BorderLayout());
		String[] ueberschriften = {"Kategorie", "Frage", "Ihre Antwort", "Antwort des Gegeners", "Richtige Antwort"};
		Object[][] tabellenDaten = new Object[18][5];
		//Daten für Tabelle befuellen
		for (int i = 0; i < 18; i++) {
			for (int j = 0; j < 5; j++) {
				switch(j) {
				case 0:
					tabellenDaten[i][0] = gespielteKategorien.get(i);
					break;
				case 1:
					tabellenDaten[i][1] = gespielteFragen.get(i);
					break;
				case 2:
					tabellenDaten[i][2] = alleAntwortenMensch.get(i);
					break;
				case 3:
					tabellenDaten[i][3] = alleAntwortenBot.get(i);
					break;
				case 4:
					tabellenDaten[i][4] = alleRichtigenAntworten.get(i);
					break;
				}
			}
		}
		
		//Tabelle erstellen und darstellen
		JTable tabelle = new JTable(tabellenDaten, ueberschriften);
		tabelle.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		for (int spalte = 0; spalte < 5; spalte++) {
			int breite = 150;
			for (int zeile = 0; zeile < 18; zeile++) {
				TableCellRenderer render = tabelle.getCellRenderer(zeile, spalte);
				Component comp = tabelle.prepareRenderer(render, zeile, spalte);
				breite = Math.max(comp.getPreferredSize().width + 20, breite);
			}
			 tabelle.getColumnModel().getColumn(spalte).setPreferredWidth(breite);
		}
		tabelle.setEnabled(false);
		JScrollPane scroll = new JScrollPane(tabelle);
		scroll.setPreferredSize(new Dimension(750, 250));
		panel.add(scroll, BorderLayout.CENTER);
		String[] optionen = {"Quiz beenden"};
		Object[] content = {nachricht, punkte, panel};
		JOptionPane.showOptionDialog(null, content, "Endstand", JOptionPane.OK_OPTION, JOptionPane.PLAIN_MESSAGE, null, optionen, optionen[0]);
	}

	
	public int getGespielteKategorien() {
		return gespielteKategorienInt;
	}

	public void setGespielteKategorien(int gespielteKategorien) {
		this.gespielteKategorienInt = gespielteKategorien;
	}

	public Integer getPunkteMenschInt() {
		return punkteMenschInt;
	}

	public void setPunkteMenschInt(Integer punkteMenschInt) {
		this.punkteMenschInt = punkteMenschInt;
	}

	public Integer getPunkteBotInt() {
		return punkteBotInt;
	}

	public void setPunkteBotInt(Integer punkteBotInt) {
		this.punkteBotInt = punkteBotInt;
	}
}
