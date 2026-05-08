package entities;

import entities.managers.EntityRegistry;

public class PlayerTypeEntity extends Entity {
    protected Attack attack; //Referenz auf die aktive Attacke der Entity
    protected int health = 100;
    protected int damage = 10;
    protected int defense = 5;
    protected int hitCooldown, verticalRange, horizontalRange;
    protected int direction = 0; //0 = rechts, 1 = unten, 2 = links, 3 = oben

    public PlayerTypeEntity(int x, int y, int height, int width, int hitCooldown, EntityRegistry registry) {
        super(x, y, height, width, registry);
        this.health = health;
        this.damage = damage;
        this.defense = defense;
        this.hitCooldown = hitCooldown;

    }

    public void setAttack(Attack attack) {
        this.attack = attack;
    }

    public Attack getAttack() {
        return attack;
    }
    
    public void setHealth(int health) {
        this.health = health;
    }

    public int getHealth() {
        return health;
    }

    public int getDamage() {
        return damage;
    }

    public int getDefense() {
        return defense;
    }

    public int getDirection() {
        return direction;
    }

    public int getHitCooldown() {
        return hitCooldown;
    }

    public int getVerticalRange() {
        return verticalRange;
    }

    public int getHorizontalRange() {
        return horizontalRange;
    }
}
