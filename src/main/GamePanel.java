package main;

import inputs.KeyboardInputs;
import inputs.MouseInputs;
import javax.swing.JPanel;
import java.awt.*;

public class GamePanel extends JPanel {
    private Game game;

    public GamePanel(Game game) {
        this.game = game;

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

        // Spieler als blaues Rechteck zeichnen
        g.setColor(Color.BLUE);

        // Wir holen uns die aktuellen Koordinaten direkt vom Player-Objekt
        int x = game.player.getX();
        int y = game.player.getY();

        int xE = game.enemy.getX();
        int yE = game.enemy.getY();

        g.fillRect(x, y, 50, 50);

        g.setColor(Color.RED);
        g.fillRect(xE, yE, 30, 30);
    }
}