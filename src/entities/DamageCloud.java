package entities;

import entities.managers.EntityRegistry;
import tools.TileManager;

import java.awt.*;


public class DamageCloud extends Entity {
    private int timeToLive;
    private int timeAlive;
    private int damage;
    private PlayerTypeEntity owner;
    private Color color;
    private int frequency;
    private boolean armorPierce;
    public DamageCloud(double x, double y, int height, int width, EntityRegistry registry, TileManager tileManager, int timeToLive, int frequency, int damage, PlayerTypeEntity owner, Color color, boolean armorPierce) {
        super(x,y,height,width,registry, tileManager);
        this.timeToLive = timeToLive;
        this.timeAlive = 0;
        this.damage = damage;
        this.owner = owner;
        this.color = color;
        this.frequency = frequency;
        this.armorPierce = armorPierce;
    }

    public void update(){

        if (timeAlive >= timeToLive) {
            registry.unregister(this);
        }
        if(timeAlive %frequency ==0){
            owner.attackRegistry.attack(owner,x,y,height,width,1,damage,armorPierce);
        }
        timeAlive ++;
    }
    public Color getColor(){
        return color;
    }
}

