package big6ix.game.utility;

import java.io.Serializable;

public class Pair implements Serializable {
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
        if (this == pair) return true;
        if (pair == null || getClass() != pair.getClass()) return false;
        Pair convertedObject = (Pair) pair;

        return (this.indexX == convertedObject.indexX && this.indexY == convertedObject.indexY);
    }

    @Override
    public String toString() {
        return "Index x : " + this.indexX + " Index y : " + this.indexY;
    }
}