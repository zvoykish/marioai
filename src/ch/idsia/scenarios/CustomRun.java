package ch.idsia.scenarios;
//https://marioai.googlecode.com/svn/trunk/src

import ch.idsia.ai.agents.Agent;
import ch.idsia.ai.agents.AmiCoAgent;
import ch.idsia.ai.agents.ai.ForwardAgent;
import ch.idsia.tools.CmdLineOptions;
import ch.idsia.tools.Evaluator;

/**
 * Created by IntelliJ IDEA.
 * User: Sergey Karakovskiy, firstName_at_idsia_dot_ch
 * Date: May 7, 2009
 * Time: 4:38:23 PM
 * Package: ch.idsia
 */

public class CustomRun
{
    public static void main(String[] args)
    {
        CmdLineOptions options = new CmdLineOptions(args);
        Evaluator evaluator = new Evaluator(options);
        Agent agent = new ForwardAgent();
        options.setMaxFPS(true);
//        options.setVisualization(false);
        System.out.println("evaluator = " + evaluator);
//        Agent agent = new AmiCoAgent();
        options.setAgent(agent);
        evaluator.evaluate();
        System.exit(0);
    }
}
