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
    protected boolean active;
    public Ability(PlayerTypeEntity owner){
        unlocked = false;
        cost = 0; //wird in den unter klassen überschrieben
        cooldown = 0; //in Millisekunden
        lastUsed = System.currentTimeMillis();
        this.owner = owner;
        duration = -10; //in Millisekunden
        active = false;
    }
    public abstract String getDescription();
    public abstract void unlock();
    public boolean use() {
        if (System.currentTimeMillis() - lastUsed < cooldown ||active){
            return false; //ability kann nicht benutzt werden

        }

        active = true;
        lastUsed = System.currentTimeMillis();
        return true;//ability kann benutzt werden
    }
    public void effect(){}
    public void update(){
        if (System.currentTimeMillis() - lastUsed < duration){

            effect();
        }
        else if (active == true){
            active = false;
            end();
        }
    }
    public abstract void end();
}
