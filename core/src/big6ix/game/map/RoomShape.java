package big6ix.game.map;

import big6ix.game.TileType;
import big6ix.game.utility.Pair;

import java.util.ArrayList;

public class RoomShape {

    private TileType[][] roomArray;
    private int rowsAmount;
    private int columnsAmount;
    private ArrayList<Pair> availableDoorsTileIndices;

    public RoomShape(TileType roomArray[][], int columnsAmount, int rowsAmount, ArrayList<Pair> availableDoorsTileIndices) {
        this.roomArray = roomArray;
        this.columnsAmount = columnsAmount;
        this.rowsAmount = rowsAmount;
        this.availableDoorsTileIndices = availableDoorsTileIndices;
    }

    @SuppressWarnings("IncompleteCopyConstructor")
    public RoomShape(RoomShape other) {
        this.rowsAmount = other.rowsAmount;
        this.columnsAmount = other.columnsAmount;

        this.roomArray = new TileType[other.rowsAmount][other.columnsAmount];
        for (int i = 0; i < rowsAmount; i++) {
            for (int j = 0; j < columnsAmount; j++) {
                this.roomArray[i][j] = other.roomArray[i][j];
            }
        }

        this.availableDoorsTileIndices = new ArrayList<>(other.availableDoorsTileIndices.size());
        for (Pair currentPair : other.availableDoorsTileIndices) {
            this.availableDoorsTileIndices.add(new Pair(currentPair));
        }
    }

    public TileType[][] getRoomArray() {
        return roomArray;
    }

    public int getRowsAmount() {
        return rowsAmount;
    }

    public int getColumnsAmount() {
        return columnsAmount;
    }

    public ArrayList<Pair> getAvailableDoorsTileIndices() {
        return availableDoorsTileIndices;
    }

    public void rotateClockwise() {
        TileType[][] rotatedRoomArray = new TileType[columnsAmount][rowsAmount];

        // Rotate roomArray Clockwise
        for (int i = 0; i < rowsAmount; i++) {
            for (int j = 0; j < columnsAmount; j++) {
                rotatedRoomArray[j][rowsAmount - i - 1] = roomArray[i][j];
            }
        }

        // Update availableDoorsTileIndices to match roomArray rotation
        for (Pair currentTileIndices : availableDoorsTileIndices) {
            int oldIndexY = currentTileIndices.getIndexY();
            int oldIndexX = currentTileIndices.getIndexX();

            currentTileIndices.setIndexY(oldIndexX);
            currentTileIndices.setIndexX(rowsAmount - oldIndexY - 1);
        }
        this.roomArray = rotatedRoomArray;

        // Update rowsAmount and columnsAmount of this RoomShape
        int oldRowsAmount = rowsAmount;
        this.rowsAmount = columnsAmount;
        this.columnsAmount = oldRowsAmount;
    }

    public Direction calculateDoorDirection(Pair door) {
        if (this.availableDoorsTileIndices.contains(door)) {
            int indexOfDoor = this.availableDoorsTileIndices.indexOf(door);
            if (this.availableDoorsTileIndices.get(indexOfDoor).getIndexY() == 0) {
                return Direction.NORTH;
            }
            if (this.availableDoorsTileIndices.get(indexOfDoor).getIndexY() == this.rowsAmount - 1) {
                return Direction.SOUTH;
            }
            if (this.availableDoorsTileIndices.get(indexOfDoor).getIndexX() == 0) {
                return Direction.WEST;
            }
            if (this.availableDoorsTileIndices.get(indexOfDoor).getIndexX() == this.columnsAmount - 1) {
                return Direction.EAST;
            }
            throw new DoorInWrongPositionException("The door in availableDoorsTileIndices is not located on the border of roomArray.");
        } else {
            throw new DoorNotFoundException("There is no door in this RoomShape which have indices equal to Pair door.");
        }
    }
}