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
     * Erstellt eine neue Ability mit Besitzer, Position, Icon, GamePanel und SkillTree.
     * Initialisiert außerdem Icon-Position, Standardwerte, Label und Mausinteraktion.
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
     * Gibt die X-Position der Ability zurück.
     */
    public int getX(){return x;}

    /**
     * Gibt die Y-Position der Ability zurück.
     */
    public int getY(){return y;}

    /**
     * Gibt die Beschreibung der Ability zurück.
     * Muss von Unterklassen implementiert werden.
     */
    public abstract String getDescription();

    /**
     * Schaltet die Ability frei.
     * Muss von Unterklassen implementiert werden.
     */
    public abstract void unlock();

    /**
     * Versucht, die Ability zu benutzen.
     * Prüft dabei Cooldown, laufenden Zustand und ob die Ability aktiv nutzbar ist.
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
     * Macht die Ability im SkillTree zugänglich.
     */
    public void setAccessible(){accessible = true;}

    /**
     * Gibt zurück, ob die Ability bereits freigeschaltet wurde.
     */
    public boolean isUnlocked(){
        return unlocked;
    }

    /**
     * Gibt zurück, ob die Ability aktuell zugänglich ist.
     */
    public boolean isAccessible() {
        return accessible;
    }

    /**
     * Führt den Effekt der Ability aus.
     * Kann von Unterklassen überschrieben werden.
     */
    public void effect(){}

    /**
     * Aktualisiert den Zustand der Ability.
     * Ruft während der Laufzeit den Effekt auf und beendet die Ability nach Ablauf der Dauer.
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
     * Gibt die Kosten der Ability in Skillpoints zurück.
     */
    public int getCost(){
        return cost;
    }

    /**
     * Zeichnet das Icon der Ability und legt je nach Zugänglichkeit oder Freischaltung eine Abdunklung darüber.
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
     * Aktualisiert, ob sich die Maus über dem Ability-Icon befindet.
     * Erstellt bei Hover eine Beschreibung und entfernt sie wieder, wenn die Maus das Icon verlässt.
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
     * Wird aufgerufen, wenn die Maus bewegt wird.
     * Die Hover-Logik wird hier nicht direkt ausgeführt, sondern über updateMouseHovering().
     */
    @Override
    public void mouseMoved(MouseEvent e) {
        // Hover state is handled in updateMouseHovering() from GamePanel.
    }

    /**
     * Wird aufgerufen, wenn auf die Ability geklickt wird.
     * Schaltet die Ability frei, wenn sie zugänglich ist und der Besitzer genug Skillpoints besitzt.
     * Aktive Abilities werden danach direkt ausgerüstet.
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
     * Beendet die Ability nach Ablauf ihrer Dauer.
     * Kann von Unterklassen überschrieben werden.
     */
    public void end(){}

    /**
     * Gibt zurück, ob es sich um eine aktiv nutzbare Ability handelt.
     */
    public boolean isActiveAbility(){return active;}

    /**
     * Wird aufgerufen, wenn eine Maustaste gedrückt wird.
     * Wird aktuell nicht verwendet.
     */
    @Override public void mousePressed(MouseEvent e) {}

    /**
     * Wird aufgerufen, wenn eine Maustaste losgelassen wird.
     * Wird aktuell nicht verwendet.
     */
    @Override public void mouseReleased(MouseEvent e) {}

    /**
     * Wird aufgerufen, wenn die Maus den Bereich betritt.
     * Wird aktuell nicht verwendet.
     */
    @Override public void mouseEntered(MouseEvent e) {}

    /**
     * Wird aufgerufen, wenn die Maus den Bereich verlässt.
     * Wird aktuell nicht verwendet.
     */
    @Override public void mouseExited(MouseEvent e) {}

    /**
     * Wird aufgerufen, wenn die Maus gezogen wird.
     * Wird aktuell nicht verwendet.
     */
    @Override public void mouseDragged(MouseEvent e) {}
}
