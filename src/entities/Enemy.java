package entities;

import entities.managers.EntityManager;
import entities.managers.EntityRegistry;
import tools.Vector;
import entities.managers.AttackManager;

import java.util.ArrayList;

public class Enemy extends PlayerTypeEntity {

    private static final int STOP_RADIUS = 40; //temporär
    private static final int ATTACK_RANGE = 45; //temporär
    private Player targetPlayer = null; // wird gesetzt wenn Spieler im Sichtfeld

    public Enemy(int x, int y, int width, int height, int hitCooldown, EntityRegistry registry, AttackManager attackManager) {
        super(x, y, width, height, hitCooldown, registry, attackManager);
        setSpeed(2);
        viewRange = 500;
        mass = 1;
        health = 1; //zu Testzwecken, für spätere Version unbedingt anpassen!!!
    }
    public void update() {
        super.update();
        if (health <= 0) {
            registry.unregister(this); //crash, muss wo anders hin
            int triggerAnimation = 1; //Platzhalter für Todesanimation
            return;
        }
        ArrayList<Entity> inView = getInView();
        for (Entity entity : inView) {


            if (entity instanceof Player) {//player im sichtfeld
                Vector vector = new Vector(getX(), getY(), entity.getX(), entity.getY());

                if (vector.getLength() > speed) { //damit der Enemy nicht immer über sein ziel hinausgeht
                    vector.setLength(speed);
                }

                if (registry.getInRange(this, 100).contains(entity)) { //gegner stoppen wenn sie angreifen können
                    vector.setLength(0);
                }
                move(vector);//schritt ausführen
                tryAttackEntity(entity);
            }
        }
    }

    private void tryAttackEntity(Entity targetPlayer) {
        Vector distVector = new Vector(x, y, targetPlayer.getX(), targetPlayer.getY());
        boolean inRange = distVector.getLength() <= ATTACK_RANGE;

        attackManager.newAttack(registry, this); // <- kommt jetzt von PlayerTypeEntity
        attackManager.attack(attack);
    }
}
