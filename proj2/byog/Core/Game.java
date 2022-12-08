package byog.Core;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.util.ArrayList;
import java.util.Random;


public class Game {
    TERenderer ter = new TERenderer();
    /* Feel free to change the width and height. */
    public  static final int WIDTH = 80;
    public static final int HEIGHT = 50;
    public static Random RANDOM;
    public static ArrayList<Rectangle> hallway = new ArrayList<>();

    public static class Rectangle {
        public int width;
        public int height;
        public int xLeft;
        public int yBottom;

        public Rectangle(int x, int y, int w, int h) {
            width = w;
            height = h;
            xLeft = x;
            yBottom = y;
        }
    }

    /**
     * Method used for playing a fresh game. The game should start from the main menu.
     */
    public void playWithKeyboard() {
    }

    /**
     * Method used for autograding and testing the game code. The input string will be a series
     * of characters (for example, "n123sswwdasdassadwas", "n123sss:q", "lwww". The game should
     * behave exactly as if the user typed these characters into the game after playing
     * playWithKeyboard. If the string ends in ":q", the same world should be returned as if the
     * string did not end with q. For example "n123sss" and "n123sss:q" should return the same
     * world. However, the behavior is slightly different. After playing with "n123sss:q", the game
     * should save, and thus if we then called playWithInputString with the string "l", we'd expect
     * to get the exact same world back again, since this corresponds to loading the saved game.
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */
    public TETile[][] playWithInputString(String input) {
        // Fill out this method to run the game using the input passed in,
        // and return a 2D tile representation of the world that would have been
        // drawn if the same inputs had been given to playWithKeyboard().
        // initialize tiles
        long seed = 0;
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (c < '0' || c > '9') {
                continue;
            }
            seed = seed * 10 + (c - '0');
        }

        RANDOM = new Random(seed);

        //ter.initialize(WIDTH, HEIGHT);

        TETile[][]finalWorldFrame = new TETile[WIDTH][HEIGHT];
        for (int x = 0; x < WIDTH; x += 1) {
            for (int y = 0; y < HEIGHT; y += 1) {
                finalWorldFrame[x][y] = Tileset.NOTHING;
            }
        }

        start(finalWorldFrame);
        completeHallway(finalWorldFrame, hallway);
        //ter.renderFrame(finalWorldFrame);
        return finalWorldFrame;
    }

    public void start(TETile[][] world) {
        int x = RandomUtils.uniform(RANDOM, 20, 60);
        int y = RandomUtils.uniform(RANDOM, 15, 35);
        int w = RandomUtils.uniform(RANDOM, 5, 15);
        int h = RandomUtils.uniform(RANDOM, 5, 15);
        mapCreator(world, x, y, w, h);
    }
    public void mapCreator(TETile[][] world, int x, int y, int w, int h) {
        Rectangle rec = drawRoomOrHallway(world, x, y, w, h);
        if (rec == null) {
            //System.out.println("recursion end");
            return;
        }
        if (rec.height == 3 || rec.width == 3) {
            hallway.add(rec);
        }

        /**numOfsides from which next room or hallway be created*/
        int num = RandomUtils.uniform(RANDOM, 1, 5);
        //System.out.println("numOfSide" + num);

        /**0 the left side,1 the bottom side, 2 the right side, 3 the top side*/
        int[] tempList = new int[]{0, 1, 2, 3};
        RandomUtils.shuffle(RANDOM, tempList);
        //for (int i = 0; i < num; i++) {
        //System.out.print(tempList[i] + " ");
        //}
        // System.out.println();
        /**after shuffle templist[i] topmost of num will be the side where next room or hallway created*/


        if (rec.width == 3) {
            //vertical hallway
            for (int i = 0; i < num; i++) {
                if (tempList[i] == 0) {
                    //left side: next only can be a horizon hallway
                    for (int j = 0; j < 1000; j++) {
                        if (rec.height > 3) {
                            int startY;
                            try {
                                startY = RandomUtils.uniform(RANDOM, rec.yBottom, rec.yBottom + rec.height - 3);
                            } catch (Exception e) {
                                startY = rec.yBottom + 1;
                            }
                            int randomW = RandomUtils.uniform(RANDOM, 5, 10);
                            if (!roomOverLap(world, rec.xLeft - randomW + 1, startY, randomW, 3)) {
                                mapCreator(world, rec.xLeft - randomW + 1, startY, randomW, 3);
                                break;
                            }
                        }
                    }
                } else if (tempList[i] == 1) {
                    //bottom side: next can be a room or a turn hallway
                    for (int j = 0; j < 1000; j++) {
                        int randomW = RandomUtils.uniform(RANDOM, 4, 15);
                        int startX = RandomUtils.uniform(RANDOM,rec.xLeft + rec.width - 1 - randomW + 1, rec.xLeft + 1);
                        int startY = rec.yBottom - RandomUtils.uniform(RANDOM, 5, 15);
                        if (!roomOverLap(world, startX, startY, randomW, rec.yBottom - startY + 1)) {
                            mapCreator(world, startX, startY, randomW, rec.yBottom - startY + 1);
                            break;
                        }
                    }
                } else if (tempList[i] == 2) {
                    //right side: next only can be a horizon hallway
                    for (int j = 0; j < 1000; j++) {
                        if (rec.height > 3) {
                            int startY;
                            try {
                                startY = RandomUtils.uniform(RANDOM, rec.yBottom, rec.yBottom + rec.height - 3);
                            } catch (Exception e) {
                                startY = rec.yBottom + 1;
                            }
                            int randomW = RandomUtils.uniform(RANDOM, 5, 10);
                            if (!roomOverLap(world, rec.xLeft + rec.width - 1, startY, randomW, 3)) {
                                mapCreator(world, rec.xLeft + rec.width - 1, startY, randomW, 3);
                                break;
                            }
                        }
                    }
                } else {
                    //System.out.println("topside");
                    //top side: next can be a room or a turn hallway
                    for (int j = 0; j < 1000; j++) {
                        int startY = rec.yBottom + rec.height - 1;
                        int randomW = RandomUtils.uniform(RANDOM, 4, 15);
                        int startX = RandomUtils.uniform(RANDOM, rec.xLeft + rec.width - 1 - randomW + 1,
                                rec.xLeft + 1);
                        int randomH = RandomUtils.uniform(RANDOM, 5, 15);
                        if (!roomOverLap(world, startX, startY, randomW, randomH)) {
                            //System.out.println("topside got it");
                            mapCreator(world, startX, startY, randomW, randomH);
                            break;
                        }
                    }
                }
            }
        } else if (rec.height == 3) {
            //horizon hallway
            for (int i = 0; i < num; i++) {
                if (tempList[i] == 0) {
                    //left side: next can be a room or a turn hallway
                    for (int j = 0; j < 1000; j++) {
                        int startX = rec.xLeft - RandomUtils.uniform(RANDOM, 5, 15);
                        int randomH = RandomUtils.uniform(RANDOM, 4, 15);
                        int startY = rec.yBottom + rec.height - 1 - randomH + 1;
                        if (!roomOverLap(world, startX, startY, rec.xLeft - startX + 1, randomH)) {
                            mapCreator(world, startX, startY, rec.xLeft - startX + 1, randomH);
                            break;
                        }
                    }
                } else if (tempList[i] == 1) {
                    //bottom side:next only can be a vertical hallway
                    for (int j = 0; j < 100; j++) {
                        if (rec.width > 3) {
                            int startX;
                            try {
                                startX = RandomUtils.uniform(RANDOM, rec.xLeft, rec.xLeft + rec.width - 3);
                            } catch (Exception e) {
                                startX = rec.xLeft + 1;
                            }
                            int randomH = RandomUtils.uniform(RANDOM, 5, 10);
                            if (!roomOverLap(world, startX, rec.yBottom - randomH + 1, 3, randomH)) {
                                mapCreator(world, startX, rec.yBottom - randomH + 1, 3, randomH);
                                break;
                            }
                        }
                    }
                } else if (tempList[i] == 2) {
                    //right side: next can be a room or a turn hallway
                    for (int j = 0; j < 1000; j++) {
                        int startX = rec.xLeft + rec.width - 1;
                        int randomW = RandomUtils.uniform(RANDOM, 5, 15);
                        int randomH = RandomUtils.uniform(RANDOM, 4, 15);
                        int startY =  RandomUtils.uniform(RANDOM, rec.yBottom
                                + rec.height - 1 - randomH + 1, rec.yBottom + 1);
                        if (!roomOverLap(world, startX, startY, randomW, randomH)) {
                            mapCreator(world, startX, startY, randomW, randomH);
                            break;
                        }
                    }
                } else {
                    //top side:next only can be a vertical hallway
                    for (int j = 0; j < 1000; j++) {
                        if (rec.width > 3) {
                            int startX;
                            try {
                                startX = RandomUtils.uniform(RANDOM, rec.xLeft, rec.xLeft + rec.width - 3);
                            } catch (Exception e) {
                                startX = rec.xLeft + 1;
                            }
                            int randomH = RandomUtils.uniform(RANDOM, 5, 10);
                            if (!roomOverLap(world, startX, rec.yBottom + rec.height - 1, 3, randomH)) {
                                mapCreator(world, startX, rec.yBottom + rec.height - 1, 3, randomH);
                                break;
                            }
                        }
                    }
                }
            }
        } else {
            //room
            for (int i = 0; i < num; i++) {
                if (tempList[i] == 0) {
                    //left side: next only can be a horizon hallway
                    for (int j = 0; j < 1000; j++) {
                        if (rec.height > 2) {
                            int startY = RandomUtils.uniform(RANDOM, rec.yBottom, rec.yBottom + rec.height - 3);
                            int randomW = RandomUtils.uniform(RANDOM, 5, 10);
                            if (!roomOverLap(world, rec.xLeft - randomW + 1, startY, randomW, 3)) {
                                mapCreator(world, rec.xLeft - randomW + 1, startY, randomW, 3);
                                break;
                            }
                        }
                    }
                } else if (tempList[i] == 1) {
                    //bottom side:next only can be a vertical hallway
                    for (int j = 0; j < 1000; j++) {
                        if (rec.width > 2) {
                            int startX = RandomUtils.uniform(RANDOM, rec.xLeft, rec.xLeft + rec.width - 3);
                            int randomH = RandomUtils.uniform(RANDOM, 5, 10);
                            if (!roomOverLap(world, startX, rec.yBottom - randomH + 1, 3, randomH)) {
                                mapCreator(world, startX, rec.yBottom - randomH + 1, 3, randomH);
                                break;
                            }
                        }
                    }
                } else if (tempList[i] == 2) {
                    //right side: next only can be a horizon hallway
                    for (int j = 0; j < 1000; j++) {
                        if (rec.height > 2) {
                            int startY = RandomUtils.uniform(RANDOM, rec.yBottom, rec.yBottom + rec.height - 3);
                            int randomW = RandomUtils.uniform(RANDOM, 5, 10);
                            if (!roomOverLap(world, rec.xLeft + rec.width - 1, startY, randomW, 3)) {
                                mapCreator(world, rec.xLeft + rec.width - 1, startY, randomW, 3);
                                break;
                            }
                        }
                    }
                } else {
                    //top side:next only can be a vertical hallway
                    for (int j = 0; j < 1000; j++) {
                        if (rec.width > 2) {
                            int startX = RandomUtils.uniform(RANDOM, rec.xLeft, rec.xLeft + rec.width - 3);
                            int randomH = RandomUtils.uniform(RANDOM, 5, 10);
                            if (!roomOverLap(world, startX, rec.yBottom + rec.height - 1, 3, randomH)) {
                                mapCreator(world, startX, rec.yBottom + rec.height - 1, 3, randomH);
                                break;
                            }
                        }
                    }
                }
            }
        }
    }



    //draw Single a random room which sides contain the random point(x,y).
    public Rectangle drawRoomOrHallway(TETile[][] world, int x, int y, int w, int h) {


        if (w < 3 && h < 3) {
            //System.out.println("w < 3 && h < 3 ");
            return null;
        }

        if (x < 0 || y < 0 || x + w - 1 > Game.WIDTH - 1 || y + h - 1 > Game.HEIGHT - 1) {
            //System.out.println("OUT OF BOUND ");
            return null;
        }

        if (roomOverLap(world, x, y, w, h)) {
            return null;
        }

        for (int i = 0; i < w; i++) {
            world[x + i][y] = Tileset.WALL;
            world[x + i][y + h - 1] = Tileset.WALL;
        }

        for (int i = 1; i < h - 1; i++) {
            world[x][y + i] = Tileset.WALL;
            world[x + w - 1][y + i] = Tileset.WALL;
        }

        for (int i = 1; i < h - 1; i++) {
            for (int j = 1; j < w - 1; j++) {
                world[x + j][y + i] = Tileset.FLOOR;
            }
        }
        //System.out.println("x:" + x + " y:" + y + " w:" + w +" h:" + h);
        return new Rectangle(x, y, w, h);
    }


    public boolean roomOverLap(TETile[][] world, int x, int y, int w, int h) {
        if (x < 0 || y < 0 || x + w - 1 > Game.WIDTH - 1 || y + h - 1 > Game.HEIGHT - 1) {
            //System.out.println("OUT OF BOUND ");
            return true;
        }

        //the ends of hallway can be overlap
        if (w == 3) {
            for (int i = 1; i < h - 1 ; i++) {
                if (world[x][y + i].character() != ' ' || world[x + w - 1][y + i].character() != ' ') {
                    //System.out.println("w==3 hallway overlap");
                    return true;
                }
            }
            if ((world[x][y].character() != ' ' || world[x + 2][y].character() != ' ')
                    && (world[x][y + h - 1].character() != ' ' || world[x + 2][y + h - 1].character() != ' ')) {
                return true;
            }
            return false;
        }
        if (h == 3) {
            for (int i = 1; i < w - 1; i++) {
                if (world[x + i][y].character() != ' ' || world[x + i][y + h - 1].character() != ' ') {
                    //System.out.println("h==3 hallway overlap");
                    return true;
                }
            }
            if ((world[x][y].character() != ' ' || world[x][y + 2].character() != ' ')
                    && (world[x + w - 1][y].character() != ' ' || world[x + w - 1][y + 2].character() != ' ')) {
                return true;
            }
            return false;
        }

        //normal room
        //can be overlap by hallway but can not be overlap by room
        //room corner detector
        try {
            if (world[x - 1][y - 1].character() == '·') {
                return true;
            }
        } catch (Exception e) {
        }

        try {
            if (world[x - 1 + w + 1][y - 1].character() == '·') {
                return true;
            }
        } catch (Exception e) {
        }
        try {
            if (world[x - 1][y - 1 + h + 1].character() == '·') {
                return true;
            }
        } catch (Exception e) {
        }
        try {
            if (world[x - 1 + w + 1][y - 1 + h + 1].character() == '·') {
                return true;
            }
        } catch (Exception e) {
        }
        //sides detector
        for (int i = 0; i < w; i++) {
            if (world[x + i][y].character() != ' ') {
                if (horizon4continualPoints(world, x + i, y)) {
                    //System.out.println("room overlap");
                    return true;
                }
            }
            if (world[x + i][y + h - 1].character() != ' ') {
                if (horizon4continualPoints(world, x + i, y + h - 1)) {
                    //System.out.println("room overlap");
                    return true;
                }
            }
        }

        for (int i = 1; i < h - 1; i++) {
            if (world[x][y + i].character() != ' ') {
                if (vertical4continualPoints(world, x, y + i)) {
                    //System.out.println("room overlap");
                    return true;
                }
            }

            if (world[x + w - 1][y + i].character() != ' ') {
                if (vertical4continualPoints(world, x + w - 1, y + i)) {
                    //System.out.println("room overlap");
                    return true;
                }
            }
        }

        //inner detector
        for (int i = 1; i < w - 1 ; i++) {
            if (world[x + i][y + 1].character() != ' ' || world[x + i][y + h - 2].character() != ' ') {
                //System.out.println("room overlap");
                return true;
            }
        }
        for (int i = 1; i < h - 1; i++) {
            if (world[x + 1][y + i].character() != ' ' || world[x + w - 2][y + i].character() != ' ') {
                //System.out.println("room overlap");
                return true;
            }
        }
        return false;
    }

    public static boolean horizon4continualPoints(TETile[][] world, int x, int y) {
        for (int i = 0; i < 4; i++) {
            if (y + i > Game.WIDTH - 1) {
                return false;
            }
            if (world[x + i][y].character() == ' ') {
                return false;
            }
        }
        return true;
    }

    public static boolean vertical4continualPoints(TETile[][] world, int x, int y) {
        for (int i = 0; i < 4; i++) {
            if (y + i > Game.HEIGHT - 1) {
                return false;
            }
            if (world[x][y + i].character() == ' ') {
                return false;
            }
        }
        return true;
    }


    public void completeHallway(TETile[][] world, ArrayList<Rectangle> hallway) {
        for (Rectangle tempHallway:hallway) {
            if (tempHallway.width == 3) {
                if (tempHallway.yBottom != 0
                        && world[tempHallway.xLeft + 1][tempHallway.yBottom - 1].character() == '·') {
                    world[tempHallway.xLeft + 1][tempHallway.yBottom] = Tileset.FLOOR;
                }
                if (tempHallway.yBottom + tempHallway.height - 1 != Game.HEIGHT - 1
                        && world[tempHallway.xLeft + 1][tempHallway.yBottom + tempHallway.height].character() == '·') {
                    world[tempHallway.xLeft + 1][tempHallway.yBottom + tempHallway.height - 1] = Tileset.FLOOR;
                }
            }
            if (tempHallway.height == 3) {
                if (tempHallway.xLeft != 0
                        && world[tempHallway.xLeft - 1][tempHallway.yBottom + 1].character() == '·') {
                    world[tempHallway.xLeft][tempHallway.yBottom + 1] = Tileset.FLOOR;
                }
                if (tempHallway.xLeft + tempHallway.width - 1 != Game.WIDTH -1
                        && world[tempHallway.xLeft + tempHallway.width][tempHallway.yBottom + 1].character() == '·') {
                    world[tempHallway.xLeft + tempHallway.width - 1][tempHallway.yBottom + 1] = Tileset.FLOOR;
                }
            }
        }
    }
}
