package ch.idsia.scenarios;

import ch.idsia.ai.agents.Agent;
import ch.idsia.ai.agents.ai.ForwardAgent;
import ch.idsia.ai.agents.ai.ForwardJumpingAgent;
import ch.idsia.mario.engine.LevelScene;
import ch.idsia.mario.engine.sprites.Mario;
import ch.idsia.mario.environments.Environment;
import ch.idsia.mario.environments.MarioEnvironment;
import ch.idsia.tools.CmdLineOptions;
import ch.idsia.tools.EvaluationInfo;

/**
 * Created by IntelliJ IDEA. User: Sergey Karakovskiy, sergey at idsia dot ch Date: Mar 1, 2010 Time: 1:50:37 PM
 * Package: ch.idsia.scenarios
 */
public class AmiCoSimulator
{
    public static void main(String[] args)
    {
        CmdLineOptions cmdLineOptions = new CmdLineOptions(args);
        int[] options = cmdLineOptions.toIntArray();
        int seed = 0;
        int diff = 0;
        int type = 2;
        int length = 440;
        int timeLimit = 128;
        int visualize = 1;
        Environment environment = new MarioEnvironment();
        Agent agent = new ForwardJumpingAgent();
        int zLevelScene = 1;
        int zLevelEnemies = 0;
        int fps = 24;
        int marioMode = 1;
        for (seed = 16; seed < 20; ++seed)
        {
            int[] setUpOptions = new int[]{seed, diff, type, length, timeLimit, marioMode, visualize, fps};
            environment.reset(setUpOptions);
            while (!environment.isLevelFinished())
            {
                environment.tick();
                agent.integrateObservation(environment.getSerializedLevelSceneObservationZ(zLevelScene),
                                           environment.getSerializedEnemiesObservationZ(zLevelEnemies),
                                           environment.getMarioFloatPos(),
                                           environment.getEnemiesFloatPos(),
                                           environment.getMarioState());
                environment.performAction(agent.getAction());
            }
            EvaluationInfo evaluationInfo = new EvaluationInfo(environment.getEvaluationInfo());
//            System.out.println("evaluationInfo = " + evaluationInfo);
        }
        System.exit(0);
    }
}