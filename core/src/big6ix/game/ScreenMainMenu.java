package big6ix.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;


public class ScreenMainMenu extends ScreenAdapter {

    final GameMain gameMain;
    private SpriteBatch batch;
    protected Stage stage;
    private TextureAtlas atlas;
    protected Skin skin;

    public ScreenMainMenu(final GameMain game)
    {
        this.gameMain = game;
        //ustawienie skins
        atlas = new TextureAtlas("uiskin.atlas");
        skin = new Skin(Gdx.files.internal("uiskin.json"), atlas);
        batch = new SpriteBatch();
        stage = new Stage();
    }


    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);

        //Tworzenie tabeli
        Table mainTable = new Table();
        mainTable.setFillParent(true);
        //Ustawienie miejsca
        mainTable.center();

        //Tworzenie buttonów
        TextButton playButton = new TextButton("Play", skin);
        TextButton optionsButton = new TextButton("Options", skin);
        TextButton exitButton = new TextButton("Exit", skin);

        //Obsługa kliknięć
        playButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ((Game)Gdx.app.getApplicationListener()).setScreen(gameMain.screenGame);
            }
        });
        exitButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });

        //Dodanie buttonów do tabeli
        mainTable.add(playButton);
        mainTable.row();
        mainTable.add(optionsButton);
        mainTable.row();
        mainTable.add(exitButton);

        //Dodanie tabeli do stage
        stage.addActor(mainTable);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(.1f, .12f, .16f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act();
        stage.draw();
    }

    @Override
    public void dispose() {
        skin.dispose();
        atlas.dispose();
    }

}