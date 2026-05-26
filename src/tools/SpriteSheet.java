package tools;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.File;

public class SpriteSheet {
    private BufferedImage sheet;
    private int frameWidth;
    private int frameHeight;

    public SpriteSheet(String path, int frameWidth, int frameHeight) {
        try {
            File file = new File(path); // direkt als Datei laden
            sheet = ImageIO.read(file);
            this.frameWidth = frameWidth;
            this.frameHeight = frameHeight;
        } catch (IOException e) {
            System.out.println("Sprite nicht gefunden: " + path);
        }
    }

    public BufferedImage getFrame(int row, int col) {
        return sheet.getSubimage(
                col * frameWidth,
                row * frameHeight,
                frameWidth,
                frameHeight
        );
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