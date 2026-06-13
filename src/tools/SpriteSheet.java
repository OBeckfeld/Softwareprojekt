package tools;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.File;


public class SpriteSheet {

    private BufferedImage sheet;  // das gesamte Spritesheet-Bild
    private int frameWidth;       // Breite eines einzelnen Frames in Pixeln
    private int frameHeight;      // Höhe eines einzelnen Frames in Pixeln


    /**
     * Erstellt ein neues SpriteSheet aus einem Dateipfad.
     * Lädt das Bild aus der angegebenen Datei und speichert die Breite und Höhe eines einzelnen Frames.
     */
    public SpriteSheet(String path, int frameWidth, int frameHeight) {
        try {
            File file = new File(path);
            sheet = ImageIO.read(file);

            if (sheet == null) {
                throw new IOException("ImageIO konnte das Bild nicht lesen: " + path);
            }

            this.frameWidth = frameWidth;
            this.frameHeight = frameHeight;
        } catch (IOException e) {
            throw new RuntimeException("Sprite nicht gefunden oder nicht lesbar: " + path, e);
        }
    }

    /**
     * Erstellt ein neues SpriteSheet aus einem bereits geladenen BufferedImage.
     * Setzt die Frame-Größe auf 0, da bei dieser Variante keine feste Frame-Größe übergeben wird.
     */
    public SpriteSheet(BufferedImage sheet) {
        this.sheet = sheet;
        this.frameWidth = 0;
        this.frameHeight = 0;
    }


    /**
     * Gibt einen Bildausschnitt anhand direkter Pixelkoordinaten und Größe zurück.
     */
    public BufferedImage getFrame(int x, int y, int width, int height) {
        return sheet.getSubimage(x, y, width, height);
    }




    /**
     * Gibt einen Frame anhand von Zeile und Spalte zurück.
     * Die genaue Position wird mithilfe der gespeicherten Frame-Breite und Frame-Höhe berechnet.
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
     * Gibt einen beliebigen Sprite-Ausschnitt anhand direkter Pixelkoordinaten und Größe zurück.
     */
    public BufferedImage getSprite(int x, int y, int width, int height) {
        return sheet.getSubimage(x, y, width, height);
    }

    /**
     * Gibt einen horizontal gespiegelten Frame anhand von Zeile und Spalte zurück.
     * Erst wird der normale Frame ausgelesen und danach in ein neues Bild gespiegelt gezeichnet.
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