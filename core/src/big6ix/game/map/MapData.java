package big6ix.game.map;

import big6ix.game.Tile;

import java.io.Serializable;
import java.util.ArrayList;

public class MapData implements Serializable {

    private Tile[][] mapArray;
    private int rowsAmount;
    private int columnsAmount;
    private ArrayList<Room> rooms;

    public MapData(Tile[][] mapArray, int rowsAmount, int columnsAmount, ArrayList<Room> rooms) {
        this.mapArray = mapArray;
        this.rowsAmount = rowsAmount;
        this.columnsAmount = columnsAmount;
        this.rooms = rooms;
    }

    public Tile[][] getMapArray() {
        return mapArray;
    }

    public int getRowsAmount() {
        return rowsAmount;
    }

    public int getColumnsAmount() {
        return columnsAmount;
    }

    public ArrayList<Room> getRooms() {
        return rooms;
    }
}