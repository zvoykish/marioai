package ch.idsia.ai.agents.ai;

import ch.idsia.ai.agents.Agent;
import ch.idsia.mario.environments.Environment;

/**
 * Created by IntelliJ IDEA.
 * User: Sergey Karakovskiy
 * Date: Apr 25, 2009
 * Time: 12:30:41 AM
 * Package: ch.idsia.ai.agents.ai;
 */
public class BasicAIAgent implements Agent
{
    protected boolean action[] = new boolean[Environment.numberOfButtons];
    protected String name = "Instance_of_BasicAIAgent._Change_this_name";
    final protected byte[][] levelScene;
    final protected byte[][] enemies;
    protected float[] marioFloatPos = null;
    protected float[] enemiesFloatPos = null;
    protected int[] marioState = null;

    protected int marioStatus;
    protected int marioMode;
    protected boolean isMarioOnGround;
    protected boolean isMarioAbleToJump;
    protected boolean isMarioAbleToShoot;
    protected boolean isMarioCarrying;
    protected int getKillsTotal;
    protected int getKillsByFire;
    protected int getKillsByStomp;
    protected int getKillsByShell;

    public BasicAIAgent(String s)
    {
        setName(s);
        levelScene = new byte[Environment.HalfObsHeight * 2][Environment.HalfObsWidth * 2];
        enemies = new byte[Environment.HalfObsHeight * 2][Environment.HalfObsWidth * 2];
    }

    public void integrateObservation(byte[] serializedLevelSceneObservationZ, byte[] serializedEnemiesObservationZ, float[] marioFloatPos, float[] enemiesFloatPos, int[] marioState)
    {
        int k = 0;
        for (int i = 0; i < levelScene.length; ++i)
        {
            for (int j = 0; j < levelScene[0].length; ++j)
            {
                levelScene[i][j] = serializedLevelSceneObservationZ[k];
                enemies[i][j] = serializedEnemiesObservationZ[k++];

                // Simulating merged observation!
                if (enemies[i][j] != 0)
                {
                    levelScene[i][j] = enemies[i][j];
                }
//                System.out.print(observation[i][j] + "\t");
            }
//            System.out.println();
        }
        this.marioFloatPos = marioFloatPos;
        this.enemiesFloatPos = enemiesFloatPos;
        this.marioState = marioState;

        marioStatus = marioState[0];
        marioMode = marioState[1];
        isMarioOnGround = marioState[2] == 1 ;
        isMarioAbleToJump = marioState[3] == 1;
        isMarioAbleToShoot = marioState[4] == 1;
        isMarioCarrying = marioState[5] == 1;
        getKillsTotal = marioState[6];
        getKillsByFire = marioState[7];
        getKillsByStomp = marioState[8];
        getKillsByShell = marioState[9];        
    }

    public boolean[] getAction()
    {
        return new boolean[0];  //To change body of implemented methods use File | Settings | File Templates.
    }

    public void reset()
    {
        action = new boolean[Environment.numberOfButtons];// Empty action
    }

    public boolean[] getAction(Environment observation)
    {
        return new boolean[Environment.numberOfButtons]; // Empty action
    }

    public AGENT_TYPE getType()
    {
        return Agent.AGENT_TYPE.AI;
    }

    public String getName() {        return name;    }

    public void setName(String Name) { this.name = Name;    }
}
