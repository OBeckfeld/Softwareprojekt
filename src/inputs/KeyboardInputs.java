package inputs;

import main.Game;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyboardInputs implements KeyListener {

    private Game g;

    public KeyboardInputs(Game g) {
        this.g = g;
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // Wird hier nicht benötigt
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // Hier könnte man später Code einfügen, um Bewegungen zu stoppen
    }

    @Override
    public void keyPressed(KeyEvent e) {
        // Abfrage, welche Taste gedrückt wurde
        switch (e.getKeyCode()) {
            case KeyEvent.VK_W:
                g.player.changeY(-5); // Nach oben (Y wird kleiner)
                break;
            case KeyEvent.VK_A:
                g.player.changeX(-5); // Nach links (X wird kleiner)
                break;
            case KeyEvent.VK_S:
                g.player.changeY(5);  // Nach unten (Y wird größer)
                break;
            case KeyEvent.VK_D:
                g.player.changeX(5);  // Nach rechts (X wird größer)
                break;
        }
    }
}