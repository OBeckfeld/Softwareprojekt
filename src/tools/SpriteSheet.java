package tools;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.File;


public class SpriteSheet {

    private BufferedImage sheet;  // das gesamte Spritesheet-Bild
    private int frameWidth;       // Breite eines einzelnen Frames in Pixeln
    private int frameHeight;      // Höhe eines einzelnen Frames in Pixeln


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
    public SpriteSheet(BufferedImage sheet) {
        this.sheet = sheet;
        this.frameWidth = 0;
        this.frameHeight = 0;
    }


    public BufferedImage getFrame(int x, int y, int width, int height) {
        return sheet.getSubimage(x, y, width, height);
    }




    public BufferedImage getFrame(int row, int col) {
        return sheet.getSubimage(
                col * frameWidth,
                row * frameHeight,
                frameWidth,
                frameHeight
        );
    }
    public BufferedImage getSprite(int x, int y, int width, int height) {
        return sheet.getSubimage(x, y, width, height);
    }

    public BufferedImage getFrameMirrored(int row, int col) {
        BufferedImage frame = getFrame(row, col);
        BufferedImage mirrored = new BufferedImage(frameWidth, frameHeight, frame.getType());
        java.awt.Graphics2D g = mirrored.createGraphics();
        g.drawImage(frame, frameWidth, 0, -frameWidth, frameHeight, null);
        g.dispose();
        return mirrored;
    }
}