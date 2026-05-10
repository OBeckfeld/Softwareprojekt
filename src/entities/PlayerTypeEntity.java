package entities;

import entities.managers.EntityRegistry;

public abstract class PlayerTypeEntity extends Entity {

    protected int health  = 100;
    protected int damage  = 10;
    protected int defense = 5;
    protected int direction = 0;
    protected Attack attack = null;

    public PlayerTypeEntity(double x, double y, int height, int width, EntityRegistry registry) {
        super(x, y, height, width, registry);
    }

    public void attack() {
        if (attack == null || attack.isExpired()) {
            attack = new Attack(getX(), getY(), height, width, registry, 30, this);
        }
    }

    public boolean isOnCooldown() {
        return attack != null && !attack.isExpired();
    }

    public void gainLife(int amount) {
        health = Math.min(health + amount, 100);
    }

    public void loseLife(int amount) {
        int actualDamage = Math.max(0, amount - defense);
        health = Math.max(0, health - actualDamage);
    }

    public boolean isAlive() { return health > 0; }

    public int getHealth()            { return health; }
    public void setHealth(int health) { this.health = health; }
    public int getDamage()            { return damage; }
    public int getDefense()           { return defense; }
    public int getDirection()         { return direction; }
    public void setDirection(int direction) { this.direction = direction; }

}
