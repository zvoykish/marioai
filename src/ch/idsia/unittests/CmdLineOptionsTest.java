package ch.idsia.unittests;

import ch.idsia.tools.CmdLineOptions;
import ch.idsia.utils.ParameterContainer;
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
        assertEquals(42, cmdLineOptions.getTotalNumberOfOptions());
    }

    @AfterTest
    public void tearDown()
    {
    }

    @Test
    public void testAllOptionsHaveDefaults()
    {
        assertEquals(ParameterContainer.getTotalNumberOfOptions(), ParameterContainer.getNumberOfAllowedOptions());
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
        cmdLineOptions.setMarioInvulnerable(true);
        assertEquals(cmdLineOptions.isMarioInvulnerable(), true);
        cmdLineOptions.setArgs("-i off");
        assertEquals(cmdLineOptions.isMarioInvulnerable(), false);
    }
}
