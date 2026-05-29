package skilltree;

import entities.PlayerTypeEntity;
import entities.components.MovementComponent;

public class Heal extends Ability {
    public Heal(PlayerTypeEntity owner){
        super(owner);
        cost = 1; //wird in den unter klassen überschrieben
        cooldown = 4000; //in Millisekunden
        duration = 1; //in Millisekunden

    }
    @Override
    public String getDescription(){ return "heal 15 life";}
    @Override
    public void unlock(){
        unlocked = true;

    }

    @Override
    public boolean use(){
        if (!super.use()){ return false; } //offcooldown check
        owner.gainLife(15);
        return true;
    }

    @Override
    public void effect() {
    }

    public void end(){}

}