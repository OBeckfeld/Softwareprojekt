package main;

import inputs.KeyboardInputs;
import inputs.MouseInputs;
import entities.Entity;
import entities.Player;
import entities.Enemy;
import javax.swing.JPanel;
import java.awt.*;
import java.util.ArrayList;
import tools.TileManager;

public class GamePanel extends JPanel {
    private Game game;
    private TileManager tileManager;

    public GamePanel(Game game, TileManager tileManager) {
        this.game = game;
        this.tileManager = tileManager;

        setPanelSize();
    }

    private void setPanelSize() {
        // Die feste Größe unseres Spielfelds
        Dimension size = new Dimension(1920, 1080);
        setPreferredSize(size);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g); // Wichtig: Bereinigt das Panel vor dem Zeichnen

        tileManager.draw(g);
        // Spieler als blaues Rechteck zeichnen
        g.setColor(Color.BLUE);

        //alle entities werden durchgegangen und an ihrer position mit ihrer weite und breite gerendert
        for (Entity entity : game.getEntities()){
            g.setColor(Color.BLUE);
            if (entity instanceof Enemy){
                g.setColor(Color.RED);//enemies werden rot
            }
            double x = entity.getX();
            double y = entity.getY();
            int width = entity.getWidth();
            int height = entity.getHeight();

            g.fillRect((int) Math.round(x), (int) Math.round(y), width, height);//rectangle wird an der gerundeten x und y position erstellt
        }
    }
}