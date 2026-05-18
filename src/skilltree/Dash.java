package skilltree;

import entities.PlayerTypeEntity;
import entities.components.MovementComponent;

public class Dash extends Ability {
    public Dash(PlayerTypeEntity owner){
        super(owner);
        cost = 1; //wird in den unter klassen überschrieben
        cooldown = 1500; //in Millisekunden
        duration = 100; //in Millisekunden
    }
    @Override
    public String getDescription(){ return "Dash";}
    @Override
    public void unlock(){ unlocked = true; }

    @Override
    public boolean use(){
        if (!super.use()){ return false; } //offcooldown check
        return true;
    }

    @Override
    public void effect() {
        int dir = owner.getDirection();
        MovementComponent movementComponent = new MovementComponent();
        double force = 7;
        if (dir == owner.NORTH ){
            movementComponent.move(owner, 0, -7);
        }
        if (dir == owner.EAST ){
            movementComponent.move(owner, force, 0);
        }
        if (dir == owner.SOUTH ){
            movementComponent.move(owner, 0, 7);
        }
        if (dir == owner.WEST ){
            movementComponent.move(owner, -7, 0);
        }
    }
}
