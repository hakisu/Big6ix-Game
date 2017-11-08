package big6ix.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Player {
    private float x;
    private float y;
    private int width;
    private int height;
    private float speed;

    private TextureRegion currentFrame;
    private float frameTime = 0;
    private Animation<TextureRegion> animation;

    public Player() {
        animation = new Animation<TextureRegion>(0.5f, GameMain.gameAtlas.findRegions(Constants.ATLAS_PLAYER_NAME), Animation.PlayMode.LOOP);

        this.x = Constants.PLAYER_STARTING_X;
        this.y = Constants.PLAYER_STARTING_Y;
        this.width = Constants.PLAYER_WIDTH;
        this.height = Constants.PLAYER_HEIGHT;
        this.speed = Constants.PLAYER_SPEED;
    }

    public void update(Map map) {
        // movement commands
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

    public void render(SpriteBatch batch) {
        frameTime += Gdx.graphics.getDeltaTime();
        currentFrame = animation.getKeyFrame(frameTime, true);
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
}