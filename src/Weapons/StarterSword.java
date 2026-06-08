package Weapons;

import entities.PlayerTypeEntity;
import entities.managers.AttackManager;
import entities.managers.AttackRegistry;
import tools.TileManager;

public class StarterSword extends Weapon {
    public StarterSword(PlayerTypeEntity owner, AttackRegistry attackRegistry, TileManager tileManager) {
        super(owner, attackRegistry, tileManager);
        damage = 10;
        attackCooldown = 1000;//inMilli Sekunden
        attackDuration = 30;//in ticks
    }
    @Override
    public boolean use(){
        if (!super.use()){return false;}//on cooldown
        int w = 50;//width
        int h = 100;//height
        double x;
        double y;
        int height;
        int width;
        switch (owner.getDirection()) { //die Position der Attacke wird abhängig von der Richtung des owners gesetzt
            case 0: //rechts
                x = Math.round(owner.getX() + owner.getWidth());
                y = Math.round(owner.getY() + owner.getHeight() / 2 - h / 2);
                height = h; //das Rectangle wird um 90 Grad gedreht, somit werden height und width vertauscht
                width = w;
                break;
                case 1: //unten
                    x = Math.round(owner.getX() + owner.getWidth() / 2 - h / 2);
                    y = Math.round(owner.getY() + owner.getHeight());
                    height = w; //das Rectangle wird um 90 Grad gedreht, somit werden height und width vertauscht
                    width = h;
                    break;
                case 2: //links
                    x = Math.round(owner.getX() - w);
                    y = Math.round(owner.getY() + owner.getHeight() / 2 - h / 2);
                    height = h; //das Rectangle wird um 90 Grad gedreht, somit werden height und width vertauscht
                    width = w;
                    break;
                case 3: //oben
                    x = Math.round(owner.getX() + owner.getWidth() / 2 - h / 2);
                    y = Math.round(owner.getY() - w);
                    height = w; //das Rectangle wird um 90 Grad gedreht, somit werden height und width vertauscht
                    width = h;
                    break;
                default:
                    throw new IllegalArgumentException();

            }

            attackRegistry.attack(owner, x, y, height, width, attackDuration, damage);
        return true;
    }
}
