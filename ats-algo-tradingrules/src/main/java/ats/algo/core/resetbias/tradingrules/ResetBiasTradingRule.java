package ats.algo.core.resetbias.tradingrules;

import java.io.Serializable;

import ats.algo.core.baseclasses.MatchParams;
import ats.algo.core.baseclasses.MatchState;
import ats.algo.core.comparetomarket.ParamFindResultsStatus;
import ats.algo.core.tradingrules.AbstractTradingRule;
import ats.algo.core.tradingrules.ParamFindingRuleResult;
import ats.algo.core.tradingrules.TradingRuleType;

public class ResetBiasTradingRule extends AbstractTradingRule implements Serializable {

    private static final long serialVersionUID = 1L;

    private boolean resetBiasFollowingFirstInPlayParamFind;

    public ResetBiasTradingRule() {
        super(TradingRuleType.RESET_BIAS_FOLLOWING_PARAM_FIND,
                        "Param bias following first successful in play param find", null);
    }

    public ResetBiasTradingRule(boolean b) {
        this();
        resetBiasFollowingFirstInPlayParamFind = b;
    }

    public boolean isResetBiasFollowingFirstInPlayParamFind() {
        return resetBiasFollowingFirstInPlayParamFind;
    }

    public void setResetBiasFollowingFirstInPlayParamFind(boolean resetBiasFollowingFirstInPlayParamFind) {
        this.resetBiasFollowingFirstInPlayParamFind = resetBiasFollowingFirstInPlayParamFind;
    }

    public ParamFindingRuleResult applyResetBiasRuleToMatchParams(MatchState matchState, MatchParams matchParams) {
        ParamFindingRuleResult result = new ParamFindingRuleResult();
        if (resetBiasFollowingFirstInPlayParamFind && matchState.getNoSuccessfulInPlayParamFindsExecuted() == 1)
            if (matchParams.resetBias()) {
                result.setResultColour(ParamFindResultsStatus.AMBER);
                result.setResultDescription("PARAM BIAS RESET");
            }
        return result;
    }


}
