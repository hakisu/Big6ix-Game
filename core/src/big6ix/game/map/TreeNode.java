package big6ix.game.map;

import big6ix.game.utility.Pair;
import com.badlogic.gdx.math.Rectangle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class TreeNode {

    private Rectangle area;
    private RoomShape roomShape;
    private Map<Pair, TreeNode> leaves;
    private TreeNode parent;
    private List<Pair> connectingDoors;

    public TreeNode(Rectangle area, RoomShape roomShape) {
        this.leaves = new HashMap<>();
        this.connectingDoors = new ArrayList<>();
        this.area = area;
        this.roomShape = roomShape;
    }

    public Rectangle getArea() {
        return area;
    }

    public RoomShape getRoomShape() {
        return roomShape;
    }

    public List<Pair> createDoorsList() {
        List<Pair> doorsList = new ArrayList<>(leaves.keySet());
        doorsList.addAll(connectingDoors);

        return doorsList;
    }

    public void addLeaf(TreeNode leaf, Pair doorPosition) {
        leaf.parent = this;
        this.leaves.put(doorPosition, leaf);
    }

    public void addConnectingDoor(Pair door) {
        this.connectingDoors.add(door);
    }

    public int calculateHeight() {
        if (parent == null) {
            return 0;
        } else {
            return 1 + parent.calculateHeight();
        }
    }

    public int calculateAmountOfNodes() {
        int count = 1;
        for (TreeNode value : leaves.values()) {
            count += value.calculateAmountOfNodes();
        }

        return count;
    }

    public List<TreeNode> createListOfNodes() {
        List<TreeNode> nodesList = new ArrayList<>();
        nodesList.add(this);

        for (TreeNode nodeValue : leaves.values()) {
            nodesList.addAll(nodeValue.createListOfNodes());
        }

        return nodesList;
    }

    public List<TreeNode> createListOfChildrenNodes() {
        List<TreeNode> nodesList = new ArrayList<>();

        for (TreeNode nodeValue : leaves.values()) {
            nodesList.addAll(nodeValue.createListOfNodes());
        }

        return nodesList;
    }

    public TreeNode findRootOfTree() {
        TreeNode root = this;
        while (root.parent != null) {
            root = root.parent;
        }

        return root;
    }
}