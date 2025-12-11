package ats.algo.sport.cricket;

import java.util.LinkedHashMap;
import java.util.Map;

import ats.algo.core.baseclasses.MatchFormat;
import ats.algo.core.baseclasses.MatchFormatOptions;
import ats.algo.core.common.SupportedSportType;
import ats.algo.core.dto.util.DtoJsonUtil;

public class CricketMatchFormat extends MatchFormat {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public int nOversinMatch;
    public int nBatmeninMatch;
    public int nBallsinOver;
    public MatchForm matchForm;

    public enum MatchForm {
        Twenty20,
        OneDayInternational
    }

    public CricketMatchFormat() {
        super(SupportedSportType.CRICKET);
        /*
         * set sensible defaults;
         */
        nOversinMatch = 20;
        nBatmeninMatch = 11;
        nBallsinOver = 6;
        matchForm = MatchForm.Twenty20;

    }

    @Override
    public void applyFormat(Map<String, Object> format) {
        nOversinMatch = DtoJsonUtil.toInt(format.get("nOversinMatch"));
        nBatmeninMatch = DtoJsonUtil.toInt(format.get("nBatmeninMatch"));
        nBallsinOver = DtoJsonUtil.toInt(format.get("nBallsinOver"));
        matchForm = MatchForm.valueOf(DtoJsonUtil.toString(format.get("matchForm")));
    }

    @Override
    public MatchFormatOptions matchFormatOptions() {
        return new CricketMatchFormatOptions();
    }

    public CricketMatchFormat(Map<String, Object> format) {
        super(SupportedSportType.CRICKET);
        applyFormat(format);

    }

    public void setnBallsinOver(int nBallsinOver) {
        this.nBallsinOver = nBallsinOver;
    }

    public int getnBallsinOver() {
        return nBallsinOver;
    }

    public void setnOversinMatch(int nOversinMatch) {
        this.nOversinMatch = nOversinMatch;
    }

    public int getnOversinMatch() {
        return nOversinMatch;
    }

    public void setMatchForm(MatchForm matchForm) {
        this.matchForm = matchForm;
    }

    public MatchForm getMatchForm() {
        return matchForm;
    }

    public int getnBatmeninMatch() {
        return nBatmeninMatch;
    }

    public void setnBatmeninMatch(int nBatmeninMatch) {
        this.nBatmeninMatch = nBatmeninMatch;
    }

    private static final String matchFormKey = "matchForm";
    private static final String nOversinMatchKey = "nOversinMatch";
    // private static final String byTeamKey = "by Team";

    @Override
    public LinkedHashMap<String, String> getAsMap() {
        LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
        map.put(matchFormKey, matchForm.toString());
        map.put(nOversinMatchKey, String.format("%d", nOversinMatch));
        if (isUseMarginChart())
            map.put(getMarginchartnamekey(), String.valueOf(getMarginChart()));
        return map;
    }

    @Override
    public String setFromMap(Map<String, String> map) {
        String errMsg = "Unknown error";
        String newValue;
        MatchForm matchForm;
        int nOversInMatch;
        int marginChart;
        try {
            newValue = map.get(nOversinMatchKey);
            errMsg = String.format("Invalid value: %s", newValue);
            nOversInMatch = Integer.parseInt(newValue);
            if (nOversInMatch == 20 || nOversInMatch == 50) {
            } else
                throw new Exception();
            newValue = map.get(matchFormKey);
            switch (newValue.toUpperCase()) {
                case "TWENTY20":
                    matchForm = MatchForm.Twenty20;
                    break;
                case "ONEDAY":
                    matchForm = MatchForm.OneDayInternational;
                    break;
                default:
                    throw new Exception();
            }
            errMsg = String.format("Invalid value: %s", newValue);

            /*
             * margin chart
             */

            newValue = map.get(getMarginchartnamekey());
            errMsg = String.format("Invalid value: %s.  Allowed integer values 1 to 10 ", newValue);
            marginChart = Integer.parseInt(newValue);
            if ((marginChart > 10) || (marginChart < 1))
                throw new Exception();
        } catch (Exception e) {
            return errMsg;
        }
        setMarginChart(marginChart);
        this.matchForm = matchForm;
        this.nOversinMatch = nOversInMatch;
        return null;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((matchForm == null) ? 0 : matchForm.hashCode());
        result = prime * result + nBallsinOver;
        result = prime * result + nBatmeninMatch;
        result = prime * result + nOversinMatch;
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
        CricketMatchFormat other = (CricketMatchFormat) obj;
        if (matchForm != other.matchForm)
            return false;
        if (nBallsinOver != other.nBallsinOver)
            return false;
        if (nBatmeninMatch != other.nBatmeninMatch)
            return false;
        if (nOversinMatch != other.nOversinMatch)
            return false;
        return true;
    }

    @Override
    public MatchFormat generateDefaultMatchFormat() {
        return new CricketMatchFormat();
    }

}
