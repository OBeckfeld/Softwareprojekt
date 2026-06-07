package entities.components;

import entities.Entity;
import entities.Player;
import entities.PlayerTypeEntity;
import inputs.KeyboardInputs;
import tools.Vector;

import java.awt.event.KeyEvent;

import tools.TileManager;

/**
 * Verwaltet die Bewegung von Entities auf der Map.
 * Prüft Kollisionen mit Wänden und verhindert das Durchdringen von Tiles.
 * Bietet verschiedene Methoden für spielergesteuerte und automatische Bewegung.
 */
public class MovementComponent {

    /** Tastatureingaben des Spielers. */
    private KeyboardInputs inputs;

    /** TileManager für den Zugriff auf die Tilemap und Kollisionsprüfungen. */
    private TileManager tileManager;

    /**
     * Erstellt eine MovementComponent ohne Tastatureingaben.
     * Geeignet für Entities die sich automatisch bewegen (z.B. Gegner).
     *
     * @param tileManager TileManager für Kollisionsprüfungen
     */
    public MovementComponent(TileManager tileManager) {
        this.tileManager = tileManager;
    }

    /**
     * Erstellt eine MovementComponent mit Tastatureingaben.
     * Geeignet für den Spieler.
     *
     * @param inputs      Tastatureingaben des Spielers
     * @param tileManager TileManager für Kollisionsprüfungen
     */
    public MovementComponent(KeyboardInputs inputs, TileManager tileManager) {
        this.tileManager = tileManager;
        this.inputs = inputs;
    }

    /**
     * Bewegt den Spieler anhand der aktuellen Tastatureingaben.
     * Prüft Kollisionen mit Wänden in alle 4 Richtungen.
     * Normalisiert diagonale Bewegung auf gleiche Geschwindigkeit.
     * Aktualisiert Position und Hurtbox des Spielers.
     *
     * @param entity Der zu bewegende Spieler
     */
    public void move(Player entity) {
        if (inputs == null) {
            entity.setMoving(false);
            return;
        }
        entity.setMoving(true);
        double dx = 0, dy = 0;
        int tileSize = tileManager.getTileSize();

        if (inputs.getHeldKeys().contains(KeyEvent.VK_W)) {
            dy -= entity.getSpeed();
            entity.setDirection(3);
        }
        if (inputs.getHeldKeys().contains(KeyEvent.VK_S)) {
            dy += entity.getSpeed();
            entity.setDirection(1);
        }
        if (inputs.getHeldKeys().contains(KeyEvent.VK_A)) {
            dx -= entity.getSpeed();
            entity.setDirection(2);
        }
        if (inputs.getHeldKeys().contains(KeyEvent.VK_D)) {
            dx += entity.getSpeed();
            entity.setDirection(0);
        }

        // Diagonale Bewegung normalisieren damit die Geschwindigkeit gleich bleibt
        if (dx != 0 && dy != 0) {
            dx = dx * 0.70710678118;
            dy = dy * 0.70710678118;
        }

        int playerWidth = (int) entity.getHurtbox().getWidth();
        int playerHeight = (int) entity.getHurtbox().getHeight();
        double newX = entity.getX() + dx;
        double newY = entity.getY() + dy;

        // Kollision rechts prüfen
        if (dx > 0) {
            double right = newX + playerWidth;
            double tileCol = right / tileSize;
            double tileRowTop = entity.getY() / tileSize;
            double tileRowBottom = (entity.getY() + playerHeight) / tileSize;
            if (isWall((int) tileRowTop, (int) tileCol) ||
                    isWall((int) tileRowBottom, (int) tileCol)) {
                newX = tileCol * tileSize - playerWidth - entity.getSpeed();
            }
            // Kollision links prüfen
        } else if (dx < 0) {
            double tileCol = newX / tileSize;
            double tileRowTop = entity.getY() / tileSize;
            double tileRowBottom = (entity.getY() + playerHeight - 1) / tileSize;
            if (isWall((int) tileRowTop, (int) tileCol) ||
                    isWall((int) tileRowBottom, (int) tileCol)) {
                newX = tileCol * tileSize + entity.getSpeed();
            }
        }

        // Kollision unten prüfen
        if (dy > 0) {
            double bottomEdge = newY + playerHeight;
            double tileRow = bottomEdge / tileSize;
            double tileColLeft = entity.getX() / tileSize;
            double tileColRight = (entity.getX() + playerWidth - 1) / tileSize;
            if (isWall((int) tileRow, (int) tileColLeft) ||
                    isWall((int) tileRow, (int) tileColRight)) {
                newY = tileRow * tileSize - playerHeight - entity.getSpeed();
            }
            // Kollision oben prüfen
        } else if (dy < 0) {
            double tileRow = newY / tileSize;
            double tileColLeft = entity.getX() / tileSize;
            double tileColRight = (entity.getX() + playerWidth - 1) / tileSize;
            if (isWall((int) tileRow, (int) tileColLeft) ||
                    isWall((int) tileRow, (int) tileColRight)) {
                newY = tileRow * tileSize + entity.getSpeed();
            }
        }

        entity.setX(newX);
        entity.setY(newY);

        // Hurtbox wird zentriert unter dem Sprite positioniert
        double hitboxX = newX + (entity.getWidth() - entity.getHurtbox().getWidth()) / 2.0;
        double hitboxY = newY + (entity.getHeight() - entity.getHurtbox().getHeight());
        entity.getHurtbox().setLocation(hitboxX, hitboxY);
    }

    /**
     * Bewegt eine beliebige Entity um den angegebenen Versatz.
     * Prüft Kollisionen mit Wänden in X- und Y-Richtung getrennt.
     * Aktualisiert Position und Hurtbox der Entity.
     *
     * @param entity Die zu bewegende Entity
     * @param dx     Versatz in X-Richtung
     * @param dy     Versatz in Y-Richtung
     */
    public void move(Entity entity, double dx, double dy) {

        int tileSize = tileManager.getTileSize();
        int playerWidth = entity.getWidth();
        int playerHeight = entity.getHeight();
        double newX = entity.getX() + dx;
        double newY = entity.getY() + dy;

        // X-Kollision rechts prüfen
        if (dx > 0) {
            double right = newX + playerWidth - 1;
            int tileCol = (int) (right / tileSize);
            int tileRowTop = (int) (entity.getY() / tileSize);
            int tileRowBottom = (int) ((entity.getY() + playerHeight - 1) / tileSize);
            if (isWall(tileRowTop, tileCol) || isWall(tileRowBottom, tileCol)) {
                newX = tileCol * tileSize - playerWidth;
            }
            // X-Kollision links prüfen
        } else if (dx < 0) {
            double left = newX;
            int tileCol = (int) (left / tileSize);
            int tileRowTop = (int) (entity.getY() / tileSize);
            int tileRowBottom = (int) ((entity.getY() + playerHeight - 1) / tileSize);
            if (isWall(tileRowTop, tileCol) || isWall(tileRowBottom, tileCol)) {
                newX = (tileCol + 1) * tileSize;
            }
        }

        entity.setX(newX);

        // Y-Kollision unten prüfen
        if (dy > 0) {
            double bottom = newY + playerHeight - 1;
            int tileRow = (int) (bottom / tileSize);
            int tileColLeft = (int) (newX / tileSize);
            int tileColRight = (int) ((newX + playerWidth - 1) / tileSize);
            if (isWall(tileRow, tileColLeft) || isWall(tileRow, tileColRight)) {
                newY = tileRow * tileSize - playerHeight;
            }
            // Y-Kollision oben prüfen
        } else if (dy < 0) {
            double top = newY;
            int tileRow = (int) (top / tileSize);
            int tileColLeft = (int) (newX / tileSize);
            int tileColRight = (int) ((newX + playerWidth - 1) / tileSize);
            if (isWall(tileRow, tileColLeft) || isWall(tileRow, tileColRight)) {
                newY = (tileRow + 1) * tileSize;
            }
        }

        entity.setY(newY);
        entity.getHurtbox().setLocation(newX, newY);
    }

    /**
     * Prüft ob eine Entity bei einem bestimmten Versatz mit einer Wand kollidieren würde,
     * ohne die Position tatsächlich zu verändern.
     *
     * @param entity Die zu prüfende Entity
     * @param dx     Versatz in X-Richtung
     * @param dy     Versatz in Y-Richtung
     * @return true wenn eine Kollision auftreten würde, sonst false
     */
    public boolean collidesWithWall(Entity entity, double dx, double dy) {

        int tileSize = tileManager.getTileSize();
        int playerWidth = entity.getWidth();
        int playerHeight = entity.getHeight();
        double newX = entity.getX() + dx;
        double newY = entity.getY() + dy;

        if (dx > 0) {
            double right = newX + playerWidth - 1;
            int tileCol = (int) (right / tileSize);
            int tileRowTop = (int) (entity.getY() / tileSize);
            int tileRowBottom = (int) ((entity.getY() + playerHeight - 1) / tileSize);
            if (isWall(tileRowTop, tileCol) || isWall(tileRowBottom, tileCol)) return true;
        } else if (dx < 0) {
            double left = newX;
            int tileCol = (int) (left / tileSize);
            int tileRowTop = (int) (entity.getY() / tileSize);
            int tileRowBottom = (int) ((entity.getY() + playerHeight - 1) / tileSize);
            if (isWall(tileRowTop, tileCol) || isWall(tileRowBottom, tileCol)) return true;
        }

        if (dy > 0) {
            double bottom = newY + playerHeight - 1;
            int tileRow = (int) (bottom / tileSize);
            int tileColLeft = (int) (newX / tileSize);
            int tileColRight = (int) ((newX + playerWidth - 1) / tileSize);
            if (isWall(tileRow, tileColLeft) || isWall(tileRow, tileColRight)) return true;
        } else if (dy < 0) {
            double top = newY;
            int tileRow = (int) (top / tileSize);
            int tileColLeft = (int) (newX / tileSize);
            int tileColRight = (int) ((newX + playerWidth - 1) / tileSize);
            if (isWall(tileRow, tileColLeft) || isWall(tileRow, tileColRight)) return true;
        }

        return false;
    }

    /**
     * Bewegt eine Entity anhand eines Vektors.
     *
     * @param entity Entity die bewegt werden soll
     * @param vector Bewegungsvektor mit X- und Y-Versatz
     */
    public void applyVector(Entity entity, Vector vector) {
        move(entity, vector.getOffsetX(), vector.getOffsetY());
    }

    /**
     * Prüft ob ein bestimmtes Tile eine Wand ist.
     * Tiles außerhalb der Map gelten ebenfalls als Wand.
     * Nur Tile-Typ 8 gilt als begehbares Tile.
     *
     * @param row Zeile des Tiles
     * @param col Spalte des Tiles
     * @return true wenn das Tile eine Wand ist, sonst false
     */
    private boolean isWall(int row, int col) {
        int[][] map = tileManager.getTileMap();
        if (row < 0 || row >= map.length || col < 0 || col >= map[0].length) {
            return true;
        }
        return map[row][col] != 8;
    }
}