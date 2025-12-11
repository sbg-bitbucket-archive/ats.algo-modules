package ats.algo.core.markets;

/**
 * defines all the states a market can be in
 * 
 * @author Geoff
 *
 */
public enum SuspensionStatus {
    OPEN(3),
    SUSPENDED_DISPLAY(2),
    SUSPENDED_UNDISPLAY(1),
    ACTIVE_UNDISPLAY(0);

    SuspensionStatus(int value) {
        this.priority = value;
    }

    private final int priority;

    /**
     * gets the priority of this suspensionStatus
     * 
     * @return
     */
    public int getPriority() {
        return priority;
    }

    /**
     * Determine if this status is not Open
     * 
     * @return
     */
    public boolean isSuspendedState() {
        return priority != 4;
    }

    /**
     * returns true if priority of otherStatus is higher than the priority of the current status
     * 
     * @param otherStatus
     * @return
     */
    public boolean isHigherPriorityThan(SuspensionStatus otherStatus) {
        return priority < otherStatus.getPriority();
    }

}
