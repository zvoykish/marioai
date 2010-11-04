package ch.idsia.scenarios.oldscenarios;

import ch.idsia.benchmark.experiments.EpisodicExperiment;
import ch.idsia.benchmark.experiments.Experiment;
import ch.idsia.benchmark.tasks.ProgressTask;
import ch.idsia.benchmark.tasks.Task;
import ch.idsia.tools.CmdLineOptions;

/**
 * Created by IntelliJ IDEA.
 * User: Sergey Karakovskiy, sergey at idsia dot ch
 * Date: Feb 24, 2010
 * Time: 12:57:18 PM
 * Package: ch.idsia.scenarios
 */
public class GeneralScenario
{
public static void main(String[] args)
{
//        Agent agent = new ForwardAgent();
    CmdLineOptions options = new CmdLineOptions(args);
    Task task = new ProgressTask(options);
    Experiment exp = new EpisodicExperiment(task, options.getAgent());
    exp.doEpisodes(2);
}

/*
        CmdLineOptions options = new CmdLineOptions(args);
//        Evaluator evaluator = new Evaluator(options);
//        Agent agent = new ForwardAgent();
        options.setFPS(24);
//        options.setVisualization(false);
        String amicoModuleName = options.getPyAmiCoModuleName();
        System.out.println("amicoModuleName = " + amicoModuleName);
//        String amicoAgentName = "ForwardAgent";
        System.out.println("options.getAgentFullLoadName() = " + options.getAgentFullLoadName());
        Agent agent = new AmiCoAgent(amicoModuleName, options.getAgentFullLoadName());
        options.setAgent(agent);
//        evaluator.evaluate();
//        System.out.println("evaluator = " + evaluator.getMeanEvaluationSummary());
        System.exit(0);

 */
}
