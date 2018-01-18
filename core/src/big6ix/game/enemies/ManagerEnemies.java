package big6ix.game.enemies;

import big6ix.game.Player;
import big6ix.game.bullets.ManagerBullets;
import big6ix.game.map.Map;
import big6ix.game.screens.GameMain;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Array;

import java.util.Iterator;

public class ManagerEnemies {

    private static final int INITIAL_ENEMIES_CAPACITY = 1000;
    private static final String ATLAS_IMMORTAL_NAME = "immortal";
    private static final int IMMORTAL_WIDTH = 64;
    private static final int IMMORTAL_HEIGHT = 32;

    // References to objects needed by ManagerEnemies
    private Player player;
    private ManagerBullets managerBullets;
    private Map map;

    private TextureAtlas.AtlasRegion immortalAtlasRegion;
    private Array<Enemy> enemies;
    private Iterator<Enemy> enemiesIterator;

    public ManagerEnemies(Player player, ManagerBullets managerBullets, Map map) {
        this.immortalAtlasRegion = GameMain.getGameAtlas().findRegion(ATLAS_IMMORTAL_NAME);

        this.enemies = new Array<>(false, INITIAL_ENEMIES_CAPACITY);
        this.player = player;
        this.managerBullets = managerBullets;
        this.map = map;
    }

    public void update() {
        Enemy currentEnemy;
        enemiesIterator = enemies.iterator();
        while (enemiesIterator.hasNext()) {
            currentEnemy = enemiesIterator.next();
            currentEnemy.update(player, this, managerBullets, map);
            removeDeadEnemy(currentEnemy);
        }
    }

    public void render(SpriteBatch batch) {
        for (Enemy currentEnemy : enemies) {
            // Draw aureole above enemy if it is immortal
            if (currentEnemy.isImmortal()) {
                batch.draw(
                        immortalAtlasRegion,
                        currentEnemy.getX(), currentEnemy.getY() - IMMORTAL_HEIGHT,
                        IMMORTAL_WIDTH, IMMORTAL_HEIGHT
                );
            }
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
            enemy.executeOnDeath();
            enemiesIterator.remove();
        }
    }

    public Iterator<Enemy> getCurrentIteratorForEnemiesArray() {
        return enemies.iterator();
    }

    public int getNumberOfAllEnemies() {
        return enemies.size;
    }

    Array<Enemy> getEnemiesArray() {
        return enemies;
    }
}