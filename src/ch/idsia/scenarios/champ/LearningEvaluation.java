package ch.idsia.scenarios.champ;

import ch.idsia.ai.agents.SRNESLearningAgent;
import ch.idsia.ai.agents.Agent;
import ch.idsia.ai.agents.LearningAgent;
import ch.idsia.ai.agents.learning.MediumSRNAgent;
import ch.idsia.maibe.tasks.BasicTask;
import ch.idsia.maibe.tasks.ProgressTask;
import ch.idsia.tools.CmdLineOptions;
import ch.idsia.tools.EvaluationInfo;

/**
 * Created by IntelliJ IDEA.
 * User: Sergey Karakovskiy, sergey at idsia dot ch
 * Date: Mar 17, 2010 Time: 8:34:17 AM
 * Package: ch.idsia.scenarios
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
            System.out.println(i+" trial");
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
            float f = evaluationInfo.computeMultiObjectiveFitness();
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
        BasicTask basicTask = new BasicTask(cmdLineOptions);
        basicTask.reset(cmdLineOptions);
        Agent bestAgent = learningAgent.getBestAgent();
        basicTask.setAgent(bestAgent);
        if (!basicTask.runOneEpisode())  // make evaluation on the same episode once
        {
            System.out.println("MarioAI: out of computational time per action!");
            disqualifications++;
        }
        EvaluationInfo evaluationInfo = basicTask.getEnvironment().getEvaluationInfo();
        float f = evaluationInfo.computeMultiObjectiveFitness();
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

        cmdLineOptions.setUpOptionsString("-lco on");
        float finalScore = LearningEvaluation.evaluateSubmission(cmdLineOptions, learningAgent);

//        ProgressTask task = new ProgressTask(options);
//        ES es = new ES (task, initial, populationSize);

//        Level 2
        cmdLineOptions.setUpOptionsString("-lco on");
        finalScore += LearningEvaluation.evaluateSubmission(cmdLineOptions, learningAgent);

//        Level 3
        cmdLineOptions.setUpOptionsString("-lco on");
        finalScore += LearningEvaluation.evaluateSubmission(cmdLineOptions, learningAgent);

//        Level 4
        cmdLineOptions.setUpOptionsString("-lco on");
        finalScore += LearningEvaluation.evaluateSubmission(cmdLineOptions, learningAgent);

//        Level 5
        cmdLineOptions.setUpOptionsString("-lco on");
        finalScore += LearningEvaluation.evaluateSubmission(cmdLineOptions, learningAgent);

        System.out.println("finalScore = " + finalScore);
        // learn on a particular level
        // create particular level
        // several

//        EvaluationInfo evaluationInfo = new EvaluationInfo(environment.getEvaluationInfoAsFloats());
//        System.out.println("evaluationInfo = " + evaluationInfo);
        System.exit(0);
    }

}
