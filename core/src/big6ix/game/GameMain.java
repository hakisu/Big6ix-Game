package big6ix.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;

public class GameMain extends Game {

    // A single batch can draw no more than 8191 sprites. Otherwise it will throw IllegalArgumentException
    private static final int BATCH_MAX_NUMBER_OF_SPRITES = 2000;
    private static final int CURSOR_X_HOTSPOT = 16;
    private static final int CURSOR_Y_HOTSPOT = 16;

    private static TextureAtlas gameAtlas;

    // Game screens
    ScreenGame screenGame;
    ScreenMainMenu screenMainMenu;
    ScreenSettings screenSettings;
    // Batch and font used for drawing available for different screen objects
    SpriteBatch batch;
    BitmapFont font;
    private BigPreferences preferences;

    public static TextureAtlas getGameAtlas() {
        return gameAtlas;
    }

    public ScreenGame getScreenGame() {
        return screenGame;
    }

    public BigPreferences getPreferences() {
        return preferences;
    }

    @Override
    public void create() {
        gameAtlas = new TextureAtlas(Gdx.files.internal("graphics/gameTexturePack.atlas"), true);
        batch = new SpriteBatch(BATCH_MAX_NUMBER_OF_SPRITES);

        preferences = new BigPreferences();

        FreeTypeFontGenerator fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/OpenSans-Regular.ttf"));
        FreeTypeFontParameter fontParameter = new FreeTypeFontParameter();
        fontParameter.size = 50;
        fontParameter.flip = false;
        font = fontGenerator.generateFont(fontParameter);
        fontGenerator.dispose();

        // Choosing crosshair image as game cursor
        Cursor gameCursor = Gdx.graphics.newCursor(
                new Pixmap(Gdx.files.internal("graphics/crosshair.png")),
                CURSOR_X_HOTSPOT, CURSOR_Y_HOTSPOT
        );
        Gdx.graphics.setCursor(gameCursor);

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
    public void pause() {
        System.out.println("Pause called from GameMain.");
    }

    @Override
    public void resume() {
        System.out.println("Resume called from GameMain.");
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
