package com.zvoykish.marioai.input;

import ch.idsia.benchmark.mario.engine.sprites.Mario;
import ch.idsia.tools.EvaluationInfo;

/**
 * Created by Zvika on 11/10/14.
 */
public class MarioGameSummaryAdapter implements MarioGameSummary {
    private EvaluationInfo evaluationInfo;

    public MarioGameSummaryAdapter(EvaluationInfo evaluationInfo) {
        this.evaluationInfo = evaluationInfo;
    }

    @Override
    public boolean isWon() {
        return evaluationInfo.marioStatus == Mario.STATUS_WIN;
    }

    @Override
    public long getTimePassed() {
        return evaluationInfo.timeSpent;
    }

    @Override
    public long getTimeLeft() {
        return evaluationInfo.timeLeft;
    }

    @Override
    public MarioMode getMarioMode() {
        return MarioMode.fromInt(evaluationInfo.marioMode);
    }

    @Override
    public long getNumberOfHitsByCreatures() {
        return evaluationInfo.collisionsWithCreatures;
    }

    @Override
    public long getPassedCells() {
        return evaluationInfo.distancePassedCells;
    }

    @Override
    public long getTotalCells() {
        return evaluationInfo.levelLength;
    }

    @Override
    public long getCoinsGained() {
        return evaluationInfo.coinsGained;
    }

    @Override
    public double getPercentageOfDiscoveredHiddenBlocks() {
        return 100 * ((double)evaluationInfo.hiddenBlocksFound / evaluationInfo.totalNumberOfHiddenBlocks);
    }

    @Override
    public double getPercentageOfMushrooms() {
        return 100*((double)evaluationInfo.mushroomsDevoured / evaluationInfo.totalNumberOfMushrooms);
    }

    @Override
    public double getPercentageOfFlowers() {
        return 100*((double)evaluationInfo.flowersDevoured / evaluationInfo.totalNumberOfFlowers);
    }

    @Override
    public double getPercentageOfCreaturesKilled() {
        return 100*((double)evaluationInfo.killsTotal / evaluationInfo.totalNumberOfCreatures);
    }

    @Override
    public long getNumberOfKillsWithFire() {
        return evaluationInfo.killsByFire;
    }

    @Override
    public long getNumberOfKillsWithShell() {
        return evaluationInfo.killsByShell;
    }

    @Override
    public long getNumberOfKillsWithStomp() {
        return evaluationInfo.killsByStomp;
    }
}
