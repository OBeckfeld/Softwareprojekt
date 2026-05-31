package entities;

import entities.enemies.Enemy;
import entities.managers.AttackRegistry;
import entities.managers.EntityRegistry;
import tools.TileManager;
import tools.Vector;

public class Projectile extends Entity {
    protected PlayerTypeEntity owner;
    protected double speed;
    protected int timeToLive;
    protected int timeAlive = 0;
    protected Vector moveVector;
    protected int damage;

    /**
     * Erstellt ein neues Projektil mit einer Bewegungsrichtung.
     * Aus der Richtung wird ein Bewegungsvektor berechnet, der anschließend
     * auf die angegebene Geschwindigkeit gesetzt wird.
     */
    public Projectile(double x, double y, int width, int height, EntityRegistry registry, AttackRegistry attackRegistry, PlayerTypeEntity owner, double speed, int direction, int timeToLive, int damage, TileManager tileManager) {
        super(x, y, width, height, registry, attackRegistry, tileManager);
        this.owner = owner;
        this.speed = speed;
        this.timeToLive = timeToLive;
        moveVector = new Vector(x, y, x+getOffsetCoords(direction)[0], y+getOffsetCoords(direction)[1]);
        moveVector.setLength(speed);
        this.damage = damage;


    }

    /**
     * Erstellt ein neues Projektil mit einem bereits vorhandenen Bewegungsvektor.
     * Der übergebene Vektor wird auf die angegebene Geschwindigkeit gesetzt.
     */
    public Projectile(double x, double y, int width, int height, EntityRegistry registry, AttackRegistry attackRegistry, PlayerTypeEntity owner, double speed, Vector vector, int timeToLive, int damage,  TileManager tileManager) {
        super(x, y, width, height, registry, attackRegistry, tileManager);
        registry.register(this);
        this.registry = registry;
        this.owner = owner;
        this.speed = speed;
        this.timeToLive = timeToLive;
        moveVector = vector;
        moveVector.setLength(speed);
        this.damage = damage;
    }

    /**
     * Aktualisiert das Projektil pro Tick.
     * Dabei wird die Lebenszeit erhöht und geprüft, ob das Projektil
     * seine maximale Lebensdauer erreicht hat.
     */
    @Override
    public void update(){

        timeAlive++;
        if (timeAlive>= timeToLive){
            hit();
            return;
        }
        logic();
    }

    /**
     * Führt die Logik des Projektils aus.
     * Prüft, ob ein Gegner getroffen wurde oder ob das Projektil
     * mit einer Wand kollidiert, und bewegt es sonst weiter.
     */
    protected void logic(){
        for (Entity entity : registry.getInRange(this, width,height)) {
            if (!(entity instanceof Enemy)) {
                continue;
            }
            hit();
            return;
        }
        if (movement.collidesWithWall(this, moveVector.getOffsetX(), moveVector.getOffsetY())){
            hit();
            return;
        }
        move();
    }

    /**
     * Bewegt das Projektil anhand seines Bewegungsvektors.
     */
    protected void move(){
        move(moveVector);
    }

    /**
     * Führt den Treffer des Projektils aus.
     * Dabei wird ein Angriff an der aktuellen Position registriert
     * und das Projektil anschließend aus dem Spiel entfernt.
     */
    protected void hit(){
        attackRegistry.attack(owner, x, y, height+2, width+2, 2, damage);
        unregister();
    }
}
