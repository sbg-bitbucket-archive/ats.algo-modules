package ats.algo.genericsupportfunctions;

/**
 * methods for measuring elapsed time
 * 
 * @author Geoff
 *
 */
public class StopWatch {

    private long startTime = 0;
    private long stopTime = 0;
    private boolean running = false;

    /**
     * starts the stopwatch
     */
    public void start() {
        this.startTime = System.currentTimeMillis();
        this.running = true;
    }

    /**
     * stops the clock
     */
    public void stop() {
        this.stopTime = System.currentTimeMillis();
        this.running = false;
    }


    /**
     * 
     * @return elapsed time between start and stop in milliseconds
     */
    public long getElapsedTimeMs() {
        long elapsed;
        if (running) {
            elapsed = (System.currentTimeMillis() - startTime);
        } else {
            elapsed = (stopTime - startTime);
        }
        return elapsed;
    }


    /**
     * 
     * @return elapsed time betwen start and stop in secs, rounded to 1 decimal place
     */
    public double getElapsedTimeSecs() {
        long elapsed = getElapsedTimeMs();
        return GCMath.round(((double) elapsed) / 1000.0, 1);
    }
}
