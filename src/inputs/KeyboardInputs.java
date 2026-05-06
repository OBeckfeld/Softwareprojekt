package inputs;

import main.Game;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashSet;
import java.util.Set;

public class KeyboardInputs implements KeyListener {

    private Game game;
    private final Set<Integer> heldKeys = new HashSet<>();

    public KeyboardInputs(Game g) {
        this.game = g;
    }

    public Set<Integer> getHeldKeys() {return heldKeys;};

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
        heldKeys.remove(e.getKeyCode());
    }

    @Override
    public void keyPressed(KeyEvent e) {
        heldKeys.add(e.getKeyCode());
        }
    }
