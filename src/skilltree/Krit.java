package skilltree;

import entities.PlayerTypeEntity;
import entities.components.MovementComponent;

public class Krit extends Ability {
    public Krit(PlayerTypeEntity owner){
        super(owner);
        cost = 1; //wird in den unter klassen überschrieben
        cooldown = 1500; //in Millisekunden
        duration = 2000; //in Millisekunden

    }
    @Override
    public String getDescription(){ return "5% Chance to deal +150% damage";}
    @Override
    public void unlock(){
        unlocked = true;
        owner.setCrit(owner.getCrit()+150);

    }

    @Override
    public boolean use(){
        if (!super.use()){ return false; } //offcooldown check

        return true;
    }

    @Override
    public void effect() {
    }

    public void end(){owner.setCrit(owner.getCrit()-150);}

}