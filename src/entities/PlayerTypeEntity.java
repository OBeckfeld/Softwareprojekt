package entities;

import Weapons.StarterSword;
import Weapons.Weapon;
import entities.managers.AbilityManager;
import entities.managers.AttackRegistry;
import entities.managers.EntityRegistry;
import main.GamePanel;
import skilltree.SkillTree;
import tools.HealthBar;
import tools.TileManager;
import tools.Vector;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

public abstract class PlayerTypeEntity extends Entity {
    protected int maxHealth = 100;
    protected int currentHealth = maxHealth;
    protected int damage  = 20;
    protected int defense = 0;
    protected int direction = 0; //0 = rechts, 1 = unten, 2 = links, 3 = oben
    protected int viewRange = 300;
    protected int mass = 2;
    protected int crit;
    protected int critChance;
    protected int damageModifier = 1;
    protected AbilityManager abilityManger;
    protected GamePanel gamePanel;
    protected double speed = 2.5;
    protected Weapon weapon;
    protected double defaultSpeed = 3;
    protected int attacking = 3;
    protected HealthBar healthBar;
    protected SkillTree skillTree;
    protected int hitCooldown;
    protected int skillPoints = 0;
    protected int pointsOnDeath = 1;
    private boolean isParrying = false;
    public PlayerTypeEntity(int x, int y, int width, int height, int attackDuration, int hitCooldown, EntityRegistry registry, AttackRegistry attackRegistry, TileManager tileManager, GamePanel gamePanel) {
        super(x, y, width, height, registry, attackRegistry, tileManager);
        abilityManger = new AbilityManager(this);
        viewRange = 500;
        mass = 2;
        damageModifier = 1;
        weapon = new StarterSword(this, attackRegistry, tileManager);
        healthBar = new HealthBar(this);
        attacking = 0;
        skillTree = new SkillTree(this, gamePanel);
        this.gamePanel = gamePanel;
    }
    public PlayerTypeEntity(int x, int y, int width, int height, int attackDuration, int hitCooldown, EntityRegistry registry, AttackRegistry attackRegistry, TileManager tileManager, GamePanel gamePanel, int hitBoxWidth, int hitBoxHeight) {
        super(x, y, width, height, registry, tileManager, hitBoxWidth, hitBoxHeight);
        abilityManger = new AbilityManager(this);
        viewRange = 500;
        mass = 2;
        damageModifier = 1;
        weapon = new StarterSword(this, attackRegistry, tileManager);
        healthBar = new HealthBar(this);
        attacking = 0;
        skillTree = new SkillTree(this, gamePanel);
        this.gamePanel = gamePanel;
    }

    public void gainLife(int amount) {
        currentHealth = Math.min(currentHealth + amount, 100);
    }
    public AbilityManager getAbilityManger(){
        return abilityManger;
    }

    public boolean isAlive() { return currentHealth > 0; }

    public int getCurrentHealth() { return currentHealth; }
    public void setCurrentHealth(int currentHealth) { this.currentHealth = currentHealth; }

    public int getCritChance(){return critChance;}
    public void setCritChance(int chance){critChance = chance;}
    public int getMaxHealth() { return maxHealth; }
    public void setMaxHealth(int maxHealth) { this.maxHealth = maxHealth; }

    public int getDamage() { return damage; }

    public int getCrit(){
        return crit;
    }
    public void setCrit(int quote){crit = quote;}
    public int getDamageModifier() {return damageModifier;}

    public int getDirection() { return direction; }
    public void setDirection(int direction) { this.direction = direction; }


    public int getViewRange(){return viewRange;}

    protected ArrayList<Entity> getInView(){ return registry.getInRange(this, getViewRange(), getViewRange()); }

    public int getDefense(){
        return defense;
    }
    public void setDefense(int def){
        defense = def;
    }

    public double getSpeed() { return speed; }
    public void setSpeed(double speed) { this.speed = speed; }

    public void setDamageModifier(int mod){damageModifier = mod;}
    public void setHealth(int life){if(life>maxHealth){
            currentHealth = maxHealth;
        }
        else {
            currentHealth = life;
    }
    }

    public boolean isAttacking() { return attacking <= 0; }
    public void setAttacking(int attackDuration) { this.attacking = attackDuration; }

    public void draw(Graphics2D g){
        healthBar.draw(g);
    }

    public void takeDamage(int damage, PlayerTypeEntity source,boolean armorPierce) {
        if (isParrying){return;}
        if (!armorPierce) {
            damage = damage - (int) (damage * defense) / 100;
        }
        if (damage > 0){
            currentHealth -= damage;
        }
        if (currentHealth < 1) {
            dead = true;
        }
        if (source != null) { // ← null check
            source.dealtDamage(damage);
        }
    }
    public void setParrying(boolean parry){
        isParrying = parry;
    }
    public void dealtDamage(int dmg){
        if(Arrays.asList(skillTree.getUnlockedAbilities()).contains("Lifesteal")){
            setHealth(getCurrentHealth()+(int)(dmg/6));
        }
    }

    public int getPointsOnDeath(){
        return pointsOnDeath;
    }

    public void move(double dx, double dy) {
        movement.move(this, dx, dy);
    }

    public void update() {
        if (skillTree != null && !skillTree.getActive()) {
            if (currentHealth <= 0) {
                if (this instanceof Player) {
                    gamePanel.setDeathScreen(true);
                    return;
                }
                unregister();
            }//wenn die Entity tod ist, macht sie nichts mehr, außer sich zu unregistern
            else {
                if (attacking > 0) {
                    speed = 0;
                    attacking--;

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

    public Weapon getWeapon() {
        return weapon;
    }

    public void setWeapon(Weapon weapon) {
        this.weapon = weapon;
        this.weapon.setDamage(damage);
        this.weapon.setHitCooldown(hitCooldown);
    }

    public void setWeapon(String weapon) {
        switch (weapon) {
            case "Gun":
                this.weapon = new Weapons.Gun(this, attackRegistry, tileManager);
                break;
            case "MiniGun":
                this.weapon = new Weapons.MiniGun(this, attackRegistry, tileManager);
                break;
            case "ShotGun":
                this.weapon = new Weapons.ShotGun(this, attackRegistry, tileManager);
                break;
            case "Rifle":
                this.weapon = new Weapons.Rifle(this, attackRegistry, tileManager);
                break;
            case "StarterSword":
                this.weapon = new StarterSword(this, attackRegistry, tileManager);
                break;
            case "IronSword":
                this.weapon = new Weapons.IronSword(this, attackRegistry, tileManager);
                break;
            default:
                this.weapon = new StarterSword(this, attackRegistry, tileManager);
        }
    }

    public SkillTree getSkillTree() {
        return skillTree;
    }

    public int getSkillPoints() {
        return skillPoints;
    }

    public void setSkillPoints(int points) {
        skillPoints = points;
    }
}
