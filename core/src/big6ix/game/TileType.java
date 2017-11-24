package big6ix.game;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;

import java.util.HashMap;
import java.util.Map;

public enum TileType {
    FLOOR_BASIC(0, true, GameMain.getGameAtlas().findRegion(Constants.ATLAS_MAP_FLOOR_NAME)),
    WALL_BASIC(1, false, GameMain.getGameAtlas().findRegion(Constants.ATLAS_MAP_WALL_NAME)),
    EMPTY(-1, false, null),
    UNKNOWN(-2, false, null);

    private static final Map<Integer, TileType> intToTileTypeMap = new HashMap<>();

    static {
        for (TileType currentTileType : TileType.values()) {
            intToTileTypeMap.put(currentTileType.id, currentTileType);
        }
    }

    private int id;
    private boolean walkable;
    private TextureAtlas.AtlasRegion atlasRegion;

    TileType(int id, boolean walkable, TextureAtlas.AtlasRegion atlasRegion) {
        this.id = id;
        this.walkable = walkable;
        this.atlasRegion = atlasRegion;
    }

    public static TileType convertFromInt(int id) {
        TileType tileType = intToTileTypeMap.get(id);
        if (tileType == null) {
            return TileType.UNKNOWN;
        }
        return tileType;
    }

    public int getId() {
        return id;
    }

    public TextureAtlas.AtlasRegion getAtlasRegion() {
        return atlasRegion;
    }

    public boolean isWalkable() {
        return walkable;

    }
}
