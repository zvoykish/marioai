package com.zvoykish.marioai.input;

/**
 * Created by Zvika on 11/10/14.
 */
public interface MarioGameSummary {
    boolean isWon();

    long getTimePassed();

    long getTimeLeft();

    MarioMode getMarioMode();

    long getNumberOfHitsByCreatures();

    long getPassedCells();

    long getTotalCells();

    long getCoinsGained();

    double getPercentageOfDiscoveredHiddenBlocks();

    double getPercentageOfMushrooms();

    double getPercentageOfFlowers();

    double getPercentageOfCreaturesKilled();

    long getNumberOfKillsWithFire();

    long getNumberOfKillsWithShell();

    long getNumberOfKillsWithStomp();
}
