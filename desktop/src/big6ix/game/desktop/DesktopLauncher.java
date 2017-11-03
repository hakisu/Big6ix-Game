package big6ix.game.desktop;

import big6ix.game.GameMain;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.tools.texturepacker.TexturePacker;
import com.badlogic.gdx.tools.texturepacker.TexturePacker.Settings;

public class DesktopLauncher {
    public static void main(String[] arg) {
        System.out.println(LwjglApplicationConfiguration.getDesktopDisplayMode().toString());

        // Combining all textures into 1(or more) texture pack atlas
        Settings texturePackerSettings = new Settings();
        texturePackerSettings.maxWidth = 1024;
        texturePackerSettings.maxHeight = 1024;
        texturePackerSettings.minWidth = 1024;
        texturePackerSettings.minHeight = 1024;
        texturePackerSettings.combineSubdirectories = true;
        texturePackerSettings.flattenPaths = true;
        TexturePacker.process(texturePackerSettings, "graphics", "graphics", "gameTexturePack");

        // Basic configuration of the game
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.setFromDisplayMode(LwjglApplicationConfiguration.getDesktopDisplayMode());
        config.title = "Game-No title yet";
        config.fullscreen = true;
        config.foregroundFPS = 0;
        config.backgroundFPS = 0;
        config.vSyncEnabled = true;
        config.forceExit = true;

        new LwjglApplication(new GameMain(), config);
    }
}
