package Weapons;

import entities.Entity;
import entities.PlayerTypeEntity;
import entities.Projectile;
import entities.managers.AttackRegistry;
import tools.TileManager;

import static java.lang.Math.round;

public class Rifle extends Weapon{
    public Rifle (PlayerTypeEntity owner, AttackRegistry attackRegistry, TileManager tileManager){
        super(owner, attackRegistry, tileManager);
        attackCooldown = 1000;//inMilli Sekunden
        attackDuration = 50;//in ticks
        damage = 80;
    }
    @Override
    public boolean use(){
        if (!super.use()){return false;}//on cooldown
        int scale = 15;
        int height = scale;
        int width = (int) Math.round(scale*3);
        switch (owner.getDirection()) { //die Position der Attacke wird abhängig von der Richtung des owners gesetzt
            case Entity.NORTH:
                height = (int) Math.round(scale*3);
                width = scale;
                break;
            case Entity.EAST:
                height = scale;
                width = (int) Math.round(scale*3);
                break;
            case Entity.SOUTH:
                height = (int) Math.round(scale*3);
                width = scale;
                break;
            case Entity.WEST:
                height = scale;
                width = (int) Math.round(scale*3);
                break;
        }
        new Projectile(owner.getCenter() [0], owner.getCenter() [1], width, height, owner.registry, attackRegistry, owner, 10, owner.getDirection(), 300, damage, tileManager);
        return true;
    }
}
