package tools;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class TileManager {

    public Tile[] tiles;
    private GameMaps gameMaps;
    public GameMaps.GameMap currentMap;

//tileSize muss doppeltes von der echten TileSize sein
    int tileSize = 120;
    int size = 512;

    public int getTileSize(){return tileSize;}


    public TileManager() {
        tiles = new Tile[10];
        gameMaps = new GameMaps();
        currentMap = gameMaps.getMap(0);

        loadTiles();
    }



//definiert die Tiles aus dem Spritesheet
    private void loadTiles() {
        try {
            BufferedImage sheet = ImageIO.read(
                    getClass().getResource("/sprites/pipeWallSpriteSheet.png")
            );

            SpriteSheet ss = new SpriteSheet(sheet, size, size);

            tiles[0] = new Tile();
            tiles[0].image = ss.getFrame(0, 0); // col 0

            tiles[1] = new Tile();
            tiles[1].image = ss.getFrame(0, 2); // col 2 (überspringt Trennstreifen)

            tiles[2] = new Tile();
            tiles[2].image = ss.getFrame(0, 4);

            tiles[3] = new Tile();
            tiles[3].image = ss.getFrame(0, 6);

            tiles[4] = new Tile();
            tiles[4].image = ss.getFrame(0, 8);

            tiles[5] = new Tile();
            tiles[5].image = ss.getFrame(0, 10);

            tiles[6] = new Tile();
            tiles[6].image = ss.getFrame(0, 12);

            tiles[7] = new Tile();
            tiles[7].image = ss.getFrame(0, 14);

            tiles[8] = new Tile();
            tiles[8].image = ss.getFrame(0, 16);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //zeichnet die currentMap
    public void draw(Graphics g) {
        int[][] map = currentMap.getLayout();
        for(int row = 0; row < map.length; row++) {
            for(int col = 0; col < map[0].length; col++) {

                int tileNum = map[row][col];

                g.drawImage(
                        tiles[tileNum].image,
                        col * tileSize, row * tileSize,
                        col * tileSize + tileSize, row * tileSize + tileSize,
                        0, 0, size, size / 2,
                        null
                );
            }
        }
    }

    public GameMaps.GameMap getCurrentMap() {
        return currentMap;
    }
}
