package skilltree;
import entities.PlayerTypeEntity;
import main.GamePanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;

public abstract class Ability implements MouseListener {
    private GamePanel gamePanel;
    protected boolean unlocked;
    protected boolean accessible;
    protected int cost;
    protected double cooldown;
    protected double lastUsed;
    protected PlayerTypeEntity owner;
    protected double duration;
    protected boolean isRunning;
    protected boolean active = true;
    public BufferedImage icon;
    protected int x, y;
    public int iconSize = 75;
    public int iconX;
    public int iconY;
    JLabel label;
    private SkillTree skillTree;
    public Ability(PlayerTypeEntity owner, int x, int y, BufferedImage icon, GamePanel gamePanel, SkillTree skillTree){
        this.gamePanel = gamePanel;
        this.skillTree = skillTree;
        this.x = x;
        this.y = y;
        iconSize = 75;
        iconX = x - iconSize/2;
        iconY = y - iconSize/2;
        unlocked = false; //freigeschaltet
        cost = 0; //wird in den unter klassen überschrieben
        cooldown = 0; //in Millisekunden
        lastUsed = System.currentTimeMillis();
        this.owner = owner;
        label = new JLabel(); //damit es angeklickt werden kann
        label.setBounds(iconX, iconY,iconSize,iconSize );
        label.addMouseListener(this);


        duration = 10; //in Millisekunden (wie lange die ability ihre effect() Methode aufrufen kann)
        isRunning = false;
        this.icon = icon;

    }
    public int getX(){return x;}
    public int getY(){return y;}
    public abstract String getDescription();
    public abstract void unlock();
    public boolean use() {
        if (System.currentTimeMillis() - lastUsed < cooldown || isRunning || !active){//wenn sie auf einem cooldown ist, wenn sie schon läuft, wenn sie eine passive ability ist
            return false; //ability kann nicht benutzt werden

        }

        isRunning = true;
        lastUsed = System.currentTimeMillis();
        return true;//ability kann benutzt werden
    }
    public void setAccessible(){accessible = true;}
    public boolean isUnlocked(){
        return unlocked;
    }
    public boolean isAccessible() {
        return accessible;
    }

    public void effect(){}
    public void update(){
        if (System.currentTimeMillis() - lastUsed < duration){
            effect();
        }
        else if (isRunning){//tick nachdem sie ihre duration überschritten hat
            isRunning = false;
            end();
        }
    }

    public void draw(Graphics g){

        g.drawImage(icon, iconX, iconY, iconSize, iconSize, null);
        if(accessible &! unlocked){
            g.setColor(new Color(0,0,0,100));
            g.fillRect(iconX, iconY, iconSize, iconSize);
        }
        if(!accessible){
            g.setColor(new Color(15,0,0,200));
            g.fillRect(iconX, iconY, iconSize, iconSize);
        }
    }

    @Override
    public void mouseEntered(MouseEvent e){  //code einfügen um Beschreibung der Fähigkeit anzuzeigen

    }
    @Override
    public void mouseClicked(MouseEvent e){
        if(accessible){
            skillTree.unlock(this);
        }
    }
    @Override
    public void mouseReleased(MouseEvent e){

    }
    @Override
    public void mouseExited(MouseEvent e){

    }
    @Override
    public void mousePressed(MouseEvent e){

    }

    public void end(){}
    public boolean isActiveAbility(){return active;}
}
