package main;

import entities.*;
import entities.enemies.Enemy;
import tools.TextBoxManager;

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
    private List<Ability> abilities = new ArrayList<>();

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

            }  else if (entity instanceof Enemy) {
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
                g.setColor(Color.MAGENTA);
            }

            g.fillRect((int) Math.round(x), (int) Math.round(y), width, height);
        }

        textBoxManager.draw(g2d);

        g2d.translate(+game.getCamera().getX(), +game.getCamera().getY());

        if (player != null && player.getSkillTree() != null && player.getSkillTree().getActive()) {
            player.getSkillTree().draw(g);
        }

        textBoxManager.drawSkillTreeBoxes(g2d);
    }

    public TextBoxManager getTextBoxManager() {
        return textBoxManager;
    }

    public void addAbility(Ability ability) {
        abilities.add(ability);
    }

    public Game getGame() { return game; }

    @Override
    public void mouseClicked(MouseEvent e) {
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