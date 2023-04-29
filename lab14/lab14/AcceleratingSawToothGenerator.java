package lab14;
import edu.princeton.cs.algs4.StdAudio;
import lab14lib.Generator;

public class AcceleratingSawToothGenerator implements Generator {
    private int period;
    private int lastPeriod;
    private int state;
    private double factor;
    private int timeChangePeriod;

	public AcceleratingSawToothGenerator(int period, double factor) {
        state = 0;
        this.period = period;
        this.factor = factor;
        this.lastPeriod = 0;
        this.timeChangePeriod = this.period + this.lastPeriod;
    }
    @Override
    public double next() {
        state = (state + 1);
        double value = normalize(state);
        if (state == timeChangePeriod) {
            lastPeriod = period;
            period = (int) Math.round(lastPeriod * factor);
            timeChangePeriod = timeChangePeriod + period;
        }
        return value;
    }

    private double normalize (int state) {
        if( state == 300) {
            System.out.println(period);
            System.out.println(lastPeriod);
            System.out.println((state - lastPeriod) % period);
            System.out.println( (double) (period / 2));
            System.out.println((((state - lastPeriod) % period) - (double) (period / 2)) / ((double) period / 2));

        }

        return (((double)(state - (timeChangePeriod - period)) % period) - (((double) period) / 2.0)) / (((double) period) / 2.0);
    }
}
