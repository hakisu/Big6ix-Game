package big6ix.game;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public final class BulletBasic extends Bullet {

    private static final int SPEED = 17;
    private static final int DAMAGE_POWER = 1;
    private static final int WIDTH = 16;
    private static final int HEIGHT = 16;

    private static TextureAtlas.AtlasRegion atlasRegion;
    static {
        atlasRegion = GameMain.getGameAtlas().findRegion(Constants.ATLAS_BULLET_BASIC_NAME);
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
        return atlasRegion;
    }

    @Override
    public void update() {
        this.x += speedX;
        this.y += speedY;
    }
}