package big6ix.game;

import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;

public abstract class Bullet {
    protected float x;
    protected float y;
    protected float speedX;
    protected float speedY;
    protected float speed;
    private boolean friendly;

    public Bullet(boolean friendly) {
        this.friendly = friendly;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public abstract void update();

    public abstract AtlasRegion getAtlasRegion();
}