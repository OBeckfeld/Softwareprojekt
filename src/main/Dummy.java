package main;

import java.awt.*;
import java.util.*;

public class Dummy {
    private int health = 100;
    private int maxHealth = 100;
    private int attack = 10;
    private int defense = 5;
    private Dummy weapon = new Dummy(100, 100, 10, 5);

    public Dummy(int health, int maxHealth, int attack, int defense) {
        this.health = health;
        this.maxHealth = maxHealth;
        this.attack = attack;
        this.defense = defense;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public void setMaxHealth(int maxHealth) {
        this.maxHealth = maxHealth;
    }
    
    public int getAttack() {
        return attack;
    }

    public void setAttack(int attack) {
        this.attack = attack;
    }

    public int getDefense() {
        return defense;
    }

    public void setDefense(int defense) {
        this.defense = defense;
    }

    public Dummy getWeapon() {
        return weapon;
    }

    public void testVoid() {
    }

    public boolean testBoolean() {
        return true;
    }

    public ArrayList<Dummy> testList() {
        return new ArrayList<>();
    }
}