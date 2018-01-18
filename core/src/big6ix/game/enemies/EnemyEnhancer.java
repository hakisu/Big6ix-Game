package big6ix.game.enemies;

import big6ix.game.Player;
import big6ix.game.bullets.ManagerBullets;
import big6ix.game.map.Map;
import big6ix.game.screens.GameMain;
import big6ix.game.utility.Utilities;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

public final class EnemyEnhancer extends Enemy {

    private static final int HEALTH = 5;
    private static final int WIDTH = 64;
    private static final int HEIGHT = 64;
    private static final String ATLAS_NAME = "enemy_enhancer";
    private static final float FRAME_DURATION = 0.8f;

    private static Animation<TextureRegion> animation;

    static {
        animation = new Animation<>(
                FRAME_DURATION,
                GameMain.getGameAtlas().findRegions(ATLAS_NAME),
                Animation.PlayMode.LOOP
        );
    }

    private float frameStateTime = 0;
    private Enemy chosenEnhancedEnemy;

    public EnemyEnhancer(float x, float y) {
        super(x, y);
        this.health = HEALTH;
        this.width = WIDTH;
        this.height = HEIGHT;
    }

    @Override
    public void executeOnDeath() {
        chosenEnhancedEnemy.setImmortal(false);
    }

    @Override
    public void receiveDamage(int damageValue) {
        this.health -= damageValue;
    }

    @Override
    public void update(Player player, ManagerEnemies managerEnemies, ManagerBullets managerBullets, Map map) {
        if (chosenEnhancedEnemy == null || chosenEnhancedEnemy.getHealth() <= 0) {
            Array<Enemy> enemies = managerEnemies.getEnemiesArray();
            chosenEnhancedEnemy = enemies.get(Utilities.generateRandomInt(0, enemies.size - 1));
        }
        chosenEnhancedEnemy.setImmortal(true);
    }

    @Override
    public TextureRegion getCurrentTextureRegion() {
        frameStateTime += Gdx.graphics.getDeltaTime();
        return animation.getKeyFrame(frameStateTime, true);
    }
}