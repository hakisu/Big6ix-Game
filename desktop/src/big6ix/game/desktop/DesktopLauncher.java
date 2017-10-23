package big6ix.game.desktop;

import big6ix.game.GameMain;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class DesktopLauncher {
    public static void main(String[] arg) {
        System.out.println(LwjglApplicationConfiguration.getDesktopDisplayMode().toString());

        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.setFromDisplayMode(LwjglApplicationConfiguration.getDesktopDisplayMode());

        config.title = "Game-No title yet";
        config.fullscreen = true;
        config.foregroundFPS = 0;
        config.backgroundFPS = 0;
        config.vSyncEnabled = false;
        config.forceExit = true;

        new LwjglApplication(new GameMain(), config);
    }
}
