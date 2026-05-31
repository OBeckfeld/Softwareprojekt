package tools;

import tools.SpriteSheet;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class TileManager {

    public Tile[] tiles;
    private int[][] tileMap;

    //tileSize muss doppeltes von der echten TileSize sein
    //muss aufpassen, dass tileSize / 2 * currentMap.getWidth größer als die Width der Kamera ist. Gleiches gilt für die Höhe
    private int tileSize = 120;
    private int size = 512;

    public TileManager() {
        tiles = new Tile[10];
        loadTiles();
    }

    public int getTileSize(){return tileSize;}

    public void setTileMap(int[][] tileMap) {
        this.tileMap = tileMap;
    }

    public int[][] getTileMap() {
        return tileMap;
    }

    // Lädt die Tiles aus dem SpriteSheet und speichert sie im Array tiles
    private void loadTiles() {
        try {
            BufferedImage sheet = ImageIO.read(
                    getClass().getResource("/data/sprites/pipeWallSpriteSheet.png")
            );

            SpriteSheet ss = new SpriteSheet(sheet);

            for (int i = 0; i < tiles.length - 1; i++) {
                tiles[i] = new Tile();
                tiles[i].image = ss.getSprite(size * i * 2, 0, size, size);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //zeichnet die currentMap
    public void draw(Graphics g) {
        for(int row = 0; row < tileMap.length; row++) {
            for(int col = 0; col < tileMap[0].length; col++) {

                int tileNum = tileMap[row][col];

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
