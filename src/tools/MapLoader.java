package tools;

import entities.managers.EntityRegistry;
import entities.managers.AttackRegistry;
import entities.managers.CollisionManager;
import inputs.KeyboardInputs;
import entities.Entity;
import entities.Player;
import entities.enemies.Enemy;
import entities.Door;
import entities.Waypoint;
import entities.Attack;
import main.GamePanel;

import java.util.ArrayList;
import java.nio.file.Files;
import java.nio.file.Path;

public class MapLoader {
    //Legt die aktuelle Map fest, welche geladen werden soll
    private int mapIndex = 1;
    //Anzahl an vorhandenen Datensätzen für die Maps
    private int indexLimit = 5;
    //Arrays, welche die Informationen über die Map enthalten
    private int[][] entityMap;
    private int[][] tileMap;
    //Maße jedes Tiles auf der Map, Hilfe für das Platzieren der Entities
    private int tileSize;
    //Referenzen von Klassen, die zum generieren der Entities benötigt werden
    private EntityRegistry registry;
    private KeyboardInputs keyboardInputs;
    private AttackRegistry attackRegistry;
    private CollisionManager collisionManager;
    private TileManager tileManager;
    private GamePanel gamePanel;

    public MapLoader(int tileSize, EntityRegistry registry, KeyboardInputs keyboardInputs, AttackRegistry attackRegistry, CollisionManager collisionManager, TileManager tileManager, GamePanel gamePanel) {
        this.tileSize = tileSize;
        this.registry = registry;
        this.keyboardInputs = keyboardInputs;
        this.attackRegistry = attackRegistry;
        this.collisionManager = collisionManager;
        this.tileManager = tileManager;
        this.gamePanel=gamePanel;
    }

    /**
     * Überprüft, ob der Player eine geöffnete Tür berührt hat, und lädt gegebenenfalls die nächste Map
     */
    public void checkMapUpdate() {
        for (Entity entity : registry.getEntities()) {
            if (entity instanceof Door) {
                if (((Door) entity).isOpen()) {
                    for (Entity e : collisionManager.getEntities(entity)) {
                        if (e instanceof Player) {
                            buildMap();
                        }
                    }
                }
            }
        }
    }

    /**
     * Methode, welche die Daten der Map aus der angegebenen Datei entnimmt
     */
    public void fetchMapData() {
        if (mapIndex > indexLimit) { mapIndex = indexLimit; }
        //try-catch block, um Fehler zu vermeiden und gegebenenfalls auszugeben
        try {
            //Nächste Datei wird aus der Liste der Dateien entnommen und der Inhalt wird als String gespeichert
            String jsonContent = Files.readString(Path.of("src", "data", "mapData", "room_" +  mapIndex + ".json"));

            //Der Inhalt der Arrays aus der JSON Datei werden in den Attributen gespeichert
            entityMap = parseJsonArray(jsonContent, "entityMap");
            tileMap = parseJsonArray(jsonContent, "tileMap");
        }
        catch (Exception e) {
            //falls ein Fehler auftritt, wird die Fehlermeldung gedruckt
            e.printStackTrace();
        }
    }

    /**
     * Methode, welche aus einem in einem String gespeicherten zweidimensionalen Array vom Typ int ein reguläres Array macht
     * @param json String, welcher das JSON Array enthält
     * @param key Key, welcher vor dem Array liegt
     * @return Array mit den Werten des JSON Arrays
     */
    public int[][] parseJsonArray(String json, String key) {
        // Enterzeichen, Carriage Returns und Leerzeichen entfernen, damit die Verarbeitung einfacher ist
        String oneLineJson = json.replace("\r", "").replace("\n", "").replace(" ", "");

        //Position des keys im String finden
        int startIndex = oneLineJson.indexOf(key);

        //falls der key nicht vorhanden ist, wird eine Exception geworfen
        if (startIndex == -1) {
            throw new IllegalArgumentException("Key " + key + " not found in JSON.");
        }

        //Start und Ende des Arrays im String werden gefunden, dabei wird alles vor dem Start des Arrays ignoriert
        int arrayStart = oneLineJson.indexOf("[[", startIndex);
        int arrayEnd = oneLineJson.indexOf("]]", arrayStart);
        
        //falls das Array nicht gefunden wird, wird eine Exception geworfen
        if(arrayStart == -1 || arrayEnd == -1) {
            throw new IllegalArgumentException("Array " + key + " has wrong format");
        }

        //Inhalt des Arrays wird in einem String gespeichert
        String arrayContent = oneLineJson.substring(arrayStart + 2, arrayEnd);
        //String wird in Reihen aufgeteilt, Verwendung von "\\[", da "[" kein reguläres Zeichen ist, sondern eine Funktion verkörpert
        String[] separateRows = arrayContent.split("],\\[");

        //Array, welches das Endergebnis enthalten soll, wird erstellt
        int numberRows = separateRows.length;
        int[][] parsedArray = new int[numberRows][];

        //Einzelne Reihen werden verarbeitet
        for (int i = 0; i < numberRows; i++) {
            String cleanRow = separateRows[i].replace("[", "").replace("]", "");
            //Array mit einzelnen Werten wird erstellt
            String[] separateValues = cleanRow.split(",");
            //Array, welches das Endergebnis enthalten soll, erhält Wert für die Stelle i
            parsedArray[i] = new int[separateValues.length];

            //Einzelne Wert werden in das Ergebnis-Array üebrtragen
            for (int j = 0; j < separateValues.length; j++) {
                //Werte werden von String zu int umgewandlet und in das Ergebnis-Array gespeichert
                parsedArray[i][j] = Integer.parseInt(separateValues[j]);
            }
        }
        return parsedArray;
    }

    /**
     * Mithilfe angegebener ID und Koordinaten wird eine entsprechende Entity gespanwt
     * @param entityId ID, welche die Art der Entity festlegt
     * @param x x-Koordinate, an welcher die Entity gespawnt werden soll
     * @param y y-Koordinate, an welcher die Entity gespawnt werden soll
     */
    public void spawnEntity(int entityId, int x, int y) {
        switch(entityId) {
            case 1:
                new Player(x, y, 40, 80, registry, keyboardInputs, attackRegistry, tileManager, gamePanel);
                break;
            case 2:
                new Door(x, y, registry, attackRegistry, tileManager);
                break;
            case 3:
                new Waypoint(x, y, registry, attackRegistry, tileManager);
                break;
            case 4:
                new Enemy(x, y , 40, 40, 100, 10, 5, 120, 60, 20, 360, registry, attackRegistry, tileManager, gamePanel);
                break;
            default:
                return;
        }
    }

    /*
     * Methode, welche die Map aufbaut
     */
    public void buildMap() {
        Player player = null;
        //Entities werden von der Map entfernt
        ArrayList<Entity> previousEntities = new ArrayList<>(registry.getEntities());
        //Mapdaten werden eingeholt
        fetchMapData();
        for (Entity entity : previousEntities) {
            if (entity instanceof Player) {
                player = (Player) entity;
                continue;
            }
            if (entity instanceof Attack) {
                ((Attack) entity).expire();
                continue;
            }
            registry.unregister(entity);
        }
        //Entities werden gespawnt
        for (int i = 0; i < entityMap.length; i++) {
            for (int j = 0; j < entityMap[i].length; j++) {
                if(entityMap[i][j] == 1) {
                    player.setX(j * tileSize);
                    player.setY(i * tileSize);
                    continue;}
                spawnEntity(entityMap[i][j], j*tileSize, i*tileSize);
            }
        }
        //Werte aus der JSON Datei werden an TileManager übergeben
        tileManager.setTileMap(tileMap);

        //Index wird hochgezählt, damit beim nächsten Aufrauf der nächste Raum geladen wird
        mapIndex++;
    }

    public void setMapIndex(int mapIndex) {
        this.mapIndex = mapIndex;
    }

    public int getMapIndex() {
        return mapIndex;
    }
}
