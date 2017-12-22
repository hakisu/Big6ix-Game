package big6ix.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class ScreenMainMenu extends ScreenAdapter {

    private final GameMain gameMain;
    private Stage stage;
    private Skin skin;
    private TextureAtlas atlas;
    private Music intro;


    public ScreenMainMenu(final GameMain game) {
        this.gameMain = game;

        atlas = new TextureAtlas("uiskin.atlas");
        skin = new Skin(Gdx.files.internal("uiskin.json"), atlas);

        Viewport stretchViewPort = new StretchViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        stage = new Stage(stretchViewPort);

        // Music
        intro = Gdx.audio.newMusic((Gdx.files.internal("sounds/intro.mp3")));
        intro.setLooping(true);
        intro.setVolume(gameMain.getPreferences().getMusicVolume());

    }

    public void updateViewPort(Viewport viewport) {
        this.stage.getViewport().update(viewport.getScreenWidth(), viewport.getScreenHeight());
    }

    @Override
    public void show() {


        if(gameMain.getPreferences().isFullscreenEnabled() == true)
        {
            Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
            stage.getViewport().update(1920, 1080);
        }
        else{
            Gdx.graphics.setWindowedMode(800, 800);
            stage.getViewport().update(800, 800);
        }

        System.out.println("ScreenMainMenu Show.");

        intro.play();
        Gdx.input.setInputProcessor(stage);

        Table mainTable = new Table();
        mainTable.setFillParent(true);

        mainTable.center();


        TextButton playButton = new TextButton("Play", skin);
        TextButton optionsButton = new TextButton("Options", skin);
        TextButton exitButton = new TextButton("Exit", skin);
        playButton.getLabel().setFontScale(3, 3);
        optionsButton.getLabel().setFontScale(3, 3);
        exitButton.getLabel().setFontScale(3, 3);

        // Manage inputs
        playButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                intro.stop();
                gameMain.setScreen(gameMain.screenGame);
            }
        });
        optionsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                gameMain.setScreen(gameMain.screenSettings);
            }
        });
        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });

        // Use .expandY() if you want to increase spacing between buttons
        mainTable.add(playButton).width(Gdx.graphics.getWidth() / 3);
        mainTable.row();
        mainTable.add(optionsButton).width(Gdx.graphics.getWidth() / 4);
        mainTable.row();
        mainTable.add(exitButton).width(Gdx.graphics.getWidth() / 6);

        stage.addActor(mainTable);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(.1f, .12f, .16f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act();
        stage.draw();

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit();
        }

    }

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void dispose() {
        intro.dispose();
        skin.dispose();
        atlas.dispose();
        stage.dispose();
    }
}