package big6ix.game;

public final class Constants {

    // AtlasRegion names
    public static final String ATLAS_BULLET_BASIC_NAME = "bullet";
    public static final String ATLAS_ENEMY_NAME = "enemy";
    public static final String ATLAS_MAP_WALL_NAME = "wall";
    public static final String ATLAS_MAP_FLOOR_NAME_0 = "floor0";
    public static final String ATLAS_MAP_FLOOR_NAME_1 = "floor1";
    public static final String ATLAS_MAP_FLOOR_NAME_2 = "floor2";
    public static final String ATLAS_MAP_FLOOR_NAME_3 = "floor3";
    public static final String ATLAS_MAP_FLOOR_WHITE_NAME = "floor_white";
    public static final String ATLAS_MAP_DOOR_NAME = "door";

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
    public static final int BULLET_BASIC_DAMAGE = 1;

    // Player properties
    public static final float PLAYER_FRAME_DURATION = 0.1f;

    // Enemies properties
    public static final int ENEMY_SHOOTER_WIDTH = 64;
    public static final int ENEMY_SHOOTER_HEIGHT = 64;
    public static final float ENEMY_SHOOTER_SPEED_BASE = 0.7f;
    public static final float ENEMY_SHOOTER_SPEED_VARIATION = 0.8f;
    public static final int ENEMY_SHOOTER_HEALTH = 3;
    public static final int ENEMY_SHOOTER_MINIMAL_DISTANCE_FROM_PLAYER = 5;
    public static final float ENEMY_SHOOTER_FRAME_DURATION = 0.2f;

    // Tiles properties
    public static final int TILE_WIDTH = 64;
    public static final int TILE_HEIGHT = 64;

    // Files properties
    public static final String ROOMS_DIRECTORY_PATH = "data/rooms";

    // Display screen properties
    public static final int SCREEN_WINDOWED_WIDTH = 800;
    public static final int SCREEN_WINDOWED_HEIGHT = 800;
    public static final int SCREEN_FULLSCREEN_WIDTH = 1920;
    public static final int SCREEN_FULLSCREEN_HEIGHT = 1080;

    // Camera properties
    public static float CAMERA_INITIAL_ZOOM = 5;

    private Constants() {
    }
}