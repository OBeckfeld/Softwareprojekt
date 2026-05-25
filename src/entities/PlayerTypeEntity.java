package entities;

import Weapons.StarterSword;
import Weapons.Weapon;
import entities.managers.AbilityManager;
import entities.managers.AttackManager;
import entities.managers.AttackRegistry;
import entities.managers.EntityRegistry;
import tools.TileManager;
import tools.Vector;

import java.awt.event.KeyEvent;
import java.util.ArrayList;

public abstract class PlayerTypeEntity extends Entity {

    protected Attack attack;
    protected int hitCooldown, verticalRange, horizontalRange;
    protected int health;
    protected int damage;
    protected int defense;
    protected int direction = 0; //0 = rechts, 1 = unten, 2 = links, 3 = oben
    protected int attacking = 0;
    protected int viewRange;
    protected int mass;
    protected int damageModifier ;
    protected AbilityManager abilityManger;
    protected double speed = 5;
    protected Weapon weapon;
    protected double defaultSpeed = 5;
    protected boolean isAttacking = false;

    public PlayerTypeEntity(int x, int y, int width, int height, int attackDuration, int hitCooldown, EntityRegistry registry, AttackRegistry attackRegistry, TileManager tileManager) {
        super(x, y, width, height, registry, attackRegistry, tileManager);
        this.hitCooldown = hitCooldown;
        verticalRange = 120;
        horizontalRange = 60;
        abilityManger = new AbilityManager(this);
        viewRange = 300;
        mass = 2;
        damageModifier = 100;
        weapon = new StarterSword(this, attackRegistry, tileManager);
    }

    public void gainLife(int amount) {
        health = Math.min(health + amount, 100);
    }

    public boolean isAlive() { return health > 0; }

    public Attack getAttack() { return attack; }
    public void setAttack(Attack attack) { this.attack = attack; }

    public int getHealth() { return health; }
    public void setHealth(int health) { this.health = health; }

    public int getDamage() { return damage; }

    public int getDefense() { return defense; }

    public int getDamageModifier() {return damageModifier;}

    public int getDirection() { return direction; }
    public void setDirection(int direction) { this.direction = direction; }

    public int getVerticalRange () { return verticalRange; }

    public int getViewRange(){return viewRange;}

    protected ArrayList<Entity> getInView(){ return registry.getInRange(this, getViewRange()); }

    public double getSpeed() { return speed; }
    public void setSpeed(double speed) { this.speed = speed; }
    public void setDamageModifier(int mod){damageModifier = mod;}

    public int getHorizontalRange() { return horizontalRange; }

    public boolean isAttacking() { return isAttacking; }
    public void setAttacking(boolean isAttacking) { this.isAttacking = isAttacking; }

    public void takeDamage(int damage) {
        damage -= defense;
        if (damage > 0){
            health -= damage;
        }
    }

    public void move(double dx, double dy) {
        movement.move(this, dx, dy);
    }

    public void update(){
        if(health <= 0){unregister();}//wenn die Entity tod ist, macht sie nichts mehr, außer sich zu unregistern
            else {
                if (isAttacking) {
                    speed = 0;

                    //falls die Attacke vorbei ist, wird sie gelöscht
                    if (attack.isExpired()) {
                        isAttacking = false;
                        attack = null;
                    }
                } else {
                    speed = defaultSpeed;
                }

                abilityManger.update();

                ArrayList<Entity> inView = getInView();

                for (Entity entity : inView) {
                    if (!(entity instanceof PlayerTypeEntity)) {
                        continue;
                    }
                    if (registry.collidesWith(this, entity)) {
                        Vector vector = new Vector(entity.getCenter()[0], entity.getCenter()[1], getCenter()[0], getCenter()[1]);
                        vector.setLength(2 / mass);
                        move(vector);
                    }
                }
            }
    }

}
