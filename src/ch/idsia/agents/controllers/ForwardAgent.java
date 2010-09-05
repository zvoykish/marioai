package ch.idsia.agents.controllers;

import ch.idsia.agents.Agent;
import ch.idsia.benchmark.mario.engine.sprites.Mario;
import ch.idsia.benchmark.mario.environments.Environment;

/**
 * Created by IntelliJ IDEA.
 * User: Sergey Karakovskiy
 * Date: Apr 8, 2009
 * Time: 4:03:46 AM
 * Package: ch.idsia.controllers.agents.controllers;
 */
public class ForwardAgent extends BasicMarioAIAgent implements Agent
{
    int trueJumpCounter = 0;
    int trueSpeedCounter = 0;

    public ForwardAgent()
    {
        super("ForwardAgent");
        reset();
    }

    public void reset()
    {
        action = new boolean[Environment.numberOfButtons];
        action[Mario.KEY_RIGHT] = true;
        action[Mario.KEY_SPEED] = true;
        trueJumpCounter = 0;
        trueSpeedCounter = 0;
    }

    private boolean DangerOfGap(byte[][] levelScene)
    {
        // TODO: introduce variables instead of constants.
        for (int x = 7; x < 19; ++x)
        {
            boolean f = true;
            for (int y = 9; y < 19; ++y)
            {
                if (levelScene[y][x] != 0)
                    f = false;
            }
            if (f ||
                    levelScene[marioCenterPos[0] + 1][marioCenterPos[1]] == 0 ||
                    (marioState[1] > 0 &&
                            (levelScene[marioCenterPos[0] + 1][marioCenterPos[1] - 1] != 0 ||
                                    levelScene[marioCenterPos[0] + 1][marioCenterPos[1]] != 0)))
                return true;
        }
        return false;
    }

    private boolean DangerOfGap()
    {
        return DangerOfGap(levelScene);
    }

    public boolean[] getAction()
    {
        // this Agent requires observation integrated in advance.

        if (mergedObservation[marioCenterPos[0]][marioCenterPos[1] + 2] != 0 ||
                mergedObservation[marioCenterPos[0]][marioCenterPos[1] + 1] != 0 ||
                DangerOfGap())
        {
            if (isMarioAbleToJump || (!isMarioOnGround && action[Mario.KEY_JUMP]))
            {
                action[Mario.KEY_JUMP] = true;
            }
            ++trueJumpCounter;
        } else
        {
            action[Mario.KEY_JUMP] = false;
            trueJumpCounter = 0;
        }

        if (trueJumpCounter > 16)
        {
            trueJumpCounter = 0;
            action[Mario.KEY_JUMP] = false;
        }

        action[Mario.KEY_SPEED] = DangerOfGap();
        return action;
    }
}
