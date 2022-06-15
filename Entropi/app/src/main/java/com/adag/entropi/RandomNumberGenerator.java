package com.adag.entropi;

public class RandomNumberGenerator {

    int encoder_seed, lin_param, indep_param, internal_module;
    int state;

    public RandomNumberGenerator(int seed, int module){
        this.encoder_seed = seed;
        this.internal_module = module;

        this.state = encoder_seed % internal_module;
    }

    public void setParamLinear(int linear){
        this.lin_param = linear;
    }

    public void setParamIndep(int independent){
        this.indep_param = independent;
    }

    public int yield(int max_value){
        state = (lin_param * state + indep_param) % internal_module;
        return state % max_value;
    }
}
