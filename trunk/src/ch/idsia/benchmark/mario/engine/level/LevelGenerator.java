package ch.idsia.benchmark.mario.engine.level;

import ch.idsia.benchmark.mario.engine.sprites.Sprite;
import ch.idsia.tools.CmdLineOptions;
import ch.idsia.tools.CreaturesMaskParser;

import java.awt.*;
import java.util.HashSet;
import java.util.Random;
import java.util.Vector;

/**
 * Using this class is very simple. Just call <b>createMethod</b> with params:
 * <ul>
 * <li>length -- length of the level in cells. On the screen one cell has 16 pixels </li>
 * <li>height -- height of the level in cells. On the screen one cell has 16 pixels </li>
 * <li>seed -- use this param to make a globalRandom level.
 * On different machines with the same seed param there will be one level</li>
 * <li>levelDifficulty -- use this param to change difficult of the level.
 * On different machines with the same seed param there will be one level</li>
 * <li>levelType -- levelType of the level. One of Overground, Underground, Castle.</li>
 * </ul>
 *
 * @see #TYPE_OVERGROUND
 * @see #TYPE_UNDERGROUND
 * @see #TYPE_CASTLE
 */

public class LevelGenerator
{
private static int k = 2;
/*
From left to right:
    0)goomba
    1)green koopa
    2)red koopa
    3)spiky
    4)winged goomba
    5)winged green koopa
    6)winged red koopa
    7)winged spiky
    8)spiky flower
*/

private static class creaturesSpreader
{
    int maxFitness = 0;

    public void getCreatures()
    {
        HashSet used = new HashSet();
        creaturesToBuild.clear();

        int pointsCount = creaturesRandom.nextInt((int) Math.log(levelDifficulty * levelDifficulty + levelDifficulty));//creaturesRandom.nextInt((levelDifficulty + 5)%(creaturesPos.size()+1)+1);
        if (pointsCount > creaturesPos.size())
        {
            pointsCount = creaturesPos.size();
//            pointsCount = (int) (pointsCount - ((float)5/13)*creaturesPos.size());
        }
//        else if (pointsCount > ((float)5/13)*creaturesPos.size())
//            pointsCount -= (((float)2/13)*creaturesPos.size());// - 2;creaturesRandom.nextInt(2);

        int i = 0;
        while (i < pointsCount)
        {
            int p = creaturesRandom.nextInt(creaturesPos.size());
            if (!used.contains(p))
            {
                used.add(p);
                creaturesToBuild.add(creaturesPos.get(p));
                ++i;
            }
        }
    }
}

public static final int TYPE_OVERGROUND = 0;
public static final int TYPE_UNDERGROUND = 1;
public static final int TYPE_CASTLE = 2;

public static final int DEFAULT_FLOOR = -1;

public static final int LevelLengthMinThreshold = 50; // minimal length of the level. used in ToolsConfigurator
private static boolean isFlatLevel;

private static int length;
private static int height;
private static Level level;
private static Random globalRandom = new Random(0);
private static Random creaturesRandom = new Random(0);

private static final int ODDS_STRAIGHT = 0;
private static final int ODDS_HILL_STRAIGHT = 1;
private static final int ODDS_TUBES = 2;
private static final int ODDS_GAPS = 3;
private static final int ODDS_CANNONS = 4;
private static final int ODDS_DEAD_ENDS = 5;
private static int[] odds = new int[6];
private static int totalOdds;
private static int levelDifficulty;
private static int levelType;
private static int levelSeed;

private static CreaturesMaskParser creaturesMaskParser;
private static Vector<Point> creaturesPos = new Vector<Point>();
private static Vector<Point> creaturesToBuild = new Vector<Point>();

private static final boolean RIGHT_DIRECTION_BOTTOM = false;
private static final int ANY_HEIGHT = -1;
private static final int INFINITY_FLOOR_HEIGHT = Integer.MAX_VALUE;

//Level customization counters
static Level.objCounters counters = new Level.objCounters();

private LevelGenerator() {}

public static Level createLevel(CmdLineOptions args)
{
    length = args.getLevelLength();
    System.out.println("args.getLevelLength() = " + args.getLevelLength());
    height = args.getLevelHeight();
    if (height < 15)
    {
        System.err.println("[MarioAI WARNING] : Minimal height of the level must be 15! Changed to 15");
        height = 15;
    }
    isFlatLevel = args.isFlatLevel();
    counters.totalHillStraight = args.getHillStraightCount() ? Integer.MAX_VALUE : 0;
    counters.totalCannons = args.getCannonsCount() ? Integer.MAX_VALUE : 0;
    counters.totalGaps = args.getGapsCount() ? Integer.MAX_VALUE : 0;
    counters.totalDeadEnds = args.getDeadEndsCount() ? Integer.MAX_VALUE : 0;
    counters.totalBlocks = args.getBlocksCount() ? Integer.MAX_VALUE : 0;
    counters.totalHiddenBlocks = args.getHiddenBlocksCount() ? Integer.MAX_VALUE : 0;
    counters.totalCoins = args.getCoinsCount() ? Integer.MAX_VALUE : 0;
    counters.totalTubes = args.getTubesCount() ? Integer.MAX_VALUE : 0;

    creaturesMaskParser = new CreaturesMaskParser(args.getEnemies());

    levelType = args.getLevelType();
    levelDifficulty = args.getLevelDifficulty();
    odds[ODDS_STRAIGHT] = 20;
    odds[ODDS_HILL_STRAIGHT] = 1;
    odds[ODDS_TUBES] = 2 + 1 * levelDifficulty;
    odds[ODDS_GAPS] = 3 * levelDifficulty;
    odds[ODDS_CANNONS] = -10 + 5 * levelDifficulty;
    odds[ODDS_DEAD_ENDS] = 2 + 2 * levelDifficulty;

    if (levelType != LevelGenerator.TYPE_OVERGROUND)
        odds[ODDS_HILL_STRAIGHT] = 0; //if not overground then there are no hill straight

    totalOdds = 0;
    for (int i = 0; i < odds.length; i++)
    {
        if (odds[i] < 0) odds[i] = 0;
        totalOdds += odds[i];
        odds[i] = totalOdds - odds[i];
    }

    level = new Level(length, height);
    levelSeed = args.getLevelRandSeed();// + levelType;
//        System.out.println("Level seed: " + String.valueOf(levelSeed));
    globalRandom.setSeed(levelSeed);
    creaturesRandom.setSeed(levelSeed);

    int length = 0; //total level length
    //mario starts on straight

    int floor = DEFAULT_FLOOR;
    if (isFlatLevel)
    {
        floor = height - 1 - globalRandom.nextInt(4);
    }

    length += buildStraight(0, level.length, true, floor, INFINITY_FLOOR_HEIGHT);
    while (length < level.length - 10)
    {
        length += buildZone(length, level.length - length, ANY_HEIGHT, floor, INFINITY_FLOOR_HEIGHT);
    }

    if (!isFlatLevel)  //NOT flat level
    {
        floor = height - 1 - globalRandom.nextInt(4); //floor of the exit line
    }

    //coordinates of the exit
    level.xExit = level.length;
    level.yExit = floor;

    //level zone where exit is located
    for (int x = length; x < level.length; x++)
    {
        for (int y = 0; y < height; y++)
        {
            if (y >= floor)
            {
                level.setBlock(x, y, (byte) (1 + 9 * 16));
            }
        }
    }

    //if underground or castle then build ceiling
    if (levelType == LevelGenerator.TYPE_CASTLE || levelType == LevelGenerator.TYPE_UNDERGROUND)
    {
        int ceiling = 0;
        int run = 0;
        for (int x = 0; x < level.length; x++)
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

//        System.out.println("deadEndsCount = " + counters.deadEndsCount);
//        System.out.println("cannonsCount = " + counters.cannonsCount);
//        System.out.println("hillStraightCount = " + counters.hillStraightCount);
//        System.out.println("tubesCount = " + counters.tubesCount);
//        System.out.println("blocksCount = " + counters.blocksCount);
//        System.out.println("coinsCount = " + counters.coinsCount);
//        System.out.println("gapsCount = " + counters.gapsCount);
//        System.out.println("hiddenBlocksCount = " + counters.hiddenBlocksFound);
//
//        System.out.println("total deadEndsCount = " + counters.totalDeadEnds);
//        System.out.println("total cannonsCount = " + counters.totalCannons);
//        System.out.println("total hillStraightCount = " + counters.totalHillStraight);
//        System.out.println("total tubesCount = " + counters.totalTubes);
//        System.out.println("total blocksCount = " + counters.totalBlocks);
//        System.out.println("total coinsCount = " + counters.totalCannons);
//        System.out.println("total gapsCount = " + counters.totalGaps);
//        System.out.println("total hiddenBlocksCount = " + counters.totalHiddenBlocks);

    level.counters = counters;

    return level;
}

private static int buildZone(int x, int maxLength, int maxHeight, int floor, int floorHeight)
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

    for (int y = 0; y < levelDifficulty; y++)
        addEnemy(x, y % (level.height - 3));

    switch (type)
    {
        case ODDS_STRAIGHT:
            return buildStraight(x, maxLength, false, floor, floorHeight);
        case ODDS_HILL_STRAIGHT:
            if (floor == DEFAULT_FLOOR && counters.hillStraightCount < counters.totalHillStraight)
            {
                counters.hillStraightCount++;
                return buildHillStraight(x, maxLength, floor);
            } else
            {
                return 0;
            }
        case ODDS_TUBES:
            //increment of tubesCount is inside of the method
            if (counters.tubesCount < counters.totalTubes)
                return buildTubes(x, maxLength, maxHeight, floor, floorHeight);
            else
                return 0;
        case ODDS_GAPS:
            if ((floor > 2 || floor == ANY_HEIGHT) && (counters.gapsCount < counters.totalGaps))
            {
                counters.gapsCount++;
                return buildGap(x, maxLength > 12 ? 12 : maxLength, maxHeight, floor, floorHeight);
            } else
            {
                return 0;
            }
        case ODDS_CANNONS:
            if (counters.cannonsCount < counters.totalCannons)
            {
                //increment of cannonsCount is inside of the method
                return buildCannons(x, maxLength, maxHeight, floor, floorHeight);
            } else
            {
                return 0;
            }
        case ODDS_DEAD_ENDS:
        {
            if (floor == DEFAULT_FLOOR && counters.deadEndsCount < counters.totalDeadEnds) //if method was not called from buildDeadEnds
            {
                counters.deadEndsCount++;
                return buildDeadEnds(x, maxLength);
            }
        }
    }


    return 0;
}

public static Random XRnd = new Random(0);

public static void addEnemy(int x, int y)
{
    int crType = creaturesRandom.nextInt(4);

    int t = creaturesMaskParser.getNativeType(crType);
    int dx = (int) XRnd.nextGaussian();
//    System.out.println("dx = " + dx);
    level.setSpriteTemplate(x + dx, y, new SpriteTemplate(t));
    ++counters.creatures;
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
    4+2+1*16 -- a rock with animated question symbol with power up
    4+1+1*16 -- a rock with animated question symbol with coin
    2+1*16 -- brick with power up. when broken becomes a rock
    1+1*16 -- brick with power coin. when broken becomes a rock
    0+1*16 -- break brick
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

private static int buildDeadEnds(int x0, int maxLength)
{
    //first of all build pre dead end zone
    int floor = height - 2 - globalRandom.nextInt(2);  //floor of pre dead end zone
    int length = 0; // total zone length
    int preDeadEndLength = 7 + globalRandom.nextInt(10);
    int rHeight = floor - 1; //rest height

    length += buildStraight(x0, preDeadEndLength, true, floor, INFINITY_FLOOR_HEIGHT);//buildZone( x0, x0+preDeadEndLength, floor ); //build pre dead end zone
    buildBlocks(x0, x0 + preDeadEndLength, floor, true, 0, 0, true, true);

    //correct direction
    //true - top, false = bottom
    globalRandom.nextInt();
    int k = globalRandom.nextInt(5);//(globalRandom.nextInt() % (this.levelDifficulty+1));
    boolean direction = globalRandom.nextInt(k + 1) != 1;

    int separatorY = 3 + globalRandom.nextInt(rHeight - 7); //Y coordinate of the top line of the separator
    //Y coordinate of the bottom line of the separator is determined as separatorY + separatorHeight
    int separatorHeight = 2 + globalRandom.nextInt(2);

    int nx = x0 + length;
    int depth = globalRandom.nextInt(levelDifficulty) + 2 * levelDifficulty;
    if (depth + length > maxLength)
    {
//        depth = maxLength
        while (depth + length > maxLength - 1)
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

    while (tLength < depth) //top part
    {
        tLength += buildZone(nx + tLength, depth - tLength, separatorY - 1, separatorY, separatorHeight);
    }
    tLength = 0;
    while (tLength < depth) //bottom part
    {
        tLength += buildZone(nx + tLength, depth - tLength, bSpace, floor, INFINITY_FLOOR_HEIGHT);
    }

    for (int x = nx; x < nx + depth; x++)
    {
        for (int y = 0; y < height; y++)
        {
            if (x - nx >= depth - wallWidth)
            {
                if (direction == RIGHT_DIRECTION_BOTTOM) //wall on the top
                {
                    if (y <= separatorY)// + separatorHeight )
                    {
                        level.setBlock(x, y, (byte) (1 + 9 * 16));
                    }
                } else
                {
                    if (y >= separatorY)
                    {
                        level.setBlock(x, y, (byte) (1 + 9 * 16));
                    }
                }
            }
        }
    }

    return length + tLength;
}

private static int buildGap(int xo, int maxLength, int maxHeight, int vfloor, int floorHeight)
{
    creaturesPos.clear();

    int gs = globalRandom.nextInt(4) + 2; //GapStairs
    int gl = globalRandom.nextInt(2) + 2; //GapLength
//        System.out.println("globalRandom.nextInt() % this.levelDifficulty+1 = " +
    globalRandom.nextInt();
    int length = gs * 2 + gl + (globalRandom.nextInt() % levelDifficulty + 1);

    boolean hasStairs = globalRandom.nextInt(3) == 0;
    if (maxHeight <= 5 && maxHeight != ANY_HEIGHT) //TODO: gs must be smaller than maxHeigth
    {
        hasStairs = false;
    }

    int floor = vfloor;
    if (vfloor == DEFAULT_FLOOR && !isFlatLevel)
    {
        floor = height - 1 - globalRandom.nextInt(4);
    } else //code in this block is a magic. don't change it
    {
        floor++;
        globalRandom.nextInt();
        if (floor > 1)
        {
            floor -= 1;
        }
    }

    if (floorHeight == INFINITY_FLOOR_HEIGHT)
    {
        floorHeight = height - floor;
    }

    if (gs > 3 && creaturesRandom.nextInt(35) > levelDifficulty + 1 && !hasStairs)
    {
//        addEnemiesLine(xo, xo + gs - 1, floor - 1);
//        addEnemiesLine(xo + length - gs, xo + length - 1, floor - 1);
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
                    if (y == floor && !hasStairs)
                        creaturesPos.add(new Point(x, y - k));
                } else if (hasStairs)
                {
                    if (x < xo + gs)
                    {
                        if (y >= floor - (x - xo) + 1 && y <= floor + floorHeight)
                        {
                            level.setBlock(x, y, (byte) (9 + 0 * 16));
                        }
                    } else
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

    if (length < 0) length = 1; //TODO: check this conditions. move it to the begining?
    if (length > maxLength) length = maxLength;
//        System.out.println("length = " + length);creaturesSpreader cs = new creaturesSpreader();

    creaturesSpreader cs = new creaturesSpreader();
    cs.getCreatures();

    for (Point v : creaturesToBuild)
    {
        addEnemiesLine(v.x, v.x + 1, v.y);
    }
    return length;
}

private static int buildCannons(int xo, int maxLength, int maxHeight, int vfloor, int floorHeight)
{
    creaturesPos.clear();

    int maxCannonHeight = 0;
    int length = globalRandom.nextInt(10) + 2;
    if (length > maxLength) length = maxLength;

    int floor = vfloor;
    if (vfloor == DEFAULT_FLOOR)
    {
        floor = height - 1 - globalRandom.nextInt(4);
    } else
    {
        globalRandom.nextInt();
    }

    if (floorHeight == INFINITY_FLOOR_HEIGHT)
    {
        floorHeight = height - floor;
    }

    int oldXCannon = -1;

    int xCannon = xo + 1 + globalRandom.nextInt(4);
    for (int x = xo; x < xo + length; x++)
    {
        if (x > xCannon)
        {
            xCannon += 2 + globalRandom.nextInt(4);
            counters.cannonsCount++;
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
                while (floor - cannonHeight > maxHeight)
                {
                    cannonHeight++;
                }
            }
            if (cannonHeight > maxCannonHeight)
                maxCannonHeight = cannonHeight;
        }

        for (int y = 0; y < height; y++)
        {
            if (y >= floor && y <= floor + floorHeight) //TODO: build straight
            {
                level.setBlock(x, y, (byte) (1 + 9 * 16));
                if (y == floor && x != xCannon)
                    creaturesPos.add(new Point(x, y - 1)); //TODO: loop; reserve creatures pos
            } else if (counters.cannonsCount <= counters.totalCannons)
            {
                if (x == xCannon && y >= cannonHeight && y <= floor)// + floorHeight)
                {
                    if (y == cannonHeight)
                    {
                        if (oldXCannon != -1 && creaturesRandom.nextInt(35) > levelDifficulty + 1)
                        {
//                            addEnemiesLine(oldXCannon + 1, xCannon - 1, floor - 1);
                        }
                        oldXCannon = x;
                        level.setBlock(x, y, (byte) (14 + 0 * 16));   // cannon barrel
                    } else if (y == cannonHeight + 1)
                    {
                        level.setBlock(x, y, (byte) (14 + 1 * 16));   // base for cannon barrel
                    } else
                    {
                        level.setBlock(x, y, (byte) (14 + 2 * 16));   // cannon pole
                    }
                }
            }
        }
    }

    if (globalRandom.nextBoolean())
        buildBlocks(xo, xo + length, floor - maxCannonHeight - 2, false, 0, 0, false, false);

    creaturesSpreader cs = new creaturesSpreader();
    cs.getCreatures();

    for (Point v : creaturesToBuild)
    {
        addEnemiesLine(v.x, v.x + 1, v.y);
    }

    return length;
}

private static int buildHillStraight(int xo, int maxLength, int vfloor)
{
    creaturesPos.clear();
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
    if (vfloor == DEFAULT_FLOOR)
    {
        floor = height - 1 - globalRandom.nextInt(4);
    } else
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

//    addEnemiesLine(xo + 1, xo + length - 1, floor - 1);
    for (int i = xo + 1; i < xo + length - 1; i++)
        creaturesPos.add(new Point(i, floor - 1));

    int h = floor;

    boolean keepGoing = true;

    boolean[] occupied = new boolean[length];
    while (keepGoing)
    {
        h = h - 2 - globalRandom.nextInt(3);

        if (h <= 0)
        {
            keepGoing = false;
        } else
        {
            int l = globalRandom.nextInt(5) + 3;
            int xxo = globalRandom.nextInt(length - l - 2) + xo + 1;

            if (occupied[xxo - xo] || occupied[xxo - xo + l] || occupied[xxo - xo - 1] || occupied[xxo - xo + l + 1])
            {
                keepGoing = false;
            } else
            {
                occupied[xxo - xo] = true;
                occupied[xxo - xo + l] = true;
//                addEnemiesLine(xxo, xxo + l, h - 1);
                for (int i = xxo; i < xxo + l; i++)
                    creaturesPos.add(new Point(i, h - k));
                if (globalRandom.nextInt(4) == 0)
                {
                    decorate(xxo - 1, xxo + l + 1, h);
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
                        } else
                        {
                            if (level.getBlock(x, y) == (byte) (4 + 8 * 16))
                                level.setBlock(x, y, (byte) (4 + 11 * 16));
                            if (level.getBlock(x, y) == (byte) (6 + 8 * 16))
                                level.setBlock(x, y, (byte) (6 + 11 * 16));
                        }
                    }
                }
            }
        }
    }

    creaturesSpreader cs = new creaturesSpreader();
    cs.getCreatures();

    for (Point v : creaturesToBuild)
    {
        addEnemiesLine(v.x, v.x + 1, v.y);
    }

    return length;
}

private static boolean canAddEnemyLine(int x0, int x1, int y)
{
    if (!creaturesMaskParser.canAdd())
    {
        return false;
    }
    boolean res = true;
    for (int x = x0; x < x1; x++)
    {
        for (int yy = y; yy > y + 1; yy++)
        {
            if (level.getBlock(x, yy) != 0)
            {
                res = false;
                break;
            }
        }
    }

    return res;
}

private static void addEnemiesLine(int x0, int x1, int y)
{
    if (x0 > 0)
        return;
//    Random locRnd = new Random(levelSeed); //TODO: move to static

    if (!canAddEnemyLine(x0, x1, y))
        return;

    for (int x = x0; x < x1; x++)
    {
        if (creaturesRandom.nextInt(25) < levelDifficulty + 1)
        {
            if (creaturesMaskParser.isComplete())
            { //Difficulty of creatures on the level depends on the levelDifficulty of the level
                int type = creaturesRandom.nextInt(4);
                if (levelDifficulty < 1)
                {
                    type = CreaturesMaskParser.GOOMBA;
                } else if (levelDifficulty < 3)
                {
                    int type1 = creaturesRandom.nextInt(3);
                    int type2 = creaturesRandom.nextInt(3) + 3;
                    type = creaturesRandom.nextInt(2) == 1 ? type1 : type2;
                }
                type = creaturesMaskParser.getNativeType(type);
                level.setSpriteTemplate(x, y, new SpriteTemplate(type));
                ++counters.creatures;
            } else
            {
                boolean enabled = false;
                int crType;// = creaturesRandom.nextInt(4);
                if (levelDifficulty < 3)
                {
                    creaturesRandom.nextInt(3);
                }
                do
                {
                    crType = creaturesRandom.nextInt(8);
                    if (creaturesMaskParser.isEnabled(crType))
                    {
                        enabled = true;
                    }
                }
                while (!enabled);

                int t = creaturesMaskParser.getNativeType(crType);
                level.setSpriteTemplate(x, y, new SpriteTemplate(t));
                ++counters.creatures;
            }
        }
    }
}

private static int buildTubes(int xo, int maxLength, int maxHeight, int vfloor, int floorHeight)
{
    creaturesPos.clear();

    int maxTubeHeight = 0;
    int length = globalRandom.nextInt(10) + 5;
    if (length > maxLength) length = maxLength;

    int floor = vfloor;
    if (vfloor == DEFAULT_FLOOR)
    {
        floor = height - 1 - globalRandom.nextInt(4);
    } else
    {
        globalRandom.nextInt();
    }
    int xTube = xo + 1 + globalRandom.nextInt(4);

    int tubeHeight = floor - globalRandom.nextInt(3) - 1;

    if (maxHeight != ANY_HEIGHT)
    {
        //maxHeight -= 2;
        if (floor - tubeHeight > maxHeight)
        {
            if (maxHeight > 4)
            {
                maxHeight = 4;
            }
            while (floor - tubeHeight > maxHeight)
            {
                tubeHeight++;
            }
        }
    }

    //TODO: make this part like in BuildStraight. Not critical but unity of code style
    if (floorHeight == INFINITY_FLOOR_HEIGHT)
    {
        floorHeight = height - floor;
    }

    int oldXTube = -1;

    for (int x = xo; x < xo + length; x++)
    {
        if (x > xTube + 1)
        {
            xTube += 3 + globalRandom.nextInt(4);
            tubeHeight = floor - globalRandom.nextInt(2) - 2;
            if (maxHeight != ANY_HEIGHT)
            {
                while (floor - tubeHeight > maxHeight - 1)
                {
                    tubeHeight++;
                }
            }

            if (tubeHeight > maxTubeHeight)
                maxTubeHeight = tubeHeight;
        }
        if (xTube >= xo + length - 2)
        {
            xTube += 10;
        }

        if (x == xTube && globalRandom.nextInt(11) < levelDifficulty + 1 && creaturesMaskParser.isEnabled(CreaturesMaskParser.SPIKY_FLOWER))
        {
            level.setSpriteTemplate(x, tubeHeight, new SpriteTemplate(Sprite.KIND_ENEMY_FLOWER));
            ++counters.creatures;
        }

        for (int y = 0; y < floor + floorHeight; y++)
        {
            if (y >= floor && y <= floor + floorHeight) //TODO: build straight
            {
                level.setBlock(x, y, (byte) (1 + 9 * 16));
                if (y == floor && x != xTube && x != xTube + 1)
                    creaturesPos.add(new Point(x, y - k));
            } else
            {
                if ((x == xTube || x == xTube + 1) && y >= tubeHeight)
                {

                    int xPic = 10 + x - xTube;
                    if (y == tubeHeight)
                    {
                        level.setBlock(x, y, (byte) (xPic + 0 * 16));
                        if (x == xTube)
                        {
                            if (oldXTube != -1 && creaturesRandom.nextInt(35) > levelDifficulty + 1)
                            {
//                                addEnemiesLine(oldXTube + 2, xTube - 1, floor - 1);
                            }
                            oldXTube = x;
                            counters.tubesCount++;
                        }
                    } else
                    {
                        level.setBlock(x, y, (byte) (xPic + 1 * 16));
                    }
                }
            }
        }
    }

    if (globalRandom.nextBoolean())
        buildBlocks(xo, xo + length, floor - maxTubeHeight - 2, false, 0, 0, false, false);

    creaturesSpreader cs = new creaturesSpreader();
    cs.getCreatures();

    for (Point v : creaturesToBuild)
    {
        addEnemiesLine(v.x, v.x + 1, v.y);
    }

    return length;
}

// parameter safe should be set to true iff length of the Straight > 10.
// minimal length = 2
//floorHeight - height of the floor. used for building of the top part of the dead end separator

private static int buildStraight(int xo, int maxLength, boolean safe, int vfloor, int floorHeight)
{
    creaturesPos.clear();

    int length;
    if (floorHeight != INFINITY_FLOOR_HEIGHT)
    {
        length = maxLength;
    } else
    {
        length = globalRandom.nextInt(8) + 2;//globalRandom.nextInt(50)+1) + 2;
        if (safe) length = 10 + globalRandom.nextInt(5);
        if (length > maxLength) length = maxLength;
    }


    int floor = vfloor;
    if (vfloor == DEFAULT_FLOOR)
    {
        floor = height - 1 - globalRandom.nextInt(4);
    } else
    {
        globalRandom.nextInt();
    }

    int y1 = height;
    if (floorHeight != INFINITY_FLOOR_HEIGHT)
    {
        y1 = floor + floorHeight;
    }

    for (int x = xo; x < xo + length; x++)
    {
        for (int y = floor; y < y1; y++)
        {
            if (y >= floor)
                level.setBlock(x, y, (byte) (1 + 9 * 16));
            if (y == floor)
                creaturesPos.add(new Point(x, y - k));
        }
    }

    if (!safe)
    {
        if (length > 5)
        {
            decorate(xo, xo + length, floor);
        }
    }

    creaturesSpreader cs = new creaturesSpreader();
    cs.getCreatures();

    for (Point v : creaturesToBuild)
    {
        addEnemiesLine(v.x, v.x + 1, v.y);
    }

    return length;
}

private static boolean canBuildBlocks(int x0, int floor, boolean isHB)
{
    if ((counters.blocksCount >= counters.totalBlocks && !isHB))
    {
        return false;
    }

    boolean res = true;

//    if (floor < 1)
//    {
//        return false;
//    }

//    for (int y = 0; y < 1; y++)
//    {
//        if (level.getBlock(x0, floor - y) != 0)
//        {
//            res = false;
//            break;
//        }
//    }

    return res;
}

private static void buildBlocks(int x0, int x1, int floor, boolean pHB, int pS, int pE, boolean onlyHB, boolean isDistance)
{
    //TODO: check canAddBlocks
    if (counters.blocksCount > counters.totalBlocks)
    {
        return;
    }
    int s = pS; //Start
    int e = pE; //End
    boolean hb = pHB;

    if (onlyHB)
        hb = onlyHB;

    --floor;
    while (floor > 0) //minimal distance between the bricks line and floor is 4
    {
        if ((x1 - 1 - e) - (x0 + 1 + s) > 0) //minimal number of bricks in the line is positive value
        {
            for (int x = x0 + s; x < x1 - e; x++)
            {
                if (hb && counters.totalHiddenBlocks != 0) //if hidden blocks to be built
                {
                    boolean isBlock = globalRandom.nextInt(2) == 1;
                    if (isBlock && canBuildBlocks(x, floor - 4, true))
                    {
                        level.setBlock(x, floor - 4, (byte) (1)); //a hidden block with a coin
                        counters.hiddenBlocks++;
                        ++counters.coinsCount;
                    }
                } else
                {
                    boolean canDeco = false; //can add enemy line and coins
                    //decorate( x0, x1, floor, true );
                    if (x != x0 + 1 && x != x1 - 2 && globalRandom.nextInt(3) == 0)
                    {
                        if (canBuildBlocks(x, floor - 4, false))
                        {
                            counters.blocksCount++;
                            if ((globalRandom.nextInt(4) == 0))
                            {
                                if (level.getBlock(x, floor) == 0)
                                    level.setBlock(x, floor, (byte) (4 + 2 + 1 * 16)); //a brick with animated question symbol with power up. when broken becomes a rock
                            } else
                            {
                                if (level.getBlock(x, floor) == 0)
                                {
                                    level.setBlock(x, floor, (byte) (4 + 1 + 1 * 16)); //a brick with animated question symbol with coin. when broken becomes a rock
                                    ++counters.coinsCount;
                                }
                            }
                            canDeco = true;
                        }
                    } else if (globalRandom.nextInt(4) == 0)
                    {
                        if (canBuildBlocks(x, floor - 4, false))
                        {
                            counters.blocksCount++;
                            if (globalRandom.nextInt(4) == 0)
                            {
                                if (level.getBlock(x, floor) == 0)
                                    level.setBlock(x, floor, (byte) (2 + 1 * 16)); //a brick with a power up. when broken becomes a rock
                            } else
                            {
                                if (level.getBlock(x, floor) == 0)
                                {
                                    level.setBlock(x, floor, (byte) (1 + 1 * 16)); //a brick with a coin. when broken becomes a rock
                                    ++counters.coinsCount;
                                }
                            }
                            canDeco = true;
                        }
                    } else if (globalRandom.nextInt(2) == 1 && canBuildBlocks(x, floor - 4, false))
                    {
                        if (level.getBlock(x, floor) == 0)
                        {
                            counters.blocksCount++; //TODO: move it in to the Level.setBlock
                            level.setBlock(x, floor, (byte) (0 + 1 * 16)); //a break brick
                            canDeco = true;
                        }
                    }
                    if (canDeco)
                    {
                        //if (creaturesRandom.nextInt(35) > levelDifficulty + 1) addEnemiesLine(x0 + 1, x1 - 1, floor - 1);
                        buildCoins(x0, x1, floor, s, e);
                    }
                }

                if (level.getBlock(x, floor - 1) == 0)
                    creaturesPos.add(new Point(x, floor - 1));
            }
            if (onlyHB)
            {
                hb = true;
            } else
            {
                hb = globalRandom.nextInt(4) == 0;//globalRandom.nextInt(3) == globalRandom.nextInt(3);
            }
        }

//        if (creaturesRandom.nextInt(35) > levelDifficulty + 1)
//            addEnemiesLine(x0 + 1, x1 - 1, floor - 1);

        floor -= isDistance ? 4 : globalRandom.nextInt(6) + 3;
        s = globalRandom.nextInt(4);
        e = globalRandom.nextInt(4);
    }
    globalRandom.nextBoolean();
}

private static void buildCoins(int x0, int x1, int floor, int s, int e)
{
    if (floor - 2 < 0) return;

    if ((x1 - 1 - e) - (x0 + 1 + s) > 1)
    {
        for (int x = x0 + 1 + s; x < x1 - 1 - e; x++)
        {
            if (counters.coinsCount >= counters.totalCoins)
            {
                break;
            }
            if (level.getBlock(x, floor - 2) == 0) //if cell (x, floor-2) is empty
            {
                counters.coinsCount++;
                level.setBlock(x, floor - 2, (byte) (2 + 2 * 16)); //coin
            }
        }
    }
}

private static void decorate(int x0, int x1, int floor)
{
    if (floor < 1) return;

    int s = globalRandom.nextInt(4);
    int e = globalRandom.nextInt(4);
    boolean hb = ((globalRandom.nextInt(levelDifficulty + 1) % (levelDifficulty + 1))) > 0.5;

//    if (!hb)
//    {
//        addEnemiesLine(x0 + 1, x1 - 1, floor - 1);
//    }

    if (floor - 2 > 0 && !hb)
    {
        buildCoins(x0, x1, floor, s, e);
    }

    buildBlocks(x0, x1, floor, hb, s, e, false, false);
}

private static void fixWalls()
{
    boolean[][] blockMap = new boolean[length + 1][height + 1];
    for (int x = 0; x < length + 1; x++)
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
    blockify(level, blockMap, length + 1, height + 1);
}

private static void blockify(Level level, boolean[][] blocks, int width, int height)
{
    int to = 0;
    if (levelType == LevelGenerator.TYPE_CASTLE)
        to = 4 * 2;
    else if (levelType == LevelGenerator.TYPE_UNDERGROUND)
        to = 4 * 3;

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
                    } else
                    {
                        // KEEP OLD BLOCK!
                    }
                } else
                {
                    if (b[0][0])
                    {
                        level.setBlock(x, y, (byte) (1 + 10 * 16 + to));
                    } else
                    {
                        level.setBlock(x, y, (byte) (1 + 8 * 16 + to));
                    }
                }
            } else if (b[0][0] == b[0][1] && b[1][0] == b[1][1])
            {
                if (b[0][0])
                {
                    level.setBlock(x, y, (byte) (2 + 9 * 16 + to));
                } else
                {
                    level.setBlock(x, y, (byte) (0 + 9 * 16 + to));
                }
            } else if (b[0][0] == b[1][1] && b[0][1] == b[1][0])
            {
                level.setBlock(x, y, (byte) (1 + 9 * 16 + to));
            } else if (b[0][0] == b[1][0])
            {
                if (b[0][0])
                {
                    if (b[0][1])
                    {
                        level.setBlock(x, y, (byte) (3 + 10 * 16 + to));
                    } else
                    {
                        level.setBlock(x, y, (byte) (3 + 11 * 16 + to));
                    }
                } else
                {
                    if (b[0][1])
                    {
                        level.setBlock(x, y, (byte) (2 + 8 * 16 + to));
                    } else
                    {
                        level.setBlock(x, y, (byte) (0 + 8 * 16 + to));
                    }
                }
            } else if (b[0][1] == b[1][1])
            {
                if (b[0][1])
                {
                    if (b[0][0])
                    {
                        level.setBlock(x, y, (byte) (3 + 9 * 16 + to));
                    } else
                    {
                        level.setBlock(x, y, (byte) (3 + 8 * 16 + to));
                    }
                } else
                {
                    if (b[0][0])
                    {
                        level.setBlock(x, y, (byte) (2 + 10 * 16 + to));
                    } else
                    {
                        level.setBlock(x, y, (byte) (0 + 10 * 16 + to));
                    }
                }
            } else
            {
                level.setBlock(x, y, (byte) (0 + 1 * 16 + to));
            }
        }
    }
}
}