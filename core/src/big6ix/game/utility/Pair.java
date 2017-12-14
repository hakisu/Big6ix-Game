package big6ix.game.utility;

public class Pair {
    private int indexX;
    private int indexY;

    public Pair(int indexX, int indexY) {
        this.indexX = indexX;
        this.indexY = indexY;
    }

    public Pair(Pair other) {
        this.indexX = other.indexX;
        this.indexY = other.indexY;
    }

    public int getIndexX() {
        return indexX;
    }

    public void setIndexX(int indexX) {
        this.indexX = indexX;
    }

    public int getIndexY() {
        return indexY;
    }

    public void setIndexY(int indexY) {
        this.indexY = indexY;
    }

    @Override
    public boolean equals(Object pair) {
        if (pair == null) return false;

        return (this.indexX == ((Pair) pair).indexX && this.indexY == ((Pair) pair).indexY);
    }

    @Override
    public String toString() {
        return "Index x : " + this.indexX + " Index y : " + this.indexY;
    }
}