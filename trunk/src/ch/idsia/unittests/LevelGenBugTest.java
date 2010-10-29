package ch.idsia.unittests;

import ch.idsia.agents.Agent;
import ch.idsia.agents.controllers.ForwardJumpingAgent;
import ch.idsia.benchmark.tasks.BasicTask;
import ch.idsia.tools.CmdLineOptions;

/**
 * Created by IntelliJ IDEA. User: Sergey Karakovskiy, sergey at idsia dot ch Date: Mar 17, 2010 Time: 8:28:00 AM
 * Package: ch.idsia.scenarios
 */

//TODO: rewrite as unit tests and move to LevelGeneratorTest class
public final class LevelGenBugTest
{
public static void main(String[] args)
{
//        final String argsString = "-vis on";
    final CmdLineOptions cmdLineOptions = new CmdLineOptions(args);
//        final Environment environment = new MarioEnvironment();
//        final Agent agent = new ForwardAgent();
//        final Agent agent = cmdLineOptions.getAgent();
//        final Agent a = AgentsPool.load("ch.idsia.controllers.agents.controllers.ForwardJumpingAgent");
    Agent agent = new ForwardJumpingAgent();
    cmdLineOptions.setAgent(agent);

    final BasicTask basicTask = new BasicTask(cmdLineOptions);
//        for (int i = 0; i < 10; ++i)
//        {
//            int seed = 0;
//            do
//            {
//                cmdLineOptions.setLevelDifficulty(i);
//                cmdLineOptions.setLevelRandSeed(seed++);

    cmdLineOptions.setArgs("-ll 100 -lco on -lb on -le off -lhb off -lg off -ltb off -lhs off -lca off -lde off -ld 3");
    cmdLineOptions.setRecordFile("first_run" +
            "_" + System.currentTimeMillis() / 100000);
    basicTask.reset(cmdLineOptions);
    basicTask.runOneEpisode();
    System.out.println(basicTask.getEnvironment().getEvaluationInfoAsString());
    System.out.println(basicTask.getEvaluationInfo());
//

    BasicTask newTask = new BasicTask(cmdLineOptions);
    cmdLineOptions.setArgs("-ll 100 -lco off -lb off -le off -lhb off -lg on -ltb off -lhs off -lca off -lde off -ld 2");
    cmdLineOptions.setRecordFile("second_run" +
            "_" + System.currentTimeMillis() / 100000);
    newTask.reset(cmdLineOptions);
    newTask.runOneEpisode();
    System.out.println(basicTask.getEnvironment().getEvaluationInfoAsString());
//            } while (basicTask.getEnvironment().getEvaluationInfo().marioStatus != Environment.MARIO_STATUS_WIN);
//        }
//
    System.exit(0);
}

}
