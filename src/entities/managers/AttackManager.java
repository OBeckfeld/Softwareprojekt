package entities.managers;

import java.util.ArrayList;
import java.util.HashMap;

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

    public AttackManager(CollisionManager collisionManager, EntityRegistry registry, TileManager tileManager) {
        this.collisionManager = collisionManager;
        this.registry = registry;
        this.tileManager = tileManager;
        attacks = new ArrayList<>();
    }

    /**
     * newAttack erstellt eine neue Attacke, falls der owner keine aktive Attacke hat, dies limitiert die Anzahl der Attacken pro Entity
     *
     * @return public Attack newAttack(PlayerTypeEntity owner) {
     * if(owner.getAttack() == null || owner.getAttack().isExpired()) { //falls der owner noch keine Attacke hat oder seine Attacke abgelaufen ist, wird eine neue Attacke erstellt
     * double x;
     * double y;
     * int height;
     * int width;
     * //owner.setAttacking(10);//sagt dem owner, wie lange er angreift
     * switch (owner.getDirection()) { //die Position der Attacke wird abhängig von der Richtung des owners gesetzt
     * case 0: //rechts
     * x = Math.round(owner.getX() + owner.getWidth());
     * y = Math.round(owner.getY() + owner.getHeight() / 2 - owner.getVerticalRange() / 2);
     * height = owner.getVerticalRange(); //das Rectangle wird um 90 Grad gedreht, somit werden height und width vertauscht
     * width = owner.getHorizontalRange();
     * break;
     * case 1: //unten
     * x = Math.round(owner.getX() + owner.getWidth() / 2 - owner.getVerticalRange() / 2);
     * y = Math.round(owner.getY() + owner.getHeight());
     * height = owner.getHorizontalRange(); //das Rectangle wird um 90 Grad gedreht, somit werden height und width vertauscht
     * width = owner.getVerticalRange();
     * break;
     * case 2: //links
     * x = Math.round(owner.getX() - owner.getHorizontalRange());
     * y = Math.round(owner.getY() + owner.getHeight() / 2 - owner.getVerticalRange() / 2);
     * height = owner.getVerticalRange(); //das Rectangle wird um 90 Grad gedreht, somit werden height und width vertauscht
     * width = owner.getHorizontalRange();
     * break;
     * case 3: //oben
     * x = Math.round(owner.getX() + owner.getWidth() / 2 - owner.getVerticalRange() / 2);
     * y = Math.round(owner.getY() - owner.getHorizontalRange());
     * height = owner.getHorizontalRange(); //das Rectangle wird um 90 Grad gedreht, somit werden height und width vertauscht
     * width = owner.getVerticalRange();
     * break;
     * default:
     * throw new IllegalArgumentException();
     * }
     * Attack attack = new Attack(x, y, width, height, registry, owner.getAttackDuration(), owner, this, owner.getDamage(), tileManager);
     * owner.setAttack(attack); //die Attacke wird als aktive Attacke des owners gespeichert, die alte Attacke wird überschrieben
     * registry.register(attack);
     * }
     * return null;
     * }
     */

    public void attack(PlayerTypeEntity owner, double x, double y, int height, int width, int duration, int damage) {
        if (!owner.isAttacking()) { //die Gültigkeit der Attacke wird überprüft, falls sie ungültig ist, geschieht nichts
            Attack attack = new Attack(x, y, width, height, registry, duration, owner, this, damage, tileManager);
            owner.setAttack(attack);
            owner.setAttacking(true);
            attacks.add(attack);
        }
    }

    /**
     * distributeDamage verteilt den gespeicherten Schaden und setzt ihn anschließend zurück
     */
    public void distributeDamage() {

        for (Attack attack : attacks) {
            for (Entity entity : collisionManager.getEntities(attack)) {//durchläuft alle Entities, die von der Attacke getroffen werden
                if (!attack.getHitList().contains(entity) && entity instanceof PlayerTypeEntity && entity.getClass() != attack.getOwner().getClass()) { //überprüft, ob die Entity bereits bekannt ist und ob sie überhaupt Schaden nehmen kann

                    attack.getHitList().add(entity); //fügt die Entity der hitlist in der Attacke hinzu

                    ((PlayerTypeEntity) entity).takeDamage(attack.getDamage());//macht den Schaden. Rüstung etc. wird bei der Entity selbst abgezogen. Das gilt auch für "negativen Schaden"
                }
            }
        }
    }
}
