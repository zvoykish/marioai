package ch.idsia.ai.agents;

import ch.idsia.ai.agents.learning.MediumSRNAgent;
import ch.idsia.ai.ea.ES;
import ch.idsia.maibe.tasks.ProgressTask;

/**
 * Created by IntelliJ IDEA.
 * User: odin
 * Date: Jul 27, 2010
 * Time: 9:40:09 PM
 */

/*
Цикл статей на хабре о том, что это за проект, как им пользоватсья и тд и тп. В частности,
очень интеренсы будут статьи о написании собственных агентов(в том числе и LearningAgent).
Статьи в оригинале на сайте/блоге. В переводе - на хабре со ссылкой на оригинал.
*/

public class AdvancedLearningAgent extends MediumSRNAgent implements LearningAgent
{
    private MediumSRNAgent agent;
    Agent bestAgent;
    private static float bestScore = 0;
    private ProgressTask task;
    ES es;
    int populationSize = 100;
    int generations = 5000;
    int numberOfTrials; //common number of trials
    int exhausted; // number of exhausted trials


    public void init()
    {
        es = new ES(task, agent, populationSize);
    }

    public AdvancedLearningAgent(MediumSRNAgent agent)
    {
        this.agent = agent;
    }

    public AdvancedLearningAgent()
    {
        this.agent = new MediumSRNAgent();
    }

    public void learn()
    {
        this.exhausted++;

        int locBestScore = 0; // local best score
                                  
        for (int gen = 0; gen < generations; gen++)
        {
            System.out.println(gen+" generation");
            es.nextGeneration();
            float fitn = es.getBestFitnesses()[0];

            if(fitn > bestScore)
            {
                bestScore = fitn;
                bestAgent = (Agent) es.getBests()[0];
            }
        }
    }

    public void giveReward(float r)
    {
    }

    public void newEpisode()
    {
        task = null;
        agent.reset();
    }

    public void setTask(ProgressTask task)
    {
        this.task = task;
    }

    public void setNumberOfTrials(int num)
    {
        this.numberOfTrials = num;
    }

    public Agent getBestAgent()
    {
        return bestAgent;
    }
}
