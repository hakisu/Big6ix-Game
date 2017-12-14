package big6ix.game.map;

public enum Direction {
    NORTH,
    EAST,
    SOUTH,
    WEST;

    public static boolean areDirectionsOpposite(Direction start, Direction end) {
        if ((start == NORTH && end == SOUTH) || (start == SOUTH && end == NORTH)) {
            return true;
        }
        if ((start == WEST && end == EAST) || (start == EAST && end == WEST)) {
            return true;
        }

        return false;
    }
}