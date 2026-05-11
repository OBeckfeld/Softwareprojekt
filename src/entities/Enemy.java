package entities;

import entities.managers.EntityManager;
import entities.managers.EntityRegistry;
import tools.Vector;
import entities.managers.AttackManager;

public class Enemy extends PlayerTypeEntity {

    private static final int STOP_RADIUS = 40;
    private static final int ATTACK_RANGE = 45;
    private Player targetPlayer = null; // wird gesetzt wenn Spieler im Sichtfeld

    public Enemy(int x, int y, int height, int width, int hitCooldown, EntityRegistry registry, AttackManager attackManager) {
        super(x, y, height, width, hitCooldown, registry, attackManager);
        setSpeed(2);
    }

    public void update() {
        if (!isAlive()) return;

        checkIfPlayerInView(); // Sichtfeld prüfen

        if (targetPlayer != null) { // nur aktiv wenn Spieler gesehen wurde
            moveTowardsPlayer();
            tryAttackPlayer();
        }
    }

    // Prüft ob Player im Sichtfeld ist
    private void checkIfPlayerInView() {
        for (Entity entity : registry.getInView(this)) {
            if (entity instanceof Player) {
                targetPlayer = (Player) entity; //Spieler gefunden
                return;
            }
        }
        targetPlayer = null; // Spieler nicht im Sichtfeld -> stopp
    }


    private void moveTowardsPlayer() {
        Vector distVector = new Vector(x, y, targetPlayer.getX(), targetPlayer.getY());

        if (distVector.getLength() > STOP_RADIUS) {
            Vector moveVector = new Vector(getX(), getY(), targetPlayer.getX(), targetPlayer.getY());
            moveVector.setLength(getSpeed());
            moveVector.apply(this);
        }
    }


    private void tryAttackPlayer() {
        Vector distVector = new Vector(x, y, targetPlayer.getX(), targetPlayer.getY());
        boolean inRange = distVector.getLength() <= ATTACK_RANGE;

        attackManager.newAttack(registry, this); // <- kommt jetzt von PlayerTypeEntity
    }
}
