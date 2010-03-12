package ch.idsia.mario.engine;

public class MThread extends Thread implements Runnable
{
    private MarioVisualComponent m = null;
    final private LevelScene levelScene;
    final private int width;
    final private int height;

    public MThread(LevelScene levelScene, int width, int height)
    {
        this.levelScene = levelScene;
        this.width = width;
        this.height = height;
    }

    public MarioVisualComponent getMVC()
    {
        while (m == null) try
        {
            Thread.sleep(500);
            System.out.println("m = " + m);
        } catch (InterruptedException e)
        {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return m;
    }

    public void run()
    {
        m = MarioVisualComponent.Create(width, height, levelScene);
    }
}
