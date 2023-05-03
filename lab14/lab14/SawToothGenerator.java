package lab14;

import edu.princeton.cs.algs4.StdAudio;
import lab14lib.Generator;

public class SawToothGenerator implements Generator {
    private int period;
    private int state;

	public SawToothGenerator(int period) {
        state = 0;
        this.period = period;
    }
    @Override
    public double next() {
        state = (state + 1);
        return normalize(state);
    }

    private double normalize (int state) {
        return (double) ((state % period) - period / 2)/ ((double) period / 2);
    }
}