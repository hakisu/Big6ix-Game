package big6ix.game.Utility;

public class Pair {
    int indexX;
    int indexY;

    public Pair(int indexX, int indexY) {
        this.indexX = indexX;
        this.indexY = indexY;
    }

    public int getIndexX() {
        return indexX;
    }

    public int getIndexY() {
        return indexY;
    }

    @Override
    public boolean equals(Object pair) {
        if (pair == null) return false;
        if (this.indexX == ((Pair) pair).indexX && this.indexY == ((Pair) pair).indexY) {
            return true;
        } else {
            return false;
        }
    }
}