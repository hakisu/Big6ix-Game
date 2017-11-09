package big6ix.game;

public class Room {
    private int roomArray[][];

    public Room(int roomArray[][]) {
        this.roomArray = roomArray;
    }

    public int[][] getRoomArray() {
        return roomArray;
    }

    public int getRowsAmount() {
        return roomArray.length;
    }

    public int getColumnsAmount() {
        return roomArray[0].length;
    }
}
