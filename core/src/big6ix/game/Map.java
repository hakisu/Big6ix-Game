package big6ix.game;

import big6ix.game.PathFinding.TileConnection;
import com.badlogic.gdx.ai.pfa.Connection;
import com.badlogic.gdx.ai.pfa.indexed.IndexedGraph;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;

import java.util.ArrayList;

public class Map implements IndexedGraph<Tile> {

    private Tile[][] mapArray;
    private int tileWidth;
    private int tileHeight;
    private int rowsAmount;
    private int columnsAmount;
    private ArrayList<Room> roomsTypes;
    private ArrayList<Room> mapRooms;

    public Map(int rowsAmount, int columnsAmount) {
        // Load all types of rooms from "*.room" files into ArrayList roomsTypes
        RoomsLoader roomsLoader = new RoomsLoader(Constants.ROOMS_DIRECTORY_PATH);
        roomsTypes = roomsLoader.loadRoomFiles();

        tileWidth = Constants.TILE_WIDTH;
        tileHeight = Constants.TILE_HEIGHT;
        this.rowsAmount = rowsAmount;
        this.columnsAmount = columnsAmount;

        mapRooms = new ArrayList<>();
        mapArray = new Tile[rowsAmount][columnsAmount];
        generateMap(rowsAmount, columnsAmount);

    }

    public void render(SpriteBatch batch) {
        for (int i = 0; i < rowsAmount; ++i) {
            for (int j = 0; j < columnsAmount; ++j) {
                if (this.mapArray[i][j].getAtlasRegion() != null) {
                    batch.draw(this.mapArray[i][j].getAtlasRegion(), j * tileWidth, i * tileHeight);
                }
            }
        }
    }

    private void generateMap(int rowsAmount, int columnsAmount) {
        // Initialize whole map with empty tiles (tiles that have their tileType set to TileType.EMPTY)
        // Give each tile uniqueId which is necessary for IndexedAStarPathFinder
        for (int i = 0; i < rowsAmount; ++i) {
            for (int j = 0; j < columnsAmount; ++j) {
                mapArray[i][j] = new Tile(j + i * columnsAmount, TileType.EMPTY);
            }
        }

        for (int i = 0; i < roomsTypes.get(0).getRowsAmount(); i++) {
            for (int j = 0; j < roomsTypes.get(0).getColumnsAmount(); j++) {
                mapArray[i][j].setTileType(roomsTypes.get(0).getRoomArray()[i][j]);
            }
        }
        mapRooms.add(roomsTypes.get(0));
        mapRooms.get(0).setX(0);
        mapRooms.get(0).setY(0);

//        for (int temp : mapRooms.get(0).calculateWalkableTilesUniqueIds()) {
//            System.out.println(temp);
//        }

//        int posX, posY;
//        Random random = new Random();
//        posX = random.nextInt(100 - 40 + 1) + 40;
//        posY = random.nextInt(100 - 40 + 1) + 40;
//
//        for (int i = 0; i < roomsTypes.get(1).getRowsAmount(); ++i) {
//            for (int j = 0; j < roomsTypes.get(1).getColumnsAmount(); ++j) {
//                mapArray[i + posY][j + posX].setType(roomsTypes.get(1).getRoomArray()[i][j]);
//            }
//        }
//
//        int startingPosX = 26, startingPosY = 15;
//        int j = startingPosX;
//        while (j <= posX + 1) {
//            mapArray[startingPosY - 1][j].setType(1);
//            mapArray[startingPosY][j].setType(0);
//            mapArray[startingPosY + 1][j].setType(0);
//            mapArray[startingPosY + 2][j].setType(1);
//            ++j;
//        }
//
//        mapArray[startingPosY - 1][j].setType(1);
//        mapArray[startingPosY][j].setType(0);
//        mapArray[startingPosY + 1][j].setType(0);
//        mapArray[startingPosY + 2][j].setType(0);
//
//        mapArray[startingPosY - 1][j + 1].setType(1);
//        mapArray[startingPosY][j + 1].setType(1);
//        mapArray[startingPosY + 1][j + 1].setType(1);
//        mapArray[startingPosY + 2][j + 1].setType(1);
//
//        int i = startingPosY + 2;
//
//        while (i < posY + 1) {
//            mapArray[i][j - 1].setType(0);
//            mapArray[i][j].setType(0);
//            mapArray[i][j + 1].setType(1);
//            mapArray[i][j - 2].setType(1);
//            ++i;
//        }
    }

    public Tile[][] getMapArray() {
        return mapArray;
    }

    public int getTileWidth() {
        return tileWidth;
    }

    public int getTileHeight() {
        return tileHeight;
    }

    @Override
    public int getIndex(Tile node) {
        return node.getUniqueId();
    }

    @Override
    public int getNodeCount() {
        return rowsAmount * columnsAmount;
    }

    @Override
    public Array<Connection<Tile>> getConnections(Tile fromNode) {
        Array<Connection<Tile>> connections = new Array<>();

        int tileIndexX = fromNode.getUniqueId() % Constants.MAP_COLUMNS_AMOUNT;
        int tileIndexY = fromNode.getUniqueId() / Constants.MAP_COLUMNS_AMOUNT;

        // Return all possible connections from given Tile based on the status of "walkable" variables of its neighbours
        if (mapArray[tileIndexY][tileIndexX].isWalkable()) {
            if (tileIndexX > 0 && mapArray[tileIndexY][tileIndexX - 1].isWalkable()) {
                connections.add(new TileConnection(fromNode, mapArray[tileIndexY][tileIndexX - 1]));
            }
            if (tileIndexX < (columnsAmount - 1) && mapArray[tileIndexY][tileIndexX + 1].isWalkable()) {
                connections.add(new TileConnection(fromNode, mapArray[tileIndexY][tileIndexX + 1]));
            }
            if (tileIndexY > 0 && mapArray[tileIndexY - 1][tileIndexX].isWalkable()) {
                connections.add(new TileConnection(fromNode, mapArray[tileIndexY - 1][tileIndexX]));
            }
            if (tileIndexY < (rowsAmount - 1) && mapArray[tileIndexY + 1][tileIndexX].isWalkable()) {
                connections.add(new TileConnection(fromNode, mapArray[tileIndexY + 1][tileIndexX]));
            }
        }

        return connections;
    }
}