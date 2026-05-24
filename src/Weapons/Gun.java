package Weapons;

import entities.FollowingProjectile;
import entities.PlayerTypeEntity;
import entities.Projectile;
import entities.managers.AttackManager;

public class Gun extends  Weapon{
    public Gun (PlayerTypeEntity owner, AttackManager attackManager){
        super(owner,attackManager);
        attackCooldown = 500;//inMilli Sekunden
        attackDuration = 5;//in ticks
    }
    @Override
    public boolean use(){
        if (!super.use()){return false;}//on cooldown
        new Projectile(owner.getCenter() [0], owner.getCenter() [1], 20, 20, owner.registry, attackManager, owner, 4, owner.getDirection(), 300, damage);
        return true;
    }
}
