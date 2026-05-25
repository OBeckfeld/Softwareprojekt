package Weapons;

import entities.PlayerTypeEntity;
import entities.Projectile;
import entities.managers.AttackManager;
import entities.managers.AttackRegistry;
import tools.TileManager;

public class Gun extends  Weapon{
    public Gun (PlayerTypeEntity owner, AttackRegistry attackRegistry, TileManager tileManager){
        super(owner,attackRegistry, tileManager);
        attackCooldown = 500;//inMilli Sekunden
        attackDuration = 5;//in ticks
    }
    @Override
    public boolean use(){
        if (!super.use()){return false;}//on cooldown
        new Projectile(owner.getCenter() [0], owner.getCenter() [1], 20, 20, owner.registry, attackRegistry, owner, 4, owner.getDirection(), 300, damage, tileManager);
        return true;
    }
}
