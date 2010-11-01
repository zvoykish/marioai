package ch.idsia.tools.amico;

/**
 * Created by IntelliJ IDEA.
 * User: Sergey Karakovskiy, sergey at idsia dot ch
 * Date: Feb 18, 2010
 * Time: 6:43:16 PM
 * Package: ch.idsia.tools.amico
 */
public class AmiCoJavaPy
{
    public AmiCoJavaPy(String moduleName)
    {
        //TODO: add check for successful initialization
        this.setModuleName(moduleName);
    }

    public native int setModuleName(String moduleName);

    public native String getName();

    public native void integrateObservation(int[] squashedObservation, int[] squashedEnemies, float[] marioPos, float[] enemiesPos, int[] marioState);

    public native int[] getAction();

    public native void giveIntermediateReward(final float intermediateReward);

    public native void reset();

    static
    {
        System.out.println("Java: loading AmiCo...");
        System.loadLibrary("AmiCoJavaPy");
        System.out.println("Java: AmiCo library has been successfully loaded!");
    }
}
