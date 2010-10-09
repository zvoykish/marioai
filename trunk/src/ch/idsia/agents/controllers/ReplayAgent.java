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
public class ReplayAgent extends BasicMarioAIAgent implements Agent
{
Replayer replayer;

public ReplayAgent()
{
    super("Replay");
}

public void setRepFile(String fileName)
{
    try
    {
        replayer = new Replayer(fileName);
        replayer.openFile("actions.act");
    } catch (IOException e)
    {
        //TODO: describe
        e.printStackTrace();
    } catch (Exception e)
    {
        //TODO: describe
        e.printStackTrace();
    }
}

public void integrateObservation(Environment environment)
{}

public boolean[] getAction()
{
    boolean[] res = null;
    try
    {
        //TODO: check if action is null
        byte[] action = replayer.readBytes(6);
        res = new boolean[action.length];
        for (int i = 0; i < action.length; i++)
            res[i] = action[i] == 0 ? false : true;
    } catch (IOException e)
    {
        //TODO: describe this
        e.printStackTrace();
    }
    return res;
}

public void reset()
{

}
}
