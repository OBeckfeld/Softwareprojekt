package tools;

import java.awt.image.BufferedImage;

public class Animation {
    private BufferedImage[] frames;
    private int currentFrame = 0;
    private int frameTimer = 0;
    private int frameSpeed;
    private boolean finished = false;
    private boolean loop; // ← neu

    public Animation(BufferedImage[] frames, int frameSpeed, boolean loop) {
        this.frames = frames;
        this.frameSpeed = frameSpeed;
        this.loop = loop;
    }

    public void update() {
        if (finished) return;
        frameTimer++;
        if (frameTimer >= frameSpeed) {
            frameTimer = 0;
            if (currentFrame < frames.length - 1) {
                currentFrame++;
            } else {
                if (loop) {
                    currentFrame = 0; // wiederholen
                } else {
                    finished = true; // stoppen
                }
            }
        }
    }

    public boolean isFinished() { return finished; }

    public BufferedImage getCurrentFrame() {
        return frames[currentFrame];
    }
}