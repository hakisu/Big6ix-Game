package big6ix.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class GameMain extends ApplicationAdapter {
	private SpriteBatch batch;
	private Texture img;
	private long oldTime;
	private long ticksPerSecond = 60;
	private long timeAccumulator = 0;
//	frameTime represents time reserved for one frame in nanoseconds
	private long frameTime = 1000000000 / ticksPerSecond;

	@Override
	public void create () {
		System.out.println("from create start");
		batch = new SpriteBatch();
		img = new Texture("badlogic.jpg");
		oldTime = System.nanoTime();
	}

	@Override
	public void render () {
//        System.out.println(String.format("%12f",Gdx.graphics.getRawDeltaTime()));
        long newTime = System.nanoTime();
        long timeDifference = newTime - oldTime;
        oldTime = newTime;
        timeAccumulator += timeDifference;

        if(timeAccumulator >= 1000000000) {
            System.out.println(timeDifference / 1000000.0);
            timeAccumulator = 0;
        }

        updateGraphics();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		img.dispose();
	}

	private void updateGraphics() {
	    Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        batch.draw(img, 0, 0);
        batch.end();
    }

    private void updatePhysics() {

    }
}
