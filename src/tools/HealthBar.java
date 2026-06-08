package tools;

import entities.PlayerTypeEntity;
import entities.enemies.Boss;
import main.Game;

import java.awt.*;
import java.awt.geom.AffineTransform;

import static main.Game.screenHeight;
import static main.Game.screenWidth;

public class HealthBar {
    private final int WIDTH = 100;
    private final int HEIGHT = 6;
    private final int Y_OFFSET = 12;
    private final PlayerTypeEntity entity;
    private boolean bossBar = false;

    /**
     * Erstellt eine neue HealthBar für die übergebene Entity.
     * Prüft dabei, ob es sich bei der Entity um einen Boss handelt.
     */
    public HealthBar(PlayerTypeEntity entity) {

        this.entity = entity;
        if(entity instanceof Boss){
            bossBar = true;
        }
    }

    /**
     * Gibt zurück, ob diese HealthBar als Boss-Leiste dargestellt wird.
     */
    public boolean getBossBar(){
        return bossBar;
    }

    /**
     * Zeichnet die Lebensanzeige der Entity.
     * Normale Entities erhalten eine kleine HealthBar über sich,
     * Boss-Entities erhalten eine große BossBar am unteren Bildschirmrand.
     */
    public void draw(Graphics2D g){

        float healthPercent = (float) entity.getCurrentHealth() / entity.getMaxHealth();
        int x = (int) entity.getX() + entity.getWidth() / 2 - WIDTH / 2;
        int y = (int) entity.getY() - HEIGHT - Y_OFFSET;


        if(!(entity instanceof Boss)) {
            //Background
            g.setColor(Color.DARK_GRAY);
            g.fillRect(x, y, WIDTH, HEIGHT);

            //Foreground
            g.setColor(getColor(healthPercent));
            g.fillRect(x, y, (int) (WIDTH * healthPercent), HEIGHT);

            //Border
            g.setColor(Color.BLACK);
            g.drawRect(x, y, WIDTH, HEIGHT);
        }
        else{
            AffineTransform old = g.getTransform();
            g.setTransform(new AffineTransform());
            //Background
            g.setColor(Color.DARK_GRAY);
            g.fillRect(10, screenHeight-10, screenWidth , 20);

            //Foreground
            g.setColor(Color.RED);
            g.fillRect(12, screenHeight-12, (int) (screenWidth * healthPercent), 16);

            //Border
            g.setColor(Color.BLACK);
            g.drawRect(10, screenHeight-10, screenWidth, 20);
            g.setTransform(old);
        }
    }

    /**
     * Bestimmt die Farbe der HealthBar anhand des aktuellen Lebensanteils.
     * Grün steht für viel Leben, Orange für mittleres Leben und Rot für wenig Leben.
     */
    private Color getColor(float healthPercent){
        if(healthPercent > 0.5){return Color.GREEN;}
        else if(healthPercent > 0.25){return Color.ORANGE;}
        else{return Color.RED;}
    }
}
