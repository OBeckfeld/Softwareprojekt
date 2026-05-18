package skilltree;
import entities.PlayerTypeEntity;

import java.time.Duration;
import java.time.LocalTime;
public abstract class Ability {
    protected boolean unlocked;
    protected int cost;
    protected double cooldown;
    protected double lastUsed;
    protected PlayerTypeEntity owner;
    protected double duration;
    public Ability(PlayerTypeEntity owner){
        unlocked = false;
        cost = 0; //wird in den unter klassen überschrieben
        cooldown = 0; //in Millisekunden
        lastUsed = System.currentTimeMillis();
        this.owner = owner;
        duration = 0; //in Millisekunden
    }
    public abstract String getDescription();
    public abstract void unlock();
    public boolean use() {
        if (System.currentTimeMillis() - lastUsed < cooldown){
            return false; //ability kann nicht benutzt werden
        }
        lastUsed = System.currentTimeMillis();
        return true;//ability kann benutzt werden
    }
    public void effect(){}
    public void update(){
        if (System.currentTimeMillis() - lastUsed < duration){
            effect();
        }
    }
}
