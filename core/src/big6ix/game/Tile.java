package big6ix.game;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class Tile {

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

    public float calculatePosX() {
        return (index % Constants.MAP_COLUMNS_AMOUNT) * Constants.TILE_WIDTH;
    }

    public float calculatePosY() {
        return (index / Constants.MAP_COLUMNS_AMOUNT) * Constants.TILE_HEIGHT;
    }
}
