package tools;

import tools.SpriteSheet;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import tools.GameMaps;

public class TileManager {

    public Tile[] tiles;
    private int[][] tileMap;
    private int scaleX;
    private int scaleY;

    //tileSize muss doppeltes von der echten TileSize sein
    int tileSize = 120;
    int size = 512;

    public int getTileSize(){return tileSize;}  

    public TileManager() {
        tiles = new Tile[10];
        loadTiles();
    }

    public void setTileMap(int[][] tileMap) {
        this.tileMap = tileMap;
    }

    public int[][] getTileMap() {
        return tileMap;
    }

    public void setScale(int scaleX, int scaleY) {
        this.scaleX = scaleX;
        this.scaleY = scaleY;
    }

    public int getScaleX() {
        return scaleX;
    }

    public int getScaleY() {
        return scaleY;
    }

    /**
     * definiert die Tiles aus dem Spritesheet
     */
    private void loadTiles() {
        try {
            BufferedImage sheet = ImageIO.read(
                    getClass().getResource("/data/sprites/pipeWallSpriteSheet.png")
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

    //zeichnet die currentMap
    public void draw(Graphics g) {
        for(int row = 0; row < tileMap.length; row++) {
            for(int col = 0; col < tileMap[0].length; col++) {

                int tileNum = tileMap[row][col];

                g.drawImage(
                        tiles[tileNum].image,
                        col * scaleX,
                        row * scaleY,
                        scaleX,
                        scaleY,
                        null
                );
            }
        }
    }
}
