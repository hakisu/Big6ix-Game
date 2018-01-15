package big6ix.game;

import big6ix.game.map.Map;
import big6ix.game.screens.GameMain;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

import java.util.HashMap;

public enum TileType {

    FLOOR_BASIC_0(0, true, GameMain.getGameAtlas().findRegion(Map.ATLAS_FLOOR_NAME_0)),
    FLOOR_BASIC_1(1, true, GameMain.getGameAtlas().findRegion(Map.ATLAS_FLOOR_NAME_1)),
    FLOOR_BASIC_2(2, true, GameMain.getGameAtlas().findRegion(Map.ATLAS_FLOOR_NAME_2)),
    FLOOR_BASIC_3(3, true, GameMain.getGameAtlas().findRegion(Map.ATLAS_FLOOR_NAME_3)),
    FLOOR_WHITE(4, true, GameMain.getGameAtlas().findRegion(Map.ATLAS_FLOOR_WHITE_NAME)),
    WALL_BASIC(100, false, GameMain.getGameAtlas().findRegion(Map.ATLAS_WALL_NAME)),
    DOOR(1000, false, GameMain.getGameAtlas().findRegion(Map.ATLAS_DOOR_NAME)),
    EMPTY(-1, false, null),
    UNKNOWN(-2, false, null);

    private static final java.util.Map<Integer, TileType> intToTileTypeMap = new HashMap<>();

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

    public TextureAtlas.AtlasRegion getAtlasRegion() {
        return atlasRegion;
    }

    public boolean isWalkable() {
        return walkable;
    }
}