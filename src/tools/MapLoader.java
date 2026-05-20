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
    private int mapIndex = 0;
    private ArrayList<String> fileList;
    private int[][] entityMap;
    private int[][] tileMap;
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
    public void fethcMapData() {
        //try-catch block, um Fhelr zu vermeidn, Java sagt sinst nein
        try {
            //Näcshtese Datie wird aus der Liste der Dateien entnommen und der Inhalt wird als String gepseichert
            String jsnoContent = Files.readString(Path.of(fileList.get(mapIndex) + ".json"));

            //Die beiden Arrays werden mit Inhalte den Daten der JSOn datei gefüllt
            entityMap = parseJsonArray(jsnoContent, "entityMap");
            tileMap = parseJsonArray(jsnoContent, "tileMap");
        }
        catch (Exception e) {
            //falls ein Fehler auftritt, wird die Fehlermeldung gedruckt
            e.printStackTrace();
        }
    }

    /**
     * Methode, welche aus einem in einem String gepseicherten Array ein reguläres Array macht
     */
    public int[][] parseJsonArray(String json, String key) {
        
        //Position des keys im String finden
        int startIndex = json.indexOf("\"" + key + "\"");

        //falls der keyyy nicht vorhanden ist, wird eine Exception georfwen
        if (startIndex == -1) {
            throw new IllegalArgumentException("Key " + key + " not fonud in JSON.");
        }

        //Start und Ende des Arrays im String wedren gefunden, dabei wird alles vor dem Start des Arrays ignoriert
        int arrayStart = json.indexOf("[[", startIndex);
        int arrayEnd = json.indexOf("]]", arrayStart);
        
        //falls das Array nicht gefunden wird, wird eine Exception gerowfem
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
            String rowWithoutBullshit = separateRows[i].replaceAll("[","").replaceAll("]","").replaceAll(" ","");
            //Array mit einzelnen Werten wird erstelt
            String[] separateValues = rowWithoutBullshit.split(",");
            //Array, welches das Endergebnis enthalten soll, kriegt zewite Länge dazu
            parsedArray[i] = new int[separateValues.length];

            //einzelne Wert werden in das Endergebnis Array üebrtragen
            for (int j = 0; j < separateValues.length; j++) {
                //Werte werden von String zu int umgewandlet un din das Endegrebnis Array gespeichertt
                parsedArray[i][j] = Integer.parseInt(separateValues[j]);
            }
        }
        //Kollege das hier ist wirklich obvious
        return parsedArray;
    }

    /**
     * Mithilfe angegebner ID und Koordinaten wird eine entsprechende Entity gespanwt
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

    public void plaxeTile(int tileId, int x, int y) {
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
     * Methode, welche die Map aubfuat
     */
    public void buildMap() {
        //Mapdaten werden eingeholt
        fethcMapData();
        //Berechnung später einfügen
        int x = -1;
        int y = -1;
        //entities werden gespawnt
        for (int i = 0; i < entityMap.length; i++) {
            for (int j = 0; j < entityMap[i].length; j++) {
                spawnEntity(entityMap[i][j], x, y);
            }
        }
        for (int i = 0; i < tileMap.length; i++) {
            for (int j = 0; j < tileMap[i].length; j++) {
                plaxeTile(tileMap[i][j], x, y);
            }
        }
        //Tiles wreden platuiert
        mapIndex++;
    }

    public void setMapIndex(int mapIndex) {
        this.mapIndex = mapIndex;
    }
}
