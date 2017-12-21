package big6ix.game.map;

import big6ix.game.Constants;
import big6ix.game.Tile;
import big6ix.game.TileType;
import big6ix.game.utility.Pair;
import big6ix.game.utility.Utilities;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.List;

public class MapCreator {

    private int currentUniqueIdForWalkableTiles = 0;
    private ArrayList<RoomShape> roomShapes;

    public MapCreator() {
        // Load all types of rooms from "*.room" files into ArrayList roomShapes
        RoomsLoader roomsLoader = new RoomsLoader(Constants.ROOMS_DIRECTORY_PATH);
        roomShapes = roomsLoader.loadRoomFiles();
    }

    public int getCurrentUniqueIdForWalkableTiles() {
        return currentUniqueIdForWalkableTiles;
    }

    private RoomShape getRandomRoomShape() {
        return this.roomShapes.get(Utilities.generateRandomInt(0, this.roomShapes.size() - 1));
    }

    public MapData generateMapData() {
        TreeNode treeRoot = createMapTemplate();

        return buildMapFromTree(treeRoot);
    }

    private TreeNode createMapTemplate() {
        int amountOfRoomsToCreate = 5;

        RoomShape currentRoomShape = getRandomRoomShape();
        Rectangle area = new Rectangle(
                0, 0,
                currentRoomShape.getColumnsAmount(),
                currentRoomShape.getRowsAmount()
        );

        // Create root room, which is the base of our map creation
        TreeNode root = new TreeNode(area, new RoomShape(currentRoomShape));

        // Hold and update amount of rooms created
        int roomsCreatedAmount = 1;
        roomsCreatedAmount += generateConnectionsForTreeNode(root);

        int maxHeightOfTreeForMapGeneration = 150;
        int currentHeightOfTree = 1;

        while (true) {
            // Create new connections for each TreeNode of a given height
            while (currentHeightOfTree < maxHeightOfTreeForMapGeneration) {
                List<TreeNode> nodesForCreatingConnection = new ArrayList<>();

                // Find and hold all nodes of a current height from the whole tree
                for (TreeNode currentNode : root.createListOfChildrenNodes()) {
                    if (currentNode.calculateHeight() == currentHeightOfTree) {
                        nodesForCreatingConnection.add(currentNode);
                    }
                }

                for (TreeNode nodeForConnections : nodesForCreatingConnection) {
                    roomsCreatedAmount += generateConnectionsForTreeNode(nodeForConnections);
                    if (roomsCreatedAmount >= amountOfRoomsToCreate) {
                        return root;
                    }
                }

                currentHeightOfTree++;
            }
            currentHeightOfTree = 1;
        }
    }

    private int generateConnectionsForTreeNode(TreeNode currentNode) {
        // Holds and then returns the amount of rooms added by this method
        int addedRoomsAmount = 0;
        // Add rooms connected to currentNode by their doors
        int maxAmountOfDoors = currentNode.getRoomShape().getAvailableDoorsTileIndices().size();
        if (maxAmountOfDoors == 0) {
            return addedRoomsAmount;
        }
        int amountOfDoors = Utilities.generateRandomInt(1, maxAmountOfDoors);

        List<Pair> availableDoors = currentNode.getRoomShape().getAvailableDoorsTileIndices();
        for (int i = 0; i < amountOfDoors; i++) {
            int randomDoorIndex = Utilities.generateRandomInt(0, availableDoors.size() - 1);
            Pair parentDoorIndices = availableDoors.get(randomDoorIndex);

            RoomShape leafRoomShape = new RoomShape(getRandomRoomShape());
            int leafRoomAmountOfDoors = leafRoomShape.getAvailableDoorsTileIndices().size();
            int leafDoorPositionInList = Utilities.generateRandomInt(0, leafRoomAmountOfDoors - 1);

            // Indices of a door that will connect new room to the parent
            Pair leafDoorIndices = leafRoomShape.getAvailableDoorsTileIndices().get(leafDoorPositionInList);

            // Calculate directions of room doors
            Direction parentDoorDirection = currentNode.getRoomShape().calculateDoorDirection(parentDoorIndices);
            Direction leafDoorDirection = leafRoomShape.calculateDoorDirection(leafDoorIndices);

            // Keep rotating door of leaf room until it is opposite to the parent room door
            while (!Direction.areDirectionsOpposite(parentDoorDirection, leafDoorDirection)) {
                leafRoomShape.rotateClockwise();
                leafDoorIndices = leafRoomShape.getAvailableDoorsTileIndices().get(leafDoorPositionInList);
                leafDoorDirection = leafRoomShape.calculateDoorDirection(leafDoorIndices);
            }

            int leafAreaPositionX = 0, leafAreaPositionY = 0;
            if (parentDoorDirection == Direction.WEST || parentDoorDirection == Direction.EAST) {
                if (parentDoorDirection == Direction.WEST) {
                    leafAreaPositionX = (int) (currentNode.getArea().x - leafRoomShape.getColumnsAmount());
                } else {
                    leafAreaPositionX = (int) (currentNode.getArea().x + currentNode.getArea().width);
                }
                leafAreaPositionY = parentDoorIndices.getIndexY() - leafDoorIndices.getIndexY() + (int) currentNode.getArea().y;
            } else if (parentDoorDirection == Direction.NORTH || parentDoorDirection == Direction.SOUTH) {
                if (parentDoorDirection == Direction.NORTH) {
                    leafAreaPositionY = (int) (currentNode.getArea().y - leafRoomShape.getRowsAmount());
                } else {
                    leafAreaPositionY = (int) (currentNode.getArea().y + currentNode.getArea().height);
                }
                leafAreaPositionX = parentDoorIndices.getIndexX() - leafDoorIndices.getIndexX() + (int) currentNode.getArea().x;
            }

            Rectangle newArea = new Rectangle(
                    leafAreaPositionX,
                    leafAreaPositionY,
                    leafRoomShape.getColumnsAmount(),
                    leafRoomShape.getRowsAmount()
            );

            if (isAreaInTheTreeFree(newArea, currentNode)) {
                RoomShape newRoomShape = new RoomShape(leafRoomShape);
                // Create TreeNode that is added as a leaf of a current TreeNode
                TreeNode newTreeNode = new TreeNode(newArea, newRoomShape);
                newTreeNode.addConnectingDoor(new Pair(leafDoorIndices));
                currentNode.addLeaf(newTreeNode, parentDoorIndices);
                addedRoomsAmount++;
            }

            availableDoors.remove(randomDoorIndex);
        }

        return addedRoomsAmount;
    }

    private boolean isAreaInTheTreeFree(Rectangle newArea, TreeNode treeNode) {
        TreeNode root = treeNode.findRootOfTree();

        // Go through all tree nodes in the tree and check if any of their areas overlap with newArea
        for (TreeNode currentTreeNode : root.createListOfNodes()) {
            if (currentTreeNode.getArea().overlaps(newArea)) {
                return false;
            }
        }

        return true;
    }

    private MapData buildMapFromTree(TreeNode root) {
        System.out.println("Rooms amount from tree : " + root.calculateAmountOfNodes());
        List<TreeNode> treeNodes = root.createListOfNodes();
        Vector2 displacement = findDisplacement(treeNodes);
        // Move all areas in treeNodes by displacement value so that each of them can be described in non negative values(necessary to store them in array)
        if (displacement.x != 0 || displacement.y != 0) {
            for (TreeNode currentTreeNode : treeNodes) {
                currentTreeNode.getArea().setX(currentTreeNode.getArea().x - displacement.x);
                currentTreeNode.getArea().setY(currentTreeNode.getArea().y - displacement.y);
            }
        }

        Vector2 maxPosition = findMaxPosition(treeNodes);
        int rowsAmount = (int) maxPosition.y;
        int columnsAmount = (int) maxPosition.x;
        Tile[][] mapArray = new Tile[rowsAmount][columnsAmount];
        System.out.println("rows amount : " + rowsAmount + "  columns amount : " + columnsAmount);

        // Initialize whole map with empty tiles (tiles that have their tileType set to TileType.EMPTY)
        for (int i = 0; i < rowsAmount; ++i) {
            for (int j = 0; j < columnsAmount; ++j) {
                mapArray[i][j] = new Tile(j + i * columnsAmount, TileType.EMPTY);
            }
        }

        ArrayList<Room> rooms = new ArrayList<>(treeNodes.size());

        for (TreeNode currentTreeNode : treeNodes) {
            int offsetY = (int) currentTreeNode.getArea().y;
            int offsetX = (int) currentTreeNode.getArea().x;
            for (int i = offsetY; i < offsetY + currentTreeNode.getArea().height; i++) {
                for (int j = offsetX; j < offsetX + currentTreeNode.getArea().width; j++) {
                    TileType currentTileType = currentTreeNode.getRoomShape().getRoomArray()[i - offsetY][j - offsetX];
                    mapArray[i][j].setTileType(currentTileType);
                    if (currentTileType.isWalkable()) {
                        mapArray[i][j].setUniqueIdForWalkableTiles(currentUniqueIdForWalkableTiles++);
                    }
                }
            }

            // Create rooms from TreeNodes and add them to the rooms list
            Room room = new Room(currentTreeNode.getRoomShape());
            room.setX((int) currentTreeNode.getArea().x);
            room.setY((int) currentTreeNode.getArea().y);

            for (Pair currentDoor : currentTreeNode.createDoorsList()) {
                room.addDoor(currentDoor);
                // Change wall that is marked as connecting door to TileType FLOOR_BASIC_0
                mapArray[room.getY() + currentDoor.getIndexY()][room.getX() + currentDoor.getIndexX()].setTileType(TileType.FLOOR_BASIC_0);
            }
            rooms.add(room);
        }

        return new MapData(mapArray, rowsAmount, columnsAmount, rooms);
    }

    private Vector2 findDisplacement(List<TreeNode> treeNodes) {
        Vector2 displacement = new Vector2();
        int minPositionX = 0;
        int minPositionY = 0;
        for (TreeNode currentTreeNode : treeNodes) {
            if (currentTreeNode.getArea().x < minPositionX) {
                minPositionX = (int) currentTreeNode.getArea().x;
            }
            if (currentTreeNode.getArea().y < minPositionY) {
                minPositionY = (int) currentTreeNode.getArea().y;
            }
        }
        displacement.set(minPositionX, minPositionY);

        return displacement;
    }

    private Vector2 findMaxPosition(List<TreeNode> treeNodes) {
        Vector2 position = new Vector2();
        int maxPositionX = 0;
        int maxPositionY = 0;
        for (TreeNode currentTreeNode : treeNodes) {
            if (currentTreeNode.getArea().x + currentTreeNode.getArea().width > maxPositionX) {
                maxPositionX = (int) (currentTreeNode.getArea().x + currentTreeNode.getArea().width);
            }
            if (currentTreeNode.getArea().y + currentTreeNode.getArea().height > maxPositionY) {
                maxPositionY = (int) (currentTreeNode.getArea().y + currentTreeNode.getArea().height);
            }
        }
        position.set(maxPositionX, maxPositionY);

        return position;
    }
}