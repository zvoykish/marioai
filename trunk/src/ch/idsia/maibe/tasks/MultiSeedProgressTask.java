package ch.idsia.maibe.tasks;

import ch.idsia.ai.agents.Agent;
import ch.idsia.tools.CmdLineOptions;

/**
 * Created by IntelliJ IDEA.
 * User: julian
 * Date: May 23, 2009
 * Time: 11:37:47 PM
 */

public class MultiSeedProgressTask implements Task
{
    private CmdLineOptions options;
    private int startingSeed = 0;
    private int numberOfSeeds = 3;
    private BasicTask basicTask;

    public MultiSeedProgressTask(CmdLineOptions evaluationOptions)
    {
        setOptions(evaluationOptions);
        this.basicTask = new BasicTask(evaluationOptions.getAgent());
    }

    public double[] evaluate(Agent controller)
    {
        double distanceTravelled = 0;

        options.setAgent(controller);

        for (int i = 0; i < numberOfSeeds; i++)
        {
            controller.reset();
            options.setAgent(controller);
            basicTask.setAgent(controller);
            options.setLevelRandSeed(startingSeed + i);
            basicTask.reset(options);
            basicTask.runOneEpisode();

            distanceTravelled += basicTask.getEnvironment().getEvaluationInfo().computeDistancePassed();
        }
        distanceTravelled = distanceTravelled / numberOfSeeds;
        return new double[]{distanceTravelled};
    }

    public void setStartingSeed (int seed)
    {
        startingSeed = seed;
    }

    public void setNumberOfSeeds (int number)
    {
        numberOfSeeds = number;
    }

    public void setOptions(CmdLineOptions options)
    {
        this.options = options;
    }

    public CmdLineOptions getOptions()
    {
        return options;
    }

    public void doEpisodes(int amount)
    {

    }

    public boolean isFinished()
    {
        return true;  
    }

    public void reset()
    {

    }
}
