package big6ix.game.Map;

import big6ix.game.TileType;
import big6ix.game.Utility.Pair;

import java.util.ArrayList;

public class RoomShape {

    private TileType roomArray[][];
    private int rowsAmount;
    private int columnsAmount;
    private ArrayList<Pair> availableDoorsTileIndices;

    public RoomShape(TileType roomArray[][], int columnsAmount, int rowsAmount, ArrayList<Pair> availableDoorsTileIndices) {
        this.roomArray = roomArray;
        this.columnsAmount = columnsAmount;
        this.rowsAmount = rowsAmount;
        this.availableDoorsTileIndices = availableDoorsTileIndices;
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
}
