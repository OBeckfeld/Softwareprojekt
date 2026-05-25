package Weapons;

import entities.Attack;
import entities.PlayerTypeEntity;
import entities.managers.AttackManager;
import tools.TileManager;

public class StarterSword extends Weapon {
    public StarterSword(PlayerTypeEntity owner, AttackManager attackManager, TileManager tileManager) {
        super(owner,attackManager, tileManager);
        damage = 10;
        attackCooldown = 1000;//inMilli Sekunden
        attackDuration = 100;//in ticks
    }
    @Override
    public boolean use(){
        if (!super.use()){return false;}//on cooldown
        if(owner.getAttack() == null || owner.getAttack().isExpired()) { //falls der owner noch keine Attacke hat oder seine Attacke abgelaufen ist, wird eine neue Attacke erstellt
            double x;
            double y;
            int height;
            int width;
            switch (owner.getDirection()) { //die Position der Attacke wird abhängig von der Richtung des owners gesetzt
                case 0: //rechts
                    x = Math.round(owner.getX() + owner.getWidth());
                    y = Math.round(owner.getY() + owner.getHeight() / 2 - owner.getVerticalRange() / 2);
                    height = owner.getVerticalRange(); //das Rectangle wird um 90 Grad gedreht, somit werden height und width vertauscht
                    width = owner.getHorizontalRange();
                    break;
                case 1: //unten
                    x = Math.round(owner.getX() + owner.getWidth() / 2 - owner.getVerticalRange() / 2);
                    y = Math.round(owner.getY() + owner.getHeight());
                    height = owner.getHorizontalRange(); //das Rectangle wird um 90 Grad gedreht, somit werden height und width vertauscht
                    width = owner.getVerticalRange();
                    break;
                case 2: //links
                    x = Math.round(owner.getX() - owner.getHorizontalRange());
                    y = Math.round(owner.getY() + owner.getHeight() / 2 - owner.getVerticalRange() / 2);
                    height = owner.getVerticalRange(); //das Rectangle wird um 90 Grad gedreht, somit werden height und width vertauscht
                    width = owner.getHorizontalRange();
                    break;
                case 3: //oben
                    x = Math.round(owner.getX() + owner.getWidth() / 2 - owner.getVerticalRange() / 2);
                    y = Math.round(owner.getY() - owner.getHorizontalRange());
                    height = owner.getHorizontalRange(); //das Rectangle wird um 90 Grad gedreht, somit werden height und width vertauscht
                    width = owner.getVerticalRange();
                    break;
                default:
                    throw new IllegalArgumentException();

            }
            Attack attack = attackManager.newAttack(owner, x, y, height, width,attackDuration,damage);
            attackManager.attack(attack);

        }
        return true;
    }
}
