package ch.idsia.tools;

import ch.idsia.benchmark.mario.engine.sprites.Mario;
import ch.idsia.benchmark.tasks.MarioSystemOfValues;
import ch.idsia.benchmark.tasks.SystemOfValues;

import java.text.DecimalFormat;


/**
 * Created by IntelliJ IDEA.
 * User: Sergey Karakovskiy
 * Date: Apr 12, 2009
 * Time: 12:44:51 AM
 * Package: .Tools
 */

public final class EvaluationInfo
{
private static final int MagicNumberUnDef = -42;

public static final int numberOfElements = 14;

// ordered in alphabetical order;
public int distancePassedCells = MagicNumberUnDef;
// TODO: migrate to all integers.
public float distancePassedPhys = MagicNumberUnDef;
public int flowersDevoured = MagicNumberUnDef;
public int killsByFire = MagicNumberUnDef;
public int killsByShell = MagicNumberUnDef;
public int killsByStomp = MagicNumberUnDef;
public int killsTotal = MagicNumberUnDef;
public int marioMode = MagicNumberUnDef;
public int marioStatus = MagicNumberUnDef;
public int mushroomsDevoured = MagicNumberUnDef;
public int coinsGained = MagicNumberUnDef;
public int timeLeft = MagicNumberUnDef;
public int timeSpent = MagicNumberUnDef;
public int hiddenBlocksFound = MagicNumberUnDef;

public int totalNumberOfCoins = MagicNumberUnDef;
public int collisionsWithCreatures = MagicNumberUnDef;

private static final float[] retFloatArray = new float[EvaluationInfo.numberOfElements];
private static final float[] zeros = new float[EvaluationInfo.numberOfElements];
public String Memo = "";

private static final DecimalFormat df = new DecimalFormat("0.00");
private static MarioSystemOfValues marioSystemOfValues = new MarioSystemOfValues();

public EvaluationInfo()
{
    System.arraycopy(EvaluationInfo.zeros, 0, retFloatArray, 0, EvaluationInfo.numberOfElements);
}

public float computeBasicFitness()
{
    return distancePassedPhys - timeSpent + coinsGained + marioStatus * marioSystemOfValues.win;
}

public float computeWeightedFitness(SystemOfValues sov)
{
    return
            distancePassedPhys * sov.distance +
                    flowersDevoured * sov.flowerFire +
                    marioStatus * sov.win +
                    marioMode * sov.mode +
                    mushroomsDevoured * sov.mushrooms +
                    coinsGained * sov.coins +
                    hiddenBlocksFound * sov.hiddenBlocks +
                    killsTotal * sov.kills +
                    killsByStomp * sov.killedByStomp +
                    killsByFire * sov.killedByFire +
                    killsByShell * sov.killedByShell +
                    timeLeft * sov.timeLeft;
}

public float computeWeightedFitness()
{
    return this.computeWeightedFitness(marioSystemOfValues);
}

public float computeDistancePassed()
{
    return distancePassedPhys;
}

public int computeKillsTotal()
{
    return this.killsTotal;
}

//TODO: possible fitnesses adjustments: penalize for collisions with creatures and especially for suicide. It's a sin.

public float[] toFloatArray()
{
    retFloatArray[0] = this.distancePassedCells;
    retFloatArray[1] = this.distancePassedPhys;
    retFloatArray[2] = this.flowersDevoured;
    retFloatArray[3] = this.killsByFire;
    retFloatArray[4] = this.killsByShell;
    retFloatArray[5] = this.killsByStomp;
    retFloatArray[6] = this.killsTotal;
    retFloatArray[7] = this.marioMode;
    retFloatArray[8] = this.marioStatus;
    retFloatArray[9] = this.mushroomsDevoured;
    retFloatArray[10] = this.coinsGained;
    retFloatArray[11] = this.timeLeft;
    retFloatArray[12] = this.timeSpent;
    retFloatArray[13] = this.hiddenBlocksFound;

    return retFloatArray;
}

public String toString()
{
    return "\n[MarioAI] ~ Evaluation Results:" +
            "\n         Weighted Fitness : " + df.format(computeWeightedFitness()) +
            "\n             Mario Status : " + ((marioStatus == Mario.STATUS_WIN) ? "WIN!" : "Loss...") +
            "\n               Mario Mode : " + Mario.MODES[marioMode] +
            "\nCollisions with creatures : " + collisionsWithCreatures +
            "\n     Passed (Cells, Phys) : " + distancePassedCells + ", " +
            df.format(distancePassedPhys) +
            "\n Time Spent(marioseconds) : " + timeSpent +
            "\n  Time Left(marioseconds) : " + timeLeft +
            "\n             Coins Gained : " + coinsGained + " of " + totalNumberOfCoins + "(" +coinsGained*100/totalNumberOfCoins + "% collected)" +
            "\n      Hidden Blocks Found : " + hiddenBlocksFound +
            "\n       Mushrooms Devoured : " + mushroomsDevoured +
            "\n         Flowers Devoured : " + flowersDevoured +
            "\n              kills Total : " + killsTotal +
            "\n            kills By Fire : " + killsByFire +
            "\n           kills By Shell : " + killsByShell +
            "\n           kills By Stomp : " + killsByStomp +
            ((Memo.equals("")) ? "" : "\nMemo: " + Memo);
}

public String toStringSingleLine()
{
    return "##" +
            " Status: " + ((marioStatus == Mario.STATUS_WIN) ? "WIN!" : "Loss") +
            "; Mode: " + Mario.MODES[marioMode] +
            " +  Passed (Cells, Phys): " + df.format((double) distancePassedCells) + ", " +
            df.format(distancePassedPhys) +
            "; Time Spent: " + timeSpent +
            "; Time Left: " + timeLeft +
            "; Coins: " + coinsGained +
            "; Hidden blocks: " + hiddenBlocksFound +
            "; Mushrooms: " + mushroomsDevoured +
            "; Flowers: " + flowersDevoured +
            "; Collisions: " + collisionsWithCreatures +
            "; kills: " + killsTotal +
            "; By Fire: " + killsByFire +
            "; By Shell: " + killsByShell +
            "; By Stomp: " + killsByStomp;
}
}
