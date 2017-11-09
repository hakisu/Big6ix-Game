package big6ix.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;

public class Player {
    private float x;
    private float y;
    private int width;
    private int height;
    // change
    public float speed;

    private TextureRegion currentFrame;
    private float frameTime = 0;
    private Animation<TextureRegion> animationDown;
    private Animation<TextureRegion> animationLeft;
    private Animation<TextureRegion> animationUp;
    private Animation<TextureRegion> animationRight;

    public Player() {
        animationDown = new Animation<TextureRegion>(0.5f, GameMain.gameAtlas.findRegions(Constants.ATLAS_PLAYER_DOWN_NAME), Animation.PlayMode.LOOP);
        animationLeft = new Animation<TextureRegion>(0.5f, GameMain.gameAtlas.findRegions(Constants.ATLAS_PLAYER_LEFT_NAME), Animation.PlayMode.LOOP);
        animationUp = new Animation<TextureRegion>(0.5f, GameMain.gameAtlas.findRegions(Constants.ATLAS_PLAYER_UP_NAME), Animation.PlayMode.LOOP);
        animationRight = new Animation<TextureRegion>(0.5f, GameMain.gameAtlas.findRegions(Constants.ATLAS_PLAYER_RIGHT_NAME), Animation.PlayMode.LOOP);

        this.x = Constants.PLAYER_STARTING_X;
        this.y = Constants.PLAYER_STARTING_Y;
        this.width = Constants.PLAYER_WIDTH;
        this.height = Constants.PLAYER_HEIGHT;
        this.speed = Constants.PLAYER_SPEED;
    }

    public void update(Map map) {
        // Movement commands for player
        int tileIndexY, tileIndexX;
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            if ((map.getMapArray()[tileIndexY = (int) (y - speed) / map.getTileHeight()][tileIndexX = (int) x / map.getTileWidth()] == 1)
                    || (map.getMapArray()[tileIndexY = (int) (y - speed) / map.getTileHeight()][tileIndexX = (int) (x + width) / map.getTileWidth()] == 1)) {
                y = tileIndexY * map.getTileHeight() + (map.getTileHeight() + 1);
            } else {
                y -= speed;
            }

        } else if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            if ((map.getMapArray()[tileIndexY = (int) (y + speed + height) / map.getTileHeight()][tileIndexX = (int) x / map.getTileWidth()] == 1)
                    || (map.getMapArray()[tileIndexY = (int) (y + speed + height) / map.getTileHeight()][tileIndexX = (int) (x + width) / map.getTileWidth()] == 1)) {
                y = tileIndexY * map.getTileHeight() - (height + 1);
            } else {
                y += speed;
            }
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            if ((map.getMapArray()[tileIndexY = (int) y / map.getTileHeight()][tileIndexX = (int) (x + speed + width) / map.getTileWidth()] == 1)
                    || (map.getMapArray()[tileIndexY = (int) (y + height) / map.getTileHeight()][tileIndexX = (int) (x + speed + width) / map.getTileWidth()] == 1)) {
                x = tileIndexX * map.getTileWidth() - (width + 1);
            } else {
                x += speed;
            }
        } else if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            if ((map.getMapArray()[tileIndexY = (int) y / map.getTileHeight()][tileIndexX = (int) (x - speed) / map.getTileWidth()] == 1)
                    || (map.getMapArray()[tileIndexY = (int) (y + height) / map.getTileHeight()][tileIndexX = (int) (x - speed) / map.getTileWidth()] == 1)) {
                x = tileIndexX * map.getTileWidth() + (map.getTileWidth() + 1);
            } else {
                x -= speed;
            }
        }
    }

    public void render(SpriteBatch batch, OrthographicCamera camera) {
        frameTime += Gdx.graphics.getDeltaTime();

        float currentPlayerAngle = getAngleToMouse(camera);
        if (currentPlayerAngle >= 315 || currentPlayerAngle < 45) {
            currentFrame = animationRight.getKeyFrame(frameTime, true);
        }
        else if (currentPlayerAngle >= 45 && currentPlayerAngle < 135) {
            currentFrame = animationDown.getKeyFrame(frameTime, true);
        }
        else if (currentPlayerAngle >= 135 && currentPlayerAngle < 225) {
            currentFrame = animationLeft.getKeyFrame(frameTime, true);
        }
        else if (currentPlayerAngle >= 225 && currentPlayerAngle < 315) {
            currentFrame = animationUp.getKeyFrame(frameTime, true);
        }

        batch.draw(this.currentFrame, this.x, this.y, this.width, this.height);
    }

    public void shoot() {
        // Na pozniej do zrobienia
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    private float getAngleToMouse(OrthographicCamera camera) {
        Vector3 mousePositionInGameWorld = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        camera.unproject(mousePositionInGameWorld);

        float angle = (float) Math.toDegrees(Math.atan2(mousePositionInGameWorld.y - (this.y + this.height / 2), mousePositionInGameWorld.x - (this.x + this.width / 2)));
        if (angle < 0) {
            angle += 360;
        }

        return angle;
    }
}