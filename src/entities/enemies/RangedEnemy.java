package entities.enemies;
import entities.PlayerTypeEntity;
import entities.managers.EntityRegistry;
import entities.managers.AttackRegistry;
import main.GamePanel;
import tools.TileManager;
import tools.Vector;
import Weapons.Weapon;

public class RangedEnemy extends Enemy {

    private static final int FLEE_RANGE = 150;
    private static final int ATTACK_RANGE = 300;

    public RangedEnemy(int x, int y, int width, int height,
                       EntityRegistry registry, AttackRegistry attackRegistry, TileManager tileManager, GamePanel gamePanel) {
        super(x, y, width, height, 60, registry, attackRegistry, tileManager, gamePanel);
        currentHealth = 30;
        setSpeed(3);
        viewRange = 400;
        hitCooldown = 5;
        pointsOnDeath = 2;
    }

    public RangedEnemy(int x, int y, int width, int height, int health, int damage, int defense, int viewrange, int hitCooldown, EntityRegistry registry, AttackRegistry attackRegistry, TileManager tileManager, GamePanel gamePanel) {
        super(x, y, width, height, 60, registry, attackRegistry, tileManager, gamePanel);
        this.maxHealth = health;
        this.currentHealth = health;
        this.damage = damage;
        this.hitCooldown = hitCooldown;
        this.defense = defense;
        setSpeed(3);
        this.viewRange = viewRange;
        pointsOnDeath = 2;
    }

    @Override
    protected void handleMovement() {
        if (player == null) return;
        Vector toPlayer = new Vector(getX(), getY(), player.getX(), player.getY());
        double dist = toPlayer.getLength();

        if (dist < FLEE_RANGE) { // umgekehrt also läuft vom Spieler weg, um Distanz zu erschaffen
            Vector fleeVector = new Vector(player.getX(), player.getY(), getX(), getY());
            fleeVector.setLength(getSpeed());
            move(fleeVector);
        } else if (dist <= ATTACK_RANGE) {
            tryAttackEntity((PlayerTypeEntity) player);
        }
    }

    public void setWeapon(Weapon weapon) {
        this.weapon = weapon;
        this.weapon.setDamage(damage);
        this.weapon.setHitCooldown(hitCooldown);
    }
}
