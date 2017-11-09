package big6ix.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class Map {

    private int mapArray[][];
    private TextureAtlas.AtlasRegion atlasRegionFloor = null;
    private TextureAtlas.AtlasRegion atlasRegionWall = null;
    private int tileWidth;
    private int tileHeight;
    private int rowsAmount;
    private int columnsAmount;

    // Testing
    private ArrayList<Room> rooms;
    private Random random;

    public Map(int rowsAmount, int columnsAmount) {
        atlasRegionWall = GameMain.gameAtlas.findRegion(Constants.ATLAS_WALL_NAME);
        atlasRegionFloor = GameMain.gameAtlas.findRegion(Constants.ATLAS_FLOOR_NAME);
        tileWidth = Constants.TILE_WIDTH;
        tileHeight = Constants.TILE_HEIGHT;
        this.rowsAmount = rowsAmount;
        this.columnsAmount = columnsAmount;
        rooms = new ArrayList<Room>();
        generateRoom1();
        generateRoom2();

        generateMap(rowsAmount, columnsAmount);
    }

    public void render(SpriteBatch batch) {
        for (int i = 0; i < rowsAmount; ++i) {
            for (int j = 0; j < columnsAmount; ++j) {
                if (this.mapArray[i][j] == 0) {
                    batch.draw(atlasRegionFloor, j * 64, i * 64);
                } else if (this.mapArray[i][j] == 1) {
                    batch.draw(atlasRegionWall, j * 64, i * 64);
                }
            }
        }
    }

    private void generateRoom1() {
        Room room = new Room(new int[][]{
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 1},
                {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 1},
                {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 1},
                {1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 1},
                {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 1},
                {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1},
                {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
                {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
                {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
                {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
                {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
                {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
                {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
                {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
                {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
                {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
                {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
                {1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
                {1, 0, 0, 0, 0, 1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
                {1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
                {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
                {1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
                {1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1},
                {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1},
                {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1},
                {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
                {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
                {1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}
        });
        this.rooms.add(room);
    }

    private void generateRoom2() {
        Room room = new Room(new int[][]{
                {1, 1, 1, 1, 1, 1, 1, 1},
                {1, 0, 0, 0, 0, 0, 0, 1},
                {1, 0, 0, 0, 0, 0, 0, 1},
                {1, 1, 1, 1, 1, 1, 1, 1}
        });
        this.rooms.add(room);
    }

    private void generateMap(int rowsAmount, int columnsAmount) {
        mapArray = new int[rowsAmount][columnsAmount];
        for (int[] currentRow : mapArray) {
            Arrays.fill(currentRow, -1);
        }

        for (int i = 0; i < rooms.get(0).getRowsAmount(); ++i) {
            for (int j = 0; j < rooms.get(0).getColumnsAmount(); ++j) {
                mapArray[i][j] = rooms.get(0).getRoomArray()[i][j];
            }
        }

        int posX, posY;
        random = new Random();
        posX = random.nextInt(100 - 40 + 1) + 40;
        posY = random.nextInt(100 - 40 + 1) + 40;

        for (int i = 0; i < rooms.get(1).getRowsAmount(); ++i) {
            for (int j = 0; j < rooms.get(1).getColumnsAmount(); ++j) {
                mapArray[i + posY][j + posX] = rooms.get(1).getRoomArray()[i][j];
            }
        }

        int startingPosX = 26, startingPosY = 15;
        int j = startingPosX;
        while (j <= posX + 1) {
            mapArray[startingPosY - 1][j] = 1;
            mapArray[startingPosY][j] = 0;
            mapArray[startingPosY + 1][j] = 0;
            mapArray[startingPosY + 2][j] = 1;
            ++j;
        }

        mapArray[startingPosY - 1][j] = 1;
        mapArray[startingPosY][j] = 0;
        mapArray[startingPosY + 1][j] = 0;
        mapArray[startingPosY + 2][j] = 0;

        mapArray[startingPosY - 1][j+1] = 1;
        mapArray[startingPosY][j+1] = 1;
        mapArray[startingPosY + 1][j+1] = 1;
        mapArray[startingPosY + 2][j+1] = 1;

//        mapArray[startingPosY + 2][j] = 1;
        int i = startingPosY + 2;

        while (i < posY + 1) {
            mapArray[i][j - 1] = 0;
            mapArray[i][j] = 0;
            mapArray[i][j + 1] = 1;
            mapArray[i][j - 2] = 1;
            ++i;
        }
    }

    public int[][] getMapArray() {
        return mapArray;
    }

    public int getTileWidth() {
        return tileWidth;
    }

    public int getTileHeight() {
        return tileHeight;
    }
}
