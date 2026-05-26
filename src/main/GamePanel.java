package main;

import entities.*;
import entities.enemies.Enemy;

import javax.swing.JPanel;
import java.awt.*;
import java.util.ArrayList;
import tools.TileManager;
import java.awt.image.BufferedImage;

public class GamePanel extends JPanel {
    private Game game;
    private TileManager tileManager;

    public GamePanel(Game game, TileManager tileManager) {
        this.game = game;
        this.tileManager = tileManager;

        setPanelSize();
    }

    private void setPanelSize() {
        Dimension size = new Dimension(1920, 1080);
        setPreferredSize(size);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, getWidth(), getHeight());
        Graphics2D g2d = (Graphics2D) g;

        tileManager.draw(g2d);

        for (Entity entity : new ArrayList<Entity>(game.getEntityManager().getEntities())) {
            g.setColor(Color.BLUE);
            if (entity == null || entity instanceof ViewBox){
                continue;
            }
            if (entity instanceof Player){
                BufferedImage sprite = ((Player)entity).getSprite();
                if (sprite != null) {
                    g2d.drawImage(sprite, (int) Math.round(entity.getX()), (int) Math.round(entity.getY()),
                            entity.getWidth(), entity.getHeight(), null);
                } else {
                    g2d.setColor(Color.BLUE);
                    g2d.fillRect((int) Math.round(entity.getX()), (int) Math.round(entity.getY()),
                            entity.getWidth(), entity.getHeight());
                }
                ((Player)entity).draw(g2d); // Healthbar
                continue;
            }
            else if (entity instanceof Enemy){
                BufferedImage sprite = ((Enemy)entity).getSprite();
                if (sprite != null) {
                    g2d.drawImage(sprite, (int) Math.round(entity.getX()), (int) Math.round(entity.getY()),
                            entity.getWidth(), entity.getHeight(), null);
                } else {
                    g.setColor(Color.RED);
                    g.fillRect((int) Math.round(entity.getX()), (int) Math.round(entity.getY()),
                            entity.getWidth(), entity.getHeight());
                }
                ((Enemy)entity).draw(g2d); // Healthbar zeichnen
                continue;

            }
            else if (entity instanceof Attack){
                if(((Attack)entity).isVisible()) {
                    g.setColor(Color.ORANGE);
                }
                else {
                    continue;
                }
            }
            else if (entity instanceof Door){
                if(((Door)entity).isOpen()) {
                    g.setColor(Color.GREEN);
                }
                else {
                    g.setColor(Color.GRAY);
                }
            }
            double x = entity.getX();
            double y = entity.getY();
            int width = entity.getWidth();
            int height = entity.getHeight();

            g.fillRect((int) Math.round(x), (int) Math.round(y), width, height);
        }
    }
}
