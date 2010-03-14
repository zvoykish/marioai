package ch.idsia.mario.simulation;

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
 * Created by IntelliJ IDEA.
 * User: Sergey Karakovskiy, sergey at idsia dot ch
 * Date: Mar 1, 2010 Time: 1:50:37 PM
 * Package: ch.idsia.scenarios
 */

public class AmiCoSimulator
{
    public static void main(String[] args)
    {
        CmdLineOptions cmdLineOptions = new CmdLineOptions(args);
        int[] options = cmdLineOptions.toIntArray();
        for (int i = 0; i < options.length; ++i)
        {
            System.out.print(options[i] + ",");
        }
        Environment environment = new MarioEnvironment();
        Agent agent = new ForwardJumpingAgent();
        for (int seed = 16; seed < 20; ++seed)
        {
            options[4] = seed;  // seed
            options[14] = seed % 2;    // visualization
            environment.reset(options);
            while (!environment.isLevelFinished())
            {
                environment.tick();
                agent.integrateObservation(environment.getSerializedLevelSceneObservationZ(options[17]),
                                           environment.getSerializedEnemiesObservationZ(options[18]),
                                           environment.getMarioFloatPos(),
                                           environment.getEnemiesFloatPos(),
                                           environment.getMarioState());
                environment.performAction(agent.getAction());
            }
            EvaluationInfo evaluationInfo = new EvaluationInfo(environment.getEvaluationInfo());
            System.out.println("evaluationInfo = " + evaluationInfo);
        }
        System.exit(0);
    }

    public void runEpisodes()
    {
        
    }
}