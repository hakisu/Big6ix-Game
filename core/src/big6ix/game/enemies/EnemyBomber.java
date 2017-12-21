package big6ix.game;

import big6ix.game.Map.Map;
import big6ix.game.PathFinding.HeuristicDistance;
import big6ix.game.PathFinding.TilePath;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.Random;

public final class EnemyBomber extends Enemy{
    private static Animation<TextureRegion> animation;
    private static Sound explosionSound;
    private static final String name = "enemyBomber";
    public static final int ENEMY_BOMBER_WIDTH = 64;
    public static final int ENEMY_BOMBER_HEIGHT = 64;
    public static final float ENEMY_BOMBER_SPEED_BASE = 4.0f;
    public static final float ENEMY_BOMBER_SPEED_VARIATION = 0.8f;
    public static final int ENEMY_BOMBER_HEALTH = 1;
    public static final float ENEMY_BOMBER_FRAME_DURATION = 0.2f;
    public static final float ENEMY_BOMBER_EXPLOSION_WITHIN = 200.0f;

    static {
        animation = new Animation<TextureRegion>(
                ENEMY_BOMBER_FRAME_DURATION,
                GameMain.getGameAtlas().findRegions(name),
                Animation.PlayMode.LOOP
        );
        explosionSound = Gdx.audio.newSound(Gdx.files.internal("sounds/shoot.wav"));
    }
    private TextureRegion currentFrame;
    private float frameStateTime = 0;
    private TilePath tilePath;
    private boolean inMovementBetweenTiles = false;

    public EnemyBomber(float x, float y){
        super(x, y);
        Random random = new Random();
        this.width = ENEMY_BOMBER_WIDTH;
        this.height = ENEMY_BOMBER_HEIGHT;
        this.speed = random.nextFloat() * (ENEMY_BOMBER_SPEED_VARIATION) + ENEMY_BOMBER_SPEED_BASE;
        this.health = ENEMY_BOMBER_HEALTH;
        tilePath = new TilePath();
    }


    private void explode(Player player)
    {
        //explosionSound.play(0.25f);
        this.setHealth(0);


    }
    @Override
    public void update(Player player, ManagerBullets managerBullets, Map map){
        if (this.health <=0)
        {
            //explode();
            //death
        }

        if((player.getY() - this.y <= ENEMY_BOMBER_EXPLOSION_WITHIN
                && player.getY() - this.y >= -(ENEMY_BOMBER_EXPLOSION_WITHIN))
                &&(player.getX() - this.x <= ENEMY_BOMBER_EXPLOSION_WITHIN
                && player.getX() - this.x >= -(ENEMY_BOMBER_EXPLOSION_WITHIN))) {
            //System.out.println("Explosion!");
            explode(player);
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
            boolean reachedPositionX = false, reachedPositionY = false;
            int indexToReach = tilePath.getCount() > 1 ? 1 : 0;
            Tile nextStepTile = tilePath.get(indexToReach);
            if (this.x + this.speed < nextStepTile.calculatePosX()) {
                this.x += this.speed;
               // this.randomizedMovementY();
            } else if (this.x - this.speed > nextStepTile.calculatePosX()) {
                this.x -= this.speed;
               // this.randomizedMovementY();
            } else {
                this.x = nextStepTile.calculatePosX();
                reachedPositionX = true;
            }
            if (this.y + this.speed < nextStepTile.calculatePosY()) {
                this.y += this.speed;
              //  this.randomizedMovementX();
            } else if (this.y - this.speed > nextStepTile.calculatePosY()) {
                this.y -= this.speed;
              //  this.randomizedMovementX();
            } else {
                this.y = nextStepTile.calculatePosY();
                reachedPositionY = true;
            }
            if (reachedPositionX && reachedPositionY) {
                inMovementBetweenTiles = false;
            }
        }

        //Bad attempt of modifying movement
    public void randomizedMovementY()
    {
        Random rand = new Random();
        int n = rand.nextInt(2) + 1;
        switch(n)
        {
            case 1:
                this.y += this.speed+ENEMY_BOMBER_SPEED_BASE*2;
            case 2:
                this.y -= this.speed+ENEMY_BOMBER_SPEED_BASE*2;
        }
    }

    public void randomizedMovementX()
    {
        Random rand = new Random();
        int n = rand.nextInt(2) + 1;
        switch(n)
        {
            case 1:
                this.x += this.speed;
            case 2:
                this.x -= this.speed;
        }
    }

    @Override
    public TextureRegion getCurrentTextureRegion() {
        frameStateTime += Gdx.graphics.getDeltaTime();
        currentFrame = animation.getKeyFrame(frameStateTime, true);

        return currentFrame;
    }
}

