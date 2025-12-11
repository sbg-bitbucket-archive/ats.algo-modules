package ats.algo.core.tradingrules;

import ats.algo.core.comparetomarket.ParamFindResultsStatus;

/**
 * container to hold the result of a Param finding trading rule
 * 
 * @author Geoff
 *
 */
public class ParamFindingRuleResult {
    private ParamFindResultsStatus resultColour;
    private String resultDescription;
    private boolean proceedWithParamFind;


    public ParamFindingRuleResult() {
        resultColour = ParamFindResultsStatus.GREEN;
        resultDescription = null;
        proceedWithParamFind = true;
    }


    public boolean isProceedWithParamFind() {
        return proceedWithParamFind;
    }

    public void setProceedWithParamFind(boolean proceedWithParamFind) {
        this.proceedWithParamFind = proceedWithParamFind;
    }

    public ParamFindResultsStatus getResultColour() {
        return resultColour;
    }

    public void setResultColour(ParamFindResultsStatus resultColour) {
        this.resultColour = resultColour;
    }

    public String getResultDescription() {
        return resultDescription;
    }

    public void setResultDescription(String resultDescription) {
        this.resultDescription = resultDescription;
    }

    /**
     * merge two sets of results, setting resultColour and proceed with ParamFind to the highest level of the two
     * 
     * @param result2
     */
    public void merge(ParamFindingRuleResult result2) {
        this.proceedWithParamFind &= result2.proceedWithParamFind;
        switch (result2.getResultColour()) {
            case GREEN:
            case BLACK:
            case YELLOW:
                break;
            case AMBER:
                if (resultColour == ParamFindResultsStatus.GREEN)
                    resultColour = ParamFindResultsStatus.AMBER;
                break;
            case BLUE:
                if (resultColour == ParamFindResultsStatus.GREEN || resultColour == ParamFindResultsStatus.AMBER)
                    resultColour = ParamFindResultsStatus.BLUE;
                break;
            case RED:
                resultColour = ParamFindResultsStatus.RED;
                break;

        }

        String string = result2.getResultDescription();
        if (string != null) {
            if (this.resultDescription != null)
                this.resultDescription += ", " + string;
            else
                this.resultDescription = string;
        }
    }


    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((resultColour == null) ? 0 : resultColour.hashCode());
        result = prime * result + ((resultDescription == null) ? 0 : resultDescription.hashCode());
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
        ParamFindingRuleResult other = (ParamFindingRuleResult) obj;
        if (resultColour != other.resultColour)
            return false;
        if (resultDescription == null) {
            if (other.resultDescription != null)
                return false;
        } else if (!resultDescription.equals(other.resultDescription))
            return false;
        return true;
    }


    @Override
    public String toString() {
        return "ParamFindingRuleResult [resultColour=" + resultColour + ", resultDescription=" + resultDescription
                        + "]";
    }

}
