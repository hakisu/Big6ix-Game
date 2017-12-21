package big6ix.game;

import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;

public abstract class Bullet {

    protected float x;
    protected float y;
    protected int width;
    protected int height;
    protected float speedX;
    protected float speedY;
    protected float speed;
    protected int damagePower;
    private boolean friendly;

    public Bullet(boolean friendly, float speed) {
        this.friendly = friendly;
        this.speed = speed;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public boolean isFriendly() {
        return friendly;
    }

    public int getDamagePower() {
        return damagePower;
    }

    public abstract void update();
    public abstract AtlasRegion getAtlasRegion();
}