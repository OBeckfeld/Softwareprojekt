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
    public void newAttack(int direction, entityRegistry registry, int timeToLive, PlayerTypeEntity owner) {
        if(owner.getAttack() == null || owner.getAttack().isExpired()) { //falls der owner noch keine Attacke hat oder seine Attacke abgelaufen ist, wird eine neue Attacke erstellt
            int x;
            int y;
            switch (direction) { //die Position der Attacke wird abhängig von der Richtung des owners gesetzt
                case 0: //rechts
                    x = owner.getX() + owner.getWidth();
                    y = owner.getY() + owner.getHeight() / 2 - 60;
                    break;
                case 1: //unten
                    x = owner.getX() + owner.getWidth() / 2 - 30;
                    y = owner.getY() + owner.getHeight();
                    break;
                case 2: //links
                    x = owner.getX() - 60;
                    y = owner.getY() + owner.getHeight() / 2 - 60;
                    break;
                case 3: //oben
                    x = owner.getX() + owner.getWidth() / 2 - 30;
                    y = owner.getY() - 60;
                    break;
                default:
                    throw new IllegalArgumentException();
            }
            Attack attack = new Attack(x, y, 120, 60, registry, timeToLive, owner);
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