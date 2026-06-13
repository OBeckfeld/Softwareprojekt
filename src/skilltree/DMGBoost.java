package skilltree;

import entities.PlayerTypeEntity;
import entities.components.MovementComponent;
import main.GamePanel;

import java.awt.image.BufferedImage;

public class DMGBoost extends Ability {
    public DMGBoost(PlayerTypeEntity owner, int x, int y, BufferedImage icon, GamePanel gamePanel, SkillTree skillTree){
        super(owner, x, y, icon, gamePanel, skillTree);
        cost = 4;
        cooldown = 1500; //in Millisekunden
        duration = 2000; //in Millisekunden
        active = false;

    }
    @Override
    public String getDescription(){ return "Plus 20 Prozent Schaden bei Gegnern";}
    @Override
    public void unlock(){
        owner.setDamageModifier(owner.getDamageModifier()+20);//20
        unlocked = true;


    }

    @Override
    public boolean use(){
        if (!super.use()){ return false; } //offcooldown check

        return true;
    }

    @Override
    public void effect() {
    }

    public void end(){}

}