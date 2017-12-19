package big6ix.game;

import big6ix.game.map.Map;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

public class Player {

    private final int HIT_BOX_WIDTH = 35;
    private final int HIT_BOX_HEIGHT = 60;
    private final int HIT_BOX_X = 14;
    private final int HIT_BOX_Y = 4;
    private final int GRAPHICAL_WIDTH = 64;
    private final int GRAPHICAL_HEIGHT = 64;
    private final int SPEED = 6;

    private float speed;
    private Rectangle hitBox;
    private boolean inMovement = false;
    private TextureRegion currentFrame;
    private float frameStateTime = 0;
    private Animation<TextureRegion> animationDown;
    private Animation<TextureRegion> animationLeft;
    private Animation<TextureRegion> animationUp;
    private Animation<TextureRegion> animationRight;
    private Sound stepSound;
    private float soundLength = 0.35f;
    private float timetoNextSound = soundLength;

    public Player(int tileIndexX, int tileIndexY) {
        animationDown = new Animation<TextureRegion>(Constants.PLAYER_FRAME_DURATION, GameMain.getGameAtlas().findRegions(Constants.ATLAS_PLAYER_DOWN_NAME), Animation.PlayMode.LOOP);
        animationLeft = new Animation<TextureRegion>(Constants.PLAYER_FRAME_DURATION, GameMain.getGameAtlas().findRegions(Constants.ATLAS_PLAYER_LEFT_NAME), Animation.PlayMode.LOOP);
        animationUp = new Animation<TextureRegion>(Constants.PLAYER_FRAME_DURATION, GameMain.getGameAtlas().findRegions(Constants.ATLAS_PLAYER_UP_NAME), Animation.PlayMode.LOOP);
        animationRight = new Animation<TextureRegion>(Constants.PLAYER_FRAME_DURATION, GameMain.getGameAtlas().findRegions(Constants.ATLAS_PLAYER_RIGHT_NAME), Animation.PlayMode.LOOP);

        // Set correct hitbox for player matching player's graphic representation
        this.hitBox = new Rectangle();
        this.hitBox.x = tileIndexX * Constants.TILE_WIDTH;
        this.hitBox.y = tileIndexY * Constants.TILE_HEIGHT;
        this.hitBox.width = HIT_BOX_WIDTH;
        this.hitBox.height = HIT_BOX_HEIGHT;

        this.speed = SPEED;
        stepSound = Gdx.audio.newSound(Gdx.files.internal("sounds/step23.mp3"));
    }

    public void update(Map map) {
        // inMovement is used to determine if running animation should be used for player
        inMovement = false;
//        debugMove();

        int tileIndexY, tileIndexX;
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            if ((!map.getMapArray()[tileIndexY = (int) (hitBox.y - speed) / map.getTileHeight()][tileIndexX = (int) hitBox.x / map.getTileWidth()].isWalkable())
                    || (!map.getMapArray()[tileIndexY = (int) (hitBox.y - speed) / map.getTileHeight()][tileIndexX = (int) (hitBox.x + hitBox.width) / map.getTileWidth()].isWalkable())) {
                hitBox.y = tileIndexY * map.getTileHeight() + (map.getTileHeight() + 1);
            } else {
                hitBox.y -= speed;
                inMovement = true;
            }
        } else if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            if ((!map.getMapArray()[tileIndexY = (int) (hitBox.y + speed + hitBox.height) / map.getTileHeight()][tileIndexX = (int) hitBox.x / map.getTileWidth()].isWalkable())
                    || (!map.getMapArray()[tileIndexY = (int) (hitBox.y + speed + hitBox.height) / map.getTileHeight()][tileIndexX = (int) (hitBox.x + hitBox.width) / map.getTileWidth()].isWalkable())) {
                hitBox.y = tileIndexY * map.getTileHeight() - (hitBox.height + 1);
            } else {
                hitBox.y += speed;
                inMovement = true;
            }
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            if ((!map.getMapArray()[tileIndexY = (int) hitBox.y / map.getTileHeight()][tileIndexX = (int) (hitBox.x + speed + hitBox.width) / map.getTileWidth()].isWalkable())
                    || (!map.getMapArray()[tileIndexY = (int) (hitBox.y + hitBox.height) / map.getTileHeight()][tileIndexX = (int) (hitBox.x + speed + hitBox.width) / map.getTileWidth()].isWalkable())) {
                hitBox.x = tileIndexX * map.getTileWidth() - (hitBox.width + 1);
            } else {
                hitBox.x += speed;
                inMovement = true;
            }
        } else if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            if ((!map.getMapArray()[tileIndexY = (int) hitBox.y / map.getTileHeight()][tileIndexX = (int) (hitBox.x - speed) / map.getTileWidth()].isWalkable())
                    || (!map.getMapArray()[tileIndexY = (int) (hitBox.y + hitBox.height) / map.getTileHeight()][tileIndexX = (int) (hitBox.x - speed) / map.getTileWidth()].isWalkable())) {
                hitBox.x = tileIndexX * map.getTileWidth() + (map.getTileWidth() + 1);
            } else {
                hitBox.x -= speed;
                inMovement = true;
            }
        }

        // Playing player movement sound
        timetoNextSound += Gdx.graphics.getDeltaTime();
        if (inMovement) {
            if (timetoNextSound >= soundLength) {
                stepSound.play(0.05f);
                timetoNextSound = 0;
            }
        }
    }

    private void debugMove() {
        int speedMultiplier = 20;
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            hitBox.y -= speed * speedMultiplier;
        } else if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            hitBox.y += speed * speedMultiplier;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            hitBox.x -= speed * speedMultiplier;
        } else if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            hitBox.x += speed * speedMultiplier;
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

        batch.draw(this.currentFrame, this.hitBox.x - HIT_BOX_X, this.hitBox.y - HIT_BOX_Y, GRAPHICAL_WIDTH, GRAPHICAL_HEIGHT);
    }

    public void shoot() {
        // Na pozniej do zrobienia
    }

    public float getX() {
        return hitBox.x;
    }

    public float getY() {
        return hitBox.y;
    }

    public int getWidth() {
        return (int) hitBox.width;
    }

    public int getHeight() {
        return (int) hitBox.height;
    }

    public int calculateIndexX() {
        return (int) (this.hitBox.x / Constants.TILE_WIDTH);
    }

    public int calculateIndexY() {
        return (int) (this.hitBox.y / Constants.TILE_HEIGHT);
    }

    private float getAngleToMouse(OrthographicCamera camera) {
        Vector3 mousePositionInGameWorld = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        camera.unproject(mousePositionInGameWorld);

        float angle = (float) Math.toDegrees(Math.atan2(mousePositionInGameWorld.y - (this.hitBox.y + this.hitBox.height / 2), mousePositionInGameWorld.x - (this.hitBox.x + this.hitBox.width / 2)));
        if (angle < 0) {
            angle += 360;
        }
        return angle;
    }
}