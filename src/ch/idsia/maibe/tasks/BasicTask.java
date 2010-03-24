package ch.idsia.maibe.tasks;

import ch.idsia.ai.agents.Agent;
import ch.idsia.mario.environments.Environment;
import ch.idsia.tools.CmdLineOptions;

/**
 * Created by IntelliJ IDEA. User: Sergey Karakovskiy, sergey at idsia dot ch Date: Mar 14, 2010 Time: 4:47:33 PM
 * Package: ch.idsia.maibe.tasks
 */

public class BasicTask
{
    private Environment environment;
    private Agent agent;

    public BasicTask(Environment environment, Agent agent)
    {
        this.setEnvironment(environment);
        this.setAgent(agent);
    }

    public void runEpisode()
    {
        while (!environment.isLevelFinished())
        {
            environment.tick();
            agent.integrateObservation(environment);
            environment.performAction(agent.getAction());
        }
    }

    public void setEnvironment(Environment environment)
    {
        this.environment = environment;
    }

    public void setAgent(Agent agent)
    {
        this.agent = agent;
        environment.setAgent(agent);
    }

    public void reset(CmdLineOptions cmdLineOptions)
    {
        environment.reset(cmdLineOptions);
        agent.reset();
    }
}
