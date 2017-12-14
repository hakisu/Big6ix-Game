package big6ix.game.map;

import big6ix.game.Constants;
import big6ix.game.TileType;
import big6ix.game.utility.Pair;

import java.util.ArrayList;

public class Room {

    private RoomShape roomShape;
    private int x;
    private int y;
    private boolean completed;
    private ArrayList<Pair> doors;

    public Room(RoomShape roomShape) {
        this.roomShape = roomShape;
        this.doors = new ArrayList<>();
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

    public ArrayList<Pair> getDoors() {
        return doors;
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

    public void addDoor(Pair door) {
        this.doors.add(door);
    }

    public ArrayList<Integer> calculateIndicesOfWalkableTiles() {
        ArrayList<Integer> walkableTilesIndices = new ArrayList<>();
        for (int i = 0; i < roomShape.getRowsAmount(); ++i) {
            for (int j = 0; j < roomShape.getColumnsAmount(); ++j) {
                if (roomShape.getRoomArray()[i][j].isWalkable()) {
                    walkableTilesIndices.add((i + y) * Constants.MAP_COLUMNS_AMOUNT + (j + x));
                }
            }
        }

        return walkableTilesIndices;
    }
}
