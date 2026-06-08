package entities.enemies;
import Weapons.Weapon;
import entities.PlayerTypeEntity;
import entities.managers.AttackRegistry;
import entities.managers.EntityRegistry;
import entities.managers.AttackManager;
import main.GamePanel;
import tools.TileManager;
import tools.Vector;

public class Boss extends Enemy {

    private static final int FLEE_RANGE = 150;
    private static final int ATTACK_RANGE = 300;
    private int attackDelay;


    public Boss(int x, int y, int width, int height, EntityRegistry registry, AttackManager attackManager, TileManager tileManager, GamePanel gamePanel) {
        super(x, y, width, height, 60, registry, attackManager, tileManager, gamePanel);
        currentHealth = 1000;
        setSpeed(2);
        viewRange = 800;
        pointsOnDeath = 30;
        skillPoints = 4;
        skillTree.unlock(skillTree.getAbilityReference("dash"));
        attackDelay = 40;
    }

    public Boss(int x, int y, int width, int height, int health, int damage, int defense, int viewrange, int hitCooldown, EntityRegistry registry, AttackRegistry attackRegistry, TileManager tileManager, GamePanel gamePanel) {
        super(x, y, width, height, 60, registry, attackRegistry, tileManager, gamePanel);
        this.maxHealth = health;
        this.currentHealth = health;
        this.damage = damage;
        this.defense = defense;
        setSpeed(2);
        this.viewRange = viewRange;
        pointsOnDeath = 10;
    }

    public void setWeapon(Weapon weapon) {
        this.weapon = weapon;
        this.weapon.setDamage(damage);
    }

    @Override
    protected void handleMovement() {
        if (player == null) return; //macht nichts wenn er den Player noch nicht gesehen hat
        Vector toPlayer = new Vector(getX(), getY(), player.getX(), player.getY());
        double dist = toPlayer.getLength();

        if (dist <= ATTACK_RANGE) {
            tryAttackEntity(player);
        }

        if (player == null) return;
        Vector vector = new Vector(getX(), getY(), player.getX(), player.getY());
        if (vector.getLength() > speed) {
            vector.setLength(speed);
        }
        if (registry.getInRange(this, 100, 100).contains(player) || attackDelay != 40) {
            vector.setLength(0);
            if (attackDelay == 0) {
                tryAttackEntity(player);
                attackDelay = 40;
            } else {
                attackDelay--;
            }
        } else {
            attackDelay = 40;
        }
        move(vector);
    }



}
