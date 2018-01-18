package big6ix.game.enemies;

import big6ix.game.bullets.ManagerBullets;
import big6ix.game.Player;
import big6ix.game.Tile;
import big6ix.game.map.Map;
import big6ix.game.pathfinding.HeuristicDistance;
import big6ix.game.pathfinding.TilePath;
import big6ix.game.screens.GameMain;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

import java.util.Random;

public final class EnemyBomber extends Enemy {

    private static final int DAMAGE = 2;
    private static final int HEALTH = 2;
    private static final float SPEED_BASE = 1.0f;
    private static final float SPEED_VARIATION = 3f;
    private static final int WIDTH = 64;
    private static final int HEIGHT = 64;
    private static final String ATLAS_NAME = "enemy_bomber";
    private static final float FRAME_DURATION = 0.2f;

    private static Animation<TextureRegion> animation;

    static {
        animation = new Animation<>(
                FRAME_DURATION,
                GameMain.getGameAtlas().findRegions(ATLAS_NAME),
                Animation.PlayMode.LOOP
        );
    }

    private float frameStateTime = 0;
    private TilePath tilePath;
    private boolean inMovementBetweenTiles = false;

    public EnemyBomber(float x, float y) {
        super(x, y);
        Random random = new Random();
        this.width = WIDTH;
        this.height = HEIGHT;
        this.speed = random.nextFloat() * (SPEED_VARIATION) + SPEED_BASE;
        this.health = HEALTH;
        tilePath = new TilePath();
    }


    private void explode(Player player) {
        player.receiveDamage(DAMAGE);
        this.health = 0;
    }

    @Override
    public void update(Player player, ManagerEnemies managerEnemies, ManagerBullets managerBullets, Map map) {
        if (player.getHitBox().overlaps(new Rectangle(this.x, this.y, this.width, this.height))) {
            explode(player);
        }

        int startTileIndexY = (int) (this.y + this.height / 2) / Map.TILE_HEIGHT;
        int startTileIndexX = (int) (this.x + this.width / 2) / Map.TILE_WIDTH;
        int endTileIndexY = (int) (player.getY() + player.getHeight() / 2) / Map.TILE_HEIGHT;
        int endTileIndexX = (int) (player.getX() + player.getWidth() / 2) / Map.TILE_WIDTH;
        Tile startTile = map.getMapArray()[startTileIndexY][startTileIndexX];
        Tile endTile = map.getMapArray()[endTileIndexY][endTileIndexX];

        if (!inMovementBetweenTiles) {
            // Find new path for this entity and hold it in tilePath
            tilePath.clear();
            boolean pathFound = map.searchPath(startTile, endTile, new HeuristicDistance(map), tilePath);

            // If there is no path from enemy to player location finish this update
            if (!pathFound) {
                return;
            } else {
                inMovementBetweenTiles = true;
            }
        }
        boolean reachedPositionX = false, reachedPositionY = false;
        int indexToReach = tilePath.getCount() > 1 ? 1 : 0;
        Tile nextStepTile = tilePath.get(indexToReach);
        if (this.x + this.speed < nextStepTile.calculatePosX(map)) {
            this.x += this.speed;
            // this.randomizedMovementY();
        } else if (this.x - this.speed > nextStepTile.calculatePosX(map)) {
            this.x -= this.speed;
            // this.randomizedMovementY();
        } else {
            this.x = nextStepTile.calculatePosX(map);
            reachedPositionX = true;
        }
        if (this.y + this.speed < nextStepTile.calculatePosY(map)) {
            this.y += this.speed;
            //  this.randomizedMovementX();
        } else if (this.y - this.speed > nextStepTile.calculatePosY(map)) {
            this.y -= this.speed;
            //  this.randomizedMovementX();
        } else {
            this.y = nextStepTile.calculatePosY(map);
            reachedPositionY = true;
        }
        if (reachedPositionX && reachedPositionY) {
            inMovementBetweenTiles = false;
        }
    }

    @Override
    public TextureRegion getCurrentTextureRegion() {
        frameStateTime += Gdx.graphics.getDeltaTime();

        return animation.getKeyFrame(frameStateTime, true);
    }
}