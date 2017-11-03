package big6ix.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;

public class ScreenSettings extends ScreenAdapter {
    private final GameMain gameMain;

    private OrthographicCamera camera;

    public ScreenSettings(GameMain gameMain) {
        this.gameMain = gameMain;

        camera = new OrthographicCamera();
        camera.setToOrtho(true, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    @Override
    public void show() {
        System.out.println("Settings screen set to active.");
    }

    @Override
    public void render(float delta) {
        gameMain.batch.setProjectionMatrix(camera.combined);

        Gdx.gl.glClearColor(0.7f, 0.5f, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        gameMain.batch.begin();



        gameMain.batch.end();

        // Wyjscie do menu
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            gameMain.setScreen(gameMain.screenMainMenu);
        }
    }

}
