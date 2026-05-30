package skilltree;

import entities.PlayerTypeEntity;
import entities.components.MovementComponent;

import java.awt.image.BufferedImage;

public class DMGNegation2 extends Ability {
    public DMGNegation2(PlayerTypeEntity owner, int x, int y, BufferedImage icon){
        super(owner, x, y, icon);
        cost = 1; //wird in den unter klassen überschrieben
        cooldown = 1500; //in Millisekunden
        duration = 2000; //in Millisekunden

    }
    @Override
    public String getDescription(){ return "damage taken reduced by 15";}
    @Override
    public void unlock(){
        unlocked = true;
        owner.setDefense(owner.getDefence()+15);

    }

    @Override
    public boolean use(){
        if (!super.use()){ return false; } //offcooldown check

        return true;
    }

    @Override
    public void effect() {
    }

    public void end(){owner.setDefense(owner.getDefence()-10);}

}