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

    private final GameWindow gameWindow;       // Fenster des Spiels
    private final GamePanel gamePanel;         // Panel, auf dem das Spiel gezeichnet wird
    private Thread gameThread;                 // Thread, in dem der Game-Loop läuft
    private final int FPS_SET = 120;           // Ziel-FPS
    private final KeyboardInputs keyboardInputs; // Tastatureingaben
    private final EntityManager entities;      // verwaltet alle Entities
    private CollisionManager collisions;       // verwaltet Kollisionen
    private final AttackManager attackManager; // verwaltet Angriffe und Schaden
    private final TileManager tileManager;     // verwaltet die Tiles der Map
    private final MapLoader mapLoader;         // lädt und baut Maps
    private final ProgressManager progressManager; // verwaltet Spielfortschritt
    private final TextBoxManager textBoxManager;   // verwaltet Textboxen
    private final Camera camera;               // Kamera folgt dem Spieler
    private Player player;                     // Referenz auf den Spieler
    private static int WIDTH;                  // Breite der aktuellen Map
    private static int HEIGHT;                 // Höhe der aktuellen Map
    public static int screenWidth;             // Bildschirmbreite
    public static int screenHeight;            // Bildschirmhöhe
    public long lastRespawned = System.currentTimeMillis();
    public Game() {
        // Bildschirmgröße ermitteln für Kamera und SkillTree
        getScreenSize();

        // Kern-Komponenten initialisieren
        tileManager = new TileManager();
        textBoxManager = new TextBoxManager();
        gamePanel = new GamePanel(this, tileManager, textBoxManager);
        keyboardInputs = new KeyboardInputs(this);

        // EntityManager und CollisionManager gegenseitig verknüpfen
        entities = new EntityManager(collisions, tileManager);
        collisions = new CollisionManager(entities);
        entities.setCollsisons(collisions);

        // AttackManager und Spieler erstellen
        attackManager = new AttackManager(collisions, entities, tileManager);
        player = new Player(tileManager.getTileSize() * 2 + 5, tileManager.getTileSize() * 2 + 5,
                80, 80, entities, keyboardInputs, attackManager, tileManager, gamePanel);
        gamePanel.assignPlayer(player);

        // Map laden und Fortschrittsmanager initialisieren
        mapLoader = new MapLoader(tileManager.getTileSize(), entities, keyboardInputs,
                attackManager, collisions, tileManager, gamePanel);
        mapLoader.buildMap();
        progressManager = new ProgressManager(player, mapLoader, collisions);

        // Anleitungs-Textbox beim Start anzeigen
        new TextBox("Anleitung:(e)Steuerung:(e)Bewegung: WASD (e) Angriff: J (e) "+
                "Skilltree: P (e) Skills: 1 bis 5, in freigeschalteter (e) Reihenfolge. (e) Waffe wechseln: L, K (e)" +
                "Generelles: Skillpoints und Kosten werden im Skilltree angezeigt.(e)" +
                "Rote Objekte sind Waypoints, an welchen der Fortschritt gespeichert werden kann. Bei Benutzung werden sie blau.(e)" +
                "Graue Objekte sind Türen, welche sich durch besiegen aller Gegner aktivieren. Interaktion mit Türen ist durch Berührung möglich." +
                "Neue Waffen werden beim Betreten bestimmter Räume freigeschaltet." +
                "Nachdem der letzte Raum erreicht ist, wird auf Wiederholung geschaltet und man kann den letzten Raum so oft wie man es schafft durchspielen.",
                0, 400, 700, 500, 5200, textBoxManager);

        // Map-Größe berechnen
        WIDTH = tileManager.getTileMap()[0].length * tileManager.getTileSize();
        HEIGHT = tileManager.getTileMap().length * tileManager.getTileSize();

        // Kamera und Fenster initialisieren
        camera = new Camera(player.getX(), player.getY(), screenWidth, screenHeight);
        gameWindow = new GameWindow(gamePanel);

        // Tastatureingaben auf das Panel setzen
        gamePanel.setFocusable(true);
        gamePanel.requestFocus();
        gamePanel.addKeyListener(keyboardInputs);

        startGameLoop();
    }

    public static int getWIDTH() { return WIDTH; }
    public static int getHEIGHT() { return HEIGHT; }
    public EntityManager getEntityManager() { return entities; }
    public ProgressManager getProgressManager() { return progressManager; }
    public Camera getCamera() { return camera; }

    /**
     * Spieler wird respawned und sein letzter gespeicherter Fortschritt wird geladen.
     * Wird aufgerufen wenn der Spieler stirbt und einen Speicherpunkt hat.
     */
    public void respawn() {
        if (lastRespawned + 5000> System.currentTimeMillis()){
            return;
        }
        if (progressManager.getSavingIndex() == 1) return; // kein Fortschritt vorhanden

        // alten Spieler entfernen und neuen erstellen
        entities.unregister(this.player);
        // Alte SkillTree-Ability-Objekte aus dem GamePanel entfernen
        gamePanel.clearAbilities();

        player = new Player(tileManager.getTileSize() * 2 + 5, tileManager.getTileSize() * 2 + 5,
                80, 80, entities, keyboardInputs, attackManager, tileManager, gamePanel);
        gamePanel.assignPlayer(player);

        // Fortschritt laden und Kamera aktualisieren
        progressManager.setPlayer(player);
        progressManager.loadNewestProgress();
        camera.setX(player.getX());
        camera.setY(player.getY());
        camera.update(player);
        player.setWeapon(player.getStarterSword());

        // Death Screen ausblenden und Fokus zurücksetzen
        textBoxManager.clearMenuTextBoxes();
        gamePanel.setDeathScreen(false);
        gamePanel.requestFocusInWindow();
    }

    /**
     * Spieler startet komplett von vorne.
     * Wird aufgerufen wenn der Spieler stirbt ohne Speicherpunkt.
     */
    public void startOver() {
        // alten Spieler entfernen und neuen erstellen

        entities.unregister(this.player);
        player = new Player(tileManager.getTileSize() * 2 + 5, tileManager.getTileSize() * 2 + 5,
                80, 80, entities, keyboardInputs, attackManager, tileManager, gamePanel);
        gamePanel.assignPlayer(player);

        // Map von vorne laden
        progressManager.setPlayer(player);
        mapLoader.setMapIndex(1);
        mapLoader.buildMap();

        // Kamera und Death Screen zurücksetzen
        camera.setX(player.getX());
        camera.setY(player.getY());
        camera.update(player);
        textBoxManager.clearMenuTextBoxes();
        gamePanel.setDeathScreen(false);
    }

    /**
     * Ermittelt die Bildschirmgröße für Kamera und SkillTree-Darstellung.
     */
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
        gameThread.start(); // startet run() in einem neuen Thread
    }

    /**
     * Aktualisiert die Map-Größe nach jedem Frame,
     * da sich die Map beim Raumwechsel ändern kann.
     */
    public void updateMapSize() {
        WIDTH = tileManager.getTileMap()[0].length * tileManager.getTileSize();
        HEIGHT = tileManager.getTileMap().length * tileManager.getTileSize();
    }

    /**
     * Game-Loop – läuft 120 mal pro Sekunde.
     * Jeder Frame: Kollisionen prüfen → Entities updaten → Schaden verteilen → zeichnen.
     */
    @Override
    public void run() {
        double timePerFrame = 1000000000.0 / FPS_SET; // Nanosekunden pro Frame
        long lastFrame = System.nanoTime();
        long now;

        while (true) {
            now = System.nanoTime();
            if (now - lastFrame >= timePerFrame) {

                collisions.checkCollisions();                  // Kollisionen aktualisieren

                for (Entity entity : new ArrayList<>(entities.getEntities())) {
                    entity.update();                           // jede Entity macht ihren Zug
                }

                updateMapSize();                               // Map-Größe aktualisieren
                attackManager.distributeDamage();              // Schaden verteilen
                camera.update(player);                         // Kamera dem Spieler folgen lassen
                progressManager.checkSaveRequests();           // Speicheranfragen prüfen
                textBoxManager.updateBoxes();                  // Textboxen aktualisieren
                mapLoader.checkMapUpdate();                    // Raumwechsel prüfen
                gamePanel.repaint();                           // Bild neu zeichnen

                lastFrame = now;
            }
        }
    }
}