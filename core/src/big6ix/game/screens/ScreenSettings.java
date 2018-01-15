package big6ix.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class ScreenSettings extends ScreenAdapter {

    private static final float VOLUME_MUSIC_STEP_VALUE = 0.01f;
    private static final float VOLUME_SOUND_EFFECTS_STEP_VALUE = 0.01f;
    private static final float SLIDER_HORIZONTAL_PADDING = 20;
    private static final float CELLS_VERTICAL_PADDING = 15;

    private final GameMain gameMain;
    TextField roomsAmountTextField;
    private Skin skin;
    private Stage stage;
    private TextureAtlas atlas;

    public ScreenSettings(GameMain gameMain) {
        this.gameMain = gameMain;

        atlas = new TextureAtlas(GameMain.UI_SKINS_ATLAS_NAME);
        skin = new Skin(Gdx.files.internal(GameMain.UI_SKINS_JSON_NAME), atlas);

        Viewport stretchViewport = new StretchViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        stage = new Stage(stretchViewport);

        // Create root table and all children elements of it
        Table table = new Table();
        table.setFillParent(true);
        table.center();

        Label volumeMusicSliderLabel = new Label("Music volume", skin);
        Slider volumeMusicSlider = new Slider(0f, 1f, VOLUME_MUSIC_STEP_VALUE, false, skin);
        volumeMusicSlider.setValue(GameMain.getPreferences().getMusicVolume());
        Label volumeMusicValueLabel = new Label(Integer.toString((int) (GameMain.getPreferences().getMusicVolume() * 100)), skin);

        Label volumeSoundEffectsSliderLabel = new Label("Sound effects volume", skin);
        Slider volumeSoundEffectsSlider = new Slider(0f, 1f, VOLUME_SOUND_EFFECTS_STEP_VALUE, false, skin);
        volumeSoundEffectsSlider.setValue(GameMain.getPreferences().getSoundEffectsVolume());
        Label volumeSoundEffectsValueLabel = new Label(Integer.toString((int) (GameMain.getPreferences().getSoundEffectsVolume() * 100)), skin);

        Label roomsAmountLabel = new Label("Rooms amount", skin);
        roomsAmountTextField = new TextField(Integer.toString(GameMain.getPreferences().getRoomsAmount()), skin);

        TextButton backButton = new TextButton("Back", skin);

        table.add(volumeMusicSliderLabel).uniform();
        table.add(volumeMusicSlider).pad(0, SLIDER_HORIZONTAL_PADDING, 0, SLIDER_HORIZONTAL_PADDING).uniform();
        table.add(volumeMusicValueLabel).left().uniform();
        table.row().padTop(CELLS_VERTICAL_PADDING);
        table.add(volumeSoundEffectsSliderLabel).uniform();
        table.add(volumeSoundEffectsSlider).padLeft(SLIDER_HORIZONTAL_PADDING).padRight(SLIDER_HORIZONTAL_PADDING).uniform();
        table.add(volumeSoundEffectsValueLabel).left().uniform();
        table.row().padTop(CELLS_VERTICAL_PADDING);
        table.add(roomsAmountLabel).uniform();
        table.add(roomsAmountTextField).uniform();
        table.row();
        table.add(backButton).colspan(3).padTop(CELLS_VERTICAL_PADDING);
        stage.addActor(table);

        // Manage inputs
        volumeMusicSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                GameMain.getPreferences().setMusicVolume(volumeMusicSlider.getValue());
                volumeMusicValueLabel.setText(Integer.toString((int) (GameMain.getPreferences().getMusicVolume() * 100)));
                gameMain.updateIntroMusicVolume();
            }
        });
        volumeSoundEffectsSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                GameMain.getPreferences().setSoundEffectsVolume(volumeSoundEffectsSlider.getValue());
                volumeSoundEffectsValueLabel.setText(Integer.toString((int) (GameMain.getPreferences().getSoundEffectsVolume() * 100)));
            }
        });
        backButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                exitSettings();
            }
        });
    }

    private void exitSettings() {
        try {
            int amountOfRoomsToCreate;
            if ((amountOfRoomsToCreate = Integer.parseInt(roomsAmountTextField.getText())) > 0 && amountOfRoomsToCreate < 2000) {
                GameMain.getPreferences().setRoomsAmout(amountOfRoomsToCreate);
                gameMain.activateMainMenuScreen();
            } else {
                roomsAmountTextField.setText("Out of range.");
            }
        } catch (NumberFormatException e) {
            roomsAmountTextField.setText("Invalid input!");
        }
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.1f, 0.12f, 0.16f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act();
        stage.draw();

        // Handle keyboard input
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            exitSettings();
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