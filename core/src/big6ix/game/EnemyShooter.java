package big6ix.game;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public final class EnemyShooter extends Enemy {
    private int updatesTimer = 0;
    private int shootingIntervalInUpdates = 60;
    private static TextureAtlas.AtlasRegion atlasRegion;

    static {
        atlasRegion = GameMain.gameAtlas.findRegion(Constants.ATLAS_ENEMY_NAME);
    }


    public EnemyShooter(float x, float y) {
        super(x, y);
        this.width = Constants.ENEMY_SHOOTER_WIDTH;
        this.height = Constants.ENEMY_SHOOTER_HEIGHT;
    }

    private void shoot(Player player, ManagerBullets managerBullets) {
        Bullet bulletBasic = new BulletBasic(false, Constants.BULLET_BASIC_SPEED, this.x + 32, this.y + 32, player.getX() + 32, player.getY() + 32);
        managerBullets.addBullet(bulletBasic);
    }

    @Override
    public void update(Player player, ManagerBullets managerBullets) {
        ++updatesTimer;
        if (updatesTimer == shootingIntervalInUpdates) {
            shoot(player, managerBullets);
            updatesTimer = 0;
        }
    }

    @Override
    public TextureAtlas.AtlasRegion getAtlasRegion() {
        return atlasRegion;
    }
}
