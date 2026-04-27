package entities;

import entities.components.EnemyMovementComponent;

import java.util.Set;

/**
 *
 * Enemy verfolgt den Spieler und greift an, wenn er nah genug ist.
 */
public class Enemy extends Entity {

    private int health;
    private int damage;
    private int attackRange;       // Pixel-Abstand, ab dem der Enemy angreift
    private int attackCooldown;    // Frames zwischen Angriffen
    private int cooldownTimer;     // Interner Zähler für den Cooldown

    private EnemyMovementComponent enemyMovement;

    /**
     * @param x           Start-X-Position
     * @param y           Start-Y-Position
     * @param height      Höhe des Enemies
     * @param width       Breite des Enemies
     * @param health      Lebenspunkte
     * @param damage      Schaden pro Angriff
     * @param attackRange Reichweite des Angriffs in Pixel
     */
    public Enemy(int x, int y, int height, int width, int health, int damage, int attackRange) {
        super(x, y, height, width);
        this.health = health;
        this.damage = damage;
        this.attackRange = attackRange;
        this.attackCooldown = 60; // 1 Sekunde bei 60 FPS
        this.cooldownTimer = 0;
        this.setSpeed(2); // Etwas langsamer als der Spieler
        this.enemyMovement = new EnemyMovementComponent();
    }

    /**
     * Update-Methode des Enemies.
     * Keyboard-Inputs werden ignoriert – der Enemy steuert sich selbst.
     * Stattdessen wird der Spieler als Ziel übergeben.
     *
     *
     */


    /**
     * Hauptmethode: Enemy verfolgt den Spieler und greift an.
     *
     * @param player Der Spieler
     */
    public void update(Entity player) {
        double distance = getDistanceTo(player);

        // Cooldown herunterzählen
        if (cooldownTimer > 0) {
            cooldownTimer--;
        }

        if (distance <= attackRange) {
            // Spieler in Reichweite → angreifen
            attack(player);
        } else {
            // Spieler nicht in Reichweite → verfolgen
            enemyMovement.move(this, player);
        }
    }

    /**
     * Berechnet die Distanz zum Spieler (Mittelpunkt zu Mittelpunkt).
     */
    private double getDistanceTo(Entity target) {
        double dx = target.getX() - this.getX();
        double dy = target.getY() - this.getY();
        return Math.sqrt(dx * dx + dy * dy);
    }

    /**
     * Greift den Spieler an, falls der Cooldown abgelaufen ist.
     * (Player braucht eine takeDamage()-Methode)
     */
    private void attack(Entity player) {
        if (cooldownTimer == 0) {
            if (player instanceof Player p) {
                p.takeDamage(damage);
                System.out.println("Enemy greift an! Schaden: " + damage);
            }
            cooldownTimer = attackCooldown;
        }
    }

    /**
     * Enemy nimmt Schaden.
     *
     * @param amount Schadensmenge
     */
    public void takeDamage(int amount) {
        health -= amount;
        System.out.println("Enemy hat " + health + " HP übrig.");
        if (health <= 0) {
            onDeath();
        }
    }

    /**
     * Wird aufgerufen, wenn der Enemy stirbt.
     */
    private void onDeath() {
        System.out.println("Enemy wurde besiegt!");
        // Hier können später Drops, Animationen etc. ausgelöst werden
    }

    public boolean isAlive() {
        return health > 0;
    }
}