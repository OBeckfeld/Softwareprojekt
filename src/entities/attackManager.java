package entities;

import java.util.ArrayList;
import java.util.HashMap;

public class AttackManager {
    private CollisionManager collisionManager;
    private HashMap<PlayerTypeEntity, Integer> damage; //damage speichert alle Entities, die Schaden nehmen, und den entsprechenden Schaden

    public AttackManager(CollisionManager collisionManager) {
        this.collisionManager = collisionManager;
        this.damage = new HashMap<>();
    }

    /**
     * newAttack erstellt eine neue Attacke, falls der owner keine aktive Attacke hat, dies limitiert die Anzahl der Attacken pro Entity
     */
    public void newAttack(double x, double y, int height, int width, entityRegistry registry, int damage, int timeToLive, int attackType, PlayerTypeEntity owner) {
        if(owner.getAttack() == null || owner.getAttack().isExpired()) { //falls der owner noch keine Attacke hat oder seine Attacke abgelaufen ist, wird eine neue Attacke erstellt
            Attack attack = new Attack(x, y, height, width, registry, damage, timeToLive, attackType, owner);
            owner.setAttack(attack); //die Attacke wird als aktive Attacke des owners gespeichert, die alte Attacke wird überschrieben
        }
    }

    /**
     * attack überprüft falls eine Attacke gültig ist alle getroffenen entities, danach wird der zugefügte Schaden berechnet und gespeichert
     */
    public void attack(Attack attack) {
        if (!attack.isExpired()) { //die Gültigkeit der Attacke wird überprüft, falls sie ungültig ist, geschieht nichts
             for (Entity entity : collisionManager.getEntities(attack)) { //durchläuft alle Entities, die von der Attacke getroffen werden
            if (!attack.getHitList().contains(entity) && entity instanceof PlayerTypeEntity) { //überprüft, ob die Entity bereits bekannt ist und ob sie überhaupt Schaden nehmen kann
                    attack.getHitList().add((PlayerTypeEntity) entity); //fügt die Entity der hitlist in der Attacke hinzu
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