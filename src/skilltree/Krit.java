package skilltree;

import entities.PlayerTypeEntity;
import entities.components.MovementComponent;
import main.GamePanel;

import java.awt.image.BufferedImage;

public class Krit extends Ability {
    public Krit(PlayerTypeEntity owner, int x, int y, BufferedImage icon, GamePanel gamePanel, SkillTree skillTree){
        super(owner, x, y, icon, gamePanel, skillTree);
        cost = 12; //wird in den unter klassen überschrieben
        cooldown = 1500; //in Millisekunden
        duration = 2000; //in Millisekunden

    }
    @Override
    public String getDescription(){ return "5% Chance to deal +150% damage";}
    @Override
    public void unlock(){
        unlocked = true;
        owner.setCrit(owner.getCrit()+150);
        owner.setCritChance(owner.getCritChance()+5);

    }

    @Override
    public boolean use(){
        if (!super.use()){ return false; } //offcooldown check

        return true;
    }

    @Override
    public void effect() {
    }

    public void end(){owner.setCrit(owner.getCrit()-150);}

}