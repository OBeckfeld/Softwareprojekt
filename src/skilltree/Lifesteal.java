package skilltree;

import entities.PlayerTypeEntity;
import entities.components.MovementComponent;
import main.GamePanel;

import java.awt.image.BufferedImage;

public class Lifesteal extends Ability {
    public Lifesteal(PlayerTypeEntity owner, int x, int y, BufferedImage icon, GamePanel gamePanel, SkillTree skillTree){
        super(owner, x, y, icon, gamePanel, skillTree);
        cost = 25;
        cooldown = 4000; //in Millisekunden
        duration = 1; //in Millisekunden
        active = false;

    }
    @Override
    public String getDescription(){ return "Regeneriert Lebenspunkte, wenn Schaden bei Gegnern verursacht wird";}
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