package entities.enemies;
import entities.PlayerTypeEntity;
import entities.managers.EntityRegistry;
import entities.managers.AttackManager;
import main.GamePanel;
import tools.TileManager;
import tools.Vector;

public class RangedEnemy extends Enemy {

    private static final int FLEE_RANGE = 150;
    private static final int ATTACK_RANGE = 300;

    public RangedEnemy(int x, int y, int width, int height,
                       EntityRegistry registry, AttackManager attackManager, TileManager tileManager, GamePanel gamePanel) {
        super(x, y, width, height, 60, registry, attackManager, tileManager, gamePanel);
        currentHealth = 30;
        setSpeed(3);
        viewRange = 400;
        pointsOnDeath = 2;
    }

    @Override
    protected void handleMovement() {
        if (player == null) return;
        Vector toPlayer = new Vector(getX(), getY(), player.getX(), player.getY());
        double dist = toPlayer.getLength();

        if (dist < FLEE_RANGE) { // umgekehrt also läuft vom Spieler weg, um Distanz zu erschaffen
            Vector fleeVector = new Vector(player.getX(), player.getY(), getX(), getY());
            fleeVector.setLength(getSpeed());
            move(fleeVector);
        } else if (dist <= ATTACK_RANGE) {
            tryAttackEntity((PlayerTypeEntity) player);
        }
    }

    {
    }
}
