package big6ix.game.bullets;

import big6ix.game.screens.GameMain;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public final class BulletBasic extends Bullet {

    public static final int WIDTH = 16;
    public static final int HEIGHT = 16;
    private static final int SPEED = 13;
    private static final int DAMAGE_POWER = 1;
    private static final String FRIENDLY_ATLAS_NAME = "bullet_friendly";
    private static final String HOSTILE_ATLAS_NAME = "bullet_hostile";

    private static TextureAtlas.AtlasRegion atlasRegionForFriendly;
    private static TextureAtlas.AtlasRegion atlasRegionForHostile;

    static {
        atlasRegionForFriendly = GameMain.getGameAtlas().findRegion(FRIENDLY_ATLAS_NAME);
        atlasRegionForHostile = GameMain.getGameAtlas().findRegion(HOSTILE_ATLAS_NAME);
    }

    public BulletBasic(boolean friendly, float sourceX, float sourceY, float targetX, float targetY) {
        super(friendly, SPEED);
        this.width = WIDTH;
        this.height = HEIGHT;
        this.damagePower = DAMAGE_POWER;

        // Calculations to receive speed of bullet in X and Y axis
        float distance = (float) Math.sqrt((targetX - sourceX) * (targetX - sourceX) + (targetY - sourceY) * (targetY - sourceY));
        this.speedX = (targetX - sourceX) / distance * speed;
        this.speedY = (targetY - sourceY) / distance * speed;

        // Setting starting position of bullet
        this.x = sourceX;
        this.y = sourceY;
    }

    @Override
    public TextureAtlas.AtlasRegion getAtlasRegion() {
        if (this.isFriendly()) {
            return atlasRegionForFriendly;
        } else {
            return atlasRegionForHostile;
        }
    }

    @Override
    public void update() {
        this.x += speedX;
        this.y += speedY;
    }
}