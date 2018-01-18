package big6ix.game.map;

import big6ix.game.Player;
import big6ix.game.Tile;
import big6ix.game.enemies.ManagerEnemies;
import big6ix.game.pathfinding.HeuristicDistance;
import big6ix.game.pathfinding.TileConnection;
import big6ix.game.pathfinding.TilePath;
import big6ix.game.screens.GameMain;
import big6ix.game.screens.ScreenGame;
import big6ix.game.utility.Utilities;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.pfa.Connection;
import com.badlogic.gdx.ai.pfa.indexed.IndexedAStarPathFinder;
import com.badlogic.gdx.ai.pfa.indexed.IndexedGraph;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;

import java.io.Serializable;
import java.util.ArrayList;

public class Map implements IndexedGraph<Tile>, Serializable {

    public static final int TILE_WIDTH = 75;
    public static final int TILE_HEIGHT = 75;
    public static final String ATLAS_WALL_NAME = "wall";
    public static final String ATLAS_FLOOR_NAME_0 = "floor0";
    public static final String ATLAS_FLOOR_NAME_1 = "floor1";
    public static final String ATLAS_FLOOR_NAME_2 = "floor2";
    public static final String ATLAS_FLOOR_NAME_3 = "floor3";
    public static final String ATLAS_FLOOR_WHITE_NAME = "floor_white";
    public static final String ATLAS_DOOR_NAME = "door";

    private MapData mapData;
    private MapState mapState;
    private MapCreator mapCreator;
    private transient IndexedAStarPathFinder<Tile> pathFinder;

    public Map() {
        mapCreator = new MapCreator();
        mapData = mapCreator.generateMapData();
        mapState = new MapState(mapData);
        pathFinder = new IndexedAStarPathFinder<>(this);
    }

    public Room getCurrentOccupiedRoom() {
        return mapState.getCurrentOccupiedRoom();
    }

    public int getRowsAmount() {
        return mapData.getRowsAmount();
    }

    public int getColumnsAmount() {
        return mapData.getColumnsAmount();
    }

    public Tile[][] getMapArray() {
        return mapData.getMapArray();
    }

    public ArrayList<Room> getRooms() {
        return mapData.getRooms();
    }

    public boolean[] getRoomsCompletionStatuses() {
        return mapState.getRoomCompletionStatus();
    }

    /**
     * Needs to be used if map was created not using constructor. For example by serialization.
     */
    public void initializeMap() {
        this.pathFinder = new IndexedAStarPathFinder<>(this);
        this.mapState.initializeSounds();
    }

    public void render(SpriteBatch batch, OrthographicCamera camera) {
        // Map rendering optimized to only display map tiles within camera visibility
        int amountOfTilesToDrawHorizontally = (int) ((Gdx.graphics.getWidth() / TILE_WIDTH) * ScreenGame.CAMERA_INITIAL_ZOOM) + 3;
        int amountOfTilesToDrawVertically = (int) ((Gdx.graphics.getHeight() / TILE_HEIGHT) * ScreenGame.CAMERA_INITIAL_ZOOM) + 3;
        int leftBoundIndexX = Math.max((int) camera.position.x / TILE_WIDTH - amountOfTilesToDrawHorizontally / 2 - 2, 0);
        int leftBoundIndexY = Math.max((int) camera.position.y / TILE_HEIGHT - amountOfTilesToDrawVertically / 2 - 2, 0);
        int rightBoundIndexX = Math.min(amountOfTilesToDrawHorizontally + leftBoundIndexX + 1, mapData.getColumnsAmount() - 1);
        int rightBoundIndexY = Math.min(amountOfTilesToDrawVertically + leftBoundIndexY + 1, mapData.getRowsAmount() - 1);

        for (int i = leftBoundIndexY; i <= rightBoundIndexY; ++i) {
            for (int j = leftBoundIndexX; j <= rightBoundIndexX; ++j) {
                if (this.getMapArray()[i][j].getAtlasRegion() != null) {
                    batch.draw(this.getMapArray()[i][j].getAtlasRegion(), j * TILE_WIDTH, i * TILE_HEIGHT, TILE_WIDTH, TILE_HEIGHT);
                }
            }
        }
    }

    public void update(ManagerEnemies managerEnemies, Player player, GameMain gameMain) {
        mapState.update(managerEnemies, player, gameMain);
    }

    public Room getRandomRoom() {
        int randomPositionInListOfRooms = Utilities.generateRandomInt(0, mapData.getRooms().size() - 1);

        return mapData.getRooms().get(randomPositionInListOfRooms);
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
        return mapCreator.getCurrentUniqueIdForWalkableTiles();
    }

    @Override
    public Array<Connection<Tile>> getConnections(Tile fromNode) {
        Array<Connection<Tile>> connections = new Array<>();

        int tileIndexX = fromNode.getIndex() % getColumnsAmount();
        int tileIndexY = fromNode.getIndex() / getColumnsAmount();

        // Return all possible connections from given Tile based on the status of "walkable" variables of its neighbours
        if (getMapArray()[tileIndexY][tileIndexX].isWalkable()) {
            if (tileIndexX > 0 && getMapArray()[tileIndexY][tileIndexX - 1].isWalkable()) {
                connections.add(new TileConnection(fromNode, getMapArray()[tileIndexY][tileIndexX - 1], this));
            }
            if (tileIndexX < (getColumnsAmount() - 1) && getMapArray()[tileIndexY][tileIndexX + 1].isWalkable()) {
                connections.add(new TileConnection(fromNode, getMapArray()[tileIndexY][tileIndexX + 1], this));
            }
            if (tileIndexY > 0 && getMapArray()[tileIndexY - 1][tileIndexX].isWalkable()) {
                connections.add(new TileConnection(fromNode, getMapArray()[tileIndexY - 1][tileIndexX], this));
            }
            if (tileIndexY < (mapData.getRowsAmount() - 1) && getMapArray()[tileIndexY + 1][tileIndexX].isWalkable()) {
                connections.add(new TileConnection(fromNode, getMapArray()[tileIndexY + 1][tileIndexX], this));
            }
            // Allow diagonal movement
            if (tileIndexX > 0 && tileIndexY > 0 && getMapArray()[tileIndexY - 1][tileIndexX - 1].isWalkable()) {
                connections.add(new TileConnection(fromNode, getMapArray()[tileIndexY - 1][tileIndexX - 1], this));
            }
            if (tileIndexX > 0 && tileIndexY < (mapData.getRowsAmount() - 1) && getMapArray()[tileIndexY + 1][tileIndexX - 1].isWalkable()) {
                connections.add(new TileConnection(fromNode, getMapArray()[tileIndexY + 1][tileIndexX - 1], this));
            }
            if (tileIndexX < (getColumnsAmount() - 1) && tileIndexY > 0 && getMapArray()[tileIndexY - 1][tileIndexX + 1].isWalkable()) {
                connections.add(new TileConnection(fromNode, getMapArray()[tileIndexY - 1][tileIndexX + 1], this));
            }
            if (tileIndexX < (getColumnsAmount() - 1) && tileIndexY < (mapData.getRowsAmount() - 1) && getMapArray()[tileIndexY + 1][tileIndexX + 1].isWalkable()) {
                connections.add(new TileConnection(fromNode, getMapArray()[tileIndexY + 1][tileIndexX + 1], this));
            }
        }

        return connections;
    }
}