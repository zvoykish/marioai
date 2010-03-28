package ch.idsia.scenarios;

import ch.idsia.ai.agents.Agent;
import ch.idsia.ai.agents.human.HumanKeyboardAgent;
import ch.idsia.maibe.tasks.BasicTask;
import ch.idsia.mario.environments.Environment;
import ch.idsia.mario.environments.MarioEnvironment;
import ch.idsia.tools.CmdLineOptions; /**
 * Created by IntelliJ IDEA.
 * User: julian
 * Date: May 5, 2009
 * Time: 12:46:43 PM
 */

/**
 * The <code>Play</code> class shows how simple is to run a MarioAI Benchmark.
 * It shows how to set up some parameters, create a task,
 * use the CmdLineParameters class to set up options from command line if any.
 * Defaults are used otherwise.
 *
 * @author  Julian Togelius, Sergey Karakovskiy
 * @version 1.0, May 5, 2009

 */

public class Play
{
    /**
     * <p>An entry point of the class.</p>
     *
     * @param args input parameters for customization of the benchmark.
     *
     * @see ch.idsia.scenarios.oldscenarios.MainRun
     * @see ch.idsia.tools.CmdLineOptions
     * @see ch.idsia.tools.EvaluationOptions
     *
     * @since   MarioAI-0.1
     */

    public static void main(String[] args)
    {
        final CmdLineOptions cmdLineOptions = new CmdLineOptions(new String[]{});
        int attempts = 1;
        if (args.length > 0 && args[0].equals("-secret"))
        {
            System.out.println("Welcome to a secret Level test for Learning track! \n" +
                               "Please, remember about Wall jumps, favor exploration and be a bit smart ;-)\n" +
                               "You are given 5 attempts, 256 mariosecond per each attempt;\n " +
                               "You'll be evaluated on the 6th trial. Enjoy!");
            cmdLineOptions.setLevelRandSeed(152);
            cmdLineOptions.setTimeLimit(256);
            attempts = 6;
        }
        else
            cmdLineOptions.setArgs(args);

        final Environment environment = new MarioEnvironment();
        final Agent agent = new HumanKeyboardAgent();
        final BasicTask basicTask = new BasicTask(environment, agent);
        cmdLineOptions.setVisualization(true);
        basicTask.reset(cmdLineOptions);
//        basicTask.runOneEpisode();

        // run 5 episodes with same options, each time giving output of Evaluation info.
        basicTask.runEpisodes(attempts, false);
        System.out.println("\nEvaluationInfo: \n" + environment.getEvaluationInfoAsString());
        System.exit(0);
    }
}
