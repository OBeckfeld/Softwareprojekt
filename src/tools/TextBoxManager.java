package tools;

import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;

public class TextBoxManager {
    // Array mit den Bildern der Schriftzeichen
    private BufferedImage[] characters;
    // Es wird eine CopyOnWriteArrayList verwendet, um ConcurrentModificationExceptions zu verhindern
    private final CopyOnWriteArrayList<TextBox> textBoxes;
    // Separate ArrayList für Menüs, um diese im Vordergrund einzublenden
    private final CopyOnWriteArrayList<TextBox> textBoxesMenu;
    private int imageWidth = 17;
    private int imageHeight = 19;

    public TextBoxManager() {
        characters = new BufferedImage[48];
        textBoxes = new CopyOnWriteArrayList<>();
        textBoxesMenu = new CopyOnWriteArrayList<>();
        loadCharacters();
    }

    public void addTextBox(TextBox box) {
        textBoxes.add(box);
    }

    public void removeTextBox(TextBox box) {
        textBoxes.remove(box);
    }

    public void addMenuTextBox(TextBox box) {
        textBoxesMenu.add(box);
    }

    public void removeMenuTextBox(TextBox box) {
        textBoxesMenu.remove(box);
    }

    public void clearMenuTextBoxes() {
        textBoxesMenu.clear();
    }

    /**
     * Methode, welche die Textboxen updated
     */
    public void updateBoxes() {
        // Liste mit den zu entfernenden Boxen wird erstellt
        ArrayList<TextBox> removeList = new ArrayList<>();
        // Alle regulären Boxen werden durchgegangen
        for (TextBox box : textBoxes) {
            // Falls Boxen ein Zeitlimit haben, wird die zeit, die sie bereits existieren geupdated
            if (box.getTimeToLive() != null) {
                box.increaseTimeAlive();
                // Falls eine Box abgelaufen ist, wird sie der removeList hinzugefügt
                if(box.getTimeAlive() >= box.getTimeToLive()) {
                    removeList.add(box);
                }
            }
            // Falls eine Box ohne zeitlimit als abgelaufen markiert wurde, wird sie der removeList hinzugefügt
            else if (!box.getAlive()) {
                removeList.add(box);
            }
        }
        // Alle Boxen aus Menüs werden durchgegangen
        for (TextBox box : textBoxesMenu) {
            // Falls Boxen ein Zeitlimit haben, wird die zeit, die sie bereits existieren geupdated
            if (box.getTimeToLive() != null) {
                box.increaseTimeAlive();
                // Falls eine Box abgelaufen ist, wird sie der removeList hinzugefügt
                if(box.getTimeAlive() >= box.getTimeToLive()) {
                    removeList.add(box);
                }
            }
            // Falls eine Box ohne Zeitlimit als abgelaufen markiert wurde, wird sie der removeList hinzugefügt
            else if (!box.getAlive()) {
                removeList.add(box);
            }
        }
        // Alle Elemente der removeList werden gelöscht
        for (TextBox box : removeList) {
            textBoxes.remove(box);
            removeList.remove(box);
        }
    }

    /**
     * Methode, welche alle regulären Boxen zeichnet
     * @param g Grafikobjekt, welches zum zeichnen benötigt wird
     */
    public void draw(Graphics g) {
        // Alle Boxen werden durchgegangen
        for (TextBox box : textBoxes) {
            // Falls die Box nicht existiert/abgelaufen ist, wird sie nicht gezeichnet
            if (box.getAlive() != null && !box.getAlive()) {
                continue;
            }
            // Box wird gezeichnet
            drawBox(g, box);
            // Indizes der Zeichen des Strings werden in Array gespeichert
            ArrayList<ArrayList<Integer>> imageIndexes = getTextIndexes(box);
            // Position des Textes wird berechnet
            int textPosX = box.getX() + box.getPadding() + box.getBorderThickness();
            int textPosY = box.getY() + box.getPadding() + box.getBorderThickness();
            // Alle Boxen werden durchgegangen und ihr Text wird gezeichnet
            for (int row = 0; row < imageIndexes.size(); row++) {
                for (int col = 0; col < imageIndexes.get(row).size(); col++) {
                    g.drawImage(
                        characters[imageIndexes.get(row).get(col)],
                        textPosX + col * imageWidth,
                        textPosY + row * imageHeight,
                        imageWidth,
                        imageHeight,
                        null
                    );
                }
            }
        }
    }

    /**
     * Methode, welche alle Boxen, die zu Menüs gehören, zeichnet
     * @param g Grafikobjekt, welches zum zeichnen benötigt wird
     */
    public void drawMenuTextBoxes(Graphics g) {
        // Alle Boxen werden durchgegangen
        for (TextBox box : textBoxesMenu) {
            // Falls die Box nicht existiert/abgelaufen ist, wird sie nicht gezeichnet
            if (box.getAlive() != null && !box.getAlive()) {
                continue;
            }
            // Box wird gezeichnet
            drawBox(g, box);
            // Indizes der Zeichen des Strings werden in Array gespeichert
            ArrayList<ArrayList<Integer>> imageIndexes = getTextIndexes(box);
            // Position des Textes wird berechnet
            int textPosX = box.getX() + box.getPadding() + box.getBorderThickness();
            int textPosY = box.getY() + box.getPadding() + box.getBorderThickness();
            // Alle Boxen werden durchgegangen und ihr Text wird gezeichnet
            for (int row = 0; row < imageIndexes.size(); row++) {
                for (int col = 0; col < imageIndexes.get(row).size(); col++) {
                    g.drawImage(
                        characters[imageIndexes.get(row).get(col)],
                        textPosX + col * imageWidth,
                        textPosY + row * imageHeight,
                        imageWidth,
                        imageHeight,
                        null
                    );
                }
            }
        }
    }

    // Lädt die Charaktere aus dem SpriteSheet und speichert sie im Array characters
    // Ordnung der Zeichen im Array:
    // 1. Alphabet:      A-Z;
    // 2. Umlaute: Ä;  Ö;  Ü;  ß;
    // 3. Zahlen:        0-9;
    // 4. Sonderzeichen: ".";  ",";  ":";  "!";  "?";  "(";  ")";  Leerzeichen
    // Index jedes Zeichens im Array:
    // A-Z: Position des Buchstaben im Alphabet minus 1
    // Ä-ß: Position in der Umlautreihenfolge (siehe oben) plus 25
    // 0-9: Wert der Zahl plus 30
    // "." - Leerzeichen: Position in der Sonderzeichenreihenfolge (siehe oben) plus 39
    public void loadCharacters() {
        try {
            // Bild wird gelesen und gespeichert
            BufferedImage sheet = ImageIO.read(getClass().getResource("/data/sprites/CharacterSpriteSheet.png"));
            // SpriteSheet wird mit dem Bild erstellt
            SpriteSheet ss = new SpriteSheet(sheet);

            // Alle Zeichen werden im Bild durchgegangen und in das Array characters[] gespeichert
            for (int i = 0; i < 7; i++) {
                for (int j = 0; j < 7; j++) {
                    int index = i * 7 + j;
                    if (index < characters.length) {
                        characters[index] = ss.getSprite(j * imageWidth, i * imageHeight, imageWidth, imageHeight);
                    }
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Methode, welche aus einem String die Indizes der Zeichen entnimmt
     * @param box TextBox, aus welcher die Informationen entnommen werden sollen
     * @return Zweidimensionale ArrayList, die die Indizes der Zeichen speichert
     */
    public ArrayList<ArrayList<Integer>> getTextIndexes(TextBox box) {
        // ArrayLists für Speicherung der Zeichen
        ArrayList<ArrayList<Integer>> imageIndexes = new ArrayList<>();
        ArrayList<Integer> imageIndexesRow = new ArrayList<>();
        //Text und dessen anfängliche Länge und Höhe werden gespeichert
        String text = box.getText();
        int textLength = 0;
        int textHeight = 0;
        //Solange noch Zeichen im Text verbleiben
        while (!text.isEmpty()) {
            // Indizes von Zeichen, die Worte beenden, werden gespeichert
            int[] wordEndIndexList = new int[7];
            wordEndIndexList[0] = text.indexOf(" ");
            wordEndIndexList[1] = text.indexOf(",");
            wordEndIndexList[2] = text.indexOf(".");
            wordEndIndexList[3] = text.indexOf("!");
            wordEndIndexList[4] = text.indexOf("?");
            wordEndIndexList[5] = text.indexOf(":");
            wordEndIndexList[6] = text.indexOf(")");
            // variable für den Index des Endes des ersten Wortes
            Integer wordEndIndex = null;
            // Speichert einen wordEndIndex, welcher nicht -1 ist, falls vorhanden
            for (int i : wordEndIndexList) {
                if(i != -1) {
                    wordEndIndex = i;
                    break;
                }
            }
            // Falls kein Index, der nicht -1 ist, vorliegt, wird der Index -1
            if (wordEndIndex == null) {
                wordEndIndex = -1;
            }
            // niedrigster wordEndIndex, welcher nicht -1 ist, wird gefunden
            for (int i : wordEndIndexList) {
                if (i != -1 && i < wordEndIndex) {
                    wordEndIndex = i;
                }
            }
            // Wort/Zeichenkette wird aus Text entnommen
            String word;
            if (wordEndIndex == 0) {
                // Falls das erste Zeichen ein Leerzeichen ist, wird es entfernt
                if (text.charAt(0) == ' ') {
                    text = text.substring(1);
                    continue;
                }
                // Falls das erste Zeichen ein anderes Trennzeichen ist, wird es einzeln gespeichert
                word = text.substring(0, 1);
            }
            // Falls keine Trennzeichen existieren, wird der gesamte Text in word gespeichert
            if (wordEndIndex == -1) {
                word = text;
            }
            // Ansonsten wird der text bis wordEndIndex + 1 entnommen
            else {
                word = text.substring(0, wordEndIndex + 1);
            }
            // entscheidet, was passiert, falls das Wort nicht in die Zeile passt
            if (textLength + word.length() * imageWidth > box.getTextWidthMax()) {
                //falls das letzte Zeichen ein Leerzeichen ist, wird geprüft, ob das Wort ohne leerzeichen passt
                if (word.endsWith(" ") && textLength + (word.length() - 1) * imageWidth <= box.getTextWidthMax()) {
                    word = word.substring(0, word.length() - 1);
                    // Textlänge wird erhöht
                    textLength += (word.length()) * imageWidth;
                }
                // Falls das Wort in die nächste Zeile passt
                else if (textHeight + imageHeight <= box.getTextHeightMax()) {
                    // textlänge wird zurückgesetzt, texthöhe wird erhöht
                    textLength = 0;
                    textHeight += imageHeight;
                    // aktuelle Reihe wird gespeichert und nächste Reihe wird hinzugefügt
                    imageIndexes.add(imageIndexesRow);
                    imageIndexesRow = new ArrayList<>();
                    continue;
                }
                // Falls das Wort nicht in die TextBox passt, wird eine Exception geworfen
                else {
                    throw new IllegalArgumentException("The word " + word + " is too big for the current text box size. WordEndIndex: " + wordEndIndex);
                }
            }
            // Falls das Wort passt, wird die textlänge erhöht
            else {
                textLength += word.length() * imageWidth;
            }
            // Falls das Wort den escape Code für ein enterZeichen enthält, wird eine neue zeile gestartet
            // (Verfahren wie oben)
            if (word.contains("(e)")) {
                textLength = 0;
                textHeight += imageHeight;
                imageIndexes.add(imageIndexesRow);
                imageIndexesRow = new ArrayList<>();
                text = text.substring(word.length());
                continue;
            }
            // Indizes der zeichen werden in der ArrayList imageIndexesRow gespeichert
            for (int i = 0; i < word.length(); i++) {
                imageIndexesRow.add(getImageIndex(word.charAt(i)));
            }
            // Das Wort wird aus dem text entfernt
            text = text.substring(word.length());
        }
        // falls die Reihe einen Inhalt hat, wird sie hinzugefügt
        if (!imageIndexesRow.isEmpty()) {
            imageIndexes.add(imageIndexesRow);
        }
        return imageIndexes;
    }

    /**
     * Die Box wird gezeichnet
     * @param g Grafikobjekt, welches zum zeichnen benötigt wird
     * @param box Die zu zeichnende Box
     */
    public void drawBox(Graphics g, TextBox box) {
        g.setColor(Color.BLACK);
        g.fillRect(box.getX(), box.getY(), box.getBoxWidth(), box.getBoxHeight());
        g.setColor(Color.WHITE);
        g.fillRect(box.getX() + box.getBorderThickness(), box.getY() + box.getBorderThickness(), box.getBoxWidth() - 2 * box.getBorderThickness(), box.getBoxHeight() - 2 * box.getBorderThickness());
    }

    /**
     * Gibt auf Angabe eines Zeichens den Index diesens wieder, falls ein solcher existiert
     */
    public int getImageIndex(char character) {
        switch(character) {
            case 'a':
                return 0;
            case 'b':
                return 1;
            case 'c':
                return 2;
            case 'd':
                return 3;
            case 'e':
                return 4;
            case 'f':
                return 5;
            case 'g':
                return 6;
            case 'h':
                return 7;
            case 'i':
                return 8;
            case 'j':
                return 9;
            case 'k':
                return 10;
            case 'l':
                return 11;
            case 'm':
                return 12;
            case 'n':
                return 13;
            case 'o':
                return 14;
            case 'p':
                return 15;
            case 'q':
                return 16;
            case 'r':
                return 17;
            case 's':
                return 18;
            case 't':
                return 19;
            case 'u':
                return 20;
            case 'v':
                return 21;
            case 'w':
                return 22;
            case 'x':
                return 23;
            case 'y':
                return 24;
            case 'z':
                return 25;
            case 'ä':
                return 26;
            case 'ö':
                return 27;
            case 'ü':
                return 28;
            case 'ß':
                return 29;
            case '1':
                return 30;
            case '2':
                return 31;
            case '3':
                return 32;
            case '4':
                return 33;
            case '5':
                return 34;
            case '6':
                return 35;
            case '7':
                return 36;
            case '8':
                return 37;
            case '9':
                return 38;
            case '0':
                return 39;
            case '.':
                return 40;
            case ',':
                return 41;
            case ':':
                return 42;
            case '!':
                return 43;
            case '?':
                return 44;
            case '(':
                return 45;
            case ')':
                return 46;
            case ' ':
                return 47;
            default:
                throw new IllegalArgumentException("Character " + character + " isn't available");
        }
    }
}