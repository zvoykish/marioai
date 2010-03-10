package ch.idsia.mario.engine;

import ch.idsia.ai.agents.Agent;
import ch.idsia.ai.agents.human.CheaterKeyboardAgent;
import ch.idsia.mario.engine.sprites.Mario;
import ch.idsia.mario.environments.Environment;
import ch.idsia.tools.EvaluationInfo;
import ch.idsia.tools.GameViewer;
import ch.idsia.tools.tcp.ServerAgent;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.image.VolatileImage;
import java.util.ArrayList;
import java.util.List;


public class MarioComponent extends JComponent implements Runnable, /*KeyListener,*/ /*FocusListener,*/ Environment
{
    private static final long serialVersionUID = 790878775993203817L;
    public static final int TICKS_PER_SECOND = 24;

    private boolean running = false;
    private int width, height;
    private GraphicsConfiguration graphicsConfiguration;
    private Scene scene;
//    private boolean focused = false;

    int frame;
    int delay;
    Thread animator;

    private int ZLevelEnemies = 1;
    private int ZLevelScene = 1;

    public void setGameViewer(GameViewer gameViewer)
    {
        this.gameViewer = gameViewer;
    }

    private GameViewer gameViewer = null;

    private Agent agent = null;
    private CheaterKeyboardAgent cheatAgent = null;

    private KeyAdapter prevHumanKeyBoardAgent;
    private Mario mario = null;
    private LevelScene levelScene = null;

    public MarioComponent(int width, int height) {
        adjustFPS();

        this.setFocusable(true);
        this.setEnabled(true);
        this.width = width;
        this.height = height;

        Dimension size = new Dimension(width, height);

        setPreferredSize(size);
        setMinimumSize(size);
        setMaximumSize(size);

        setFocusable(true);

        if (this.cheatAgent == null)
        {
            this.cheatAgent = new CheaterKeyboardAgent();
            this.addKeyListener(cheatAgent);
        }        

        GlobalOptions.registerMarioComponent(this);
    }

    public void adjustFPS()
    {
        int fps = GlobalOptions.FPS;
        delay = (fps > 0) ? (fps >= GlobalOptions.MaxFPS) ? 0 : (1000 / fps) : 100;
//        System.out.println("Delay: " + delay);
    }

    public void paint(Graphics g) {
    }

    public void update(Graphics g) {
    }

    public void init()
    {
        graphicsConfiguration = getGraphicsConfiguration();
//        if (graphicsConfiguration != null) {
            Art.init(graphicsConfiguration);
//        }
    }

    public void start() {
        if (!running) {
            running = true;
            animator = new Thread(this, "Game Thread");
            animator.start();
        }
    }

    public void stop() {
        running = false;
        System.out.println("\nmario.x = " + mario.x);
        System.out.println("mario.xD = " + mario.xDeathPos);
        System.out.println("mario.getStatus() = " + mario.getStatus());
        System.out.println("frame = " + frame);
    }

    public void run() {}

    public EvaluationInfo run1(int currentTrial)
    {
        running = true;
        adjustFPS();
        EvaluationInfo evaluationInfo = new EvaluationInfo();

        VolatileImage thisVolatileImage = null;
        Graphics thisGraphics = null;
        Graphics thisVolatileImageGraphics = null;

        
        thisVolatileImage = this.createVolatileImage(320, 240);
        thisGraphics = getGraphics();
        thisVolatileImageGraphics = thisVolatileImage.getGraphics();

        if (!GlobalOptions.VisualizationOn)
        {
            String msgClick = "Vizualization is not available";
            drawString(thisVolatileImageGraphics, msgClick, 160 - msgClick.length() * 4, 110, 1);
            drawString(thisVolatileImageGraphics, msgClick, 160 - msgClick.length() * 4, 110, 7);
        }

//        addFocusListener(this);

        // Remember the starting time
        long tm = System.currentTimeMillis();
        int marioStatus = Mario.STATUS_RUNNING;

        mario = ((LevelScene) scene).mario;
        int totalActionsPerfomed = 0;
// TODO: Manage better place for this:
        Mario.resetCoins();

        while (/*Thread.currentThread() == animator*/ running)
        {
            // Display the next frame of animation.
//                repaint();
            levelScene.tick();
            if (gameViewer != null && gameViewer.getContinuousUpdatesState())
                gameViewer.tick();

            float alpha = 0;

//            thisVolatileImageGraphics.setColor(Color.RED);
            if (GlobalOptions.VisualizationOn) {
//                thisVolatileImageGraphics.fillRect(0, 0, 320, 240);
//                levelScene.render(thisVolatileImageGraphics, alpha);
            }

            if (agent instanceof ServerAgent && !((ServerAgent) agent).isAvailable()) {
                System.err.println("Agent became unavailable. Simulation Stopped");
                running = false;
                break;
            }

            boolean[] action = agent.getAction(this/*DummyEnvironment*/);
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
            ((LevelScene) scene).mario.keys = action;
            ((LevelScene) scene).mario.cheatKeys = cheatAgent.getAction(null);

            if (GlobalOptions.VisualizationOn)
                thisGraphics.drawImage(thisVolatileImage, 0, 0, null);
            
            if (false)
            {
                String msg = "Agent: " + agent.getName();
//                LevelScene.drawStringDropShadow(thisVolatileImageGraphics, msg, 0, 7, 5);

                msg = "Selected Actions: ";
//                LevelScene.drawStringDropShadow(thisVolatileImageGraphics, msg, 0, 8, 6);

                msg = "";
                if (action != null)
                {
                    for (int i = 0; i < Environment.numberOfButtons; ++i)
                        msg += (action[i]) ? scene.keysStr[i] : "      ";
                    }
                else
                    msg = "NULL";                    
                drawString(thisVolatileImageGraphics, msg, 6, 78, 1);

                if (!this.hasFocus() && tm / 4 % 2 == 0) {
                    String msgClick = "CLICK TO PLAY";
//                    thisVolatileImageGraphics.setColor(Color.YELLOW);
//                    thisVolatileImageGraphics.drawString(msgClick, 320 + 1, 20 + 1);
                    drawString(thisVolatileImageGraphics, msgClick, 160 - msgClick.length() * 4, 110, 1);
                    drawString(thisVolatileImageGraphics, msgClick, 160 - msgClick.length() * 4, 110, 7);
                }
                thisVolatileImageGraphics.setColor(Color.DARK_GRAY);
//                LevelScene.drawStringDropShadow(thisVolatileImageGraphics, "FPS: ", 32, 2, 7);
//                LevelScene.drawStringDropShadow(thisVolatileImageGraphics, ((GlobalOptions.FPS > 99) ? "\\infty" : GlobalOptions.FPS.toString()), 32, 3, 7);

//                msg = totalNumberOfTrials == -2 ? "" : currentTrial + "(" + ((totalNumberOfTrials == -1) ? "\\infty" : totalNumberOfTrials) + ")";

//                LevelScene.drawStringDropShadow(thisVolatileImageGraphics, "Trial:", 33, 4, 7);
//                LevelScene.drawStringDropShadow(thisVolatileImageGraphics, msg, 33, 5, 7);

                if (width != 320 || height != 240) {
                        thisGraphics.drawImage(thisVolatileImage, 0, 0, 640 * 2, 480 * 2, null);
                } else {
                    thisGraphics.drawImage(thisVolatileImage, 0, 0, null);
                }
                // Win or Die without renderer!! independently.
                marioStatus = ((LevelScene) scene).mario.getStatus();
                if (marioStatus != Mario.STATUS_RUNNING)
                    stop();                
            } else {
                // Win or Die without renderer!! independently.
                marioStatus = ((LevelScene) scene).mario.getStatus();
                if (marioStatus != Mario.STATUS_RUNNING)
                    stop();
            }
            // Delay depending on how far we are behind.
            if (delay > 0)
                try {
                    tm += delay;
                    Thread.sleep(Math.max(0, tm - System.currentTimeMillis()));
                } catch (InterruptedException e) {
                    break;
                }
            // Advance the frame
            frame++;
        }
//=========
        evaluationInfo.agentType = agent.getClass().getSimpleName();
        evaluationInfo.agentName = agent.getName();
        evaluationInfo.marioStatus = mario.getStatus();
        evaluationInfo.livesLeft = mario.lives;
        evaluationInfo.lengthOfLevelPassedPhys = mario.x;
        evaluationInfo.lengthOfLevelPassedCells = mario.mapX;
        evaluationInfo.totalLengthOfLevelCells = levelScene.level.getWidthCells();
        evaluationInfo.totalLengthOfLevelPhys = levelScene.level.getWidthPhys();
        evaluationInfo.timeSpentOnLevel = levelScene.getStartTime();
        evaluationInfo.timeLeft = levelScene.getTimeLeft();
        evaluationInfo.totalTimeGiven = levelScene.getTimeLimit();
        evaluationInfo.numberOfGainedCoins = Mario.coins;
//        evaluationInfo.totalNumberOfCoins   = -1 ; // TODO: total Number of coins.
        evaluationInfo.totalActionsPerfomed = totalActionsPerfomed; // Counted during the play/simulation process
        evaluationInfo.totalFramesPerfomed = frame;
        evaluationInfo.marioMode = mario.getMode();
        evaluationInfo.killsTotal = mario.world.killedCreaturesTotal;
//        evaluationInfo.Memo = "Number of attempt: " + Mario.numberOfAttempts;
        if (agent instanceof ServerAgent && mario.keys != null /*this will happen if client quits unexpectedly in case of Server mode*/)
            ((ServerAgent)agent).integrateEvaluationInfo(evaluationInfo);
        return evaluationInfo;
    }

    private void drawString(Graphics g, String text, int x, int y, int c) {
        char[] ch = text.toCharArray();
        for (int i = 0; i < ch.length; i++) {
            g.drawImage(Art.font[ch[i] - 32][c], x + i * 8, y, null);
        }
    }

    public void startLevel(long seed, int difficulty, int type, int levelLength, int timeLimit) {
        scene = new LevelScene(graphicsConfiguration, this, seed, difficulty, type, levelLength, timeLimit);
        levelScene = ((LevelScene) scene);
        scene.init();
    }

    public void levelFailed() {
//        scene = mapScene;
        Mario.lives--;
        stop();
    }

//    public void focusGained(FocusEvent arg0) {
//        focused = true;
//    }

//    public void focusLost(FocusEvent arg0) {
//        focused = false;
//    }

    public void levelWon() {
        stop();
//        scene = mapScene;
//        mapScene.levelWon();
    }

    public void toTitle() {
//        Mario.resetStatic();
//        scene = new TitleScene(this, graphicsConfiguration);
//        scene.init();
    }

    public List<String> getTextObservation(boolean Enemies, boolean LevelMap, boolean Complete, int ZLevelMap, int ZLevelEnemies) {
        if (scene instanceof LevelScene)
            return ((LevelScene) scene).LevelSceneAroundMarioASCII(Enemies, LevelMap, Complete, ZLevelMap, ZLevelEnemies);
        else {
            return new ArrayList<String>();
        }
    }

    // Chaning ZLevel during the game on-the-fly;
    public byte[][] getMergedObservationZZ(int zLevelScene, int zLevelEnemies) {
        return levelScene.getMergedObservationZZ(zLevelScene, zLevelEnemies);
    }

    public byte[][] getLevelSceneObservationZ(int zLevelScene) {
        return levelScene.getLevelSceneObservationZ(zLevelScene);
    }

    public byte[][] getEnemiesObservationZ(int zLevelEnemies) {
        return levelScene.getEnemiesObservationZ(zLevelEnemies);
    }

    public int getKillsTotal() {
        return mario.world.killedCreaturesTotal;
    }

    public int getKillsByFire() {
        return mario.world.killedCreaturesByFireBall;
    }

    public int getKillsByStomp() {
        return mario.world.killedCreaturesByStomp;
    }

    public int getKillsByShell() {
        return mario.world.killedCreaturesByShell;
    }

    public int[] getMarioState()
    {
        int[] marioState = new int[]{
                this.getMarioStatus(),
                this.getMarioMode(),
                this.isMarioOnGround() ? 1 : 0,
                this.isMarioAbleToJump() ? 1 : 0,
                this.isMarioAbleToShoot() ? 1 : 0,
                this.isMarioCarrying() ? 1 : 0,
                this.getKillsTotal(),
                this.getKillsByFire(),
                this.getKillsByStomp(),
                this.getKillsByStomp(),
                this.getKillsByShell()
        };
        return marioState;
    }

    public void performAction(boolean[] action)
    {
        // no need here...
    }

    public boolean isLevelFinished()
    {
        return levelScene.isLevelFinished();
    }

    public double[] getEvaluationInfo()
    {
        double[] evaluationInfoArray = new double[]{
                mario.getStatus(),
                mario.x,
                mario.mapX,
                levelScene.level.getWidthCells(),
                levelScene.level.getWidthPhys(),
                levelScene.getStartTime(),
                levelScene.getTimeLeft(),
                levelScene.getTimeLimit(),
                Mario.coins,
                mario.getMode(),
                mario.world.killedCreaturesTotal,
        };
        return evaluationInfoArray;
    }

    public boolean isMarioAbleToShoot() {
        return mario.isCanShoot();  
    }

    public int getMarioStatus()
    {
        return mario.getStatus();
    }

    public double[] getSerializedFullObservationZZ(int ZLevelScene, int ZLevelEnemies)
    {
        // TODO:SK, serialize all data to a sole double[]        
        return new double[0];  //To change body of implemented methods use File | Settings | File Templates.
    }

    public byte[] getSerializedLevelSceneObservationZ(int ZLevelScene)
    {
        // serialization into arrays of primitive types to speed up the data transfer.
        byte[][] levelScene = this.getLevelSceneObservationZ(ZLevelScene);
        byte[][] enemies = this.getEnemiesObservationZ(ZLevelEnemies);
        int rows = Environment.HalfObsHeight*2;
        int cols = Environment.HalfObsWidth*2;

        byte[] squashedLevelScene = new byte[rows * cols];
//        byte[] squashedEnemies = new byte[enemies.length * enemies[0].length];

        for (int i = 0; i < squashedLevelScene.length; ++i)
        {
            squashedLevelScene[i] = levelScene[i / cols][i % rows];
//            squashedEnemies[i] = enemies[i / cols][i % rows];
        }
        return squashedLevelScene; 
    }

    public byte[] getSerializedEnemiesObservationZ(int ZLevelEnemies)
    {
        // serialization into arrays of primitive types to speed up the data transfer.
        byte[][] levelScene = this.getLevelSceneObservationZ(ZLevelScene);
        byte[][] enemies = this.getEnemiesObservationZ(ZLevelEnemies);
        int rows = Environment.HalfObsHeight*2;
        int cols = Environment.HalfObsWidth*2;

        byte[] squashedLevelScene = new byte[rows * cols];
        byte[] squashedEnemies = new byte[enemies.length * enemies[0].length];

        for (int i = 0; i < squashedLevelScene.length; ++i)
        {
            squashedEnemies[i] = enemies[i / cols][i % rows];
        }
        return squashedEnemies;
    }

    public byte[] getSerializedMergedObservationZZ(int ZLevelScene, int ZLevelEnemies)
    {
        return new byte[0];  //To change body of implemented methods use File | Settings | File Templates.
    }

    public float[] getCreaturesFloatPos()
    {
        float[] enemies = this.getEnemiesFloatPos();
        float ret[] = new float[enemies.length + 2];
        System.arraycopy(this.getMarioFloatPos(), 0, ret, 0, 2);
        System.arraycopy(enemies, 0, ret, 2, enemies.length);
        return ret;
    }

    public byte[][] getCompleteObservation()
    {
        return levelScene.getMergedObservationZZ(this.ZLevelScene, this.ZLevelEnemies);
    }

    public byte[][] getEnemiesObservation()
    {
        return levelScene.getEnemiesObservationZ(this.ZLevelEnemies);
    }

    public byte[][] getLevelSceneObservation() {
        return levelScene.getLevelSceneObservationZ(this.ZLevelScene);
    }

    public void resetDefault()
    {
        
    }

    public void reset(int[] setUpData)
    {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void tick()
    {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public boolean isMarioOnGround() {
        return mario.isOnGround();
    }

    public boolean isMarioAbleToJump() {
        return mario.mayJump();
    }

    public void setAgent(Agent agent) {
        this.agent = agent;
        if (agent instanceof KeyAdapter) {
            if (prevHumanKeyBoardAgent != null)
                this.removeKeyListener(prevHumanKeyBoardAgent);
            this.prevHumanKeyBoardAgent = (KeyAdapter) agent;
            this.addKeyListener(prevHumanKeyBoardAgent);
        }
    }

    public void setMarioInvulnerable(boolean invulnerable)
    {
        Mario.isMarioInvulnerable = invulnerable;
    }

    public void setPaused(boolean paused) {
        levelScene.paused = paused;
    }

    public void setZLevelEnemies(int ZLevelEnemies) {
        this.ZLevelEnemies = ZLevelEnemies;
    }

    public void setZLevelScene(int ZLevelScene) {
        this.ZLevelScene = ZLevelScene;
    }

    public float[] getMarioFloatPos()
    {
        return new float[]{this.mario.x, this.mario.y};
    }

    public float[] getEnemiesFloatPos()
    {
        return levelScene.getEnemiesFloatPos();
    }

    public int getMarioMode()
    {
        return mario.getMode();
    }

    public boolean isMarioCarrying()
    {
        return mario.carried != null;
    }
}