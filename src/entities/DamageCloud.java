package entities;

import entities.managers.EntityRegistry;
import tools.TileManager;


public class DamageCloud extends Entity {
    private int timeToLive;
    private int timeAlive;
    private int damage;
    private PlayerTypeEntity owner;
    public DamageCloud(double x, double y, int height, int width, EntityRegistry registry, TileManager tileManager, int timeToLive, int damage, PlayerTypeEntity owner) {
        super(x,y,height,width,registry, tileManager);
        this.timeToLive = timeToLive;
        this.timeAlive = 0;
        this.damage = damage;
        this.owner = owner;

    }
    public void update(){

        if (timeAlive >= timeToLive) {
            registry.unregister(this);
        }
        if(timeAlive %100 ==0){
            owner.attackRegistry.attack(owner,x,y,height,width,10,damage);
        }
        timeAlive ++;
    }
}

