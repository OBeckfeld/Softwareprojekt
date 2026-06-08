package Weapons;

import entities.PlayerTypeEntity;
import entities.Projectile;
import entities.managers.AttackManager;
import entities.managers.AttackRegistry;
import tools.TileManager;

public class Gun extends  Weapon{
    public Gun (PlayerTypeEntity owner, AttackRegistry attackRegistry, TileManager tileManager){
        super(owner,attackRegistry, tileManager);
        attackCooldown = 750;//inMilli Sekunden
        attackDuration = 5;//in ticks
        damage = 15;
    }
    @Override
    public boolean use(){
        if (!super.use()){return false;}//on cooldown
        new Projectile(owner.getCenter() [0], owner.getCenter() [1], 20, 20, owner.registry, attackRegistry, owner, 10, owner.getDirection(), 400, damage, tileManager);
        applyKnockback(2);
        return true;
    }
}
