package main;

import entities.*;
import entities.enemies.Enemy;
import skilltree.PoisonCloud;
import tools.TextBoxManager;
import tools.TextBox;

import javax.swing.JPanel;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import skilltree.Ability;
import tools.TileManager;

public class GamePanel extends JPanel implements MouseListener, MouseMotionListener {
    private Game game;
    private TileManager tileManager;
    private TextBoxManager textBoxManager;
    private Player player;
    private boolean showDeathScreen = false;
    private List<Ability> abilities = new ArrayList<>();
    private Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    private int width = (int)screenSize.getWidth();
    private int height = (int)screenSize.getHeight();

    public GamePanel(Game game, TileManager tileManager, TextBoxManager textBoxManager) {
        this.game = game;
        this.tileManager = tileManager;
        this.textBoxManager = textBoxManager;
        addMouseListener(this);
        addMouseMotionListener(this);
        setPanelSize();
    }

    public void assignPlayer(Player player) {
        this.player = player;
    }

    private void setPanelSize() {
        Dimension size = new Dimension(1920, 1080);
        setPreferredSize(size);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        g2d.translate(-game.getCamera().getX(), -game.getCamera().getY());

        tileManager.draw(g2d);

        for (Entity entity : new ArrayList<>(game.getEntityManager().getEntities())) {
            if (entity == null || entity instanceof ViewBox) continue;

            double x = entity.getX();
            double y = entity.getY();
            int width = entity.getWidth();
            int height = entity.getHeight();

            if (entity instanceof Player){
                BufferedImage sprite = ((Player)entity).getSprite();
                if (sprite != null) {
                    g2d.drawImage(sprite, (int) Math.round(entity.getX()), (int) Math.round(entity.getY()),
                            entity.getWidth(), entity.getHeight(), null);
                } else {
                    g2d.setColor(Color.BLUE);
                    g2d.fillRect((int) Math.round(entity.getX()), (int) Math.round(entity.getY()),
                            entity.getWidth(), entity.getHeight());
                }
                ((Player)entity).draw(g2d); // Healthbar
                continue;


            }
            else if (entity instanceof DamageCloud){
                Color c = ((DamageCloud) entity).getColor();
                g.setColor(new Color(c.getRed(), c.getGreen(), c.getBlue(), 128));
            }
            else if (entity instanceof Enemy) {
            BufferedImage sprite = ((Enemy) entity).getSprite();
            if (sprite != null) {
                g2d.drawImage(sprite, (int) Math.round(x), (int) Math.round(y), width, height, null);
            } else {
                g.setColor(Color.RED);
                g.fillRect((int) Math.round(x), (int) Math.round(y), width, height);
            }
            ((Enemy) entity).draw(g2d); //
            continue;

            } else if (entity instanceof Attack) {
                if (((Attack) entity).isVisible()) {
                    g.setColor(Color.ORANGE);
                } else {
                    continue;
                }
            } else if (entity instanceof Door) {
                g.setColor(((Door) entity).isOpen() ? Color.GREEN : Color.GRAY);
            } else if (entity instanceof Waypoint) {
                if (!((Waypoint)entity).isUsed()) {
                    g.setColor(Color.RED);
                }
                else {
                    g.setColor(Color.BLUE);
                }
            }

            g.fillRect((int) Math.round(x), (int) Math.round(y), width, height);
        }

        textBoxManager.draw(g2d);

        g2d.translate(+game.getCamera().getX(), +game.getCamera().getY());

        if (player != null && player.getSkillTree() != null && player.getSkillTree().getActive()) {
            player.getSkillTree().draw(g);
        }

        // Der death screen wird gezeichnet/geladen
        if (showDeathScreen) {
            // Hintergrund wird grau
            g.setColor(new Color(0,0,0,180));
            g.drawRect(0,0,width,height);
            g.fillRect(0,0,width,height);
            // bestehende Menüs werden entfernt
            textBoxManager.clearMenuTextBoxes();
            // Textboxen für die buttons werden hinzugefügt
            // Infobox für Spielertod
            TextBox deathMessage = new TextBox("Du bist gestorben", width/2 - 320/2, height/3, 320, 43, true, textBoxManager);
            textBoxManager.removeTextBox(deathMessage);
            textBoxManager.addMenuTextBox(deathMessage);
            // falls gespeicherter fortschritt vorliegt, wird die möglichkeit zum respawn angezeigt
            if (game.getProgressManager().getSavingIndex() != 1) {
                TextBox respawnButton = new TextBox("Respawn", width/2 - 143/2, height/2, 143, 43, true, textBoxManager);
                textBoxManager.removeTextBox(respawnButton);
                textBoxManager.addMenuTextBox(respawnButton);
            }
            //falls kein gespeicherter fortschritt vorliegt, wird dies angezeigt
            else {
                TextBox respawnButton = new TextBox("Kein respawn möglich", width/2 - 364/2, height/2, 364, 43, true, textBoxManager);
                textBoxManager.removeTextBox(respawnButton);
                textBoxManager.addMenuTextBox(respawnButton);
            }
            // Möglichkeit zum von vorne beginnen wird angezeigt
            TextBox startOverButton = new TextBox("Von vorne beginnen", width/2 - 330/2, height * 2/3, 330, 43, true, textBoxManager);
            textBoxManager.removeTextBox(startOverButton);
            textBoxManager.addMenuTextBox(startOverButton);
        }
        // Menü textboxen werden gezeichnet
        textBoxManager.drawMenuTextBoxes(g2d);
    }

    public TextBoxManager getTextBoxManager() {
        return textBoxManager;
    }

    public void addAbility(Ability ability) {
        abilities.add(ability);
    }

    public void setDeathScreen(boolean deathScreen) {showDeathScreen = deathScreen;}

    public boolean getDeathScreen() {return showDeathScreen;}

    public Game getGame(){return game;}

    @Override
    public void mouseClicked(MouseEvent e) {
        // Falls der respawn button existiert und angeklickt wird, respawnt der Spieler
        if (showDeathScreen && e.getX() >= width/2 - 143/2 && e.getX() <= width/2 + 143/2
            && e.getY() >= height/2 && e.getY() <= height/2 + 43) {
                game.respawn();
            }
        // Falls der startOver button existiert und angeklickt wird, startet der Spieler vom Anfang an
        else if(showDeathScreen && e.getX() >= width/2 - 330/2 && e.getX() <= width/2 + 330/2
            && e.getY() >= height * 2/3 && e.getY() <= height * 2/3 + 43) {
                game.startOver();
            }
        for (Ability ability : abilities) {
            if (e.getX() >= ability.iconX && e.getX() <= ability.iconX + ability.iconSize
                    && e.getY() >= ability.iconY && e.getY() <= ability.iconY + ability.iconSize) {
                ability.mouseClicked(e);
            }
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        for (Ability ability : abilities) {
            ability.updateMouseHovering(e);
        }
    }

    @Override public void mousePressed(MouseEvent e) {}
    @Override public void mouseReleased(MouseEvent e) {}
    @Override public void mouseEntered(MouseEvent e) {}
    @Override public void mouseExited(MouseEvent e) {}
    @Override public void mouseDragged(MouseEvent e) {}
}