package main;

import entities.Entity;
import entities.Player;
import entities.Enemy;
import entities.managers.CollisionManager;
import entities.managers.EntityRegistry;
import inputs.KeyboardInputs;
import entities.managers.EntityManager;
import entities.managers.AttackManager;

import java.util.ArrayList;


public class Game implements Runnable {

    private GameWindow gameWindow;
    private GamePanel gamePanel;
    private Thread gameThread;
    private final int FPS_SET = 120; // Wir zielen auf 120 Bilder pro Sekunde ab;
    private KeyboardInputs keyboardInputs;
    private EntityManager entities;
    private CollisionManager collisions;
    private AttackManager attackManager;

    public Game() {
        // Initialisierung der Kern-Komponenten
        gamePanel = new GamePanel(this);
        gameWindow = new GameWindow(gamePanel);
        keyboardInputs = new KeyboardInputs(this);
        entities = new EntityManager();
        collisions = new CollisionManager(entities);
        entities.setCollisions(collisions);
        attackManager = new AttackManager(collisions, entities);
        Player player = new Player(200, 200, 80, 40, entities, keyboardInputs, attackManager);
        new Enemy(500, 500, 40, 40, 360, entities, attackManager);

        new Enemy(700, 700, 40, 40, 360, entities, attackManager);//provisorisch


        // Wichtig: Das Panel muss den Fokus haben, um Tastatureingaben zu erkennen
        gamePanel.setFocusable(true);
        gamePanel.requestFocus();

        //Listener für die Eingaben im GamePanel registrieren
        gamePanel.addKeyListener(keyboardInputs);

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
                collisions.checkCollisions();
                //erlaubt es jeder entity jeden tick etwas zu machen
                for (Entity entity : new ArrayList<>(entities.getEntities())){
                    entity.update();
                }
                gamePanel.repaint();
                lastFrame = now;
            }
        }
    }
    public ArrayList<Entity> getEntities(){ return entities.getEntities();}//nur zum Testen, muss noch entfernt werden

    }