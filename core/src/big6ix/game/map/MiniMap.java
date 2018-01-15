package big6ix.game.map;

import big6ix.game.Player;
import big6ix.game.utility.Pair;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import java.util.List;

public class MiniMap {

    public void render(ShapeRenderer shapeRenderer, List<Room> rooms, boolean[] roomsCompletionStatuses, Player player, int mapColumnsAmount, int mapRowsAmount) {
        float unitWidthValue = Map.TILE_WIDTH;
        float unitHeightValue = Map.TILE_HEIGHT;

        // Draw background
        shapeRenderer.setColor(Color.GRAY);
        shapeRenderer.rect(0, 0, mapColumnsAmount * unitWidthValue, mapRowsAmount * unitHeightValue);

        // Draw rooms on the minimap
        for (int i = 0; i < rooms.size(); ++i) {
            // Draw rooms borders
            shapeRenderer.setColor(Color.BROWN);
            shapeRenderer.rect(
                    rooms.get(i).getX() * unitWidthValue,
                    rooms.get(i).getY() * unitHeightValue,
                    rooms.get(i).getColumnsAmount() * unitWidthValue,
                    rooms.get(i).getRowsAmount() * unitHeightValue
            );

            if (roomsCompletionStatuses[i]) {
                shapeRenderer.setColor(Color.GREEN);
            } else {
                shapeRenderer.setColor(Color.RED);
            }
            shapeRenderer.rect(
                    rooms.get(i).getX() * unitWidthValue + unitWidthValue,
                    rooms.get(i).getY() * unitHeightValue + unitHeightValue,
                    (rooms.get(i).getColumnsAmount() - 2) * unitWidthValue,
                    (rooms.get(i).getRowsAmount() - 2) * unitHeightValue
            );
        }

        // Draw doors on the minimap
        shapeRenderer.setColor(Color.YELLOW);
        for (Room currentRoom : rooms) {
            for (Pair currentDoor : currentRoom.getDoors()) {
                shapeRenderer.rect(
                        (currentDoor.getIndexX() + currentRoom.getX()) * unitWidthValue,
                        (currentDoor.getIndexY() + currentRoom.getY()) * unitHeightValue,
                        unitWidthValue,
                        unitHeightValue
                );
            }
        }

        // Draw player on the minimap
        shapeRenderer.setColor(Color.BLACK);
        shapeRenderer.rect(
                (player.calculateIndexX() - 1) * unitWidthValue,
                (player.calculateIndexY() - 1) * unitHeightValue,
                unitWidthValue * 3,
                unitHeightValue * 3
        );
    }
}