package entities.managers;

import entities.Entity;
import entities.ViewBox;
import tools.TileManager;

import java.util.ArrayList;
import java.util.Arrays;


public class EntityManager implements EntityRegistry {
    private ArrayList<Entity> entities;
    private CollisionManager collisionManager;
    private TileManager tileManager;

    public EntityManager(CollisionManager collisions, TileManager tileManager) {
        entities = new ArrayList<>();
        collisionManager = collisions;
        this.tileManager =  tileManager;
    }

    public void setCollsisons(CollisionManager collisionManager) {this.collisionManager = collisionManager;}//temporär bis ihr euer Zeug gefixt habt

    @Override
    public void register(Entity entity){
        if (entity != null) {
            entities.add(entity);
            entities = quickSortForY(entities);
        }
    }

    public void unregister(Entity entity){entities.remove(entity);
    }

    public ArrayList<Entity> getEntities(){return new ArrayList<>(entities);}//damit die eigentliche Liste nicht von anderen Klassen bearbeitet werden kann

    public ArrayList<Entity> getInRange(Entity entity, int rangeX, int rangeY){//gibt alle entities zurück, die im sichtfeld von entity sind
        ViewBox viewBox = new ViewBox((entity.getCenter() [0]-rangeX/2), (entity.getCenter() [1]-rangeY/2), rangeY, rangeX,  this, tileManager);//viewBox wird zentriert
        collisionManager.checkCollisions();//collisions werden geupdatet, da die viewbox am anfang nicht da war | könnte das gameplay langsamer machen
        ArrayList<Entity> colls = collisionManager.getEntities(viewBox);//Entities checken, die in range sind

        entities.remove(viewBox);//view box wieder entfernen

        colls.remove(entity);//die original Entity wird nicht zu den Entities, die gesehen werden ,gezählt

        return colls;
    }

    public boolean collidesWith(Entity entity1, Entity entity2){ return collisionManager.getEntities(entity1).contains(entity2);}
    
    public ArrayList<Entity> getCollisions(Entity entity){ return collisionManager.getEntities(entity); }


    public ArrayList<Entity> quickSortForY(ArrayList<Entity> list)
    {
        Entity [] array = list.toArray(new Entity [list.size()]);
        quickSort(array, 0, array.length - 1); //Länge-1 da länge bei 1 startet aber Array bei 0 startet
        return new ArrayList<Entity>(Arrays.asList(array));
    }

    /**
     * Quick Sort
     */
    public void quickSort(Entity[] daten, int start, int ende)
    {
        if(start<ende) //prüft, ob start kleiner ist als ende, sonst wird nicht weiter sortiert
        {
            int pIndex=sortiere(daten,start,ende); //Die Trennstelle zwischen größerem und kleinerem Teil wird bestimmt.
            quickSort(daten, start,pIndex-1); //kleinerer (linker) Teilbereich wird sortiert.
            quickSort(daten,pIndex+1,ende); //größerer (rechter) Teilbereich wird sortiert.
        }
    }

    private int sortiere(Entity[] daten,int start,int ende)
    {
        double pivot= daten[ende].getY()+ daten[ende].getHeight();
        double pivotZ= daten[ende].getZ();//Das letzte Element dient als Vergleichswert
        int pIndex=start; //Aktuelle Grenze für kleinere Elemente; hier landet später das Pivot.
        for(int i=start;i<ende;i++)
        {
            if(pivotZ > daten[i].getZ()) //wenn Zahl an Stelle i <= pivot
            {
                tauschen(daten,i,pIndex); //dann wird Zahl an Stelle i mit Zahl an Stelle pIndex getauscht.
                pIndex++; //Stelle an die später Pivot kommt, wird mit jeder kleineren Zahl, die nach links getauscht wird, um 1 erhöht.
            }
            else if(daten[i].getY() +daten[i].getHeight()<pivot) //wenn Zahl an Stelle i <= pivot
            {
                tauschen(daten,i,pIndex); //dann wird Zahl an Stelle i mit Zahl an Stelle pIndex getauscht.
                pIndex++; //Stelle an die später Pivot kommt, wird mit jeder kleineren Zahl, die nach links getauscht wird, um 1 erhöht.
            }
        }
        tauschen(daten,pIndex,ende); //Pivot wird an richtige (Trenn-)Stelle getauscht(links kleiner, rechts größer).
        return pIndex;
    }

    public void tauschen(Entity[] daten, int a, int b)
    {
        Entity i= daten[a];
        daten[a]=daten[b];
        daten[b]=i;
    }

}
