package ats.algo.sport.outrights.server.api;

class AlertId {
    private int id;

    AlertId() {
        id = 0;
    }

    int nextId() {
        int nextId;
        synchronized (this) {
            id++;
            nextId = id;
        }
        return nextId;
    }
}
