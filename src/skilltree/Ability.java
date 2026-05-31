package skilltree;

import entities.PlayerTypeEntity;
import main.GamePanel;
import tools.TextBox;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;

public abstract class Ability implements MouseListener, MouseMotionListener {
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
    private boolean mouseHovers;
    private TextBox description;
    protected JLabel label;
    private SkillTree skillTree;

    /**
     * Erstellt eine neue Ability und initialisiert Position, Icon, Besitzer,
     * SkillTree-Anbindung sowie die grundlegenden Werte wie Cooldown, Dauer
     * und Freischaltungsstatus.
     */
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

    /**
     * Gibt die X-Position der Ability im SkillTree zurück.
     */
    public int getX(){return x;}

    /**
     * Gibt die Y-Position der Ability im SkillTree zurück.
     */
    public int getY(){return y;}

    /**
     * Gibt die Beschreibung der jeweiligen Ability zurück.
     */
    public abstract String getDescription();

    /**
     * Schaltet die jeweilige Ability frei.
     */
    public abstract void unlock();

    /**
     * Versucht die Ability zu benutzen und prüft dabei Cooldown,
     * Laufstatus und ob es sich um eine aktive Ability handelt.
     */
    public boolean use() {
        if (System.currentTimeMillis() - lastUsed < cooldown || isRunning || !active){//wenn sie auf einem cooldown ist, wenn sie schon läuft, wenn sie eine passive ability ist
            return false; //ability kann nicht benutzt werden

        }

        isRunning = true;
        lastUsed = System.currentTimeMillis();
        return true;//ability kann benutzt werden
    }

    /**
     * Macht die Ability im SkillTree erreichbar.
     */
    public void setAccessible(){accessible = true;}

    /**
     * Gibt zurück, ob die Ability bereits freigeschaltet wurde.
     */
    public boolean isUnlocked(){
        return unlocked;
    }

    /**
     * Gibt zurück, ob die Ability im SkillTree erreichbar ist.
     */
    public boolean isAccessible() {
        return accessible;
    }

    /**
     * Führt den Effekt der Ability aus.
     */
    public void effect(){}

    /**
     * Aktualisiert die Ability und ruft während der Dauer ihren Effekt auf.
     * Nach Ablauf der Dauer wird die Ability beendet.
     */
    public void update(){
        if (System.currentTimeMillis() - lastUsed < duration){
            effect();
        }
        else if (isRunning){//tick nachdem sie ihre duration überschritten hat
            isRunning = false;
            end();
        }
    }

    /**
     * Gibt die Skillpoint-Kosten der Ability zurück.
     */
    public int getCost(){
        return cost;
    }

    /**
     * Zeichnet das Icon der Ability und legt je nach Zugänglichkeit
     * eine halbtransparente Überlagerung darüber.
     */
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

    /**
     * Aktualisiert den Hover-Zustand der Maus über dem Ability-Icon
     * und zeigt oder entfernt die Beschreibung im SkillTree-Menü.
     */
    public void updateMouseHovering(MouseEvent e) {
        int mouseX = e.getX();
        int mouseY = e.getY();

        boolean insideNow = (mouseX >= iconX && mouseX <= iconX + iconSize) && (mouseY >= iconY && mouseY <= iconY + iconSize);

        if (insideNow != mouseHovers) {
            mouseHovers = insideNow;

            if (mouseHovers && skillTree.getActive()) {
                if (description == null) {
                    description = new TextBox(
                            getClass().getSimpleName() + ":(e)" + getDescription() + " (e)(e)Kosten: " + cost + " Skillpoints (e)(e)Skillpoints:" + owner.getSkillPoints(),
                            iconX + iconSize + 30,
                            iconY,
                            400,
                            250,
                            true,
                            gamePanel.getTextBoxManager()
                    );
                    gamePanel.getTextBoxManager().removeTextBox(description);
                    gamePanel.getTextBoxManager().addMenuTextBox(description);
                }
            } else if (description != null) {
                description.setAlive(false);
                description = null;
            }
        }
    }

    /**
     * Reagiert auf Mausbewegungen.
     */
    @Override
    public void mouseMoved(MouseEvent e) {
        // Hover state is handled in updateMouseHovering() from GamePanel.
    }

    /**
     * Reagiert auf einen Mausklick auf die Ability und schaltet sie frei,
     * wenn sie erreichbar ist und genügend Skillpoints vorhanden sind.
     */
    @Override
    public void mouseClicked(MouseEvent e){
        if(accessible){
            if(owner.getSkillPoints() >= cost) {
                owner.getAbilityManger().unlock(this);
                if (active) {
                    owner.getAbilityManger().equip(this);
                }
            }
        }
    }

    /**
     * Wird aufgerufen, wenn die Ability nach ihrer Dauer beendet wird.
     */
    public void end(){}

    /**
     * Gibt zurück, ob es sich um eine aktive Ability handelt.
     */
    public boolean isActiveAbility(){return active;}

    /**
     * Reagiert auf das Drücken einer Maustaste.
     */
    @Override public void mousePressed(MouseEvent e) {}

    /**
     * Reagiert auf das Loslassen einer Maustaste.
     */
    @Override public void mouseReleased(MouseEvent e) {}

    /**
     * Reagiert darauf, wenn die Maus den Bereich betritt.
     */
    @Override public void mouseEntered(MouseEvent e) {}

    /**
     * Reagiert darauf, wenn die Maus den Bereich verlässt.
     */
    @Override public void mouseExited(MouseEvent e) {}

    /**
     * Reagiert auf das Ziehen der Maus.
     */
    @Override public void mouseDragged(MouseEvent e) {}
}
