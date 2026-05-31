package entities;

import java.util.*;

import entities.managers.AttackManager;
import entities.managers.AttackRegistry;
import entities.managers.EntityRegistry;
import tools.TileManager;

public class Attack extends Entity{
    private final int damage;//schon mit multiplier vom Besitzer multipliziert
    private final int duration; // gibt den Schaden und die Dauer einer Attacke an
    private int timeAlive = 0; //gibt an, wie lange eine Attacke schon aktiv ist
    private boolean expired, visible; //expired gibt an, ob eine Attacke noch aktiv ist
    private final ArrayList<Entity> hitList; //hitList speichert alle Entities, die von der Attacke getroffen werden
    private final PlayerTypeEntity owner; //Referenz auf Verursacher der Attacke, macht 'Rückschlüsse' möglich
    private boolean armorPierce = false;

    /**
     * Erstellt eine neue Attacke und berechnet den Schaden anhand
     * des Schadens und Schadensmodifikators des Besitzers.
     */
    public Attack(double x, double y, int width, int height, EntityRegistry registry, int duration, PlayerTypeEntity owner, AttackRegistry attackRegistry, TileManager tileManager) {
        super(x, y, width, height, registry, attackRegistry, tileManager);
        this.expired = false;
        this.visible = true;
        this.hitList = new ArrayList<>();
        this.duration = duration;
        this.damage = owner.getDamage() + owner.getDamage() * owner.getDamageModifier()/100;//why der Wert ist viel zu hoch, weil der Damage doppelt genommen wird
        this.owner = owner;
    }

    /**
     * Erstellt eine neue Attacke mit einem direkt übergebenen Schadenswert
     * und berechnet daraus den endgültigen Schaden mit Schadensmodifikator.
     */
    public Attack(double x, double y, int width, int height, EntityRegistry registry, int duration, PlayerTypeEntity owner, AttackManager attackManager, int damage, TileManager tileManager) {
        super(x, y, width, height, registry, attackManager, tileManager);
        this.expired = false;
        this.visible = true;
        this.hitList = new ArrayList<>();
        this.duration = duration;
        this.damage = damage + (damage * owner.getDamageModifier())/100;
        this.owner = owner;
    }

    /**
     * Attacke wird geupdated, um alte Attacken zu entfernen und die Anzahl an Attacken zu begrenzen
     */
    public void update() {
        timeAlive ++;

        if (timeAlive >= duration) {
            visible = false;
            expired = true;
            registry.unregister(this);
        }
    }

    /**
     * Legt fest, ob diese Attacke Verteidigung ignoriert.
     */
    public void setArmorPierce(boolean pierce){armorPierce = pierce;}

    /**
     * Gibt zurück, ob diese Attacke Verteidigung ignoriert.
     */
    public boolean getArmorPierce(){return armorPierce;}

    /**
     * Gibt die Dauer der Attacke zurück.
     */
    public int getDuration() {
        return duration;
    }

    /**
     * Gibt zurück, ob die Attacke abgelaufen ist.
     */
    public boolean isExpired() {
        return expired;
    }

    /**
     * Lässt die Attacke sofort ablaufen und entfernt sie beim nächsten Update.
     */
    public void expire() {
        timeAlive = duration;
        update();
    }

    /**
     * Gibt den Besitzer beziehungsweise Verursacher der Attacke zurück.
     */
    public PlayerTypeEntity getOwner() {
        return owner;
    }

    /**
     * Gibt den Schaden der Attacke zurück.
     */
    public int getDamage() {
        return damage;
    }

    /**
     * Gibt zurück, ob die Attacke sichtbar ist.
     */
    public boolean isVisible() {
        return visible;
    }

    /**
     * Gibt die Liste aller Entities zurück, die bereits von dieser Attacke getroffen wurden.
     */
    public ArrayList<Entity> getHitList() {
        return hitList;
    }
}