package entities.enemies;
import entities.PlayerTypeEntity;
import entities.managers.EntityRegistry;
import entities.managers.AttackManager;
import main.GamePanel;
import tools.TileManager;
import tools.Vector;

public class Boss extends Enemy {

    private static final int FLEE_RANGE = 150;
    private static final int ATTACK_RANGE = 300;

    public Boss(int x, int y, int width, int height, EntityRegistry registry, AttackManager attackManager, TileManager tileManager, GamePanel gamePanel) {
        super(x, y, width, height, 60, registry, attackManager, tileManager, gamePanel);
        currentHealth = 1000;
        setSpeed(2);
        viewRange = 800;
        pointsOnDeath = 10;
        skillPoints = 4;
        skillTree.unlock(skillTree.getAbilityReference("dash"));
    }

    @Override
    protected void handleMovement() {
        if (player == null) return; //macht nichts wenn er den Player noch nicht gesehen hat
        Vector toPlayer = new Vector(getX(), getY(), player.getX(), player.getY());
        double dist = toPlayer.getLength();

        if (dist <= ATTACK_RANGE) {
            tryAttackEntity((PlayerTypeEntity) player);
        }

        if (player == null) return;
        Vector vector = new Vector(getX(), getY(), player.getX(), player.getY());
        if (vector.getLength() > speed) {
            vector.setLength(speed);
        }
        if (registry.getInRange(this, 100, 100).contains(player) || attackDelay != 40) {
            vector.setLength(0);
            if (attackDelay == 0) {
                tryAttackEntity((PlayerTypeEntity) player);
                attackDelay = 40;
            } else {
                attackDelay--;
            }
        } else {
            attackDelay = 40;
        }
        move(vector);
    }



}
