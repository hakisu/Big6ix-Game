package big6ix.game;

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

    public ArrayList<Room> loadRoomFiles() {
        ArrayList<Room> rooms = new ArrayList<>();

        try (DirectoryStream<Path> roomsFilePaths = Files.newDirectoryStream(Paths.get(roomsDirectoryPath), "*.room")) {
            for (Path currentPath : roomsFilePaths) {
                String currentFileContent = new String(Files.readAllBytes(currentPath));
                Scanner scanner = new Scanner(currentFileContent);

                // Get the number of columns and rows in the current room
                int columnsAmount = scanner.nextInt();
                int rowsAmount = scanner.nextInt();
                TileType[][] currentRoomArray = new TileType[rowsAmount][columnsAmount];

                for (int i = 0; i < rowsAmount; ++i) {
                    for (int j = 0; j < columnsAmount; ++j) {
                        currentRoomArray[i][j] = TileType.convertFromInt(scanner.nextInt());
                    }
                }
                rooms.add(new Room(currentRoomArray, columnsAmount, rowsAmount));
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        }

        return rooms;
    }
}