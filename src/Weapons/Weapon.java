package Weapons;

import entities.PlayerTypeEntity;
import entities.managers.AttackRegistry;
import tools.TileManager;
import tools.Vector;

public abstract class Weapon {
    protected int damage;
    protected int attackCooldown;
    protected String name;
    protected String info;
    protected PlayerTypeEntity owner;
    protected AttackRegistry attackRegistry;
    protected long lastUsed;
    protected int attackDuration;
    protected TileManager tileManager;

    /**
     * Erstellt eine neue Waffe mit Besitzer, AttackRegistry und TileManager.
     * Initialisiert außerdem den Zeitpunkt der letzten Nutzung und die Standard-Angriffsdauer.
     */
    public Weapon(PlayerTypeEntity owner, AttackRegistry attackRegistry, TileManager tileManager){
        this.owner = owner;
        this.attackRegistry = attackRegistry;
        lastUsed = System.currentTimeMillis();
        attackDuration = 100;//in ticks
        this.tileManager = tileManager;
    }

    /**
     * Versucht, die Waffe zu benutzen.
     * Prüft den Angriffscooldown, setzt bei erfolgreicher Nutzung den letzten Nutzungszeitpunkt
     * und versetzt den Besitzer für die Angriffsdauer in den Angriffs-Zustand.
     */
    public boolean use(){
        if (onCooldown()){
            return false; //kann nicht benutzt werden
        }

        lastUsed = System.currentTimeMillis();
        owner.setAttacking(attackDuration);
        return true;//ability kann benutzt werden
    }

    public boolean onCooldown(){
        if (System.currentTimeMillis() - lastUsed < attackCooldown){
            return true; //kann nicht benutzt werden
        }
        else {
            return false;
        }
    }

    /**
     * Wendet Rückstoß auf den Besitzer der Waffe an.
     * Die Rückstoßrichtung wird aus der entgegengesetzten Blickrichtung berechnet.
     */
    protected void applyKnockback(double kb){
        int dir = owner.getOppDirection(owner.getDirection());
        double x = owner.getX();
        double y = owner.getY();
        Vector kbVector = new Vector(owner.getX(), y, x+owner.getOffsetCoords(dir)[0], y+owner.getOffsetCoords(dir)[1]);
        kbVector.setLength(kb);
        owner.move(kbVector);
    }

    /**
     * Setzt den Schaden der Waffe.
     */
    public void setDamage(int damage) {
        this.damage = damage;
    }

    /**
     * Setzt den Angriffscooldown der Waffe.
     */
    public void setLastUsed(long time) {
        lastUsed = time;
    }
}