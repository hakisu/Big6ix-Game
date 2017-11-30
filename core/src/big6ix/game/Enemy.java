package big6ix.game;

import big6ix.game.Map.Map;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public abstract class Enemy {

    protected float x;
    protected float y;
    protected int width;
    protected int height;
    protected float speed;
    protected int health;

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

    public int getHealth() {

        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public abstract void update(Player player, ManagerBullets managerBullets, Map map);

    public abstract TextureRegion getCurrentTextureRegion();
}