package ch.idsia.benchmark.tasks;

import ch.idsia.agents.Agent;
import ch.idsia.agents.controllers.ReplayAgent;
import ch.idsia.benchmark.mario.engine.GlobalOptions;
import ch.idsia.benchmark.mario.engine.Replayer;
import ch.idsia.benchmark.mario.environments.Environment;
import ch.idsia.benchmark.mario.environments.MarioEnvironment;
import ch.idsia.tools.CmdLineOptions;
import ch.idsia.tools.ReplayerOptions;

import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: Sergey Karakovskiy, sergey.karakovskiy@gmail.com
 * Date: Oct 9, 2010
 * Time: 7:17:49 PM
 * Package: ch.idsia.benchmark.tasks
 */
public class ReplayTask implements Task
{
protected final static Environment environment = MarioEnvironment.getInstance();
private Agent agent;
protected CmdLineOptions options;
private String name = getClass().getSimpleName();
private Replayer replayer;

public ReplayTask(CmdLineOptions cmdLineOptions)
{
    setOptions(cmdLineOptions);
}

public boolean startReplay()
{
    try
    {
        while (replayer.openNextReplayFile()) //while there are more .zip files
        {
            environment.setReplayer(replayer);
            ((ReplayAgent) agent).setReplayer(replayer);            
            environment.reset(options);
            environment.setVisualization(false);

            ReplayerOptions.Interval interval = replayer.getNextIntervalInMarioseconds();
            if (interval == null)
            {
                interval = new ReplayerOptions.Interval(0, replayer.actionsFileSize());
            }

            replayer.openFile("actions.act");

            while (!environment.isLevelFinished())
            {
                if (environment.getTimeSpent() == interval.from) //TODO: fix?
                    environment.setVisualization(true);
                else if (environment.getTimeSpent() == interval.to)
                {
                    environment.setVisualization(false);
                    interval = replayer.getNextIntervalInMarioseconds();
                    if (interval == null)
                        break;
                }
                environment.tick();
                if (!GlobalOptions.isGameplayStopped)
                {
                    boolean[] action = agent.getAction();
                    environment.performAction(action);
                }
            }
            
            environment.setVisualization(true);
            replayer.closeFile();
            replayer.closeZip();
        }

    } catch (IOException e)
    {
        e.printStackTrace();
    } catch (Exception e)
    {
        e.printStackTrace();
    }

//    environment.getEvaluationInfo().setTaskName(name);
    return true;
}

public float[] evaluate(final Agent controller)
{
    return new float[0];  //To change body of implemented methods use File | Settings | File Templates.
}

public void setOptions(final CmdLineOptions options)
{
    this.options = options;
}

public CmdLineOptions getOptions()
{
    return null;
}

public void doEpisodes(final int amount, final boolean verbose)
{}

public boolean isFinished()
{
    return false;
}

public void reset(CmdLineOptions cmdLineOptions)
{
    options = cmdLineOptions;
    String replayOptions = options.getReplayOptions();
    replayer = new Replayer(replayOptions);

    cmdLineOptions.setAgent(new ReplayAgent());

//    environment.reset(cmdLineOptions);
    agent = options.getAgent();

    agent.reset();
}

public void reset()
{}

public String getName()
{
    return name;
}

public Environment getEnvironment()
{
    return environment;
}
}
