package ch.idsia.agents.controllers;

import ch.idsia.benchmark.mario.environments.Environment;
import com.zvoykish.marioai.MarioAIAdapter;
import com.zvoykish.marioai.input.MarioGameSummary;
import com.zvoykish.marioai.input.MarioInputAdapter;
import com.zvoykish.marioai.output.MarioAdapterActions;

/**
 * Created by Zvika on 11/10/14.
 */
public class ExternalMarioAgent extends BasicMarioAIAgent {
    private MarioAIAdapter adapter;
    private Environment environment;

    public ExternalMarioAgent(String adapterName) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        super(adapterName);
        Class<?> clazz = Class.forName(adapterName);
        if (!MarioAIAdapter.class.isAssignableFrom(clazz)) {
            throw new IllegalArgumentException("Invalid Mario AI adapter: " + adapterName);
        }
        this.adapter = (MarioAIAdapter) clazz.newInstance();
    }

    @Override
    public void integrateObservation(Environment environment) {
        super.integrateObservation(environment);
        this.environment = environment;
    }

    @Override
    public boolean[] getAction() {
        MarioAdapterActions actions = adapter.getActions(new MarioInputAdapter(environment));
        return actions.toBooleanArray();
    }

    public void onGameEnd(MarioGameSummary summary) {
        //noinspection StringBufferReplaceableByString
        StringBuilder sb = new StringBuilder()
                .append("External mario agent summary\n")
                .append("----------------------------\n")
                .append("Mario wins? ").append(summary.isWon()).append('\n')
                .append("Mode: ").append(summary.getMarioMode()).append('\n')
                .append("Cells passed: %").append(100 * ((double) summary.getPassedCells() / summary.getTotalCells())).append('\n')
                .append("Coins: ").append(summary.getCoinsGained()).append('\n')
                .append("Creatures killed: %").append(summary.getPercentageOfCreaturesKilled()).append('\n')
                .append("Time passed: ").append(summary.getTimePassed()).append('\n')
                .append("Time left: ").append(summary.getTimeLeft()).append('\n')
                .append("----------------------------");
        System.out.println(sb.toString());
        adapter.onGameEnd(summary);
    }
}
