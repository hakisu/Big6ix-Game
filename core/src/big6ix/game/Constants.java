package big6ix.game;

public final class Constants {
    // AtlasRegion names
    public static final String BULLET_NAME = "bullet";
    public static final String ENEMY_NAME = "enemy";
    public static final String PLAYER_NAME = "player";
    public static final String WALL_NAME = "wall";
    public static final String FLOOR_NAME = "floor";

    public static final int INITIAL_BULLETS_CAPACITY = 10000;

    // A single batch can draw no more than 8191 sprites. Otherwise it will throw IllegalArgumentException
    public static final int BATCH_MAX_NUMBER_OF_SPRITES = 100;

    // Bullets properties
    public static final float BULLET_BASIC_SPEED = 14;

    private Constants() {
    }
}