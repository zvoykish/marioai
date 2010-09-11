package ch.idsia.benchmark.mario.environments;

import ch.idsia.agents.Agent;
import ch.idsia.benchmark.mario.engine.GlobalOptions;
import ch.idsia.benchmark.mario.engine.LevelScene;
import ch.idsia.benchmark.mario.engine.MarioVisualComponent;
import ch.idsia.benchmark.mario.engine.sprites.Mario;
import ch.idsia.tools.CmdLineOptions;
import ch.idsia.tools.EvaluationInfo;

/**
 * Created by IntelliJ IDEA.
 * User: Sergey Karakovskiy, sergey@idsia.ch
 * Date: Mar 3, 2010 Time: 10:08:13 PM
 * Package: ch.idsia.benchmark.mario.environments
 */

public final class MarioEnvironment implements Environment
{
private int[] marioCenterPos = new int[2];

private final LevelScene levelScene;
//    private int frame = 0;
private MarioVisualComponent marioVisualComponent;
private Agent agent;

private static final MarioEnvironment ourInstance = new MarioEnvironment();
private static final EvaluationInfo evaluationInfo = new EvaluationInfo();

public static MarioEnvironment getInstance()
{
    return ourInstance;
}

private MarioEnvironment()
{
//        System.out.println("System.getProperty(\"java.awt.headless\") = " + System.getProperty("java.awt.headless"));
//        System.out.println("System.getProperty(\"verbose\") = " + System.getProperty("-verbose"));
//        System.out.println("Java: JA ZDES'!!");
//        System.out.flush();
    System.out.println(GlobalOptions.getBenchmarkName());
    levelScene = new LevelScene();
}

public void resetDefault()
{
    levelScene.resetDefault();
}

public void reset(String args)
{
    CmdLineOptions cmdLineOptions = CmdLineOptions.getOptionsByString(args);
    this.reset(cmdLineOptions);
//        CmdLineOptions opts = new CmdLineOptions(setUpOptions);
//        int[] intOpts = opts.toIntArray();
//        this.reset(intOpts);
}

public void reset(CmdLineOptions setUpOptions)
{
    /*System.out.println("\nsetUpOptions = " + setUpOptions);
    for (int i = 0; i < setUpOptions.length; ++i)
    {
        System.out.print(" op[" + i +"] = " + setUpOptions[i]);
    }
    System.out.println("");
    System.out.flush();*/
    this.setAgent(setUpOptions.getAgent());
    marioCenterPos[0] = setUpOptions.getReceptiveFieldWidth() / 2;
    marioCenterPos[1] = setUpOptions.getReceptiveFieldHeight() / 2;
//        System.out.println("marioCenterPosX = " + marioCenterPos[0]);
//        System.out.println("marioCenterPosY = " + marioCenterPos[1]);

    if (/*levelScene.visualization*/ setUpOptions.isVisualization())
    {
        if (marioVisualComponent == null)
            marioVisualComponent = MarioVisualComponent.getInstance(setUpOptions, levelScene);
        levelScene.reset(setUpOptions);
        marioVisualComponent.reset();
        marioVisualComponent.postInitGraphicsAndLevel();
        marioVisualComponent.setAgent(agent);
        marioVisualComponent.setLocation(setUpOptions.getViewLocation());
        marioVisualComponent.setAlwaysOnTop(setUpOptions.isViewAlwaysOnTop());
    } else
        levelScene.reset(setUpOptions);

}

public void tick()
{
    levelScene.tick();
    if (levelScene.visualization)
        marioVisualComponent.tick();
}

public float[] getMarioFloatPos()
{
    return levelScene.getMarioFloatPos();
}

public int getMarioMode()
{
    return levelScene.getMarioMode();
}

public float[] getEnemiesFloatPos()
{
    return levelScene.getEnemiesFloatPos();
}

public boolean isMarioOnGround()
{
    return levelScene.isMarioOnGround();
}

public boolean isMarioAbleToJump()
{
    return levelScene.isMarioAbleToJump();
}

public boolean isMarioCarrying()
{
    return levelScene.isMarioCarrying();
}

public boolean isMarioAbleToShoot()
{
    return levelScene.isMarioAbleToShoot();
}

public int getReceptiveFieldWidth()
{
    return levelScene.getReceptiveFieldWidth();
}

public int getReceptiveFieldHeight()
{
    return levelScene.getReceptiveFieldHeight();
}

public byte[][] getMergedObservationZZ(int ZLevelScene, int ZLevelEnemies)
{
    return levelScene.getMergedObservationZZ(ZLevelScene, ZLevelEnemies);
}

public byte[][] getLevelSceneObservationZ(int ZLevelScene)
{
    return levelScene.getLevelSceneObservationZ(ZLevelScene);
}

public byte[][] getEnemiesObservationZ(int ZLevelEnemies)
{
    return levelScene.getEnemiesObservationZ(ZLevelEnemies);
}

public int getKillsTotal()
{
    return levelScene.getKillsTotal();
}

public int getKillsByFire()
{
    return levelScene.getKillsByFire();
}

public int getKillsByStomp()
{
    return levelScene.getKillsByStomp();
}

public int getKillsByShell()
{
    return levelScene.getKillsByShell();
}

public int getMarioStatus()
{
    return levelScene.getMarioStatus();
}

public float[] getSerializedFullObservationZZ(int ZLevelScene, int ZLevelEnemies)
{
    return levelScene.getSerializedFullObservationZZ(ZLevelScene, ZLevelEnemies);
}

public int[] getSerializedLevelSceneObservationZ(int ZLevelScene)
{
    return levelScene.getSerializedLevelSceneObservationZ(ZLevelScene);
}

public int[] getSerializedEnemiesObservationZ(int ZLevelEnemies)
{
    return levelScene.getSerializedEnemiesObservationZ(ZLevelEnemies);
}

public int[] getSerializedMergedObservationZZ(int ZLevelScene, int ZLevelEnemies)
{
    return levelScene.getSerializedMergedObservationZZ(ZLevelScene, ZLevelEnemies);
}

public float[] getCreaturesFloatPos()
{
    return levelScene.getCreaturesFloatPos();
}

public int[] getMarioState()
{
    return levelScene.getMarioState();
}

public void performAction(boolean[] action)
{
    levelScene.performAction(action);
}

public boolean isLevelFinished()
{
    return levelScene.isLevelFinished();
}

public float[] getEvaluationInfoAsFloats()
{
    return this.getEvaluationInfo().toFloatArray();
}

public String getEvaluationInfoAsString()
{
    return this.getEvaluationInfo().toString();
}

public EvaluationInfo getEvaluationInfo()
{
//        evaluationInfo.agentType = agent.getClass().getSimpleName();
//        evaluationInfo.agentName = agent.getName();
    evaluationInfo.marioStatus = levelScene.getMarioStatus();
    evaluationInfo.flowersDevoured = Mario.flowersDevoured;
    evaluationInfo.distancePassedPhys = levelScene.getMarioFloatPos()[0];
    evaluationInfo.distancePassedCells = levelScene.mario.mapX;
//     evaluationInfo.totalLengthOfLevelCells = levelScene.level.getWidthCells();
//     evaluationInfo.totalLengthOfLevelPhys = levelScene.level.getWidthPhys();
    evaluationInfo.timeSpent = levelScene.getTimeSpent();
    evaluationInfo.timeLeft = levelScene.getTimeLeft();
    evaluationInfo.coinsGained = Mario.coins;
    evaluationInfo.totalNumberOfCoins = levelScene.level.counters.coinsCount;
    evaluationInfo.totalNumberOfHiddenBlocks = levelScene.level.counters.hiddenBlocks;
    evaluationInfo.totalNumberOfFlowers = levelScene.level.counters.flowers;
    evaluationInfo.totalNumberOfMushrooms = levelScene.level.counters.mushrooms;
    evaluationInfo.marioMode = levelScene.getMarioMode();
    evaluationInfo.mushroomsDevoured = Mario.mushroomsDevoured;
    evaluationInfo.killsTotal = levelScene.getKillsTotal();
    evaluationInfo.killsByStomp = levelScene.getKillsByStomp();
    evaluationInfo.killsByFire = levelScene.getKillsByFire();
    evaluationInfo.killsByShell = levelScene.getKillsByShell();
    evaluationInfo.hiddenBlocksFound = Mario.hiddenBlocks;
    evaluationInfo.collisionsWithCreatures = Mario.collisionsWithCreatures;
    evaluationInfo.Memo = levelScene.memo;
    evaluationInfo.levelLength = levelScene.level.xExit - 1;
    return evaluationInfo;
}


public void setAgent(Agent agent)
{
    this.agent = agent;
}

public float getIntermediateReward()
{
    // TODO: reward for coins, killed creatures, cleared dead-ends, bypassed gaps, hidden blocks found
    return -1;
}

public int[] getMarioReceptiveFieldCenter()
{
    return marioCenterPos;
}
}
