package com.zvoykish.marioai.input;

import ch.idsia.benchmark.mario.environments.Environment;

/**
 * Created by Zvika on 11/10/14.
 */
public class MarioInputAdapter implements MarioInput {
    private Environment environment;

    public MarioInputAdapter(Environment environment) {
        this.environment = environment;
    }

    @Override
    public float getMarioX() {
        return environment.getMarioFloatPos()[0];
    }

    @Override
    public float getMarioY() {
        return environment.getMarioFloatPos()[1];
    }

    @Override
    public MarioMode getMarioMode() {
        return MarioMode.fromInt(environment.getMarioMode());
    }

    @Override
    public boolean is(MarioFeature feature) {
        switch (feature) {
            case CAN_JUMP:
                return environment.isMarioAbleToJump();
            case CAN_SHOOT:
                return environment.isMarioAbleToShoot();
            case ON_GROUND:
                return environment.isMarioOnGround();
            case CARRYING:
                return environment.isMarioCarrying();
            default:
                throw new IllegalArgumentException("Unsupported mario feature: " + feature);
        }
    }

    @Override
    public String toString() {
        return "Mario (" + getMarioMode() + ") @ (" + getMarioX() + ',' + getMarioY() + ')';
    }
}
