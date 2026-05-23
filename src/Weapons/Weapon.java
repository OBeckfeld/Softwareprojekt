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

    public Weapon(PlayerTypeEntity owner, AttackManager attackManager){
        this.owner = owner;
        this.attackManager = attackManager;
        lastUsed = System.currentTimeMillis();
    }
    public boolean use(){
        System.out.println(System.currentTimeMillis() - lastUsed);
        if (System.currentTimeMillis() - lastUsed < attackCooldown){
            return false; //kann nicht benutzt werden


        }

        lastUsed = System.currentTimeMillis();
        return true;//ability kann benutzt werden
    }
}
