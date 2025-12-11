package ats.algo.matchrunner;

import ats.algo.core.baseclasses.MatchFormat;
import ats.algo.core.baseclasses.MatchIncident;
import ats.algo.core.baseclasses.MatchParams;

public interface MatchRunnable {

    public void handleMarketRowSelected(String fullKey, String selKey);

    public void handleNewPricesRowRequest(String fullKey);

    public void setButtonClickAble(boolean buttonClickAble);

    public void handleSelectionRowSelected(String fullKey, String sequenceId, String selKey);

    public void handleRemovePricesRowRequest(String fullKey);

    public void handleMatchStateRequest();

    public void handleMatchParamsChanged(MatchParams matchParams);

    public void handleFindParamsButtonPressed();

    public void handleStartParamFind(boolean detailedLogReqd);

    public void setEventTier(int eventTier);

    public int getEventTier();

    public void handleExportCollectedMarkets();

    public void handleRevertToEarlierState(String revertToEarlierStateForRequestId);

    public void handleMatchIncident(MatchIncident matchIncident);

    public void handleNewMatch(MatchFormat matchFormat);
}
