package entities.managers;

import entities.Entity;
import entities.ViewBox;

import java.util.ArrayList;

public class EntityManager implements EntityRegistry {
    private ArrayList<Entity> entities;
    CollisionManager collisionManager;
    public EntityManager(){

        entities = new ArrayList<>();
    }

    @Override
    public void register(Entity entity){ //fügt eine (neu erstellte) Entity in die Liste hinzu
        entities.add(entity);
    }//fügt eine (neu erstellte) Entity in die Liste hinzu
    public void unregister(Entity entity){entities.remove(entity); //entfernt eine Entity aus der entities Liste
    }

    public ArrayList<Entity> getEntities(){return entities;}

    public ArrayList<Entity> getInView(Entity entity){//gibt alle entities zurück, die im sichtfeld von entity sind
        ViewBox viewBox = new ViewBox((entity.getCenter() [0]-entity.getViewRange()/2), (entity.getCenter() [1]-entity.getViewRange()/2), entity.getViewRange(), entity.getViewRange(),  this);//die x und y Koordinaten von der view box werden so offset dass es das Zentrum der box auf dem Zentrum der entity steht, sonst würde sie nicht das sichtfeld der Entity auf der linken und oberen seite abdecken und doppelt so weit in die anderen richtingen schauen
        collisionManager.checkCollisions();//collisions werden geupdatet, da die viewbox am anfang nicht da war könnte das gameplay langsamer machen
        ArrayList<Entity> colls = collisionManager.getEntities(viewBox);//Entities checken, die im sichtfeld sind

        entities.remove(viewBox);//view box wieder entfernen

        colls.remove(entity);//die original Entity wird nicht zu den Entities, die gesehen werden ,gezählt

        return colls;
    }
    public void setCollisions(CollisionManager collisionManager){
        this.collisionManager = collisionManager;
    }

}
