package entities;

import java.awt.*;
import java.util.*;

public class Attack extends Entity{
    private int damage, timeToLive, attackType; //attackType kann später für verschiedene Angriffsarten genutzt werden
    private int timeAlive = 0;
    private boolean expired; //expired gibt an, ob eine Attacke noch aktiv ist
    private ArrayList<Entity> hitList; //hitList speichert alle Entities, die von der Attacke getroffen werden
    private PlayerTypeEntity owner; //Referenz auf Verursacher der Attacke, macht 'Rückschlüsse' möglich

    public Attack(double x, double y, int height, int width, entityRegistry registry, int damage, int timeToLive, int attackType, PlayerTypeEntity owner) {
    super(x, y, height, width, registry);
    this.expired = false;
    this.hitList = new ArrayList<>();
    this.damage = damage;
    this.timeToLive = timeToLive;
    this.attackType = attackType;
    this.owner = owner;
    }

    /**
     * Attacke wird geupdated, um alte Attacken zu entfernen und die Anzahl an Attacken zu begrenzen
     */
    public void update() {
        timeAlive++;
        if (timeAlive >= timeToLive) {
            expired = true;
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

    public ArrayList<Entity> getHitList() {
        return hitList;
    }
}