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
    private boolean friendly;

    public Bullet() {
    }

    public Bullet(boolean friendly) {
        this.friendly = friendly;
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

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public boolean isFriendly() {
        return friendly;
    }

    public abstract void update();

    public abstract AtlasRegion getAtlasRegion();

    public abstract Bullet clone();
}