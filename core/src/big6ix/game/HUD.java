package big6ix.game;

import big6ix.game.map.Map;
import big6ix.game.screens.GameMain;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;


public class HUD {

    private static final String ATLAS_LIFE_ICON_NAME = "life_icon";
    private static final int LIFE_ICON_WIDTH = 32;
    private static final int LIFE_ICON_HEIGHT = 32;
    private static final int LIFE_ICON_OFFSET = 10;
    private static final int VERTICAL_OFFSET = 5;

    private TextureAtlas.AtlasRegion lifeAtlasRegion;

    public HUD() {
        lifeAtlasRegion = GameMain.getGameAtlas().findRegion(ATLAS_LIFE_ICON_NAME);
    }

    public void render(GameMain gameMain, Player player, Map map) {
        for (int i = 0; i < player.getHealth(); i++) {
            gameMain.batch.draw(lifeAtlasRegion, i * (LIFE_ICON_WIDTH + LIFE_ICON_OFFSET), 0, LIFE_ICON_WIDTH, LIFE_ICON_HEIGHT);
        }

        int passedCount = 0;
        for (int i = 0; i < map.getRoomsCompletionStatuses().length; i++) {
            if (map.getRoomsCompletionStatuses()[i])
                passedCount++;
        }
        // Green color
        gameMain.getFont().setColor(new Color(0, 0.902f, 0.251f, 1));
        gameMain.getFont().draw(gameMain.batch, "Rooms completed: " + passedCount, 0, LIFE_ICON_HEIGHT + VERTICAL_OFFSET);

        // Red color
        gameMain.getFont().setColor(Color.RED);
        gameMain.getFont().draw(gameMain.batch, "Rooms left: " + (map.getRooms().size() - passedCount), 0, LIFE_ICON_HEIGHT + GameMain.FONT_SIZE + 2 * VERTICAL_OFFSET);
    }
}