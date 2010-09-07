package ch.idsia.benchmark.tasks;

import ch.idsia.agents.Agent;
import ch.idsia.benchmark.mario.environments.Environment;
import ch.idsia.benchmark.mario.environments.MarioEnvironment;
import ch.idsia.tools.CmdLineOptions;

/**
 * Created by IntelliJ IDEA. User: Sergey Karakovskiy, sergey at idsia dot ch Date: Mar 14, 2010 Time: 4:47:33 PM
 * Package: ch.idsia.maibe.tasks
 */

public class BasicTask implements Task
{
protected final static Environment environment = MarioEnvironment.getInstance();
private Agent agent;
protected CmdLineOptions options;
private long COMPUTATION_TIME_BOUND = 42; // stands for prescribed  FPS 24.
private int callsCounter = 0;

public BasicTask(CmdLineOptions cmdLineOptions)
{
    // TODO: remove this crutch
    this.setAgent(cmdLineOptions.getAgent());
    this.setOptions(cmdLineOptions);
}

/**
 * @return boolean flat whether controller is disqualified or not
 */


public boolean runOneEpisode()
{
//        System.out.println("agent = " + agent);
//        boolean tormoz = r.nextInt() < 10;
    ++callsCounter;
//        System.out.println("callsCounter = " + callsCounter);
    while (!environment.isLevelFinished())
    {
        environment.tick();
//            start timer
//            long tm = System.currentTimeMillis();
        agent.integrateObservation(environment);
        agent.giveIntermediateReward(environment.getIntermediateReward());

        boolean[] action = agent.getAction();

//            System.out.println("System.currentTimeMillis() - tm > COMPUTATION_TIME_BOUND = " + (System.currentTimeMillis() - tm ));
//            if (System.currentTimeMillis() - tm > COMPUTATION_TIME_BOUND)
//            {
////                # controller disqualified on this level
//                System.out.println("Agent is disqualified on this level");
//                return false;
//            }
        environment.performAction(action);
    }
    return true;
}

// TODO: remove this crutch

public final void setAgent(Agent agent)
{
    this.agent = agent;
    // TODO: remove this crutch
    environment.setAgent(agent);
}

public void reset(CmdLineOptions cmdLineOptions)
{
    options = cmdLineOptions;
    environment.reset(cmdLineOptions);
    agent.reset();
}

public Environment getEnvironment()
{
    return environment;
}

public float[] evaluate(Agent controller)
{
    return new float[0];  //To change body of implemented methods use File | Settings | File Templates.
}

public void setOptions(CmdLineOptions options)
{
    this.options = options;
}

public CmdLineOptions getOptions()
{
    return options;
}

public void doEpisodes(int amount, boolean verbose)
{
    for (int i = 0; i < amount; ++i)
    {
        this.reset(options);
        this.runOneEpisode();
        if (verbose)
        {
            System.out.println(environment.getEvaluationInfoAsString());
        }
    }
}

public boolean isFinished()
{
    return false;
}

public void reset()
{

}
}
