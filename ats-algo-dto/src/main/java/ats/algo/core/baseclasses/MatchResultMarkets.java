package ats.algo.core.baseclasses;

import java.util.ArrayList;
import java.util.List;
import ats.algo.core.markets.Market;
import ats.algo.core.markets.Markets;
import ats.algo.core.markets.ResultedMarket;
import ats.algo.core.markets.ResultedMarkets;
import ats.core.util.json.JsonUtil;

/**
 * base class for deriving sport specific
 * 
 * @author Geoff
 *
 */
public abstract class MatchResultMarkets {

    /**
     * 
     * @param matchState
     * @param markets
     * @return
     */

    public ResultedMarkets resultMarkets(Markets markets, MatchState previousMatchState, MatchState currentMatchState) {
        ResultedMarkets resultedMarkets = new ResultedMarkets();
        for (Market mkt : markets) {
            CheckMarketResultedOutcome checkResultedOutcome =
                            checkMarketResulted(mkt, previousMatchState, currentMatchState);
            /*
             * make sure when checkResultedOutcome is only for the whole market not some selections Logic need to be
             * changed in the future, Jin
             */

            if (checkResultedOutcome != null && checkResultedOutcome.isFullyResulted()) {
                String type = mkt.getType();
                String sequenceId = mkt.getSequenceId();
                List<String> halfWon;

                if (checkResultedOutcome.getWinningSelections().contains("halfWon")) {
                    halfWon = new ArrayList<String>();
                    halfWon.add(checkResultedOutcome.getWinningSelections().get(0).substring(7));
                }
                if (mkt.getType().contains("SPRD")) {
                    ResultedMarket resultedMarket = new ResultedMarket(type, mkt.getLineId(), mkt.getCategory(),
                                    sequenceId, checkResultedOutcome.getIsVoid(), mkt.getMarketDescription(),
                                    checkResultedOutcome.getWinningSelections(), checkResultedOutcome.getActualLineId(),
                                    checkResultedOutcome.getScores());
                    resultedMarkets.addMarket(resultedMarket);
                } else {

                    ResultedMarket resultedMarket = new ResultedMarket(type, mkt.getLineId(), mkt.getCategory(),
                                    sequenceId, checkResultedOutcome.getIsVoid(), mkt.getMarketDescription(),
                                    checkResultedOutcome.getWinningSelections(),
                                    checkResultedOutcome.getActualLineId());
                    resultedMarkets.addMarket(resultedMarket);
                }


            } else if (checkResultedOutcome != null && !checkResultedOutcome.isFullyResulted()) {
                /*
                 * Jin add check partially resulting here
                 */

                String type = mkt.getType();
                String sequenceId = mkt.getSequenceId();
                /*
                 * Partially resulting constructor
                 */
                ResultedMarket resultedMarket = new ResultedMarket(type, mkt.getLineId(), mkt.getCategory(), sequenceId,
                                checkResultedOutcome.getIsVoid(), mkt.getMarketDescription(),
                                checkResultedOutcome.getWinningSelections(), checkResultedOutcome.getActualLineId(),
                                checkResultedOutcome.getLosingSelections(), checkResultedOutcome.getVoidedSelections(),
                                false);

                resultedMarkets.addMarket(resultedMarket);

            }



        }
        return resultedMarkets;
    }

    public ResultedMarkets resultMarkets(Markets markets, MatchState previousMatchState, MatchState currentMatchState,
                    List<String> suspendList) {
        ResultedMarkets resultedMarkets = new ResultedMarkets();
        boolean listExist = suspendList != null;
        for (Market mkt : markets) {
            if (listExist) {
                if (suspendList.contains(mkt.getType()))
                    mkt.setDoNotResultThisMarket(true);
            }

            CheckMarketResultedOutcome checkResultedOutcome =
                            checkMarketResulted(mkt, previousMatchState, currentMatchState);
            /*
             * make sure when checkResultedOutcome is only for the whole market not some selections Logic need to be
             * changed in the future, Jin
             */

            if (checkResultedOutcome != null && checkResultedOutcome.isFullyResulted()) {
                String type = mkt.getType();
                String sequenceId = mkt.getSequenceId();
                List<String> halfWon;
                if (checkResultedOutcome.getWinningSelections().contains("halfWon")) {
                    halfWon = new ArrayList<String>();
                    halfWon.add(checkResultedOutcome.getWinningSelections().get(0).substring(7));
                }

                if (mkt.getType().contains("SPRD")) {
                    ResultedMarket resultedMarket = new ResultedMarket(type, mkt.getLineId(), mkt.getCategory(),
                                    sequenceId, checkResultedOutcome.getIsVoid(), mkt.getMarketDescription(),
                                    checkResultedOutcome.getWinningSelections(), checkResultedOutcome.getActualLineId(),
                                    checkResultedOutcome.getScores());
                    resultedMarkets.addMarket(resultedMarket);
                } else {

                    ResultedMarket resultedMarket = new ResultedMarket(type, mkt.getLineId(), mkt.getCategory(),
                                    sequenceId, checkResultedOutcome.getIsVoid(), mkt.getMarketDescription(),
                                    checkResultedOutcome.getWinningSelections(),
                                    checkResultedOutcome.getActualLineId());
                    resultedMarkets.addMarket(resultedMarket);
                }
            } else if (checkResultedOutcome != null && !checkResultedOutcome.isFullyResulted()) {
                /*
                 * Jin add check partially resulting here
                 */

                String type = mkt.getType();
                String sequenceId = mkt.getSequenceId();
                /*
                 * Partially resulting constructor
                 */
                ResultedMarket resultedMarket = new ResultedMarket(type, mkt.getLineId(), mkt.getCategory(), sequenceId,
                                checkResultedOutcome.getIsVoid(), mkt.getMarketDescription(),
                                checkResultedOutcome.getWinningSelections(), checkResultedOutcome.getActualLineId(),
                                checkResultedOutcome.getLosingSelections(), checkResultedOutcome.getVoidedSelections(),
                                false);

                resultedMarkets.addMarket(resultedMarket);

            }



        }
        return resultedMarkets;
    }

    /**
     * container to hold results of the checkResulted method
     * 
     * @author Geoff
     *
     */
    protected class CheckMarketResultedOutcome {
        private Boolean isVoid;
        private List<String> winningSelections;
        private List<String> losingSelections;
        private List<String> voidedSelections;
        private boolean isFullyResulted = true;
        private int actualLineId;
        private List<String> scores;
        // /*
        // * Jin added for immediate selection resulting
        // * */
        // private Map<String,String> resultedSelections;

        /**
         * 
         * @param winningSelection - the winning selection, or null if the market is to be voided
         * @param actualLineId
         */
        public CheckMarketResultedOutcome(String winningSelection, int actualLineId) {
            this.winningSelections = new ArrayList<String>(1);
            this.actualLineId = actualLineId;
            if (winningSelection == null) {
                this.isVoid = true;
                winningSelections.add("Void");
                this.isFullyResulted = true;
            } else if (winningSelection == "selection only") {
                this.losingSelections = new ArrayList<String>();
                this.voidedSelections = new ArrayList<String>();
                this.isFullyResulted = false;
                this.isVoid = false;
            } else {
                this.winningSelections.add(winningSelection);
                this.isVoid = false;
                this.isFullyResulted = true;
            }
        }

        /**
         * 
         * @param winningSelection - the winning selection, or null if the market is to be voided
         * @param actualLineId
         */
        public CheckMarketResultedOutcome(String winningSelection, int actualLineId, List<String> scores) {
            this.winningSelections = new ArrayList<String>(1);
            this.actualLineId = actualLineId;
            this.scores = new ArrayList<>(scores.size());
            this.scores = scores;
            if (winningSelection == null) {
                this.isVoid = true;
                winningSelections.add("Void");
                this.isFullyResulted = true;
            } else if (winningSelection == "selection only") {
                this.losingSelections = new ArrayList<String>();
                this.voidedSelections = new ArrayList<String>();
                this.isFullyResulted = false;
                this.isVoid = false;
            } else {
                this.winningSelections.add(winningSelection);
                this.isVoid = false;
                this.isFullyResulted = true;
            }
        }

        /**
         * 
         * @param winningSelections - list of the winning selections, or null if market is to be voided
         * @param actualLineId
         */
        public CheckMarketResultedOutcome(List<String> winningSelections, int actualLineId) {
            this.winningSelections = winningSelections;
            this.actualLineId = actualLineId;
            this.isVoid = winningSelections == null;;
        }

        /**
         * @param winningSelection - the winning selection, or null if the market is to be voided
         * @param winningSelection
         */
        public CheckMarketResultedOutcome(String winningSelection) {
            this(winningSelection, -9999);
        }

        /*
         * Result selections, where the resulted out come is null.
         * 
         * Jin
         */
        /**
         * @param selections - the selection should be resulted
         * @param outcome - result of this selection
         */
        public CheckMarketResultedOutcome(List<String> partialLosingSelections, List<String> partialVoidedSelections,
                        boolean fullyResulted) {
            this("selection only", -9999);
            this.setWinningSelections(null);
            this.setLosingSelections(partialLosingSelections);
            this.setVoidedSelections(partialVoidedSelections);
            this.isFullyResulted = fullyResulted;
        }

        public CheckMarketResultedOutcome(List<String> winningSelections) {
            this(winningSelections, -9999);
        }

        public CheckMarketResultedOutcome() {
            this.isVoid = true;
            this.winningSelections = new ArrayList<String>(1);
            this.winningSelections.add("VOID");
            this.actualLineId = -9999;
            this.isFullyResulted = true;
        }


        public List<String> getWinningSelections() {
            return winningSelections;
        }

        public List<String> getLosingSelections() {
            return losingSelections;
        }

        public void setLosingSelections(List<String> losingSelections) {
            this.losingSelections = losingSelections;
        }



        public List<String> getVoidedSelections() {
            return voidedSelections;
        }

        public void setVoidedSelections(List<String> partialVoidedSelections) {
            this.voidedSelections = partialVoidedSelections;
        }

        public boolean isFullyResulted() {
            return isFullyResulted;
        }

        public void setFullyResulted(boolean isFullyResulted) {
            this.isFullyResulted = isFullyResulted;
        }

        public void setWinningSelections(List<String> winningSelections) {
            this.winningSelections = winningSelections;
        }

        public int getActualLineId() {
            return actualLineId;
        }

        public Boolean getIsVoid() {
            return isVoid;
        }

        public List<String> getScores() {
            return scores;
        }

        public void setScores(List<String> scores) {
            this.scores = scores;
        }


    }


    @Override
    public String toString() {
        return JsonUtil.marshalJson(this);
    }


    protected abstract CheckMarketResultedOutcome checkMarketResulted(Market market, MatchState previousMatchState,
                    MatchState currentMatchState);

}
