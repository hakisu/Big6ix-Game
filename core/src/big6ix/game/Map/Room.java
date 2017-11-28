package big6ix.game.Map;

import big6ix.game.Constants;
import big6ix.game.TileType;

import java.util.ArrayList;

public class Room {

    private RoomShape roomShape;
    private int x;
    private int y;

    public Room(RoomShape roomShape) {
        this.roomShape = roomShape;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public TileType[][] getRoomArray() {
        return roomShape.getRoomArray();
    }

    public int getRowsAmount() {
        return roomShape.getRowsAmount();
    }

    public int getColumnsAmount() {
        return roomShape.getColumnsAmount();
    }

    public ArrayList<Integer> calculateWalkableTilesUniqueIds() {
        ArrayList<Integer> tilesUniqueIds = new ArrayList<>();
        for (int i = 0; i < roomShape.getRowsAmount(); ++i) {
            for (int j = 0; j < roomShape.getColumnsAmount(); ++j) {
                if (roomShape.getRoomArray()[i][j].isWalkable()) {
                    tilesUniqueIds.add((i + y) * Constants.MAP_COLUMNS_AMOUNT + (j + x));
                }
            }
        }

        return tilesUniqueIds;
    }
}
