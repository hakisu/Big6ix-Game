package big6ix.game;

public final class Constants {

    // AtlasRegion names
    public static final String ATLAS_BULLET_BASIC_NAME = "bullet";
    public static final String ATLAS_ENEMY_NAME = "enemy";
    public static final String ATLAS_PLAYER_DOWN_NAME = "player_down";
    public static final String ATLAS_PLAYER_LEFT_NAME = "player_left";
    public static final String ATLAS_PLAYER_UP_NAME = "player_up";
    public static final String ATLAS_PLAYER_RIGHT_NAME = "player_right";
    public static final String ATLAS_MAP_WALL_NAME = "wall";
    public static final String ATLAS_MAP_FLOOR_NAME = "floor";

    // Values responsible for game performance
    public static final int INITIAL_BULLETS_CAPACITY = 10000;
    public static final int INITIAL_ENEMIES_CAPACITY = 1000;
    // A single batch can draw no more than 8191 sprites. Otherwise it will throw IllegalArgumentException
    public static final int BATCH_MAX_NUMBER_OF_SPRITES = 2000;

    // Cursor properties
    public static final int CURSOR_X_HOTSPOT = 16;
    public static final int CURSOR_Y_HOTSPOT = 16;

    // Bullets properties
    public static final float BULLET_BASIC_SPEED = 17;
    public static final int BULLET_BASIC_WIDTH = 16;
    public static final int BULLET_BASIC_HEIGHT = 16;

    // Player properties
    public static final float PLAYER_SPEED = 6;
    public static final int PLAYER_WIDTH = 64;
    public static final int PLAYER_HEIGHT = 64;
    public static final float PLAYER_STARTING_X = 900;
    public static final float PLAYER_STARTING_Y = 500;
    public static final float PLAYER_FRAME_DURATION = 0.1f;

    // Enemies properties
    public static final int ENEMY_SHOOTER_WIDTH = 64;
    public static final int ENEMY_SHOOTER_HEIGHT = 64;
    public static final int ENEMY_SHOOTER_SPEED = 4;

    // Map properties
    public static final int MAP_ROWS_AMOUNT = 1000;
    public static final int MAP_COLUMNS_AMOUNT = 1000;

    // Tiles properties
    public static final int TILE_WIDTH = 64;
    public static final int TILE_HEIGHT = 64;

    // Files properties
    public static final String ROOMS_DIRECTORY_PATH = "data/rooms";

    private Constants() {
    }
}