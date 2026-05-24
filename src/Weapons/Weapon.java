package Weapons;

import entities.PlayerTypeEntity;
import entities.managers.AttackManager;

public abstract class Weapon {
    protected int damage;
    protected int attackCooldown;
    protected String name;
    protected String info;
    protected PlayerTypeEntity owner;
    protected AttackManager attackManager;
    protected long lastUsed;
    public int attackDuration;

    public Weapon(PlayerTypeEntity owner, AttackManager attackManager){
        this.owner = owner;
        this.attackManager = attackManager;
        lastUsed = System.currentTimeMillis();
        attackDuration = 100;//in ticks
    }
    public boolean use(){
        System.out.println(System.currentTimeMillis() - lastUsed);
        if (System.currentTimeMillis() - lastUsed < attackCooldown){
            return false; //kann nicht benutzt werden


        }

        lastUsed = System.currentTimeMillis();
        owner.setAttacking(attackDuration);
        return true;//ability kann benutzt werden
    }
}
