package ch.idsia.mario.engine;

import ch.idsia.mario.engine.level.BgLevelGenerator;
import ch.idsia.mario.engine.level.Level;
import ch.idsia.mario.engine.level.LevelGenerator;
import ch.idsia.mario.engine.level.SpriteTemplate;
import ch.idsia.mario.engine.sprites.*;

import java.awt.*;
import java.io.DataInputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;


public class LevelScene extends Scene implements SpriteContext
{
    private List<Sprite> sprites = new ArrayList<Sprite>();
    private List<Sprite> spritesToAdd = new ArrayList<Sprite>();
    private List<Sprite> spritesToRemove = new ArrayList<Sprite>();

    public Level level;
    public Mario mario;
    public float xCam, yCam, xCamO, yCamO;
    public static Image tmpImage;
    private int tick;

    private LevelRenderer layer;
    private BgRenderer[] bgLayer = new BgRenderer[2];

    private GraphicsConfiguration graphicsConfiguration;

    public boolean paused = false;
    public int startTime = 0;
    public int timeLeft;

    public int getTotalTime() {  return totalTime; }

    public void setTotalTime(int totalTime) {  this.totalTime = totalTime; }

    private int totalTime = 200;

    //    private Recorder recorder = new Recorder();
    //    private Replayer replayer = null;

    private long levelSeed;
    private MarioComponent renderer;
    private int levelType;
    private int levelDifficulty;
    private int levelLength;

    public LevelScene(GraphicsConfiguration graphicsConfiguration, MarioComponent renderer, long seed, int levelDifficulty, int type, int levelLength)
    {
        this.graphicsConfiguration = graphicsConfiguration;
        this.levelSeed = seed;
        this.renderer = renderer;
        this.levelDifficulty = levelDifficulty;
        this.levelType = type;
        this.levelLength = levelLength;
    }

    private String mapElToStr(int el)
    {
        String s = "";
        if  (el == 0)
            s = "##";
        s += (el == mario.kind) ? "M" : el;
        while (s.length() < 4)
            s += "#";
        return s + " ";
    }

    private byte ZLevelMapElementGeneralization(byte el, int ZLevel)
    {
        switch (ZLevel)
        {
            case(0):
                return el;
            case(1):
                switch(el)
                {
                    case(-108):
                    case(-107):
                    case(-106):
                        return 0;
                    case(-128):
                    case(-127):
                    case(-126):
                    case(-125):
                    case(-120):
                    case(-119):
                    case(-118):
                    case(-117):
                    case(-116):
                    case(-115):
                    case(-114):
                    case(-101):
                    case(-112):
                    case(-111):
                    case(-110):
                    case(-109):
                    case(-104):
                    case(-103):
                    case(-102):
                    case(-100):
                    case(-99):
                    case(-98):
                    case(-97):
                    case(-69):
                    case(-88):
                    case(-87):
                    case(-86):
                    case(-85):
                    case(-84):
                    case(-83):
                    case(-82):
                    case(-81):
                    case(4):
                    case(30): // canon
                        return -10;   // border, cannot pass through, can stand on
                    case(9):
                        return -12; // hard formation border. Pay attention!
                    case(-124):
                    case(-123):
                    case(-122):
                    case(-76):
                    case(-74):
                        return -11; // half-border, can jump through from bottom and can stand on
                    case(10): case(11): case(26): case(27):
                        return 20; // flower
                }
                return el;
            case(2):
                return (el == 0) ? el : -20;
        }
        return el; //TODO: Throw unknown ZLevel exception
    }

    public byte[][] levelSceneObservation(int ZLevel)
    {
        // TODO: Move to constants          11
        int HalfObsWidth = 11;
        int HalfObsHeight = 11;
        byte[][] ret = new byte[HalfObsWidth*2][HalfObsHeight*2];
        //TODO: Move to constants 16
        int MarioXInMap = (int)mario.x/16;
        int MarioYInMap = (int)mario.y/16;

        for (int y = MarioYInMap - HalfObsHeight, obsX = 0; y < MarioYInMap + HalfObsHeight; y++, obsX++)
        {
            for (int x = MarioXInMap - HalfObsWidth, obsY = 0; x < MarioXInMap + HalfObsWidth; x++, obsY++)
            {
                if (x >=0 && x <= level.xExit && y >= 0 && y < level.height)
                {
                    ret[obsX][obsY] = ZLevelMapElementGeneralization(level.map[x][y], ZLevel);
                }
                else
                    ret[obsX][obsY] = 0;
                if (x == MarioXInMap && y == MarioYInMap)
                    ret[obsX][obsY] = mario.kind;
            }
        }
        return ret;
    }

    public byte[][] enemiesObservation(int ZLevel) {
//        // TODO: Move to constants          11
        int HalfObsWidth = 11;
        int HalfObsHeight = 11;
        byte[][] ret = new byte[HalfObsWidth*2][HalfObsHeight*2];
//        //TODO: Move to constants 16
//        int MarioXInMap = (int)mario.x/16;
//        int MarioYInMap = (int)mario.y/16;
//
//        for (int w = 0; w < level.width; w++)
//            for (int h = 0; h < level.height; h++)
//                level.observation[w][h] = -1;
//        for (Sprite sprite : sprites)
//            if (sprite.mapX >= 0 && sprite.mapX < level.observation.length &&
//                    sprite.mapY >= 0 && sprite.mapY < level.observation[0].length)
//                level.observation[sprite.mapX][sprite.mapY] = sprite.kind;
//
//        for (int y = MarioYInMap - HalfObsHeight, obsX = 0; y < MarioYInMap + HalfObsHeight; y++, obsX++)
//        {
//            for (int x = MarioXInMap - HalfObsWidth, obsY = 0; x < MarioXInMap + HalfObsWidth; x++, obsY++)
//            {
//                if (x >=0 && x <= level.xExit && y >= 0 && y < level.height)
//                {
//                    ret[obsX][obsY] = ZLevelMapElementGeneralization(level.map[x][y], ZLevel);
//                }
//                else
//                    ret[obsX][obsY] = 0;
//                if (x == MarioXInMap && y == MarioYInMap)
//                    ret[obsX][obsY] = mario.kind;
//            }
//        }
//
        return ret;
    }


    public List<String> LevelSceneAroundMarioASCII(boolean Enemies, boolean LevelMap, boolean CompleteObservation, int ZLevel){
//        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os));//        bw.write("\nTotal world width = " + level.width);
        List<String> ret = new ArrayList<String>();
        if (level != null && mario != null)
        {
            ret.add("Total world width = " + level.width);
            ret.add("Total world height = " + level.height);
            ret.add("Physical Mario Position (x,y): (" + mario.x + "," + mario.y + ")");
            int ObservationWidth = 21;
            ret.add("Mario Observation Width " + ObservationWidth);
            ret.add("X Exit Position: " + level.xExit);
            int MarioXInMap = (int)mario.x/16;
            int MarioYInMap = (int)mario.y/16;
            ret.add("Calibrated Mario Position (x,y): (" + MarioXInMap + "," + MarioYInMap + ")\n");

            byte[][] levelScene = levelSceneObservation(ZLevel);
            if (LevelMap)
            {
                ret.add("~ZLevel: Z" + ZLevel + " map:\n");
//                for (int y = 0; y < level.height; y++)
//                {
//                    String tmpData = "";
//                    for (int x = MarioXInMap - ObservationWidth/2; x < MarioXInMap + ObservationWidth/2 +1; x++)
//                    {
//                        if (x >=0 && x <= level.xExit)
//                            tmpData += mapElToStr(ZLevelMapElementGeneralization(level.map[x][y], ZLevel));
//                    }
//                    ret.add(tmpData);
//                }
                for (int y = 0; y < levelScene[0].length; ++y)
                {
                    String tmpData = "";
                    for (int x = 0; x < levelScene.length; ++x)
                        tmpData += mapElToStr(levelScene[y][x]);
                    ret.add(tmpData);
                }
            }

            byte[][] enemiesObservation = null;
            if (Enemies || CompleteObservation)
            {
                enemiesObservation = enemiesObservation(1);
            }

            if (Enemies)
            {

                ret.add("~ZLevel: Z" + ZLevel + " Enemies Observation:\n");
                for (int y = 0; y < level.height; y++)
                {
                    String tmpData = "";
                    for (int x = MarioXInMap - 10; x < MarioXInMap + 10 + 1; x++)
                    {
                        if (x >=0 && x <= level.xExit)
                            tmpData += enemiesObservation[x][y] + " ";
                    }
                    ret.add(tmpData);
                }
            }

            if (CompleteObservation)
            {
                ret.add("~ZLevel: Z" +  ZLevel + "===========\nAll objects: (LevelMap[x,y], Sprite[x,y])==/* Mario ~> M! */=====\n");
                for (int y = 0; y < level.height; y++)
                {
                    String tmpData = "";
                    for (int x = MarioXInMap - 10; x < MarioXInMap + 10 + 1; x++)
                    {
                        if (x >=0 && x < level.width)
                            tmpData += "(" + mapElToStr(ZLevelMapElementGeneralization(level.map[x][y], ZLevel)) + "," +
                                    (((MarioXInMap == x) && (MarioYInMap == y) ? "M!" : level.observation[x][y]) + ")");
                    }
                    ret.add(tmpData);
                }
            }
        }
        else
            ret.add("level is not available");
        return ret;
    }

    public void init()
    {
        try
        {
            Level.loadBehaviors(new DataInputStream(LevelScene.class.getResourceAsStream("tiles.dat")));
        }
        catch (IOException e)
        {
            e.printStackTrace();
            System.exit(0);
        }
        /*        if (replayer!=null)
         {
         level = LevelGenerator.createLevel(2048, 15, replayer.nextLong());
         }
         else
         {*/
//        level = LevelGenerator.createLevel(320, 15, levelSeed);
        level = LevelGenerator.createLevel(levelLength, 15, levelSeed, levelDifficulty, levelType);
        //        }

        /*        if (recorder != null)
         {
         recorder.addLong(LevelGenerator.lastSeed);
         }*/


        paused = false;
        Sprite.spriteContext = this;
        sprites.clear();
        layer = new LevelRenderer(level, graphicsConfiguration, 320, 240);
        for (int i = 0; i < 2; i++)
        {
            int scrollSpeed = 4 >> i;
            int w = ((level.width * 16) - 320) / scrollSpeed + 320;
            int h = ((level.height * 16) - 240) / scrollSpeed + 240;
            Level bgLevel = BgLevelGenerator.createLevel(w / 32 + 1, h / 32 + 1, i == 0, levelType);
            bgLayer[i] = new BgRenderer(bgLevel, graphicsConfiguration, 320, 240, scrollSpeed);
        }
        mario = new Mario(this);
        sprites.add(mario);
        startTime = 1;

        timeLeft = totalTime*15;

        tick = 0;
    }

    public int fireballsOnScreen = 0;

    List<Shell> shellsToCheck = new ArrayList<Shell>();

    public void checkShellCollide(Shell shell)
    {
        shellsToCheck.add(shell);
    }

    List<Fireball> fireballsToCheck = new ArrayList<Fireball>();

    public void checkFireballCollide(Fireball fireball)
    {
        fireballsToCheck.add(fireball);
    }

    public void tick()
    {
        if (GlobalOptions.TimerOn)
            timeLeft--; // TODO: Stop time. Done
        if (timeLeft==0)
        {
            mario.die();
        }
        xCamO = xCam;
        yCamO = yCam;

        if (startTime > 0)
        {
            startTime++;
        }

        float targetXCam = mario.x - 160;

        xCam = targetXCam;

        if (xCam < 0) xCam = 0;
        if (xCam > level.width * 16 - 320) xCam = level.width * 16 - 320;

        /*      if (recorder != null)
         {
         recorder.addTick(mario.getKeyMask());
         }
         
         if (replayer!=null)
         {
         mario.setKeys(replayer.nextTick());
         }*/

        fireballsOnScreen = 0;

        for (Sprite sprite : sprites)
        {
            if (sprite != mario)
            {
                float xd = sprite.x - xCam;
                float yd = sprite.y - yCam;
                if (xd < -64 || xd > 320 + 64 || yd < -64 || yd > 240 + 64)
                {
                    removeSprite(sprite);
                }
                else
                {
                    if (sprite instanceof Fireball)
                    {
                        fireballsOnScreen++;
                    }
                }
            }
        }

        if (paused)
        {
            for (Sprite sprite : sprites)
            {
                if (sprite == mario)
                {
                    sprite.tick();
                }
                else
                {
                    sprite.tickNoMove();
                }
            }
        }
        else
        {
            tick++;
            level.tick();

            boolean hasShotCannon = false;
            int xCannon = 0;

            for (int x = (int) xCam / 16 - 1; x <= (int) (xCam + layer.width) / 16 + 1; x++)
                for (int y = (int) yCam / 16 - 1; y <= (int) (yCam + layer.height) / 16 + 1; y++)
                {
                    int dir = 0;

                    if (x * 16 + 8 > mario.x + 16) dir = -1;
                    if (x * 16 + 8 < mario.x - 16) dir = 1;

                    SpriteTemplate st = level.getSpriteTemplate(x, y);

                    if (st != null)
                    {
                        if (st.lastVisibleTick != tick - 1)
                        {
                            if (st.sprite == null || !sprites.contains(st.sprite))
                            {
                                st.spawn(this, x, y, dir);
                            }
                        }

                        st.lastVisibleTick = tick;
                    }

                    if (dir != 0)
                    {
                        byte b = level.getBlock(x, y);
                        if (((Level.TILE_BEHAVIORS[b & 0xff]) & Level.BIT_ANIMATED) > 0)
                        {
                            if ((b % 16) / 4 == 3 && b / 16 == 0)
                            {
                                if ((tick - x * 2) % 100 == 0)
                                {
                                    xCannon = x;
                                    for (int i = 0; i < 8; i++)
                                    {
                                        addSprite(new Sparkle(x * 16 + 8, y * 16 + (int) (Math.random() * 16), (float) Math.random() * dir, 0, 0, 1, 5));
                                    }
                                    addSprite(new BulletBill(this, x * 16 + 8 + dir * 8, y * 16 + 15, dir));
                                    hasShotCannon = true;
                                }
                            }
                        }
                    }
                }

            for (Sprite sprite : sprites)
            {
                sprite.tick();
            }

            for (Sprite sprite : sprites)
            {
                sprite.collideCheck();
            }

            for (Shell shell : shellsToCheck)
            {
                for (Sprite sprite : sprites)
                {
                    if (sprite != shell && !shell.dead)
                    {
                        if (sprite.shellCollideCheck(shell))
                        {
                            if (mario.carried == shell && !shell.dead)
                            {
                                mario.carried = null;
                                shell.die();
                            }
                        }
                    }
                }
            }
            shellsToCheck.clear();

            for (Fireball fireball : fireballsToCheck)
            {
                for (Sprite sprite : sprites)
                {
                    if (sprite != fireball && !fireball.dead)
                    {
                        if (sprite.fireballCollideCheck(fireball))
                        {
                            fireball.die();
                        }
                    }
                }
            }
            fireballsToCheck.clear();
        }

        sprites.addAll(0, spritesToAdd);
        sprites.removeAll(spritesToRemove);
        spritesToAdd.clear();
        spritesToRemove.clear();
    }

    private DecimalFormat df = new DecimalFormat("00");
    private DecimalFormat df2 = new DecimalFormat("000");

    public void render(Graphics g, float alpha)
    {
        int xCam = (int) (mario.xOld + (mario.x - mario.xOld) * alpha) - 160;
        int yCam = (int) (mario.yOld + (mario.y - mario.yOld) * alpha) - 120;

        if (GlobalOptions.MarioAlwaysInCenter)
        {
        }
        else
        {
            //int xCam = (int) (xCamO + (this.xCam - xCamO) * alpha);
            //        int yCam = (int) (yCamO + (this.yCam - yCamO) * alpha);
            if (xCam < 0) xCam = 0;
            if (yCam < 0) yCam = 0;
            if (xCam > level.width * 16 - 320) xCam = level.width * 16 - 320;
            if (yCam > level.height * 16 - 240) yCam = level.height * 16 - 240;
        }
        //      g.drawImage(Art.background, 0, 0, null);

        for (int i = 0; i < 2; i++)
        {
            bgLayer[i].setCam(xCam, yCam);
            bgLayer[i].render(g, tick, alpha);
        }

        g.translate(-xCam, -yCam);

        for (Sprite sprite : sprites)
        {
            if (sprite.layer == 0) sprite.render(g, alpha);
        }

        g.translate(xCam, yCam);

        layer.setCam(xCam, yCam);
        layer.render(g, tick, paused?0:alpha);
        layer.renderExit0(g, tick, paused?0:alpha, mario.winTime==0);

        g.translate(-xCam, -yCam);

        // TODO: Dump out of render!
        if (mario.cheatKeys[Mario.KEY_DUMP_CURRENT_WORLD])
            for (int w = 0; w < level.width; w++)
                for (int h = 0; h < level.height; h++)
                    level.observation[w][h] = -1;

        for (Sprite sprite : sprites)
        {
            if (sprite.layer == 1) sprite.render(g, alpha);
            if (mario.cheatKeys[Mario.KEY_DUMP_CURRENT_WORLD] && sprite.mapX >= 0 && sprite.mapX < level.observation.length &&
                    sprite.mapY >= 0 && sprite.mapY < level.observation[0].length)
                level.observation[sprite.mapX][sprite.mapY] = sprite.kind;

        }

        g.translate(xCam, yCam);
        g.setColor(Color.BLACK);
        layer.renderExit1(g, tick, paused?0:alpha);

        drawStringDropShadow(g, "MARIO LIVES: " + df.format(Mario.lives), 0, 0, 7);
        drawStringDropShadow(g, "#########", 0, 1, 7);

        drawStringDropShadow(g, "COINS", 14, 0, 7);
        drawStringDropShadow(g, " "+df.format(Mario.coins), 14, 1, 7);

        drawStringDropShadow(g, "DIFFICULTY", 24, 0, 7);
        drawStringDropShadow(g, " "+ this.levelDifficulty, 24, 1, 7);

        drawStringDropShadow(g, "WorldPause", 24, 2, 7);
        drawStringDropShadow(g, " "+ mario.world.paused, 24, 3, 7);


        drawStringDropShadow(g, "TIME", 35, 0, 7);
        int time = (timeLeft+15-1)/15;
        if (time<0) time = 0;
        drawStringDropShadow(g, " "+df2.format(time), 35, 1, 7);
        if (GlobalOptions.Labels)
        {
            g.drawString("xCam: " + xCam + "yCam: " + yCam, 70, 40);
            g.drawString("x : " + mario.x + "y: " + mario.y, 70, 50);
            g.drawString("xOld : " + mario.xOld + "yOld: " + mario.yOld, 70, 60);
        }

//        if (mario.keys[Mario.KEY_DUMP_CURRENT_WORLD])
//        {
//            g.fillRect(0, 0, 640*2, 480*2);
//            g.setColor(Color.YELLOW);
//            int y_dump = 0;
//            g.drawString("GAME VIEWER: ", 320, y_dump += 11 );
//            g.setColor(Color.GREEN);
//            for (String s: LevelSceneAroundMarioASCII(true, true, true) )
//
//                g.drawString(s, (y_dump > 250) ? 0 : 320, y_dump += 11 );
//        }

        if (startTime > 0)
        {
            float t = startTime + alpha - 2;
            t = t * t * 0.6f;
            renderBlackout(g, 160, 120, (int) (t));
        }
//        mario.x>level.xExit*16
        if (mario.winTime > 0)
        {
            float t = mario.winTime + alpha;
            t = t * t * 0.2f;

            if (t > 900)
            {
                renderer.levelWon();
                //              replayer = new Replayer(recorder.getBytes());
//                init();
            }

            renderBlackout(g, mario.xDeathPos - xCam, mario.yDeathPos - yCam, (int) (320 - t));
        }

        if (mario.deathTime > 0)
        {
            float t = mario.deathTime + alpha;
            t = t * t * 0.4f;

            if (t > 1800)
            {
                renderer.levelFailed();
                //              replayer = new Replayer(recorder.getBytes());
//                init();
            }

            renderBlackout(g, (int) (mario.xDeathPos - xCam), (int) (mario.yDeathPos - yCam), (int) (320 - t));
        }
    }

    private void drawStringDropShadow(Graphics g, String text, int x, int y, int c)
    {
        drawString(g, text, x*8+5, y*8+5, 0);
        drawString(g, text, x*8+4, y*8+4, c);
    }

    private void drawString(Graphics g, String text, int x, int y, int c)
    {
        char[] ch = text.toCharArray();
        for (int i = 0; i < ch.length; i++)
        {
            g.drawImage(Art.font[ch[i] - 32][c], x + i * 8, y, null);
        }
    }

    private void renderBlackout(Graphics g, int x, int y, int radius)
    {
        if (radius > 320) return;

        int[] xp = new int[20];
        int[] yp = new int[20];
        for (int i = 0; i < 16; i++)
        {
            xp[i] = x + (int) (Math.cos(i * Math.PI / 15) * radius);
            yp[i] = y + (int) (Math.sin(i * Math.PI / 15) * radius);
        }
        xp[16] = 320;
        yp[16] = y;
        xp[17] = 320;
        yp[17] = 240;
        xp[18] = 0;
        yp[18] = 240;
        xp[19] = 0;
        yp[19] = y;
        g.fillPolygon(xp, yp, xp.length);

        for (int i = 0; i < 16; i++)
        {
            xp[i] = x - (int) (Math.cos(i * Math.PI / 15) * radius);
            yp[i] = y - (int) (Math.sin(i * Math.PI / 15) * radius);
        }
        xp[16] = 320;
        yp[16] = y;
        xp[17] = 320;
        yp[17] = 0;
        xp[18] = 0;
        yp[18] = 0;
        xp[19] = 0;
        yp[19] = y;

        g.fillPolygon(xp, yp, xp.length);
    }


    public void addSprite(Sprite sprite)
    {
        spritesToAdd.add(sprite);
        sprite.tick();
    }

    public void removeSprite(Sprite sprite)
    {
        spritesToRemove.add(sprite);
    }

    public float getX(float alpha)
    {
        int xCam = (int) (mario.xOld + (mario.x - mario.xOld) * alpha) - 160;
        //        int yCam = (int) (mario.yOld + (mario.y - mario.yOld) * alpha) - 120;
        //int xCam = (int) (xCamO + (this.xCam - xCamO) * alpha);
        //        int yCam = (int) (yCamO + (this.yCam - yCamO) * alpha);
        if (xCam < 0) xCam = 0;
        //        if (yCam < 0) yCam = 0;
        //        if (yCam > 0) yCam = 0;
        return xCam + 160;
    }

    public float getY(float alpha)
    {
        return 0;
    }

    public void bump(int x, int y, boolean canBreakBricks)
    {
        byte block = level.getBlock(x, y);

        if ((Level.TILE_BEHAVIORS[block & 0xff] & Level.BIT_BUMPABLE) > 0)
        {
            bumpInto(x, y - 1);
            level.setBlock(x, y, (byte) 4);
            level.setBlockData(x, y, (byte) 4);

            if (((Level.TILE_BEHAVIORS[block & 0xff]) & Level.BIT_SPECIAL) > 0)
            {
                if (!Mario.large)
                {
                    addSprite(new Mushroom(this, x * 16 + 8, y * 16 + 8));
                }
                else
                {
                    addSprite(new FireFlower(this, x * 16 + 8, y * 16 + 8));
                }
            }
            else
            {
                Mario.getCoin();
                addSprite(new CoinAnim(x, y));
            }
        }

        if ((Level.TILE_BEHAVIORS[block & 0xff] & Level.BIT_BREAKABLE) > 0)
        {
            bumpInto(x, y - 1);
            if (canBreakBricks)
            {
                level.setBlock(x, y, (byte) 0);
                for (int xx = 0; xx < 2; xx++)
                    for (int yy = 0; yy < 2; yy++)
                        addSprite(new Particle(x * 16 + xx * 8 + 4, y * 16 + yy * 8 + 4, (xx * 2 - 1) * 4, (yy * 2 - 1) * 4 - 8));
            }
            else
            {
                level.setBlockData(x, y, (byte) 4);
            }
        }
    }

    public void bumpInto(int x, int y)
    {
        byte block = level.getBlock(x, y);
        if (((Level.TILE_BEHAVIORS[block & 0xff]) & Level.BIT_PICKUPABLE) > 0)
        {
            Mario.getCoin();
            level.setBlock(x, y, (byte) 0);
            addSprite(new CoinAnim(x, y + 1));
        }

        for (Sprite sprite : sprites)
        {
            sprite.bumpCheck(x, y);
        }
    }

    public void update(boolean[] action)
    {
        //TODO: Use NumberOfActions or just mario.keys = action;
        System.arraycopy(action, 0, mario.keys, 0, 6);           
    }

    public int getStartTime() {  return startTime / 15;    }

    public int getTimeLeft() {        return timeLeft / 15;    }

}