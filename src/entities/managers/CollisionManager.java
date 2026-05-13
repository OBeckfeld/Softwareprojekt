package entities.managers;

import entities.Entity;
import tools.GameMaps;
import tools.TileManager;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.HashMap;

public class CollisionManager {
    private EntityManager entityManager;
    private HashMap<Entity, ArrayList<Entity>> entities;//Gibt für jede Entity alle anderen Entities zurück, mit denen es kollidiert
    private TileManager tiles;

    public CollisionManager(EntityManager entityManager, TileManager tiles){
        this.entityManager = entityManager;
        this.entities = new HashMap<>();
        this.tiles = tiles;
    }


    public ArrayList<Entity> getEntities(Entity entity){return entities.getOrDefault(entity, new ArrayList<>());}//getOrDefault, damit eine leere Liste statt null zurück gegeben wird, wenn key nicht vorhanden ist. Verhindert Null Pointer Exceptions

    public void checkCollisions(){
        entities.clear();//Map wird am Anfang des Frames geleert, um dopplungen mit vergangenen Frames zu vermeiden
        for(Entity entity : entityManager.getEntities()){//von jeder Entity wird mit jeder anderen Entity die Hitbox vergleicht
            for(Entity otherEntity : entityManager.getEntities()){
                if(entity != otherEntity && entity.getHurtbox().intersects(otherEntity.getHurtbox())){
                    if(!entities.containsKey(entity)){//falls die Entity noch nicht in der Liste erfasst wurde, wird eine neue Liste erstellt
                        entities.put(entity, new ArrayList<>());
                    }
                    entities.get(entity).add(otherEntity);//in die Liste wird die Entity eingefügt, die mit der aktuellen Entity kolliedert
                }
            }
        }
    }

    public void resolveWallCollision(double dx, double dy, Entity entity){
        GameMaps.GameMap map = tiles.getCurrentMap();//aktuelle map
        int tileWidth = 1920 / map.getLayout()[0].length;//Weite einzelner Tiles
        int tileHeight = 1080 / map.getLayout().length;//Höhe einzelner Tiles

        double newX = entity.getX() + entity.getWidth() + dx;//rechtsseitig X
        double newY = entity.getY() + entity.getHeight() + dy;//unten Y


        }
    }


