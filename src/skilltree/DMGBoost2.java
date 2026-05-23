package skilltree;

import entities.PlayerTypeEntity;
import entities.components.MovementComponent;

public class DMGBoost2 extends Ability {
    public DMGBoost2(PlayerTypeEntity owner){
        super(owner);
        cost = 1; //wird in den unter klassen überschrieben
        cooldown = 1500; //in Millisekunden
        duration = 100; //in Millisekunden
    }
    @Override
    public String getDescription(){ return "+ 15% damage";}
    @Override
    public void unlock(){
        unlocked = true;
        owner.setDamageModifier(owner.getDamageModifier()+15);
    }

    @Override
    public boolean use(){
        if (!super.use()){ return false; } //offcooldown check
        return true;
    }
    public void end(){}

    public void end(){}
}