package entities;

import java.util.*;
import entities.managers.EntityRegistry;

public class Attack extends Entity{
    private int damage, lifetime; // gibt den Schaden und die Dauer einer Attacke an
    private int timeAlive = 0; //gibt an, wie lange eine Attacke schon aktiv ist
    private boolean expired; //expired gibt an, ob eine Attacke noch aktiv ist
    private ArrayList<Entity> hitList; //hitList speichert alle Entities, die von der Attacke getroffen werden
    private PlayerTypeEntity owner; //Referenz auf Verursacher der Attacke, macht 'Rückschlüsse' möglich

    public Attack(double x, double y, int width, int height, EntityRegistry registry, int lifetime, PlayerTypeEntity owner) {
    super(x, y, width, height, registry);
    this.expired = false;
    this.hitList = new ArrayList<>();
    this.lifetime = lifetime;
    this.damage = owner.getDamage();
    this.owner = owner;
    }

    /**
     * Attacke wird geupdated, um alte Attacken zu entfernen und die Anzahl an Attacken zu begrenzen
     */
    public void update() {
        timeAlive++;
        if (timeAlive >= lifetime) {
            expired = true;
            registry.unregister(this);
            owner.setAttacking(false);//sagt dem owner, dass die attacke vorbei ist
        }
    }

    public int getTimeAlive() {
        return timeAlive;
    }

    public boolean isExpired() {
        return expired;
    }

    public PlayerTypeEntity getOwner() {
        return owner;
    }

    public int getDamage() {
        return damage;
    }

    public ArrayList<Entity> getHitList() {
        return hitList;
    }
}