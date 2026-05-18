package entities;

import entities.managers.EntityManager;
import entities.managers.EntityRegistry;
import tools.Vector;
import entities.managers.AttackManager;

import java.util.ArrayList;

public class Enemy extends PlayerTypeEntity {
    Player player;
    public Enemy(int x, int y, int width, int height, int hitCooldown, EntityRegistry registry, AttackManager attackManager) {
        super(x, y, width, height, hitCooldown, registry, attackManager);
        setSpeed(2);
        viewRange = 500;
        mass = 1;
        health = 1; //zu Testzwecken, für spätere Version unbedingt anpassen!!!
        player = null;
    }
    public void update() {
        super.update();
        if (health <= 0) {
            registry.unregister(this);
            int triggerAnimation = 1; //Platzhalter für Todesanimation
            return;
        }

        if (player != null){

        }
        else {
            ArrayList<Entity> inView = getInView();
            for (Entity entity : inView) {


                if (entity instanceof Player) {//player im sichtfeld
                    player = (Player) entity; //spieler speichern
                }
            }
        }
        if (player != null) {//player im sichtfeld
            Vector vector = new Vector(getX(), getY(), player.getX(), player.getY());
            if (vector.getLength() > speed) { //damit der Enemy nicht immer über sein ziel hinausgeht
                vector.setLength(speed);
            }

            if (registry.getInRange(this, 100).contains(player)) { //gegner stoppen wenn sie angreifen können
                vector.setLength(0);
                tryAttackEntity((PlayerTypeEntity) player);
            }
            move(vector);//schritt ausführen
        }
    }

    private void tryAttackEntity(PlayerTypeEntity targetPlayer) {
        direction = getDirectionTo(targetPlayer.getCenter()[0], targetPlayer.getCenter()[1]);
        attackManager.newAttack(registry, this);
        attackManager.attack(attack);
    }
}
