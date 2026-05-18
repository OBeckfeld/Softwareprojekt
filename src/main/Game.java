package main;

import entities.*;
import inputs.KeyboardInputs;
import inputs.MouseInputs;
import tools.TileManager;

import java.util.ArrayList;


public class Game implements Runnable {

    private GameWindow gameWindow;
    private GamePanel gamePanel;
    private Thread gameThread;
    private final int FPS_SET = 120; // Wir zielen auf 120 Bilder pro Sekunde ab
    public Player player;
    private KeyboardInputs keyboardInputs;
    private MouseInputs mouseInputs;
    private EntityManager entities;
    public CollisionManager collision;
    private TileManager tileManager;
    public Game() {
        // Initialisierung der Kern-Komponenten
        tileManager = new TileManager();
        gamePanel = new GamePanel(this, tileManager);
        gameWindow = new GameWindow(gamePanel);
        keyboardInputs = new KeyboardInputs(this);
        mouseInputs = new MouseInputs(gamePanel);
        entities = new EntityManager();
        collision = new CollisionManager(entities);
        player = new Player(200, 200, 80, 40, entities, tileManager);
        Enemy enemy = new Enemy(100, 100, 40, 40, entities, player, tileManager);

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
                ArrayList<Entity> allEntities = entities.getEntities();
                //erlaubt es jeder entity jeden tick etwas zu machen
                for (Entity entity : allEntities){
                    entity.act();
                }
                player.update(keyboardInputs.getHeldKeys());
                gamePanel.repaint();
                lastFrame = now;
            }
        }
    }
    public ArrayList<Entity> getEntities(){ return entities.getEntities();}

    }