package skilltree;

import entities.DamageCloud;
import entities.Entity;
import entities.PlayerTypeEntity;
import entities.components.MovementComponent;
import main.GamePanel;
import tools.TileManager;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Parry extends Ability {
    private DamageCloud cloud;
    private int shown;
    public Parry(PlayerTypeEntity owner, int x, int y, BufferedImage icon, GamePanel gamePanel, SkillTree skillTree){
        super(owner, x, y, icon, gamePanel, skillTree);
        cost = 25;
        cooldown = 2500; //in Millisekunden
        duration = 400; //in Millisekunden

    }
    @Override
    public String getDescription(){ return "Blockiert die Attacke des Gegners";}
    @Override
    public void unlock(){
        unlocked = true;

    }

    @Override
    public boolean use(){
        if (!super.use()){ return false; } //offcooldown check
        owner.setParrying(true);
        cloud= new entities.DamageCloud(owner.getX(),owner.getY(),owner.getWidth(),owner.getHeight(), owner.registry, new TileManager(),10000,10000,0,owner, Color.LIGHT_GRAY, false);
        cloud.setZ(2);
        shown = 100;
        return true;
    }

    @Override
    public void effect() {
        shown--;
        if (shown <= 0){
            cloud.registry.unregister(cloud);
        }
    }
    @Override
    public void end(){
        owner.setParrying(false);
        if (cloud != null){
            cloud.registry.unregister(cloud);
        }
    }

}