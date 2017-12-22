package big6ix.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class ScreenSettings extends ScreenAdapter {
    private final GameMain gameMain;
    private final Skin skin;
    private Stage stage;
    private TextureAtlas atlas;
    private BigPreferences pref;
    private Label fullscreenOnOffLabel;
    private Label volumeMusicSliderLabel;
    private Label volumeSoundEffectsSliderLabel;


    public ScreenSettings(GameMain gameMain) {
        this.gameMain = gameMain;
        atlas = new TextureAtlas("uiskin.atlas");
        skin = new Skin(Gdx.files.internal("uiskin.json"), atlas);
        Viewport stretchViewport = new StretchViewport(Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
        stage = new Stage(stretchViewport);
        pref = new BigPreferences();
    }

    @Override
    public void show() {
        stage.clear();
        Gdx.input.setInputProcessor(stage);

        //Checkbox dla trybów ekranu
        final CheckBox fullscreenCheckbox = new CheckBox(null,skin);
        fullscreenCheckbox.setChecked(gameMain.getPreferences().isFullscreenEnabled());
        fullscreenCheckbox.addListener(new EventListener() {
            @Override
            public boolean handle(Event event) {
                boolean enabled = fullscreenCheckbox.isChecked();
                gameMain.getPreferences().setFullscreenEnabled(enabled);
                if(enabled == true){
                    Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
                    stage.getViewport().update(1920, 1080);
                }
                else{
                    Gdx.graphics.setWindowedMode(800, 800);
                    stage.getViewport().update(800, 800);
                }
                return false;
            }
        });

        //Slider do ustawiania glośności muzyki
        final Slider volumeMusicSlider = new Slider(0f,1f,0.1f,false,skin);
        volumeMusicSlider.setValue(gameMain.getPreferences().getMusicVolume());
        volumeMusicSlider.addListener(new EventListener() {
            @Override
            public boolean handle(Event event) {
                gameMain.getPreferences().setMusicVolume(volumeMusicSlider.getValue());
                return false;
            }
        });

        //Slider do ustawiania głośności efektów dźwiękowych
        final Slider volumeSoundEffectsSlider = new Slider(0f,1f,0.1f,false,skin);
        volumeSoundEffectsSlider.setValue(gameMain.getPreferences().getSoundEffectsVolume());
        volumeSoundEffectsSlider.addListener(new EventListener() {
            @Override
            public boolean handle(Event event) {
                gameMain.getPreferences().setSoundEffectsVolume(volumeSoundEffectsSlider.getValue());
                return false;
            }
        });

        //Button by powrócić do MainMenu
        final TextButton backButton = new TextButton("Back",skin);
        backButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                gameMain.setScreen(gameMain.screenMainMenu);
            }
        });

        Table table = new Table();
        table.setFillParent(true);
        table.center();

        fullscreenOnOffLabel = new Label("Fullscreen: ", skin);
        volumeMusicSliderLabel = new Label("Music volume ", skin);
        volumeSoundEffectsSliderLabel = new Label("Sound effects volume ", skin);

        table.add(fullscreenOnOffLabel).uniform();
        table.add(fullscreenCheckbox);
        table.row().pad(10,0,0,10);
        table.add(volumeMusicSliderLabel).uniform();
        table.add(volumeMusicSlider);
        table.row().pad(10,0,0,10);
        table.add(volumeSoundEffectsSliderLabel).uniform();
        table.add(volumeSoundEffectsSlider);
        table.row().pad(10,0,0,10);
        table.add(backButton).colspan(2);



        stage.addActor(table);
        System.out.println("Settings screen set to active.");
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(.1f, .12f, .16f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();

        // Wyjscie do menu
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            gameMain.screenMainMenu.updateViewPort(this.stage.getViewport());
            gameMain.setScreen(gameMain.screenMainMenu);
        }

    }

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void dispose() {

        skin.dispose();
        atlas.dispose();
        stage.dispose();
    }
}