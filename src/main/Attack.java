package main;

import java.awt.*;
import java.util.*;

public class Attack extends Game{
    private Rectangle hitbox;
    private int damage, duration, hitboxPosition, attackType;
    private boolean expired;
    private ArrayList<Dummy> hitList;

    public Attack(int damage, int duration, int attackType) {
        this.hitbox = new Rectangle();
        this.expired = false;
        this.hitList = new ArrayList<>();
        this.damage = damage;
        this.duration = duration;
        this.attackType = attackType;
    }

    public void attack() {
        
    }
}


