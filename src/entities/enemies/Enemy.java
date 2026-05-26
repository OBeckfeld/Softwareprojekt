package entities.enemies;

import entities.Player;
import entities.PlayerTypeEntity;
import entities.managers.AttackRegistry;
import entities.managers.EntityRegistry;
import entities.Entity;
import tools.TileManager;
import tools.Vector;

import java.util.ArrayList;

public class Enemy extends PlayerTypeEntity {
    protected Player player;

    public Enemy(int x, int y, int width, int height, int hitCooldown, EntityRegistry registry, AttackRegistry attackRegistry, TileManager tileManager) {
        super(x, y, width, height, 100,100, registry, attackRegistry, tileManager);
        defaultSpeed = 2;
        speed = defaultSpeed;
        viewRange = 500;
        mass = 1;
        currentHealth = 100; //zu Testzwecken, für spätere Version unbedingt anpassen!!!
        player = null;
    }

    public void update() {
        super.update();
        if (currentHealth <= 0) {
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
        if (registry.getInRange(this, 100, 100).contains(player)) {
            vector.setLength(0);
            tryAttackEntity((PlayerTypeEntity) player);
        }
        move(vector);
    }

    protected void tryAttackEntity(PlayerTypeEntity targetPlayer) {
        //attacking = 100;
        direction = getDirectionTo(targetPlayer.getCenter()[0], targetPlayer.getCenter()[1]);
        //attackRegistry.newAttack(this);
        //attackRegistry.attack(attack);
        weapon.use();
    }
}
