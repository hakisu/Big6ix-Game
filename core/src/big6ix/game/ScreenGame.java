package big6ix.game;

import big6ix.game.bullets.BulletBasic;
import big6ix.game.map.Map;
import big6ix.game.map.Room;
import big6ix.game.utility.Pair;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;

public class ScreenGame extends ScreenAdapter {

    private static final long TICKS_PER_SECOND = 60;
    // FrameTime represents time reserved for one frame in nanoseconds
    private static final long FRAME_TIME = 1000000000 / TICKS_PER_SECOND;
    public static float CAMERA_INITIAL_ZOOM = 1;

    private final GameMain gameMain;
    private ManagerBullets managerBullets;
    private ManagerEnemies managerEnemies;
    private Player player;
    private Map map;
    private OrthographicCamera camera;
    // Time management fields
    private long oldTime;
    private long timeAccumulator = 0;
    private long updatesCount = 0;
    private Music mainTheme;
    private Sound shootingSound;

    public ScreenGame(GameMain gameMain) {
        this.gameMain = gameMain;
        shootingSound = Gdx.audio.newSound(Gdx.files.internal("sounds/shoot.wav"));

//        map = new Map();
//        player = new Player();
//        managerBullets = new ManagerBullets(this.player, this.map);
//        managerEnemies = new ManagerEnemies(this.player, this.managerBullets, this.map);
//        managerBullets.setManagerEnemies(managerEnemies);

        // Music
        mainTheme = Gdx.audio.newMusic(Gdx.files.internal("sounds/main_theme.mp3"));
        mainTheme.setLooping(true);
        mainTheme.setVolume(gameMain.getPreferences().getMusicVolume());

        camera = new OrthographicCamera();
        camera.setToOrtho(true, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

//        managerEnemies.addEnemy(new EnemyShooter(128, 200));
//        managerEnemies.addEnemy(new EnemyShooter(700, 1200));
//        managerEnemies.addEnemy(new EnemyShooter(1100, 700));
//        managerEnemies.addEnemy(new EnemyShooter(1520, 1100));
    }

    @Override
    public void show() {
        map = new Map();
        // Spawn player character in random room on random tile
        Room roomForPlayerSpawn = map.getRandomRoom();
        Pair tileIndicesForPlayerSpawn = roomForPlayerSpawn.getRandomWalkableTileIndices();
        player = new Player(tileIndicesForPlayerSpawn.getIndexX(), tileIndicesForPlayerSpawn.getIndexY());

        managerBullets = new ManagerBullets(this.player, this.map);
        managerEnemies = new ManagerEnemies(this.player, this.managerBullets, this.map);
        managerBullets.setManagerEnemies(managerEnemies);
        mainTheme.play();
        // For debugging
        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean scrolled(int amount) {
                CAMERA_INITIAL_ZOOM += amount;
                return true;
            }
        });
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

        if (timeAccumulator >= FRAME_TIME) {
            timeAccumulator -= FRAME_TIME;
            ++updatesCount;
            update();
            handleInput();
        }
    }

    @Override
    public void dispose() {
        mainTheme.dispose();
    }

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
    }

    private void updateGraphics(long timeDifference) {
        // Cleaning the display with given color
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.position.x = player.getX() + player.getWidth() / 2;
        camera.position.y = player.getY() + player.getWidth() / 2;
        camera.zoom = CAMERA_INITIAL_ZOOM;

        camera.update();
        gameMain.batch.setProjectionMatrix(camera.combined);

        // Starting drawing in batch
        gameMain.batch.begin();

        map.render(gameMain.batch, camera);
        managerEnemies.render(gameMain.batch);
        player.render(gameMain.batch, this.camera);
        managerBullets.render(gameMain.batch);

        // Ending drawing in batch
        gameMain.batch.end();
    }

    private void update() {
        player.update(this.map, gameMain);
        managerBullets.update();
        managerEnemies.update();
        map.update(managerEnemies, player, gameMain);
    }

    private void handleInput() {
        if (Gdx.input.justTouched()) {
            shootingSound.play(gameMain.getPreferences().getSoundEffectsVolume());
            Vector3 mousePositionInGameWorld = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(mousePositionInGameWorld);

            BulletBasic bulletBasic = new BulletBasic(
                    true,
                    player.getX() + player.getWidth() / 2 - BulletBasic.WIDTH / 2,
                    player.getY() + player.getHeight() / 2 - BulletBasic.HEIGHT / 2,
                    mousePositionInGameWorld.x - BulletBasic.WIDTH / 2,
                    mousePositionInGameWorld.y - BulletBasic.HEIGHT / 2
            );
            managerBullets.addBullet(bulletBasic);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            exitGameScreen();
        }
        if (Gdx.input.isKeyPressed(Input.Keys.TAB)) {
            CAMERA_INITIAL_ZOOM = 300;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.BACKSPACE)) {
            CAMERA_INITIAL_ZOOM = 1;
        }
    }

    public void exitGameScreen() {
        this.mainTheme.stop();
        this.gameMain.setScreen(gameMain.screenMainMenu);
    }
}
