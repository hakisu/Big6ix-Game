package big6ix.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.sun.xml.internal.bind.v2.model.annotation.RuntimeInlineAnnotationReader;

import java.util.Random;

public class Bullet {
    private float x;
    private float y;
    private float speedX;
    private float speedY;
    private float speed = (float) Math.random() * 4;
    private Texture texture;
    public Sprite sprite;

    public Bullet(float sourceX, float sourceY, float targetX, float targetY) {
        // Calculations to receive speed of bullet in X and Y axis
        float distance = (float) Math.sqrt((targetX - sourceX) * (targetX - sourceX) + (targetY - sourceY) * (targetY - sourceY));
        this.speedX = (targetX - sourceX) / distance * speed;
        this.speedY = (targetY - sourceY) / distance * speed;

        // Setting starting position of bullet
        this.x = sourceX;
        this.y = sourceY;

        // temp
        texture = new Texture("bullet.png");
        sprite = new Sprite(texture);
        sprite.flip(false, true);
    }

    public void update() {
        this.x += speedX;
        this.y += speedY;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public void setX(float x) {
        this.x = x;
    }

    void setY(float y) {
        this.y = y;
    }
}
