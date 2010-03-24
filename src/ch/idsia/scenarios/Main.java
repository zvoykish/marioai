package ch.idsia.scenarios;

import ch.idsia.ai.agents.Agent;
import ch.idsia.ai.agents.AgentsPool;
import ch.idsia.ai.agents.ai.ForwardAgent;
import ch.idsia.ai.agents.human.HumanKeyboardAgent;
import ch.idsia.maibe.tasks.BasicTask;
import ch.idsia.mario.environments.Environment;
import ch.idsia.mario.environments.MarioEnvironment;
import ch.idsia.tools.CmdLineOptions;
import ch.idsia.tools.EvaluationInfo;

/**
 * Created by IntelliJ IDEA. User: Sergey Karakovskiy, sergey at idsia dot ch Date: Mar 17, 2010 Time: 8:28:00 AM
 * Package: ch.idsia.scenarios
 */
public class Main
{
    public static void main(String[] args)
    {
//        final String argsString = "-vis on";
//        args = argsString.split("\\s");
        final CmdLineOptions cmdLineOptions = new CmdLineOptions(args);
        final Environment environment = new MarioEnvironment();
//        final Agent agent = new ForwardAgent();
        final Agent agent = cmdLineOptions.getAgent();
//        final Agent a = AgentsPool.load("ch.idsia.ai.agents.ai.ForwardJumpingAgent");
        final BasicTask basicTask = new BasicTask(environment, agent);
        basicTask.reset(cmdLineOptions);
        basicTask.runEpisode();
        EvaluationInfo evaluationInfo = new EvaluationInfo(environment.getEvaluationInfo());
        System.out.println("evaluationInfo = " + evaluationInfo);
        System.exit(0);
    }
}
