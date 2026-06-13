package entities.managers;

import java.util.ArrayList;
import java.util.Random;

import entities.Attack;
import entities.Entity;
import entities.PlayerTypeEntity;
import tools.TileManager;


/**
 * Verwaltet alle aktiven Angriffe im Spiel und verteilt den Schaden
 * an getroffene Entities. Implementiert das AttackRegistry Interface.
 */
public class AttackManager implements AttackRegistry {

    /** CollisionManager für die Kollisionsprüfung zwischen Angriffen und Entities. */
    private final CollisionManager collisionManager;

    /** EntityRegistry für den Zugriff auf alle aktiven Entities. */
    private final EntityRegistry registry;

    /** TileManager für tile-basierte Berechnungen. */
    private final TileManager tileManager;

    /** Liste aller aktuell aktiven Angriffe. */
    private final ArrayList<Attack> attacks;

    /**
     * Erstellt einen neuen AttackManager.
     *
     * @param collisionManager CollisionManager für Kollisionsprüfungen
     * @param registry         EntityRegistry für den Zugriff auf Entities
     * @param tileManager      TileManager für tile-basierte Berechnungen
     */
    public AttackManager(CollisionManager collisionManager, EntityRegistry registry, TileManager tileManager) {
        this.collisionManager = collisionManager;
        this.registry = registry;
        this.tileManager = tileManager;
        attacks = new ArrayList<>();
    }

    /**
     * Wird für Interface-Kompatibilität beibehalten.
     * Der Angreifer wird direkt im Attack-Objekt gespeichert.
     *
     * @param source Angreifende Entity
     */
    public void grabOwner(PlayerTypeEntity source) {
        // kept for interface compatibility; attack owner is stored on each Attack.
    }

    /**
     * Erstellt einen neuen Angriff und fügt ihn der Angriffsliste hinzu.
     *
     * @param owner    Angreifende Entity
     * @param x        X-Koordinate des Angriffs
     * @param y        Y-Koordinate des Angriffs
     * @param height   Höhe der Angriffs-Hitbox
     * @param width    Breite der Angriffs-Hitbox
     * @param duration Dauer des Angriffs in Ticks
     * @param damage   Schadenswert des Angriffs
     */
    public void attack(PlayerTypeEntity owner, double x, double y, int height, int width, int duration, int damage) {
        Attack attack = new Attack(x, y, width, height, registry, duration, owner, this, damage, tileManager);
        attacks.add(attack);
    }

    /**
     * Erstellt einen neuen Angriff mit optionaler Rüstungsdurchdringung
     * und fügt ihn der Angriffsliste hinzu.
     *
     * @param owner       Angreifende Entity
     * @param x           X-Koordinate des Angriffs
     * @param y           Y-Koordinate des Angriffs
     * @param height      Höhe der Angriffs-Hitbox
     * @param width       Breite der Angriffs-Hitbox
     * @param duration    Dauer des Angriffs in Ticks
     * @param damage      Schadenswert des Angriffs
     * @param armorPierce true wenn der Angriff Rüstung ignoriert
     */
    public void attack(PlayerTypeEntity owner, double x, double y, int height, int width, int duration, int damage, boolean armorPierce) {
        Attack attack = new Attack(x, y, width, height, registry, duration, owner, this, damage, tileManager);
        attack.setArmorPierce(armorPierce);
        attacks.add(attack);
    }

    /**
     * Verteilt den Schaden aller aktiven Angriffe an getroffene Entities.
     * Prüft für jeden Angriff welche Entities kollidieren und fügt ihnen Schaden zu.
     * Berücksichtigt kritische Treffer, Rüstungsdurchdringung und Skillpunkte bei Tod.
     * Jede Entity kann pro Angriff nur einmal getroffen werden (HitList).
     */
    public void distributeDamage() {
        for (Attack attack : attacks) {
            for (Entity entity : collisionManager.getEntities(attack)) {

                // Nur unbekannte Entities treffen die Schaden nehmen können
                // und nicht zur gleichen Klasse wie der Angreifer gehören
                if (!attack.getHitList().contains(entity)
                        && entity instanceof PlayerTypeEntity
                        && entity.getClass() != attack.getOwner().getClass()) {

                    attack.getHitList().add(entity);

                    Random random = new Random();
                    int rand = random.nextInt(100);
                    PlayerTypeEntity attackOwner = attack.getOwner();

                    // Kritischen Treffer prüfen
                    if (rand < attackOwner.getCritChance()) {
                        if (!entity.isDead()) {
                            ((PlayerTypeEntity) entity).takeDamage(
                                    attack.getDamage() * (attackOwner.getCrit() / 100),
                                    attackOwner,
                                    attack.getArmorPierce()
                            );
                            // Skillpunkte bei Tod vergeben
                            if (entity.isDead()) {
                                attackOwner.setSkillPoints(
                                        attackOwner.getSkillPoints() + entity.getPointsOnDeath()
                                );
                            }
                        }
                    } else {
                        if (!entity.isDead()) {
                            ((PlayerTypeEntity) entity).takeDamage(
                                    attack.getDamage(),
                                    attackOwner,
                                    attack.getArmorPierce()
                            );
                            // Skillpunkte bei Tod vergeben
                            if (entity.isDead()) {
                                attackOwner.setSkillPoints(
                                        attackOwner.getSkillPoints() + entity.getPointsOnDeath()
                                );
                            }
                        }
                    }
                }
            }
        }
    }
}