package skilltree;

import entities.PlayerTypeEntity;
import entities.components.MovementComponent;

public class DMGBoost extends Ability {
    public DMGBoost(PlayerTypeEntity owner){
        super(owner);
        cost = 1; //wird in den unter klassen überschrieben
        cooldown = 1500; //in Millisekunden
        duration = 100; //in Millisekunden
    }
    @Override
    public String getDescription(){ return "+ 10% damage";}
    @Override
    public void unlock(){
        unlocked = true;
        owner.setDamageModifier(owner.getDamageModifier()+10);
    }

    @Override
    public boolean use(){
        if (!super.use()){ return false; } //offcooldown check
        return true;
    }


}