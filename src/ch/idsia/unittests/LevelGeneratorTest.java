package ch.idsia.unittests;

import ch.idsia.mario.engine.level.Level;
import ch.idsia.mario.engine.level.LevelGenerator;
import ch.idsia.tools.CmdLineOptions;
import junit.framework.TestCase;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

/**
 * Created by IntelliJ IDEA.
 * User: Sergey Karakovskiy, sergey.karakovskiy@gmail.com
 * Date: Aug 28, 2010
 * Time: 10:30:34 PM
 * Package: ch.idsia.unittests
 */
public class LevelGeneratorTest extends TestCase
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
    public void testCreateLevel() throws Exception 
    {
        final CmdLineOptions cmdLineOptions = new CmdLineOptions();
        Level level1 = LevelGenerator.createLevel(cmdLineOptions); 
        Level level2 = LevelGenerator.createLevel(cmdLineOptions);
        assertSame(level1, level2);
    }
}
