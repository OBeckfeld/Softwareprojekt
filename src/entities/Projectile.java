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

    public Projectile(double x, double y, int width, int height, EntityRegistry registry, AttackRegistry attackRegistry, PlayerTypeEntity owner, double speed, int direction, int timeToLive, int damage, TileManager tileManager) {
        super(x, y, width, height, registry, attackRegistry, tileManager);
        this.owner = owner;
        this.speed = speed;
        this.timeToLive = timeToLive;
        moveVector = new Vector(x, y, x+getOffsetCoords(direction)[0], y+getOffsetCoords(direction)[1]);
        moveVector.setLength(speed);
        this.damage = damage;


    }
    public Projectile(double x, double y, int width, int height, EntityRegistry registry, AttackRegistry attackRegistry, PlayerTypeEntity owner, double speed, Vector vector, int timeToLive, int damage,  TileManager tileManager) {
        super(x, y, width, height, registry, attackRegistry, tileManager);
        this.owner = owner;
        this.speed = speed;
        this.timeToLive = timeToLive;
        moveVector = vector;
        moveVector.setLength(speed);
        this.damage = damage;
    }
    @Override
    public void update(){

        timeAlive++;
        if (timeAlive>= timeToLive){
            hit();
            return;
        }
        logic();
    }
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
    protected void move(){
        move(moveVector);
    }
    protected void hit(){
        attackRegistry.attack(owner, x, y, height+2, width+2, 2, damage);
        unregister();
    }
}