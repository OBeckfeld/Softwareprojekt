package entities;

import Weapons.StarterSword;
import Weapons.Weapon;
import entities.managers.AbilityManager;
import entities.managers.AttackManager;
import entities.managers.EntityRegistry;
import tools.Vector;

import java.awt.event.KeyEvent;
import java.util.ArrayList;

public abstract class PlayerTypeEntity extends Entity {

    protected Attack attack;
    protected int attackDuration, verticalRange, horizontalRange;
    protected int health  = 10;
    protected int damage  = 34;
    protected int defense = 0;
    protected int direction = 0; //0 = rechts, 1 = unten, 2 = links, 3 = oben
    protected int attacking = 0;
    protected int viewRange;
    protected int mass;
    protected int damageModifier ;
    protected AbilityManager abilityManger;
    protected double speed = 5;
    protected Weapon weapon;
    protected double defaultSpeed = 5;
    protected int hitCooldown;

    public PlayerTypeEntity(int x, int y, int width, int height, int attackDuration,int hitCooldown, EntityRegistry registry, AttackManager attackManager) {
        super(x, y, width, height, registry, attackManager);
        this.hitCooldown = hitCooldown;
        this.attackManager = attackManager;
        this.attackDuration = attackDuration;
        verticalRange = 120;
        horizontalRange = 60;
        abilityManger = new AbilityManager(this);
        viewRange = 300;
        mass = 2;
        damageModifier = 100;

        weapon = new StarterSword(this, attackManager);
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

    public int getDamageModifier(){return damageModifier;}

    public int getDirection()         { return direction; }
    public void setDirection(int direction) { this.direction = direction; }

    public int getAttackDuration() { return attackDuration; }

    public int getVerticalRange () { return verticalRange; }

    public int getViewRange(){return viewRange;}

    protected ArrayList<Entity> getInView(){ return registry.getInRange(this, getViewRange()); }

    public double getSpeed() { return speed; }
    public void setSpeed(double speed) { this.speed = speed; }
    public void setDamageModifier(int mod){damageModifier = mod;}

    public int getHorizontalRange() { return horizontalRange; }

    public void setAttacking(int attacking){this.attacking = attacking;}

    public void update(){
        if(attacking >0){
            speed = 0;
            attacking --;
        }
        else{
            speed = defaultSpeed;
        }
        abilityManger.update();
        ArrayList<Entity> inView = getInView();
        for (Entity entity : inView) {
            if (!(entity instanceof PlayerTypeEntity)){continue;}
            if (registry.collidesWith(this, entity)) {
                Vector vector = new Vector(entity.getCenter()[0], entity.getCenter()[1], getCenter()[0], getCenter()[1]);
                vector.setLength(2/mass);
                move(vector);
            }
        }
    }

}
