package big6ix.game.map;

import big6ix.game.Constants;
import big6ix.game.ManagerEnemies;
import big6ix.game.Player;
import big6ix.game.Tile;
import big6ix.game.pathfinding.HeuristicDistance;
import big6ix.game.pathfinding.TileConnection;
import big6ix.game.pathfinding.TilePath;
import big6ix.game.utility.Utilities;
import com.badlogic.gdx.ai.pfa.Connection;
import com.badlogic.gdx.ai.pfa.indexed.IndexedAStarPathFinder;
import com.badlogic.gdx.ai.pfa.indexed.IndexedGraph;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;

public class Map implements IndexedGraph<Tile> {

    private MapData mapData;
    private MapState mapState;
    private MapCreator mapCreator;
    private IndexedAStarPathFinder<Tile> pathFinder;
    private int tileWidth;
    private int tileHeight;

    public Map() {
        tileWidth = Constants.TILE_WIDTH;
        tileHeight = Constants.TILE_HEIGHT;

        mapCreator = new MapCreator();
        mapData = mapCreator.generateMapData();
        mapState = new MapState(mapData);
        pathFinder = new IndexedAStarPathFinder<>(this);
    }

    public Tile[][] getMapArray() {
        return mapData.getMapArray();
    }

    public int getRowsAmount() {
        return mapData.getRowsAmount();
    }

    public int getColumnsAmount() {
        return mapData.getColumnsAmount();
    }

    public int getTileWidth() {
        return tileWidth;
    }

    public int getTileHeight() {
        return tileHeight;
    }

    public void render(SpriteBatch batch) {
        for (int i = 0; i < mapData.getRowsAmount(); ++i) {
            for (int j = 0; j < mapData.getColumnsAmount(); ++j) {
                if (this.getMapArray()[i][j].getAtlasRegion() != null) {
                    batch.draw(this.getMapArray()[i][j].getAtlasRegion(), j * tileWidth, i * tileHeight);
                }
            }
        }
    }

    public void update(ManagerEnemies managerEnemies, Player player) {
        mapState.update(managerEnemies, player);
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
                connections.add(new TileConnection(fromNode, getMapArray()[tileIndexY][tileIndexX - 1]));
            }
            if (tileIndexX < (getColumnsAmount() - 1) && getMapArray()[tileIndexY][tileIndexX + 1].isWalkable()) {
                connections.add(new TileConnection(fromNode, getMapArray()[tileIndexY][tileIndexX + 1]));
            }
            if (tileIndexY > 0 && getMapArray()[tileIndexY - 1][tileIndexX].isWalkable()) {
                connections.add(new TileConnection(fromNode, getMapArray()[tileIndexY - 1][tileIndexX]));
            }
            if (tileIndexY < (getRowsAmount() - 1) && getMapArray()[tileIndexY + 1][tileIndexX].isWalkable()) {
                connections.add(new TileConnection(fromNode, getMapArray()[tileIndexY + 1][tileIndexX]));
            }
            // Allow diagonal movement
            if (tileIndexX > 0 && tileIndexY > 0 && getMapArray()[tileIndexY - 1][tileIndexX - 1].isWalkable()) {
                connections.add(new TileConnection(fromNode, getMapArray()[tileIndexY - 1][tileIndexX - 1]));
            }
            if (tileIndexX > 0 && tileIndexY < (getRowsAmount() - 1) && getMapArray()[tileIndexY + 1][tileIndexX - 1].isWalkable()) {
                connections.add(new TileConnection(fromNode, getMapArray()[tileIndexY + 1][tileIndexX - 1]));
            }
            if (tileIndexX < (getColumnsAmount() - 1) && tileIndexY > 0 && getMapArray()[tileIndexY - 1][tileIndexX + 1].isWalkable()) {
                connections.add(new TileConnection(fromNode, getMapArray()[tileIndexY - 1][tileIndexX + 1]));
            }
            if (tileIndexX < (getColumnsAmount() - 1) && tileIndexY < (getRowsAmount() - 1) && getMapArray()[tileIndexY + 1][tileIndexX + 1].isWalkable()) {
                connections.add(new TileConnection(fromNode, getMapArray()[tileIndexY + 1][tileIndexX + 1]));
            }
        }

        return connections;
    }
}