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
            for (Entity entity : collisionManager.getEntities(attack)) {//durchläuft alle Entities, die von der Attacke getroffen werden
                if (!attack.getHitList().contains(entity) && entity instanceof PlayerTypeEntity && entity.getClass() != attack.getOwner().getClass()) { //überprüft, ob die Entity bereits bekannt ist und ob sie überhaupt Schaden nehmen kann

                    attack.getHitList().add(entity); //fügt die Entity der hitlist in der Attacke hinzu

                    Random random = new Random();
                    int rand = random.nextInt(100);
                    if(rand < ((PlayerTypeEntity) entity).getCritChance()){
                        if(!entity.isDead()) {
                            ((PlayerTypeEntity) entity).takeDamage(attack.getDamage() * (((PlayerTypeEntity) entity).getCrit() / 100));//fügt kritischen Schaden zu
                            if (entity.isDead()) {
                                owner.setSkillPoints(owner.getSkillPoints() + entity.getPointsOnDeath());
                            }
                        }
                    }
                    else{
                        if(!entity.isDead()) {
                            ((PlayerTypeEntity) entity).takeDamage(attack.getDamage());//macht den Schaden. Rüstung etc. wird bei der Entity selbst abgezogen. Das gilt auch für "negativen Schaden"
                            if (entity.isDead()) {
                                owner.setSkillPoints(owner.getSkillPoints() + entity.getPointsOnDeath());
                                System.out.println(owner.getSkillPoints());
                            }
                        }
                    }
                }
            }
        }
    }
}
