package ch.idsia.unittests;

import ch.idsia.benchmark.mario.environments.Environment;
import ch.idsia.benchmark.mario.environments.MarioEnvironment;
import ch.idsia.tools.CmdLineOptions;
import junit.framework.TestCase;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

/**
 * Created by IntelliJ IDEA.
 * User: Sergey Karakovskiy, sergey.karakovskiy@gmail.com
 * Date: Aug 28, 2010
 * Time: 8:43:51 PM
 * Package: ch.idsia.unittests
 */
public class MarioEnvironmentTest  extends TestCase
{
    @BeforeTest
    public void setUp() {
    }

    @AfterTest
    public void tearDown() {
    }

    @Test
    public void testGetInstance() throws Exception {
    }

    @Test
    public void testResetDefault() throws Exception {
    }

    @Test
    public void testReset() throws Exception {
    }

    @Test
    public void testTick() throws Exception {
    }

    @Test
    public void testGetMarioFloatPos() throws Exception {
    }

    @Test
    public void testGetMarioMode() throws Exception {
    }

    @Test
    public void testGetEnemiesFloatPos() throws Exception {
    }

    @Test
    public void testIsMarioOnGround() throws Exception {
    }

    @Test
    public void testIsMarioAbleToJump() throws Exception {
    }

    @Test
    public void testIsMarioCarrying() throws Exception {
    }

    @Test
    public void testIsMarioAbleToShoot() throws Exception {
    }

    @Test
    public void testGetObservationWidth() throws Exception {
    }

    @Test
    public void testGetObservationHeight() throws Exception {
    }

    @Test
    public void testGetMergedObservationZZ() throws Exception {
    }

    @Test
    public void testGetLevelSceneObservationZ() throws Exception {
    }

    @Test
    public void testGetEnemiesObservationZ() throws Exception {
    }

    @Test
    public void testGetKillsTotal() throws Exception {
    }

    @Test
    public void testGetKillsByFire() throws Exception {
    }

    @Test
    public void testGetKillsByStomp() throws Exception {
    }

    @Test
    public void testGetKillsByShell() throws Exception {
    }

    @Test
    public void testGetMarioStatus() throws Exception {
    }

    @Test
    public void testGetSerializedFullObservationZZ() throws Exception {
    }

    @Test
    public void testGetSerializedLevelSceneObservationZ() throws Exception {
    }

    @Test
    public void testGetSerializedEnemiesObservationZ() throws Exception {
    }

    @Test
    public void testGetSerializedMergedObservationZZ() throws Exception {
    }

    @Test
    public void testGetCreaturesFloatPos() throws Exception {
    }

    @Test
    public void testGetMarioState() throws Exception {
    }

    @Test
    public void testPerformAction() throws Exception {
    }

    @Test
    public void testIsLevelFinished() throws Exception {
    }

    @Test
    public void testGetEvaluationInfoAsFloats() throws Exception {
    }

    @Test
    public void testGetEvaluationInfoAsString() throws Exception {
    }

    @Test
    public void testGetEvaluationInfo() throws Exception {
    }

    @Test
    public void testSetAgent() throws Exception {
    }

    @Test
    public void testGetIntermediateReward() throws Exception {
    }

    @Test
    public void testMarioCenterPos() throws Exception
    {
        CmdLineOptions cmdLineOptions = new CmdLineOptions("-rfw 5 -rfh 7");
        MarioEnvironment env = MarioEnvironment.getInstance();

        assertNotNull(env);

        env.reset(cmdLineOptions);

        int[] pos = env.getMarioCenterPos();
        assertEquals(2, pos[0]);
        assertEquals(3, pos[1]);
    }
}
