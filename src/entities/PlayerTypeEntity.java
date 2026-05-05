package entities;

public class PlayerTypeEntity extends Entity {
    protected Attack attack; //Referenz auf die aktive Attacke der Entity
    protected int health = 100;
    protected int damage = 10;
    protected int defense = 5;

    public PlayerTypeEntity(int x, int y, int height, int width, entityRegistry registry) {
        super(x, y, height, width, registry);
        this.health = health;
        this.damage = damage;
        this.defense = defense;
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
}
