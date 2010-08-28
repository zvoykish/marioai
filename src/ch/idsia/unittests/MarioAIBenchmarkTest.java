package ch.idsia.unittests;

import ch.idsia.maibe.tasks.BasicTask;
import ch.idsia.tools.CmdLineOptions;
import junit.framework.TestCase;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

/**
 * Created by IntelliJ IDEA.
 * User: Sergey Karakovskiy, sergey.karakovskiy@gmail.com
 * Date: Aug 28, 2010
 * Time: 8:39:30 PM
 * Package: ch.idsia.unittests
 */
public class MarioAIBenchmarkTest extends TestCase
{
    @BeforeTest
    public void setUp()
    {
    }

    @AfterTest
    public void tearDown()
    {
    }

    @Test
    public void testAgentLoadAndName()
    {
        final CmdLineOptions cmdLineOptions = new CmdLineOptions("-vis off -ag ch.idsia.ai.agents.controllers.ForwardJumpingAgent");
        assertNotNull(cmdLineOptions.getAgent());
        assertEquals(cmdLineOptions.getAgent().getName(), "ForwardJumpingAgent");
        assertEquals(cmdLineOptions.getAgentLoadFullName(), "ch.idsia.ai.agents.controllers.ForwardJumpingAgent");
    }

    @Test
    public void testForwardJumpingAgentFitness()
    {
        final CmdLineOptions cmdLineOptions = new CmdLineOptions("-vis off -ag ch.idsia.ai.agents.controllers.ForwardJumpingAgent -echo on");
        final BasicTask basicTask = new BasicTask(cmdLineOptions);
        basicTask.reset(cmdLineOptions);
        basicTask.runOneEpisode();
        System.out.println(basicTask.getEnvironment().getEvaluationInfoAsString());
        assertEquals(7414, basicTask.getEnvironment().getEvaluationInfo().computeWeightedFitness());
    }

    @Test
    public void testForwardAgentFitness()
    {
        final CmdLineOptions cmdLineOptions = new CmdLineOptions("-vis off -ag ch.idsia.ai.agents.controllers.ForwardAgent -echo on");
        final BasicTask basicTask = new BasicTask(cmdLineOptions);
        basicTask.reset(cmdLineOptions);
        basicTask.runOneEpisode();
        System.out.println(basicTask.getEnvironment().getEvaluationInfoAsString());
        assertEquals(7478, basicTask.getEnvironment().getEvaluationInfo().computeWeightedFitness());
    }
}
