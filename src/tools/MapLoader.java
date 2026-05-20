package tools;

import entities.managers.EntityRegistry;
import entities.managers.AttackManager;
import inputs.KeyboardInputs;
import entities.Player;
import entities.Enemy;
import entities.Door;

import java.util.ArrayList;
import java.nio.file.Files;
import java.nio.file.Path;

public class MapLoader {
    //Legt die aktuelle Map fest, welche geladen werden soll
    private int mapIndex = 0;
    //Liste mit den JSON Dateien, welche die Informationen enthalten
    private ArrayList<String> fileList;
    //Arrays, welche die Informationen über die Map enthalten
    private int[][] entityMap;
    private int[][] tileMap;
    //Referenzen von Klassen, die zum generieren der Entities benötigt werden
    private EntityRegistry registry;
    private KeyboardInputs inputs;
    private AttackManager attackManager;

    public MapLoader(ArrayList<String> fileList, EntityRegistry registry, KeyboardInputs inputs, AttackManager attackManager) {
        this.fileList = fileList;
        this.registry = registry;
        this.inputs = inputs;
        this.attackManager = attackManager;
    }

    /**
     * Methode, welche die Daten der Map aus der angegebenen Datei entnimmt
     */
    public void fetchMapData() {
        //try-catch block, um Fehler zu vermeiden und gegebenenfalls auszugeben
        try {
            //Nächste Datei wird aus der Liste der Dateien entnommen und der Inhalt wird als String gespeichert
            String jsonContent = Files.readString(Path.of("room_" +  fileList.get(mapIndex) + ".json"));

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
        
        //Position des keys im String finden
        int startIndex = json.indexOf("\"" + key + "\"");

        //falls der key nicht vorhanden ist, wird eine Exception geworfen
        if (startIndex == -1) {
            throw new IllegalArgumentException("Key " + key + " not fonud in JSON.");
        }

        //Start und Ende des Arrays im String werden gefunden, dabei wird alles vor dem Start des Arrays ignoriert
        int arrayStart = json.indexOf("[[", startIndex);
        int arrayEnd = json.indexOf("]]", arrayStart);
        
        //falls das Array nicht gefunden wird, wird eine Exception geworfen
        if(arrayStart == -1 || arrayEnd == -1) {
            throw new IllegalArgumentException("Array " + key + " has wrongf ormat");
        }

        //Inhalt des Arrays wird in einem String gespeichert
        String arrayContent = json.substring(arrayStart + 2, arrayEnd);
        //String wird in Reihen aufgeteilt
        String[] separateRows = arrayContent.split("], [");

        //Array, welches das Endergebnis entahlten soll, wird erstellt
        int numberRows = separateRows.length;
        int[][] parsedArray = new int[numberRows][];

        //Einzelne Reihen werden verarbeitet
        for (int i = 0; i < numberRows; i++) {
            //Unnötige Zeichen werden entfernt
            String cleanRow = separateRows[i].replaceAll("[","").replaceAll("]","").replaceAll(" ","");
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
                Player player = new Player(x, y, 40, 80, registry, inputs, attackManager);
                registry.register(player);
                break;
            case 2:
                Door door = new Door(x, y, registry);
                registry.register(door);
                break;
            case 3:
                Enemy enemy = new Enemy(x, y , 40, 40, 100, 10, 5, 120, 60, 360,registry, attackManager);
                registry.register(enemy);
                break;
            default:
                return;
        }
    }

    public void placeTile(int tileId, int x, int y) {
        switch(tileId) {
            case 1:
                String tile1 = "tile gespawnr"; //Platzhlater für Tile
                break;
            case 2:
                String tile2 = "tile gespawnr"; //Platzhlater für Tile
                break;
            case 3:
                String tile3 = "tile gespawnr"; //Platzhlater für Tile
                break;
            default:
                return;
        }
    }

    /*
     * Methode, welche die Map aufbaut
     */
    public void buildMap() {
        //Mapdaten werden eingeholt
        fetchMapData();
        //Berechnung später einfügen
        int x = 700;
        int y = 700;
        //Entities werden gespawnt
        for (int i = 0; i < entityMap.length; i++) {
            for (int j = 0; j < entityMap[i].length; j++) {
                spawnEntity(entityMap[i][j], j*100, i*100);
            }
        }
        //Tiles wreden platuiert
        for (int i = 0; i < tileMap.length; i++) {
            for (int j = 0; j < tileMap[i].length; j++) {
                placeTile(tileMap[i][j], j*100, i*100);
            }
        }
        //Index wird hochgezählt, damit beim nächsten Aufrauf der nächste Raum geladen wird
        mapIndex++;
    }

    public void setMapIndex(int mapIndex) {
        this.mapIndex = mapIndex;
    }
}
