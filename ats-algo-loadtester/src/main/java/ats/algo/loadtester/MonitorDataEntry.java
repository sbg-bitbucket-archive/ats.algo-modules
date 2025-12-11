package ats.algo.loadtester;

import java.io.IOException;


class MonitorDataEntry implements Runnable {

    private volatile boolean terminate;


    @Override
    public void run() {
        terminate = false;
        try {
            System.in.read();
        } catch (IOException e) {
        }
        terminate = true;
    }

    public boolean finishRequested() {
        return terminate;
    }

}
