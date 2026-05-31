package main;

import entities.*;
import entities.enemies.Enemy;
import tools.TextBoxManager;

import javax.swing.JPanel;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

import skilltree.Ability;
import tools.TileManager;

public class GamePanel extends JPanel implements MouseListener {
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

        setPanelSize();
    }

    public void assignPlayer(Player player){
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
        //verschiebt alles um die Kamera ab diesem Zeitpunkt
        g2d.translate(-game.getCamera().getX(), -game.getCamera().getY());

        tileManager.draw(g2d);

        for (Entity entity : new ArrayList<Entity>(game.getEntityManager().getEntities())) {
            g.setColor(Color.BLUE);
            if (entity == null || entity instanceof ViewBox){
                continue;
            }
            else if (entity instanceof Player){
                g2d.setColor(Color.BLUE);
                ((Player)entity).draw(g2d);
            }
            else if (entity instanceof Enemy){
                g.setColor(Color.RED);
                ((Enemy)entity).draw(g2d);
            }
            else if (entity instanceof Attack){
                if(((Attack)entity).isVisible()) {
                    g.setColor(Color.ORANGE);
                }
                else {
                    continue;
                }
            }
            else if (entity instanceof Door){
                if(((Door)entity).isOpen()) {
                    g.setColor(Color.GREEN);
                }
                else {
                    g.setColor(Color.GRAY);
                }
            }
            else if (entity instanceof Waypoint){
                g.setColor(Color.MAGENTA);
            }
            double x = entity.getX();
            double y = entity.getY();
            int width = entity.getWidth();
            int height = entity.getHeight();

            g.fillRect((int) Math.round(x), (int) Math.round(y), width, height);
        }

        textBoxManager.draw(g2d);
        g2d.translate(+game.getCamera().getX(), +game.getCamera().getY()); //zeichnet den Skill tree über den rest ohne verschoben zu sein
        if(player.getSkillTree().getActive()){
            player.getSkillTree().draw(g);
        }
    }

    public void addAbility(Ability ability) {
        abilities.add(ability);
    }

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
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
