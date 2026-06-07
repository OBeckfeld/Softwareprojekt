package skilltree;

import entities.PlayerTypeEntity;
import entities.components.MovementComponent;
import main.GamePanel;

import java.awt.image.BufferedImage;

public class DMGNegation extends Ability {
    public DMGNegation(PlayerTypeEntity owner, int x, int y, BufferedImage icon, GamePanel gamePanel, SkillTree skillTree){
        super(owner, x, y, icon, gamePanel, skillTree);
        cost = 2;
        cooldown = 1500; //in Millisekunden
        duration = 2000; //in Millisekunden
        active = false;

    }
    @Override
    public String getDescription(){ return "Erhaltener Schaden wird um 10 Prozent verringert";}
    @Override
    public void unlock(){
        unlocked = true;
        owner.setDefense(owner.getDefense()+15);

    }

    @Override
    public boolean use(){
        if (!super.use()){ return false; } //offcooldown check

        return true;
    }

    @Override
    public void effect() {
    }

    public void end(){owner.setDefense(owner.getDefense()-10);}

}