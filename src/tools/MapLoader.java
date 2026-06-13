package tools;

import Weapons.Gun;
import Weapons.IronSword;
import Weapons.ShotGun;
import entities.enemies.Boss;
import entities.enemies.ExplodeEnemy;
import entities.enemies.RangedEnemy;
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
    private int indexLimit = 9;
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

    /**
     * Erstellt einen neuen MapLoader mit allen benötigten Referenzen zum Laden und Erzeugen einer Map.
     * Speichert Tile-Größe, Registries, Manager, Eingaben und GamePanel für die spätere Map-Erstellung.
     */
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
     * Überprüft, ob der Player eine geöffnete Tür berührt hat, und lädt gegebenenfalls die nächste Map.
     */
    public void checkMapUpdate() {
        for (Entity entity : registry.getEntities()) {
            if (entity instanceof Door) {
                if (((Door) entity).isOpen()) {
                    for (Entity e : collisionManager.getEntities(entity)) {
                        if (e instanceof Player) {
                            ((Player) e).unlockWeapon(mapIndex);
                            buildMap();
                        }
                    }
                }
            }
        }
    }

    /**
     * Lädt die Mapdaten aus der passenden JSON-Datei.
     * Speichert die Entity-Map und Tile-Map aus der Datei in den entsprechenden Arrays.
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
     * Wandelt ein zweidimensionales int-Array aus einem JSON-String in ein reguläres int[][]-Array um.
     *
     * @param json String, welcher das JSON-Array enthält
     * @param key Key, welcher vor dem Array liegt
     * @return Array mit den Werten des JSON-Arrays
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
     * Spawnt anhand einer Entity-ID die passende Entity an den angegebenen Koordinaten.
     * Je nach ID werden Spieler, Türen, Waypoints oder verschiedene Gegnertypen erzeugt.
     *
     * @param entityId ID, welche die Art der Entity festlegt
     * @param x x-Koordinate, an welcher die Entity gespawnt werden soll
     * @param y y-Koordinate, an welcher die Entity gespawnt werden soll
     */
    public void spawnEntity(int entityId, int x, int y) {
        switch(entityId) {
            case 1:
                new Player(x, y, 80, 80, registry, keyboardInputs, attackRegistry, tileManager, gamePanel);
                break;
            case 2:
                new Door(x, y, registry, attackRegistry, tileManager);
                break;
            case 3:
                new Waypoint(x, y, registry, attackRegistry, tileManager);
                break;
            case 10:
                new Enemy(x, y , 60, 60, 20, 5, 0, 120, 60, 20, 2000, registry, attackRegistry, tileManager, gamePanel);
                break;
            case 11:
                new Enemy(x, y , 61, 61, 60, 10, 0, 120, 60, 20, 2000, registry, attackRegistry, tileManager, gamePanel);
                break;
            case 12:
                new Enemy(x, y , 62, 62, 100, 20, 0, 120, 60, 20, 2000, registry, attackRegistry, tileManager, gamePanel);
                break;
            case 13:
                new Enemy(x, y , 63, 63, 100, 20, 15, 120, 60, 20, 2000, registry, attackRegistry, tileManager, gamePanel);
                break;
            case 14:
                new Enemy(x, y , 64, 64, 100, 5, 80, 120, 60, 20, 3000, registry, attackRegistry, tileManager, gamePanel);
                break;
            case 15:
                new Enemy(x, y , 50, 50, 75, 10, 10, 100, 70, 15, 3000, registry, attackRegistry, tileManager, gamePanel);
                break;
            case 16:
                new Enemy(x, y , 65, 65, 110, 45, 20, 150, 80, 40, 3500, registry, attackRegistry, tileManager, gamePanel);
                break;
            case 20:
                RangedEnemy rangedEnemyLvL1 = new RangedEnemy(x, y , 60, 60, 10, 2, 0, 300, 1000, registry, attackRegistry, tileManager, gamePanel);
                rangedEnemyLvL1.setWeapon(new Gun(rangedEnemyLvL1, attackRegistry, tileManager));
                break;
            case 21:
                RangedEnemy rangedEnemyLvL2 = new RangedEnemy(x, y , 60, 60, 60, 10, 0, 300, 2000, registry, attackRegistry, tileManager, gamePanel);
                rangedEnemyLvL2.setWeapon(new Gun(rangedEnemyLvL2, attackRegistry, tileManager));
                break;
            case 22:
                RangedEnemy rangedEnemyLvL3 = new RangedEnemy(x, y , 60, 60, 100, 20, 0, 300, 2000, registry, attackRegistry, tileManager, gamePanel);
                rangedEnemyLvL3.setWeapon(new Gun(rangedEnemyLvL3, attackRegistry, tileManager));
                break;
            case 23:
                RangedEnemy rangedEnemyLvL4 = new RangedEnemy(x, y , 60, 60, 100, 20, 15, 300, 2000, registry, attackRegistry, tileManager, gamePanel);
                rangedEnemyLvL4.setWeapon(new Gun(rangedEnemyLvL4, attackRegistry, tileManager));
                break;
            case 24:
                RangedEnemy rangedEnemyLvL5 = new RangedEnemy(x, y , 60, 60, 100, 5, 80, 300, 3000, registry, attackRegistry, tileManager, gamePanel);
                rangedEnemyLvL5.setWeapon(new Gun(rangedEnemyLvL5, attackRegistry, tileManager));
                break;
            case 25:
                RangedEnemy rangedEnemyLvL6 = new RangedEnemy(x, y , 55, 60, 150, 25, 15, 600, 3000, registry, attackRegistry, tileManager, gamePanel);
                rangedEnemyLvL6.setWeapon(new Gun(rangedEnemyLvL6, attackRegistry, tileManager));
                break;
            case 30:
                new ExplodeEnemy(x, y , 60, 60, registry, attackRegistry, tileManager, gamePanel);
                break;
            case 40:
                Boss firstBoss = new Boss(x, y , 100, 100, 500, 15, 75, 800, 5000, registry, attackRegistry, tileManager, gamePanel);
                firstBoss.setWeapon1(new ShotGun(firstBoss, attackRegistry, tileManager));
                firstBoss.setWeapon2(new IronSword(firstBoss, attackRegistry, tileManager));
                break;
            default:
                return;
        }
    }

    /*
     * Methode, welche die Map aufbaut
     */
    /**
     * Baut die aktuelle Map anhand der geladenen Mapdaten auf.
     * Entfernt alte Entities, behält den Player bei, entfernt Angriffe,
     * spawnt neue Entities und übergibt die TileMap an den TileManager.
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

    /**
     * Setzt den aktuellen Map-Index.
     */
    public void setMapIndex(int mapIndex) {
        this.mapIndex = mapIndex;
    }

    /**
     * Gibt den aktuellen Map-Index zurück.
     */
    public int getMapIndex() {
        return mapIndex;
    }

    /**
     * Gibt die maximale Anzahl vorhandener Map-Indizes zurück.
     */
    public int getMapIndexLimit() {
        return indexLimit;
    }
}