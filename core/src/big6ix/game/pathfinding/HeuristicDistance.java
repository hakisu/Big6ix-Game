package big6ix.game.pathfinding;

import big6ix.game.Tile;
import big6ix.game.map.Map;
import com.badlogic.gdx.ai.pfa.Heuristic;

public class HeuristicDistance implements Heuristic<Tile> {

    private Map map;

    public HeuristicDistance(Map map) {
        this.map = map;
    }

    @Override
    public float estimate(Tile startNode, Tile endNode) {
        int startIndexX = startNode.getIndex() % map.getColumnsAmount();
        int startIndexY = startNode.getIndex() / map.getColumnsAmount();
        int endIndexX = endNode.getIndex() % map.getColumnsAmount();
        int endIndexY = endNode.getIndex() / map.getColumnsAmount();

        float distance = Math.abs(endIndexX - startIndexX) + Math.abs(endIndexY - startIndexY);
        return distance;
    }
}