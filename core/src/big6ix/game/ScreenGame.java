package big6ix.game;

import big6ix.game.Map.Map;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;

public class ScreenGame extends ScreenAdapter {
    private final GameMain gameMain;
    private final long ticksPerSecond = 60;
    ManagerBullets managerBullets;
    ManagerEnemies managerEnemies;
    Player player;
    private Map map;
    private OrthographicCamera camera;
    // Time management fields
    private long oldTime;
    private long timeAccumulator = 0;
    // frameTime represents time reserved for one frame in nanoseconds
    private long frameTime = 1000000000 / ticksPerSecond;
    private long updatesCount = 0;

    public ScreenGame(GameMain gameMain) {
        this.gameMain = gameMain;

        map = new Map(Constants.MAP_ROWS_AMOUNT, Constants.MAP_COLUMNS_AMOUNT);
        player = new Player();
        managerBullets = new ManagerBullets(this.player, this.map);
        managerEnemies = new ManagerEnemies(this.player, this.managerBullets, this.map);
        managerBullets.setManagerEnemies(managerEnemies);

        camera = new OrthographicCamera();
        camera.setToOrtho(true, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        managerEnemies.addEnemy(new EnemyShooter(128, 200));
        managerEnemies.addEnemy(new EnemyShooter(700, 1200));
        managerEnemies.addEnemy(new EnemyShooter(1100, 700));
        managerEnemies.addEnemy(new EnemyShooter(1520, 1100));
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

        updateGraphics(timeDifference);

        if (timeAccumulator >= frameTime) {
            timeAccumulator -= frameTime;
            ++updatesCount;
            update();
            handleInput();
        }
    }

    @Override
    public void dispose() {
    }

    private void updateGraphics(long timeDifference) {
        // Cleaning the display with given color
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.position.x = player.getX() + player.getWidth() / 2;
        camera.position.y = player.getY() + player.getWidth() / 2;

        camera.update();
        gameMain.batch.setProjectionMatrix(camera.combined);

        // Starting drawing in batch
        gameMain.batch.begin();

        map.render(gameMain.batch);
        managerEnemies.render(gameMain.batch);
        player.render(gameMain.batch, this.camera);
        managerBullets.render(gameMain.batch);

        // Ending drawing in batch
        gameMain.batch.end();
    }

    private void update() {
        player.update(this.map);
        managerEnemies.update();
        managerBullets.update();
    }

    private void handleInput() {
        if (Gdx.input.justTouched()) {
            Vector3 mousePositionInGameWorld = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(mousePositionInGameWorld);

            BulletBasic bulletBasic = new BulletBasic(
                    true, Constants.BULLET_BASIC_SPEED,
                    player.getX() + player.getWidth() / 2 - Constants.BULLET_BASIC_WIDTH / 2,
                    player.getY() + player.getHeight() / 2 - Constants.BULLET_BASIC_HEIGHT / 2,
                    mousePositionInGameWorld.x - Constants.BULLET_BASIC_WIDTH / 2,
                    mousePositionInGameWorld.y - Constants.BULLET_BASIC_HEIGHT / 2
            );
            managerBullets.addBullet(bulletBasic);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            gameMain.setScreen(gameMain.screenMainMenu);
        }
    }
}
