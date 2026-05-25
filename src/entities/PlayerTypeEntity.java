package entities;

import Weapons.StarterSword;
import Weapons.Weapon;
import entities.managers.AbilityManager;
import entities.managers.AttackRegistry;
import entities.managers.EntityRegistry;
import tools.HealthBar;
import tools.TileManager;
import tools.Vector;

import java.awt.*;
import java.util.ArrayList;

public abstract class PlayerTypeEntity extends Entity {

    protected Attack attack;
    protected int attackDuration, verticalRange, horizontalRange;
    protected int maxHealth = 100;
    protected int currentHealth = maxHealth;
    protected int damage  = 20;
    protected int defense = 5;
    protected int direction = 0; //0 = rechts, 1 = unten, 2 = links, 3 = oben
    protected int viewRange;
    protected int mass;
    protected int damageModifier ;
    protected AbilityManager abilityManger;
    protected double speed = 5;
    protected Weapon weapon;
    protected double defaultSpeed = 5;
    protected int hitCooldown;
    protected boolean isAttacking;
    protected HealthBar healthBar;

    public PlayerTypeEntity(int x, int y, int width, int height, int attackDuration, int hitCooldown, EntityRegistry registry, AttackRegistry attackRegistry, TileManager tileManager) {
        super(x, y, width, height, registry, attackRegistry, tileManager);
        this.hitCooldown = hitCooldown;
        this.attackDuration = attackDuration;
        verticalRange = 120;
        horizontalRange = 60;
        abilityManger = new AbilityManager(this);
        viewRange = 300;
        mass = 2;
        damageModifier = 1;
        weapon = new StarterSword(this, attackRegistry, tileManager);
        healthBar = new HealthBar(this);
        isAttacking = false;
    }

    public void gainLife(int amount) {
        currentHealth = Math.min(currentHealth + amount, 100);
    }

    public boolean isAlive() { return currentHealth > 0; }

    public Attack getAttack() { return attack; }
    public void setAttack(Attack attack) { this.attack = attack; }

    public int getCurrentHealth() { return currentHealth; }
    public void setCurrentHealth(int currentHealth) { this.currentHealth = currentHealth; }

    public int getMaxHealth() { return maxHealth; }
    public void setMaxHealth(int maxHealth) { this.maxHealth = maxHealth; }

    public int getDamage() { return damage; }

    public int getDefense() { return defense; }

    public int getDamageModifier() {return damageModifier;}

    public int getDirection() { return direction; }
    public void setDirection(int direction) { this.direction = direction; }

    public int getAttackDuration() { return attackDuration; }

    public int getVerticalRange () { return verticalRange; }

    public int getViewRange(){return viewRange;}

    protected ArrayList<Entity> getInView(){ return registry.getInRange(this, getViewRange()); }

    public double getSpeed() { return speed; }
    public void setSpeed(double speed) { this.speed = speed; }
    public void setDamageModifier(int mod){damageModifier = mod;}

    public int getHorizontalRange() { return horizontalRange; }

    public boolean isAttacking() { return isAttacking; }
    public void setAttacking(boolean isAttacking) { this.isAttacking = isAttacking; }

    public void draw(Graphics2D g){
        healthBar.draw(g);
    }

    public void takeDamage(int damage) {
        damage -= defense;
        if (damage > 0){
            currentHealth -= damage;
        }
    }

    public void move(double dx, double dy) {
        movement.move(this, dx, dy);
    }

    public void update(){
        if(currentHealth <= 0){unregister();}//wenn die Entity tod ist, macht sie nichts mehr, außer sich zu unregistern
            else {
                if (isAttacking) {
                    speed = 0;

                    try{//falls es vorkommt, dass isAttacking true ist, obwohl die Entity keine Attacke hat
                    //falls die Attacke vorbei ist, wird sie gelöscht
                    if (attack.isExpired()) {
                        isAttacking = false;
                        attack = null;
                    }
                    }
                    catch (NullPointerException e){
                        isAttacking = false;
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
