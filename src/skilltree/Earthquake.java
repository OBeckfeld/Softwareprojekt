package skilltree;

import entities.DamageCloud;
import entities.PlayerTypeEntity;
import entities.components.MovementComponent;
import main.GamePanel;
import tools.TileManager;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Earthquake extends Ability {
    public Earthquake(PlayerTypeEntity owner, int x, int y, BufferedImage icon, GamePanel gamePanel, SkillTree skillTree){
        super(owner, x, y, icon, gamePanel, skillTree);
        cost = 25;
        cooldown = 4000; //in Millisekunden
        duration = 1; //in Millisekunden

    }
    @Override
    public String getDescription(){ return "Große AOE Attacke (Flächenschaden)";}
    @Override
    public void unlock(){
        unlocked = true;

    }

    @Override
    public boolean use(){
        if (!super.use()){ return false; } //offcooldown check
        DamageCloud cloud= new entities.DamageCloud(owner.getCenter()[0]-110,owner.getCenter()[1]-110,220,220, owner.registry, new TileManager(),120,60, 35, owner, new Color(60, 38, 38, 180), false);
        cloud.setZ(0);
        return true;
    }

    @Override
    public void effect() {
    }

    public void end(){}

}