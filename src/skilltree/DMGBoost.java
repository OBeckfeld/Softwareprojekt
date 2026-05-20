package skilltree;

import entities.PlayerTypeEntity;
import entities.components.MovementComponent;

public class DMGBoost extends Ability {
    public DMGBoost(PlayerTypeEntity owner){
        super(owner);
        cost = 1; //wird in den unter klassen überschrieben
        cooldown = 1500; //in Millisekunden
        duration = 2000; //in Millisekunden

    }
    @Override
    public String getDescription(){ return "+ 10% damage";}
    @Override
    public void unlock(){
        unlocked = true;


    }

    @Override
    public boolean use(){
        if (!super.use()){ return false; } //offcooldown check
        owner.setDamageModifier(owner.getDamageModifier()+10000);
        return true;
    }

    @Override
    public void effect() {
    }

    public void end(){owner.setDamageModifier(owner.getDamageModifier()-100);}

}