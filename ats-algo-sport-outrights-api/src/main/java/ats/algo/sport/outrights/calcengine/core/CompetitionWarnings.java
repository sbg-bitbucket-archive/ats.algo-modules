package ats.algo.sport.outrights.calcengine.core;

import java.util.ArrayList;
import java.util.List;

import ats.core.util.json.JsonUtil;

public class CompetitionWarnings {

    protected boolean stateOk;
    protected List<String> warningMessages;

    public CompetitionWarnings() {
        super();
        warningMessages = new ArrayList<>();
    }

    public boolean isStateOk() {
        return stateOk;
    }

    public void setStateOk(boolean stateOk) {
        this.stateOk = stateOk;
    }

    public List<String> getWarningMessages() {
        return warningMessages;
    }

    public void setWarningMessages(List<String> warningMessages) {
        this.warningMessages = warningMessages;
    }

    public void addWarningMessage(String warningMessage) {
        warningMessages.add(warningMessage);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (stateOk ? 1231 : 1237);
        result = prime * result + ((warningMessages == null) ? 0 : warningMessages.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        CompetitionWarnings other = (CompetitionWarnings) obj;
        if (stateOk != other.stateOk)
            return false;
        if (warningMessages == null) {
            if (other.warningMessages != null)
                return false;
        } else if (!warningMessages.equals(other.warningMessages))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return JsonUtil.marshalJson(this);
    }

}
