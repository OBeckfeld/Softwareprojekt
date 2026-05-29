package skilltree;

import entities.PlayerTypeEntity;
import entities.components.MovementComponent;

public class Krit2 extends Ability {
    public Krit2(PlayerTypeEntity owner){
        super(owner);
        cost = 1; //wird in den unter klassen überschrieben
        cooldown = 1500; //in Millisekunden
        duration = 2000; //in Millisekunden

    }
    @Override
    public String getDescription(){ return "10% Chance to deal +200% damage";}
    @Override
    public void unlock(){
        unlocked = true;
        owner.setCrit(owner.getCrit()+100);
        owner.setCritChance(owner.getCritChance()+5);

    }

    @Override
    public boolean use(){
        if (!super.use()){ return false; } //offcooldown check

        return true;
    }

    @Override
    public void effect() {
    }

    public void end(){owner.setCrit(owner.getCrit()-200);}

}