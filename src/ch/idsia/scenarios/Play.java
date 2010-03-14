package ch.idsia.scenarios;

import ch.idsia.ai.agents.Agent;
import ch.idsia.ai.agents.ai.ForwardJumpingAgent;
import ch.idsia.ai.agents.human.HumanKeyboardAgent;
import ch.idsia.maibe.tasks.BasicTask;
import ch.idsia.maibe.tasks.ProgressTask;
import ch.idsia.maibe.tasks.Task;
import ch.idsia.mario.environments.Environment;
import ch.idsia.mario.environments.MarioEnvironment;
import ch.idsia.tools.CmdLineOptions;
import ch.idsia.tools.EvaluationInfo;
import ch.idsia.tools.EvaluationOptions;

/**
 * Created by IntelliJ IDEA.
 * User: julian
 * Date: May 5, 2009
 * Time: 12:46:43 PM
 */

/**
 * The <code>Play</code> class shows how simple is to run an iMario benchmark.
 * It shows how to set up some parameters, create a task,
 * use the CmdLineParameters class to set up options from command line if any.
 * Defaults are used otherwise.
 *
 * @author  Julian Togelius, Sergey Karakovskiy
 * @version 1.0, May 5, 2009

 */

public class Play {
    /**
     * <p>An entry point of the class.
     *
     * @param args input parameters for customization of the benchmark.
     *
     * @see ch.idsia.scenarios.MainRun
     * @see ch.idsia.tools.CmdLineOptions
     * @see ch.idsia.tools.EvaluationOptions
     *
     * @since   MarioAI-0.1
     */

    public static void main(String[] args)
    {
        final CmdLineOptions cmdLineOptions = new CmdLineOptions(args);

        final Environment environment = new MarioEnvironment();
        final Agent agent = new HumanKeyboardAgent();
//        agent.reset();
        final BasicTask basicTask = new BasicTask(environment, agent);
        int seed  = 16;
//        for (int seed = 16; seed < 20; ++seed)
//        {
            cmdLineOptions.setLevelRandSeed(seed);  // seed
//            cmdLineOptions.setVisualization(seed % 2 == 1);    // visualization
        cmdLineOptions.setVisualization(true);
            environment.reset(cmdLineOptions);
            basicTask.setEnvironment(environment);
        basicTask.runEpisode();
            EvaluationInfo evaluationInfo = new EvaluationInfo();
            System.out.println("evaluationInfo = " + evaluationInfo);
//        }
        System.exit(0);
    }
}
