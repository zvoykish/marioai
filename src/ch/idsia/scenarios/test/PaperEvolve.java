package ch.idsia.scenarios.test;

import ch.idsia.ai.Evolvable;
import ch.idsia.ai.agents.Agent;
import ch.idsia.ai.agents.AgentsPool;
import ch.idsia.ai.agents.learning.SmallMLPAgent;
import ch.idsia.ai.ea.ES;
import ch.idsia.maibe.tasks.BasicTask;
import ch.idsia.maibe.tasks.ProgressTask;
import ch.idsia.mario.engine.GlobalOptions;
import ch.idsia.mario.environments.Environment;
import ch.idsia.tools.CmdLineOptions;
import wox.serial.Easy;

/**
 * Created by IntelliJ IDEA.
 * User: julian
 * Date: Jun 13, 2009
 * Time: 2:16:18 PM
 */
public class PaperEvolve
{
    final static int generations = 4096;
    final static int populationSize = 100;

    public static void main(String[] args)
    {
        CmdLineOptions options = new CmdLineOptions(new String[0]);
//        Evolvable initial = new LargeSRNAgent();
//        Evolvable initial = new SmallSRNAgent();
//        Evolvable initial = new MediumSRNAgent();
        Evolvable initial = new SmallMLPAgent();
//        Evolvable initial = new MediumSRNAgent();
//        Evolvable initial = new LargeMLPAgent();
        if (args.length > 0)
        {
            initial = (Evolvable) AgentsPool.load (args[0]);
        }
        options.setTimeLimit(100);
        options.setAgent((Agent) initial);
        options.setFPS(GlobalOptions.MaxFPS);
        options.setPauseWorld(false);
        options.setVisualization(false);
        ProgressTask task = new ProgressTask(options);
        int seed = (int) (Math.random () * Integer.MAX_VALUE / 100000);
        ES es = new ES (task, initial, populationSize);
        System.out.println("Evolving " + initial + " with task " + task);
        int difficulty = 0;
        int uid =  seed;
//        options.setLevelRandSeed(seed);

        BasicTask bt = new BasicTask(null);
        CmdLineOptions c = new CmdLineOptions(new String[]{"-vis", "on", "-fps", "24"});

        // start learning in mode 0
        System.out.println("options.getTimeLimit() = " + options.getTimeLimit());
        options.setMarioMode(0);
        for (int gen = 0; gen < generations; gen++)
        {
            options.setLevelRandSeed(gen);
            es.nextGeneration();

            double bestResult = es.getBestFitnesses()[0];
            System.out.print("Generation: " + gen + " ld: " + difficulty + "  best: " + bestResult + ";  ");

            int marioStatus = task.getEnvironment().getEvaluationInfo().marioStatus;
            if (bestResult > 4000 && marioStatus == Environment.MARIO_STATUS_WIN)
            {
                final String fileName = "evolved-" + gen + "-uid-" + uid + ".xml";
                Easy.save (es.getBests()[0], fileName);
                final Agent a = (Agent) es.getBests()[0];
                c.setLevelRandSeed(options.getLevelRandSeed());
                c.setLevelDifficulty(options.getLevelDifficulty());
                c.setTimeLimit(options.getTimeLimit());
                c.setAgent(a);
                bt.setAgent(a);
                bt.getEnvironment().reset(c);
                bt.runOneEpisode();
                System.out.print("TIME LEFT: " + task.getEnvironment().getEvaluationInfo().timeLeft);
                System.out.println(", STATUS = " + task.getEnvironment().getEvaluationInfo().marioStatus);
                
                difficulty++;
                options.setLevelDifficulty(difficulty);
            }
        }
//        Stats.main(new String[]{fileName, "0"});
        System.out.println("\n\n\n\n\n\n\n\n\n");
    }
}
