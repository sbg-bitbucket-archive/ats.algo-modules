package ats.algo.core.triggerparamfind.tradingrules;

import ats.algo.core.comparetomarket.ParamFindResults;

public class ParamFindsCacheEntry {

    private long pfStartTime;
    private long pfEndTime;
    private boolean fatalPfError;
    private ParamFindResults paramfindResults;

    public ParamFindsCacheEntry(long pfStartTime) {
        super();
        this.pfStartTime = pfStartTime;
    }

    public long getPfEndTime() {
        return pfEndTime;
    }

    public void setPfEndTime(long pfEndTime) {
        this.pfEndTime = pfEndTime;
    }

    public boolean isFatalPfError() {
        return fatalPfError;
    }

    public void setFatalPfError(boolean fatalPfError) {
        this.fatalPfError = fatalPfError;
    }

    public ParamFindResults getParamfindResults() {
        return paramfindResults;
    }

    public void setParamfindResults(ParamFindResults paramfindResults) {
        this.paramfindResults = paramfindResults;
    }

    public long getPfStartTime() {
        return pfStartTime;
    }



}
