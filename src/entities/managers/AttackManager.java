package entities.managers;

import java.util.HashMap;
import java.awt.event.KeyEvent;

import entities.Attack;
import entities.Entity;
import entities.Player;
import inputs.KeyboardInputs;
import entities.PlayerTypeEntity;

public class AttackManager {
    private CollisionManager collisionManager;
    private HashMap<PlayerTypeEntity, Integer> damage; //damage speichert alle Entities, die Schaden nehmen, und den entsprechenden Schaden
    private KeyboardInputs inputs;
    private EntityRegistry registry;

    public AttackManager(CollisionManager collisionManager, EntityRegistry registry) {
        this.collisionManager = collisionManager;
        this.inputs = inputs;
        this.registry = registry;
        this.damage = new HashMap<>();
    }

    /**
     * newAttack erstellt eine neue Attacke, falls der owner keine aktive Attacke hat, dies limitiert die Anzahl der Attacken pro Entity
     */
    public void newAttack(EntityRegistry registry, PlayerTypeEntity owner) {
        if(owner.getAttack() == null || owner.getAttack().isExpired()) { //falls der owner noch keine Attacke hat oder seine Attacke abgelaufen ist, wird eine neue Attacke erstellt
            double x;
            double y;
            int height;
            int width;
            switch (owner.getDirection()) { //die Position der Attacke wird abhängig von der Richtung des owners gesetzt
                case 0: //rechts
                    x = Math.round(owner.getX() + owner.getWidth());
                    y = Math.round(owner.getY() + owner.getHeight() / 2 - owner.getVerticalRange() / 2);
                    height = owner.getVerticalRange(); //das Rectangle wird um 90 Grad gedreht, somit werden height und width vertauscht
                    width = owner.getHorizontalRange();
                    break;
                case 1: //unten
                    x = Math.round(owner.getX() + owner.getWidth() / 2 - owner.getVerticalRange() / 2);
                    y = Math.round(owner.getY() + owner.getHeight());
                    height = owner.getHorizontalRange(); //das Rectangle wird um 90 Grad gedreht, somit werden height und width vertauscht
                    width = owner.getVerticalRange();
                    break;
                case 2: //links
                    x = Math.round(owner.getX() - owner.getHorizontalRange());
                    y = Math.round(owner.getY() + owner.getHeight() / 2 - owner.getVerticalRange() / 2);
                    height = owner.getVerticalRange(); //das Rectangle wird um 90 Grad gedreht, somit werden height und width vertauscht
                    width = owner.getHorizontalRange();
                    break;
                case 3: //oben
                    x = Math.round(owner.getX() + owner.getWidth() / 2 - owner.getVerticalRange() / 2);
                    y = Math.round(owner.getY() - owner.getHorizontalRange());
                    height = owner.getHorizontalRange(); //das Rectangle wird um 90 Grad gedreht, somit werden height und width vertauscht
                    width = owner.getVerticalRange();
                    break;
                default:
                    throw new IllegalArgumentException();
            }
            Attack attack = new Attack(x, y, width, height, registry, owner.getHitCooldown(), owner);
            owner.setAttack(attack); //die Attacke wird als aktive Attacke des owners gespeichert, die alte Attacke wird überschrieben
        }
    }

    /**
     * attack überprüft falls eine Attacke gültig ist alle getroffenen entities, danach wird der zugefügte Schaden berechnet und gespeichert
     */
    public void attack(Attack attack) {
        if (!attack.isExpired()) { //die Gültigkeit der Attacke wird überprüft, falls sie ungültig ist, geschieht nichts
            for (Entity entity : collisionManager.getEntities(attack)) { //durchläuft alle Entities, die von der Attacke getroffen werden
                if (!attack.getHitList().contains(entity) && entity instanceof PlayerTypeEntity && entity != attack.getOwner()) { //überprüft, ob die Entity bereits bekannt ist und ob sie überhaupt Schaden nehmen kann
                    attack.getHitList().add(entity); //fügt die Entity der hitlist in der Attacke hinzu
                    int newDamage = attack.getDamage() - ((PlayerTypeEntity) entity).getDefense(); //berechnet den zugefügten Schaden
                    if (newDamage > 0) { //falls der Schaden positiv ist, wird er gespeichert
                        damage.put((PlayerTypeEntity) entity, newDamage);
                    }
                }
            }
        }
    }

    /**
     * damageDistribution verteilt den gespeicherten Shcaden und setzt ihn anschließend zurück
     */
    public void damageDistribution() {
        for (PlayerTypeEntity entity : damage.keySet()) { //durchläuft alle Entities, die Schaden nehmen
            entity.setHealth(entity.getHealth() - damage.get(entity)); //zieht den Schaden von der Gesundheit der Entity ab
        }
        damage.clear(); //setzt den Schaden zurück, um auf die nächste Runde vorzubereiten
    }
}