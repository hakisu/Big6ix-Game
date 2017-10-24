package big6ix.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

import java.util.Iterator;

public class GameMain extends ApplicationAdapter {
    private Texture playerImage;
    private Sprite playerSprite;
    private Texture enemyImage;
    private Sprite enemySprite;
    private Array<Bullet> bullets;
    private Iterator<Bullet> bulletsIterator;


    private SpriteBatch batch;
    private OrthographicCamera camera;

    //	time management fields
    private long oldTime;
    private final long ticksPerSecond = 60;
    private long timeAccumulator = 0;
    // frameTime represents time reserved for one frame in nanoseconds
    private long frameTime = 1000000000 / ticksPerSecond;

    // temp fields
    private float posX = 0;
    private float posY = 0;
    private float speed = 2.5f;

    @Override
    public void create() {
        batch = new SpriteBatch();

        playerImage = new Texture("player.png");
        playerSprite = new Sprite(playerImage);
        playerSprite.flip(false, true);
        enemyImage = new Texture("enemy.png");
        enemySprite = new Sprite(enemyImage);
        enemySprite.flip(false, true);

        camera = new OrthographicCamera();
        camera.setToOrtho(true, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        Cursor gameCursor = Gdx.graphics.newCursor(new Pixmap(Gdx.files.internal("crosshair.png")), 16, 16);
        Gdx.graphics.setCursor(gameCursor);

        bullets = new Array<Bullet>();

        oldTime = System.nanoTime();
    }

    @Override
    public void render() {
        long newTime = System.nanoTime();
        long timeDifference = newTime - oldTime;
        oldTime = newTime;
        timeAccumulator += timeDifference;

        if (timeAccumulator >= frameTime) {
            timeAccumulator -= frameTime;
            handleInput();
            updatePhysics();
        }

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
        batch.draw(playerSprite, posX, posY);
        batch.draw(enemySprite, 200, 100);
        for (Bullet bullet : bullets) {
            batch.draw(bullet.sprite, bullet.getX() - 16, bullet.getY() - 16, 32, 32);
        }
        batch.end();
    }

    private void updatePhysics() {
        bulletsIterator = bullets.iterator();
        while (bulletsIterator.hasNext()) {
            Bullet bullet = bulletsIterator.next();
            bullet.update();
        }
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
            Vector3 mousePositionInGameWorld = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(mousePositionInGameWorld);

            Bullet bullet = new Bullet(posX + 32, posY + 32, mousePositionInGameWorld.x, mousePositionInGameWorld.y);
            bullets.add(bullet);
        }
        // exit command
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit();
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            System.out.println(bullets.size);
        }
    }
}
