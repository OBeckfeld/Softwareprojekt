package main;

import entities.Entity;
import entities.Player;
import entities.Enemy;
import entities.managers.CollisionManager;
import inputs.KeyboardInputs;
import entities.managers.EntityManager;

import java.util.ArrayList;


public class Game implements Runnable {

    private GameWindow gameWindow;
    private GamePanel gamePanel;
    private Thread gameThread;
    private final int FPS_SET = 120;
    private KeyboardInputs keyboardInputs;
    private EntityManager entities;
    private CollisionManager collisions;

    public Game() {
        gamePanel = new GamePanel(this);
        gameWindow = new GameWindow(gamePanel);
        keyboardInputs = new KeyboardInputs(this);
        entities = new EntityManager();
        Player player = new Player(200, 200, 80, 40, entities, keyboardInputs);
        new Enemy(100, 100, 40, 40, entities, player);
        collisions = new CollisionManager(entities);

        gamePanel.setFocusable(true);
        gamePanel.requestFocus();

        gamePanel.addKeyListener(keyboardInputs);

        startGameLoop();
    }

    private void startGameLoop() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {
        double timePerFrame = 1000000000.0 / FPS_SET;
        long lastFrame = System.nanoTime();
        long now;

        while (true) {
            now = System.nanoTime();
            if (now - lastFrame >= timePerFrame) {
                collisions.checkCollisions();
                for (Entity entity : entities.getEntities()){
                    entity.update();
                }
                gamePanel.repaint();
                lastFrame = now;
            }
        }
    }
    public ArrayList<Entity> getEntities(){ return entities.getEntities();}

    }
