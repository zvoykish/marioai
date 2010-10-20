package ch.idsia.benchmark.mario.engine;

import ch.idsia.benchmark.mario.engine.sprites.Sprite;

/**
 * Created by IntelliJ IDEA.
 * User: Sergey Karakovskiy, sergey@idsia.ch
 * Date: Aug 5, 2009
 * Time: 7:05:46 PM
 * Package: ch.idsia.benchmark.mario.engine
 */

public class GeneralizerLevelScene implements Generalizer
{
private static final int CANNON_MUZZLE = -82;
private static final int CANNON_TRUNK = -80;
private static final int COIN_ANIM = Sprite.KIND_COIN_ANIM;  //1
private static final int BREAKABLE_BRICK = -20;
private static final int UNBREAKABLE_BRICK = -22; //a rock with animated question mark
private static final int BRICK = -24;           //a rock with animated question mark
private static final int FLOWER_POT = -90;
private static final int BORDER_CANNOT_PASS_THROUGH = -60;
private static final int BORDER_HILL = -62;
// TODO : resolve this FLOWER_POT_OR_CANNON = -85;
private static final int FLOWER_POT_OR_CANNON = -85;

public byte ZLevelGeneralization(byte el, int ZLevel)
{
    if (el == 0)
        return 0;
    switch (ZLevel)
    {
        case (0):
            switch (el)
            {
                case 16:  // brick, simple, without any surprise.
                case 17:  // brick with a hidden coin
                case 18:  // brick with a hidden friendly flower
                    return BREAKABLE_BRICK;
                case 21:       // question brick, contains coin
                case 22:       // question brick, contains flower/mushroom
                    return UNBREAKABLE_BRICK; // question brick, contains something
                case 34:
                    return COIN_ANIM;
                case 4:
                    return BORDER_CANNOT_PASS_THROUGH;
                case 14:
                    return CANNON_MUZZLE;
                case 30:
                case 46:
                    return CANNON_TRUNK;
                case 10:
                case 11:
                case 26:
                case 27:
                    return FLOWER_POT;
                case 1:
                    return 0; //hidden block
                case -124:
                case -123:
                case -122:
                case -74:
                    return BORDER_HILL;
                case -108:
                case -107:
                case -106:
                    return 0; //background of the hill. empty space
            }
            return el;
        case (1):
            switch (el)
            {
                case 16:  // brick, simple, without any surprise.
                case 17:  // brick with a hidden coin
                case 18:  // brick with a hidden flower
                case 21:       // question brick, contains coin
                case 22:       // question brick, contains flower/mushroom
                    return BRICK; // any brick
                case 1:   // hidden block
                case (-111):
                case (-108):
                case (-107):
                case (-106):
                case (15): // Sparcle, irrelevant
                    return 0;
                case (34):
                    return COIN_ANIM;
                case (-128):
                case (-127):
                case (-126):
                case (-125):
                case (-120):
                case (-119):
                case (-118):
                case (-117):
                case (-116):
                case (-115):
                case (-114):
                case (-113):
                case (-112):
                case (-110):
                case (-109):
                case (-104):
                case (-103):
                case (-102):
                case (-101):
                case (-100):
                case (-99):
                case (-98):
                case (-97):
                case (-96):
                case (-95):
                case (-94):
                case (-93):
                case (-69):
                case (-65):
                case (-88):
                case (-87):
                case (-86):
                case (-85):
                case (-84):
                case (-83):
                case (-82):
                case (-81):
                case (-77):
                case (4):  // kicked hidden brick
                case (9):
                    return BORDER_CANNOT_PASS_THROUGH;   // border, cannot pass through, can stand on
//                    case(9):
//                        return -12; // hard formation border. Pay attention!
                case (-124):
                case (-123):
                case (-122):
                case (-76):
                case (-74):
                    return BORDER_HILL; // half-border, can jump through from bottom and can stand on
                case (10):
                case (11):
                case (26):
                case (27): //flower pot
                case (14):
                case (30):
                case (46): // canon
                    return FLOWER_POT_OR_CANNON;  // angry flower pot or cannon
            }
            System.err.println("ZLevelMapElementGeneralization: Unknown value el = " + el + " Possible Level tiles bug; " +
                    "Please, inform sergey@idsia.ch or julian@togelius.com. Thanks!");
            return el;
        case (2):
            switch (el)
            {
                //cancel out half-borders, that could be passed through
                case (0):
                case (-108):
                case (-107):
                case (-106):
                case 1:   //hidden block
                case (15): // Sparcle, irrelevant
                    return 0;
                case (34): // coins
                    return COIN_ANIM;
                case 16:  // brick, simple, without any surprise.
                case 17:  // brick with a hidden coin
                case 18:  // brick with a hidden flower
                case 21:       // question brick, contains coin
                case 22:       // question brick, contains flower/mushroom
                    //here bricks are any objects cannot jump through and can stand on
                case 4: //kicked hidden block
                case 9:
                case (10):
                case (11):
                case (26):
                case (27): //flower pot
                case (14):
                case (30):
                case (46): // canon
                    return BORDER_CANNOT_PASS_THROUGH; // question brick, contains something
            }
            return 1;  // everything else is "something", so it is 1
    }
    System.err.println("Unkown ZLevel Z" + ZLevel);
    return el; //TODO: Throw unknown ZLevel exception
}
}
