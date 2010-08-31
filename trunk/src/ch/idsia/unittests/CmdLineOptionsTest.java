package ch.idsia.unittests;

import ch.idsia.tools.CmdLineOptions;
import junit.framework.TestCase;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

/**
 * Created by IntelliJ IDEA.
 * User: Sergey Karakovskiy, sergey.karakovskiy@gmail.com
 * Date: Aug 28, 2010
 * Time: 8:36:02 PM
 * Package: ch.idsia.unittests
 */
public class CmdLineOptionsTest extends TestCase
{
    CmdLineOptions cmdLineOptions;

    @BeforeTest
    public void setUp()
    {
        cmdLineOptions = new CmdLineOptions();
        assertEquals(11, cmdLineOptions.getTotalNumberOfOptions());
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
    public void testIsMarioInvulnerable() throws Exception
    {
    }

    @Test
    public void testSetMarioInvulnerable() throws Exception
    {
    }
}
