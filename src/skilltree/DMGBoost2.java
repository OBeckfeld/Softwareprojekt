package skilltree;

import entities.PlayerTypeEntity;
import entities.components.MovementComponent;
import main.GamePanel;

import java.awt.image.BufferedImage;

public class DMGBoost2 extends Ability {
    public DMGBoost2(PlayerTypeEntity owner, int x, int y, BufferedImage icon, GamePanel gamePanel, SkillTree skillTree){
        super(owner, x, y, icon, gamePanel, skillTree);
        cost = 12;
        cooldown = 1500; //in Millisekunden
        duration = 100; //in Millisekunden
        active = false;
    }
    @Override
    public String getDescription(){ return "Plus 30 Prozent Schaden bei Gegnern";}
    @Override
    public void unlock(){
        unlocked = true;
        owner.setDamageModifier(owner.getDamageModifier()+30);
    }

    @Override
    public boolean use(){
        if (!super.use()){ return false; } //offcooldown check
        return true;
    }
    public void end(){}

}