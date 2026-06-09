package Weapons;

import entities.Entity;
import entities.FollowingProjectile;
import entities.PlayerTypeEntity;
import entities.Projectile;
import entities.managers.AttackRegistry;
import tools.TileManager;
import tools.Vector;

import java.util.Random;
public class MiniGun extends Weapon{
    int ammo = 200; // Nummer an Schüssen, bevor die MiniGun reloaden muss
    int reloadTime = 60; // Zeit in Ticks, die zum reloaden benötigt wird
    int shotsSinceLastReload = 0;
    int reloadTimer = 0;
    
    public MiniGun(PlayerTypeEntity owner, AttackRegistry attackRegistry, TileManager tileManager) {
        super(owner, attackRegistry, tileManager);
        damage = 6;
        attackCooldown = 50;//inMilli Sekunden
        attackDuration = 10;//in ticks
    }

    @Override
    public boolean use(){
        if (!super.use()){return false;}//on cooldown
        if (reloadTimer > 0){
            reloadTimer--;
            return false; // weapon is temporarily disabled after too many shots
        }

        Random random = new Random();
        double xOffset = random.nextDouble(1)-0.5;
        double yOffset = random.nextDouble(1)-0.5;
        int projectileWidth = 10;
        int projectileHeight = 10;

        double x = owner.getCenter()[0];
        double y = owner.getCenter()[1];

        switch (owner.getDirection()) {
            case Entity.NORTH:
                x -= projectileWidth / 2.0;
                y -= owner.getHeight() / 2.0 + projectileHeight;
                break;

            case Entity.EAST:
                x += owner.getWidth() / 2.0;
                y -= projectileHeight / 2.0;
                break;

            case Entity.SOUTH:
                x -= projectileWidth / 2.0;
                y += owner.getHeight() / 2.0;
                break;

            case Entity.WEST:
                x -= owner.getWidth() / 2.0 + projectileWidth;
                y -= projectileHeight / 2.0;
                break;
        }
        Vector offsetVector = new Vector(x,y,x+xOffset,y+yOffset);
        Vector normalVector = new Vector(x, y, x+owner.getOffsetCoords(owner.getDirection())[0], y+owner.getOffsetCoords(owner.getDirection())[1]);
        normalVector.setLength(10);
        offsetVector.setLength(5);
        normalVector.combineVector(offsetVector);
        new FollowingProjectile(x, y, 10, 10, owner.registry, attackRegistry, owner, 8, normalVector, 40, damage,tileManager);

        applyKnockback(5);

        shotsSinceLastReload++;
        if (shotsSinceLastReload >= ammo){
            reloadTimer = reloadTime;
            shotsSinceLastReload = 0;
        }

        return true;
    }

    public void setShotsBeforeBlock(int ammo) {
        this.ammo = ammo;
    }

    public void setShotBlockFrames(int reloadTime) {
        this.reloadTime = reloadTime;
    }

    public int getShotBlockTimer() {
        return reloadTimer;
    }
}
