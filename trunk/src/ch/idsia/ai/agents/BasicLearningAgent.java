package ch.idsia.ai.agents;

import ch.idsia.ai.agents.learning.SimpleMLPAgent;
import ch.idsia.ai.agents.learning.SmallMLPAgent;

/**
 * Created by IntelliJ IDEA.
 * User: Sergey Karakovskiy, sergey.karakovskiy@gmail.com
 * Date: Jun 9, 2010
 * Time: 7:03:17 PM
 * Package: ch.idsia.ai.agents
 */
public class BasicLearningAgent extends SimpleMLPAgent implements LearningAgent
{
    Agent agent;
    Agent finalAgent;

    public BasicLearningAgent(Agent agent)
    {
        this.agent = agent;
    }

    public BasicLearningAgent()
    {
        this.agent = new SmallMLPAgent();
    }

    public void learn()
    {

    }

    public void giveReward(float r)
    {

    }

    public void newEpisode()
    {
        
    }
}
