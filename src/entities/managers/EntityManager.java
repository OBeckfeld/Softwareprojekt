package entities.managers;

import entities.Entity;
import entities.ViewBox;

import java.util.ArrayList;
import java.util.Arrays;

public class EntityManager implements EntityRegistry {
    private ArrayList<Entity> entities;
    CollisionManager collisionManager;

    public EntityManager(){

        entities = new ArrayList<>();
    }

    @Override
    public void register(Entity entity){
        if (entity != null) {
            entities.add(entity);
            entities = bubbleSortForY(entities);
        }
    }

    public void unregister(Entity entity){entities.remove(entity);
    }

    public ArrayList<Entity> getEntities(){return new ArrayList<>(entities);}//damit die eigentliche Liste nicht von anderen Klassen bearbeitet werden kann

    public ArrayList<Entity> getInRange(Entity entity, int range){//gibt alle entities zurück, die im sichtfeld von entity sind
        ViewBox viewBox = new ViewBox((entity.getCenter() [0]-range/2), (entity.getCenter() [1]-range/2), range, range,  this);//viewBox wird zentriert
        collisionManager.checkCollisions();//collisions werden geupdatet, da die viewbox am anfang nicht da war | könnte das gameplay langsamer machen
        ArrayList<Entity> colls = collisionManager.getEntities(viewBox);//Entities checken, die in range sind

        entities.remove(viewBox);//view box wieder entfernen

        colls.remove(entity);//die original Entity wird nicht zu den Entities, die gesehen werden ,gezählt

        return colls;
    }

    public void setCollisions(CollisionManager collisionManager){
        this.collisionManager = collisionManager;
    }

    public boolean collidesWith(Entity entity1, Entity entity2){ return collisionManager.getEntities(entity1).contains(entity2);}
    
    public ArrayList<Entity> getCollisions(Entity entity){ return collisionManager.getEntities(entity); }

    private ArrayList<Entity> bubbleSortForY(ArrayList<Entity> entityList)
    {
        Entity [] entities = entityList.toArray(new Entity [entityList.size()]);
        int n = entities.length;                 //die Länge (n) der Datenliste  wird ermittelt
        for (int k = 0; k < n; k++)           //Die Schleife wird n mal ausgeführt
        {
            boolean flag=false;               //Eine flag wird Erstellt
            for (int i =0; i < n-k-1; i++)    //Der unsortierte Teil der Schleife wird durchgegangen
            {
                if (entities[i].getY() > entities[i+1].getY())    //Das Datenelement wird mit dem nächsten verglichen
                {
                    Entity temp = entities[i];
                    entities[i] = entities[i+1];    //Die Elemente werden getauscht
                    entities[i+1] = temp;
                    flag = true;
                }
            }
            if (!flag)                        //Falls die Liste schon sortiert ist wird abgebrochen
            {
                return new ArrayList<Entity>(Arrays.asList(entities));
            }
        }
        return new ArrayList<Entity>(Arrays.asList(entities));
    }
}
