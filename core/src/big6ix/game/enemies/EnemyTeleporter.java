package big6ix.game.enemies;

import big6ix.game.Player;
import big6ix.game.bullets.Bullet;
import big6ix.game.bullets.BulletCircular;
import big6ix.game.bullets.ManagerBullets;
import big6ix.game.map.Map;
import big6ix.game.map.Room;
import big6ix.game.screens.GameMain;
import big6ix.game.screens.ScreenGame;
import big6ix.game.utility.Pair;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public final class EnemyTeleporter extends Enemy {

    private static final int HEALTH = 3;
    private static final int WIDTH = 64;
    private static final int HEIGHT = 64;
    private static final String ATLAS_NAME = "enemy_teleporter";
    private static final float FRAME_DURATION = 0.4f;
    private static final float TELEPORTATION_INTERVAL_IN_SECONDS = 5;
    private static final int TELEPORTATION_UPDATES_AMOUNT = (int) (TELEPORTATION_INTERVAL_IN_SECONDS * ScreenGame.TICKS_PER_SECOND);
    private static final float SHOOTING_INTERVAL_IN_SECONDS = 0.5f;
    private static final int SHOOTING_INTERVAL_IN_UPDATES = (int) (SHOOTING_INTERVAL_IN_SECONDS * ScreenGame.TICKS_PER_SECOND);

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

    public EnemyTeleporter(float x, float y) {
        super(x, y);
        this.health = HEALTH;
        this.width = WIDTH;
        this.height = HEIGHT;
    }

    private void shoot(ManagerBullets managerBullets) {
        Bullet bulletCircular = new BulletCircular(
                this.x + this.width / 2 - BulletCircular.WIDTH / 2,
                this.y + this.height / 2 - BulletCircular.HEIGHT / 2
        );
        managerBullets.addBullet(bulletCircular);
    }

    @Override
    public void update(Player player, ManagerEnemies managerEnemies, ManagerBullets managerBullets, Map map) {
        if (updatesTimer % SHOOTING_INTERVAL_IN_UPDATES == 0) {
            shoot(managerBullets);
        }
        ++updatesTimer;
        if (updatesTimer >= TELEPORTATION_UPDATES_AMOUNT) {
            Room currentRoom = map.getCurrentOccupiedRoom();
            Pair newPositionIndices = currentRoom.getRandomWalkableTileIndices();
            this.x = newPositionIndices.getIndexX() * Map.TILE_WIDTH;
            this.y = newPositionIndices.getIndexY() * Map.TILE_HEIGHT;

            this.updatesTimer = 0;
        }
    }

    @Override
    public TextureRegion getCurrentTextureRegion() {
        frameStateTime += Gdx.graphics.getDeltaTime();
        return animation.getKeyFrame(frameStateTime, true);
    }
}
