package skilltree;

import entities.PlayerTypeEntity;
import entities.components.MovementComponent;

import java.awt.image.BufferedImage;

public class Lifesteal extends Ability {
    public Lifesteal(PlayerTypeEntity owner, int x, int y, BufferedImage icon){
        super(owner, x, y, icon);
        cost = 1; //wird in den unter klassen überschrieben
        cooldown = 4000; //in Millisekunden
        duration = 1; //in Millisekunden

    }
    @Override
    public String getDescription(){ return "heal life when you deal damage";}
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