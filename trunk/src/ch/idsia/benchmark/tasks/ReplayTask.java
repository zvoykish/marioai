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
private String name = getClass().getSimpleName();
private Replayer replayer;

public ReplayTask()
{}

public void playOneFile(final CmdLineOptions options)
{
    ReplayerOptions.Interval interval = replayer.getNextIntervalInMarioseconds();
    if (interval == null)
    {
        interval = new ReplayerOptions.Interval(0, replayer.actionsFileSize());
    }

    while (!environment.isLevelFinished())
    {
        if (environment.getTimeSpent() == interval.from) //TODO: Comment this piece
            GlobalOptions.isVisualization = true;
        else if (environment.getTimeSpent() == interval.to)
        {
            GlobalOptions.isVisualization = false;
            interval = replayer.getNextIntervalInMarioseconds();
        }
        environment.tick();
        if (!GlobalOptions.isGameplayStopped)
        {
            boolean[] action = agent.getAction();
            environment.performAction(action);
        }

        if (interval == null)
            break;
    }
}

public float[] evaluate(final Agent controller)
{
    return new float[0];  //To change body of implemented methods use File | Settings | File Templates.
}

public void setOptions(final CmdLineOptions options)
{}

public CmdLineOptions getOptions()
{
    return null;
}

public void doEpisodes(final int amount, final boolean verbose)
{}

public void startReplay()
{
    try
    {
        while (replayer.openNextReplayFile())
        {
            replayer.openFile("options");
            String strOptions = (String) replayer.readObject();
            CmdLineOptions options = new CmdLineOptions(strOptions);
            options.setVisualization(true);
            options.setRecordFile("off");
            options.setAgent(new ReplayAgent(options.getAgent().getName()));
            agent = options.getAgent();
            agent.reset();
            ((ReplayAgent) agent).setReplayer(replayer);

            environment.setReplayer(replayer);
            environment.reset(options);
            GlobalOptions.isVisualization = false;

            replayer.openFile("actions.act");

            playOneFile(options);

            GlobalOptions.isVisualization = true;
//            replayer.closeFile();
            replayer.closeReplayFile();
        }
    } catch (IOException e)
    {
        e.printStackTrace();
    } catch (Exception e)
    {
        e.printStackTrace();
    }
}

public boolean isFinished()
{
    return false;
}

public void reset(String replayOptions)
{
    replayer = new Replayer(replayOptions);
    GlobalOptions.isReplaying = true;
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
