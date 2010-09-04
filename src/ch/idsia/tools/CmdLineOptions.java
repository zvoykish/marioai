package ch.idsia.tools;

import ch.idsia.benchmark.mario.engine.GlobalOptions;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by IntelliJ IDEA.
 * User: Sergey Karakovskiy
 * Date: Apr 25, 2009
 * Time: 9:05:20 AM
 * Package: ch.idsia.tools
 */

/**
 * The <code>CmdLineOptions</code> class handles the command-line options
 * It sets up parameters from command line if there are any.
 * Defaults are used otherwise.
 *
 * @author Sergey Karakovskiy
 * @version 1.0, Apr 25, 2009
 * @see ch.idsia.utils.ParameterContainer
 * @see ch.idsia.tools.EvaluationOptions
 * @since MarioAI0.1
 */

public final class CmdLineOptions extends EvaluationOptions
{
    private static final HashMap<String, CmdLineOptions> CmdLineOptionsMapString = new HashMap<String, CmdLineOptions>();
    private String optionsString;

    public CmdLineOptions(String[] args)
    {
        super();
        this.setArgs(args);
    }

//    @Deprecated

    public CmdLineOptions(String args)
    {
        //USE CmdLineOptions.getCmdLineOptionsClassByString(String args) method
        super();
        this.setArgs(args);
    }

    public CmdLineOptions()
    {
        super();
        this.setArgs("");
    }

    public void setArgs(String argString)
    {
        if (!"".equals(argString))
            this.setArgs(argString.trim().split("\\s+"));
        else
            this.setArgs((String[]) null);
    }


    public void setArgs(String[] args)
    {
//        if (args.length > 0 && !args[0].startsWith("-") /*starts with a path to agent then*/)
//        {
//            this.setAgent(args[0]);
//
//            String[] shiftedargs = new String[args.length - 1];
//            System.arraycopy(args, 1, shiftedargs, 0, args.length - 1);
//            this.setUpOptions(shiftedargs);
//        }
//        else
        this.setUpOptions(args);

        if (isEcho())
        {
            this.printOptions(false);
        }
        GlobalOptions.observationGridWidth = getReceptiveFieldWidth();
        GlobalOptions.observationGridHeight = getReceptiveFieldHeight();
        GlobalOptions.VISUAL_COMPONENT_HEIGHT = getViewHeight();
        GlobalOptions.VISUAL_COMPONENT_WIDTH = getViewWidth();
//        Environment.ObsWidth = GlobalOptions.observationGridWidth/2;
//        Environment.ObsHeight = GlobalOptions.observationGridHeight/2;
        GlobalOptions.isShowReceptiveField = isReceptiveFieldVisualized();
        GlobalOptions.isGameplayStopped = isGameplayStopped();
    }

    public float getGravity()
    {
        return f(getParameterValue("-gr"));
    }


    public int getViewWidth()
    {
        return i(getParameterValue("-vw"));
    }

    public int getViewHeight()
    {
        return i(getParameterValue("-vh"));
    }

    public void printOptions(boolean singleLine)
    {
        System.out.println("\n[MarioAI] : Options have been set to:");
        for (Map.Entry<String, String> el : optionsHashMap.entrySet())
            if (singleLine)
                System.out.print(el.getKey() + " " + el.getValue() + " ");
            else
                System.out.println(el.getKey() + " " + el.getValue() + " ");
    }

    public static CmdLineOptions getOptionsByString(String argString)
    {
        if (CmdLineOptionsMapString.get(argString) == null)
        {
            final CmdLineOptions value = new CmdLineOptions(argString.trim().split("\\s+"));
            CmdLineOptionsMapString.put(argString, value);
            return value;
        }
        return CmdLineOptionsMapString.get(argString);
    }

    public static CmdLineOptions getDefaultOptions()
    {
        return getOptionsByString("");
    }

    public Boolean isToolsConfigurator()
    {
        return b(getParameterValue("-tc"));
    }

    public Boolean isGameViewer()
    {
        return b(getParameterValue("-gv"));
    }

    public Boolean isGameViewerContinuousUpdates()
    {
        return b(getParameterValue("-gvc"));
    }

    public Boolean isEcho()
    {
        return b(getParameterValue("-echo"));
    }

    public Boolean isGameplayStopped()
    {
        return b(getParameterValue("-stop"));
    }

    public String getPyAmiCoModuleName()
    {
        return getParameterValue("-pym");
    }

    public Integer getReceptiveFieldWidth()
    {
        int ret = i(getParameterValue("-rfw"));

        if (ret % 2 == 0)
        {
            System.err.println("\nWrong value for receptive field length: " + ret++ +
                    " ; receptive field length set to " + ret);
        }
        return ret;
    }

    private Integer getReceptiveFieldHeight()
    {
        int ret = i(getParameterValue("-rfh"));
        if (ret % 2 == 0)
        {
            System.err.println("\nWrong value for receptive field height: " + ret++ +
                    " ; receptive field height set to " + ret);
        }
        return ret;
    }

    public Boolean isReceptiveFieldVisualized()
    {
        return b(getParameterValue("-srf"));
    }

//    @Deprecated
//    public int[] toIntArray()
//    {
//        return new int[]
//                {
//                        this.isGameViewer() ? 1 : 0,           /*0*/
//                        this.isMarioInvulnerable() ? 1 : 0,    /*1*/
//                        this.getLevelDifficulty(),             /*2*/
//                        this.getLevelLength(),                 /*3*/
//                        this.getLevelRandSeed(),               /*4*/
//                        this.getLevelType(),                   /*5*/
//                        this.getMarioMode(),                   /*6*/
//                        this.getFPS(),                         /*7*/
//                        this.isPowerRestoration() ? 1 : 0,     /*8*/
//                        this.isPauseWorld() ? 1 : 0,           /*9*/
//                        this.isTimer() ? 1 : 0,                /*10*/
//                        // TODO:SK remove redundant opts(-1 -- no time limit)
//                        this.isToolsConfigurator() ? 1 : 0,    /*11*/
//                        this.getTimeLimit(),                   /*12*/
//                        this.isViewAlwaysOnTop() ? 1 : 0,      /*13*/
//                        this.isVisualization() ? 1 : 0,        /*14*/
//                        this.getViewLocation().x,              /*15*/
//                        this.getViewLocation().y,              /*16*/
//                        this.getZLevelEnemies(),               /*17*/
//                        this.getZLevelScene(),                 /*18*/
//                        this.getLevelHeight(),                 /*19*/
//                        this.getDeadEndsCount() ? Integer.MAX_VALUE : 0,       /*20*/
//                        this.getCannonsCount()  ? Integer.MAX_VALUE : 0,       /*21*/
//                        this.getHillStraightCount() ? Integer.MAX_VALUE : 0,   /*22*/
//                        this.getTubesCount() ? Integer.MAX_VALUE : 0,          /*23*/
//                        this.getBlocksCount() ? Integer.MAX_VALUE : 0,         /*24*/
//                        this.getCoinsCount() ? Integer.MAX_VALUE : 0,          /*25*/
//                        this.getGapsCount() ? Integer.MAX_VALUE : 0,           /*26*/
//                        this.getHiddenBlocksCount() ? Integer.MAX_VALUE : 0,   /*27*/
//                        Integer.valueOf(this.getEnemies()),                    /*28*/
//                        this.isFlatLevel() ? 1 : 0                             /*29*/
//                };
//    }
//
}

//!!!        "-ag",    ?????? ??? ???????? ??????????? %,5^$#<>%
// !!!       "-amico",
//!!!        "-echo",
// !!!       "-ewf",
// !!!       "-gv",
//        "-gvc",
//        "-i",
//        "-ld",
//        "-ll",
//        "-ls",
//        "-lt",
//!!!<<<        "-m",
//        "-mm",
//        "-maxFPS",
//                    "-not",
//                    "-port",
//        "-pr",
//        "-pw",
//!!!        "-pym",
//                    "-server",
//                    "-ssiw",
//        "-t",
//        "-tc",
//        "-tl",
//        "-vaot",
//        "-vis",
//        "-vlx",
//        "-vly",
//        "-ze",
//        "-zs"
//        "-lh"
//        "-lde"   level: dead ends count
//        "-lc"    level: cannons count
//        "-lhs"   level: HillStraight count
//        "-ltb"   level: Tubes count
//        "-lg"    level: gaps count
//        "-lhb"   level: hidden blocks count
//        "-le"    level: enemies enabled
//        "-lb"    level: blocks count
//        "-lco"   level: coins count
//        "-lf"    level: flat level