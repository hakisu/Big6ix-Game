package big6ix.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import big6ix.game.GameMain;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.fullscreen = false;
        config.foregroundFPS = 0;
        config.backgroundFPS = 0;
        config.vSyncEnabled = false;
        config.x = -1;
        config.y = -1;
        config.width = 800;
        config.height = 400;
        config.useGL30 = true;
        config.title = "Game-No title yet";

		new LwjglApplication(new GameMain(), config);
	}
}
