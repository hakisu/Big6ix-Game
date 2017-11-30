package big6ix.game;

import big6ix.game.Map.Map;
import big6ix.game.PathFinding.HeuristicDistance;
import big6ix.game.PathFinding.TilePath;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.Random;

public final class EnemyShooter extends Enemy {

    private static Animation<TextureRegion> animation;
    private static Sound shootingSound;

    static {
        animation = new Animation<TextureRegion>(
                Constants.ENEMY_SHOOTER_FRAME_DURATION,
                GameMain.getGameAtlas().findRegions(Constants.ATLAS_ENEMY_NAME),
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
        this.width = Constants.ENEMY_SHOOTER_WIDTH;
        this.height = Constants.ENEMY_SHOOTER_HEIGHT;
        this.speed = random.nextFloat() * (Constants.ENEMY_SHOOTER_SPEED_VARIATION) + Constants.ENEMY_SHOOTER_SPEED_BASE;
        this.health = Constants.ENEMY_SHOOTER_HEALTH;
        tilePath = new TilePath();
    }

    private void shoot(Player player, ManagerBullets managerBullets) {
        shootingSound.play(0.25f);
        Bullet bulletBasic = new BulletBasic(
                false, Constants.BULLET_BASIC_SPEED,
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

        int startTileIndexY = (int) (this.y + this.height / 2) / map.getTileHeight();
        int startTileIndexX = (int) (this.x + this.width / 2) / map.getTileWidth();
        int endTileIndexY = (int) (player.getY() + player.getHeight() / 2) / map.getTileHeight();
        int endTileIndexX = (int) (player.getX() + player.getWidth() / 2) / map.getTileWidth();
        Tile startTile = map.getMapArray()[startTileIndexY][startTileIndexX];
        Tile endTile = map.getMapArray()[endTileIndexY][endTileIndexX];

        if (inMovementBetweenTiles == false) {
            // Find new path for this entity and hold it in tilePath
            tilePath.clear();
            boolean pathFound = map.searchPath(startTile, endTile, new HeuristicDistance(), tilePath);

            // If there is no path from enemy to player location finish this update
            if (pathFound == false) {
                return;
            } else {
                inMovementBetweenTiles = true;
            }
        }

//        float distance = (float) Math.sqrt(Math.pow(player.getX() - this.x, 2) + Math.pow(player.getY() - this.y, 2));
//        if (new HeuristicDistance().estimate(startTile, endTile) > Constants.ENEMY_SHOOTER_MINIMAL_DISTANCE_FROM_PLAYER) {
        boolean reachedPositionX = false, reachedPositionY = false;
        int indexToReach = tilePath.getCount() > 1 ? 1 : 0;
        Tile nextStepTile = tilePath.get(indexToReach);
        if (this.x + this.speed < nextStepTile.calculatePosX()) {
            this.x += this.speed;
        } else if (this.x - this.speed > nextStepTile.calculatePosX()) {
            this.x -= this.speed;
        } else {
            this.x = nextStepTile.calculatePosX();
            reachedPositionX = true;
        }
        if (this.y + this.speed < nextStepTile.calculatePosY()) {
            this.y += this.speed;
        } else if (this.y - this.speed > nextStepTile.calculatePosY()) {
            this.y -= this.speed;
        } else {
            this.y = nextStepTile.calculatePosY();
            reachedPositionY = true;
        }
        if (reachedPositionX && reachedPositionY) {
            inMovementBetweenTiles = false;
        }
    }

    @Override
    public TextureRegion getCurrentTextureRegion() {
        frameStateTime += Gdx.graphics.getDeltaTime();
        currentFrame = animation.getKeyFrame(frameStateTime, true);

        return currentFrame;
    }
}