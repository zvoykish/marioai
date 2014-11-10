package com.zvoykish.marioai.input;

/**
 * Created by Zvika on 11/10/14.
 */
public interface MarioInput {
    float getMarioX();

    float getMarioY();

    MarioMode getMarioMode();

    boolean is(MarioFeature feature);

    // TODO: Extend this to include the surrounding cells/enemies, etc.....
}
