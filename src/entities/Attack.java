package entities;

import java.util.*;

import entities.managers.AttackManager;
import entities.managers.AttackRegistry;
import entities.managers.EntityRegistry;
import tools.TileManager;

public class Attack extends Entity{
    private final int damage;
    private final int duration; // gibt den Schaden und die Dauer einer Attacke an
    private int timeAlive = 0; //gibt an, wie lange eine Attacke schon aktiv ist
    private boolean expired, visible; //expired gibt an, ob eine Attacke noch aktiv ist
    private final ArrayList<Entity> hitList; //hitList speichert alle Entities, die von der Attacke getroffen werden
    private final PlayerTypeEntity owner; //Referenz auf Verursacher der Attacke, macht 'Rückschlüsse' möglich

    public Attack(double x, double y, int width, int height, EntityRegistry registry, int duration, PlayerTypeEntity owner, AttackRegistry attackRegistry, TileManager tileManager) {
        super(x, y, width, height, registry, tileManager);
        this.expired = false;
        this.visible = true;
        this.hitList = new ArrayList<>();
        this.duration = duration;
        this.damage = owner.getDamage()+owner.getDamage()* owner.getDamageModifier()/100;
        this.owner = owner;
    }
    public Attack(double x, double y, int width, int height, EntityRegistry registry, int duration, PlayerTypeEntity owner, AttackManager attackManager, int damage, TileManager tileManager) {
        super(x, y, width, height, registry, tileManager);
        this.expired = false;
        this.visible = true;
        this.hitList = new ArrayList<>();
        this.duration = duration;
        this.damage = damage* owner.getDamageModifier()/100;
        this.owner = owner;
    }

    /**
     * Attacke wird geupdated, um alte Attacken zu entfernen und die Anzahl an Attacken zu begrenzen
     */
    public void update() {
        timeAlive ++;
        if (timeAlive >= duration) {
            visible = false;
        }
        if (timeAlive >= duration) {
            expired = true;
            registry.unregister(this);
            //owner.setAttacking(false);//sagt dem owner, dass die attacke vorbei ist
            //owner.setAttack(null);
        }
    }

    public int getDuration() {
        return duration;
    }

    public boolean isExpired() {
        return expired;
    }

    public void expire() {
        timeAlive = duration;
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