package com.zvoykish.marioai.output;

/**
 * Created by Zvika on 11/10/14.
 */
public interface MarioAdapterActions {
    MarioAdapterActions duck();

    MarioAdapterActions jump();

    MarioAdapterActions left();

    MarioAdapterActions right();

    MarioAdapterActions runOrShoot();

    boolean[] toBooleanArray();
}
