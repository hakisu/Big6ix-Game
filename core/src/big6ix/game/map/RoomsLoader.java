package big6ix.game.map;

import big6ix.game.TileType;
import big6ix.game.utility.Pair;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;

public class RoomsLoader {

    private String roomsDirectoryPath;

    public RoomsLoader(String roomsDirectoryPath) {
        this.roomsDirectoryPath = roomsDirectoryPath;
    }

    public ArrayList<RoomShape> loadRoomFiles() {
        ArrayList<RoomShape> roomShapes = new ArrayList<>();

        try (DirectoryStream<Path> roomsFilePaths = Files.newDirectoryStream(Paths.get(roomsDirectoryPath), "*.room")) {
            for (Path currentPath : roomsFilePaths) {
                String currentFileContent = new String(Files.readAllBytes(currentPath));
                Scanner scanner = new Scanner(currentFileContent);

                // Get the number of columns and rows in the current room
                int columnsAmount = scanner.nextInt();
                int rowsAmount = scanner.nextInt();
                TileType[][] currentRoomArray = new TileType[rowsAmount][columnsAmount];
                ArrayList<Pair> availableDoorsTileIndices = new ArrayList();

                // Change int numbers in scanner input to TileTypes
                for (int i = 0; i < rowsAmount; ++i) {
                    for (int j = 0; j < columnsAmount; ++j) {
                        currentRoomArray[i][j] = TileType.convertFromInt(scanner.nextInt());
                    }
                }

                while (scanner.hasNextInt()) {
                    int indexX = scanner.nextInt();
                    int indexY = scanner.nextInt();
                    availableDoorsTileIndices.add(new Pair(indexX, indexY));
                }

                if (availableDoorsTileIndices.size() == 0) {
                    throw new DoorNotFoundException("Each RoomShape has to have at least 1 available door!(");
                }

                roomShapes.add(new RoomShape(currentRoomArray, columnsAmount, rowsAmount, availableDoorsTileIndices));
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        }

        return roomShapes;
    }
}