package ch.idsia.benchmark.mario.engine.sprites;

import ch.idsia.benchmark.mario.engine.GlobalOptions;
import ch.idsia.benchmark.mario.engine.LevelScene;

/**
 * Created by IntelliJ IDEA.
 * User: Nikolay Sohryakov, nikolay.sohyrakov@gmail.com
 * Date: Nov 28, 2010
 * Time: 12:21:34 AM
 * Package: ch.idsia.benchmark.mario.engine.sprites
 */
public class WaveGoomba extends Enemy
{
private LevelScene world;
private float sin = 0f;
private float inc = 0.01f;
private int phase = 1;
private int startX;

public WaveGoomba(LevelScene world, int x, int y, int dir, int mapX, int mapY)
{
    super(world, x, y, dir, Sprite.KIND_WAVE_GOOMBA, true, mapX, mapY);
    noFireballDeath = false;
    this.xPic = 0;
    this.yPic = 7;
    this.world = world;
    startX = x;
}

public void move()
{
    if (GlobalOptions.areFrozenCreatures == true)
    {
        return;
    }
    wingTime++;
    if (deadTime > 0)
    {
        deadTime--;

        if (deadTime == 0)
        {
            deadTime = 1;
            for (int i = 0; i < 8; i++)
            {
                world.addSprite(new Sparkle((int) (x + Math.random() * 16 - 8) + 4, (int) (y - Math.random() * 8) + 4, (float) (Math.random() * 2 - 1), (float) Math.random() * -1, 0, 1, 5));
            }
            spriteContext.removeSprite(this);
        }

        if (flyDeath)
        {
            x += xa;
            y += ya;
            ya *= 0.95;
            ya += 1;
        }
        return;
    }


    float sideWaysSpeed = 0.75f;
    //        float sideWaysSpeed = onGround ? 2.5f : 1.2f;

    if (xa > 2)
    {
        facing = 1;
    }
    if (xa < -2)
    {
        facing = -1;
    }

    xa = facing * sideWaysSpeed;
//    xa += facing == 1 ? -wind : wind;
//        mayJump = (onGround);

    xFlipPic = facing == -1;

    runTime += (Math.abs(xa)) + 5;

    int runFrame = ((int) (runTime / 20)) % 2;

    if (!onGround)
    {
        runFrame = 1;
    }

    if (!move(xa, 0)) facing = -facing;
    onGround = false;
    if (Math.abs(sin) >= 0.15f && winged)
    {
        ya *= -1;
        inc *= -1;
        phase *= -1;
    }
    sin += inc;
    move(0, ya);

    if (Math.abs(x - startX) >= 32 && winged)
        facing *= -1;

    ya *= winged ? 0.95f : 0.85f;
    if (onGround)
    {
        xa *= GROUND_INERTIA;
    } else
    {
        xa *= AIR_INERTIA;
    }

    if (!onGround)
    {
        if (winged)
        {
            ya += 0.1f * phase;
        } else
        {
            ya += yaa;
        }
    } else if (winged)
    {
//        ya = -10;
    }

    if (winged) runFrame = wingTime / 4 % 2;

    xPic = runFrame;
}
}
