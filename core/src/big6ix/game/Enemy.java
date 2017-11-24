package big6ix.game;

import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;

public abstract class Enemy {

    protected float x;
    protected float y;
    protected int width;
    protected int height;
    protected float speed;

    private int shootingIntervalInUpdates = 60;
    private int updatesTimer = 0;

    public Enemy(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public float getSpeed() {
        return speed;
    }

    public abstract void update(Player player, ManagerBullets managerBullets, Map map);

    public abstract AtlasRegion getAtlasRegion();
}