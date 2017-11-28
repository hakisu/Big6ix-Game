package big6ix.game.Map;

import big6ix.game.*;
import big6ix.game.PathFinding.HeuristicDistance;
import big6ix.game.PathFinding.TileConnection;
import big6ix.game.PathFinding.TilePath;
import com.badlogic.gdx.ai.pfa.Connection;
import com.badlogic.gdx.ai.pfa.indexed.IndexedAStarPathFinder;
import com.badlogic.gdx.ai.pfa.indexed.IndexedGraph;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;

import java.util.ArrayList;

public class Map implements IndexedGraph<Tile> {

    private static int currentUniqueId = 0;

    private Tile[][] mapArray;
    private int tileWidth;
    private int tileHeight;
    private int rowsAmount;
    private int columnsAmount;
    private ArrayList<RoomShape> roomShapes;
    private ArrayList<Room> rooms;
    private IndexedAStarPathFinder<Tile> pathFinder;

    public Map(int rowsAmount, int columnsAmount) {
        // Load all types of rooms from "*.room" files into ArrayList roomShapes
        RoomsLoader roomsLoader = new RoomsLoader(Constants.ROOMS_DIRECTORY_PATH);
        roomShapes = roomsLoader.loadRoomFiles();

        tileWidth = Constants.TILE_WIDTH;
        tileHeight = Constants.TILE_HEIGHT;
        this.rowsAmount = rowsAmount;
        this.columnsAmount = columnsAmount;

        rooms = new ArrayList<>();
        mapArray = new Tile[rowsAmount][columnsAmount];
        generateMap(rowsAmount, columnsAmount);
        pathFinder = new IndexedAStarPathFinder<>(this);
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
        // Give each tile uniqueIdForWalkableTiles which is necessary for IndexedAStarPathFinder
        for (int i = 0; i < rowsAmount; ++i) {
            for (int j = 0; j < columnsAmount; ++j) {
                mapArray[i][j] = new Tile(j + i * columnsAmount, TileType.EMPTY);
            }
        }

        for (int i = 0; i < roomShapes.get(0).getRowsAmount(); i++) {
            for (int j = 0; j < roomShapes.get(0).getColumnsAmount(); j++) {
                TileType currentTileType = roomShapes.get(0).getRoomArray()[i][j];
                mapArray[i][j].setTileType(currentTileType);
                if (currentTileType.isWalkable()) {
                    mapArray[i][j].setUniqueIdForWalkableTiles(currentUniqueId++);
                }
            }
        }
        rooms.add(new Room(roomShapes.get(0)));
        rooms.get(0).setX(0);
        rooms.get(0).setY(0);

//        for (int temp : rooms.get(0).calculateWalkableTilesUniqueIds()) {
//            System.out.println(temp);
//        }

//        int posX, posY;
//        Random random = new Random();
//        posX = random.nextInt(100 - 40 + 1) + 40;
//        posY = random.nextInt(100 - 40 + 1) + 40;
//
//        for (int i = 0; i < roomShapes.get(1).getRowsAmount(); ++i) {
//            for (int j = 0; j < roomShapes.get(1).getColumnsAmount(); ++j) {
//                mapArray[i + posY][j + posX].setType(roomShapes.get(1).getRoomArray()[i][j]);
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

    public boolean searchPath(Tile startTile, Tile endTile, HeuristicDistance heuristicDistance, TilePath outPath) {
        return pathFinder.searchNodePath(startTile, endTile, heuristicDistance, outPath);
    }

    @Override
    public int getIndex(Tile node) {
        return node.getUniqueIdForWalkableTiles();
    }

    @Override
    public int getNodeCount() {
        System.out.println(currentUniqueId);
        return currentUniqueId;
    }

    @Override
    public Array<Connection<Tile>> getConnections(Tile fromNode) {
        Array<Connection<Tile>> connections = new Array<>();

        int tileIndexX = fromNode.getIndex() % Constants.MAP_COLUMNS_AMOUNT;
        int tileIndexY = fromNode.getIndex() / Constants.MAP_COLUMNS_AMOUNT;

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