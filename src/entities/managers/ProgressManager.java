package entities.managers;

import entities.Entity;
import entities.Player;
import entities.Waypoint;
import tools.MapLoader;
import skilltree.SkillTree;

import java.nio.file.Files;
import java.nio.file.Path;
import java.io.FileWriter;

public class ProgressManager {
    private Player player;
    private MapLoader mapLoader;
    private SkillTree skillTree;
    private CollisionManager collisionManager;
    // Index, um die Speicherdatei zu benennen
    private int currentSavingIndex = 1;


    public ProgressManager(Player player, MapLoader mapLoader, CollisionManager collisionManager) {
        this.player = player;
        this.mapLoader = mapLoader;
        this.skillTree = player.getSkillTree();
        this.collisionManager = collisionManager;
    }

    /**
     * Interaktion des Spielers mit Waypoint wird geprüft
     * Falls der Spieler einen Waypoint berührt, wird gespeichert
     */
    public void checkSaveRequests() {
        // Waypoint wird gesucht
        for (Entity entity : player.getRegistry().getEntities()) {
            if (entity instanceof Waypoint) {
                // Falls der Spieler mit dem Waypoint kollidiert, wird gespeichert
                if (collisionManager.getEntities(entity).contains(player)) {
                    if (!((Waypoint) entity).isUsed()) {
                        saveProgress();
                        ((Waypoint) entity).setUsed(true);
                    }
                }
            }
        }
    }

    public void setPlayer(Player player) {
        this.player = player;
        this.skillTree = player.getSkillTree();
    }

    public int getSavingIndex() {
        return currentSavingIndex;
    }

    /**
     * Fortschritt wird aus der neuesten Speicherdatei geladen
     */
    public void loadNewestProgress() {
        try {
            if(currentSavingIndex == 1) {
                return;
            }
            // Inhalt der JSON Datei wird gelesen und gespeichert
            // Von currentSavingIndex muss 1 abgezogen werden, da dieser Index immer auf die nächste zu speichernde Datei zeigt
            // Somit muss für den Zugriff auf die zuletzt gespeicherte Datei 1 abgezogen werden
            String jsonContent = Files.readString(Path.of("src", "data", "progressdata", "save_" +  (currentSavingIndex - 1) + ".json"));
            int mapIndex = parseJsonInt(jsonContent, "mapIndex");
            String weaponClass = parseJsonString(jsonContent, "weapon");
            int skillPoints = parseJsonInt(jsonContent, "skillPoints");
            String[] unlockedAbilities = parseJsonArray(jsonContent, "skillTreeData");
            // Werte aus der datei werden in die entsprechenden Klassen eingespeist
            mapLoader.setMapIndex(mapIndex);
            mapLoader.buildMap();
            player.unlockWeapon(currentSavingIndex);
            player.setWeapon(weaponClass);
            player.setSkillpoints(skillPoints);
            for (String abilityName : unlockedAbilities) {
                // Falls der name der ability existiert, wird sie unlocked
                if (abilityName == null || abilityName.isBlank()) {
                    continue;
                }
                skillTree.unlock(skillTree.getAbilityReference(abilityName), true);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Fortschritt wird in eine JSON Datei gespeichert
     */
    public void saveProgress() {
        // Daten des Skill Trees sammeln
        String[] abilityData = skillTree.getUnlockedAbilities();
        // String mit den Skills wird in die Form eines JSON Arrays gebracht
        String skillTreeData = "\"skillTreeData\":[";
        for (int i = 0; i < abilityData.length; i++) {
            skillTreeData += "\"" + abilityData[i] + "\"";
            // Damit nach dem letzten Wert kein Komma steht (JSON Syntax)
            if (i < abilityData.length - 1) {
                skillTreeData += ", ";
            }
        }
        // Array wird geschlossen
        skillTreeData += "]";
        // Alle Daten werden in einem String im JSON Format gesammelt
        // Von MapIndex muss 1 abgezogen werden, da in MapLoader nach dem aufbauen der Map der Index um 1 erhöht wird,
        // somit ist der Index des aktuellen Raumes immer 1 niedriger als der Index, welcher in MapLoader gespeichert ist
        String jsonContent = "{\n" +
                "\"mapIndex\":" + (mapLoader.getMapIndex() - 1) + ",\n" +
                "\"weapon\":\"" + player.getWeapon().getClass().getSimpleName() + "\",\n" +
                "\"skillPoints\":" + player.getSkillpoints() + ",\n" +
                skillTreeData + "\n}";
        // String wird in eine Datei geschrieben
        try {
            // Datei wird nach dem aktuellen Index benannt
            FileWriter fileWriter = new FileWriter("src/data/progressdata/save_" + currentSavingIndex + ".json");
            // Inhalt wird in die Datei eingespeist
            fileWriter.write(jsonContent);
            fileWriter.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        // Index wird für nächste Speicherung erhöht
        currentSavingIndex = mapLoader.getMapIndex();
    }

    /**
     * Methode, um aus einem JSON int (welcher in einem String vorliegt) einen regulären Java int zu machen
     * @param json String, welcher den JSON int enthält
     * @param key Key, welcher vor dem int liegt
     * @return int, welcher den Wert des JSON ints enthält
     */
    public int parseJsonInt(String json, String key) {
        // Position des keys im String wird ermittelt
        int startIndex = json.indexOf(key);
        // falls der key nicht gefunden wird, wird eine Exception geworfen
        if (startIndex == -1) {
            throw new IllegalArgumentException("Key " + key + " not found in JSON.");
        }
        // Start und Ende des int im String werden ermittelt
        int intStart = json.indexOf(":", startIndex) + 1;
        int intEnd = json.indexOf(",", intStart);
        // String, der zwischen Start und Ende liegt, wird zu int umgewandelt und zurückgegeben
        return Integer.parseInt(json.substring(intStart, intEnd));
    }

    /**
     * Methode, um aus einem JSON String (welcher in einem String vorliegt) einen regulären Java String zu machen
     * @param json String, welcher den JSON String enthält
     * @param key Key, welcher vor dem String liegt
     * @return String, welcher den Wert des JSON Strings enthält
     */
    public String parseJsonString(String json, String key) {
        // Position des keys im String wird ermittelt
        int startIndex = json.indexOf(key);
        // falls der key nicht gefunden wird, wird eine Exception geworfen
        if (startIndex == -1) {
            throw new IllegalArgumentException("Key " + key + " not found in JSON.");
        }
        // Start und Ende des Strings im String werden ermittelt
        int intStart = json.indexOf(":", startIndex) + 1;
        int intEnd = json.indexOf(",", intStart);
        // String, der zwischen Start und Ende liegt, wird zurückgegeben
        return json.substring(intStart, intEnd).replace("\"", "");
    }

    /**
     * Abgeänderte Kopie aus MapLoader
     * Macht aus einem in einem String vorliegenden eindimensionalen JSON Array vom Typ String ein reguläres Java Array
     * @param json String, welcher das JSON Array enthält
     * @param key Key, welcher vor dem Array liegt
     * @return Array mit den Werten des JSON Arrays
     */
    public String[] parseJsonArray(String json, String key) {
        // Enterzeichen, Carriage Returns und Leerzeichen entfernen, damit die Verarbeitung einfacher ist
        String oneLineJson = json.replace("\r", "").replace("\n", "").replace(" ", "");

        //Position des keys im String finden
        int startIndex = oneLineJson.indexOf(key);

        //falls der key nicht vorhanden ist, wird eine Exception geworfen
        if (startIndex == -1) {
            throw new IllegalArgumentException("Key " + key + " not found in JSON.");
        }

        //Start und Ende des Arrays im String werden gefunden, dabei wird alles vor dem Start des Arrays ignoriert
        int arrayStart = oneLineJson.indexOf("[", startIndex);
        int arrayEnd = oneLineJson.indexOf("]", arrayStart);

        //falls das Array nicht gefunden wird, wird eine Exception geworfen
        if(arrayStart == -1 || arrayEnd == -1) {
            throw new IllegalArgumentException("Array " + key + " has wrong format");
        }

        //Inhalt des Arrays wird in einem String gespeichert
        String arrayContent = oneLineJson.substring(arrayStart + 1, arrayEnd).replace("\"", "");

        //Inhalt des Arrays wird aufgeteilt, in ein Array eingespeist und zurückgegeben
        if (arrayContent.isEmpty()) {
            return new String[0];
        }
        String[] parsedArray = arrayContent.split(",");
        return parsedArray;
    }
}