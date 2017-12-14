package big6ix.game;

import big6ix.game.map.Map;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
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
    private float speed;

    private boolean inMovement = false;
    private TextureRegion currentFrame;
    private float frameStateTime = 0;
    private Animation<TextureRegion> animationDown;
    private Animation<TextureRegion> animationLeft;
    private Animation<TextureRegion> animationUp;
    private Animation<TextureRegion> animationRight;

    private Sound stepSound;
    private float soundLength = 0.25f;
    private float timetoNextSound = soundLength;

    public Player() {
        animationDown = new Animation<TextureRegion>(Constants.PLAYER_FRAME_DURATION, GameMain.getGameAtlas().findRegions(Constants.ATLAS_PLAYER_DOWN_NAME), Animation.PlayMode.LOOP);
        animationLeft = new Animation<TextureRegion>(Constants.PLAYER_FRAME_DURATION, GameMain.getGameAtlas().findRegions(Constants.ATLAS_PLAYER_LEFT_NAME), Animation.PlayMode.LOOP);
        animationUp = new Animation<TextureRegion>(Constants.PLAYER_FRAME_DURATION, GameMain.getGameAtlas().findRegions(Constants.ATLAS_PLAYER_UP_NAME), Animation.PlayMode.LOOP);
        animationRight = new Animation<TextureRegion>(Constants.PLAYER_FRAME_DURATION, GameMain.getGameAtlas().findRegions(Constants.ATLAS_PLAYER_RIGHT_NAME), Animation.PlayMode.LOOP);

        this.x = 64;
        this.y = 64;
        this.width = Constants.PLAYER_WIDTH;
        this.height = Constants.PLAYER_HEIGHT;
        this.speed = Constants.PLAYER_SPEED;

        stepSound = Gdx.audio.newSound(Gdx.files.internal("sounds/step23.mp3"));
    }

    public void update(Map map) {
        // Movement commands for player
        int tileIndexY, tileIndexX;
        // inMovement is used to determine if running animation should be used for player
        inMovement = false;

        debugMove();

//        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
//            if ((!map.getMapArray()[tileIndexY = (int) (y - speed) / map.getTileHeight()][tileIndexX = (int) x / map.getTileWidth()].isWalkable())
//                    || (!map.getMapArray()[tileIndexY = (int) (y - speed) / map.getTileHeight()][tileIndexX = (int) (x + width) / map.getTileWidth()].isWalkable())) {
//                y = tileIndexY * map.getTileHeight() + (map.getTileHeight() + 1);
//            } else {
//                y -= speed;
//                inMovement = true;
//            }
//        } else if (Gdx.input.isKeyPressed(Input.Keys.S)) {
//            if ((!map.getMapArray()[tileIndexY = (int) (y + speed + height) / map.getTileHeight()][tileIndexX = (int) x / map.getTileWidth()].isWalkable())
//                    || (!map.getMapArray()[tileIndexY = (int) (y + speed + height) / map.getTileHeight()][tileIndexX = (int) (x + width) / map.getTileWidth()].isWalkable())) {
//                y = tileIndexY * map.getTileHeight() - (height + 1);
//            } else {
//                y += speed;
//                inMovement = true;
//            }
//        }
//        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
//            if ((!map.getMapArray()[tileIndexY = (int) y / map.getTileHeight()][tileIndexX = (int) (x + speed + width) / map.getTileWidth()].isWalkable())
//                    || (!map.getMapArray()[tileIndexY = (int) (y + height) / map.getTileHeight()][tileIndexX = (int) (x + speed + width) / map.getTileWidth()].isWalkable())) {
//                x = tileIndexX * map.getTileWidth() - (width + 1);
//            } else {
//                x += speed;
//                inMovement = true;
//            }
//        } else if (Gdx.input.isKeyPressed(Input.Keys.A)) {
//            if ((!map.getMapArray()[tileIndexY = (int) y / map.getTileHeight()][tileIndexX = (int) (x - speed) / map.getTileWidth()].isWalkable())
//                    || (!map.getMapArray()[tileIndexY = (int) (y + height) / map.getTileHeight()][tileIndexX = (int) (x - speed) / map.getTileWidth()].isWalkable())) {
//                x = tileIndexX * map.getTileWidth() + (map.getTileWidth() + 1);
//            } else {
//                x -= speed;
//                inMovement = true;
//            }
//        }

        // Playing player movement sound
        timetoNextSound += Gdx.graphics.getDeltaTime();
        if (inMovement) {
            if (timetoNextSound >= soundLength) {
                stepSound.play(1);
                timetoNextSound = 0;
            }
        }
    }

    private void debugMove() {
        int speedMultiplier = 20;
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            y -= speed * speedMultiplier;
        } else if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            y += speed * speedMultiplier;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            x -= speed * speedMultiplier;
        } else if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            x += speed * speedMultiplier;
        }
    }

    public void render(SpriteBatch batch, OrthographicCamera camera) {
        frameStateTime += Gdx.graphics.getDeltaTime();

        // Rotate player based on mouse position
        float currentPlayerAngle = getAngleToMouse(camera);
        if (currentPlayerAngle >= 315 || currentPlayerAngle < 45) {
            if (inMovement) {
                currentFrame = animationRight.getKeyFrame(frameStateTime, true);
            } else {
                currentFrame = animationRight.getKeyFrame(0);
            }
        } else if (currentPlayerAngle >= 45 && currentPlayerAngle < 135) {
            if (inMovement) {
                currentFrame = animationDown.getKeyFrame(frameStateTime, true);
            } else {
                currentFrame = animationDown.getKeyFrame(0);
            }
        } else if (currentPlayerAngle >= 135 && currentPlayerAngle < 225) {
            if (inMovement) {
                currentFrame = animationLeft.getKeyFrame(frameStateTime, true);
            } else {
                currentFrame = animationLeft.getKeyFrame(0);
            }
        } else if (currentPlayerAngle >= 225 && currentPlayerAngle < 315) {
            if (inMovement) {
                currentFrame = animationUp.getKeyFrame(frameStateTime, true);
            } else {
                currentFrame = animationUp.getKeyFrame(0);
            }
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