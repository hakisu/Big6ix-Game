package big6ix.game.enemies;

import big6ix.game.GameMain;
import big6ix.game.ManagerBullets;
import big6ix.game.Player;
import big6ix.game.Tile;
import big6ix.game.bullets.Bullet;
import big6ix.game.bullets.BulletBasic;
import big6ix.game.map.Map;
import big6ix.game.pathfinding.HeuristicDistance;
import big6ix.game.pathfinding.TilePath;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.Random;

public final class EnemyShooter extends Enemy {

    private static final int HEALTH = 4;
    private static final float SPEED_BASE = 0.7f;
    private static final float SPEED_VARIATION = 0.8f;
    private static final int MINIMAL_DISTANCE_FROM_PLAYER = 5;
    private static final int WIDTH = 64;
    private static final int HEIGHT = 64;
    private static final String ATLAS_NAME = "enemy_shooter";
    private static final float FRAME_DURATION = 0.2f;

    private static Animation<TextureRegion> animation;
    private static Sound shootingSound;

    static {
        animation = new Animation<>(
                FRAME_DURATION,
                GameMain.getGameAtlas().findRegions(ATLAS_NAME),
                Animation.PlayMode.LOOP
        );
        shootingSound = Gdx.audio.newSound(Gdx.files.internal("sounds/shoot.wav"));
    }

    private TextureRegion currentFrame;
    private float frameStateTime = 0;
    private int updatesTimer = 0;
    private int shootingIntervalInUpdates = 60;
    private TilePath tilePath;
    private boolean inMovementBetweenTiles = false;

    public EnemyShooter(float x, float y) {
        super(x, y);
        Random random = new Random();
        this.width = WIDTH;
        this.height = HEIGHT;
        this.speed = random.nextFloat() * SPEED_VARIATION + SPEED_BASE;
        this.health = HEALTH;
        tilePath = new TilePath();
    }

    private void shoot(Player player, ManagerBullets managerBullets) {
        shootingSound.play(0.15f);
        Bullet bulletBasic = new BulletBasic(
                false,
                this.x + 32, this.y + 32,
                player.getX() + 32, player.getY() + 32
        );
        managerBullets.addBullet(bulletBasic);
    }

    @Override
    public void update(Player player, ManagerBullets managerBullets, Map map) {
        ++updatesTimer;
        if (updatesTimer == shootingIntervalInUpdates) {
            shoot(player, managerBullets);
            updatesTimer = 0;
        }

        int startTileIndexY = (int) (this.y + this.height / 2) / Map.TILE_HEIGHT;
        int startTileIndexX = (int) (this.x + this.width / 2) / Map.TILE_WIDTH;
        int endTileIndexY = (int) (player.getY() + player.getHeight() / 2) / Map.TILE_HEIGHT;
        int endTileIndexX = (int) (player.getX() + player.getWidth() / 2) / Map.TILE_WIDTH;
        Tile startTile = map.getMapArray()[startTileIndexY][startTileIndexX];
        Tile endTile = map.getMapArray()[endTileIndexY][endTileIndexX];

        if (inMovementBetweenTiles == false) {
            // Find new path for this entity and hold it in tilePath
            tilePath.clear();
            boolean pathFound = map.searchPath(startTile, endTile, new HeuristicDistance(map), tilePath);

            // If there is no path from enemy to player location finish this update
            if (pathFound == false) {
                return;
            } else {
                inMovementBetweenTiles = true;
            }
        }

//        float distance = (float) Math.sqrt(Math.pow(player.getX() - this.x, 2) + Math.pow(player.getY() - this.y, 2));
        if (new HeuristicDistance(map).estimate(startTile, endTile) > MINIMAL_DISTANCE_FROM_PLAYER) {
            boolean reachedPositionX = false, reachedPositionY = false;
            int indexToReach = tilePath.getCount() > 1 ? 1 : 0;
            Tile nextStepTile = tilePath.get(indexToReach);
            if (this.x + this.speed < nextStepTile.calculatePosX(map)) {
                this.x += this.speed;
            } else if (this.x - this.speed > nextStepTile.calculatePosX(map)) {
                this.x -= this.speed;
            } else {
                this.x = nextStepTile.calculatePosX(map);
                reachedPositionX = true;
            }
            if (this.y + this.speed < nextStepTile.calculatePosY(map)) {
                this.y += this.speed;
            } else if (this.y - this.speed > nextStepTile.calculatePosY(map)) {
                this.y -= this.speed;
            } else {
                this.y = nextStepTile.calculatePosY(map);
                reachedPositionY = true;
            }
            if (reachedPositionX && reachedPositionY) {
                inMovementBetweenTiles = false;
            }
        }
    }

    @Override
    public TextureRegion getCurrentTextureRegion() {
        frameStateTime += Gdx.graphics.getDeltaTime();
        currentFrame = animation.getKeyFrame(frameStateTime, true);

        return currentFrame;
    }
}