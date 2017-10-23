package big6ix.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class GameMain extends ApplicationAdapter {
    private Texture img;
    private Texture playerImage;
    private SpriteBatch batch;
    private OrthographicCamera camera;

    //	time management fields
    private long oldTime;
    private final long ticksPerSecond = 60;
    private long timeAccumulator = 0;
    // frameTime represents time reserved for one frame in nanoseconds
    private long frameTime = 1000000000 / ticksPerSecond;

    // temp fields
    private float posX = 20;
    private float posY = 100;
    private float speed = 0.1f;

    @Override
    public void create() {
        batch = new SpriteBatch();
        playerImage = new Texture("player.png");
        camera = new OrthographicCamera();
        camera.setToOrtho(true, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        Cursor gameCursor = Gdx.graphics.newCursor(new Pixmap(Gdx.files.internal("crosshair.png")), 16, 16);
        Gdx.graphics.setCursor(gameCursor);

        oldTime = System.nanoTime();
    }

    @Override
    public void render() {
        long newTime = System.nanoTime();
        long timeDifference = newTime - oldTime;
        oldTime = newTime;
        timeAccumulator += timeDifference;

        if (timeAccumulator >= 1000000000) {
            System.out.println(timeDifference / 1000000.0);
            timeAccumulator = 0;
        }

        handleInput();
        updatePhysics();
        updateGraphics(timeDifference);
    }

    @Override
    public void dispose() {
        playerImage.dispose();
        batch.dispose();
        System.out.println("Dispose was successful!");
    }

    private void updateGraphics(long timeDifference) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.position.x = posX + 32;
        camera.position.y = posY + 32;

        camera.update();
        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        batch.draw(playerImage, posX, posY);
        batch.draw(playerImage, 200, 100);
        batch.end();
    }

    private void updatePhysics() {
    }

    private void handleInput() {
        // movement commands
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            posY -= speed;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            posY += speed;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            posX += speed;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            posX -= speed;
        }
        if (Gdx.input.isTouched()) {
            System.out.println(Gdx.input.getX() + "  " + Gdx.input.getY());
        }
        // exit command
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit();
        }
    }
}
