package big6ix.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;

public class ScreenMainMenu extends ScreenAdapter {
    private final GameMain gameMain;
    private Color fontColor;

    private OrthographicCamera camera;

    public ScreenMainMenu(GameMain gameMain) {
        this.gameMain = gameMain;

        fontColor = Color.BLACK;
        gameMain.font.setColor(fontColor);
        gameMain.font.getData().setScale(1.0f);

        camera = new OrthographicCamera();
        camera.setToOrtho(true, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    @Override
    public void show() {
        gameMain.batch.setProjectionMatrix(camera.combined);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.3f, 0.5f, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        gameMain.batch.begin();

        gameMain.font.draw(
                gameMain.batch, "testujemy pisanie",
                Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);

        gameMain.batch.end();

        if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
            gameMain.setScreen(gameMain.screenGame);
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit();
        }
    }
}
