package ch.idsia.unittests;

import ch.idsia.benchmark.mario.engine.level.Level;
import ch.idsia.benchmark.mario.engine.level.LevelGenerator;
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

    String format( int k )
    {
        String s = String.valueOf (k);
        while (s.length() < 4)
            s += " ";

        return s;
    }

    @Test
    public void testCreateLevel() throws Exception
    {
        final CmdLineOptions cmdLineOptions = new CmdLineOptions();
        Level level1 = LevelGenerator.createLevel(cmdLineOptions);
        Level level2 = LevelGenerator.createLevel(cmdLineOptions);

        System.out.println("Size of the first  level: " + level1.length + " " + level1.height);
        System.out.println("Size of the second level: " + level2.length + " " + level2.height);

        for (int i = 0; i < level1.height; i++)
        {
            for (int j = 0; j < level1.length; j++)
                System.out.print(format(level1.getBlock (j, i)) + " ");
            System.out.print("\n");
        }

        for (int i = 0; i < level1.length; i++)
            System.out.print(format(i) + " ");
        System.out.println();
            

        for (int i = 0; i < level2.height; i++)
        {
            for (int j = 0; j < level2.length; j++)
                System.out.print(format(level2.getBlock (j, i)) + " ");
            System.out.print("\n");
        }

        for (int i = 0; i < level1.length; i++)
            System.out.print(format(i) + " ");
        System.out.println();

        for (int i = 0; i < level1.length; i++)
            for (int j = 0; j < level1.height; j++)
                assertEquals (level1.getBlock (i, j), level2.getBlock (i, j));
    }
}
