package entities.managers;

import java.util.ArrayList;
import entities.Attack;
import entities.Entity;
import entities.PlayerTypeEntity;
import tools.TileManager;

public class AttackManager implements AttackRegistry {

    private final CollisionManager collisionManager;
    private final EntityRegistry registry;
    private final TileManager tileManager;
    private final ArrayList<Attack> attacks;

    public AttackManager(CollisionManager collisionManager, EntityRegistry registry, TileManager tileManager) {
        this.collisionManager = collisionManager;
        this.registry = registry;
        this.tileManager = tileManager;
        attacks = new ArrayList<>();
    }

    public Attack newAttack(PlayerTypeEntity owner) {
        if (owner.getAttack() == null || owner.getAttack().isExpired()) {
            double x;
            double y;
            int height;
            int width;
            switch (owner.getDirection()) {
                case 0:
                    x = Math.round(owner.getX() + owner.getWidth());
                    y = Math.round(owner.getY() + owner.getHeight() / 2 - owner.getVerticalRange() / 2);
                    height = owner.getVerticalRange();
                    width = owner.getHorizontalRange();
                    break;
                case 1:
                    x = Math.round(owner.getX() + owner.getWidth() / 2 - owner.getVerticalRange() / 2);
                    y = Math.round(owner.getY() + owner.getHeight());
                    height = owner.getHorizontalRange();
                    width = owner.getVerticalRange();
                    break;
                case 2:
                    x = Math.round(owner.getX() - owner.getHorizontalRange());
                    y = Math.round(owner.getY() + owner.getHeight() / 2 - owner.getVerticalRange() / 2);
                    height = owner.getVerticalRange();
                    width = owner.getHorizontalRange();
                    break;
                case 3:
                    x = Math.round(owner.getX() + owner.getWidth() / 2 - owner.getVerticalRange() / 2);
                    y = Math.round(owner.getY() - owner.getHorizontalRange());
                    height = owner.getHorizontalRange();
                    width = owner.getVerticalRange();
                    break;
                default:
                    throw new IllegalArgumentException();
            }
            Attack attack = new Attack(x, y, width, height, registry, owner.getAttackDuration(), owner, this, tileManager);
            owner.setAttack(attack);
            registry.register(attack);
        }
        return null;
    }

    @Override
    public void attack(PlayerTypeEntity owner, double x, double y, int height, int width, int duration, int damage) {
        Attack attack = new Attack(x, y, width, height, registry, duration, owner, this, damage, tileManager);
        owner.setAttack(attack);
        registry.register(attack);

        // Sofort Schaden verteilen basierend auf Position
        for (Entity entity : registry.getInRange(attack, Math.max(width, height), Math.max(height, width))) {
            if (entity instanceof PlayerTypeEntity && entity.getClass() != owner.getClass()) {
                ((PlayerTypeEntity) entity).takeDamage(damage * owner.getDamageModifier());

            }
        }
    }

    @Override
    public void distributeDamage() {
        System.out.println("distributeDamage läuft, attacks: " + attacks.size());
        for (Attack attack : attacks) {
            for (Entity entity : collisionManager.getEntities(attack)) {
                if (!attack.getHitList().contains(entity)
                        && entity instanceof PlayerTypeEntity
                        && entity.getClass() != attack.getOwner().getClass()) {
                    attack.getHitList().add(entity);
                    ((PlayerTypeEntity) entity).takeDamage(attack.getDamage());
                }
            }
        }
        attacks.clear();
    }
}