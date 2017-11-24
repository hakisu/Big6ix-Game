package big6ix.game;

import big6ix.game.PathFinding.HeuristicDistance;
import big6ix.game.PathFinding.TilePath;
import com.badlogic.gdx.ai.pfa.indexed.IndexedAStarPathFinder;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public final class EnemyShooter extends Enemy {

    private static TextureAtlas.AtlasRegion atlasRegion;

    static {
        atlasRegion = GameMain.getGameAtlas().findRegion(Constants.ATLAS_ENEMY_NAME);
    }

    private int updatesTimer = 0;
    private int shootingIntervalInUpdates = 60;


    public EnemyShooter(float x, float y) {
        super(x, y);
        this.width = Constants.ENEMY_SHOOTER_WIDTH;
        this.height = Constants.ENEMY_SHOOTER_HEIGHT;
        this.speed = Constants.ENEMY_SHOOTER_SPEED;
    }

    private void shoot(Player player, ManagerBullets managerBullets) {
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

        TilePath tilePath = new TilePath();
        IndexedAStarPathFinder<Tile> pathFinder = new IndexedAStarPathFinder<>(map);
        pathFinder.searchNodePath(
                map.getMapArray()[(int) this.y / map.getTileHeight()][(int) this.x / map.getTileWidth()],
                map.getMapArray()[(int) player.getY() / map.getTileHeight()][(int) player.getX() / map.getTileWidth()],
                new HeuristicDistance(),
                tilePath
        );

        if (tilePath.getCount() != 0) {
            int indexToReach = tilePath.getCount() >= 2 ? 1 : 0;
            if (this.x < tilePath.get(indexToReach).calculatePosX()) {
                this.x += this.speed;
            } else if (this.x > tilePath.get(indexToReach).calculatePosX()) {
                this.x -= this.speed;
            }
            if (this.y < tilePath.get(indexToReach).calculatePosY()) {
                this.y += this.speed;
            } else if (this.y > tilePath.get(indexToReach).calculatePosY()) {
                this.y -= this.speed;
            }
        }
    }

    @Override
    public TextureAtlas.AtlasRegion getAtlasRegion() {
        return atlasRegion;
    }
}