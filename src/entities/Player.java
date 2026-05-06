package entities;

import entities.managers.EntityRegistry;

import java.util.Set;

public class Player extends Entity {

    public Player(int x, int y, int height, int width, EntityRegistry registry) {
        super(x, y, height, width, registry);
    }


    public void update(Set<Integer> keyboardInputs) {
        movement.move(keyboardInputs, this);
    }

    public String getName(){//zum debuggen
        return "Player";
    }
}

