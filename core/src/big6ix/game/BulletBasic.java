package big6ix.game;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public final class BulletBasic extends Bullet {
    private static TextureAtlas.AtlasRegion atlasRegion;

    static {
        atlasRegion = GameMain.getGameAtlas().findRegion(Constants.ATLAS_BULLET_BASIC_NAME);
    }

    public BulletBasic() {
    }

    public BulletBasic(boolean friendly, float speed, float sourceX, float sourceY, float targetX, float targetY) {
        super(friendly, speed);
        this.width = Constants.BULLET_BASIC_WIDTH;
        this.height = Constants.BULLET_BASIC_HEIGHT;
        this.damage = Constants.BULLET_BASIC_DAMAGE;

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

    // Will be needed later
    @Override
    public Bullet clone() {
        return new BulletBasic();
    }
}