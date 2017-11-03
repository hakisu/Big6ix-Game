package big6ix.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;

import static com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;

public class ScreenGame extends ScreenAdapter {
    private final GameMain gameMain;

    ManagerBullets managerBullets = null;

    private AtlasRegion playerImage;
    private AtlasRegion enemyImage;
    private AtlasRegion wallImage;
    private AtlasRegion floorImage;


    private Map map;
    private OrthographicCamera camera;

    // Time management fields
    private long oldTime;
    private final long ticksPerSecond = 60;
    private long timeAccumulator = 0;
    // frameTime represents time reserved for one frame in nanoseconds
    private long frameTime = 1000000000 / ticksPerSecond;

    // temp fields
    private float posX = 900;
    private float posY = 500;
    private float speed = 8f;

    public ScreenGame(GameMain gameMain) {
        this.gameMain = gameMain;

        map = new Map(3, 3);
        managerBullets = new ManagerBullets(this.map);

        playerImage = GameMain.gameAtlas.findRegion(Constants.PLAYER_NAME);
        if (playerImage == null)
            System.out.println("player null");
        enemyImage = GameMain.gameAtlas.findRegion(Constants.ENEMY_NAME);
        if (enemyImage == null)
            System.out.println("enemy null");
        wallImage = GameMain.gameAtlas.findRegion(Constants.WALL_NAME);
        if (wallImage == null)
            System.out.println("wall null");
        floorImage = GameMain.gameAtlas.findRegion(Constants.FLOOR_NAME);
        if (floorImage == null)
            System.out.println("floor null");

        camera = new OrthographicCamera();
        camera.setToOrtho(true, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    @Override
    public void show() {
        oldTime = System.nanoTime();
        System.out.println("Game screen set to active.");
    }

    // Main game loop for physics, input and graphics updates
    @Override
    public void render(float delta) {
        long newTime = System.nanoTime();
        long timeDifference = newTime - oldTime;
        oldTime = newTime;
        timeAccumulator += timeDifference;

        if (timeAccumulator >= frameTime) {
            timeAccumulator -= frameTime;

            if (handleInput()) {
                return;
            }
            updatePhysics();
        }
        updateGraphics(timeDifference);
    }

    @Override
    public void dispose() {

    }

    private void updateGraphics(long timeDifference) {
        // Cleaning the display with given color
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.position.x = posX + 32;
        camera.position.y = posY + 32;

        camera.update();
        gameMain.batch.setProjectionMatrix(camera.combined);

        gameMain.batch.totalRenderCalls = 0;
        gameMain.batch.begin();
        // test
        for (int i = 0; i < 30; ++i) {
            for (int j = 0; j < 30; ++j) {
                if (map.getMap()[i][j] == 0) {
                    gameMain.batch.draw(floorImage, i * 64, j * 64);
                } else if (map.getMap()[i][j] == 1) {
                    gameMain.batch.draw(wallImage, i * 64, j * 64);
                }
            }
        }
        gameMain.batch.draw(playerImage, posX, posY);
        gameMain.batch.draw(enemyImage, 200, 100);
        managerBullets.render(gameMain.batch);
        gameMain.batch.end();
    }

    private void updatePhysics() {
        managerBullets.update();
    }

    private boolean handleInput() {
        // movement commands
        int tileIndexX, tileIndexY;
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            if (map.getMap()[tileIndexX = (int) posX / 64][tileIndexY = (int) (posY - speed) / 64] != 1) {
                posY -= speed;
            } else {
                posY = tileIndexY * 64 + 64;
            }
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            if (map.getMap()[tileIndexX = (int) posX / 64][tileIndexY = (int) (posY + speed + 64) / 64] != 1) {
                posY += speed;
            } else {
                posY = tileIndexY * 64 - 64;
            }
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            if (map.getMap()[tileIndexX = (int) (posX + speed + 64) / 64][tileIndexY = (int) posY / 64] != 1) {
                posX += speed;
            } else {
                posX = tileIndexX * 64 - 64;
            }
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            if (map.getMap()[tileIndexX = (int) (posX - speed) / 64][tileIndexY = (int) posY / 64] != 1) {
                posX -= speed;
            } else {
                posX = tileIndexX * 64 + 64;
            }
        }

        // Needs to be changed later for custom InputProcessor/InputAdapter
        if (Gdx.input.justTouched()) {
            Vector3 mousePositionInGameWorld = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(mousePositionInGameWorld);

            // Temp for now, each enemy and player will be responsible for it in method shoot()
            BulletBasic bulletBasic = new BulletBasic(
                    true, posX + 32, posY + 32,
                    mousePositionInGameWorld.x, mousePositionInGameWorld.y
            );
            managerBullets.addBullet(bulletBasic);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            gameMain.setScreen(gameMain.screenMainMenu);
            return true;
        }

        return false;
    }
}
