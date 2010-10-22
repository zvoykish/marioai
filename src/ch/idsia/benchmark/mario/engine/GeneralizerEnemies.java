package ch.idsia.benchmark.mario.engine;

import ch.idsia.benchmark.mario.engine.sprites.Sprite;

/**
 * Created by IntelliJ IDEA.
 * User: Sergey Karakovskiy, sergey@idsia.ch
 * Date: Aug 5, 2009
 * Time: 7:04:19 PM
 * Package: ch.idsia.benchmark.mario.engine
 */

public class GeneralizerEnemies implements Generalizer
{

public byte ZLevelGeneralization(byte el, int ZLevel)
{
    switch (ZLevel)
    {
        case (0):
            switch (el)
            {
                // cancel irrelevant sprite codes
                case (Sprite.KIND_COIN_ANIM):
                case (Sprite.KIND_PARTICLE):
                case (Sprite.KIND_SPARCLE):
                case (Sprite.KIND_MARIO):
                    return Sprite.KIND_NONE;
            }
            return el;   // all the rest should go as is
        case (1):
            switch (el)
            {
                case (Sprite.KIND_COIN_ANIM):
                case (Sprite.KIND_PARTICLE):
                case (Sprite.KIND_SPARCLE):
                case (Sprite.KIND_MARIO):
                    return Sprite.KIND_NONE;
                case (Sprite.KIND_FIRE_FLOWER):
                    return Sprite.KIND_FIRE_FLOWER;
                case (Sprite.KIND_MUSHROOM):
                    return Sprite.KIND_MUSHROOM;
                case (Sprite.KIND_FIREBALL):
                    return Sprite.KIND_FIREBALL;
                case (Sprite.KIND_BULLET_BILL):
                case (Sprite.KIND_GOOMBA):
                case (Sprite.KIND_GOOMBA_WINGED):
                case (Sprite.KIND_GREEN_KOOPA):
                case (Sprite.KIND_GREEN_KOOPA_WINGED):
                case (Sprite.KIND_RED_KOOPA):
                case (Sprite.KIND_RED_KOOPA_WINGED):
                case (Sprite.KIND_SHELL):
                    return Sprite.KIND_GOOMBA;
                case (Sprite.KIND_SPIKY):
                case (Sprite.KIND_ENEMY_FLOWER):
                case (Sprite.KIND_SPIKY_WINGED):
                    return Sprite.KIND_SPIKY;
            }
            System.err.println("Z1 UNKOWN el = " + el);
            return el;
        case (2):
            switch (el)
            {
                case (Sprite.KIND_COIN_ANIM):
                case (Sprite.KIND_PARTICLE):
                case (Sprite.KIND_SPARCLE):
                case (Sprite.KIND_FIREBALL):
                case (Sprite.KIND_MARIO):
                case (Sprite.KIND_FIRE_FLOWER):
                case (Sprite.KIND_MUSHROOM):
                    return Sprite.KIND_NONE;
                case (Sprite.KIND_BULLET_BILL):
                case (Sprite.KIND_GOOMBA):
                case (Sprite.KIND_GOOMBA_WINGED):
                case (Sprite.KIND_GREEN_KOOPA):
                case (Sprite.KIND_GREEN_KOOPA_WINGED):
                case (Sprite.KIND_RED_KOOPA):
                case (Sprite.KIND_RED_KOOPA_WINGED):
                case (Sprite.KIND_SHELL):
                case (Sprite.KIND_SPIKY):
                case (Sprite.KIND_ENEMY_FLOWER):
                case (Sprite.KIND_SPIKY_WINGED):
                    return 1;
            }
            System.err.println("ERROR: Z2 UNKNOWNN el = " + el);
            return 1;
    }
    return el; //TODO:TASK:|L|, Build Hierarchy of Exceptions, here: Throw unknown ZLevel exception

}
}
