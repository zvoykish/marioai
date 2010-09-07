package ch.idsia.unittests;

import ch.idsia.tools.CreaturesMaskParser;
import junit.framework.TestCase;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

/**
 * Created by IntelliJ IDEA.
 * User: Sergey Karakovskiy, sergey.karakovskiy@gmail.com
 * Date: Sep 7, 2010
 * Time: 12:57:21 AM
 * Package: ch.idsia.unittests
 */
public class CreaturesMaskParserTest extends TestCase
{
    @BeforeTest
    public void setUp() {
    }

    @AfterTest
    public void tearDown() {
    }

    @Test
    public void testCanAddEnemies_EmptyMask()
    {
        CreaturesMaskParser creatures = new CreaturesMaskParser("0");
        assertEquals(false, creatures.canAdd());
    }

    @Test
    public void testCanAddEnemies_OnlyFlowerEnabled()
    {
        CreaturesMaskParser creatures = new CreaturesMaskParser("000000001");
        assertEquals(false, creatures.canAdd());
    }
}
