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
    private Texture wallTexture;
    private Texture floorTexture;

    private Map map;
    private SpriteBatch batch;
    private OrthographicCamera camera;

    //	time management fields
    private long oldTime;
    private final long ticksPerSecond = 60;
    private long timeAccumulator = 0;
    // frameTime represents time reserved for one frame in nanoseconds
    private long frameTime = 1000000000 / ticksPerSecond;

    // temp fields
    private float posX = 900;
    private float posY = 500;
    private float speed = 5f;

    @Override
    public void create() {
        Cursor gameCursor = Gdx.graphics.newCursor(new Pixmap(Gdx.files.internal("crosshair.png")), 16, 16);
        Gdx.graphics.setCursor(gameCursor);

        batch = new SpriteBatch();
        playerImage = new Texture("player.png");
        playerSprite = new Sprite(playerImage);
        playerSprite.flip(false, true);
        enemyImage = new Texture("enemy.png");
        enemySprite = new Sprite(enemyImage);
        enemySprite.flip(false, true);
        wallTexture = new Texture("wall.png");
        floorTexture = new Texture("floor.png");

        camera = new OrthographicCamera();
        camera.setToOrtho(true, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());


        bullets = new Array<Bullet>();

        oldTime = System.nanoTime();

        // temp
        map = new Map(3, 3);
    }

    // Main game loop for physics, input and graphics updates
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
        // test
        for (int i = 0; i < 30; ++i) {
            for (int j = 0; j < 30; ++j) {
                if (map.getMap()[i][j] == 0) {
                    batch.draw(floorTexture, i * 64, j * 64);
                } else if (map.getMap()[i][j] == 1) {
                    batch.draw(wallTexture, i * 64, j * 64);
                }
            }
        }
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
            if (map.getMap()[(int) bullet.getX() / 64][(int) bullet.getY() / 64] == 1) {
                bulletsIterator.remove();
            }
        }
    }

    private void handleInput() {
        // movement commands
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            if (map.getMap()[(int) posX / 64][(int) (posY - speed) / 64] != 1) {
                posY -= speed;
            }
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            if (map.getMap()[(int) posX / 64][(int) (posY + speed + 64) / 64] != 1) {
                posY += speed;
            }
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            if (map.getMap()[(int) (posX + speed + 64) / 64][(int) posY / 64] != 1) {
                posX += speed;
            }
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            if (map.getMap()[(int) (posX - speed) / 64][(int) posY / 64] != 1) {
                posX -= speed;
            }
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
