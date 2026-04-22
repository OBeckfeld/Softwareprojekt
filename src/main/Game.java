package main;

import entities.Player;

public class Game implements Runnable {

    private GameWindow gameWindow;
    private GamePanel gamePanel;
    private Thread gameThread;
    private final int FPS_SET = 120; // Wir zielen auf 120 Bilder pro Sekunde ab
    public Player player;

    public Game() {
        // Initialisierung der Kern-Komponenten
        player = new Player(200, 200, 40, 40);
        gamePanel = new GamePanel(this);
        gameWindow = new GameWindow(gamePanel);

        // Wichtig: Das Panel muss den Fokus haben, um Tastatureingaben zu erkennen
        gamePanel.setFocusable(true);
        gamePanel.requestFocus();

        startGameLoop();
    }

    private void startGameLoop() {
        gameThread = new Thread(this);
        gameThread.start(); // Startet die run() Methode in einem neuen Thread
    }

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
                gamePanel.repaint();
                lastFrame = now;
            }
        }
    }
}