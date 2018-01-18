package big6ix.game.bullets;

import big6ix.game.enemies.ManagerEnemies;
import big6ix.game.Player;
import big6ix.game.enemies.Enemy;
import big6ix.game.map.Map;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;

import java.util.Iterator;

public class ManagerBullets {

    private static final int INITIAL_BULLETS_CAPACITY = 10000;

    // References to objects needed by ManagerBullets
    private Map map;
    private Player player;
    private ManagerEnemies managerEnemies;

    private Array<Bullet> bullets;

    public ManagerBullets(Player player, Map map) {
        this.bullets = new Array<>(false, INITIAL_BULLETS_CAPACITY);
        this.map = map;
        this.player = player;
    }

    public void setManagerEnemies(ManagerEnemies managerEnemies) {
        this.managerEnemies = managerEnemies;
    }

    public void update() {
        Bullet currentBullet;
        Iterator<Bullet> bulletsIterator = bullets.iterator();

        while (bulletsIterator.hasNext()) {
            currentBullet = bulletsIterator.next();
            boolean bulletStillExists = true;

            if (checkTerrainCollision(currentBullet)) {
                bulletsIterator.remove();
                bulletStillExists = false;
            }
            // Performs action after bullet hits player
            if (bulletStillExists && checkPlayerCollision(currentBullet)) {
                player.receiveDamage(currentBullet.getDamagePower());
                bulletsIterator.remove();
                bulletStillExists = false;
            }
            // Performs action after bullet hits enemy
            if (bulletStillExists && checkEnemyCollision(currentBullet)) {
                bulletsIterator.remove();
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
        if (bullet.getX() >= 0 && bullet.getX() <= Map.TILE_WIDTH * map.getMapArray()[0].length
                && bullet.getY() >= 0 && bullet.getY() <= Map.TILE_HEIGHT * map.getMapArray().length) {
            if (!map.getMapArray()[(int) bullet.getY() / Map.TILE_HEIGHT][(int) bullet.getX() / Map.TILE_WIDTH].isWalkable()) {
                return true;
            }
        }
        return false;
    }

    // Used only for friendly bullets
    private boolean checkEnemyCollision(Bullet bullet) {
        if (bullet.isFriendly()) {
            Enemy currentEnemy;
            Iterator<Enemy> enemiesIterator = managerEnemies.getCurrentIteratorForEnemiesArray();
            while (enemiesIterator.hasNext()) {
                currentEnemy = enemiesIterator.next();

                float distanceX = (bullet.getX() + bullet.getWidth() / 2) - (currentEnemy.getX() + currentEnemy.getWidth() / 2);
                float distanceY = (bullet.getY() + bullet.getHeight() / 2) - (currentEnemy.getY() + currentEnemy.getHeight() / 2);
                float distance = (float) Math.sqrt(distanceX * distanceX + distanceY * distanceY);
                // Check if there is collision between bullet and specific enemy
                if (distance <= (bullet.getWidth() / 2 + currentEnemy.getWidth() / 2)) {
                    currentEnemy.receiveDamage(bullet.getDamagePower());
                    return true;
                }
            }
        }
        return false;
    }

    // Used only for hostile (enemy) bullets
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