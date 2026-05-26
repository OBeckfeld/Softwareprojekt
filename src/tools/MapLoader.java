package tools;

import entities.managers.EntityRegistry;
import entities.managers.AttackManager;
import entities.managers.CollisionManager;
import inputs.KeyboardInputs;
import entities.Entity;
import entities.Player;
import entities.enemies.Enemy;
import entities.Door;
import main.Game;

import java.util.ArrayList;
import java.nio.file.Files;
import java.nio.file.Path;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.geom.AffineTransform;
import java.awt.Rectangle;

public class MapLoader {
    //Legt die aktuelle Map fest, welche geladen werden soll
    private int mapIndex = 1;
    //Anzahl an vorhandenen Datensätzen für die Maps
    private int indexLimit = 2;
    //Arrays, welche die Informationen über die Map enthalten
    private int[][] entityMap;
    private int[][] tileMap;
    //Maße jedes Tiles auf der Map, Hilfe für das Platzieren der Entities
    private int scaleX;
    private int scaleY;
    //Referenzen von Klassen, die zum generieren der Entities benötigt werden
    private EntityRegistry registry;
    private KeyboardInputs keyboardInputs;
    private AttackManager attackManager;
    private CollisionManager collisionManager;
    private TileManager tileManager;

    public MapLoader(EntityRegistry registry, KeyboardInputs keyboardInputs, AttackManager attackManager, CollisionManager collisionManager, TileManager tileManager) {
        this.registry = registry;
        this.keyboardInputs = keyboardInputs;
        this.attackManager = attackManager;
        this.collisionManager = collisionManager;
        this.tileManager = tileManager;
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

    public void scaleMap(int[][] map) {
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        GraphicsConfiguration gc = gd.getDefaultConfiguration();

        Rectangle screenBounds = gc.getBounds();

        AffineTransform at = new AffineTransform();
        double screenWidth = Game.WIDTH;//Math.round(screenBounds.width / at.getScaleX());
        double screenHeight = Game.HEIGHT;//Math.round(screenBounds.height / at.getScaleY());

        scaleX = (int) (screenWidth / map[0].length);
        scaleY = (int) (screenHeight / map.length);
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
     * Methode, welche aus einem in einem String gespeicherten Array ein reguläres Array macht
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
     */
    public void spawnEntity(int entityId, int x, int y) {
        switch(entityId) {
            case 1:
                Player player = new Player(x, y, 40, 80, registry, keyboardInputs, attackManager, tileManager);
                registry.register(player);
                break;
            case 2:
                Door door = new Door(x, y, registry, attackManager, tileManager);
                registry.register(door);
                break;
            case 3:
                Enemy enemy = new Enemy(x, y , 40, 40, 1, 10, 5, 120, 60, 20, 360, registry, attackManager, tileManager);
                registry.register(enemy);
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
            registry.unregister(entity);
        }
        // Zu verwendende Skalierung der Map ermitteln
        scaleMap(tileMap);
        //Entities werden gespawnt
        for (int i = 0; i < entityMap.length; i++) {
            for (int j = 0; j < entityMap[i].length; j++) {
                if(entityMap[i][j] == 1) {
                    player.setX(j * scaleX);
                    player.setY(i * scaleY);
                    continue;}
                spawnEntity(entityMap[i][j], j*scaleX, i*scaleY);
            }
        }
        //Werte aus der JSON Datei werden an TileManager übergeben
        tileManager.setTileMap(tileMap);
        tileManager.setScale(scaleX, scaleY);

        //Index wird hochgezählt, damit beim nächsten Aufrauf der nächste Raum geladen wird
        mapIndex++;
    }

    public void setMapIndex(int mapIndex) {
        this.mapIndex = mapIndex;
    }
}