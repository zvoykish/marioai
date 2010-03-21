package ch.idsia.mario.engine;

import ch.idsia.mario.engine.sprites.Mario;
import ch.idsia.mario.environments.Environment;
import ch.idsia.tools.EvaluationInfo;
import ch.idsia.ai.agents.Agent;

/**
 * Created by IntelliJ IDEA. User: Sergey Karakovskiy, sergey at idsia dot ch Date: Feb 26, 2010 Time: 3:55:08 PM
 * Package: ch.idsia.mario
 */
public class MarioEvaluationComponent
{
    private Mario mario;
    private LevelScene levelScene;
    private boolean running;
    private Scene scene;
    private Agent agent = null;
    private int frame;
    private Agent cheatAgent;
    private long delay;

    public void stop()
    {
        running = false;
//        System.out.println("\nmario.x = " + mario.x);
//        System.out.println("mario.xD = " + mario.xDeathPos);
//        System.out.println("mario.getStatus() = " + mario.getStatus());
//        System.out.println("frame = " + frame);
    }


    public EvaluationInfo evaluateOneLevel(int currentTrial, int totalNumberOfTrials)
    {
        running = true;
//        adjustFPS();
        EvaluationInfo evaluationInfo = new EvaluationInfo();

//        VolatileImage image = null;
//        Graphics g = null;
//        Graphics og = null;

//        image = createVolatileImage(320, 240);
//        g = getGraphics();
//        og = image.getGraphics();

//        if (!GlobalOptions.VisualizationOn)
//        {
//            String msgClick = "Vizualization is not available";
//            drawString(og, msgClick, 160 - msgClick.length() * 4, 110, 1);
//            drawString(og, msgClick, 160 - msgClick.length() * 4, 110, 7);
//        }

//        addFocusListener(this);

        // Remember the starting time
        long tm = System.currentTimeMillis();
        long tick = tm;
        int marioStatus = Mario.STATUS_RUNNING;

        mario = levelScene.mario;
        int totalActionsPerfomed = 0;
// TODO: Manage better place for this:
        Mario.resetCoins();

        while (/*Thread.currentThread() == animator*/ running) {
            // Display the next frame of animation.
//                repaint();
            scene.tick();
//            if (gameViewer != null && gameViewer.getContinuousUpdatesState())
//                gameViewer.tick();

            float alpha = 0;

//            og.setColor(Color.RED);
//            if (GlobalOptions.VisualizationOn) {
//                og.fillRect(0, 0, 320, 240);
//                scene.render(og, alpha);
//            }

//            if (agent instanceof ServerAgent && !((ServerAgent) agent).isAvailable()) {
//                System.err.println("Agent became unavailable. Simulation Stopped");
//                running = false;
//                break;
//            }

            boolean[] action = agent.getAction(levelScene);
            if (action != null)
            {
                for (int i = 0; i < Environment.numberOfButtons; ++i)
                    if (action[i])
                    {
                        ++totalActionsPerfomed;
                        break;
                    }
            }
            else
            {
                System.err.println("Null Action received. Skipping simulation...");
                stop();
            }


            //Apply action;
//            scene.keys = action;
            levelScene.mario.keys = action;
            levelScene.mario.cheatKeys = cheatAgent.getAction(null);

/*            if (GlobalOptions.VisualizationOn)
            {
                String msg = "Agent: " + agent.getName();
                LevelScene.drawStringDropShadow(og, msg, 0, 7, 5);

                msg = "Selected Actions: ";
                LevelScene.drawStringDropShadow(og, msg, 0, 8, 6);

                msg = "";
                if (action != null)
                {
                    for (int i = 0; i < Environment.numberOfButtons; ++i)
                        msg += (action[i]) ? scene.keysStr[i] : "      ";
                }
                else
                    msg = "NULL";
                drawString(og, msg, 6, 78, 1);

                if (!this.hasFocus() && tick / 4 % 2 == 0) {
                    String msgClick = "CLICK TO PLAY";
//                    og.setColor(Color.YELLOW);
//                    og.drawString(msgClick, 320 + 1, 20 + 1);
                    drawString(og, msgClick, 160 - msgClick.length() * 4, 110, 1);
                    drawString(og, msgClick, 160 - msgClick.length() * 4, 110, 7);
                }
                og.setColor(Color.DARK_GRAY);
                LevelScene.drawStringDropShadow(og, "FPS: ", 32, 2, 7);
                LevelScene.drawStringDropShadow(og, ((GlobalOptions.FPS > 99) ? "\\infty" : GlobalOptions.FPS.toString()), 32, 3, 7);

                msg = totalNumberOfTrials == -2 ? "" : currentTrial + "(" + ((totalNumberOfTrials == -1) ? "\\infty" : totalNumberOfTrials) + ")";

                LevelScene.drawStringDropShadow(og, "Trial:", 33, 4, 7);
                LevelScene.drawStringDropShadow(og, msg, 33, 5, 7);

                if (width != 320 || height != 240) {
                        g.drawImage(image, 0, 0, 640 * 2, 480 * 2, null);
                } else {
                    g.drawImage(image, 0, 0, null);
                }
                // Win or Die without renderer!! independently.
                marioStatus = ((LevelScene) scene).mario.getStatus();
                if (marioStatus != Mario.STATUS_RUNNING)
                    stop();
            } else */
            {
                // Win or Die without renderer!! independently.
                marioStatus = levelScene.mario.getStatus();
                if (marioStatus != Mario.STATUS_RUNNING)
                    stop();
            }
            // Delay depending on how far we are behind.
            if (delay > 0)
                try
                {
                    tm += delay;
                    Thread.sleep(Math.max(0, tm - System.currentTimeMillis()));
                } catch (InterruptedException e)
                {
                    break;
                }
            // Advance the frame
            frame++;
        }
// EvaluationInfo update
        evaluationInfo.agentType = agent.getClass().getSimpleName();
        evaluationInfo.agentName = agent.getName();
        evaluationInfo.marioStatus = mario.getStatus();
        evaluationInfo.livesLeft = mario.lives;
        evaluationInfo.lengthOfLevelPassedPhys = mario.x;
        evaluationInfo.lengthOfLevelPassedCells = mario.mapX;
        evaluationInfo.totalLengthOfLevelCells = levelScene.level.getWidthCells();
        evaluationInfo.totalLengthOfLevelPhys = levelScene.level.getWidthPhys();
        evaluationInfo.timeSpentOnLevel = levelScene.getTimeSpent();
        evaluationInfo.timeLeft = levelScene.getTimeLeft();
        evaluationInfo.totalTimeGiven = levelScene.getTimeLimit();
        evaluationInfo.numberOfGainedCoins = Mario.coins;
//        evaluationInfo.totalNumberOfCoins   = -1 ; // TODO: total Number of coins.
        evaluationInfo.totalActionsPerfomed = totalActionsPerfomed; // Counted during the play/simulation process
        evaluationInfo.totalFramesPerfomed = frame;
        evaluationInfo.marioMode = mario.getMode();
        evaluationInfo.killsTotal = mario.world.killedCreaturesTotal;
//        evaluationInfo.Memo = "Number of attempt: " + Mario.numberOfAttempts;
//        if (agent instanceof ServerAgent && mario.keys != null /*this will happen if client quits unexpectedly in case of Server mode*/)
//            ((ServerAgent)agent).integrateEvaluationInfo(evaluationInfo);
        return evaluationInfo;
    }
}
