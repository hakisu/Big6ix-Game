package big6ix.game.enemies;

import big6ix.game.Player;
import big6ix.game.bullets.ManagerBullets;
import big6ix.game.map.Map;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public abstract class Enemy {

    protected float x;
    protected float y;
    protected int width;
    protected int height;
    protected float speed;
    protected int health;
    protected boolean immortal;

    public Enemy(float x, float y) {
        this.x = x;
        this.y = y;
        this.immortal = false;
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

    public int getHealth() {
        return health;
    }

    public boolean isImmortal() {
        return immortal;
    }

    public void setImmortal(boolean immortal) {
        this.immortal = immortal;
    }

    public void receiveDamage(int damageValue) {
        if (!this.immortal) {
            this.health -= damageValue;
        }
    }

    public void executeOnDeath() {
    }

    public abstract void update(Player player, ManagerEnemies managerEnemies, ManagerBullets managerBullets, Map map);

    public abstract TextureRegion getCurrentTextureRegion();
}