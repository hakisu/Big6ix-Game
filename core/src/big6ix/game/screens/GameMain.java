package big6ix.game.screens;

import big6ix.game.BigPreferences;
import big6ix.game.map.Map;
import big6ix.game.utility.Pair;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class GameMain extends Game {

    public static final String PATH_TO_SAVE_FILE = "data/game.save";
    public static final String UI_SKINS_ATLAS_NAME = "skins/uiskin.atlas";
    public static final String UI_SKINS_JSON_NAME = "skins/uiskin.json";
    public static final int FONT_SIZE = 20;
    // A single batch can draw no more than 8191 sprites. Otherwise it will throw IllegalArgumentException
    private static final int BATCH_MAX_NUMBER_OF_SPRITES = 2000;
    private static final int CURSOR_X_HOTSPOT = 16;
    private static final int CURSOR_Y_HOTSPOT = 16;
    private static TextureAtlas gameAtlas;
    private static BigPreferences preferences;

    static {
        preferences = new BigPreferences();
    }

    // Batch and font used for drawing available for different screen objects
    public SpriteBatch batch;
    private BitmapFont font;
    private Cursor gameCursor;
    // Game screens
    private ScreenGame screenGame;
    private ScreenMainMenu screenMainMenu;
    private ScreenSettings screenSettings;

    public static TextureAtlas getGameAtlas() {
        return gameAtlas;
    }

    public static BigPreferences getPreferences() {
        return preferences;
    }

    public static void removeSaveFile() {
        // Delete saved game state if it exists
        Path path = Paths.get(GameMain.PATH_TO_SAVE_FILE);
        try {
            Files.deleteIfExists(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public BitmapFont getFont() {
        return font;
    }

    public ScreenGame getScreenGame() {
        return screenGame;
    }

    public void updateIntroMusicVolume() {
        screenMainMenu.updateIntroMusicVolume();
    }

    public void initializeGame() {
        screenGame.initializeGame();
    }

    public void initializeGame(Map map, Pair playerPosition, Integer playerHealth) {
        screenGame.initializeGame(map, playerPosition, playerHealth);
    }

    public void activateGameScreen() {
        activateGameCursor();
        this.setScreen(screenGame);
    }

    public void activateMainMenuScreen() {
        activateNormalCursor();
        this.setScreen(screenMainMenu);
    }

    public void activateSettingsScreen() {
        this.setScreen(screenSettings);
    }

    public void activateGameCursor() {
        Gdx.graphics.setCursor(gameCursor);
    }

    public void activateNormalCursor() {
        Gdx.graphics.setSystemCursor(Cursor.SystemCursor.Arrow);
    }

    @Override
    public void create() {
        gameAtlas = new TextureAtlas(Gdx.files.internal("graphics/gameTexturePack.atlas"), true);
        batch = new SpriteBatch(BATCH_MAX_NUMBER_OF_SPRITES);

        FreeTypeFontGenerator fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/OpenSans-Regular.ttf"));
        FreeTypeFontParameter fontParameter = new FreeTypeFontParameter();
        fontParameter.size = FONT_SIZE;
        fontParameter.flip = true;
        font = fontGenerator.generateFont(fontParameter);
        fontGenerator.dispose();

        // Choosing crosshair image as game cursor
        gameCursor = Gdx.graphics.newCursor(
                new Pixmap(Gdx.files.internal("graphics/crosshair.png")),
                CURSOR_X_HOTSPOT,
                CURSOR_Y_HOTSPOT
        );

        // Screens initialization
        screenGame = new ScreenGame(this);
        screenMainMenu = new ScreenMainMenu(this);
        screenSettings = new ScreenSettings(this);

        this.setScreen(screenMainMenu);
    }

    @Override
    public void render() {
        // Render method is called on each screen object differently
        super.render();
    }

    @Override
    public void dispose() {
        // Dispose methods for screens must be used manually
        System.out.println("Dispose called from GameMain.");
        batch.dispose();
        font.dispose();
        screenGame.dispose();
        screenSettings.dispose();
        screenMainMenu.dispose();
        gameAtlas.dispose();
    }
}