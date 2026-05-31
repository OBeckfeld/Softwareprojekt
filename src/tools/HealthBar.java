package tools;

import entities.PlayerTypeEntity;

import java.awt.*;

import static main.Game.screenHeight;
import static main.Game.screenWidth;

public class HealthBar {
    private final int WIDTH = 100;
    private final int HEIGHT = 6;
    private final int Y_OFFSET = 12;
    private final PlayerTypeEntity entity;
    private boolean bossBar = false;

    /**
     * Erstellt eine neue HealthBar für eine bestimmte PlayerTypeEntity
     * und prüft, ob es sich bei dieser Entity um einen Boss handelt.
     */
    public HealthBar(PlayerTypeEntity entity) {

        this.entity = entity;

    }

    /**
     * Gibt zurück, ob diese HealthBar als BossBar dargestellt wird.
     */
    public boolean getBossBar(){
        return bossBar;
    }

    /**
     * Zeichnet die Lebensanzeige der Entity.
     * Bei normalen Entities wird eine kleine HealthBar über der Entity gezeichnet.
     * Bei Bossen wird stattdessen eine große BossBar am Bildschirmrand gezeichnet.
     */
    public void draw(Graphics2D g) {
        float healthPercent = (float) entity.getCurrentHealth() / entity.getMaxHealth();

        if (bossBar) {
            // BossBar am unteren Bildschirmrand
            // Background
            g.setColor(Color.DARK_GRAY);
            g.fillRect(10, screenHeight - 10, screenWidth - 20, 20);

            // Foreground
            g.setColor(Color.RED);
            g.fillRect(12, screenHeight - 12, (int) ((screenWidth - 20) * healthPercent), 16);

            // Border
            g.setColor(Color.BLACK);
            g.drawRect(10, screenHeight - 10, screenWidth - 20, 20);

        } else {
            // Normale HealthBar über der Entity
            int x = (int) entity.getX() + entity.getWidth() / 2 - WIDTH / 2;
            int y = (int) entity.getY() - HEIGHT - Y_OFFSET;

            // Background
            g.setColor(Color.DARK_GRAY);
            g.fillRect(x, y, WIDTH, HEIGHT);

            // Foreground
            g.setColor(getColor(healthPercent));
            g.fillRect(x, y, (int) (WIDTH * healthPercent), HEIGHT);

            // Border
            g.setColor(Color.BLACK);
            g.drawRect(x, y, WIDTH, HEIGHT);
        }
    }

    /**
     * Gibt abhängig vom aktuellen Lebensanteil eine passende Farbe zurück.
     * Grün steht für viel Leben, Orange für mittleres Leben und Rot für wenig Leben.
     */
    private Color getColor(float healthPercent){
        if(healthPercent > 0.5){return Color.GREEN;}
        else if(healthPercent > 0.25){return Color.ORANGE;}
        else{return Color.RED;}
    }
}
