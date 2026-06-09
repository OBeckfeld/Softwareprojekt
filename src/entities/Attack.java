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

    public Attack(double x, double y, int width, int height, EntityRegistry registry, int duration, PlayerTypeEntity owner, AttackRegistry attackRegistry, TileManager tileManager) {
        super(x, y, width, height, registry, attackRegistry, tileManager);
        this.expired = false;
        this.visible = true;
        this.hitList = new ArrayList<>();
        this.duration = duration;
        this.damage = owner.getDamage() + owner.getDamage() * owner.getDamageModifier()/100;//why der Wert ist viel zu hoch, weil der Damage doppelt genommen wird
        this.owner = owner;
    }
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
     * Aktualisiert die Attacke und entfernt sie nach Ablauf ihrer Dauer
     */
    public void update() {
        timeAlive ++;

        if (timeAlive >= duration) {
            visible = false;
            expired = true;
            registry.unregister(this);
        }
    }

    public void setArmorPierce(boolean pierce){armorPierce = pierce;}
    public boolean getArmorPierce(){return armorPierce;}

    public int getDuration() {
        return duration;
    }

    public boolean isExpired() {
        return expired;
    }

    public void expire() {
        timeAlive = duration;
        update();
    }

    public PlayerTypeEntity getOwner() {
        return owner;
    }

    public int getDamage() {
        return damage;
    }

    public boolean isVisible() {
        return visible;
    }

    public ArrayList<Entity> getHitList() {
        return hitList;
    }
}