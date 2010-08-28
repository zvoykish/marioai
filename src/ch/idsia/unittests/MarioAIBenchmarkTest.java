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
        final CmdLineOptions cmdLineOptions = new CmdLineOptions("-vis off -lc 0 -ag ch.idsia.ai.agents.controllers.ForwardJumpingAgent");
        assertNotNull(cmdLineOptions.getAgent());
        assertEquals(cmdLineOptions.getAgent().getName(), "ForwardJumpingAgent");
        assertEquals(cmdLineOptions.getAgentLoadFullName(), "ch.idsia.ai.agents.controllers.ForwardJumpingAgent");
    }

    @Test
    public void testForwardJumpingAgentFitnessWithoutCreatures()
    {
        final CmdLineOptions cmdLineOptions = new CmdLineOptions("-vis off -lc 0 -ag ch.idsia.ai.agents.controllers.ForwardJumpingAgent -echo on");
        final BasicTask basicTask = new BasicTask(cmdLineOptions);
        basicTask.reset(cmdLineOptions);
        basicTask.runOneEpisode();
        System.out.println(basicTask.getEnvironment().getEvaluationInfoAsString());
        assertEquals("Capacity", 7974, basicTask.getEnvironment().getEvaluationInfo().computeWeightedFitness(), 0.1);
    }

    @Test
    public void testForwardAgentFitnessWithoutCreatures()
    {
        final CmdLineOptions cmdLineOptions = new CmdLineOptions("-vis off -lc 0 -ag ch.idsia.ai.agents.controllers.ForwardAgent -echo on");
        final BasicTask basicTask = new BasicTask(cmdLineOptions);
        basicTask.reset(cmdLineOptions);
        basicTask.runOneEpisode();
        System.out.println(basicTask.getEnvironment().getEvaluationInfoAsString());
        assertEquals("Capacity", 7916, basicTask.getEnvironment().getEvaluationInfo().computeWeightedFitness(), 0.1);
    }

    @Test
    public void testForwardJumpingAgentFitnessWithDefaultCreatures()
    {
        final CmdLineOptions cmdLineOptions = new CmdLineOptions("-vis off -ag ch.idsia.ai.agents.controllers.ForwardJumpingAgent -echo on");
        final BasicTask basicTask = new BasicTask(cmdLineOptions);
        basicTask.reset(cmdLineOptions);
        basicTask.runOneEpisode();
        System.out.println(basicTask.getEnvironment().getEvaluationInfoAsString());
        assertEquals("Capacity", 7956, basicTask.getEnvironment().getEvaluationInfo().computeWeightedFitness(), 0.1);
    }

    @Test
    public void testForwardAgentFitnessWithDefaultCreatures()
    {
        final CmdLineOptions cmdLineOptions = new CmdLineOptions("-vis off -ag ch.idsia.ai.agents.controllers.ForwardAgent -echo on");
        final BasicTask basicTask = new BasicTask(cmdLineOptions);
        basicTask.reset(cmdLineOptions);
        basicTask.runOneEpisode();
        System.out.println(basicTask.getEnvironment().getEvaluationInfoAsString());
        assertEquals(7478, basicTask.getEnvironment().getEvaluationInfo().computeWeightedFitness());
    }
}
