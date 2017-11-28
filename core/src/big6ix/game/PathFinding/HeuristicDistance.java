package big6ix.game.PathFinding;

import big6ix.game.Constants;
import big6ix.game.Tile;
import com.badlogic.gdx.ai.pfa.Heuristic;

public class HeuristicDistance implements Heuristic<Tile> {

    @Override
    public float estimate(Tile startNode, Tile endNode) {
        int startIndexX = startNode.getIndex() % Constants.MAP_COLUMNS_AMOUNT;
        int startIndexY = startNode.getIndex() / Constants.MAP_COLUMNS_AMOUNT;
        int endIndexX = endNode.getIndex() % Constants.MAP_COLUMNS_AMOUNT;
        int endIndexY = endNode.getIndex() / Constants.MAP_COLUMNS_AMOUNT;

        float distance = Math.abs(endIndexX - startIndexX) + Math.abs(endIndexY - startIndexY);
        return distance;
    }
}