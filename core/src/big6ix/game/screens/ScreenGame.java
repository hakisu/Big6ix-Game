package big6ix.game.screens;

import big6ix.game.HUD;
import big6ix.game.Player;
import big6ix.game.bullets.BulletBasic;
import big6ix.game.bullets.ManagerBullets;
import big6ix.game.enemies.ManagerEnemies;
import big6ix.game.map.Map;
import big6ix.game.map.MiniMap;
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
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.HashMap;

public class ScreenGame extends ScreenAdapter {

    public static final long TICKS_PER_SECOND = 60;
    // FrameTime represents time reserved for one frame in nanoseconds
    private static final long FRAME_TIME = 1000000000 / TICKS_PER_SECOND;
    private static final float MINIMAP_CAMERA_INITIAL_ZOOM = 20;
    private static final float MINIMAP_CAMERA_MOVE_SPEED = 200;
    private static final float MINIMAP_CAMERA_MINIMUM_ZOOM = 5;
    // In case if we easily want to change zoom in the game
    public static float CAMERA_INITIAL_ZOOM = 1;

    private final GameMain gameMain;
    private ShapeRenderer shapeRenderer;
    private ManagerBullets managerBullets;
    private ManagerEnemies managerEnemies;
    private Player player;
    private Map map;
    private MiniMap miniMap;
    private HUD hud;

    // Game cameras
    private OrthographicCamera camera;
    private OrthographicCamera uiCamera;
    private OrthographicCamera miniMapCamera;

    // Time management fields
    private long oldTime;
    private long timeAccumulator = 0;

    // Logic control fields
    private boolean gameRunning = true;
    private boolean miniMapActive = false;
    private boolean gameInitialized = false;

    // Sounds and music
    private Music mainTheme;
    private Sound shootingSound;

    public ScreenGame(GameMain gameMain) {
        this.gameMain = gameMain;
        this.shapeRenderer = new ShapeRenderer();
        miniMap = new MiniMap();
        hud = new HUD();
        shootingSound = Gdx.audio.newSound(Gdx.files.internal("sounds/shoot.wav"));

        // Music
        mainTheme = Gdx.audio.newMusic(Gdx.files.internal("sounds/main_theme.mp3"));
        mainTheme.setLooping(true);
        mainTheme.setVolume(GameMain.getPreferences().getMusicVolume());

        // Set up game cameras
        camera = new OrthographicCamera();
        camera.setToOrtho(true, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        uiCamera = new OrthographicCamera();
        uiCamera.setToOrtho(true, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        miniMapCamera = new OrthographicCamera();
        miniMapCamera.setToOrtho(true, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    public boolean isGameInitialized() {
        return gameInitialized;
    }

    private void update() {
        player.update(this.map, gameMain);
        managerBullets.update();
        managerEnemies.update();
        map.update(managerEnemies, player, gameMain);
    }

    private void updateGraphics() {
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

        // Before drawing hud we need to change camera used by batch to uiCamera
        uiCamera.update();
        gameMain.batch.setProjectionMatrix(uiCamera.combined);
        hud.render(gameMain, player, map);

        // Ending drawing in batch
        gameMain.batch.end();

        if (miniMapActive) {
            miniMapCamera.update();
            shapeRenderer.setProjectionMatrix(miniMapCamera.combined);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            miniMap.render(this.shapeRenderer, map.getRooms(), map.getRoomsCompletionStatuses(), this.player, map.getColumnsAmount(), map.getRowsAmount());
            shapeRenderer.end();
        }
    }

    private void handleInput() {
        if (!miniMapActive) {
            if (Gdx.input.justTouched()) {
                shootingSound.play(GameMain.getPreferences().getSoundEffectsVolume());
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
        } else {
            // Handle input for visible minimap
            if (Gdx.input.isKeyPressed(Input.Keys.W)) {
                miniMapCamera.position.y -= MINIMAP_CAMERA_MOVE_SPEED;
            }
            if (Gdx.input.isKeyPressed(Input.Keys.S)) {
                miniMapCamera.position.y += MINIMAP_CAMERA_MOVE_SPEED;
            }
            if (Gdx.input.isKeyPressed(Input.Keys.D)) {
                miniMapCamera.position.x += MINIMAP_CAMERA_MOVE_SPEED;
            }
            if (Gdx.input.isKeyPressed(Input.Keys.A)) {
                miniMapCamera.position.x -= MINIMAP_CAMERA_MOVE_SPEED;
            }
        }

        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            this.mainTheme.stop();
            this.gameMain.activateMainMenuScreen();
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.TAB)) {
            gameRunning = !gameRunning;
            miniMapActive = !miniMapActive;
        }
    }

    public void executeGameOver() {
        GameMain.removeSaveFile();
        this.mainTheme.stop();
        this.gameInitialized = false;
        this.gameMain.activateMainMenuScreen();
    }

    /**
     * Used to start new game.
     */
    public void initializeGame() {
        map = new Map();
        // Spawn player character in random room on random tile
        Room roomForPlayerSpawn = map.getRandomRoom();
        Pair tileIndicesForPlayerSpawn = roomForPlayerSpawn.getRandomWalkableTileIndices();
        player = new Player(tileIndicesForPlayerSpawn.getIndexX(), tileIndicesForPlayerSpawn.getIndexY());

        managerBullets = new ManagerBullets(this.player, this.map);
        managerEnemies = new ManagerEnemies(this.player, this.managerBullets, this.map);
        managerBullets.setManagerEnemies(managerEnemies);
        gameInitialized = true;
    }

    /**
     * Used to resume already created game.
     *
     * @param map            game Map
     * @param playerPosition position of the player on the map
     * @param playerHealth   health of the player
     */
    public void initializeGame(Map map, Pair playerPosition, Integer playerHealth) {
        this.map = map;
        this.map.initializePathFinder();
        this.player = new Player(playerPosition.getIndexX(), playerPosition.getIndexY());
        this.player.setHealth(playerHealth);
        this.managerBullets = new ManagerBullets(this.player, this.map);
        this.managerEnemies = new ManagerEnemies(this.player, this.managerBullets, this.map);
        this.managerBullets.setManagerEnemies(managerEnemies);
        gameInitialized = true;

    }

    @Override
    public void show() {
        mainTheme.play();
        mainTheme.setVolume(GameMain.getPreferences().getMusicVolume());

        // Handle input for minimap zoom changes
        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean scrolled(int amount) {
                if (miniMapActive) {
                    miniMapCamera.zoom += amount;
                    if (miniMapCamera.zoom <= MINIMAP_CAMERA_MINIMUM_ZOOM) {
                        miniMapCamera.zoom = MINIMAP_CAMERA_MINIMUM_ZOOM;
                    }
                }
                return true;
            }
        });
        // Minimap initial values
        miniMapCamera.zoom = MINIMAP_CAMERA_INITIAL_ZOOM;
        miniMapCamera.position.x = map.getColumnsAmount() * Map.TILE_WIDTH / 2;
        miniMapCamera.position.y = map.getRowsAmount() * Map.TILE_HEIGHT / 2;

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

        updateGraphics();

        if (timeAccumulator >= FRAME_TIME) {
            timeAccumulator -= FRAME_TIME;
            if (gameRunning) {
                update();
            }
            handleInput();
        }
    }

    @Override
    public void dispose() {
        HashMap<String, Object> savedGame = new HashMap<>();
        if (map != null && this.gameInitialized) {
            try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(GameMain.PATH_TO_SAVE_FILE))) {
                savedGame.put("map", this.map);
                savedGame.put("playerPosition", new Pair((int) this.player.getX() / Map.TILE_WIDTH, (int) this.player.getY() / Map.TILE_HEIGHT));
                savedGame.put("playerHealth", this.player.getHealth());
                out.writeObject(savedGame);
                out.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        mainTheme.dispose();
    }

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
    }
}