package byog.lab5;
import org.junit.Test;
import static org.junit.Assert.*;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.util.Random;

/**
 * Draws a world consisting of hexagonal regions.
 */
public class HexWorld {

    static Random r = new Random(200);
    private static TETile tileRandom() {
        int tileNum = r.nextInt(6);
        switch (tileNum) {
            case 0:
                return Tileset.WALL;
            case 1:
                return Tileset.FLOWER;
            case 2:
                return Tileset.GRASS;
            case 3:
                return Tileset.WATER;
            case 4:
                return Tileset.MOUNTAIN;
            case 5:
                return Tileset.TREE;
            default:
                return Tileset.TREE;
        }
    }

    /** A single N-width(N-size) hex has 2*N row,different row has different width and xOffset
     * row = 0 is the bottom row and this row's width is size
     * when row increase, width increase.
     * until row = size
     * */
    public static int calWidth(int row, int size) {
        int increaseWidth = row;
        if(row >= size) {
            increaseWidth = (size - 1) -  (row - size);
        }
        return size + 2 * increaseWidth;
    }

    public static int calXoffSet(int row, int size) {
        if(row > size) {
            return row - size -1;
        }
        return size - row;
    }

    /**px,py is the leftmost position of the row*/
    public static void addRow(TETile[][] world, Position p, int width, TETile t) {
        for (int xi = 0; xi < width; xi += 1) {
            int xCoord = p.x + xi;
            int yCoord = p.y;
            world[xCoord][yCoord] = TETile.colorVariant(t, 32, 32, 32, r);
        }
    }

    /**px,py is the bottom left position of the hexgon*/
    public static void addHexagon(TETile[][] world, Position p, int s) {
        TETile randomTile = tileRandom();
        if (s < 2) {
            throw new IllegalArgumentException("Hexagon must be at least size 2.");
        }

        // hexagons have 2*s rows. this code iterates up from the bottom row,
        // which we call row 0.
        for (int yi = 0; yi < 2 * s; yi += 1) {
            int thisRowY = p.y + yi;
            int xRowStart = p.x + calXoffSet(s, yi);
            int rowWidth = calWidth(yi, s);
            HexWorld hw = new HexWorld();
            Position startPointOfOneHex = hw.new Position(xRowStart,thisRowY);
            addRow(world, startPointOfOneHex, rowWidth, randomTile);
        }
    }

    /** 5 columns,consisting of 3, 4, 5, 4, and 3 hexagons
     * num is the num of column: 0,1,2,3,4*/
    public static void addVerticalHexagons(TETile[][] world, Position p, int s, int num) {
        int numOfHexagon;
        p.x = p.xStartPoint(num, s);
        p.y = p.yStartPoint(num, s);
        switch (num) {
            case 0:
                numOfHexagon = 3;
                break;
            case 1:
                numOfHexagon = 4;
                break;
            case 2:
                numOfHexagon = 5;
                break;
            case 3:
                numOfHexagon = 4;
                break;
            case 4:
                numOfHexagon = 3;
                break;
            default:
                numOfHexagon = 3;
        }

        for(int i = 0; i < numOfHexagon; i++) {
            addHexagon(world, p, s);
            p.y = p.y + 2 * s;
        }
    }

    public class Position {
        public int x;
        public int y;

        public Position(int i, int j) {
            x = i;
            y = j;
        }
        /**
         * according to columns to determine the startpoint of each column
         */
        private int xStartPoint(int num, int size) {
            return x + num * (size + size - 1);
        }

        private int yStartPoint(int num, int size) {
            if (num > 2) {
                return y - size + (num - 3) * size;
            }
            return y - num * size;
        }
    }
}
