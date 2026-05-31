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
    protected double speed = 2;
    protected Weapon weapon;
    protected double defaultSpeed = 2;
    protected int attacking = 3;
    protected HealthBar healthBar;
    protected SkillTree skillTree;
    protected int hitCooldown;
    protected int skillPoints = 0;
    protected int pointsOnDeath = 1;
    private boolean isParrying = false;

    /**
     * Erstellt eine neue PlayerTypeEntity und initialisiert grundlegende Werte
     * wie Fähigkeiten, Waffe, HealthBar, SkillTree, Bewegung und Spielreferenzen.
     */
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

    /**
     * Erhöht die aktuellen Lebenspunkte um den angegebenen Wert,
     * ohne dabei das maximale Leben zu überschreiten.
     */
    public void gainLife(int amount) {
        currentHealth = Math.min(currentHealth + amount, 100);
    }

    /**
     * Gibt den AbilityManager dieser Entity zurück.
     */
    public AbilityManager getAbilityManger(){
        return abilityManger;
    }

    /**
     * Gibt zurück, ob die Entity noch lebt.
     */
    public boolean isAlive() { return currentHealth > 0; }

    /**
     * Gibt die aktuellen Lebenspunkte zurück.
     */
    public int getCurrentHealth() { return currentHealth; }

    /**
     * Setzt die aktuellen Lebenspunkte auf den angegebenen Wert.
     */
    public void setCurrentHealth(int currentHealth) { this.currentHealth = currentHealth; }

    /**
     * Gibt die aktuelle kritische Trefferchance zurück.
     */
    public int getCritChance(){return critChance;}

    /**
     * Setzt die kritische Trefferchance auf den angegebenen Wert.
     */
    public void setCritChance(int chance){critChance = chance;}

    /**
     * Gibt die maximalen Lebenspunkte zurück.
     */
    public int getMaxHealth() { return maxHealth; }

    /**
     * Setzt die maximalen Lebenspunkte auf den angegebenen Wert.
     */
    public void setMaxHealth(int maxHealth) { this.maxHealth = maxHealth; }

    /**
     * Gibt den Grundschaden der Entity zurück.
     */
    public int getDamage() { return damage; }

    /**
     * Gibt den kritischen Schadenswert zurück.
     */
    public int getCrit(){
        return crit;
    }

    /**
     * Setzt den kritischen Schadenswert auf den angegebenen Wert.
     */
    public void setCrit(int quote){crit = quote;}

    /**
     * Gibt den aktuellen Schadensmodifikator zurück.
     */
    public int getDamageModifier() {return damageModifier;}

    /**
     * Gibt die aktuelle Blickrichtung der Entity zurück.
     */
    public int getDirection() { return direction; }

    /**
     * Setzt die Blickrichtung der Entity.
     */
    public void setDirection(int direction) { this.direction = direction; }


    /**
     * Gibt die Sichtweite der Entity zurück.
     */
    public int getViewRange(){return viewRange;}

    /**
     * Gibt alle Entities zurück, die sich innerhalb der Sichtweite befinden.
     */
    protected ArrayList<Entity> getInView(){ return registry.getInRange(this, getViewRange(), getViewRange()); }

    /**
     * Gibt den Verteidigungswert der Entity zurück.
     */
    public int getDefense(){
        return defense;
    }

    /**
     * Setzt den Verteidigungswert der Entity.
     */
    public void setDefense(int def){
        defense = def;
    }

    /**
     * Gibt die aktuelle Bewegungsgeschwindigkeit zurück.
     */
    public double getSpeed() { return speed; }

    /**
     * Setzt die Bewegungsgeschwindigkeit auf den angegebenen Wert.
     */
    public void setSpeed(double speed) { this.speed = speed; }

    /**
     * Setzt den Schadensmodifikator auf den angegebenen Wert.
     */
    public void setDamageModifier(int mod){damageModifier = mod;}

    /**
     * Setzt die aktuellen Lebenspunkte und begrenzt sie dabei auf das maximale Leben.
     */
    public void setHealth(int life){if(life>maxHealth){
        currentHealth = maxHealth;
    }
    else {
        currentHealth = life;
    }
    }

    /**
     * Gibt zurück, ob die Entity aktuell angreifen kann.
     */
    public boolean isAttacking() { return attacking <= 0; }

    /**
     * Setzt die Angriffsdauer beziehungsweise den Angriffscooldown der Entity.
     */
    public void setAttacking(int attackDuration) { this.attacking = attackDuration; }

    /**
     * Zeichnet die HealthBar dieser Entity.
     */
    public void draw(Graphics2D g){
        healthBar.draw(g);
    }

    /**
     * Fügt der Entity Schaden zu und berücksichtigt dabei Parieren,
     * Verteidigung, Rüstungsdurchdringung und den Tod der Entity.
     */
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

    /**
     * Setzt, ob die Entity gerade pariert und dadurch Schaden blockt.
     */
    public void setParrying(boolean parry){
        isParrying = parry;
    }

    /**
     * Wird aufgerufen, wenn diese Entity Schaden verursacht hat,
     * und verarbeitet Effekte wie Lifesteal.
     */
    public void dealtDamage(int dmg){
        if(Arrays.asList(skillTree.getUnlockedAbilities()).contains("Lifesteal")){
            setHealth(getCurrentHealth()+(int)(dmg/6));
        }
    }

    /**
     * Gibt zurück, wie viele Skillpoints diese Entity beim Tod gibt.
     */
    public int getPointsOnDeath(){
        return pointsOnDeath;
    }

    /**
     * Bewegt die Entity um die angegebenen Werte in X- und Y-Richtung.
     */
    public void move(double dx, double dy) {
        movement.move(this, dx, dy);
    }

    /**
     * Aktualisiert die Entity pro Tick.
     * Dabei werden Tod, Angriffszustand, Fähigkeiten und Kollisionen
     * mit anderen PlayerTypeEntities verarbeitet.
     */
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

    /**
     * Gibt die aktuelle Waffe der Entity zurück.
     */
    public Weapon getWeapon() {
        return weapon;
    }

    public void setWeapon(Weapon weapon) {
        this.weapon = weapon;
        this.weapon.setDamage(damage);
        this.weapon.setHitCooldown(hitCooldown);
    }

    /**
     * Setzt die Waffe der Entity anhand eines String-Namens.
     */
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

    /**
     * Gibt den SkillTree dieser Entity zurück.
     */
    public SkillTree getSkillTree() {
        return skillTree;
    }

    /**
     * Gibt die aktuellen Skillpoints dieser Entity zurück.
     */
    public int getSkillPoints() {
        return skillPoints;
    }

    /**
     * Setzt die Skillpoints dieser Entity auf den angegebenen Wert.
     */
    public void setSkillPoints(int points) {
        skillPoints = points;
    }
}
