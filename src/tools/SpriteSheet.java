package tools;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.File;

/**
 * Lädt ein Spritesheet und ermöglicht den Zugriff auf einzelne Frames
 * anhand von Zeile und Spalte.
 */
public class SpriteSheet {

    private BufferedImage sheet;  // das gesamte Spritesheet-Bild
    private int frameWidth;       // Breite eines einzelnen Frames in Pixeln
    private int frameHeight;      // Höhe eines einzelnen Frames in Pixeln

    /**
     * Lädt ein Spritesheet aus einer Datei.
     *
     * @param path        Dateipfad zum Spritesheet
     * @param frameWidth  Breite eines Frames in Pixeln
     * @param frameHeight Höhe eines Frames in Pixeln
     */
    public SpriteSheet(String path, int frameWidth, int frameHeight) {
        try {
            File file = new File(path);
            sheet = ImageIO.read(file);
            this.frameWidth = frameWidth;
            this.frameHeight = frameHeight;
        } catch (IOException e) {
            System.out.println("Sprite nicht gefunden: " + path);
        }
    }

    /**
     * Erstellt ein SpriteSheet aus einem bereits geladenen BufferedImage.
     *
     * @param sheet       das Spritesheet-Bild
     * @param frameWidth  Breite eines Frames in Pixeln
     * @param frameHeight Höhe eines Frames in Pixeln
     */
    public SpriteSheet(BufferedImage sheet, int frameWidth, int frameHeight) {
        this.sheet = sheet;
        this.frameWidth = frameWidth;
        this.frameHeight = frameHeight;
    }

    /**
     * Gibt einen einzelnen Frame aus dem Spritesheet zurück.
     *
     * @param row Zeile im Spritesheet (0-basiert)
     * @param col Spalte im Spritesheet (0-basiert)
     * @return der ausgeschnittene Frame als BufferedImage
     */
    public BufferedImage getFrame(int row, int col) {
        return sheet.getSubimage(
                col * frameWidth,
                row * frameHeight,
                frameWidth,
                frameHeight
        );
    }

    /**
     * Gibt einen horizontal gespiegelten Frame zurück.
     * Nützlich für Links-Animationen wenn nur Rechts-Frames vorhanden sind.
     *
     * @param row Zeile im Spritesheet (0-basiert)
     * @param col Spalte im Spritesheet (0-basiert)
     * @return der gespiegelte Frame als BufferedImage
     */
    public BufferedImage getFrameMirrored(int row, int col) {
        BufferedImage frame = getFrame(row, col);
        BufferedImage mirrored = new BufferedImage(frameWidth, frameHeight, frame.getType());
        java.awt.Graphics2D g = mirrored.createGraphics();
        g.drawImage(frame, frameWidth, 0, -frameWidth, frameHeight, null);
        g.dispose();
        return mirrored;
    }
}