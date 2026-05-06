package tools;

import main.Game;
import tools.SpriteSheet;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import tools.GameMaps;

public class TileManager {

    public Tile[] tiles;
    public int[][] map;
    private GameMaps gameMaps;
    private GameMaps.GameMap currentMap;


    int tileSize = 64;
    int size = 512;

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
    public TileManager() {
        tiles = new Tile[10];
        gameMaps = new GameMaps();
        currentMap = gameMaps.getMap(0);

        loadTiles();
    }



    private void loadTiles() {
        try {
            BufferedImage sheet = ImageIO.read(
                    getClass().getResource("/res/pipeWallSpriteSheet.png")
            );

            SpriteSheet ss = new SpriteSheet(sheet);

            tiles[0] = new Tile();
            tiles[0].image = ss.getSprite(0, 0, size, size);

            tiles[1] = new Tile();
            tiles[1].image = ss.getSprite(size*2, 0,  size, size);

            tiles[2] = new Tile();
            tiles[2].image = ss.getSprite(size*4, 0, size, size);

            tiles[3] = new Tile();
            tiles[3].image = ss.getSprite(size*6, 0, size, size);

            tiles[4] = new Tile();
            tiles[4].image = ss.getSprite(size*8, 0, size, size);

            tiles[5] = new Tile();
            tiles[5].image = ss.getSprite(size*10, 0, size, size);

            tiles[6] = new Tile();
            tiles[6].image = ss.getSprite(size*12, 0, size, size);

            tiles[7] = new Tile();
            tiles[7].image = ss.getSprite(size*14, 0, size, size);

            tiles[8] = new Tile();
            tiles[8].image = ss.getSprite(size*16, 0, size, size);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadMap() {
        for(int row = 0; row < map.length; row++) {
            for(int col = 0; col < map[0].length; col++) {
                map[row][col] = (row == 0 || col == 0 || row == 9 || col == 9) ? 1 : 0;
            }
        }
    }

    public void draw(Graphics g) {
        int[][] map = currentMap.getLayout();
        for(int row = 0; row < map.length; row++) {
            for(int col = 0; col < map[0].length; col++) {

                int tileNum = map[row][col];

                g.drawImage(
                        tiles[tileNum].image,
                        col * tileSize,
                        row * tileSize,
                        tileSize,
                        tileSize,
                        null
                );
            }
        }
    }
}
