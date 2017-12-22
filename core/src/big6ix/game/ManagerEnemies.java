package big6ix.game;

import big6ix.game.enemies.Enemy;
import big6ix.game.map.Map;
import com.badlogic.gdx.ai.pfa.indexed.IndexedAStarPathFinder;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;

import java.util.Iterator;

public class ManagerEnemies {

    private static final int INITIAL_ENEMIES_CAPACITY = 1000;
    
    // References to objects needed by ManagerEnemies
    private Player player;
    private ManagerBullets managerBullets;
    private Map map;

    private Array<Enemy> enemies;
    private Iterator<Enemy> enemiesIterator;
    private IndexedAStarPathFinder<Tile> pathFinder;

    public ManagerEnemies(Player player, ManagerBullets managerBullets, Map map) {
        this.enemies = new Array<Enemy>(false, INITIAL_ENEMIES_CAPACITY);
        this.player = player;
        this.managerBullets = managerBullets;
        this.map = map;
        this.pathFinder = new IndexedAStarPathFinder(this.map);
    }

    public void update() {
        Enemy currentEnemy;
        enemiesIterator = enemies.iterator();
        while (enemiesIterator.hasNext()) {
            currentEnemy = enemiesIterator.next();
            currentEnemy.update(player, managerBullets, map);
            removeDeadEnemy(currentEnemy);
        }
    }

    public void render(SpriteBatch batch) {
        for (Enemy currentEnemy : enemies) {
            batch.draw(
                    currentEnemy.getCurrentTextureRegion(),
                    currentEnemy.getX(), currentEnemy.getY(),
                    currentEnemy.getWidth(), currentEnemy.getHeight()
            );
        }
    }

    public void addEnemy(Enemy enemy) {
        this.enemies.add(enemy);
    }

    private void removeDeadEnemy(Enemy enemy) {
        if (enemy.getHealth() <= 0) {
            enemiesIterator.remove();
        }
    }

    public Iterator<Enemy> getCurrentIteratorForEnemiesArray() {
        return enemies.iterator();
    }

    public int getNumberOfAllEnemies() {
        return enemies.size;
    }
}
