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
    public int attackDuration;
    protected TileManager tileManager;

    public Weapon(PlayerTypeEntity owner, AttackRegistry attackRegistry, TileManager tileManager){
        this.owner = owner;
        this.attackRegistry = attackRegistry;
        lastUsed = System.currentTimeMillis();
        attackDuration = 100;//in ticks
        this.tileManager = tileManager;
    }

    public boolean use(){
        if (System.currentTimeMillis() - lastUsed < attackCooldown){
            return false; //kann nicht benutzt werden
        }

        lastUsed = System.currentTimeMillis();
        owner.setAttacking(attackDuration);
        return true;//ability kann benutzt werden
    }

    protected void applyKnockback(double kb){
        int dir = owner.getOppDirection(owner.getDirection());
        double x = owner.getX();
        double y = owner.getY();
        Vector kbVector = new Vector(owner.getX(), y, x+owner.getOffsetCoords(dir)[0], y+owner.getOffsetCoords(dir)[1]);
        kbVector.setLength(kb);
        owner.move(kbVector);
    }
}
