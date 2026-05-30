package skilltree;
import entities.PlayerTypeEntity;

import java.awt.*;
import java.awt.image.BufferedImage;

public abstract class Ability {
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
    public Ability(PlayerTypeEntity owner, int x, int y, BufferedImage icon){
        unlocked = false; //freigeschaltet
        cost = 0; //wird in den unter klassen überschrieben
        cooldown = 0; //in Millisekunden
        lastUsed = System.currentTimeMillis();
        this.owner = owner;
        duration = 10; //in Millisekunden (wie lange die ability ihre effect() Methode aufrufen kann)
        isRunning = false;
        this.icon = icon;
        this.x = x;
        this.y = y;
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
        int iconSize = 75;
        int iconX = x - iconSize/2;
        int iconY = y - iconSize/2;
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

    public void end(){}
    public boolean isActiveAbility(){return active;}
}
