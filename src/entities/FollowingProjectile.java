package entities;

import entities.enemies.Enemy;
import entities.managers.AttackManager;
import entities.managers.AttackRegistry;
import entities.managers.EntityRegistry;
import tools.TileManager;
import tools.Vector;

public class FollowingProjectile extends Projectile{
    private PlayerTypeEntity target;
    public FollowingProjectile(double x, double y, int width, int height, EntityRegistry registry, AttackRegistry attackManager, PlayerTypeEntity owner, double speed, int direction, int timeToLive, int damage, TileManager tileManager) {
        super(x,y,width,height,registry,attackManager,owner,speed,direction,timeToLive, damage, tileManager);
    }
    public FollowingProjectile(double x, double y, int width, int height, EntityRegistry registry, AttackRegistry attackManager, PlayerTypeEntity owner, double speed, Vector vector, int timeToLive, int damage, TileManager tileManager) {
        super(x,y,width,height,registry,attackManager,owner,speed,vector,timeToLive, damage, tileManager);

    }
    @Override
    protected void logic(){
        if (target != null){
            if (!target.isAlive()){
                target = null;
            }
        }
        if (target == null) {
            int range = timeAlive*5+200;
            if (range > 500){
                range = 500;
            }
            for (Entity entity : registry.getInRange(this, range, range)) {
                if ((entity instanceof Enemy)) {
                    target = (PlayerTypeEntity) entity;
                    break;
                }
            }
        }
        for (Entity entity : registry.getInRange(this, width, height)) {
            if (!(entity instanceof  Enemy)) {
                continue;
            }
            hit();
        }
        if (movement.collidesWithWall(this, moveVector.getOffsetX(), moveVector.getOffsetY())){
            hit();
            return;
        }
        move();
    }
    @Override
    protected void move(){
        moveVector.setLength(speed);
        if(target != null) {
            Vector vTarget = new Vector(x, y, target.getCenter()[0], target.getCenter()[1]);
            vTarget.setLength(5);
            moveVector.setLength(100);
            moveVector.combineVector(vTarget);
        }
        moveVector.setLength(speed);
        move(moveVector);
    }
}
