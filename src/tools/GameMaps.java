package tools;

public class GameMaps {

    // 🔹 Inner class (represents ONE map)
    public static class GameMap {
        private int[][] layout;

        public GameMap(int[][] layout) {
            this.layout = layout;
        }

        public int[][] getLayout() {
            return layout;
        }
    }

    // 🔹 Store all maps here
    private GameMap[] maps;
    /**
     * 0  -  horizontal
     * 1  -  Kreuz
     * 2  -  vertikal
     * 3  -  links, unten
     * 4  -  links, oben
     * 5  -  rechts, oben
     * 6  -  rechts, unten
     * 7  -  leere Wand
     * 8  -  Boden
     */
    public GameMaps() {
        maps = new GameMap[2]; //Anzahl der aller Maps

        //jede Zeile muss dieselbe Länge haben (nicht map übergreifend)
        maps[0] = new GameMap(new int[][]{
                {6,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,3,7},
                {2,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,5,3},
                {2,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,2},
                {2,8,8,8,8,2,8,8,8,8,8,8,8,8,8,8,8,8,8,2},
                {2,8,8,8,8,2,8,8,8,8,8,8,8,8,8,8,8,8,8,2},
                {2,8,8,8,8,2,8,8,8,8,8,8,8,8,8,8,8,8,8,2},
                {2,8,8,8,8,2,8,8,8,8,8,8,8,8,8,8,8,8,8,2},
                {2,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,2},
                {2,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,2},
                {2,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,2},
                {5,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,4}
        });

        maps[1] = new GameMap(new int[][]{
                {6,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,3,7},
                {2,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,5,3},
                {2,8,8,8,8,6,3,8,8,8,8,8,8,8,8,8,8,8,8,2},
                {2,8,8,8,8,2,5,3,8,8,8,8,8,8,8,8,8,8,8,2},
                {2,8,8,8,8,2,6,4,8,8,8,8,8,8,8,8,8,8,8,2},
                {2,8,8,8,8,2,2,8,8,8,8,8,8,8,8,8,8,8,8,2},
                {2,8,8,8,8,2,2,8,8,8,8,8,8,8,8,8,8,8,8,2},
                {2,8,8,8,8,5,4,8,8,8,8,8,8,8,8,8,8,8,8,2},
                {2,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,2},
                {2,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,2},
                {5,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,4}
        });
    }

    public GameMap getMap(int index) {
        return maps[index];
    }
}







