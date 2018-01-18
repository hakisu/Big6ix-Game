package big6ix.game.bullets;

import big6ix.game.screens.GameMain;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public final class BulletCircular extends Bullet {

    public static final int WIDTH = 16;
    public static final int HEIGHT = 16;
    private static final float INITIAL_RADIUS = 20;
    private static final float RADIUS_INCREMENT_SPEED = 0.3f;
    private static final float SPEED = 2.5f;
    private static final int DAMAGE_POWER = 1;
    private static final String ATLAS_NAME = "bullet_hostile";

    private static TextureAtlas.AtlasRegion atlasRegion;

    static {
        atlasRegion = GameMain.getGameAtlas().findRegion(ATLAS_NAME);
    }

    private double sourceX;
    private double sourceY;
    private double radius;
    private double angle;

    public BulletCircular(float sourceX, float sourceY) {
        super(false, SPEED);
        this.width = WIDTH;
        this.height = HEIGHT;
        this.damagePower = DAMAGE_POWER;

        this.sourceX = sourceX;
        this.sourceY = sourceY;
        this.radius = INITIAL_RADIUS;
        this.angle = 0;

        // We execute 1 update in constructor to automatically chose initial bullet position
        this.update();
    }

    @Override
    public void update() {
        // Change in angle is the speed with which the bullet moves in a circle
        angle = angle + Math.toRadians(speed);
        this.x = (float) (sourceX + Math.sin(angle) * radius);
        this.y = (float) (sourceY + Math.cos(angle) * radius);

        radius += RADIUS_INCREMENT_SPEED;
    }

    @Override
    public TextureAtlas.AtlasRegion getAtlasRegion() {
        return atlasRegion;
    }
}