package ch.idsia.maibe.tasks;

import ch.idsia.ai.agents.Agent;
import ch.idsia.tools.CmdLineOptions;

/**
 * Created by IntelliJ IDEA.
 * User: Sergey Karakovskiy, sergey at idsia dot ch
 * Date: Apr 3, 2010 Time: 3:46:41 PM
 * Package: ch.idsia.maibe.tasks
 */

public class MultiObjectiveFitnessTask extends BasicTask implements Task
{
    public MultiObjectiveFitnessTask(Agent agent)
    {
        super(agent);
    }

    public double[] evaluate(Agent controller)
    {
        return new double[0];
    }

    public void setOptions(CmdLineOptions options)
    {

    }


    public void doEpisodes(int amount)
    {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public boolean isFinished()
    {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public void reset()
    {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
