package big6ix.game.Map;

import big6ix.game.Constants;
import big6ix.game.Tile;
import big6ix.game.TileType;

import java.util.ArrayList;

public class MapCreator {

    private int currentUniqueIdForWalkableTiles = 0;
    private ArrayList<RoomShape> roomShapes;

    public MapCreator() {
        // Load all types of rooms from "*.room" files into ArrayList roomShapes
        RoomsLoader roomsLoader = new RoomsLoader(Constants.ROOMS_DIRECTORY_PATH);
        roomShapes = roomsLoader.loadRoomFiles();
    }

    public void generateMap(Tile[][] mapArray, int rowsAmount, int columnsAmount, ArrayList<Room> rooms) {
        // Initialize whole map with empty tiles (tiles that have their tileType set to TileType.EMPTY)
        // Give each tile uniqueIdForWalkableTiles which is necessary for IndexedAStarPathFinder
        for (int i = 0; i < rowsAmount; ++i) {
            for (int j = 0; j < columnsAmount; ++j) {
                mapArray[i][j] = new Tile(j + i * columnsAmount, TileType.EMPTY);
            }
        }

        for (int i = 0; i < roomShapes.get(0).getRowsAmount(); i++) {
            for (int j = 0; j < roomShapes.get(0).getColumnsAmount(); j++) {
                TileType currentTileType = roomShapes.get(0).getRoomArray()[i][j];
                mapArray[i][j].setTileType(currentTileType);
                if (currentTileType.isWalkable()) {
                    mapArray[i][j].setUniqueIdForWalkableTiles(currentUniqueIdForWalkableTiles++);
                }
            }
        }
        rooms.add(new Room(roomShapes.get(0)));
        rooms.get(0).setX(0);
        rooms.get(0).setY(0);

//        for (int temp : rooms.get(0).calculateWalkableTilesUniqueIds()) {
//            System.out.println(temp);
//        }

//        int posX, posY;
//        Random random = new Random();
//        posX = random.nextInt(100 - 40 + 1) + 40;
//        posY = random.nextInt(100 - 40 + 1) + 40;
//
//        for (int i = 0; i < roomShapes.get(1).getRowsAmount(); ++i) {
//            for (int j = 0; j < roomShapes.get(1).getColumnsAmount(); ++j) {
//                mapArray[i + posY][j + posX].setType(roomShapes.get(1).getRoomArray()[i][j]);
//            }
//        }
//
//        int startingPosX = 26, startingPosY = 15;
//        int j = startingPosX;
//        while (j <= posX + 1) {
//            mapArray[startingPosY - 1][j].setType(1);
//            mapArray[startingPosY][j].setType(0);
//            mapArray[startingPosY + 1][j].setType(0);
//            mapArray[startingPosY + 2][j].setType(1);
//            ++j;
//        }
//
//        mapArray[startingPosY - 1][j].setType(1);
//        mapArray[startingPosY][j].setType(0);
//        mapArray[startingPosY + 1][j].setType(0);
//        mapArray[startingPosY + 2][j].setType(0);
//
//        mapArray[startingPosY - 1][j + 1].setType(1);
//        mapArray[startingPosY][j + 1].setType(1);
//        mapArray[startingPosY + 1][j + 1].setType(1);
//        mapArray[startingPosY + 2][j + 1].setType(1);
//
//        int i = startingPosY + 2;
//
//        while (i < posY + 1) {
//            mapArray[i][j - 1].setType(0);
//            mapArray[i][j].setType(0);
//            mapArray[i][j + 1].setType(1);
//            mapArray[i][j - 2].setType(1);
//            ++i;
//        }
    }

    public int getCurrentUniqueIdForWalkableTiles() {
        return currentUniqueIdForWalkableTiles;
    }
}
