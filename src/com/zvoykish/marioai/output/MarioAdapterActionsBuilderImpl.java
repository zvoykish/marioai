package com.zvoykish.marioai.output;

import ch.idsia.benchmark.mario.environments.Environment;

/**
 * Created by Zvika on 11/10/14.
 */
public class MarioAdapterActionsBuilderImpl implements MarioAdapterActionsBuilder {
    private boolean[] actions;

    public MarioAdapterActionsBuilderImpl() {
        actions = new boolean[Environment.numberOfKeys];
    }

    @Override
    public MarioAdapterActions build() {
        return this;
    }

    @Override
    public MarioAdapterActionsBuilder duck() {
        actions[Environment.MARIO_KEY_DOWN] = true;
        return this;
    }

    @Override
    public MarioAdapterActionsBuilder jump() {
        actions[Environment.MARIO_KEY_JUMP] = true;
        return this;
    }

    @Override
    public MarioAdapterActionsBuilder left() {
        actions[Environment.MARIO_KEY_LEFT] = true;
        return this;
    }

    @Override
    public MarioAdapterActionsBuilder right() {
        actions[Environment.MARIO_KEY_RIGHT] = true;
        return this;
    }

    @Override
    public MarioAdapterActionsBuilder runOrShoot() {
        actions[Environment.MARIO_KEY_SPEED] = true;
        return this;
    }

    @Override
    public boolean[] toBooleanArray() {
        return actions;
    }
}
