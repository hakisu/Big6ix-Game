package big6ix.game;

import big6ix.game.Map.Map;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;

import java.util.Iterator;

public class ManagerBullets {

    // References to objects needed by ManagerBullets
    private Map map;
    private Player player;
    private ManagerEnemies managerEnemies;

    private Array<Bullet> bullets;
    private Iterator<Bullet> bulletsIterator;

    public ManagerBullets(Player player, Map map) {
        this.bullets = new Array<Bullet>(false, Constants.INITIAL_BULLETS_CAPACITY);
        this.map = map;
        this.player = player;
    }

    public void setManagerEnemies(ManagerEnemies managerEnemies) {
        this.managerEnemies = managerEnemies;
    }

    public void update() {
        Bullet currentBullet;
        bulletsIterator = bullets.iterator();
        while (bulletsIterator.hasNext()) {
            currentBullet = bulletsIterator.next();
            boolean bulletStillExists = true;

            if (bulletStillExists && checkTerrainCollision(currentBullet)) {
                bulletsIterator.remove();
                bulletStillExists = false;
            }
            if (bulletStillExists && checkPlayerCollision(currentBullet)) {
                System.out.println("Player hit!");
                bulletsIterator.remove();
                bulletStillExists = false;
            }
            if (bulletStillExists && checkEnemyCollision(currentBullet)) {
                bulletsIterator.remove();
                bulletStillExists = false;
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
        if (bullet.getX() >= 0 && bullet.getX() <= map.getTileWidth() * map.getMapArray()[0].length
                && bullet.getY() >= 0 && bullet.getY() <= map.getTileHeight() * map.getMapArray().length) {
            if (!map.getMapArray()[(int) bullet.getY() / map.getTileHeight()][(int) bullet.getX() / map.getTileWidth()].isWalkable()) {
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
                    currentEnemy.setHealth(currentEnemy.getHealth() - bullet.damage);
                    if (currentEnemy.getHealth() <= 0) {
                        enemiesIterator.remove();
                    }
                    return true;
                }
            }
        }
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