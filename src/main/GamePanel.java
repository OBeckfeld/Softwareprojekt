package main;

import entities.*;
import entities.enemies.Enemy;
import skilltree.PoisonCloud;
import tools.Hitbox;
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
    private boolean showDeathScreen = false;      // ob der Death Screen angezeigt wird
    private List<Ability> abilities = new ArrayList<>();
    private Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    private int width = (int) screenSize.getWidth();
    private int height = (int) screenSize.getHeight();

    public GamePanel(Game game, TileManager tileManager, TextBoxManager textBoxManager) {
        this.game = game;
        this.tileManager = tileManager;
        this.textBoxManager = textBoxManager;
        addMouseListener(this);
        addMouseMotionListener(this);
        setPanelSize();
    }

    /**
     * Weist dem Panel den aktuellen Spieler zu.
     * Wird nach dem Erstellen oder Respawnen des Spielers aufgerufen.
     */
    public void assignPlayer(Player player) {
        this.player = player;
    }

    private void setPanelSize() {
        Dimension size = new Dimension(Game.WIDTH, Game.HEIGHT);
        setPreferredSize(size);
    }

    /**
     * Zeichnet jeden Frame neu.
     * Reihenfolge: Map → Entities → Textboxen → SkillTree → Death Screen
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        // Kamera-Offset anwenden – alles wird relativ zur Kamera gezeichnet
        g2d.translate(-game.getCamera().getX(), -game.getCamera().getY());

        // Map zeichnen
        tileManager.draw(g2d);

        // Alle Entities zeichnen
        for (Entity entity : new ArrayList<>(game.getEntityManager().getEntities())) {
            if (entity == null || entity instanceof ViewBox) continue;

            double x = entity.getX();
            double y = entity.getY();
            int width = entity.getWidth();
            int height = entity.getHeight();

            // Skalierungswerte für Sprites
            int scale = 2;
            int drawW = entity.getWidth() * scale;
            int drawH = entity.getHeight() * scale;
            int drawX = (int) Math.round(entity.getX()) - (drawW - entity.getWidth()) / 2;
            int spriteOffsetY = 80;
            int drawY = (int) Math.round(entity.getY()) - (drawH - entity.getHeight()) / 2 + spriteOffsetY;
            g2d.setColor(Color.BLACK);
            if (entity instanceof Player) {
                // Spieler-Sprite zentriert auf der Hitbox zeichnen
                BufferedImage sprite = ((Player) entity).getSprite();

                int playerDrawWidth = 80 * scale;
                int playerDrawHeight = 80 * scale;

                int playerDrawX = (int) Math.round(entity.getX()) + entity.getWidth() / 2 - playerDrawWidth / 2;
                int playerDrawY = (int) Math.round(entity.getY()) + entity.getHeight() - playerDrawHeight;

                if (sprite != null) {
                    g2d.drawImage(sprite, playerDrawX, playerDrawY + spriteOffsetY, playerDrawWidth, playerDrawHeight, null);
                } else {
                    // Fallback: blaues Rechteck wenn kein Sprite vorhanden
                    g2d.setColor(Color.BLUE);
                    g2d.fillRect(playerDrawX, playerDrawY, playerDrawWidth, playerDrawHeight);
                }

                ((Player) entity).draw(g2d); // Healthbar zeichnen
                continue;

            } else if (entity instanceof DamageCloud) {
                // DamageCloud halbtransparent zeichnen
                Color c = ((DamageCloud) entity).getColor();
                g.setColor(new Color(c.getRed(), c.getGreen(), c.getBlue(), 128));

            } else if (entity instanceof Enemy) {
                // Enemy-Sprite zeichnen
                BufferedImage sprite = ((Enemy) entity).getSprite();
                if (sprite != null) {
                    g2d.drawImage(sprite, (int) Math.round(x), (int) Math.round(y), width, height, null);
                } else {
                    // Fallback: rotes Rechteck wenn kein Sprite vorhanden
                    g.setColor(Color.RED);
                    g.fillRect((int) Math.round(x), (int) Math.round(y), width, height);
                }
                ((Enemy) entity).draw(g2d); // Healthbar zeichnen
                continue;

            } else if (entity instanceof Attack) {
                // Angriffs-Hitbox orange zeichnen wenn sichtbar
                if (((Attack) entity).isVisible()) {
                    g.setColor(Color.ORANGE);
                } else {
                    continue; // unsichtbare Attacken nicht zeichnen
                }

            } else if (entity instanceof Door) {
                // Tür grün wenn offen, grau wenn geschlossen
                g.setColor(((Door) entity).isOpen() ? Color.GREEN : Color.GRAY);

            } else if (entity instanceof Waypoint) {
                // Waypoint rot wenn unbenutzt, blau wenn benutzt
                g.setColor(((Waypoint) entity).isUsed() ? Color.BLUE : Color.RED);
            }

            g.fillRect((int) Math.round(x), (int) Math.round(y), width, height);
        }

        // Textboxen zeichnen (noch mit Kamera-Offset)
        textBoxManager.draw(g2d);

        // Kamera-Offset zurücksetzen – SkillTree und Death Screen werden ohne Offset gezeichnet
        g2d.translate(+game.getCamera().getX(), +game.getCamera().getY());

        // SkillTree zeichnen wenn aktiv
        if (player != null && player.getSkillTree() != null && player.getSkillTree().getActive()) {
            player.getSkillTree().draw(g);
        }

        // Death Screen zeichnen wenn aktiv
        if (showDeathScreen) {
            // Halbdurchsichtiger schwarzer Hintergrund
            g.setColor(new Color(0, 0, 0, 180));
            g.drawRect(0, 0, width, height);
            g.fillRect(0, 0, width, height);

            // Bestehende Menü-Textboxen entfernen und neu erstellen
            textBoxManager.clearMenuTextBoxes();

            // Todesanzeige
            TextBox deathMessage = new TextBox("Du bist gestorben",
                    width / 2 - 320 / 2, height / 3, 320, 43, true, textBoxManager);
            textBoxManager.removeTextBox(deathMessage);
            textBoxManager.addMenuTextBox(deathMessage);

            // Respawn-Button nur anzeigen wenn Fortschritt gespeichert wurde
            if (game.getProgressManager().getSavingIndex() != 1) {
                TextBox respawnButton = new TextBox("Respawn",
                        width / 2 - 143 / 2, height / 2, 143, 43, true, textBoxManager);
                textBoxManager.removeTextBox(respawnButton);
                textBoxManager.addMenuTextBox(respawnButton);
            } else {
                TextBox respawnButton = new TextBox("Kein respawn möglich",
                        width / 2 - 364 / 2, height / 2, 364, 43, true, textBoxManager);
                textBoxManager.removeTextBox(respawnButton);
                textBoxManager.addMenuTextBox(respawnButton);
            }

            // Von-vorne-Button immer anzeigen
            TextBox startOverButton = new TextBox("Von vorne beginnen",
                    width / 2 - 330 / 2, height * 2 / 3, 330, 43, true, textBoxManager);
            textBoxManager.removeTextBox(startOverButton);
            textBoxManager.addMenuTextBox(startOverButton);
        }

        // Menü-Textboxen zeichnen (Skilltree, Death Screen etc.)
        textBoxManager.drawMenuTextBoxes(g2d);
    }

    public TextBoxManager getTextBoxManager() { return textBoxManager; }

    public void clearAbilities() { abilities.clear(); }

    public void addAbility(Ability ability) {
        if (!abilities.contains(ability)) {
            abilities.add(ability);
        }
    }

    public void setDeathScreen(boolean deathScreen) { showDeathScreen = deathScreen; }

    public boolean getDeathScreen() { return showDeathScreen; }

    public Game getGame() { return game; }

    @Override
    public void mouseClicked(MouseEvent e) {
        // Respawn-Button geklickt
        if (showDeathScreen && e.getX() >= width / 2 - 143 / 2 && e.getX() <= width / 2 + 143 / 2
                && e.getY() >= height / 2 && e.getY() <= height / 2 + 43) {
            game.respawn();
        }
        // Von-vorne-Button geklickt
        else if (showDeathScreen && e.getX() >= width / 2 - 330 / 2 && e.getX() <= width / 2 + 330 / 2
                && e.getY() >= height * 2 / 3 && e.getY() <= height * 2 / 3 + 43) {
            game.startOver();
        }

        // SkillTree-Ability geklickt
        for (Ability ability : abilities) {
            if (e.getX() >= ability.iconX && e.getX() <= ability.iconX + ability.iconSize
                    && e.getY() >= ability.iconY && e.getY() <= ability.iconY + ability.iconSize) {
                ability.mouseClicked(e);
            }
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        // Maus-Hover für SkillTree-Abilities aktualisieren
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