package ch.idsia.scenarios;

import ch.idsia.agents.Agent;
import ch.idsia.agents.controllers.ForwardAgent;
import ch.idsia.benchmark.mario.environments.Environment;
import ch.idsia.benchmark.tasks.BasicTask;
import ch.idsia.tools.CmdLineOptions;

import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: Sergey Karakovskiy, sergey@idsia.ch
 * Date: May 7, 2009
 * Time: 4:38:23 PM
 * Package: ch.idsia
 */

public class Custom
{
public static void main(String[] args)
{
//        final String argsString = "-vis on";
    final CmdLineOptions cmdLineOptions = new CmdLineOptions(args);
    final Agent agent = new ForwardAgent();
    final BasicTask basicTask = new BasicTask(cmdLineOptions);
    for (int i = 0; i < 10; ++i)
    {
        int seed = 0;
        do
        {
            cmdLineOptions.setLevelDifficulty(i);
            cmdLineOptions.setLevelRandSeed(seed++);
            basicTask.reset(cmdLineOptions);
            basicTask.runOneEpisode();
            System.out.println(basicTask.getEnvironment().getEvaluationInfoAsString());
        } while (basicTask.getEnvironment().getEvaluationInfo().marioStatus != Environment.MARIO_STATUS_WIN);
    }
    Runtime rt = Runtime.getRuntime();
    try
    {
//            Process proc = rt.exec("/usr/local/bin/mate " + marioTraceFile);
        Process proc = rt.exec("python hello.py");
    } catch (IOException e)
    {
        e.printStackTrace();
    }
    System.exit(0);

}
}
