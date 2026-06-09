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

/**
 * Abstrakte Basisklasse für alle kampffähigen Entities im Spiel.
 * Erweitert Entity um Kampf-, Bewegungs- und Skillsystem-Logik.
 * Wird von Spieler und Gegnern gleichermaßen genutzt.
 */
public abstract class PlayerTypeEntity extends Entity {

    /** Maximale Trefferpunkte der Entity. */
    protected int maxHealth = 100;

    /** Aktuelle Trefferpunkte der Entity. */
    protected int currentHealth = maxHealth;

    /** Basisschadenswert der Entity. */
    protected int damage = 20;

    /** Rüstungswert in Prozent (0-100). */
    protected int defense = 0;

    /** Aktuelle Bewegungsrichtung (0=rechts, 1=unten, 2=links, 3=oben). */
    protected int direction = 0;

    /** Sichtweite der Entity in Pixeln. */
    protected int viewRange = 300;

    private boolean initialized = false;

    /** Masse der Entity, beeinflusst den Rückstoß bei Kollisionen. */
    protected int mass = 2;

    /** Kritischer Schadensmultiplikator in Prozent. */
    protected int crit;

    /** Wahrscheinlichkeit für einen kritischen Treffer in Prozent. */
    protected int critChance;

    /** Schadensmultiplikator der Entity. */
    protected int damageModifier = 1;

    /** AbilityManager für die Verwaltung der Fähigkeiten. */
    protected AbilityManager abilityManger;

    /** Referenz auf das GamePanel für UI-Interaktionen. */
    protected GamePanel gamePanel;






    /** Aktuell ausgerüstete Waffe. */
    protected Weapon weapon;

    /** Standardgeschwindigkeit der Entity. */
    protected double defaultSpeed = 3;

    /** Aktuelle Bewegungsgeschwindigkeit. */
    protected double speed = defaultSpeed;

    /** Aktueller speedBoost. */
    protected double speedBoost = 0;

    /** Verbleibende Ticks des Angriffs-Lockouts. */
    protected int attacking = 3;

    /** Healthbar der Entity. */
    protected HealthBar healthBar;

    /** SkillTree der Entity. */
    protected SkillTree skillTree;

    /** Cooldown zwischen Treffern in Ticks. */
    protected int hitCooldown;

    /** Aktuell verfügbare Skillpunkte. */
    protected int skillPoints = 0;

    /** Skillpunkte die beim Tod dieser Entity vergeben werden. */
    protected int pointsOnDeath = 1;

    /** Gibt an ob die Entity gerade pariert. */
    private boolean isParrying = false;

    /**
     * Erstellt eine neue PlayerTypeEntity mit Standardhitbox.
     *
     * @param x              X-Koordinate
     * @param y              Y-Koordinate
     * @param width          Breite der Entity
     * @param height         Höhe der Entity
     * @param attackDuration Dauer eines Angriffs in Ticks
     * @param hitCooldown    Cooldown zwischen Treffern in Ticks
     * @param registry       EntityRegistry
     * @param attackRegistry AttackRegistry
     * @param tileManager    TileManager
     * @param gamePanel      GamePanel
     */
    public PlayerTypeEntity(int x, int y, int width, int height, int attackDuration,
                            int hitCooldown, EntityRegistry registry, AttackRegistry attackRegistry,
                            TileManager tileManager, GamePanel gamePanel) {
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
        initialized = true;
    }

    /**
     * Erstellt eine neue PlayerTypeEntity mit angepasster Hitbox-Größe.
     *
     * @param x              X-Koordinate
     * @param y              Y-Koordinate
     * @param width          Breite der Entity
     * @param height         Höhe der Entity
     * @param attackDuration Dauer eines Angriffs in Ticks
     * @param hitCooldown    Cooldown zwischen Treffern in Ticks
     * @param registry       EntityRegistry
     * @param attackRegistry AttackRegistry
     * @param tileManager    TileManager
     * @param gamePanel      GamePanel
     * @param hitBoxWidth    Breite der Hitbox
     * @param hitBoxHeight   Höhe der Hitbox
     */
    public PlayerTypeEntity(int x, int y, int width, int height, int attackDuration,
                            int hitCooldown, EntityRegistry registry, AttackRegistry attackRegistry,
                            TileManager tileManager, GamePanel gamePanel,
                            int hitBoxWidth, int hitBoxHeight) {
        super(x, y, width, height, registry, tileManager, hitBoxWidth, hitBoxHeight);
        initialized = true;
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
     * Erhöht die aktuellen Trefferpunkte um den angegebenen Betrag.
     * Überschreitet nicht das Maximum.
     *
     * @param amount Anzahl der wiederherzustellenden Trefferpunkte
     */
    public void gainLife(int amount) {
        currentHealth = Math.min(currentHealth + amount, 100);
    }

    /**
     * Gibt den AbilityManager der Entity zurück.
     *
     * @return AbilityManager
     */
    public AbilityManager getAbilityManger() { return abilityManger; }

    /**
     * Gibt an ob die Entity noch lebt.
     *
     * @return true wenn currentHealth größer 0
     */
    public boolean isAlive() { return currentHealth > 0; }

    /**
     * Gibt die aktuellen Trefferpunkte zurück.
     *
     * @return Aktuelle Trefferpunkte
     */
    public int getCurrentHealth() { return currentHealth; }

    /**
     * Setzt die aktuellen Trefferpunkte.
     *
     * @param currentHealth Neue Trefferpunkte
     */
    public void setCurrentHealth(int currentHealth) { this.currentHealth = currentHealth; }

    /**
     * Gibt die Kritischer-Treffer-Chance zurück.
     *
     * @return Kritischer-Treffer-Chance in Prozent
     */
    public int getCritChance() { return critChance; }

    /**
     * Setzt die Kritischer-Treffer-Chance.
     *
     * @param chance Neue Chance in Prozent
     */
    public void setCritChance(int chance) { critChance = chance; }

    /**
     * Gibt die maximalen Trefferpunkte zurück.
     *
     * @return Maximale Trefferpunkte
     */
    public int getMaxHealth() { return maxHealth; }

    /**
     * Setzt die maximalen Trefferpunkte.
     *
     * @param maxHealth Neue maximale Trefferpunkte
     */
    public void setMaxHealth(int maxHealth) { this.maxHealth = maxHealth; }

    /**
     * Gibt den Basisschadenswert zurück.
     *
     * @return Schadenswert
     */
    public int getDamage() { return damage; }

    public void setSpeedBoost(double boost){
        speedBoost += boost;
    }
    public double getSpeedBoost(){
        return speedBoost;
    }
    /**
     * Gibt den kritischen Schadensmultiplikator zurück.
     *
     * @return Kritischer Schadensmultiplikator in Prozent
     */
    public int getCrit() { return crit; }

    /**
     * Setzt den kritischen Schadensmultiplikator.
     *
     * @param quote Neuer Multiplikator in Prozent
     */
    public void setCrit(int quote) { crit = quote; }

    /**
     * Gibt den Schadensmultiplikator zurück.
     *
     * @return Schadensmultiplikator
     */
    public int getDamageModifier() { return damageModifier; }

    /**
     * Gibt die aktuelle Bewegungsrichtung zurück.
     *
     * @return Richtung (0=rechts, 1=unten, 2=links, 3=oben)
     */
    public int getDirection() { return direction; }

    /**
     * Setzt die aktuelle Bewegungsrichtung.
     *
     * @param direction Neue Richtung
     */
    public void setDirection(int direction) { this.direction = direction; }

    /**
     * Gibt die Sichtweite der Entity zurück.
     *
     * @return Sichtweite in Pixeln
     */
    public int getViewRange() { return viewRange; }

    /**
     * Gibt alle Entities innerhalb der Sichtweite zurück.
     *
     * @return Liste der sichtbaren Entities
     */
    protected ArrayList<Entity> getInView() {
        return registry.getInRange(this, getViewRange(), getViewRange());
    }

    /**
     * Gibt den Rüstungswert zurück.
     *
     * @return Rüstungswert in Prozent
     */
    public int getDefense() { return defense; }

    /**
     * Setzt den Rüstungswert.
     *
     * @param def Neuer Rüstungswert in Prozent
     */
    public void setDefense(int def) { defense = def; }

    /**
     * Gibt die aktuelle Bewegungsgeschwindigkeit zurück.
     *
     * @return Aktuelle Geschwindigkeit
     */
    public double getSpeed() { return speed; }

    /**
     * Setzt die aktuelle Bewegungsgeschwindigkeit.
     *
     * @param speed Neue Geschwindigkeit
     */
    public void setSpeed(double speed) { this.speed = speed; }

    /**
     * Setzt den Schadensmultiplikator.
     *
     * @param mod Neuer Schadensmultiplikator
     */
    public void setDamageModifier(int mod) { damageModifier = mod; }

    /**
     * Setzt die Trefferpunkte auf den angegebenen Wert.
     * Überschreitet nicht das Maximum.
     *
     * @param life Neue Trefferpunkte
     */
    public void setHealth(int life) {
        if (life > maxHealth) {
            currentHealth = maxHealth;
        } else {
            currentHealth = life;
        }
    }

    /**
     * Gibt an ob die Entity gerade angreift.
     *
     * @return true wenn kein Angriffs-Lockout aktiv ist
     */
    public boolean isAttacking() { return attacking <= 0; }

    /**
     * Setzt den Angriffs-Lockout.
     *
     * @param attackDuration Dauer des Lockouts in Ticks
     */
    public void setAttacking(int attackDuration) { this.attacking = attackDuration; }

    /**
     * Zeichnet die Healthbar der Entity.
     *
     * @param g Graphics2D Kontext
     */
    public void draw(Graphics2D g) { healthBar.draw(g); }

    /**
     * Fügt der Entity Schaden zu.
     * Berücksichtigt Parieren, Rüstung und Rüstungsdurchdringung.
     * Setzt dead auf true wenn die HP unter 1 fallen.
     *
     * @param damage      Schadenswert
     * @param source      Angreifende Entity
     * @param armorPierce true wenn Rüstung ignoriert wird
     */
    public void takeDamage(int damage, PlayerTypeEntity source, boolean armorPierce) {
        if (isParrying) {
            Vector pushVector = new Vector(x, y, source.getX(), source.getY());
            pushVector.setLength(50);
            source.takeDamage(10,this, false);
            source.move(pushVector);
            return;
        }
        if (!armorPierce) {
            damage = damage - (int) (damage * defense) / 100;
        }
        if (damage > 0) {
            currentHealth -= damage;
        }
        if (currentHealth < 1) {
            dead = true;
        }
        if (source != null) {
            source.dealtDamage(damage);
        }
    }

    /**
     * Aktiviert oder deaktiviert den Parierzustand.
     *
     * @param parry true um zu parieren, false um aufzuhören
     */
    public void setParrying(boolean parry) { isParrying = parry; }

    /**
     * Wird aufgerufen wenn diese Entity Schaden ausgeteilt hat.
     * Aktiviert Lifesteal falls im SkillTree freigeschaltet.
     *
     * @param dmg Ausgeteilter Schaden
     */
    public void dealtDamage(int dmg) {
        if (Arrays.asList(skillTree.getUnlockedAbilities()).contains("Lifesteal")) {
            int heal = (dmg / 6);
            if(heal<=0) {
                heal =1;
            }
            setHealth(getCurrentHealth() + heal);
        }
    }

    /**
     * Gibt die Skillpunkte zurück die beim Tod vergeben werden.
     *
     * @return Skillpunkte beim Tod
     */
    public int getPointsOnDeath() { return pointsOnDeath; }

    /**
     * Bewegt die Entity um den angegebenen Versatz.
     *
     * @param dx Versatz in X-Richtung
     * @param dy Versatz in Y-Richtung
     */
    public void move(double dx, double dy) { movement.move(this, dx, dy); }

    /**
     * Aktualisiert die Entity jeden Tick.
     * Verwaltet Tod, Angriffs-Lockout, Fähigkeiten und Kollisionen mit anderen Entities.
     * Wird nur ausgeführt wenn der SkillTree nicht aktiv ist.
     */
    public void update() {
        if (!initialized) {
            return;
        }
        if (skillTree != null && !skillTree.getActive()) {
            speed = defaultSpeed + speedBoost;
            if (currentHealth <= 0) {
                // Spieler bekommt Death Screen, andere Entities werden unregistriert
                if (this instanceof Player) {
                    gamePanel.setDeathScreen(true);
                    return;
                }
                unregister();
            } else {
                // Angriffs-Lockout verwalten
                if (attacking > 0) {
                    speed = 0;
                    attacking--;
                }

                abilityManger.update();

                // Kollisionen mit anderen PlayerTypeEntities verarbeiten
                ArrayList<Entity> inView = getInView();
                for (Entity entity : inView) {
                    if (!(entity instanceof PlayerTypeEntity)) continue;
                    if (registry.collidesWith(this, entity)) {
                        Vector vector = new Vector(
                                entity.getCenter()[0], entity.getCenter()[1],
                                getCenter()[0], getCenter()[1]
                        );
                        vector.setLength(2 / mass);
                        move(vector);
                    }
                }
            }
        }
    }

    /**
     * Gibt die aktuell ausgerüstete Waffe zurück.
     *
     * @return Aktuelle Waffe
     */
    public Weapon getWeapon() { return weapon; }

    /**
     * Setzt eine neue Waffe und übernimmt Schaden und Cooldown.
     *
     * @param weapon Neue Waffe
     */
    public void setWeapon(Weapon weapon) {
        this.weapon = weapon;
        this.weapon.setDamage(damage);
        this.weapon.setLastUsed(System.currentTimeMillis());
    }

    /**
     * Setzt eine neue Waffe anhand ihres Klassennamens als String.
     * Erstellt die entsprechende Waffeninstanz automatisch.
     *
     * @param weapon Klassenname der Waffe
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
     * Gibt den SkillTree der Entity zurück.
     *
     * @return SkillTree
     */
    public SkillTree getSkillTree() { return skillTree; }

    /**
     * Gibt die aktuellen Skillpunkte zurück.
     *
     * @return Skillpunkte
     */
    public int getSkillPoints() { return skillPoints; }

    /**
     * Setzt die Skillpunkte auf den angegebenen Wert.
     *
     * @param points Neue Skillpunkte
     */
    public void setSkillPoints(int points) { skillPoints = points; }
}