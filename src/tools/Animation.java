package tools;

import java.awt.image.BufferedImage;

/**
 * Verwaltet eine Frame-basierte Sprite-Animation.
 * Unterstützt loopende und einmalige Animationen.
 */
public class Animation {

    private BufferedImage[] frames;  // alle Frames der Animation
    private int currentFrame = 0;    // Index des aktuellen Frames
    private int frameTimer = 0;      // zählt Ticks seit letztem Frame-Wechsel
    private int frameSpeed;          // Ticks pro Frame (niedriger = schneller)
    private boolean finished = false;// ob eine einmalige Animation abgeschlossen ist
    private boolean loop;            // ob die Animation wiederholt wird

    /**
     * Erstellt eine neue Animation.
     *
     * @param frames     Array der Frames in Reihenfolge
     * @param frameSpeed Ticks pro Frame
     * @param loop       true = wiederholen, false = einmalig abspielen
     */
    public Animation(BufferedImage[] frames, int frameSpeed, boolean loop) {
        this.frames = frames;
        this.frameSpeed = frameSpeed;
        this.loop = loop;
    }

    /**
     * Aktualisiert die Animation um einen Tick.
     * Bei einmaliger Animation wird nach dem letzten Frame gestoppt.
     */
    public void update() {
        if (finished) return; // einmalige Animation bereits abgeschlossen

        frameTimer++;
        if (frameTimer >= frameSpeed) {
            frameTimer = 0;
            if (currentFrame < frames.length - 1) {
                currentFrame++; // nächsten Frame laden
            } else {
                if (loop) {
                    currentFrame = 0; // zurück zum ersten Frame
                } else {
                    finished = true;  // Animation beenden
                }
            }
        }
    }
    public void reset() {
        currentFrame = 0;
        finished = false;
    }

    /** @return true wenn eine einmalige Animation den letzten Frame erreicht hat */
    public boolean isFinished() { return finished; }

    /** @return der aktuell angezeigte Frame */
    public BufferedImage getCurrentFrame() {
        return frames[currentFrame];
    }
}