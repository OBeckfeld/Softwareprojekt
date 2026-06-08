package skilltree;

import entities.PlayerTypeEntity;
import entities.components.MovementComponent;
import main.GamePanel;

import java.awt.image.BufferedImage;

public class SpeedBoost extends Ability {
    public SpeedBoost(PlayerTypeEntity owner, int x, int y, BufferedImage icon, GamePanel gamePanel, SkillTree skillTree){
        super(owner, x, y, icon, gamePanel, skillTree);
        cost = 12;
        cooldown = 1500; //in Millisekunden
        duration = 2000; //in Millisekunden
        active = false;

    }
    @Override
    public String getDescription(){ return "Plus 1 Speed (viel)";}
    @Override
    public void unlock(){
        unlocked = true;
        owner.setSpeedBoost(owner.getSpeedBoost()+1);

    }

    @Override
    public boolean use(){
        if (!super.use()){ return false; } //offcooldown check

        return true;
    }

    @Override
    public void effect() {
    }

    public void end(){owner.setSpeed(owner.getSpeed()-3);}

}