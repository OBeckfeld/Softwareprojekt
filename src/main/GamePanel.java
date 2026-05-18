package main;

import entities.Attack;
import entities.Entity;
import entities.Enemy;
import entities.Player;
import entities.ViewBox;

import javax.swing.JPanel;
import java.awt.*;
import java.util.ArrayList;

public class GamePanel extends JPanel {
    private Game game;

    public GamePanel(Game game) {
        this.game = game;

        setPanelSize();
    }

    private void setPanelSize() {
        Dimension size = new Dimension(1920, 1080);
        setPreferredSize(size);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        for (Entity entity : new ArrayList<Entity>(game.getEntities())){
            if (entity == null || entity instanceof ViewBox){
                continue;
            }
            if (entity instanceof Player){
                g.setColor(Color.BLUE);
            }
            else if (entity instanceof Enemy){
                g.setColor(Color.RED);
            }
            else if (entity instanceof Attack){
                if(((Attack)entity).isVisible()) {
                    g.setColor(Color.ORANGE);
                }
                else {
                    continue;
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
