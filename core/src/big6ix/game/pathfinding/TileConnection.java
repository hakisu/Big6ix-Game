package big6ix.game.pathfinding;

import big6ix.game.Tile;
import big6ix.game.map.Map;
import com.badlogic.gdx.ai.pfa.DefaultConnection;

public class TileConnection extends DefaultConnection<Tile> {

    private final Map map;

    public TileConnection(Tile fromNode, Tile toNode, Map map) {
        super(fromNode, toNode);
        this.map = map;
    }

    @Override
    public float getCost () {
        int fromIndexX = fromNode.getIndex() % map.getColumnsAmount();
        int fromIndexY = fromNode.getIndex() / map.getColumnsAmount();
        int toIndexX = toNode.getIndex() % map.getColumnsAmount();
        int toIndexY = toNode.getIndex() / map.getColumnsAmount();

        // Normal connection cost
        if (fromIndexX == toIndexX && Math.abs(fromIndexY - toIndexY) == 1) {
            return 1f;
        }
        if (fromIndexY == toIndexY && Math.abs(fromIndexX - toIndexX) == 1) {
            return 1f;
        }

        // Diagonal connection cost
        if (Math.abs(fromIndexX - toIndexX) == 1 && Math.abs(fromIndexY - toIndexY) == 1) {
            return 1.5f;
        }

        return 2f;
    }
}