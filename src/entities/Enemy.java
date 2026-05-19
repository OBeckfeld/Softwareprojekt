package entities;

import entities.managers.EntityRegistry;
import tools.Vector;
import entities.managers.AttackManager;

import java.util.ArrayList;

public class Enemy extends PlayerTypeEntity {
    protected Player player;

    public Enemy(int x, int y, int width, int height, int hitCooldown, EntityRegistry registry, AttackManager attackManager) {
        super(x, y, width, height, hitCooldown, registry, attackManager);
        setSpeed(2);
        viewRange = 500;
        mass = 1;
        health = 1; //zu Testzwecken, für spätere Version unbedingt anpassen!!!
        player = null;
    }

    public void update() {
        super.update();
        if (health <= 0) {
            registry.unregister(this);
            int triggerAnimation = 1;
            return;
        }
        if (player == null) {
            ArrayList<Entity> inView = getInView();
            for (Entity entity : inView) {
                if (entity instanceof Player) {
                    player = (Player) entity;
                }
            }
        }
        handleMovement();
    }

    protected void handleMovement() {
        if (player == null) return;
        Vector vector = new Vector(getX(), getY(), player.getX(), player.getY());
        if (vector.getLength() > speed) {
            vector.setLength(speed);
        }
        if (registry.getInRange(this, 100).contains(player)) {
            vector.setLength(0);
            tryAttackEntity((PlayerTypeEntity) player);
        }
        move(vector);
    }

    protected void tryAttackEntity(PlayerTypeEntity targetPlayer) {
        direction = getDirectionTo(targetPlayer.getCenter()[0], targetPlayer.getCenter()[1]);
        attackManager.newAttack(registry, this);
        attackManager.attack(attack);
    }
}
