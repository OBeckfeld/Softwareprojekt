package main;

import entities.Enemy;
import entities.Entity;
import entities.Player;
import inputs.KeyboardInputs;
import inputs.MouseInputs;

public class Game implements Runnable {

    private GameWindow gameWindow;
    private GamePanel gamePanel;
    private Thread gameThread;
    private final int FPS_SET = 120; // Wir zielen auf 120 Bilder pro Sekunde ab
    public Player player;
    public Enemy enemy;
    private KeyboardInputs keyboardInputs;
    private MouseInputs mouseInputs;
    private Dummy map;//Repräsentation der map

    public Game() {
        // Initialisierung der Kern-Komponenten
        player = new Player(200, 200, 80, 40,100);
        gamePanel = new GamePanel(this);
        gameWindow = new GameWindow(gamePanel);
        keyboardInputs = new KeyboardInputs(this);
        mouseInputs = new MouseInputs(gamePanel);
        enemy = new Enemy(100, 100, 40, 50, 30, 1, 5);

        // Wichtig: Das Panel muss den Fokus haben, um Tastatureingaben zu erkennen
        gamePanel.setFocusable(true);
        gamePanel.requestFocus();

        //Listener für die Eingaben im GamePanel registrieren
        gamePanel.addKeyListener(keyboardInputs);
        gamePanel.addMouseListener(mouseInputs);
        gamePanel.addMouseMotionListener(mouseInputs);

        startGameLoop();
    }

    private void startGameLoop() {
        gameThread = new Thread(this);
        gameThread.start(); // Startet die run() Methode in einem neuen Thread
    }

    //Game Loop
    @Override
    public void run() {
        // Die Zeitspanne pro Frame in Nanosekunden
        double timePerFrame = 1000000000.0 / FPS_SET;
        long lastFrame = System.nanoTime();
        long now;

        while (true) {
            now = System.nanoTime();
            // Wenn genug Zeit vergangen ist, zeichnen wir neu
            if (now - lastFrame >= timePerFrame) {
                player.update(keyboardInputs.getHeldKeys());
                gamePanel.repaint();
                lastFrame = now;
            }
        }
    }

    public void healthManager(Dummy attacker, Dummy target) {//angegriffene entity wird übergeben
        if(map.testBoolean()) {//dummy repräsentiert map; test() überprüft, ob Gegner in Reichweite waren
            target.setHealth(target.getHealth() - (attacker.getAttack() + attacker.getWeapon().getAttack() - target.getDefense()));//health der angegriffenen entity wird bearbeitet
    }
    }
}