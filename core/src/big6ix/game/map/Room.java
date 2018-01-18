package big6ix.game.map;

import big6ix.game.utility.Pair;
import big6ix.game.utility.Utilities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Room implements Serializable {

    private RoomShape roomShape;
    private int x;
    private int y;
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

    public int getRowsAmount() {
        return roomShape.getRowsAmount();
    }

    public int getColumnsAmount() {
        return roomShape.getColumnsAmount();
    }

    public void addDoor(Pair door) {
        this.doors.add(door);
    }

    public ArrayList<Pair> calculateIndicesOfWalkableTiles() {
        ArrayList<Pair> walkableTilesIndices = new ArrayList<>();
        for (int i = 0; i < roomShape.getRowsAmount(); ++i) {
            for (int j = 0; j < roomShape.getColumnsAmount(); ++j) {
                if (roomShape.getRoomArray()[i][j].isWalkable()) {
                    walkableTilesIndices.add(new Pair(j + this.x, i + this.y));
                }
            }
        }

        return walkableTilesIndices;
    }

    public Pair getRandomWalkableTileIndices() {
        List<Pair> walkableTilesIndices = calculateIndicesOfWalkableTiles();
        int randomPositionInListOfWalkableTiles = Utilities.generateRandomInt(0, walkableTilesIndices.size() - 1);

        return walkableTilesIndices.get(randomPositionInListOfWalkableTiles);
    }
}
