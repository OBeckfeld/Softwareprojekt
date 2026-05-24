package entities;

import entities.managers.AttackManager;
import entities.managers.EntityRegistry;
import tools.Vector;

public class Projectile extends Entity {
    protected PlayerTypeEntity owner;
    protected double speed;
    protected int direction;
    protected int timeToLive;
    protected int timeAlive = 0;
    protected Vector moveVector;
    public Projectile(double x, double y, int width, int height, EntityRegistry registry, AttackManager attackManager, PlayerTypeEntity owner, double speed, int direction, int timeToLive){
        super(x, y, width, height, registry, attackManager);
        this.owner = owner;
        this.speed = speed;
        this.direction = direction;
        this.timeToLive = timeToLive;
        moveVector = new Vector(x, y, x+getOffsetCoords(direction)[0], y+getOffsetCoords(direction)[1]);
        moveVector.setLength(speed);
    }
    public Projectile(double x, double y, int width, int height, PlayerTypeEntity owner, double speed, int direction, int timeToLive){
        super(x, y, width, height, owner.registry, owner.attackManager);
    }
    @Override
    public void update(){
        timeAlive++;
        if (timeAlive>= timeToLive){
            registry.unregister(this);
            return;
        }
        logic();
    }
    protected void logic(){
        for (Entity entity : registry.getInRange(this, width)) {
            if (!(entity instanceof  Enemy)) {
                continue;
            }
            hit();
        }
        move();
    }
    protected void move(){
        move(moveVector);
    }
    protected void hit(){

        Attack attack = attackManager.newAttack(owner, x, y, width+1, width+1, 2, 40);
        System.out.println(attackManager);
        attackManager.attack(attack);
        unregister();
    }
}
