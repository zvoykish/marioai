package ch.idsia.scenarios;

import ch.idsia.benchmark.tasks.ReplayTask;

/**
 * Created by IntelliJ IDEA.
 * User: Sergey Karakovskiy, sergey.karakovskiy@gmail.com
 * Date: Oct 9, 2010
 * Time: 8:00:13 PM
 * Package: ch.idsia.scenarios
 */
public class Replay
{
public static void main(String[] args)
{

    //TODO : FIX IT!
    /* TODO : FIX IT!
[~ Mario AI Benchmark ~ 0.1.9]
Exception in thread "main" java.lang.ArrayIndexOutOfBoundsException: 0
	at ch.idsia.scenarios.Replay.main(Replay.java:18)
    
     */
//    final CmdLineOptions cmdLineOptions = new CmdLineOptions(args);
    final ReplayTask replayTask = new ReplayTask();
    replayTask.reset(args[0]);
    replayTask.startReplay();
    // TODO: output evaluationInfo as in BasicTask
    System.out.println(replayTask.getEnvironment().getEvaluationInfoAsString());
    System.exit(0);
}
}
