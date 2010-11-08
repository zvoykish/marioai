/*
 * Copyright (c) 2009-2010, Sergey Karakovskiy and Julian Togelius
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the Mario AI nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
 * NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */

package ch.idsia.agents;

import ch.idsia.agents.learning.MediumSRNAgent;
import ch.idsia.benchmark.tasks.ProgressTask;
import ch.idsia.evolution.ea.ES;

/**
 * Created by IntelliJ IDEA.
 * User: odin
 * Date: Jul 27, 2010
 * Time: 9:40:09 PM
 */


public class SRNESLearningAgent extends MediumSRNAgent implements LearningAgent
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

public SRNESLearningAgent(MediumSRNAgent agent)
{
    this.agent = agent;
}

public SRNESLearningAgent()
{
    this.agent = new MediumSRNAgent();
}

public void learn()
{
    this.exhausted++;

    int locBestScore = 0; // local best score

    for (int gen = 0; gen < generations; gen++)
    {
        System.out.println(gen + " generation");
        es.nextGeneration();
        float fitn = es.getBestFitnesses()[0];

        if (fitn > bestScore)
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
