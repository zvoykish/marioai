package ch.idsia.agents.controllers;

import ch.idsia.agents.Agent;
import ch.idsia.benchmark.mario.engine.Replayer;
import ch.idsia.benchmark.mario.environments.Environment;

import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: Sergey Karakovskiy, sergey.karakovskiy@gmail.com
 * Date: Oct 9, 2010
 * Time: 2:08:47 AM
 * Package: ch.idsia.agents.controllers
 */
public class ReplayAgent implements Agent
{
private Replayer replayer;
private String name;


public ReplayAgent()
{
    setName("Replay");
}

//this method should return mario state and position array
//byte[]
public void setReplayer(Replayer replayer)
{
    this.replayer = replayer;
}

public boolean[] getAction()
{
    //handle situation when time is over
    boolean[] action = null;
    try
    {
        action = replayer.readAction();
//        if (action == null)
//        {
//            action = new boolean[Environment.numberOfButtons];
//            for (int i = 0; i < Environment.numberOfButtons; i++)
//                action[i] = false;
//        }
    } catch (IOException e)
    {
        //TODO: describe this
        e.printStackTrace();
    }
    return action;
}

public void integrateObservation(final Environment environment)
{}

public void giveIntermediateReward(final float intermediateReward)
{}

public void reset()
{}

public String getName()
{
    return name;
}

public void setName(final String name)
{
    this.name = name;
}

public void closeReplayer()
{
    try
    {
        replayer.closeFile();
        replayer.closeZip();
    } catch (IOException e)
    {
        e.printStackTrace();
    }
}
}
