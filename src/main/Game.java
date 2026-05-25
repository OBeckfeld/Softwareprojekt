package main;

import entities.Entity;
import entities.Player;
import entities.enemies.Enemy;
import entities.managers.CollisionManager;
import inputs.KeyboardInputs;
import entities.managers.EntityManager;
import entities.managers.AttackManager;
import tools.TileManager;

import java.util.ArrayList;


public class Game implements Runnable {

    private final GameWindow gameWindow;
    private final GamePanel gamePanel;
    private Thread gameThread;
    private final int FPS_SET = 120; // Wir zielen auf 120 Bilder pro Sekunde ab;
    private final KeyboardInputs keyboardInputs;
    private final EntityManager entities;
    private CollisionManager collisions;//müsste Final aber geht nicht
    private final AttackManager attackManager;
    private final TileManager tileManager;

    public Game() {
        // Initialisierung der Kern-Komponenten
        tileManager = new TileManager();
        gamePanel = new GamePanel(this, tileManager);
        gameWindow = new GameWindow(gamePanel);
        keyboardInputs = new KeyboardInputs(this);
        entities = new EntityManager(collisions, tileManager);
        collisions = new CollisionManager(entities);
        entities.setCollsisons(collisions);//temporär bis ihr diese absolut gekochten Abhängigkeiten gefixt habt

        attackManager = new AttackManager(collisions, entities, tileManager);
        Player player = new Player(200, 200, 40, 80, entities, keyboardInputs, attackManager, tileManager);
        new Enemy(500, 500, 40, 40, 360, entities, attackManager, tileManager);

        new Enemy(700, 700, 40, 40, 360, entities, attackManager, tileManager);//provisorisch
        new Enemy(700, 700, 40, 40, 360, entities, attackManager, tileManager);//provisorisch
        new Enemy(700, 700, 40, 40, 360, entities, attackManager, tileManager);//provisorisch
        new Enemy(700, 700, 40, 40, 360, entities, attackManager, tileManager);//provisorisch
        new Enemy(700, 700, 40, 40, 360, entities, attackManager, tileManager);//provisorisch
        new Enemy(700, 700, 40, 40, 360, entities, attackManager, tileManager);//provisorisch


        // Wichtig: Das Panel muss den Fokus haben, um Tastatureingaben zu erkennen
        gamePanel.setFocusable(true);
        gamePanel.requestFocus();

        //Listener für die Eingaben im GamePanel registrieren
        gamePanel.addKeyListener(keyboardInputs);

        startGameLoop();
    }

    public EntityManager getEntityManager(){return entities;}

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
                collisions.checkCollisions();
                //erlaubt es jeder entity jeden tick etwas zu machen
                for (Entity entity : new ArrayList<>(entities.getEntities())){
                    entity.update();
                }
                attackManager.distributeDamage();
                gamePanel.repaint();
                lastFrame = now;
            }
        }
    }

    }