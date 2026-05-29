package skilltree;

import entities.PlayerTypeEntity;
import entities.components.MovementComponent;

public class SpeedBoost extends Ability {
    public SpeedBoost(PlayerTypeEntity owner){
        super(owner);
        cost = 1; //wird in den unter klassen überschrieben
        cooldown = 1500; //in Millisekunden
        duration = 2000; //in Millisekunden

    }
    @Override
    public String getDescription(){ return "+1 Speed (a lot)";}
    @Override
    public void unlock(){
        unlocked = true;
        owner.setSpeed(owner.getSpeed()+1);

    }

    @Override
    public boolean use(){
        if (!super.use()){ return false; } //offcooldown check

        return true;
    }

    @Override
    public void effect() {
    }

    public void end(){owner.setSpeed(owner.getSpeed()-1);}

}