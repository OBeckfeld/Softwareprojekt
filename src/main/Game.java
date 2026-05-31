package main;

import entities.Entity;
import entities.Player;
import entities.enemies.Enemy;
import entities.managers.CollisionManager;
import entities.managers.EntityManager;
import entities.managers.AttackManager;
import entities.managers.ProgressManager;
import inputs.KeyboardInputs;
import tools.Camera;
import tools.MapLoader;
import tools.TileManager;
import tools.TextBoxManager;
import tools.TextBox;

import java.util.ArrayList;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.geom.AffineTransform;
import java.awt.Rectangle;

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
    private final MapLoader mapLoader;
    private final ProgressManager progressManager;
    private final TextBoxManager textBoxManager;
    private final Camera camera;
    private final Player player;
    private static int WIDTH;
    private static int HEIGHT;
    private static int screenWidth;
    private static int screenHeight;

    public Game() {
        // Initialisierung der Kern-Komponenten
        getScreenSize();
        textBoxManager = new TextBoxManager();
        tileManager = new TileManager();
        gamePanel = new GamePanel(this, tileManager, textBoxManager);
        keyboardInputs = new KeyboardInputs(this);
        entities = new EntityManager(collisions, tileManager);
        collisions = new CollisionManager(entities);
        entities.setCollsisons(collisions);//temporär bis ihr diese absolut gekochten Abhängigkeiten gefixt habt





        attackManager = new AttackManager(collisions, entities, tileManager);
        player = new Player(tileManager.getTileSize() * 2 + 5, tileManager.getTileSize() * 2 + 5, 40, 80, entities, keyboardInputs, attackManager, tileManager, gamePanel);

        mapLoader = new MapLoader(tileManager.getTileSize(), entities, keyboardInputs, attackManager, collisions, tileManager, gamePanel);
        mapLoader.buildMap();
        progressManager = new ProgressManager(player, mapLoader, collisions);

        new TextBox("Hi! Glad you found this text box... (PS: It is completely useless) You can remove it by deleting its constructor in Game (line 60)", 200, 200, 400, 200, 1000, textBoxManager);

        WIDTH = tileManager.getTileMap()[0].length * tileManager.getTileSize();
        HEIGHT = tileManager.getTileMap().length * tileManager.getTileSize();

        camera = new Camera(player.getX(), player.getY(), screenWidth, screenHeight);
        gameWindow = new GameWindow(gamePanel);
        // Wichtig: Das Panel muss den Fokus haben, um Tastatureingaben zu erkennen
        gamePanel.setFocusable(true);
        gamePanel.requestFocus();

        //Listener für die Eingaben im GamePanel registrieren
        gamePanel.addKeyListener(keyboardInputs);

        startGameLoop();
    }

    public static int getWIDTH() {return WIDTH;}
    public static int getHEIGHT() {return HEIGHT;}

    public EntityManager getEntityManager(){return entities;}

    public Camera getCamera(){return camera;}

    public void getScreenSize() {
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        GraphicsConfiguration gc = gd.getDefaultConfiguration();

        Rectangle screenBounds = gc.getBounds();

        AffineTransform at = new AffineTransform();
        screenWidth = Math.toIntExact(Math.round(screenBounds.width / at.getScaleX()));
        screenHeight = Math.toIntExact(Math.round(screenBounds.height / at.getScaleY()));    
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
                attackManager.distributeDamage();
                camera.update(player);
                progressManager.checkSaveRequests();
                mapLoader.checkMapUpdate();
                gamePanel.repaint();
                lastFrame = now;
            }
        }
    }

    }