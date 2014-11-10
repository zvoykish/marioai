package com.zvoykish.marioai.input;

/**
 * Created by Zvika on 11/10/14.
 */
public enum MarioMode {
    SMALL,
    BIG,
    FIRE;

    public static MarioMode fromInt(int value) {
        switch (value) {
            case 0:
                return MarioMode.SMALL;
            case 1:
                return MarioMode.BIG;
            case 2:
                return MarioMode.FIRE;
            default:
                throw new IllegalStateException("Unknown mario state: " + value);
        }
    }
}
