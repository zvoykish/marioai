package ch.idsia.unittests;

import ch.idsia.benchmark.mario.engine.LevelScene;
import ch.idsia.tools.CmdLineOptions;
import junit.framework.TestCase;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

/**
 * Created by IntelliJ IDEA.
 * User: Sergey Karakovskiy, sergey.karakovskiy@gmail.com
 * Date: Aug 28, 2010
 * Time: 8:31:52 PM
 * Package: ch.idsia.unittests
 */
public class LevelSceneTest extends TestCase
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
public void testSetArgs() throws Exception
{
}

@Test
public void testGetSerializedLevelSceneObservationZ_1x3() throws Exception
{
    CmdLineOptions cmd = new CmdLineOptions("-rfw 1 -rfh 3");
    LevelScene levelScene = new LevelScene();
    //levelScene.init(cmd);
    levelScene.reset(cmd);
    int[] obs = levelScene.getSerializedLevelSceneObservationZ(1);
}

@Test
public void testGetSerializedLevelSceneObservationZ_3x1() throws Exception
{
    CmdLineOptions cmd = new CmdLineOptions("-rfw 3 -rfh 1");
    LevelScene levelScene = new LevelScene();
    //levelScene.init(cmd);
    levelScene.reset(cmd);
    int[] obs = levelScene.getSerializedLevelSceneObservationZ(1);
}

@Test
public void testGetSerializedLevelSceneObservationZ_1x1() throws Exception
{
    CmdLineOptions cmd = new CmdLineOptions("-rfw 1 -rfh 1");
    LevelScene levelScene = new LevelScene();
    //levelScene.init(cmd);
    levelScene.reset(cmd);
    int[] obs = levelScene.getSerializedLevelSceneObservationZ(1);
}

@Test
public void testGetSerializedLevelSceneObservationZ_3x3() throws Exception
{
    CmdLineOptions cmd = new CmdLineOptions("-rfw 3 -rfh 3");
    LevelScene levelScene = new LevelScene();
    //levelScene.init(cmd);
    levelScene.reset(cmd);
    int[] obs = levelScene.getSerializedLevelSceneObservationZ(1);
}
}
