package ch.idsia.maibe.tasks;

import ch.idsia.ai.agents.Agent;
import ch.idsia.tools.CmdLineOptions;

/**
 * Created by IntelliJ IDEA.
 * User: Sergey Karakovskiy
 * Date: Apr 8, 2009
 * Time: 11:26:43 AM
 * Package: ch.idsia.maibe.tasks
 */

public final class ProgressTask extends BasicTask implements Task
{
    private CmdLineOptions options;
    private int startingSeed;


    public ProgressTask(CmdLineOptions evaluationOptions)
    {
        super(evaluationOptions.getAgent());
        setOptions(evaluationOptions);    }

    public double[] evaluate(Agent controller)
    {
        double distanceTravelled = 0;
        controller.reset();
//        options.setLevelRandSeed(startingSeed++);
//        System.out.println("controller = " + controller);
        options.setAgent(controller);
        this.setAgent(controller);
        this.reset(options);
        this.runOneEpisode();
        distanceTravelled += this.getEnvironment().getEvaluationInfo().computeDistancePassed();
        return new double[]{distanceTravelled};
    }

    public void setOptions(CmdLineOptions options) {
        this.options = options;
    }

    public CmdLineOptions getOptions() {
        return options;
    }

    public void doEpisodes(int amount)
    {
        
    }

    public boolean isFinished()
    {
        return false;
    }

    public void reset()
    {
        
    }

}
