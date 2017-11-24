package big6ix.game;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class Tile {

    private TileType tileType;
    // uniqueId is used for IndexedAStarPathFinder
    private int uniqueId;

    public Tile(int uniqueId, TileType tileType) {
        this.uniqueId = uniqueId;
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

    public int getUniqueId() {
        return uniqueId;
    }

    public float calculatePosX() {
        return (uniqueId % Constants.MAP_COLUMNS_AMOUNT) * Constants.TILE_WIDTH;
    }

    public float calculatePosY() {
        return (uniqueId / Constants.MAP_COLUMNS_AMOUNT) * Constants.TILE_HEIGHT;
    }
}
