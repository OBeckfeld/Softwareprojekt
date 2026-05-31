package entities.managers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import entities.Attack;
import entities.Entity;
import entities.PlayerTypeEntity;
import tools.TileManager;

import javax.accessibility.AccessibleTable;

public class AttackManager implements AttackRegistry {

    private final CollisionManager collisionManager;
    private final EntityRegistry registry;
    private final TileManager tileManager;
    private final ArrayList<Attack> attacks;
    private PlayerTypeEntity owner;

    public AttackManager(CollisionManager collisionManager, EntityRegistry registry, TileManager tileManager) {
        this.collisionManager = collisionManager;
        this.registry = registry;
        this.tileManager = tileManager;
        attacks = new ArrayList<>();
    }



    public void attack(PlayerTypeEntity owner, double x, double y, int height, int width, int duration, int damage) {
        Attack attack = new Attack(x, y, width, height, registry, duration, owner, this, damage, tileManager);
        owner.setAttack(attack);
        attacks.add(attack);
    }

    public void grabOwner(PlayerTypeEntity source){
        owner = source;
    }

    /**
     * distributeDamage verteilt den gespeicherten Schaden und setzt ihn anschließend zurück
     */
    public void distributeDamage() {
        for (Attack attack : attacks) {
            for (Entity entity : collisionManager.getEntities(attack)) {
                if (!attack.getHitList().contains(entity) && entity instanceof PlayerTypeEntity && entity.getClass() != attack.getOwner().getClass()) {
                    attack.getHitList().add(entity);

                    Random random = new Random();
                    int rand = random.nextInt(100);
                    PlayerTypeEntity attacker = attack.getOwner(); // ← Owner von der Attack holen statt von owner Feld

                    if (rand < attacker.getCritChance()) {
                        if (!entity.isDead()) {
                            ((PlayerTypeEntity) entity).takeDamage(attack.getDamage() * (attacker.getCrit() / 100), attacker);
                            if (entity.isDead() && attacker != null) {
                                attacker.setSkillPoints(attacker.getSkillPoints() + entity.getPointsOnDeath());
                            }
                        }
                    } else {
                        if (!entity.isDead()) {
                            ((PlayerTypeEntity) entity).takeDamage(attack.getDamage(), attacker);
                            if (entity.isDead() && attacker != null) {
                                attacker.setSkillPoints(attacker.getSkillPoints() + entity.getPointsOnDeath());
                            }
                        }
                    }
                }
            }
        }
    }
}
