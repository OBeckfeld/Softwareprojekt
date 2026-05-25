package entities;

import entities.managers.EntityRegistry;
import tools.TileManager;
import tools.Vector;
import entities.managers.AttackManager;
import javax.swing.*;
import java.awt.event.KeyEvent;
import java.util.Set;
import java.util.ArrayList;

public class Enemy extends PlayerTypeEntity {
    protected Player player;

    public Enemy(int x, int y, int width, int height, int hitCooldown, EntityRegistry registry, AttackManager attackManager, TileManager tileManager) {
        super(x, y, width, height, 180, registry, attackManager, tileManager);
        defaultSpeed = 2;
        speed = defaultSpeed;
        viewRange = 500;
        mass = 1;
        health = 100; //zu Testzwecken, für spätere Version unbedingt anpassen!!!
        player = null;
    }

    public Enemy(int x, int y, int width, int height, int health, int damage, int defense, int verticalRange, int horizontalRange, int hitCooldown, EntityRegistry registry, AttackManager attackManager, TileManager tileManager) {
        super(x, y, width, height, hitCooldown, registry, attackManager, tileManager);
        this.attackManager = attackManager;
        this.hitCooldown = hitCooldown;
        this.health = health;
        this.damage = damage;
        this.defense = defense;
        this.verticalRange = verticalRange;
        this.horizontalRange = horizontalRange;
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
        //attacking = 100;
        direction = getDirectionTo(targetPlayer.getCenter()[0], targetPlayer.getCenter()[1]);
        //attackManager.newAttack(this);
        //attackManager.attack(attack);
        weapon.use();
    }
}
