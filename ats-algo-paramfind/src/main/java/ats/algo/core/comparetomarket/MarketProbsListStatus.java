package ats.algo.core.comparetomarket;

import java.util.ArrayList;
import java.util.List;

public class MarketProbsListStatus {
    private boolean pricesOk;
    private List<String> errorMessages;
    private List<String> warningMessages;
    private List<MarketProbs> targetProbsList;

    public MarketProbsListStatus() {
        pricesOk = true;
        errorMessages = new ArrayList<String>();
        warningMessages = new ArrayList<String>();
        targetProbsList = new ArrayList<MarketProbs>();
    }

    public String toString() {
        String s = "MarketProbsListStats\n";
        s += String.format("pricesOk: %b\n", pricesOk);
        s += String.format("Error message count: %d\n", errorMessages.size());
        for (String msg : errorMessages)
            s += "  " + msg + "\n";
        s += String.format("Warning message count: %d\n", warningMessages.size());
        for (String msg : warningMessages)
            s += "  " + msg + "\n";
        for (MarketProbs t : targetProbsList) {
            s += t.toString();
        }
        return s;
    }

    public void addErrorMsg(String s) {
        errorMessages.add(s);
    }

    public void addWarningMsg(String s) {
        warningMessages.add(s);
    }

    public List<MarketProbs> getTargetProbsList() {
        return targetProbsList;
    }

    public void addTargetProbs(MarketProbs targetProbs) {
        targetProbsList.add(targetProbs);
    }

    public boolean isPricesOk() {
        return pricesOk;
    }

    public void setPricesOk(boolean pricesOk) {
        this.pricesOk = pricesOk;
    }

    public List<String> getErrorMessages() {
        return errorMessages;
    }

    public List<String> getWarningMessages() {
        return warningMessages;
    }

}
