/*
 * Copyright (c) 2009-2010, Sergey Karakovskiy and Julian Togelius
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *  Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 *  Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 *  Neither the name of the Mario AI nor the
 * names of its contributors may be used to endorse or promote products
 * derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
 * NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */

package ch.idsia.benchmark.tasks;

import ch.idsia.tools.MarioAIOptions;

/**
 * Created by IntelliJ IDEA. \n User: Sergey Karakovskiy, sergey at idsia dot ch Date: Mar 24, 2010 Time: 12:58:00 PM
 * Package: ch.idsia.maibe.tasks
 */
public class GamePlayTask extends BasicTask implements Task
{
public GamePlayTask(MarioAIOptions marioAIOptions)
{
    super(marioAIOptions);
}

public void doEpisodes(final int amount, final boolean verbose, final int repetitionsOfSingleEpisode)
{
    for (int i = 0; i < amount; ++i)
    {
        options.setLevelLength(200 + (i * 128) + (options.getLevelRandSeed() % (i + 1)));
        options.setLevelType(i % 3);
        options.setLevelRandSeed(options.getLevelRandSeed()+ i);
        options.setLevelDifficulty(i % 10);
        options.setGapsCount(i % 3 == 0);
        options.setCannonsCount(i % 3 != 0);
        options.setCoinsCount(i % 5 != 0);
        options.setBlocksCount(i % 4 != 0);
        options.setDeadEndsCount(i % 10 == 0);
        options.setLevelLadder(i % 10 == 2);
        this.reset();
        this.runSingleEpisode(repetitionsOfSingleEpisode);
        if (verbose)
            System.out.println(environment.getEvaluationInfoAsString());
    }
}
}
