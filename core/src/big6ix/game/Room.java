package big6ix.game;

import java.util.ArrayList;

public class Room {

    private TileType roomArray[][];
    private int x;
    private int y;
    private int rowsAmount;
    private int columnsAmount;

    public Room(TileType roomArray[][], int columnsAmount, int rowsAmount) {
        this.roomArray = roomArray;
        this.columnsAmount = columnsAmount;
        this.rowsAmount = rowsAmount;
    }

    public TileType[][] getRoomArray() {
        return roomArray;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getRowsAmount() {
        return rowsAmount;
    }

    public int getColumnsAmount() {
        return columnsAmount;
    }

    public ArrayList<Integer> calculateWalkableTilesUniqueIds() {
        ArrayList<Integer> tilesUniqueIds = new ArrayList<>();
        for (int i = 0; i < rowsAmount; ++i) {
            for (int j = 0; j < columnsAmount; ++j) {
                if (roomArray[i][j].isWalkable()) {
                    tilesUniqueIds.add((i + y) * Constants.MAP_COLUMNS_AMOUNT + (j + x));
                }
            }
        }

        return tilesUniqueIds;
    }
}
