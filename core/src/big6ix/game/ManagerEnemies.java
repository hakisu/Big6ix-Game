package big6ix.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;

import java.util.Iterator;

public class ManagerEnemies {
    private Array<Enemy> enemies = null;
    private Iterator<Enemy> enemiesIterator = null;

    // References to objects needed by ManagerEnemies
    private final Player player;
    private final ManagerBullets managerBullets;

    public ManagerEnemies(Player player, ManagerBullets managerBullets) {
        this.enemies = new Array<Enemy>(false, Constants.INITIAL_ENEMIES_CAPACITY);
        this.player = player;
        this.managerBullets = managerBullets;
    }

    public void update() {
        Enemy currentEnemy;
        enemiesIterator = enemies.iterator();
        while (enemiesIterator.hasNext()) {
            currentEnemy = enemiesIterator.next();

            currentEnemy.update(player, managerBullets);
        }
    }

    public void render(SpriteBatch batch) {
        for (Enemy currentEnemy : enemies) {
            batch.draw(
                    currentEnemy.getAtlasRegion(), currentEnemy.getX(), currentEnemy.getY(),
                    currentEnemy.getWidth(), currentEnemy.getHeight()
            );
        }
    }

    public void addEnemy(Enemy enemy) {
        this.enemies.add(enemy);
    }
}
