package big6ix.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;

import java.util.Iterator;

public class ManagerBullets {
    private Array<Bullet> bullets = null;
    private Iterator<Bullet> bulletsIterator = null;

    // References to objects needed by ManagerBullets
    private final Map map;
    private final Player player;

    public ManagerBullets(Player player, Map map) {
        this.bullets = new Array<Bullet>(false, Constants.INITIAL_BULLETS_CAPACITY);
        this.map = map;
        this.player = player;
    }

    public void update() {
        Bullet currentBullet;
        bulletsIterator = bullets.iterator();
        while (bulletsIterator.hasNext()) {
            currentBullet = bulletsIterator.next();

            if (checkTerrainCollision(currentBullet)) {
                bulletsIterator.remove();
            }
            if (checkPlayerCollision(currentBullet)) {
                System.out.println("Player hit!");
                bulletsIterator.remove();
            }
            if (checkEnemyCollision(currentBullet)) {

            }
            currentBullet.update();
        }
    }

    public void render(SpriteBatch batch) {
        for (Bullet currentBullet : bullets) {
            batch.draw(
                    currentBullet.getAtlasRegion(), currentBullet.getX(), currentBullet.getY(),
                    currentBullet.getWidth(), currentBullet.getHeight()
            );
        }
    }

    public void addBullet(Bullet bullet) {
        this.bullets.add(bullet);
    }

    private boolean checkTerrainCollision(Bullet bullet) {
        // Add conditions depending on current bullet speedX and speedY
        if (map.getMapArray()[(int) bullet.getY() / map.getTileHeight()][(int) bullet.getX() / map.getTileWidth()] == 1) {
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
        if (!bullet.isFriendly()) {
            float distanceX = (bullet.getX() + bullet.getWidth() / 2) - (player.getX() + player.getWidth() / 2);
            float distanceY = (bullet.getY() + bullet.getHeight() / 2) - (player.getY() + player.getHeight() / 2);
            float distance = (float) Math.sqrt(distanceX * distanceX + distanceY * distanceY);
            if (distance <= (bullet.getWidth() / 2 + player.getWidth() / 2)) {
                return true;
            }
        }
        return false;
    }
}