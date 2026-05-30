package skilltree;

import entities.PlayerTypeEntity;
import entities.components.MovementComponent;
import main.GamePanel;

import java.awt.image.BufferedImage;

public class PoisonCloud extends Ability {
    public PoisonCloud(PlayerTypeEntity owner, int x, int y, BufferedImage icon, GamePanel gamePanel, SkillTree skillTree){
        super(owner, x, y, icon, gamePanel, skillTree);
        cost = 25; //wird in den unter klassen überschrieben
        cooldown = 4000; //in Millisekunden
        duration = 1; //in Millisekunden

    }
    @Override
    public String getDescription(){ return "create a cloud which poisons enemies";}
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