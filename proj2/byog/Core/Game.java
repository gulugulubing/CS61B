package byog.Core;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;
import edu.princeton.cs.introcs.StdDraw;

import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Random;


public class Game implements Serializable{
    TERenderer ter = new TERenderer();
    /* Feel free to change the width and height. */
    public  static final int WIDTH = 80;
    public static final int HEIGHT = 50;
    int xLocation;//player's
    int yLocation;//player's
    public static Random RANDOM;
    public static ArrayList<Rectangle> hallway = new ArrayList<>();

    public TETile[][]finalWorldFrame = new TETile[WIDTH][HEIGHT];

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
        ter.initialize(WIDTH,HEIGHT);
        drawMenu();
        char startKey;
        while (true) {
            if (!StdDraw.hasNextKeyTyped()) {
                continue;
            }
            startKey = StdDraw.nextKeyTyped();
            if (startKey == 'n' || startKey == 'N') {
                StdDraw.clear();
                StdDraw.clear(Color.black);
                StdDraw.text(WIDTH / 2, HEIGHT / 2, "Enter the Seed:");
                StdDraw.show();
                String inputSeed = "";
                while (true) {
                    if (!StdDraw.hasNextKeyTyped()) {
                        continue;
                    }
                    char key = StdDraw.nextKeyTyped();
                    inputSeed += String.valueOf(key);
                    StdDraw.clear();
                    StdDraw.clear(Color.black);
                    StdDraw.text(WIDTH / 2, HEIGHT / 2 + 10, inputSeed);
                    StdDraw.show();
                    if (key == 's' || key == 'S') {
                        newStartGame(inputSeed);
                        playerMove(finalWorldFrame, ter, this);
                        break;
                    }
                }
            } else if (startKey == 'l' || startKey == 'L') {
                ter.initialize(WIDTH,HEIGHT);
                Game g = loadWorld();
                this.xLocation = g.xLocation;
                this.yLocation = g.yLocation;
                this.finalWorldFrame = g.finalWorldFrame;
                if(g != null) {
                    playerMove(g.finalWorldFrame,ter,this);
                    break;
                }
            } else if (startKey == 'q' || startKey == 'Q') {
                System.exit(0);
            }
        }
    }

    private void newStartGame(String inputSeed) {
        ter.initialize(WIDTH,HEIGHT);
        long seed = 0;
        for (int i = 0; i < inputSeed.length() - 1; i++) {
            char c = inputSeed.charAt(i);
            if (c < '0' || c > '9') {
                continue;
            }
            seed = seed * 10 + (c - '0');
        }

        RANDOM = new Random(seed);
        for (int x = 0; x < WIDTH; x += 1) {
            for (int y = 0; y < HEIGHT; y += 1) {
                finalWorldFrame[x][y] = Tileset.NOTHING;
            }
        }

        start(finalWorldFrame);
        completeHallway(finalWorldFrame, hallway);
        //ter.renderFrame(finalWorldFrame);
    }
    private static Game loadWorld() {
        File f = new File("./last.ser");
        if (f.exists()) {
            try {
                FileInputStream fs = new FileInputStream(f);
                ObjectInputStream os = new ObjectInputStream(fs);
                Game loadGame = (Game) os.readObject();
                os.close();
                return loadGame;
            } catch (FileNotFoundException e) {
                System.out.println("file not found");
                System.exit(0);
            } catch (IOException e) {
                System.out.println(e);
                System.exit(0);
            } catch (ClassNotFoundException e) {
                System.out.println("class not found");
                System.exit(0);
            }
        }
        return null;
    }

    private static void saveWorld(Game g) {
        File f = new File("./last.ser");
        try {
            if (!f.exists()) {
                f.createNewFile();
            }
            FileOutputStream fs = new FileOutputStream(f);
            ObjectOutputStream os = new ObjectOutputStream(fs);
            os.writeObject(g);
            os.close();
        }  catch (FileNotFoundException e) {
            System.out.println("file not found");
            System.exit(0);
        } catch (IOException e) {
            System.out.println(e);
            System.exit(0);
        }
    }

    public void drawMenu() {
        Font bigFont = new Font("Monaco", Font.BOLD, 40);
        StdDraw.setFont(bigFont);
        StdDraw.setPenColor(Color.white);
        StdDraw.text(WIDTH/2, 3*HEIGHT/4, "CS61B:THE GAME");


        Font smallFont = new Font("Monaco", Font.BOLD, 20);
        StdDraw.setFont(smallFont);
        StdDraw.text(WIDTH/2, HEIGHT/2, "New Game (N)");
        StdDraw.text(WIDTH/2, HEIGHT/2 - 5, "Load Game (L)");
        StdDraw.text(WIDTH/2, HEIGHT/2 - 10, "Quit Game (Q)");
        StdDraw.show();
    }

    public void playerMove(TETile[][] world, TERenderer ter,Game g) {

        while (true && RANDOM != null) {//if load, RANDOM WILL BE NULL
            g.xLocation = RandomUtils.uniform(RANDOM, 0, Game.WIDTH);
            g.yLocation = RandomUtils.uniform(RANDOM, 0, Game.HEIGHT);
            if (world[g.xLocation][g.yLocation].character() == '·') {
                world[g.xLocation][g.yLocation] = Tileset.PLAYER;
                break;
            }
        }


        while (true) {
            /*mouse hover display*/
            int xMouse = (int) Math.floor(StdDraw.mouseX());
            int yMouse = (int) Math.floor(StdDraw.mouseY());
            StdDraw.setPenColor(StdDraw.WHITE);
            StdDraw.textLeft(1, HEIGHT - 1, world[xMouse][yMouse].description());
            StdDraw.show();
            ter.renderFrame(world);


            /* check if the user has typed a key; if so, process it */
            if (StdDraw.hasNextKeyTyped()) {
                char key = StdDraw.nextKeyTyped();
                if (key == 'w') {
                    if (g.yLocation + 1 < Game.HEIGHT && world[g.xLocation][g.yLocation + 1].character() == '·') {
                        world[g.xLocation][g.yLocation] = Tileset.FLOOR;
                        g.yLocation = g.yLocation + 1;
                        world[g.xLocation][g.yLocation] = Tileset.PLAYER;
                        ter.renderFrame(world);
                    }
                } else if (key == 'a') {
                    if (g.xLocation - 1 >= 0 && world[g.xLocation - 1][g.yLocation].character() == '·') {
                        world[g.xLocation][g.yLocation] = Tileset.FLOOR;
                        g.xLocation = g.xLocation - 1;
                        world[g.xLocation][g.yLocation] = Tileset.PLAYER;
                        ter.renderFrame(world);
                    }
                } else if (key == 's') {
                    if (g.yLocation - 1 >= 0 && world[g.xLocation][g.yLocation - 1].character() == '·') {
                        world[g.xLocation][g.yLocation] = Tileset.FLOOR;
                        g.yLocation = g.yLocation - 1;
                        world[g.xLocation][g.yLocation] = Tileset.PLAYER;
                        ter.renderFrame(world);
                    }
                } else if (key == 'd') {
                    if (g.xLocation + 1 < Game.WIDTH && world[g.xLocation + 1][g.yLocation].character() == '·') {
                        world[g.xLocation][g.yLocation] = Tileset.FLOOR;
                        g.xLocation = g.xLocation + 1;
                        world[g.xLocation][g.yLocation] = Tileset.PLAYER;
                        ter.renderFrame(world);
                    }
                } else if (key == 'q') {
                    saveWorld(this);
                    System.exit(0);
                }
            }
        }
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

        //ter.initialize(WIDTH, HEIGHT);
        for (int x = 0; x < WIDTH; x += 1) {
            for (int y = 0; y < HEIGHT; y += 1) {
                finalWorldFrame[x][y] = Tileset.NOTHING;
            }
        }
        char firstLetter = input.charAt(0);
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (c < '0' || c > '9') {
                continue;
            }
            seed = seed * 10 + (c - '0');
        }


        String inputLst[] = input.split("[SLsl]",2);
        String moveInput;
        if (inputLst.length == 2) {
            moveInput = inputLst[1];
        } else {
            moveInput = null;
        }


        if (firstLetter == 'N' || firstLetter == 'n') {
            RANDOM = new Random(seed);
            start(finalWorldFrame);
            completeHallway(finalWorldFrame, hallway);
            stringPlayerMove(finalWorldFrame, moveInput);
        } else if (firstLetter == 'L' || firstLetter == 'l') {
            Game g = loadWorld();
            this.finalWorldFrame = g.finalWorldFrame;
            this.yLocation = g.yLocation;
            this.xLocation = g.xLocation;
            stringPlayerMove(this.finalWorldFrame, moveInput);
        }
        return this.finalWorldFrame;

    }

    public void stringPlayerMove(TETile[][] world, String moveS) {
        while (true && RANDOM != null) {//if load, RANDOM WILL BE NULL
            xLocation = RandomUtils.uniform(RANDOM, 0, Game.WIDTH);
            yLocation = RandomUtils.uniform(RANDOM, 0, Game.HEIGHT);
            if (world[xLocation][yLocation].character() == '·') {
                world[xLocation][yLocation] = Tileset.PLAYER;
                break;
            }
        }

        if (moveS != null) {
            for (int i = 0; i < moveS.length(); i++) {
                /* check if the user has typed a key; if so, process it */
                if (moveS.charAt(i) == 'w' || moveS.charAt(i) == 'W') {
                    if (yLocation + 1 < Game.HEIGHT && world[xLocation][yLocation + 1].character() == '·') {
                        world[xLocation][yLocation] = Tileset.FLOOR;
                        yLocation = yLocation + 1;
                        world[xLocation][yLocation] = Tileset.PLAYER;
                        //ter.renderFrame(world);
                    }
                } else if (moveS.charAt(i) == 'a' || moveS.charAt(i) == 'A') {
                    if (xLocation - 1 >= 0 && world[xLocation - 1][yLocation].character() == '·') {
                        world[xLocation][yLocation] = Tileset.FLOOR;
                        xLocation = xLocation - 1;
                        world[xLocation][yLocation] = Tileset.PLAYER;
                        //ter.renderFrame(world);
                    }
                } else if (moveS.charAt(i) == 's' || moveS.charAt(i) == 'S') {
                    if (yLocation - 1 >= 0 && world[xLocation][yLocation - 1].character() == '·') {
                        world[xLocation][yLocation] = Tileset.FLOOR;
                        yLocation = yLocation - 1;
                        world[xLocation][yLocation] = Tileset.PLAYER;
                        //ter.renderFrame(world);
                    }
                } else if (moveS.charAt(i) == 'd' || moveS.charAt(i) == 'D') {
                    if (xLocation + 1 < Game.WIDTH && world[xLocation + 1][yLocation].character() == '·') {
                        world[xLocation][yLocation] = Tileset.FLOOR;
                        xLocation = xLocation + 1;
                        world[xLocation][yLocation] = Tileset.PLAYER;
                        //ter.renderFrame(world);
                    }
                } else if (moveS.charAt(i) == ':' && (moveS.charAt(i + 1) == 'q' || moveS.charAt(i + 1) == 'Q')) {
                    saveWorld(this);
                    break;
                }
            }
        }
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
