package ch.idsia.mario.environments;

import ch.idsia.mario.engine.GlobalOptions;
import ch.idsia.mario.engine.LevelScene;
import ch.idsia.mario.engine.MarioVisualComponent;

/**
 * Created by IntelliJ IDEA. User: Sergey Karakovskiy, sergey at idsia dot ch Date: Mar 3, 2010 Time: 10:08:13 PM
 * Package: ch.idsia.mario.environments
 */

public class MarioEnvironment implements Environment
{
    final LevelScene levelScene;
    private int frame = 0;
    private MarioVisualComponent marioVisualComponent;

    public MarioEnvironment()
    {
        levelScene = new LevelScene(0, 0, 0, 0, 0, 0);
    }

    public void resetDefault()
    {
        levelScene.resetDefault();
    }

    public void reset(int[] setUpOptions)
    {
        if (/*levelScene.visualization*/ setUpOptions[14] == 1)
        {
            if (marioVisualComponent == null)
                marioVisualComponent = MarioVisualComponent.Create(GlobalOptions.VISUAL_COMPONENT_WIDTH,
                                                                   GlobalOptions.VISUAL_COMPONENT_HEIGHT,
                                                                   levelScene);
            levelScene.reset(setUpOptions);
            marioVisualComponent.reset();
            marioVisualComponent.postInitGraphicsAndLevel();
        }
        else
            levelScene.reset(setUpOptions);
    }

    public void tick()
    {
        levelScene.tick();
        if (levelScene.visualization)
            marioVisualComponent.tick();
        // Advance the frame
        ++frame;
    }

    public byte[][] getCompleteObservation()
    {
        return levelScene.getCompleteObservation();
    }

    public byte[][] getEnemiesObservation()
    {
        return levelScene.getEnemiesObservation();
    }

    public byte[][] getLevelSceneObservation()
    {
        return levelScene.getLevelSceneObservation();
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

    public double[] getSerializedFullObservationZZ(int ZLevelScene, int ZLevelEnemies)
    {
        return levelScene.getSerializedFullObservationZZ(ZLevelScene, ZLevelEnemies);
    }

    public byte[] getSerializedLevelSceneObservationZ(int ZLevelScene)
    {
        return levelScene.getSerializedLevelSceneObservationZ(ZLevelScene);
    }

    public byte[] getSerializedEnemiesObservationZ(int ZLevelEnemies)
    {
        return levelScene.getSerializedEnemiesObservationZ(ZLevelEnemies);
    }

    public byte[] getSerializedMergedObservationZZ(int ZLevelScene, int ZLevelEnemies)
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

    public double[] getEvaluationInfo()
    {
        return levelScene.getEvaluationInfo();
    }
}
