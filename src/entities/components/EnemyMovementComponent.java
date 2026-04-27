package entities.components;

import entities.Entity;

import java.util.Set;

/**
 * Bewegungskomponente für Enemies.
 * Der Enemy verfolgt automatisch den Spieler (Chase-KI).
 */
public class EnemyMovementComponent {

    public EnemyMovementComponent() {}

    /**
     * Bewegt den Enemy in Richtung des Spielers.
     *
     * @param enemy  Der Enemy, der bewegt wird
     * @param player Der Spieler, dem der Enemy folgt
     */
    public void move(Entity enemy, Entity player) {
        double dx = player.getX() - enemy.getX();
        double dy = player.getY() - enemy.getY();


        double distance = Math.sqrt(dx * dx + dy * dy);

        // Nur bewegen, wenn der Spieler nicht direkt auf dem Enemy steht
        if (distance == 0) return;

        // Richtungsvektor normalisieren und mit Speed multiplizieren
        double normalizedDx = (dx / distance) * enemy.getSpeed();
        double normalizedDy = (dy / distance) * enemy.getSpeed();

        // Diagonale Bewegung normalisieren (wie in MovementComponent)
        int vx, vy;
        if (normalizedDx != 0 && normalizedDy != 0) {
            vx = (int) Math.round(normalizedDx / 1.4142135623730951);
            vy = (int) Math.round(normalizedDy / 1.4142135623730951);
        } else {
            vx = (int) Math.round(normalizedDx);
            vy = (int) Math.round(normalizedDy);
        }

        enemy.setX(enemy.getX() + vx);
        enemy.setY(enemy.getY() + vy);
    }
}