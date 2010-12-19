/*
 * Copyright (c) 2009-2010, Sergey Karakovskiy and Julian Togelius
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *  Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 *  Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 *  Neither the name of the Mario AI nor the
 * names of its contributors may be used to endorse or promote products
 * derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
 * NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */

package ch.idsia.agents;

import ch.idsia.benchmark.mario.environments.Environment;
import ch.idsia.benchmark.tasks.LearningTask;

/**
 * Created by IntelliJ IDEA.
 * User: Sergey Karakovskiy, sergey.karakovskiy@gmail.com
 * Date: 12/12/10
 * Time: 12:24 AM
 * Package: ch.idsia.agents
 */
public class MLPESLearningAgent implements LearningAgent
{
private LearningTask learningTask = null;
private long evaluationQuota = 0;


public void learn()
{

}

public void giveReward(final float reward)
{
    //To change body of implemented methods use File | Settings | File Templates.
}

public void newEpisode()
{
    //To change body of implemented methods use File | Settings | File Templates.
}

public void setLearningTask(final LearningTask learningTask)
{
    this.learningTask = learningTask;
}

public void setEvaluationQuota(final long num)
{
    this.evaluationQuota = num;
}

public Agent getBestAgent()
{
    return null;  //To change body of implemented methods use File | Settings | File Templates.
}

public void init()
{
    //To change body of implemented methods use File | Settings | File Templates.
}

public boolean[] getAction()
{
    return new boolean[0];  //To change body of implemented methods use File | Settings | File Templates.
}

public void integrateObservation(final Environment environment)
{
    //To change body of implemented methods use File | Settings | File Templates.
}

public void giveIntermediateReward(final float intermediateReward)
{
    //To change body of implemented methods use File | Settings | File Templates.
}

/**
 * clears all dynamic data, such as hidden layers in recurrent networks
 * just implement an empty method for a reactive controller
 */
public void reset()
{
    //To change body of implemented methods use File | Settings | File Templates.
}

public void setObservationDetails(final int rfWidth, final int rfHeight, final int egoRow, final int egoCol)
{
    //To change body of implemented methods use File | Settings | File Templates.
}

public String getName()
{
    return null;  //To change body of implemented methods use File | Settings | File Templates.
}

public void setName(final String name)
{
    //To change body of implemented methods use File | Settings | File Templates.
}
}
