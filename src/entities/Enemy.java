package entities;

import entities.managers.EntityRegistry;
import tools.Vector;

public class Enemy extends PlayerTypeEntity {

    private static final int ATTACK_RANGE = 45;

    Player player;

    public Enemy(double x, double y, int height, int width, EntityRegistry registry, Player player) {
        super(x, y, height, width, registry);
        setSpeed(2);
        this.player = player;
    }

    @Override
    public void update() {
        if (!isAlive() || player == null) return;

        moveTowardsPlayer();
    }

    private void moveTowardsPlayer() {
        Vector distVector = new Vector(getX(), getY(), player.getX(), player.getY());

        if (distVector.getLength() > ATTACK_RANGE) {
            Vector moveVector = new Vector(getX(), getY(), player.getX(), player.getY());
            moveVector.setLength(getSpeed());
            moveVector.apply(this);
        }
    }
}
