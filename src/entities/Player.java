package entities;

import java.util.Set;

/**
 * Der Spieler-Charakter.
 * Erbt Bewegung von Entity und besitzt Lebenspunkte.
 */
public class Player extends Entity {

    private int health;
    private int maxHealth;

    public Player(int x, int y, int height, int width, int health) {
        super(x, y, height, width);
        this.health = health;
        this.maxHealth = health;
    }

    @Override
    public void update(Set<Integer> keyboardInputs) {
        super.update(keyboardInputs); // Bewegung via MovementComponent
    }

    /**
     * Spieler nimmt Schaden – wird vom Enemy aufgerufen.
     *
     * @param amount Schadensmenge
     */
    public void takeDamage(int amount) {
        health -= amount;
        health = Math.max(health, 0); // Nicht unter 0 fallen
        System.out.println("Spieler hat " + health + "/" + maxHealth + " HP.");
        if (health == 0) {
            System.out.println("Spieler ist gestorben!");
        }
    }

    public boolean isAlive()   { return health > 0; }
    public int getHealth()     { return health; }
    public int getMaxHealth()  { return maxHealth; }
}