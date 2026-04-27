package gameData;

import entities.Entity;

import java.util.ArrayList;

public class GameMap {
    private ArrayList<Entity> entities;

    public GameMap(){
        entities = new ArrayList<>();
    }

    public void logEntity(Entity entity){ //fuegt einen neu erstellten Entity in die Liste hinzu
        entities.add(entity);
    }
}
