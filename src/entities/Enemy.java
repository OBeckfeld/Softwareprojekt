package entities;

import entities.managers.EntityRegistry;
import tools.Vector;
import entities.managers.AttackManager;

import java.util.ArrayList;

public class Enemy extends PlayerTypeEntity {

    public Enemy(int x, int y, int width, int height, int hitCooldown, EntityRegistry registry, AttackManager attackManager) {
        super(x, y, width, height, hitCooldown, registry, attackManager);
        setSpeed(2);
        viewRange = 500;
        mass = 1;
        health = 1; //zu Testzwecken, für spätere Version unbedingt anpassen!!!
    }

    public Enemy(int x, int y, int width, int height, int health, int damage, int defense, int verticalRange, int horizontalRange, int hitCooldown, EntityRegistry registry, AttackManager attackManager) {
        super(x, y, width, height, hitCooldown, registry, attackManager);
        this.attackManager = attackManager;
        this.hitCooldown = hitCooldown;
        this.health = health;
        this.damage = damage;
        this.defense = defense;
        this.verticalRange = verticalRange;
        this.horizontalRange = horizontalRange;
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
                    tryAttackEntity(entity);
                }
                move(vector);//schritt ausführen
            }
        }
    }

    private void tryAttackEntity(Entity targetPlayer) {
        direction = getDirectionTo(targetPlayer.getCenter()[0], targetPlayer.getCenter()[1]);
        attackManager.newAttack(registry, this);
        attackManager.attack(attack);
    }
}
