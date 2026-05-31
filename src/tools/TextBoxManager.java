package tools;

import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;

public class TextBoxManager {
    private BufferedImage[] characters;
    // Es wird eine CopyOnWriteArrayList verwendet, um ConcurrentModificationExceptions zu verhindern
    private final CopyOnWriteArrayList<TextBox> textBoxes;
    private final CopyOnWriteArrayList<TextBox> textBoxesSkillTree;
    private int imageWidth = 17;
    private int imageHeight = 19;

    public TextBoxManager() {
        characters = new BufferedImage[48];
        textBoxes = new CopyOnWriteArrayList<>();
        textBoxesSkillTree = new CopyOnWriteArrayList<>();
        loadCharacters();
    }

    public void addTextBox(TextBox box) {
        textBoxes.add(box);
    }

    public void removeTextBox(TextBox box) {
        textBoxes.remove(box);
    }

    public void addSkillTreeTextBox(TextBox box) {
        textBoxesSkillTree.add(box);
    }

    public void removeSkillTreeTextBox(TextBox box) {
        textBoxesSkillTree.remove(box);
    }

    public void updateBoxes() {
        ArrayList<TextBox> removeList = new ArrayList<>();
        for (TextBox box : textBoxes) {
            if (box.getTimeToLive() != null) {
                box.increaseTimeAlive();
                if(box.getTimeAlive() >= box.getTimeToLive()) {
                    removeList.add(box);
                }
            }
            else if (!box.getAlive()) {
                removeList.add(box);
            }
        }
        for (TextBox box : textBoxesSkillTree) {
            if (box.getTimeToLive() != null) {
                box.increaseTimeAlive();
                if(box.getTimeAlive() >= box.getTimeToLive()) {
                    removeList.add(box);
                }
            }
            else if (!box.getAlive()) {
                removeList.add(box);
            }
        }
        for (TextBox box : removeList) {
            textBoxes.remove(box);
        }
    }

    // Methode, welche alle normalen Boxen zeichnet
    public void draw(Graphics g) {
        for (TextBox box : textBoxes) {
            if (box.getAlive() != null && !box.getAlive()) {
                continue;
            }
            drawBox(g, box);
            ArrayList<ArrayList<Integer>> imageIndexes = getTextIndexes(box);
            int textPosX = box.getX() + box.getPadding() + box.getBorderThickness();
            int textPosY = box.getY() + box.getPadding() + box.getBorderThickness();
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

    // Methode, welche alle Boxen des Skill Trees zeichnet
    public void drawSkillTreeBoxes(Graphics g) {
        for (TextBox box : textBoxesSkillTree) {
            if (box.getAlive() != null && !box.getAlive()) {
                continue;
            }
            drawBox(g, box);
            ArrayList<ArrayList<Integer>> imageIndexes = getTextIndexes(box);
            int textPosX = box.getX() + box.getPadding() + box.getBorderThickness();
            int textPosY = box.getY() + box.getPadding() + box.getBorderThickness();
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
            BufferedImage sheet = ImageIO.read(getClass().getResource("/data/sprites/CharacterSpriteSheet.png"));
            SpriteSheet ss = new SpriteSheet(sheet);

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
    
    public ArrayList<ArrayList<Integer>> getTextIndexes(TextBox box) {
        ArrayList<ArrayList<Integer>> imageIndexes = new ArrayList<>();
        ArrayList<Integer> imageIndexesRow = new ArrayList<>();
        int rowNumber = 0;
        String text = box.getText();
        int textLength = 0;
        int textHeight = 0;
        while (!text.isEmpty()) {
            int[] wordEndIndexList = new int[7];
            wordEndIndexList[0] = text.indexOf(" ");
            wordEndIndexList[1] = text.indexOf(",");
            wordEndIndexList[2] = text.indexOf(".");
            wordEndIndexList[3] = text.indexOf("!");
            wordEndIndexList[4] = text.indexOf("?");
            wordEndIndexList[5] = text.indexOf(":");
            wordEndIndexList[6] = text.indexOf(")");
            Integer wordEndIndex = null;
            for (int i : wordEndIndexList) {
                if(i != -1) {
                    wordEndIndex = i;
                }
            }
            if (wordEndIndex == null) {
                wordEndIndex = -1;
            }
            for (int i : wordEndIndexList) {
                if (i >= 0 && i < wordEndIndex) {
                    wordEndIndex = i;
                }
            }
            String word;
            if (wordEndIndex == 0) {
                if (text.charAt(0) == ' ') {
                text = text.substring(1);
                continue;
                }
                word = text.substring(0, 1);
            }
            if (wordEndIndex == -1) {
                word = text;
            }
            else {
                word = text.substring(0, wordEndIndex + 1);
            }
            if (textLength + word.length() * imageWidth > box.getTextWidthMax()) {
                if (word.endsWith(" ") && textLength + (word.length() - 1) * imageWidth <= box.getTextWidthMax()) {
                    word = word.substring(0, word.length() - 1);
                    textLength += (word.length()) * imageWidth;
                }
                else if (textHeight + imageHeight <= box.getTextHeightMax()) {
                    textLength = 0;
                    textHeight += imageHeight;
                    imageIndexes.add(imageIndexesRow);
                    imageIndexesRow = new ArrayList<>();
                    continue;
                }
                else {
                    throw new IllegalArgumentException("The word " + word + " is too big for the current text box size. WordEndIndex: " + wordEndIndex);
                }
            }
            else {
                textLength += word.length() * imageWidth;
            }
            if (word.contains("(e)")) {
                textLength = 0;
                textHeight += imageHeight;
                imageIndexes.add(imageIndexesRow);
                imageIndexesRow = new ArrayList<>();
                text = text.substring(word.length());
                continue;
            }
            for (int i = 0; i < word.length(); i++) {
                imageIndexesRow.add(getImageIndex(word.charAt(i)));
            }
            text = text.substring(word.length());
        }
        if (!imageIndexesRow.isEmpty()) {
            imageIndexes.add(imageIndexesRow);
        }
        return imageIndexes;
    }

    public void drawBox(Graphics g, TextBox box) {
        g.setColor(Color.BLACK);
        g.fillRect(box.getX(), box.getY(), box.getBoxWidth(), box.getBoxHeight());
        g.setColor(Color.WHITE);
        g.fillRect(box.getX() + box.getBorderThickness(), box.getY() + box.getBorderThickness(), box.getBoxWidth() - 2 * box.getBorderThickness(), box.getBoxHeight() - 2 * box.getBorderThickness());
    }

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