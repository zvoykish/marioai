package ch.idsia.mario.engine;

import ch.idsia.tools.GameViewer;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class GlobalOptions {
    public static boolean Labels = false;
    public static boolean MarioAlwaysInCenter = false;
    public static Integer FPS = 24;
    public static int MaxFPS = 100;
    public static boolean pauseWorld = false;

    public static boolean VisualizationOn = true;
    public static boolean GameVeiwerOn = true;

    private static MarioComponent marioComponent = null;
    private static MarioVisualComponent marioVisualComponent;
    private static GameViewer gameViewer = null;
    public static boolean TimerOn = true;

    //    public static Defaults defaults = new Defaults();
    public static boolean GameVeiwerContinuousUpdatesOn = false;
    public static boolean PowerRestoration;

    public static boolean StopSimulationIfWin;
    public static boolean isMarioInvulnerable;
    public static int jumpTicks = 0;
    public static String MAIBeVersionStr = "0.1";

    public static void registerMarioComponent(MarioComponent mc)
    {
        marioComponent = mc;
    }

    public static void registerMarioVisualComponent(MarioVisualComponent mc)
    {
        marioVisualComponent = mc;
    }

    public static MarioComponent getMarioComponent()
    {   return marioComponent;                       }
    

    public static void registerGameViewer(GameViewer gv)
    {
        gameViewer = gv;
    }

    public static void AdjustMarioComponentFPS() { marioComponent.adjustFPS(); }

    public static void gameViewerTick()
    {
        if (gameViewer != null)
            gameViewer.tick();
//        else
//            LOGGER.println("GameViewer is not available. Request for dump ignored.", LOGGER.VERBOSE_MODE.ERROR);
    }

    public static String getDateTime(Long d)
    {
        DateFormat dateFormat = (d == null) ? new SimpleDateFormat("yyyy/MM/dd HH:mm:ss:ms") :
                new SimpleDateFormat("HH:mm:ss:ms") ;
        if (d != null)
            dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        Date date = (d == null) ? new Date() : new Date(d);
        return dateFormat.format(date);
    }
}
