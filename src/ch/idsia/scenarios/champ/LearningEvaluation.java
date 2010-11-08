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

package ch.idsia.scenarios.champ;

import ch.idsia.agents.Agent;
import ch.idsia.agents.LearningAgent;
import ch.idsia.agents.SRNESLearningAgent;
import ch.idsia.agents.learning.MediumSRNAgent;
import ch.idsia.benchmark.tasks.BasicTask;
import ch.idsia.benchmark.tasks.ProgressTask;
import ch.idsia.tools.CmdLineOptions;
import ch.idsia.tools.EvaluationInfo;

/**
 * Created by IntelliJ IDEA.
 * User: Sergey Karakovskiy, sergey at idsia dot ch
 * Date: Mar 17, 2010 Time: 8:34:17 AM
 * Package: ch.idsia.scenarios
 */

/**
 * Class used for agent evaluation on GamePlay track
 * www.marioai.org/learning-track
 */

public final class LearningEvaluation
{
final static int numberOfTrials = 1000;
final static boolean scoring = false;
private static int killsSum = 0;
private static float marioStatusSum = 0;
private static int timeLeftSum = 0;
private static int marioModeSum = 0;
private static boolean detailedStats = false;

final static int populationSize = 100;

private static float evaluateSubmission(CmdLineOptions cmdLineOptions, LearningAgent learningAgent)
{
    boolean verbose = false;
    float fitness = 0;
    int disqualifications = 0;

    cmdLineOptions.setVisualization(false);
    //final LearningTask learningTask = new LearningTask(cmdLineOptions);
    //learningTask.setAgent(learningAgent);
    ProgressTask task = new ProgressTask(cmdLineOptions);

    learningAgent.newEpisode();
    learningAgent.setTask(task);
    learningAgent.setNumberOfTrials(numberOfTrials);
    learningAgent.init();

    for (int i = 0; i < numberOfTrials; ++i)
    {
        System.out.println("-------------------------------");
        System.out.println(i + " trial");
        //learningTask.reset(cmdLineOptions);
        task.reset(cmdLineOptions);
        // inform your agent that new episode is coming, pick up next representative in population.
//            learningAgent.learn();
        task.runOneEpisode();
        /*if (!task.runOneEpisode())  // make evaluation on an episode once
        {
            System.out.println("MarioAI: out of computational time per action!");
            disqualifications++;
            continue;
        }*/

        EvaluationInfo evaluationInfo = task.getEnvironment().getEvaluationInfo();
        float f = evaluationInfo.computeWeightedFitness();
        if (verbose)
        {
            System.out.println("Intermediate SCORE = " + f + "; Details: " + evaluationInfo.toStringSingleLine());
        }
        // learn the reward
        //learningAgent.giveReward(f);
    }
    // do some post processing if you need to. In general: select the Agent with highest score.
    learningAgent.learn();
    // perform the gameplay task on the same level
    cmdLineOptions.setVisualization(true);
    Agent bestAgent = learningAgent.getBestAgent();
    cmdLineOptions.setAgent(bestAgent);
    BasicTask basicTask = new BasicTask(cmdLineOptions);
    basicTask.reset(cmdLineOptions);
//        basicTask.setAgent(bestAgent);
    if (!basicTask.runOneEpisode())  // make evaluation on the same episode once
    {
        System.out.println("MarioAI: out of computational time per action!");
        disqualifications++;
    }
    EvaluationInfo evaluationInfo = basicTask.getEnvironment().getEvaluationInfo();
    float f = evaluationInfo.computeWeightedFitness();
    if (verbose)
    {
        System.out.println("Intermediate SCORE = " + f + "; Details: " + evaluationInfo.toStringSingleLine());
    }
    System.out.println("LearningEvaluation final score = " + f);
    return f;
}

public static void main(String[] args)
{
//        Level 1
    // set up parameters
    final CmdLineOptions cmdLineOptions = new CmdLineOptions(args);
    LearningAgent learningAgent = new SRNESLearningAgent(new MediumSRNAgent()); // Your Competition Entry goes here

    cmdLineOptions.setArgs("-lco off -lb off -le off -lhb off -lg off -ltb off -lhs off -lca off -lde off");
    float finalScore = LearningEvaluation.evaluateSubmission(cmdLineOptions, learningAgent);

//        ProgressTask task = new ProgressTask(options);
//        ES es = new ES (task, initial, populationSize);

//        Level 2
    cmdLineOptions.setArgs("-lco on -lb on -lhb off -lg off -ltb off -lhs off -lca off -lde off -ld 3");
    finalScore += LearningEvaluation.evaluateSubmission(cmdLineOptions, learningAgent);

//        Level 3
    cmdLineOptions.setArgs("-lco off -lb off -le off -lhb off -lg on -ltb off -lhs off -lca off -lde off -ld 2");
    finalScore += LearningEvaluation.evaluateSubmission(cmdLineOptions, learningAgent);

//        Level 4
    cmdLineOptions.setArgs("-lco off -lb off -le off -lhb off -lg on -ltb on -lhs off -lca on -lde off -ld 3");
    finalScore += LearningEvaluation.evaluateSubmission(cmdLineOptions, learningAgent);

//        Level 5
    cmdLineOptions.setArgs("-lco on -lb off -le g,rk,gw -lhb on -lg off -ltb on -lhs off -lca off -lde off -ld 3");
    finalScore += LearningEvaluation.evaluateSubmission(cmdLineOptions, learningAgent);


    System.out.println("finalScore = " + finalScore);
    System.exit(0);
}
}
