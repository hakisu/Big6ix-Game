package big6ix.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;

import java.util.Iterator;

public class ManagerBullets {
    private Array<Bullet> bullets = null;
    private Iterator<Bullet> bulletsIterator = null;

    // References to objects needed by ManagerBullets
    private final Map map;

    public ManagerBullets(Map map) {
        this.bullets = new Array<Bullet>(false, Constants.INITIAL_BULLETS_CAPACITY);
        this.map = map;
    }

    public void update() {
        Bullet currentBullet;
        bulletsIterator = bullets.iterator();
        while (bulletsIterator.hasNext()) {
            currentBullet = bulletsIterator.next();

            currentBullet.update();
            if (checkTerrainCollision(currentBullet)) {
                bulletsIterator.remove();
            }
        }
    }

    public void render(SpriteBatch batch) {
        for (Bullet currentBullet : bullets) {
            batch.draw(currentBullet.getAtlasRegion(), currentBullet.getX() - 16, currentBullet.getY() - 16, 32, 32);
        }
    }

    public void addBullet(Bullet bullet) {
        this.bullets.add(bullet);
    }

    private boolean checkTerrainCollision(Bullet bullet) {
        if (map.getMap()[(int) bullet.getX() / 64][(int) bullet.getY() / 64] == 1) {
            return true;
        }

        return false;
    }

    // Used only for friendly bullets
    private boolean checkEnemyCollision(Bullet bullet) {
        // do zrobienia
        return false;
    }

    // Used only for enemy (not friendly) bullets
    private boolean checkPlayerCollision(Bullet bullet) {
        // do zrobienia
        return false;
    }
}