package Weapons;

import entities.Entity;
import entities.PlayerTypeEntity;
import entities.Projectile;
import entities.managers.AttackManager;
import tools.Vector;

import java.util.Random;
public class MiniGun extends Weapon{
    public MiniGun(PlayerTypeEntity owner, AttackManager attackManager){
        super(owner,attackManager);
        damage = 10;
        attackCooldown = 50;//inMilli Sekunden
        attackDuration = 10;//in ticks
    }
    @Override
    public boolean use(){
        if (!super.use()){return false;}//on cooldown
        Random random = new Random();
        double xOffset = random.nextInt(1)-0.5;
        double yOffset = random.nextInt(1)-0.5;
        double y = 0;
        double x = 0;

        switch (owner.getDirection()) { //die Position der Attacke wird abhängig von der Richtung des owners gesetzt
            case Entity.NORTH:
                xOffset = random.nextDouble(1)-0.5;
                y = y -owner.getHeight()/2;
                yOffset = random.nextDouble(1)-1;
                break;
            case Entity.EAST:
                yOffset = random.nextDouble(1)-0.5;
                x = x+owner.getWidth()/2;
                xOffset = random.nextDouble(1);
                break;
            case Entity.SOUTH:
                xOffset = random.nextDouble(1)-0.5;
                y = y+owner.getHeight()/2;
                yOffset = random.nextDouble(1);
                break;
            case Entity.WEST:
                yOffset = random.nextDouble(1)-0.5;
                x = x-owner.getWidth()/2;
                xOffset = random.nextDouble(1)-1;
                break;
        }
        x = x + owner.getCenter() [0];
        y = y + owner.getCenter() [1];
        Vector offsetVector = new Vector(x,y,x+xOffset,y+yOffset);
        Vector normalVector = new Vector(x, y, x+owner.getOffsetCoords(owner.getDirection())[0], y+owner.getOffsetCoords(owner.getDirection())[1]);
        normalVector.setLength(10);
        offsetVector.setLength(5);
        normalVector.combineVector(offsetVector);
        new Projectile(x, y, 10, 10, owner.registry, attackManager, owner, 5, normalVector, 50, damage);

        int dir = owner.getOppDirection(owner.getDirection());
        Vector kbVector = new Vector(x, y, x+owner.getOffsetCoords(dir)[0], y+owner.getOffsetCoords(dir)[1]);
        kbVector.setLength(4);
        owner.move(kbVector);
        return true;
    }
}
