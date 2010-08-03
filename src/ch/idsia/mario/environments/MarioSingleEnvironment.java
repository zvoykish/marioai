package ch.idsia.mario.environments;

/**
 * Created by IntelliJ IDEA.
 * User: Sergey Karakovskiy, sergey.karakovskiy@gmail.com
 * Date: Aug 3, 2010
 * Time: 8:28:23 PM
 * Package: ch.idsia.mario.environments
 */
public class MarioSingleEnvironment
{
    private static MarioSingleEnvironment ourInstance = new MarioSingleEnvironment();

    public static MarioSingleEnvironment getInstance()
    {
        return ourInstance;
    }

    private MarioSingleEnvironment()
    {
    }
}
