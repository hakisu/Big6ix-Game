package big6ix.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class ScreenSettings extends ScreenAdapter {
    private final GameMain gameMain;
    private final Skin skin;
    private Stage stage;
    private TextureAtlas atlas;

    public ScreenSettings(GameMain gameMain) {
        this.gameMain = gameMain;
        atlas = new TextureAtlas("uiskin.atlas");
        skin = new Skin(Gdx.files.internal("uiskin.json"), atlas);
        Viewport stretchViewport = new StretchViewport(Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
        stage = new Stage(stretchViewport);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);

        //ScreenBox dla trybów ekranu
        Object[] screenSet = new Object[2];
        screenSet[0] = new Label("Fullscreen", skin);
        screenSet[1] = new Label("Windowed", skin);
        SelectBox<Object> sbSize = new SelectBox<>(skin);
        sbSize.setItems(screenSet);

        //ScreenBox dla Vsync
        Object[] vsyncSet = new Object[2];
        vsyncSet[0] = new Label("On", skin);
        vsyncSet[1] = new Label("Off", skin);
        final SelectBox<Object> sbVsync = new SelectBox(skin);
        sbVsync.setItems(vsyncSet);

        Label screenSizeLabel = new Label("Screen size ", skin);
        screenSizeLabel.setFontScale(2, 2);
        Label vSyncLabel = new Label("Vsync ", skin);
        vSyncLabel.setFontScale(2, 2);

        Table table = new Table();
        table.setFillParent(true);
        table.center();

        table.add(screenSizeLabel).uniform();
        table.add(sbSize);
        table.row();
        table.add(vSyncLabel);
        table.add(sbVsync);

        sbSize.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (screenSet[0] == sbSize.getSelected()) {
                    Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
                    stage.getViewport().update(1920, 1080);
                } else {
                    Gdx.graphics.setWindowedMode(800, 800);
                    stage.getViewport().update(800, 800);
                }

            }
        });

        /*sbVsync.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if(vsyncSet[0] == sbVsync.getSelected())
                {
                    Gdx.graphics.setVSync(true);
                }
                else
                {
                    Gdx.graphics.setVSync(false);
                }
            }
        });*/
        stage.addActor(table);
        System.out.println("Settings screen set to active.");
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(.1f, .12f, .16f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act();
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