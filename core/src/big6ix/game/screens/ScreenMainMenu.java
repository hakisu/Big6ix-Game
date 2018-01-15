package big6ix.game.screens;

import big6ix.game.Player;
import big6ix.game.map.Map;
import big6ix.game.utility.Pair;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.HashMap;

public class ScreenMainMenu extends ScreenAdapter {

    private final GameMain gameMain;
    private Stage stage;
    private Skin skin;
    private TextureAtlas atlas;
    private Music intro;


    public ScreenMainMenu(GameMain game) {
        this.gameMain = game;

        atlas = new TextureAtlas(GameMain.UI_SKINS_ATLAS_NAME);
        skin = new Skin(Gdx.files.internal(GameMain.UI_SKINS_JSON_NAME), atlas);

        Viewport stretchViewPort = new StretchViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        stage = new Stage(stretchViewPort);

        // Create root table and all children elements of it
        Table mainTable = new Table();
        mainTable.setFillParent(true);
        mainTable.center();

        TextButton resumeButton = new TextButton("Resume", skin);
        TextButton playButton = new TextButton("New game", skin);
        TextButton optionsButton = new TextButton("Options", skin);
        TextButton exitButton = new TextButton("Exit", skin);

        mainTable.add(resumeButton).width(Gdx.graphics.getWidth() / 9);
        mainTable.row();
        mainTable.add(playButton).width(Gdx.graphics.getWidth() / 6);
        mainTable.row();
        mainTable.add(optionsButton).width(Gdx.graphics.getWidth() / 4);
        mainTable.row();
        mainTable.add(exitButton).width(Gdx.graphics.getWidth() / 3);
        stage.addActor(mainTable);

        // Manage inputs
        resumeButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                intro.stop();
                try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(GameMain.PATH_TO_SAVE_FILE))) {
                    // noinspection unchecked
                    HashMap<String, Object> savedGame = (HashMap<String, Object>) in.readObject();
                    gameMain.initializeGame((Map) savedGame.get("map"), (Pair) savedGame.get("playerPosition"));
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
                gameMain.activateGameScreen();
            }
        });
        playButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                intro.stop();
                game.initializeGame();
                game.activateGameScreen();
            }
        });
        optionsButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                gameMain.activateSettingsScreen();
            }
        });
        exitButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.exit();
            }
        });

        // Music
        intro = Gdx.audio.newMusic((Gdx.files.internal("sounds/intro.mp3")));
        intro.setLooping(true);
        this.updateIntroMusicVolume();
    }

    public void updateIntroMusicVolume() {
        intro.setVolume(GameMain.getPreferences().getMusicVolume());
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
        intro.play();
    }

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.1f, 0.12f, 0.16f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act();
        stage.draw();

        // Handle keyboard input
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit();
        }
    }

    @Override
    public void dispose() {
        intro.dispose();
        skin.dispose();
        atlas.dispose();
        stage.dispose();
    }
}