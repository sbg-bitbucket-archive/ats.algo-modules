package ats.algo.sport.testcricket;

import java.util.LinkedHashMap;
import java.util.Map;

import ats.algo.core.baseclasses.MatchFormat;
import ats.algo.core.baseclasses.MatchFormatOptions;
import ats.algo.core.common.SupportedSportType;
import ats.algo.core.dto.util.DtoJsonUtil;

public class TestCricketMatchFormat extends MatchFormat {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public int nInningsinMatch;
    public int maxOverinInning;
    public int nBatmeninMatch;
    public int nBallsinOver;
    public int nDayinMatch;
    public MatchForm matchForm;

    public enum MatchForm {
        Test
    }

    public TestCricketMatchFormat() {
        super(SupportedSportType.TESTCRICKET);
        /*
         * set sensible defaults;
         */
        nInningsinMatch = 2;
        nBatmeninMatch = 11;
        nBallsinOver = 6;
        maxOverinInning = 1000;
        nDayinMatch = 5;
        matchForm = MatchForm.Test;

    }

    @Override
    public void applyFormat(Map<String, Object> format) {
        nInningsinMatch = DtoJsonUtil.toInt(format.get("nInningsinMatch"));
        nBatmeninMatch = DtoJsonUtil.toInt(format.get("nBatmeninMatch"));
        nBallsinOver = DtoJsonUtil.toInt(format.get("nBallsinOver"));
        matchForm = MatchForm.valueOf(DtoJsonUtil.toString(format.get("matchForm")));
        maxOverinInning = DtoJsonUtil.toInt(format.get("maxOverinInning"));
        nDayinMatch = DtoJsonUtil.toInt(format.get("nDayinMatch"));
    }

    @Override
    public MatchFormatOptions matchFormatOptions() {
        return new TestCricketMatchFormatOptions();
    }

    public int getnDayinMatch() {
        return nDayinMatch;
    }

    public void setnDayinMatch(int nDayinMatch) {
        this.nDayinMatch = nDayinMatch;
    }

    public int getMaxOverinInning() {
        return maxOverinInning;
    }

    public void setMaxOverinInning(int maxOverinInning) {
        this.maxOverinInning = maxOverinInning;
    }

    public void setnBallsinOver(int nBallsinOver) {
        this.nBallsinOver = nBallsinOver;
    }

    public int getnBallsinOver() {
        return nBallsinOver;
    }

    public int getnInningsinMatch() {
        return nInningsinMatch;
    }

    public void setnInningsinMatch(int nInningsinMatch) {
        this.nInningsinMatch = nInningsinMatch;
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
    private static final String nDayinMatchKey = "nDayinMatch";

    @Override
    public LinkedHashMap<String, String> getAsMap() {
        LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
        map.put(matchFormKey, matchForm.toString());
        map.put(nDayinMatchKey, String.valueOf(nDayinMatch));
        if (isUseMarginChart())
            map.put(getMarginchartnamekey(), String.valueOf(getMarginChart()));
        return map;
    }

    @Override
    public String setFromMap(Map<String, String> map) {
        String errMsg = "Unknown error";
        String newValue;
        int nDayinMatch;
        MatchForm matchForm;
        int marginChart;

        try {
            newValue = map.get(matchFormKey);
            matchForm = MatchForm.Test;
            errMsg = String.format("Invalid value: %s", newValue);
            newValue = map.get(nDayinMatchKey);
            if (newValue.equals("4") || newValue.equals("5"))
                nDayinMatch = Integer.valueOf(newValue);
            else
                throw new Exception();
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
        this.nDayinMatch = nDayinMatch;
        return null;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((matchForm == null) ? 0 : matchForm.hashCode());
        result = prime * result + maxOverinInning;
        result = prime * result + nBallsinOver;
        result = prime * result + nBatmeninMatch;
        result = prime * result + nInningsinMatch;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        TestCricketMatchFormat other = (TestCricketMatchFormat) obj;
        if (matchForm != other.matchForm)
            return false;
        if (maxOverinInning != other.maxOverinInning)
            return false;
        if (nBallsinOver != other.nBallsinOver)
            return false;
        if (nBatmeninMatch != other.nBatmeninMatch)
            return false;
        if (nInningsinMatch != other.nInningsinMatch)
            return false;
        return true;
    }

    public TestCricketMatchFormat(Map<String, Object> format) {
        super(SupportedSportType.TESTCRICKET);
        applyFormat(format);
    }

    @Override
    public MatchFormat generateDefaultMatchFormat() {
        return new TestCricketMatchFormat();
    }
}
