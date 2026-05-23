package entities;

import java.util.*;

import entities.managers.AttackManager;
import entities.managers.EntityRegistry;

public class Attack extends Entity{
    private int damage, duration, timeToSee; // gibt den Schaden und die Dauer einer Attacke an
    private int timeAlive = 0; //gibt an, wie lange eine Attacke schon aktiv ist
    private boolean expired, visible; //expired gibt an, ob eine Attacke noch aktiv ist
    private ArrayList<Entity> hitList; //hitList speichert alle Entities, die von der Attacke getroffen werden
    private PlayerTypeEntity owner; //Referenz auf Verursacher der Attacke, macht 'Rückschlüsse' möglich

    public Attack(double x, double y, int width, int height, EntityRegistry registry, int timeToLive, int duration, PlayerTypeEntity owner, AttackManager attackManager) {
    super(x, y, width, height, registry, attackManager);
    this.expired = false;
    this.visible = true;
    this.hitList = new ArrayList<>();
    this.duration = duration;
    this.timeToSee = duration; //Zeit, die eine Attacke sichtbar ist
    this.damage = owner.getDamage()+owner.getDamage()* owner.getDamageModifier()/100;

    this.owner = owner;
    }
    public Attack(double x, double y, int width, int height, EntityRegistry registry, int timeToLive, PlayerTypeEntity owner, AttackManager attackManager, int damage) {
        super(x, y, width, height, registry, attackManager);
        this.expired = false;
        this.visible = true;
        this.hitList = new ArrayList<>();
        this.timeToLive = timeToLive;
        this.timeToSee = 120; //Zeit, die eine Attacke sichtbar ist
        this.damage = damage* owner.getDamageModifier()/100;

        this.owner = owner;

    }

    /**
     * Attacke wird geupdated, um alte Attacken zu entfernen und die Anzahl an Attacken zu begrenzen
     */
    public void update() {
        timeAlive++;
        if (timeAlive >= timeToSee) {
            visible = false;
        }
        if (timeAlive >= duration) {
            expired = true;
            registry.unregister(this);
            owner.setAttacking(false);//sagt dem owner, dass die attacke vorbei ist
        }
    }

    public int timeAlive() {
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

    public boolean isVisible() {
        return visible;
    }

    public ArrayList<Entity> getHitList() {
        return hitList;
    }
}