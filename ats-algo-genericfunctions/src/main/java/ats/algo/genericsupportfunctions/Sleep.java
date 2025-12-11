package ats.algo.genericsupportfunctions;

public class Sleep {

    public static void sleep(int nSecs) {
        for (int i = 0; i < nSecs; i++) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.printf("Waiting... %d secs (of %d)\n", i + 1, nSecs);
        }
    }

    public static void sleepMs(int mSecs) {
        try {
            Thread.sleep(mSecs);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


}
