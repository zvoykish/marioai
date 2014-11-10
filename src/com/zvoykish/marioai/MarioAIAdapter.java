package com.zvoykish.marioai;

import com.zvoykish.marioai.input.MarioGameSummary;
import com.zvoykish.marioai.input.MarioInput;
import com.zvoykish.marioai.output.MarioAdapterActions;

/**
 * Created by Zvika on 11/10/14.
 */
public interface MarioAIAdapter {
    /**
     * Implement to return actions to perform in game, based on the given input.
     *
     * @param input The input to process
     * @return Actions to perform
     * @see com.zvoykish.marioai.output.MarioAdapterActionsBuilder.Factory
     */
    MarioAdapterActions getActions(MarioInput input);

    /**
     * Implement to perform any post processing/evaluation specific to the adapter. <br/>
     * e.g. When writing an Evolutionary computation adapter - This is the place to extract, process and evaluate the results...
     *
     * @param summary The game summary
     */
    void onGameEnd(MarioGameSummary summary);
}
