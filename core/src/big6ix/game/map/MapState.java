package big6ix.game.map;

import big6ix.game.Player;
import big6ix.game.TileType;
import big6ix.game.enemies.EnemyCreator;
import big6ix.game.enemies.ManagerEnemies;
import big6ix.game.screens.GameMain;
import big6ix.game.utility.Pair;
import big6ix.game.utility.Utilities;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

import java.io.Serializable;
import java.util.List;

public class MapState implements Serializable {

    private static final int ENEMIES_PER_ROOM_MIN = 4;
    private static final int ENEMIES_PER_ROOM_MAX = 7;
    private static final int DISTANCE_FOR_DOOR_CLOSING = 2;
    private static final String SOUND_CLOSING_DOORS_PATH = "sounds/closing_doors.wav";
    private static final String SOUND_ROOM_FINISHED_PATH = "sounds/room_finished.wav";

    private boolean inFight;
    private MapData mapData;
    private Room currentOccupiedRoom;
    private int currentOccupiedRoomIndex;
    private boolean[] roomsCompletionStatuses;

    // Sounds
    private transient Sound closingDoorsSound;
    private transient Sound roomFinishedSound;

    MapState(MapData mapData) {
        this.inFight = false;
        this.mapData = mapData;
        currentOccupiedRoom = null;
        currentOccupiedRoomIndex = 0;
        roomsCompletionStatuses = new boolean[mapData.getRooms().size()];

        initializeSounds();
    }

    public Room getCurrentOccupiedRoom() {
        return currentOccupiedRoom;
    }

    public boolean[] getRoomCompletionStatus() {
        return roomsCompletionStatuses;
    }

    /**
     * Sounds are assigned in separate method that can be used in constructor
     * or after reading serialized MapState
     */
    public void initializeSounds() {
        closingDoorsSound = Gdx.audio.newSound(Gdx.files.internal(SOUND_CLOSING_DOORS_PATH));
        roomFinishedSound = Gdx.audio.newSound(Gdx.files.internal(SOUND_ROOM_FINISHED_PATH));
    }

    public void update(ManagerEnemies managerEnemies, Player player, GameMain gameMain) {
        if (inFight) {
            if (checkIfAllEnemiesEliminated(managerEnemies)) {
                roomsCompletionStatuses[currentOccupiedRoomIndex] = true;
                openDoors();
                roomFinishedSound.play(GameMain.getPreferences().getSoundEffectsVolume());
                inFight = false;
            }
        } else {
            calculateAndChangeCurrentOccupiedRoom(player);
            if (currentOccupiedRoom != null && !roomsCompletionStatuses[currentOccupiedRoomIndex]) {
                closeDoors();
                closingDoorsSound.play(GameMain.getPreferences().getSoundEffectsVolume());

                inFight = true;
                int numberOfEnemies = Utilities.generateRandomInt(ENEMIES_PER_ROOM_MIN, ENEMIES_PER_ROOM_MAX);
                int currentNumberOfEnemies = 0;
                while (currentNumberOfEnemies < numberOfEnemies) {
                    Pair enemyIndices = currentOccupiedRoom.getRandomWalkableTileIndices();
                    managerEnemies.addEnemy(new EnemyCreator(enemyIndices.getIndexX() * Map.TILE_WIDTH, enemyIndices.getIndexY() * Map.TILE_HEIGHT));
                    currentNumberOfEnemies++;
                }
            }
        }
        if (checkIfLevelIsCompleted()) {
            gameMain.getScreenGame().executeGameOver();
        }
    }

    private boolean checkIfAllEnemiesEliminated(ManagerEnemies managerEnemies) {
        return managerEnemies.getNumberOfAllEnemies() == 0;
    }

    private void closeDoors() {
        for (Pair currentDoorIndices : currentOccupiedRoom.getDoors()) {
            mapData.getMapArray()[currentDoorIndices.getIndexY() + currentOccupiedRoom.getY()][currentDoorIndices.getIndexX() + currentOccupiedRoom.getX()].setTileType(TileType.DOOR);
        }
    }

    private void openDoors() {
        for (Pair currentDoorIndices : currentOccupiedRoom.getDoors()) {
            mapData.getMapArray()[currentDoorIndices.getIndexY() + currentOccupiedRoom.getY()][currentDoorIndices.getIndexX() + currentOccupiedRoom.getX()].setTileType(TileType.FLOOR_BASIC_0);
        }
    }

    private void calculateAndChangeCurrentOccupiedRoom(Player player) {
        int playerIndexX = player.calculateIndexX();
        int playerIndexY = player.calculateIndexY();

        int currentRoomIndex = 0;
        for (Room currentRoom : mapData.getRooms()) {
            int positionX = currentRoom.getX();
            int positionY = currentRoom.getY();
            int width = currentRoom.getColumnsAmount();
            int height = currentRoom.getRowsAmount();

            if ((playerIndexX >= positionX && playerIndexX < positionX + width)
                    && (playerIndexY >= positionY && playerIndexY < positionY + height)
                    && (this.currentOccupiedRoom != currentRoom)) {

                Pair closestDoorToPlayer = calculateClosestDoorToPlayer(player, currentRoom);
                int doorMapPositionX = closestDoorToPlayer.getIndexX() + positionX;
                int doorMapPositionY = closestDoorToPlayer.getIndexY() + positionY;
                if (playerIndexX != doorMapPositionX || playerIndexY != doorMapPositionY) {
                    int distanceToDoor = calculateDistanceBetweenPositions(new Pair(playerIndexX, playerIndexY), new Pair(doorMapPositionX, doorMapPositionY));
                    if (distanceToDoor >= DISTANCE_FOR_DOOR_CLOSING) {
                        this.currentOccupiedRoom = currentRoom;
                        this.currentOccupiedRoomIndex = currentRoomIndex;
                        return;
                    }
                }
            }
            currentRoomIndex++;
        }
    }

    private Pair calculateClosestDoorToPlayer(Player player, Room room) {
        int playerIndexX = player.calculateIndexX();
        int playerIndexY = player.calculateIndexY();
        int distanceToDoor = Integer.MAX_VALUE;
        Pair currentClosestDoor = null;

        List<Pair> doors = room.getDoors();
        for (Pair currentDoor : doors) {
            int distanceHorizontal = Math.abs(currentDoor.getIndexX() + room.getX() - playerIndexX);
            int distanceVertical = Math.abs(currentDoor.getIndexY() + room.getY() - playerIndexY);
            int distance = distanceHorizontal + distanceVertical;

            if (distance < distanceToDoor) {
                distanceToDoor = distance;
                currentClosestDoor = currentDoor;
            }
        }

        return currentClosestDoor;
    }

    private int calculateDistanceBetweenPositions(Pair position1, Pair position2) {
        int distanceHorizontal = Math.abs(position1.getIndexX() - position2.getIndexX());
        int distanceVertical = Math.abs(position1.getIndexY() - position2.getIndexY());

        return distanceHorizontal + distanceVertical;
    }

    private boolean checkIfLevelIsCompleted() {
        for (boolean currentRoomCompletionStatus : roomsCompletionStatuses) {
            if (!currentRoomCompletionStatus) {
                return false;
            }
        }

        return true;
    }
}