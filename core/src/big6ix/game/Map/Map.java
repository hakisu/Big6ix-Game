package big6ix.game.Map;

import big6ix.game.Constants;
import big6ix.game.PathFinding.HeuristicDistance;
import big6ix.game.PathFinding.TileConnection;
import big6ix.game.PathFinding.TilePath;
import big6ix.game.Tile;
import com.badlogic.gdx.ai.pfa.Connection;
import com.badlogic.gdx.ai.pfa.indexed.IndexedAStarPathFinder;
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
    private ArrayList<Room> rooms;
    private MapCreator mapCreator;
    private IndexedAStarPathFinder<Tile> pathFinder;

    public Map(int rowsAmount, int columnsAmount) {
        tileWidth = Constants.TILE_WIDTH;
        tileHeight = Constants.TILE_HEIGHT;
        this.rowsAmount = rowsAmount;
        this.columnsAmount = columnsAmount;

        mapArray = new Tile[rowsAmount][columnsAmount];
        rooms = new ArrayList<>();
        mapCreator = new MapCreator();
        mapCreator.generateMap(mapArray, rowsAmount, columnsAmount, rooms);
        pathFinder = new IndexedAStarPathFinder<>(this);
    }

    private void generateMap() {
        this.mapCreator.generateMap(this.mapArray, this.rowsAmount, this.columnsAmount, this.rooms);
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
        return mapCreator.getCurrentUniqueIdForWalkableTiles();
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
            // Allow diagonal movement
            if (tileIndexX > 0 && tileIndexY > 0 && mapArray[tileIndexY - 1][tileIndexX - 1].isWalkable()) {
                connections.add(new TileConnection(fromNode, mapArray[tileIndexY - 1][tileIndexX - 1]));
            }
            if (tileIndexX > 0 && tileIndexY < (rowsAmount - 1) && mapArray[tileIndexY + 1][tileIndexX - 1].isWalkable()) {
                connections.add(new TileConnection(fromNode, mapArray[tileIndexY + 1][tileIndexX - 1]));
            }
            if (tileIndexX < (columnsAmount - 1) && tileIndexY > 0 && mapArray[tileIndexY - 1][tileIndexX + 1].isWalkable()) {
                connections.add(new TileConnection(fromNode, mapArray[tileIndexY - 1][tileIndexX + 1]));
            }
            if (tileIndexX < (columnsAmount - 1) && tileIndexY < (rowsAmount - 1) && mapArray[tileIndexY + 1][tileIndexX + 1].isWalkable()) {
                connections.add(new TileConnection(fromNode, mapArray[tileIndexY + 1][tileIndexX + 1]));
            }
        }

        return connections;
    }
}