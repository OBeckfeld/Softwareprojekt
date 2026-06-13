package skilltree;

import entities.DamageCloud;
import entities.PlayerTypeEntity;
import entities.components.MovementComponent;
import main.GamePanel;

import java.awt.*;
import java.awt.image.BufferedImage;
import tools.TileManager;

public class PoisonCloud extends Ability {
    public PoisonCloud(PlayerTypeEntity owner, int x, int y, BufferedImage icon, GamePanel gamePanel, SkillTree skillTree){
        super(owner, x, y, icon, gamePanel, skillTree);
        cost = 25; //wird in den unter klassen überschrieben
        cooldown = 4000; //in Millisekunden
        duration = 1; //in Millisekunden

    }
    @Override
    public String getDescription(){ return "Erschafft eine Wolke, welche Gegner vergiftet";}
    @Override
    public void unlock(){
        unlocked = true;

    }

    @Override
    public boolean use(){
        if (!super.use()){ return false; } //offcooldown check
        DamageCloud cloud= new entities.DamageCloud(owner.getCenter()[0]-90,owner.getCenter()[1]-90,180,180, owner.registry, new TileManager(),600,100,15,owner, new Color(62, 185, 33, 180), true);
        cloud.setZ(2);
        return true;
    }

    @Override
    public void effect() {
    }

    public void end(){}

}