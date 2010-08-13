package ch.idsia.mario.engine.level;

import ch.idsia.mario.engine.sprites.Enemy;

import java.util.Random;

/**
 * Using this class is very simple. Just call <b>createMethod</b> with params:
 * <ul>
 *  <li>width -- width of the level in cells. On the screen one cell has 16 pixels </li>
 *  <li>height -- height of the level in cells. On the screen one cell has 16 pixels </li>
 *  <li>seed -- use this param to make a globalRandom level.
 *      On different machines with the same seed param there will be one level</li>
 *  <li>difficulty -- use this param to change difficult of the level.
 *      On different machines with the same seed param there will be one level</li>
 *  <li>type -- type of the level. One of Overground, Underground, Castle.</li>
 * </ul>
 *
 * @see #TYPE_OVERGROUND
 * @see #TYPE_UNDERGROUND
 * @see #TYPE_CASTLE
 *

 */

public class LevelGenerator
{
    public static final int TYPE_OVERGROUND = 0;
    public static final int TYPE_UNDERGROUND = 1;
    public static final int TYPE_CASTLE = 2;
    public static final int DEFAULT_FLOOR = -1;
    private int[] cmdArgs; //ATTENTION: not cloned.

    //TODO: length of the level shouldn't be fewer than LevelLengthMinThreshold
    public static final int LevelLengthMinThreshold = 50; // minimal length of the level. used in ToolsConfigurator
    private int levelDifficulty;

    public static Level createLevel(int[] args)
    {
        LevelGenerator levelGenerator = new LevelGenerator(args);
        return levelGenerator.createLevel(args[4], args[2], args[5]);
    }

    private int width;
    private int height;
    Level level;
    final static Random globalRandom = new Random();
    final static Random creaturesRandom = new Random();

    private static final int ODDS_STRAIGHT = 0;
    private static final int ODDS_HILL_STRAIGHT = 1;
    private static final int ODDS_TUBES = 2;
    private static final int ODDS_GAPS = 3;
    private static final int ODDS_CANNONS = 4;
    private static final int ODDS_DEAD_ENDS = 5;
    private int[]  odds = new int[6];
    private int totalOdds;
    private int difficulty; //level difficulty
    private int type;  //level type

    //constants for dead ends
    private static final boolean RIGHT_DIRECTION_BOTTOM = false;
    private static final int ANY_HEIGHT = -1;
    private static final int INFINITY_FLOOR_HEIGHT = Integer.MAX_VALUE;

    //Level customization counters
    private int deadEndsCount = 0;
    private int cannonsCount = 0;
    private int hillStraightCount = 0;
    private int tubesCount = 0;
    private int blocksCount = 0;
    private int coinsCount = 0;
    private int gapsCount = 0;
    private int hiddenBlocksCount = 0;

    private LevelGenerator(int[] args)
    {
        this.width = args[3];
        if (args[19] < 15)
        {
            System.err.println("Minimal height of the level is 15! Setting up to 15.");
            System.err.flush();
            this.height = 15;
        }
        else
        {
            this.height = args[19];
        }
        this.cmdArgs = args;
    }

    private Level createLevel(long seed, int difficulty, int type)
    {
        this.type = type;
        this.difficulty = difficulty;
        odds[ODDS_STRAIGHT] = 20;
        odds[ODDS_HILL_STRAIGHT] = 1;
        odds[ODDS_TUBES] = 2 + 1 * difficulty;
        this.levelDifficulty = difficulty;
        odds[ODDS_GAPS] = 3 * difficulty;
        odds[ODDS_CANNONS] = -10 + 5 * difficulty;
        odds[ODDS_DEAD_ENDS] = 2 + 2 * difficulty;

        if (type != LevelGenerator.TYPE_OVERGROUND)
        {
            odds[ODDS_HILL_STRAIGHT] = 0; //if not overground then there are no hill straight
        }

        for (int i = 0; i < odds.length; i++)
        {
            if (odds[i] < 0) odds[i] = 0;
            totalOdds += odds[i];
            odds[i] = totalOdds - odds[i];
        }

        level = new Level(width, height);
        globalRandom.setSeed(seed);

        int length = 0; //total level length
        //mario starts on straight

        int floor = DEFAULT_FLOOR;
        if (cmdArgs[29] == 1)
        {
            floor = height - 1 - globalRandom.nextInt(4);
        }

        length += buildStraight(0, level.width, true, floor, INFINITY_FLOOR_HEIGHT);
        while (length < level.width - 64)
        {
            length += buildZone(length, level.width - length, ANY_HEIGHT, floor, INFINITY_FLOOR_HEIGHT);
        }

        if (cmdArgs[29] == 0)
        {
            floor = height -1 - globalRandom.nextInt(4); //floor of the exit line
        }

        //coordinates of finish
        level.xExit = length + 8;
        level.yExit = floor;

        //fix floor
        for (int x = length; x < level.width; x++)
        {
            for (int y = 0; y < height; y++)
            {
                if (y >= floor)
                {
                    level.setBlock(x, y, (byte) (1 + 9 * 16));
                }
            }
        }

        //if underground or castle then built ceiling
        if (type == LevelGenerator.TYPE_CASTLE || type == LevelGenerator.TYPE_UNDERGROUND)
        {
            int ceiling = 0;
            int run = 0;
            for (int x = 0; x < level.width; x++)
            {
                if (run-- <= 0 && x > 4)
                {
                    ceiling = globalRandom.nextInt(4);
                    run = globalRandom.nextInt(4) + 4;
                }
                for (int y = 0; y < level.height; y++)
                {
                    if ((x > 4 && y <= ceiling) || x < 1)
                    {
                        level.setBlock(x, 0, (byte) (1 + 9 * 16));
                    }
                }
            }
        }

        fixWalls();

        return level;
    }

    private int buildZone(int x, int maxLength, int maxHeight, int floor, int floorHeight)
    {
        int t = globalRandom.nextInt(totalOdds);
        int type = 0;
        //calculate what will be built
        for (int i = 0; i < odds.length; i++)
        {
            if (odds[i] <= t)
            {
                type = i;
            }
        }

       switch (type)
        {
            case ODDS_STRAIGHT:
                return buildStraight(x, maxLength, false, floor, floorHeight);
            case ODDS_HILL_STRAIGHT:
                if( floor == DEFAULT_FLOOR && hillStraightCount < cmdArgs[22] )
                {
                    hillStraightCount++;
                    return buildHillStraight(x, maxLength, floor);
                }
                else
                {
                    return 0;
                }
            case ODDS_TUBES:
                if( tubesCount < cmdArgs[23] )
                {
                    //increment of tubesCount is inside of the method
                    return buildTubes(x, maxLength, maxHeight, floor, floorHeight);
                }
                else
                {
                    return 0;
                }
            case ODDS_GAPS:
                if ((floor > 2 || floor == ANY_HEIGHT) && gapsCount < cmdArgs[26])
                {
                    gapsCount++;
                    return buildGap(x, 12, maxHeight, floor, floorHeight);
                }
                else
                {
                    return 0;
                }
            case ODDS_CANNONS:
                if (cannonsCount < cmdArgs[21])
                {
                    //increment of cannons is inside of the method
                    return buildCannons(x, maxLength, maxHeight, floor, floorHeight);
                }
                else
                {
                    return 0;
                }
            case ODDS_DEAD_ENDS:
            {
               if (floor == DEFAULT_FLOOR && deadEndsCount < cmdArgs[20]) //if method was not called from buildDeadEnds
               {
                   deadEndsCount++;
                   return buildDeadEnds(x, maxLength);
               }
            }
        }
        return 0;
    }


/*
    first component of sum : position on  Y axis
    second component of sum : position  on X axis
    starting at 0
    *16 because size of the picture is 16x16 pixels
    0+9*16 -- left side of the ground
    1+9*16 -- upper side of ground; common block telling "it's smth (ground) here". Is processed further.
    2+9*16 -- right side of the earth
    3+9*16 -- peice of the earth
    9+0*16 -- block of a ladder
    14+0*16 -- cannon barrel
    14+1*16 -- base for cannon barrel
    14+2*16 -- cannon pole
    4+8*16 -- left piece of a hill of ground
    4+11*16 -- left piece of a hill of ground as well
    6+8*16 --  right upper peice of a hill
    6+11*16 -- right upper peice of a hill on earth
    2+2*16 --  animated coin
    4+2+1*16 -- animated cube with question mark
    4+1+1*16 -- animated cube with question mark
    2+1*16 -- animated brick
    1+1*16 -- animated brick
    0+1*16 -- animated brick
    1+10*16 -- earth, bottom piece
    1+8*16 --  earth, upper piece
    3+10*16 -- piece of earth
    3+11*16 -- piece of earth
    2+8*16 -- right part of earth
    0+8*16 -- left upper part of earth
    3+8*16 -- piece of earth
    2+10*16 -- right bottomp iece of earth
    0+10*16 -- left bottom piece of earth
*/

    //x0 - first block to start from
    //maxLength - maximal length of the zone
    private int buildDeadEnds( int x0, int maxLength )
    {
        //first of all build pre dead end zone
        int floor = height - 2 - globalRandom.nextInt( 2 );  //floor of pre dead end zone
        int length = 0; // total zone length
        int preDeadEndLength = 7 + globalRandom.nextInt(10);
        int rHeight = floor-1; //rest height

        length += buildStraight(x0, preDeadEndLength, true, floor, INFINITY_FLOOR_HEIGHT);//buildZone( x0, x0+preDeadEndLength, floor ); //build pre dead end zone
        buildBlocks( x0, x0+preDeadEndLength, floor, true, 0, 0, true );

        //correct direction
        //true - top, false = bottom
        globalRandom.nextInt();
        int k = globalRandom.nextInt(5);//(globalRandom.nextInt() % (this.levelDifficulty+1));
        boolean direction = globalRandom.nextInt( k+1 ) != 1;

        int separatorY = 3 + globalRandom.nextInt(rHeight-7); //Y coordinate of the top line of the separator
        //Y coordinate of the bottom line of the separator is determined as separatorY + separatorHeight
        int separatorHeight = 2 + globalRandom.nextInt(2);

        int nx = x0 + length;
        int depth = 12 + globalRandom.nextInt(15) + this.difficulty;
        if (depth + length > maxLength)
        {
            while (depth + length > maxLength)
            {
                depth--;
            }
        }

        int tLength = 0;
        int bSpace = floor - (separatorY + separatorHeight);
        if (bSpace < 4)
        {
            while (bSpace < 4)
            {
                separatorY -= 1;
                bSpace = floor - (separatorY + separatorHeight);
            }
        }

        int wallWidth = 2 + globalRandom.nextInt(3);

        while( tLength < depth ) //top part
        {
            tLength += buildZone(nx+tLength,depth-tLength, separatorY-1, separatorY, separatorHeight);
        }
        tLength = 0;
        while( tLength < depth ) //bottom part
        {
            tLength += buildZone( nx+tLength, depth-tLength, bSpace, floor, INFINITY_FLOOR_HEIGHT);
        }

        for( int x = nx; x < nx + depth; x++ )
        {
            for( int y = 0; y < height; y++ )
            {
                if( x-nx >= depth-wallWidth )
                {
                    if (direction == RIGHT_DIRECTION_BOTTOM) //wall on the top
                    {
                        if( y <= separatorY)// + separatorHeight )
                        {
                            level.setBlock(x, y, (byte) (1 + 9 * 16));
                        }
                    }
                    else
                    {
                        if( y >= separatorY )
                        {
                            level.setBlock(x, y, (byte) (1 + 9 * 16));
                        }
                    }
                }
            }
        }


        return length+tLength;
    }

    private int buildGap(int xo, int maxLength, int maxHeight, int vfloor, int floorHeight)
    {
        int gs = globalRandom.nextInt(4) + 2; //GapStairs
        int gl = globalRandom.nextInt(2) + 2; //GapLength
//        System.out.println("globalRandom.nextInt() % this.levelDifficulty+1 = " +
        globalRandom.nextInt();
        int length = gs * 2 + gl + (globalRandom.nextInt() % this.levelDifficulty+1);

        boolean hasStairs = globalRandom.nextInt(3) == 0;
        if(maxHeight <= 5 && maxHeight != ANY_HEIGHT) //TODO: gs must be smaller than maxHeigth
        {
            hasStairs = false;
        }

        int floor = vfloor;
        if( vfloor == DEFAULT_FLOOR)
        {
            floor = height - 1 - globalRandom.nextInt(4);
        }
        else
        {
            globalRandom.nextInt();
            if (floor > 1)
            {
                floor -= 1;
            }
        }
        if( floorHeight == INFINITY_FLOOR_HEIGHT )
        {
            floorHeight = height - floor;
        }
        for (int x = xo; x < xo + length; x++)
        {
            if (x < xo + gs || x > xo + length - gs - 1)
            {
                for (int y = 0; y < height; y++)
                {
                    if (y >= floor && y <= floor + floorHeight)
                    {
                        level.setBlock(x, y, (byte) (1 + 9 * 16));
                    }
                    else if (hasStairs)
                    {
                        if (x < xo + gs)
                        {
                            if (y >= floor - (x - xo) + 1 && y <= floor + floorHeight)
                            {
                                level.setBlock(x, y, (byte) (9 + 0 * 16));
                            }
                        }
                        else
                        {
                            if (y >= floor - ((xo + length) - x) + 2 && y <= floor + floorHeight)
                            {
                                level.setBlock(x, y, (byte) (9 + 0 * 16));
                            }
                        }
                    }
                }
            }
        }

        if (length < 0) length = 1;
        if (length > maxLength) length = maxLength;
//        System.out.println("length = " + length);
        return length;
    }

    private int buildCannons(int xo, int maxLength, int maxHeight, int vfloor, int floorHeight)
    {
        int length = globalRandom.nextInt(10) + 2;
        if (length > maxLength) length = maxLength;

        int floor = vfloor;
        if( vfloor == DEFAULT_FLOOR)
        {
            floor = height - 1 - globalRandom.nextInt(4);
        }
        else
        {
            globalRandom.nextInt();
        }

        if( floorHeight == INFINITY_FLOOR_HEIGHT )
        {
            floorHeight = height - floor;
        }

        int xCannon = xo + 1 + globalRandom.nextInt(4);
        for (int x = xo; x < xo + length; x++)
        {
            if (x > xCannon)
            {
                xCannon += 2 + globalRandom.nextInt(4);
                cannonsCount++;
            }
            if (xCannon == xo + length - 1)
            {
                xCannon += 10;
            }

            int cannonHeight = floor - globalRandom.nextInt(3) - 1; //cannon height is a Y coordinate of top part of the cannon
            if (maxHeight != ANY_HEIGHT)
            {
                //maxHeight -= 2;
                if (floor - cannonHeight >= maxHeight)
                {
                    if (maxHeight > 4)
                    {
                        maxHeight = 4;
                    }
                    while( floor - cannonHeight > maxHeight )
                    {
                        cannonHeight++;
                    }
                }
            }

            for (int y = 0; y < height; y++)
            {
                if (y >= floor && y <= floor + floorHeight)
                {
                    level.setBlock(x, y, (byte) (1 + 9 * 16));
                }
                else if (cannonsCount <= cmdArgs[21])
                {
                    if (x == xCannon && y >= cannonHeight && y <= floor)// + floorHeight)
                    {
                        if (y == cannonHeight)
                        {
                            level.setBlock(x, y, (byte) (14 + 0 * 16));
                        }
                        else if (y == cannonHeight + 1)
                        {
                            level.setBlock(x, y, (byte) (14 + 1 * 16));
                        }
                        else
                        {
                            level.setBlock(x, y, (byte) (14 + 2 * 16));
                        }
                    }
                }
            }
        }

        return length;
    }

    private int buildHillStraight(int xo, int maxLength, int vfloor)
    {
        int length = globalRandom.nextInt(10) + 10;
        if (length > maxLength)
        {
            length = maxLength;
        }
/*        if( maxLength < 10 )
        {
            return 0;
        }
*/
        int floor = vfloor;
        if( vfloor == DEFAULT_FLOOR)
        {
            floor = height - 1 - globalRandom.nextInt(4);
        }
        else
        {
            globalRandom.nextInt();
        }
        for (int x = xo; x < xo + length; x++)
        {
            for (int y = 0; y < height; y++)
            {
                if (y >= floor)
                {
                    level.setBlock(x, y, (byte) (1 + 9 * 16));
                }
            }
        }

        addEnemyLine(xo + 1, xo + length - 1, floor - 1);

        int h = floor;

        boolean keepGoing = true;

        boolean[] occupied = new boolean[length];
        while (keepGoing)
        {
            h = h - 2 - globalRandom.nextInt(3);

            if (h <= 0)
            {
                keepGoing = false;
            }
            else
            {
                int l = globalRandom.nextInt(5) + 3;
                int xxo = globalRandom.nextInt(length - l - 2) + xo + 1;

                if (occupied[xxo - xo] || occupied[xxo - xo + l] || occupied[xxo - xo - 1] || occupied[xxo - xo + l + 1])
                {
                    keepGoing = false;
                }
                else
                {
                    occupied[xxo - xo] = true;
                    occupied[xxo - xo + l] = true;
                    addEnemyLine(xxo, xxo + l, h - 1);
                    if (globalRandom.nextInt(4) == 0)
                    {
                        decorate(xxo - 1, xxo + l + 1, h, false);
                        keepGoing = false;
                    }
                    for (int x = xxo; x < xxo + l; x++)
                    {
                        for (int y = h; y < floor; y++)
                        {
                            int xx = 5;
                            if (x == xxo) xx = 4;
                            if (x == xxo + l - 1) xx = 6;
                            int yy = 9;
                            if (y == h) yy = 8;

                            if (level.getBlock(x, y) == 0)
                            {
                                level.setBlock(x, y, (byte) (xx + yy * 16));
                            }
                            else
                            {
                                if (level.getBlock(x, y) == (byte) (4 + 8 * 16)) level.setBlock(x, y, (byte) (4 + 11 * 16));
                                if (level.getBlock(x, y) == (byte) (6 + 8 * 16)) level.setBlock(x, y, (byte) (6 + 11 * 16));
                            }
                        }
                    }
                }
            }
        }

        return length;
    }

/*
    0)goomba
    1)green coopa
    2)winged green coopa
    3)red coopa
    4)winged red coopa
    5)spiky
    6)winged spiky
*/

    private void addEnemyLine(int x0, int x1, int y)
    {
        String creatures = String.valueOf(cmdArgs[28]);

        while (creatures.length() < 7)
        {
            creatures = "0"+creatures;
        }

        boolean canAdd = true;
        for (int x = x0; x < x1; x++)
        {
            if (level.getBlock( x, y ) == -95)
            {
                canAdd = false;
                break;
            }
        }

        if (!canAddEnemyLine(x0, x1, y))
        {
            canAdd = false;
        }
        if(!canAdd) return;

        for (int x = x0; x < x1; x++)
        {
            if (creaturesRandom.nextInt(35) < difficulty + 1)
            {
                if (creatures.equals("1111111"))
                { //difficulty of creatures on the level depends on the difficulty of the level
                    int type = creaturesRandom.nextInt(4);
                    if (difficulty < 1)
                    {
                        type = Enemy.ENEMY_GOOMBA;
                    }
                    else if (difficulty < 3)
                    {
                        type = creaturesRandom.nextInt(3);
                    }
                    level.setSpriteTemplate(x, y, new SpriteTemplate(type, creaturesRandom.nextInt(35) < difficulty));
                }
                else
                {
                    boolean allowable = false;
                    int type = creaturesRandom.nextInt(4);
                    if (difficulty < 3)
                    {
                        creaturesRandom.nextInt(3);
                    }
                    Random locRnd = new Random();
                    do
                    {
                        type = locRnd.nextInt(7);
                        if (type == 7)
                        {
                            type--;
                        }
                        if (creatures.charAt(type) == '1')
                        {
                            allowable = true;
                        }
                    }
                    while (!allowable);

                    boolean winged = (type > 3);
                    if (type > 3)
                    {
                        type -= 3;
                    }
                    level.setSpriteTemplate(x, y, new SpriteTemplate(type, winged));
                }
            }
        }
    }

    private int buildTubes(int xo, int maxLength, int maxHeight, int vfloor, int floorHeight)
    {
        int tubes = 0;
        int length = globalRandom.nextInt(10) + 5;
        if (length > maxLength) length = maxLength;

        int floor = vfloor;
        if( vfloor == DEFAULT_FLOOR)
        {
            floor = height - 1 - globalRandom.nextInt(4);
        }
        else
        {
            globalRandom.nextInt();
        }
        int xTube = xo + 1 + globalRandom.nextInt(4);

        int tubeHeight = floor - globalRandom.nextInt(3) - 1; //и здесь

        if (maxHeight != ANY_HEIGHT)
        {
            //maxHeight -= 2;   //TODO: если че, то поправил здесь
            if (floor - tubeHeight > maxHeight)
            {
                if (maxHeight > 4)
                {
                    maxHeight = 4;
                }
                while( floor - tubeHeight > maxHeight )
                {
                    tubeHeight++;
                }
            }
        }

        //TODO: make this part like in BuildStraight. Not critical but unity of code style
        if( floorHeight == INFINITY_FLOOR_HEIGHT )
        {
            floorHeight = height - floor;
        }

        for (int x = xo; x < xo + length; x++)
        {
            if (x > xTube + 1)
            {
                xTube += 3 + globalRandom.nextInt(4);
                tubeHeight = floor - globalRandom.nextInt(2) - 2;
                if (maxHeight != ANY_HEIGHT)
                {
                    while( floor - tubeHeight > maxHeight-1 )
                    {
                         tubeHeight++;
                    }
                }
            }
            if (xTube >= xo + length - 2)
            {
                xTube += 10;
            }

            if (x == xTube && globalRandom.nextInt(11) < difficulty + 1 && cmdArgs[28] == 1)
            {
                level.setSpriteTemplate(x, tubeHeight, new SpriteTemplate(Enemy.ENEMY_FLOWER, false));
            }

            for (int y = 0; y < floor+floorHeight; y++)
            {
                if (y >= floor && y <= floor+floorHeight)
                {
                    level.setBlock(x, y, (byte) (1 + 9 * 16));
                }
                else
                {
                    if ((x == xTube || x == xTube + 1) && y >= tubeHeight)
                    {

                        int xPic = 10 + x - xTube;
                        if (y == tubeHeight)
                        {
                            level.setBlock(x, y, (byte) (xPic + 0 * 16));
                            if (x == xTube)
                            {
                                tubesCount++;
                            }
                        }
                        else
                        {
                            level.setBlock(x, y, (byte) (xPic + 1 * 16));
                        }
                    }
                }
            }
        }

        return length;
    }

    // parameter safe should be set to true iff length of the Straight > 10.
    // minimal length = 2
    //floorHeight - height of the floor. used for building of the top part of the dead end separator
    private int buildStraight(int xo, int maxLength, boolean safe, int vfloor, int floorHeight)
    {
        int length;
        if( floorHeight != INFINITY_FLOOR_HEIGHT )
        {
            length = maxLength;
        }
        else
        {
            length = globalRandom.nextInt(10) + 2;
            if (safe) length = 10 + globalRandom.nextInt(5);
            if (length > maxLength) length = maxLength;
        }


        int floor = vfloor;
        if( vfloor == DEFAULT_FLOOR)
        {
           floor = height - 1 - globalRandom.nextInt(4);
        }
        else
        {
            globalRandom.nextInt();
        }

        int y1 = height;
        if( floorHeight != INFINITY_FLOOR_HEIGHT )
        {
            y1 = floor + floorHeight;
        }

        for (int x = xo; x < xo + length; x++)
        {
            for (int y = floor; y < y1; y++)
            {
                if (y >= floor)
                {
                    level.setBlock(x, y, (byte) (1 + 9 * 16));
                }
            }
        }

        if (!safe)
        {
            if (length > 5)
            {
                decorate(xo, xo + length, floor, false);
            }
        }

        return length;
    }

    private boolean canBuildBlocks( int x0, int floor, boolean isHB )
    {
        if ((blocksCount >= cmdArgs[24] && !isHB))
        {
            return false;
        }

        boolean res = true;

        if( floor < 4 )
        {
            return false;
        }

        for (int y = 0; y < 4; y++)
        {
            if (level.getBlock(x0,floor-y) != 0)
            {
                res = false;
                break;
            }
        }

        return res;
    }

    private void buildBlocks(int x0, int x1, int floor, boolean pHB, int pS, int pE, boolean onlyHB)
    {
        if (blocksCount > cmdArgs[24])
        {
            return;
        }
        int s = pS; //Start
        int e = pE; //End
        boolean hb = pHB;

        if( onlyHB )
            hb = onlyHB;

        while(floor - 4 > 0) //minimal distance between the bricks line and floor is 4
        {
            if ((x1 - 1 - e) - (x0 + 1 + s) > 0) //minimal number of bricks in the line is positive value
            {
                for (int x = x0 + s; x < x1  - e; x++)
                {
                    if(hb && cmdArgs[27] != 0) //if hidden blocks to be built
                    {
                        boolean isBlock = globalRandom.nextInt(2) == 1;
                        if(isBlock && canBuildBlocks(x, floor-4, true))
                        {
                            level.setBlock(x, floor - 4, (byte) (1)); //hidden block
                        }
                    }
                    else
                    {
                        boolean canDeco = false; //can add enemy line and coins
                        //decorate( x0, x1, floor, true );
                        if (x != x0 + 1 && x != x1 - 2 && globalRandom.nextInt(3) == 0)
                        {
                            if (canBuildBlocks(x, floor-4, false))
                            {
                                blocksCount++;
                                if ((globalRandom.nextInt(4) == 0))
                                {
                                    level.setBlock(x, floor - 4, (byte) (4 + 2 + 1 * 16)); //a rock with animated question symbol with flower. when broken becomes a rock
                                }
                                else
                                {
                                   level.setBlock(x, floor - 4, (byte) (4 + 1 + 1 * 16)); //a brick with animated question symbol. when broken becomes a rock
                                }
                                canDeco = true;
                            }
                        }
                        else if (globalRandom.nextInt(4) == 0)
                        {
                            if  (canBuildBlocks(x, floor-4, false))
                            {
                                blocksCount++;
                                if (globalRandom.nextInt(4) == 0)
                                {
                                    level.setBlock(x, floor - 4, (byte) (2 + 1 * 16)); //a brick with a flower. when broken becomes a rock
                                }
                                else
                                {
                                    level.setBlock(x, floor - 4, (byte) (1 + 1 * 16)); //a brick with a coin. when broken becomes a rock
                                }
                                canDeco = true;
                            }
                        }
                        else if(globalRandom.nextInt(2)==1 && canBuildBlocks(x, floor-4, false))
                        {
                            blocksCount++;
                            level.setBlock(x, floor - 4, (byte) (0 + 1 * 16)); //a break brick
                            canDeco = true;
                        }
                        if (canDeco)
                        {
                            if( globalRandom.nextInt(4) == 2 ) addEnemyLine(x0 + 1, x1 - 1, floor - 1);
                            buildCoins( x0, x1, floor, s, e );                                
                        }
                    }
                }
                if (onlyHB)
                {
                    hb = true;
                }
                else
                {
                    hb = globalRandom.nextBoolean();//globalRandom.nextInt(3) == globalRandom.nextInt(3);
                }
            }
            floor -= 4;
            s = globalRandom.nextInt(4);
            e = globalRandom.nextInt(4);
        }
        globalRandom.nextBoolean();
    }

    private void buildCoins( int x0, int x1, int floor, int s, int e )
    {
        if( floor - 2 < 0 ) return;

        if ((x1 - 1 - e) - (x0 + 1 + s) > 1)
        {
            for (int x = x0 + 1 + s; x < x1 - 1 - e; x++)
            {
                if (coinsCount >= cmdArgs[25])
                {
                    break;
                }
                if( level.getBlock( x, floor - 2 ) == 0 ) //if cell (x, floor-2) is empty
                {
                    coinsCount++;
                    level.setBlock(x, floor - 2, (byte) (2 + 2 * 16)); //coin
                }
            }
        }
    }

    private boolean canAddEnemyLine( int x0, int x1, int floor )
    {
        if (cmdArgs[28] == 0x0) //not one bit selected
        {
            return false;
        }
        boolean res = true;
        for (int x = x0; x < x1; x++)
        {
            for (int y = floor; y > floor-2; y--)
            {
                if (level.getBlock(x, y) != 0)
                {
                    res = false;
                    break;
                }
            }
        }

        return res;
    }

    private void decorate(int x0, int x1, int floor, boolean recurs)
    {
        if (floor < 1) return;

        int s = globalRandom.nextInt(4);
        int e = globalRandom.nextInt(4);
        boolean hb = ((globalRandom.nextInt(levelDifficulty+1) % (levelDifficulty + 1))) > 0.5;

        if (!hb)
        {
            addEnemyLine(x0 + 1, x1 - 1, floor - 1);
        }

        if (floor - 2 > 0 && !hb )
        {
            buildCoins( x0, x1, floor, s, e );
        }

        if (!recurs) buildBlocks( x0, x1, floor, hb, s, e, false);

        int length = x1 - x0 - 2;

        if (length > 2)
        {
           //decorate(x0, x1, floor - 4);
           globalRandom.nextInt();
        }
    }

    private void fixWalls()
    {
        boolean[][] blockMap = new boolean[width + 1][height + 1];
        for (int x = 0; x < width + 1; x++)
        {
            for (int y = 0; y < height + 1; y++)
            {
                int blocks = 0;
                for (int xx = x - 1; xx < x + 1; xx++)
                {
                    for (int yy = y - 1; yy < y + 1; yy++)
                    {
                        if (level.getBlockCapped(xx, yy) == (byte) (1 + 9 * 16)) blocks++;
                    }
                }
                blockMap[x][y] = blocks == 4;
            }
        }
        blockify(level, blockMap, width + 1, height + 1);
    }

    private void blockify(Level level, boolean[][] blocks, int width, int height)
    {
        int to = 0;
        if (type == LevelGenerator.TYPE_CASTLE)
        {
            to = 4 * 2;
        }
        else if (type == LevelGenerator.TYPE_UNDERGROUND)
        {
            to = 4 * 3;
        }

        boolean[][] b = new boolean[2][2];
        for (int x = 0; x < width; x++)
        {
            for (int y = 0; y < height; y++)
            {
                for (int xx = x; xx <= x + 1; xx++)
                {
                    for (int yy = y; yy <= y + 1; yy++)
                    {
                        int _xx = xx;
                        int _yy = yy;
                        if (_xx < 0) _xx = 0;
                        if (_yy < 0) _yy = 0;
                        if (_xx > width - 1) _xx = width - 1;
                        if (_yy > height - 1) _yy = height - 1;
                        b[xx - x][yy - y] = blocks[_xx][_yy];
                    }
                }

                if (b[0][0] == b[1][0] && b[0][1] == b[1][1])
                {
                    if (b[0][0] == b[0][1])
                    {
                        if (b[0][0])
                        {
                            level.setBlock(x, y, (byte) (1 + 9 * 16 + to));
                        }
                        else
                        {
                            // KEEP OLD BLOCK!
                        }
                    }
                    else
                    {
                        if (b[0][0])
                        {
                            level.setBlock(x, y, (byte) (1 + 10 * 16 + to));
                        }
                        else
                        {
                            level.setBlock(x, y, (byte) (1 + 8 * 16 + to));
                        }
                    }
                }
                else if (b[0][0] == b[0][1] && b[1][0] == b[1][1])
                {
                    if (b[0][0])
                    {
                        level.setBlock(x, y, (byte) (2 + 9 * 16 + to));
                    }
                    else
                    {
                        level.setBlock(x, y, (byte) (0 + 9 * 16 + to));
                    }
                }
                else if (b[0][0] == b[1][1] && b[0][1] == b[1][0])
                {
                    level.setBlock(x, y, (byte) (1 + 9 * 16 + to));
                }
                else if (b[0][0] == b[1][0])
                {
                    if (b[0][0])
                    {
                        if (b[0][1])
                        {
                            level.setBlock(x, y, (byte) (3 + 10 * 16 + to));
                        }
                        else
                        {
                            level.setBlock(x, y, (byte) (3 + 11 * 16 + to));
                        }
                    }
                    else
                    {
                        if (b[0][1])
                        {
                            level.setBlock(x, y, (byte) (2 + 8 * 16 + to));
                        }
                        else
                        {
                            level.setBlock(x, y, (byte) (0 + 8 * 16 + to));
                        }
                    }
                }
                else if (b[0][1] == b[1][1])
                {
                    if (b[0][1])
                    {
                        if (b[0][0])
                        {
                            level.setBlock(x, y, (byte) (3 + 9 * 16 + to));
                        }
                        else
                        {
                            level.setBlock(x, y, (byte) (3 + 8 * 16 + to));
                        }
                    }
                    else
                    {
                        if (b[0][0])
                        {
                            level.setBlock(x, y, (byte) (2 + 10 * 16 + to));
                        }
                        else
                        {
                            level.setBlock(x, y, (byte) (0 + 10 * 16 + to));
                        }
                    }
                }
                else
                {
                    level.setBlock(x, y, (byte) (0 + 1 * 16 + to));
                }
            }
        }
    }
}