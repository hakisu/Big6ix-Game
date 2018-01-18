package big6ix.game.enemies;

import big6ix.game.Player;
import big6ix.game.bullets.ManagerBullets;
import big6ix.game.map.Map;
import big6ix.game.screens.GameMain;
import big6ix.game.screens.ScreenGame;
import big6ix.game.utility.Utilities;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public final class EnemyCreator extends Enemy {

    // Needs to be manually updated if we add new enemy type
    private static final int ENEMY_TYPES_FOR_CREATION_AMOUNT = 4;
    private static final float CREATE_ENEMY_TIME_IN_SECONDS = 3;
    // Health is set to arbitrary high value so that you can't destroy enemy creator before true enemy it is created
    private static final int HEALTH = 100000;
    private static final int WIDTH = 64;
    private static final int HEIGHT = 64;
    private static final String ATLAS_NAME = "enemy_creator";
    private static final float FRAME_DURATION = 0.1f;
    // After how many logical updates enemy is created
    private static final int ENEMY_CREATION_UPDATES_AMOUNT = (int) (CREATE_ENEMY_TIME_IN_SECONDS * ScreenGame.TICKS_PER_SECOND);

    private static Animation<TextureRegion> animation;

    static {
        animation = new Animation<>(
                FRAME_DURATION,
                GameMain.getGameAtlas().findRegions(ATLAS_NAME),
                Animation.PlayMode.LOOP_PINGPONG
        );
    }

    private float frameStateTime = 0;
    private int updatesTimer = 0;

    public EnemyCreator(float x, float y) {
        super(x, y);
        this.width = WIDTH;
        this.height = HEIGHT;
        this.health = HEALTH;
    }

    @Override
    public void update(Player player, ManagerEnemies managerEnemies, ManagerBullets managerBullets, Map map) {
        ++updatesTimer;
        if (updatesTimer >= ENEMY_CREATION_UPDATES_AMOUNT) {
            // Choose random enemy to create
            int enemyType = Utilities.generateRandomInt(0, ENEMY_TYPES_FOR_CREATION_AMOUNT - 1);
            if (enemyType == 0) {
                managerEnemies.addEnemy(new EnemyShooter(this.x, this.y));
            } else if (enemyType == 1) {
                managerEnemies.addEnemy(new EnemyBomber(this.x, this.y));
            } else if (enemyType == 2) {
                managerEnemies.addEnemy(new EnemyEnhancer(this.x, this.y));
            } else if (enemyType == 3) {
                managerEnemies.addEnemy(new EnemyTeleporter(this.x, this.y));
            }

            this.health = 0;
        }
    }

    @Override
    public TextureRegion getCurrentTextureRegion() {
        frameStateTime += Gdx.graphics.getDeltaTime();

        return animation.getKeyFrame(frameStateTime, true);
    }
}
