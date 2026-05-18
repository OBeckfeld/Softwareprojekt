package entities;

import entities.managers.AttackManager;
import entities.managers.EntityRegistry;

public abstract class PlayerTypeEntity extends Entity {

    protected AttackManager attackManager;
    protected Attack attack;
    protected int hitCooldown, verticalRange, horizontalRange;
    protected int health  = 100;
    protected int damage  = 10;
    protected int defense = 5;
    protected int direction = 0; //0 = rechts, 1 = unten, 2 = links, 3 = oben
    protected boolean isAttacking = false;

    public PlayerTypeEntity(int x, int y, int width, int height, int hitCooldown, EntityRegistry registry, AttackManager attackManager) {
        super(x, y, width, height, registry);
        this.attackManager = attackManager;
        this.hitCooldown = hitCooldown;
        verticalRange = 120;
        horizontalRange = 60;
    }

    public void gainLife(int amount) {
        health = Math.min(health + amount, 100);
    }

    public boolean isAlive() { return health > 0; }

    public Attack getAttack() { return attack; }
    public void setAttack(Attack attack) { this.attack = attack; }

    public int getHealth()            { return health; }
    public void setHealth(int health) { this.health = health; }

    public int getDamage()            { return damage; }

    public int getDefense()           { return defense; }

    public int getDirection()         { return direction; }
    public void setDirection(int direction) { this.direction = direction; }

    public int getHitCooldown() { return hitCooldown; }

    public int getVerticalRange () { return verticalRange; }

    public int getHorizontalRange() { return horizontalRange; }

    public void setAttacking(boolean attacking){isAttacking = attacking;}
}
