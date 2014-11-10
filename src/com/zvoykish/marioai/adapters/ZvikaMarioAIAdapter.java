package com.zvoykish.marioai.adapters;

import com.zvoykish.marioai.MarioAIAdapter;
import com.zvoykish.marioai.input.MarioFeature;
import com.zvoykish.marioai.input.MarioGameSummary;
import com.zvoykish.marioai.input.MarioInput;
import com.zvoykish.marioai.output.MarioAdapterActions;
import com.zvoykish.marioai.output.MarioAdapterActionsBuilder;

/**
 * Created by Zvika on 11/10/14.
 */
public class ZvikaMarioAIAdapter implements MarioAIAdapter {
    @Override
    public MarioAdapterActions getActions(MarioInput input) {
        MarioAdapterActionsBuilder builder = MarioAdapterActionsBuilder.Factory.create();
        double a;
        do {
            double b = Math.random();
            if (b > 0.75 && input.is(MarioFeature.CAN_JUMP)) {
                builder = builder.jump();
            } else if (b > 0.5) {
                builder = builder.runOrShoot();
            } else if (b > 0.25) {
                builder = builder.duck();
            }
            a = Math.random();
        } while (a > 0.5);
        return builder.right().build();
    }

    @Override
    public void onGameEnd(MarioGameSummary summary) {
        // Any post processing / evaluation specific to an adapter... e.g. When writing an Evo. adapter - This is the place to extract & process the results...
    }
}
