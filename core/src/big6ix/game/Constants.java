package big6ix.game;

public final class Constants {
    // AtlasRegion names
    public static final String ATLAS_BULLET_BASIC_NAME = "bullet";
    public static final String ATLAS_ENEMY_NAME = "enemy";
    public static final String ATLAS_PLAYER_NAME = "player";
    public static final String ATLAS_WALL_NAME = "wall";
    public static final String ATLAS_FLOOR_NAME = "floor";

    // Values responisble for game performance
    public static final int INITIAL_BULLETS_CAPACITY = 10000;
    public static final int INITIAL_ENEMIES_CAPACITY = 1000;

    // A single batch can draw no more than 8191 sprites. Otherwise it will throw IllegalArgumentException
    public static final int BATCH_MAX_NUMBER_OF_SPRITES = 2000;

    // Cursor properties
    public static final int CURSOR_X_HOTSPOT = 16;
    public static final int CURSOR_Y_HOTSPOT = 16;

    // Tiles properties
    public static final int TILE_WIDTH = 64;
    public static final int TILE_HEIGHT = 64;

    // Bullets properties
    public static final float BULLET_BASIC_SPEED = 17;
    public static final int BULLET_BASIC_WIDTH = 32;
    public static final int BULLET_BASIC_HEIGHT = 32;

    // Player properties
    public static final float PLAYER_SPEED = 6;
    public static final int PLAYER_WIDTH = 64;
    public static final int PLAYER_HEIGHT = 64;
    public static final float PLAYER_STARTING_X = 900;
    public static final float PLAYER_STARTING_Y = 500;

    // Enemies properties
    public static final int ENEMY_SHOOTER_WIDTH = 64;
    public static final int ENEMY_SHOOTER_HEIGHT = 64;

    private Constants() {
    }
}