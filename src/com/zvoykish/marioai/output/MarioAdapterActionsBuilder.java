package com.zvoykish.marioai.output;

/**
 * Created by Zvika on 11/10/14.
 */
public interface MarioAdapterActionsBuilder extends MarioAdapterActions {
    MarioAdapterActions build();

    @Override
    MarioAdapterActionsBuilder duck();

    @Override
    MarioAdapterActionsBuilder jump();

    @Override
    MarioAdapterActionsBuilder left();

    @Override
    MarioAdapterActionsBuilder right();

    @Override
    MarioAdapterActionsBuilder runOrShoot();

    public static class Factory {
        public static MarioAdapterActionsBuilder create() {
            return new MarioAdapterActionsBuilderImpl();
        }
    }
}
