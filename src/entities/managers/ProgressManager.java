package entities.managers;

import entities.Player;
import tools.MapLoader;
import skilltree.SkillTree;

import java.util.ArrayList;
import java.nio.file.Files;
import java.nio.file.Path;

public class ProgressManager {
    private Player player;
    private MapLoader mapLoader;
    private SkillTree skillTree;
    // Index, um die Speicherdatei zu benennen
    private int currentSavingIndex = 1;

    public ProgressManager(Player player, MapLoader mapLoader) {
        this.player = player;
        this.mapLoader = mapLoader;
    }

    public void saveProgress() {
        String skillTreeData = "    \"skillTree\": {\n" +
                "\"unlockedAbilities\": [\n";
        String jsonContent = "{\n" +
                "\"mapIndex\": " + mapLoader.getMapIndex() + ",\n" +
                "\"weapon\": " + player.getWeapon().getClass().getName() + ",\n" +
                "skillTreeData";
    }
}