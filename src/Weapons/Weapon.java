package Weapons;

import entities.PlayerTypeEntity;
import entities.managers.AttackRegistry;
import tools.TileManager;

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
        System.out.println(System.currentTimeMillis() - lastUsed);//Testmethode
        if (System.currentTimeMillis() - lastUsed < attackCooldown){
            return false; //kann nicht benutzt werden
        }

        lastUsed = System.currentTimeMillis();
        return true;//ability kann benutzt werden
    }
}
