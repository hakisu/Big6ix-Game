package big6ix.game;

import big6ix.game.map.Map;
import big6ix.game.screens.GameMain;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

import java.io.Serializable;

public class Player{

    private static final int SPEED = 6;
    private static final int INITIAL_HEALTH = 3;
    private static final int HIT_BOX_WIDTH = 35;
    private static final int HIT_BOX_HEIGHT = 60;
    private static final int HIT_BOX_X = 14;
    private static final int HIT_BOX_Y = 4;
    private static final int GRAPHICAL_WIDTH = 64;
    private static final int GRAPHICAL_HEIGHT = 64;
    private static final String RIGHT_ATLAS_NAME = "player_right";
    private static final String LEFT_ATLAS_NAME = "player_left";
    private static final String DOWN_ATLAS_NAME = "player_down";
    private static final String UP_ATLAS_NAME = "player_up";
    private static final float FRAME_DURATION = 0.1f;

    private Rectangle hitBox;
    private float speed;
    private int health;
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
        animationDown = new Animation<>(FRAME_DURATION, GameMain.getGameAtlas().findRegions(DOWN_ATLAS_NAME), Animation.PlayMode.LOOP);
        animationLeft = new Animation<>(FRAME_DURATION, GameMain.getGameAtlas().findRegions(LEFT_ATLAS_NAME), Animation.PlayMode.LOOP);
        animationUp = new Animation<>(FRAME_DURATION, GameMain.getGameAtlas().findRegions(UP_ATLAS_NAME), Animation.PlayMode.LOOP);
        animationRight = new Animation<>(FRAME_DURATION, GameMain.getGameAtlas().findRegions(RIGHT_ATLAS_NAME), Animation.PlayMode.LOOP);

        // Set correct hitbox for player matching player's graphic representation
        this.hitBox = new Rectangle();
        this.hitBox.x = tileIndexX * Map.TILE_WIDTH;
        this.hitBox.y = tileIndexY * Map.TILE_HEIGHT;
        this.hitBox.width = HIT_BOX_WIDTH;
        this.hitBox.height = HIT_BOX_HEIGHT;

        this.speed = SPEED;
        this.health = INITIAL_HEALTH;

        stepSound = Gdx.audio.newSound(Gdx.files.internal("sounds/step23.mp3"));
    }

    public Rectangle getHitBox() {
        return hitBox;
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
        return (int) (this.hitBox.x / Map.TILE_WIDTH);
    }

    public int calculateIndexY() {
        return (int) (this.hitBox.y / Map.TILE_HEIGHT);
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

    public void update(Map map, GameMain gameMain) {
        // inMovement is used to determine if running animation should be used for player
        inMovement = false;

        int tileIndexY, tileIndexX;
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            if ((!map.getMapArray()[tileIndexY = (int) (hitBox.y - speed) / Map.TILE_HEIGHT][tileIndexX = (int) hitBox.x / Map.TILE_WIDTH].isWalkable())
                    || (!map.getMapArray()[tileIndexY = (int) (hitBox.y - speed) / Map.TILE_HEIGHT][tileIndexX = (int) (hitBox.x + hitBox.width) / Map.TILE_WIDTH].isWalkable())) {
                hitBox.y = tileIndexY * Map.TILE_HEIGHT + (Map.TILE_HEIGHT + 1);
            } else {
                hitBox.y -= speed;
                inMovement = true;
            }
        } else if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            if ((!map.getMapArray()[tileIndexY = (int) (hitBox.y + speed + hitBox.height) / Map.TILE_HEIGHT][tileIndexX = (int) hitBox.x / Map.TILE_WIDTH].isWalkable())
                    || (!map.getMapArray()[tileIndexY = (int) (hitBox.y + speed + hitBox.height) / Map.TILE_HEIGHT][tileIndexX = (int) (hitBox.x + hitBox.width) / Map.TILE_WIDTH].isWalkable())) {
                hitBox.y = tileIndexY * Map.TILE_HEIGHT - (hitBox.height + 1);
            } else {
                hitBox.y += speed;
                inMovement = true;
            }
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            if ((!map.getMapArray()[tileIndexY = (int) hitBox.y / Map.TILE_HEIGHT][tileIndexX = (int) (hitBox.x + speed + hitBox.width) / Map.TILE_WIDTH].isWalkable())
                    || (!map.getMapArray()[tileIndexY = (int) (hitBox.y + hitBox.height) / Map.TILE_HEIGHT][tileIndexX = (int) (hitBox.x + speed + hitBox.width) / Map.TILE_WIDTH].isWalkable())) {
                hitBox.x = tileIndexX * Map.TILE_WIDTH - (hitBox.width + 1);
            } else {
                hitBox.x += speed;
                inMovement = true;
            }
        } else if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            if ((!map.getMapArray()[tileIndexY = (int) hitBox.y / Map.TILE_HEIGHT][tileIndexX = (int) (hitBox.x - speed) / Map.TILE_WIDTH].isWalkable())
                    || (!map.getMapArray()[tileIndexY = (int) (hitBox.y + hitBox.height) / Map.TILE_HEIGHT][tileIndexX = (int) (hitBox.x - speed) / Map.TILE_WIDTH].isWalkable())) {
                hitBox.x = tileIndexX * Map.TILE_WIDTH + (Map.TILE_WIDTH + 1);
            } else {
                hitBox.x -= speed;
                inMovement = true;
            }
        }

        // Playing player movement sound
        timetoNextSound += Gdx.graphics.getDeltaTime();
        if (inMovement) {
            if (timetoNextSound >= soundLength) {
                stepSound.play(GameMain.getPreferences().getSoundEffectsVolume());
                timetoNextSound = 0;
            }
        }

        if (!isPlayerStillAlive()) {
            gameMain.getScreenGame().exitGameScreen();
        }
    }

    public void shoot() {
        // Na pozniej do zrobienia
    }

    public void receiveDamage(int damageValue) {
        this.health -= damageValue;
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

    private boolean isPlayerStillAlive() {
        return this.health > 0;
    }
}