package big6ix.game.PathFinding;

import big6ix.game.Tile;
import com.badlogic.gdx.ai.pfa.DefaultConnection;

public class TileConnection extends DefaultConnection<Tile> {

    public TileConnection(Tile fromNode, Tile toNode) {
        super(fromNode, toNode);
    }
}