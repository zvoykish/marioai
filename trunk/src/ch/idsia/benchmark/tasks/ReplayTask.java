package ch.idsia.benchmark.tasks;

import ch.idsia.agents.Agent;
import ch.idsia.agents.controllers.ReplayAgent;
import ch.idsia.benchmark.mario.engine.Art;
import ch.idsia.benchmark.mario.engine.GlobalOptions;
import ch.idsia.benchmark.mario.engine.Replayer;
import ch.idsia.benchmark.mario.engine.sprites.Mario;
import ch.idsia.benchmark.mario.environments.Environment;
import ch.idsia.benchmark.mario.environments.MarioEnvironment;
import ch.idsia.tools.CmdLineOptions;

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
    String replayFileName;
    try
    {
        boolean f = true;
        while ((replayFileName = replayer.getNextReplayFileName()) != null)
        {

            replayer.openFile(replayFileName + ".mario");
            Mario mario = (Mario) replayer.readObject();
            switch (mario.getMode())
            {
                case 0: //small
                    mario.sheet = Art.smallMario;
                    mario.prevSheet = Art.smallMario;
                    break;
                case 1: //big
                    mario.sheet = Art.mario;
                    mario.prevSheet = Art.mario;
                    break;
                case 2: //fire
                    mario.sheet = Art.fireMario;
                    mario.prevSheet = Art.fireMario;
                    break;
            }
            mario.levelScene = environment.getLevelScene();
            environment.setMario(mario); //TODO: remove cloning in Mario
            if (f)
            {
                environment.reset(options);
                f = false;
            }
            else
                System.err.println("something");
//                environment.reset
//            replayer.closeFile();
            replayer.openFile(replayFileName + ".act");

            while (!environment.isLevelFinished())
            {
                environment.tick();
                if (!GlobalOptions.isGameplayStopped)
                {
                    boolean[] action = agent.getAction();
                    environment.performAction(action);
                }
            }
            System.err.println("next replay");
        }
        replayer.closeFile();

        replayer.closeZip();
    } catch (IOException e)
    {
        e.printStackTrace();
    } catch (Exception e)
    {
        e.printStackTrace();
    }

    environment.getEvaluationInfo().setTaskName(name);
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
    String replayFile = options.getReplayFileName();
    try
    {
        replayer = new Replayer(replayFile);
    } catch (IOException e)
    {
        e.printStackTrace();
    }

    if (!replayFile.equals(""))
        cmdLineOptions.setAgent(new ReplayAgent());

    environment.reset(cmdLineOptions);
    agent = options.getAgent();

    if (!replayFile.equals(""))
        ((ReplayAgent) agent).setReplayer(replayer);

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
