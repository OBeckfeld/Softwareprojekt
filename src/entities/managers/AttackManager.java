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

    /**
     * Erstellt einen neuen AttackManager und speichert die Referenzen
     * auf CollisionManager, EntityRegistry und TileManager.
     * Zusätzlich wird die Liste für aktive Attacken initialisiert.
     */
    public AttackManager(CollisionManager collisionManager, EntityRegistry registry, TileManager tileManager) {
        this.collisionManager = collisionManager;
        this.registry = registry;
        this.tileManager = tileManager;
        attacks = new ArrayList<>();
    }

    public void grabOwner(PlayerTypeEntity source) {
        // kept for interface compatibility; attack owner is stored on each Attack.
    }


    /**
     * Erstellt eine neue Attacke mit den angegebenen Werten
     * und fügt sie der Liste der aktiven Attacken hinzu.
     */
    public void attack(PlayerTypeEntity owner, double x, double y, int height, int width, int duration, int damage) {
        Attack attack = new Attack(x, y, width, height, registry, duration, owner, this, damage, tileManager);
        attacks.add(attack);
    }

    /**
     * Erstellt eine neue Attacke mit den angegebenen Werten,
     * setzt zusätzlich die Rüstungsdurchdringung und fügt sie
     * der Liste der aktiven Attacken hinzu.
     */
    public void attack(PlayerTypeEntity owner, double x, double y, int height, int width, int duration, int damage, boolean armorPierce) {
        Attack attack = new Attack(x, y, width, height, registry, duration, owner, this, damage, tileManager);
        attack.setArmorPierce(armorPierce);
        attacks.add(attack);
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
                    PlayerTypeEntity attackOwner = attack.getOwner();
                    if(rand < ((PlayerTypeEntity) entity).getCritChance()){
                        if(!entity.isDead()) {
                            ((PlayerTypeEntity) entity).takeDamage(attack.getDamage() * (attackOwner.getCrit() / 100), attackOwner, attack.getArmorPierce());
                            if (entity.isDead()) {
                                attackOwner.setSkillPoints(attackOwner.getSkillPoints() + entity.getPointsOnDeath());
                            }
                        }
                    }
                    else{
                        if(!entity.isDead()) {
                            ((PlayerTypeEntity) entity).takeDamage(attack.getDamage(), attackOwner, attack.getArmorPierce());
                            if (entity.isDead()) {
                                attackOwner.setSkillPoints(attackOwner.getSkillPoints() + entity.getPointsOnDeath());
                            }
                        }
                    }
                }
            }
        }
    }
}
