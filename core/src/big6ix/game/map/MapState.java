package big6ix.game.map;

import big6ix.game.*;
import big6ix.game.utility.Pair;

import java.util.List;

public class MapState {

    private boolean inFight;
    private MapData mapData;
    private Room currentOccupiedRoom;
    private int currentOccupiedRoomIndex;
    private boolean[] roomsCompletionStatuses;

    public MapState(MapData mapData) {
        this.inFight = false;
        this.mapData = mapData;
        currentOccupiedRoom = null;
        currentOccupiedRoomIndex = 0;
        roomsCompletionStatuses = new boolean[mapData.getRooms().size()];
    }

    public void update(ManagerEnemies managerEnemies, Player player) {
        if (inFight == true) {
            if (checkIfAllEnemiesEliminated(managerEnemies) == true) {
                roomsCompletionStatuses[currentOccupiedRoomIndex] = true;
                openDoors();
                inFight = false;
            }
        } else {
            calculateCurrentOccupiedRoom(player);
            if (roomsCompletionStatuses[currentOccupiedRoomIndex] == false) {
                closeDoors();
                inFight = true;
                // testing spawing enemy
                Pair enemyIndices = currentOccupiedRoom.getRandomWalkableTileIndices();
                managerEnemies.addEnemy(new EnemyShooter(enemyIndices.getIndexX() * Constants.TILE_WIDTH, enemyIndices.getIndexY() * Constants.TILE_HEIGHT));
            }
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

    private void calculateCurrentOccupiedRoom(Player player) {
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
                if (playerIndexX != (closestDoorToPlayer.getIndexX() + positionX) || (playerIndexY != closestDoorToPlayer.getIndexY() + positionY)) {
                    this.currentOccupiedRoom = currentRoom;
                    this.currentOccupiedRoomIndex = currentRoomIndex;
                    return;
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
}