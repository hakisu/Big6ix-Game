package big6ix.game;

import big6ix.game.map.Map;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

import java.io.Serializable;

public class Tile implements Serializable {

    private TileType tileType;
    // Each tile in map has unique index
    private int index;
    // uniqueIdForWalkableTiles is used for IndexedAStarPathFinder
    private int uniqueIdForWalkableTiles = -1;

    public Tile(int index, TileType tileType) {
        this.index = index;
        this.tileType = tileType;
    }

    public boolean isWalkable() {
        return this.tileType.isWalkable();
    }

    public TextureAtlas.AtlasRegion getAtlasRegion() {
        return this.tileType.getAtlasRegion();
    }

    public void setTileType(TileType tileType) {
        this.tileType = tileType;
    }

    public int getIndex() {
        return index;
    }

    public int getUniqueIdForWalkableTiles() {
        return uniqueIdForWalkableTiles;
    }

    public void setUniqueIdForWalkableTiles(int uniqueIdForWalkableTiles) {
        this.uniqueIdForWalkableTiles = uniqueIdForWalkableTiles;
    }

    public float calculatePosX(Map map) {
        return (index % map.getColumnsAmount()) * Map.TILE_WIDTH;
    }

    public float calculatePosY(Map map) {
        return (index / map.getColumnsAmount()) * Map.TILE_HEIGHT;
    }
}