package big6ix.game.enemies;

import big6ix.game.ManagerBullets;
import big6ix.game.ManagerEnemies;
import big6ix.game.Player;
import big6ix.game.map.Map;
import big6ix.game.screens.GameMain;
import big6ix.game.screens.ScreenGame;
import big6ix.game.utility.Pair;
import big6ix.game.utility.Utilities;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public final class EnemyCreator extends Enemy {

    private static final float CREATE_ENEMY_TIME_IN_SECONDS = 3;
    // Health is set to arbitrary high value so that you can't destroy enemy creator before true enemy it is created
    private static final int HEALTH = 100000;
    private static final int WIDTH = 64;
    private static final int HEIGHT = 64;
    private static final String ATLAS_NAME = "enemy_creator";
    private static final float FRAME_DURATION = 0.5f;

    private static Animation<TextureRegion> animation;

    static {
        animation = new Animation<>(
                FRAME_DURATION,
                GameMain.getGameAtlas().findRegions(ATLAS_NAME),
                Animation.PlayMode.LOOP
        );
    }

    private float frameStateTime = 0;
    private int updatesTimer = 0;
    // After how many logical updates enemy is created
    private int enemyCreationUpdatesAmount = (int) (CREATE_ENEMY_TIME_IN_SECONDS * ScreenGame.TICKS_PER_SECOND);

    public EnemyCreator(float x, float y) {
        super(x, y);
        this.width = WIDTH;
        this.height = HEIGHT;
        this.health = HEALTH;
    }

    @Override
    public void update(Player player, ManagerEnemies managerEnemies, ManagerBullets managerBullets, Map map) {
        ++updatesTimer;
        if (updatesTimer >= enemyCreationUpdatesAmount) {
            // Choose random enemy to create
            int enemyType = Utilities.generateRandomInt(0, 1);
            if (enemyType == 0) {
                managerEnemies.addEnemy(new EnemyShooter(this.x, this.y));
            } else if (enemyType == 1) {
                managerEnemies.addEnemy(new EnemyBomber(this.x, this.y));
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
