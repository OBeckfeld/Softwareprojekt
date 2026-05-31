package entities.enemies;

import entities.Player;
import entities.PlayerTypeEntity;
import entities.managers.AttackRegistry;
import entities.managers.EntityRegistry;
import entities.Entity;
import main.GamePanel;
import tools.TileManager;
import tools.Vector;

import java.util.ArrayList;

public class Enemy extends PlayerTypeEntity {
    protected Player player;
    private int attackDelay = 20;
    public Enemy(int x, int y, int width, int height, int hitCooldown, EntityRegistry registry, AttackRegistry attackRegistry, TileManager tileManager, GamePanel gamePanel) {
        super(x, y, width, height, 100,100, registry, attackRegistry, tileManager, gamePanel);
        defaultSpeed = 1.4;
        speed = defaultSpeed;
        viewRange = 20000;
        mass = 1;
        currentHealth = 100; //zu Testzwecken, für spätere Version unbedingt anpassen!!!
        player = null;
        pointsOnDeath = 1;
    }

    public Enemy(int x, int y, int width, int height, int health, int damage, int defense, int verticalRange, int horizontalRange, int attackDuration, int hitCooldown, EntityRegistry registry, AttackRegistry attackRegistry, TileManager tileManager, GamePanel gamePanel) {
        super(x, y, width, height, attackDuration, hitCooldown, registry, attackRegistry, tileManager, gamePanel);
        this.hitCooldown = hitCooldown;
        this.maxHealth = health;
        this.currentHealth = health;
        this.damage = damage;
        this.defense = defense;
        this.verticalRange = verticalRange;
        this.horizontalRange = horizontalRange;
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
        if(player != null) {
            if (!player.getSkillTree().getActive()) {
                handleMovement();
            }
        }
    }

    protected void handleMovement() {

            if (player == null) return;
            Vector vector = new Vector(getX(), getY(), player.getX(), player.getY());
            if (vector.getLength() > speed) {
                vector.setLength(speed);
            }
            if (registry.getInRange(this, 100, 100).contains(player) || attackDelay != 40) {
                vector.setLength(0);
                if (attackDelay == 0) {
                    tryAttackEntity((PlayerTypeEntity) player);
                    attackDelay = 40;
                } else {
                    attackDelay--;
                }
            } else {
                attackDelay = 40;
            }
            move(vector);

    }

    protected void tryAttackEntity(PlayerTypeEntity targetPlayer) {
        direction = getDirectionTo(targetPlayer.getCenter()[0], targetPlayer.getCenter()[1]);

        weapon.use();
    }
}
