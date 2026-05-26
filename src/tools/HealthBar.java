package tools;

import entities.PlayerTypeEntity;

import java.awt.*;

public class HealthBar {
    private final int WIDTH = 100;
    private final int HEIGHT = 6;
    private final int Y_OFFSET = 12;
    private final PlayerTypeEntity entity;

    public HealthBar(PlayerTypeEntity entity) {
        this.entity = entity;
    }

    public void draw(Graphics2D g){

        float healthPercent = (float) entity.getCurrentHealth() / entity.getMaxHealth();
        int x = (int) entity.getX() + entity.getWidth() / 2 - WIDTH / 2;
        int y = (int) entity.getY() - HEIGHT - Y_OFFSET;

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

    private Color getColor(float healthPercent){
        if(healthPercent > 0.5){return Color.GREEN;}
        else if(healthPercent > 0.25){return Color.ORANGE;}
        else{return Color.RED;}
    }
}
