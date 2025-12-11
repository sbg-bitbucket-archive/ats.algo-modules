package ats.algo.core.markets;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.google.common.collect.Sets;

import ats.core.util.json.JsonUtil;

/**
 * contains the status information for this market which may be set by the application of trading rules
 * 
 * @author Geoff
 *
 */
public class DerivedMarketsInfo implements Serializable {

    private static final long serialVersionUID = 1L;


    private String staticLineList;
    private DataToGenerateStaticMarkets dataToGenerateStaticMarkets;

    /*
     * these properties only relevant to HCAP and OVUN markets
     */
    private String linesToDisplayReason;
    private String linesToDisplayRuleName;
    private int noLinesToDisplayEachSideOfBalancedLine;
    private int displayLineSpacing;
    private int normalLineInverseGap; // default value 1 – can be 1, 2 or 4
    private List<DerivedMarketSpec> derivedMarketSpecs;
    private Set<String> rulesThatExecuted;


    public DerivedMarketsInfo() {
        reset();
    }


    public String getStaticLineList() {
        return staticLineList;
    }



    public void setStaticLineList(String staticLineList) {
        this.staticLineList = staticLineList;
    }



    public DataToGenerateStaticMarkets getDataToGenerateStaticMarkets() {
        return dataToGenerateStaticMarkets;
    }



    public void setDataToGenerateStaticMarkets(DataToGenerateStaticMarkets dataToGenerateStaticMarkets) {
        this.dataToGenerateStaticMarkets = dataToGenerateStaticMarkets;
    }



    /**
     * gets a descriptive string that explains why the linbestoDisplay property is set as it is
     * 
     * @return
     */
    public String getLinesToDisplayReason() {
        return linesToDisplayReason;
    }

    /**
     * sets a descriptive string that explains why the linestoDisplay property is set as it is
     * 
     * @return
     */
    public void setLinesToDisplayReason(String linesToDisplayReason) {
        this.linesToDisplayReason = linesToDisplayReason;
    }

    /**
     * gets the name of the rule that set the linesToDisplay property
     * 
     * @return
     */
    public String getLinesToDisplayRuleName() {
        return linesToDisplayRuleName;
    }

    /**
     * sets the name of the rule that set the linesToDisplay property
     * 
     * @param linesToDisplayRuleName
     */
    public void setLinesToDisplayRuleName(String linesToDisplayRuleName) {
        this.linesToDisplayRuleName = linesToDisplayRuleName;
    }

    /**
     * gets the no of lines to display each side of the balanced line
     * 
     * @return
     */
    public int getNoLinesToDisplayEachSideOfBalancedLine() {
        return noLinesToDisplayEachSideOfBalancedLine;
    }

    /**
     * sets the no of lines to display each side of the balanced line
     * 
     * @param noLinesToDisplay
     */
    public void setNoLinesToDisplayEachSideOfBalancedLine(int noLinesToDisplay) {
        this.noLinesToDisplayEachSideOfBalancedLine = noLinesToDisplay;
    }

    /**
     * gets the display line spacing - i.e. the gap between each line that is displayed. So e.g. if displayLineSpacing
     * is 2 and #lines to display is 3 and balanced line is 21.5 then lines19.5,21.5,23.5 will be displayed
     * 
     * @return
     */
    public int getDisplayLineSpacing() {
        return displayLineSpacing;
    }

    /**
     * sets the display line spacing - i.e. the gap between each line that is displayed. So e.g. take case where #lines
     * to display is 3 and balanced line is 21.5. If displayLineSpacing is 2 and normalLineInverseGap is 1 then
     * lines19.5, 21.5, 23.5 will be displayed. If displayLineSpacing is 1 and normalInverseGap is 2 then lines 21, 21.5
     * and 22.5 would be displayed
     * 
     * @param displayLineSpacing
     */
    public void setDisplayLineSpacing(int displayLineSpacing) {
        this.displayLineSpacing = displayLineSpacing;
    }

    /**
     * sets the line spacing. May be set to whole line(1), half line(2) or quarter line (4).So e.g. take case where
     * #lines to display is 3 and balanced line is 21.5. If displayLineSpacing is 2 and normalLineInverseGap is 1 then
     * lines19.5, 21.5, 23.5 will be displayed. If displayLineSpacing is 1 and normalInverseGap is 2 then lines 21, 21.5
     * and 22.5 would be displayed
     * 
     * @return
     */
    public int getNormalLineInverseGap() {
        return normalLineInverseGap;
    }

    /**
     * gets the line spacing. May be set to whole line(1), half line(2) or quarter line (4).So e.g. take case where
     * #lines to display is 3 and balanced line is 21.5. If displayLineSpacing is 2 and normalLineInverseGap is 1 then
     * lines19.5, 21.5, 23.5 will be displayed. If displayLineSpacing is 1 and normalInverseGap is 2 then lines 21, 21.5
     * and 22.5 would be displayed
     * 
     * @param normalLineInverseGap
     */
    public void setNormalLineInverseGap(int normalLineInverseGap) {
        this.normalLineInverseGap = normalLineInverseGap;
    }



    public List<DerivedMarketSpec> getDerivedMarketSpecs() {
        return derivedMarketSpecs;
    }

    public void setDerivedMarketSpecs(List<DerivedMarketSpec> derivedMarketSpecs) {
        this.derivedMarketSpecs = derivedMarketSpecs;
    }

    /**
     * reset everything to default values
     */
    public void reset() {

        noLinesToDisplayEachSideOfBalancedLine = 0;
        displayLineSpacing = 1;
        linesToDisplayRuleName = "Default";
        linesToDisplayReason = "Default";
        normalLineInverseGap = 1; // default value 1 – can be 1, 2 or 4
        derivedMarketSpecs = new ArrayList<DerivedMarketSpec>();

        staticLineList = null;
        dataToGenerateStaticMarkets = new DataToGenerateStaticMarkets();
        rulesThatExecuted = Sets.newLinkedHashSet();
    }


    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((dataToGenerateStaticMarkets == null) ? 0 : dataToGenerateStaticMarkets.hashCode());
        result = prime * result + ((derivedMarketSpecs == null) ? 0 : derivedMarketSpecs.hashCode());
        result = prime * result + displayLineSpacing;
        result = prime * result + ((linesToDisplayReason == null) ? 0 : linesToDisplayReason.hashCode());
        result = prime * result + ((linesToDisplayRuleName == null) ? 0 : linesToDisplayRuleName.hashCode());
        result = prime * result + noLinesToDisplayEachSideOfBalancedLine;
        result = prime * result + normalLineInverseGap;
        result = prime * result + ((rulesThatExecuted == null) ? 0 : rulesThatExecuted.hashCode());
        result = prime * result + ((staticLineList == null) ? 0 : staticLineList.hashCode());
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
        DerivedMarketsInfo other = (DerivedMarketsInfo) obj;
        if (dataToGenerateStaticMarkets == null) {
            if (other.dataToGenerateStaticMarkets != null)
                return false;
        } else if (!dataToGenerateStaticMarkets.equals(other.dataToGenerateStaticMarkets))
            return false;
        if (derivedMarketSpecs == null) {
            if (other.derivedMarketSpecs != null)
                return false;
        } else if (!derivedMarketSpecs.equals(other.derivedMarketSpecs))
            return false;
        if (displayLineSpacing != other.displayLineSpacing)
            return false;
        if (linesToDisplayReason == null) {
            if (other.linesToDisplayReason != null)
                return false;
        } else if (!linesToDisplayReason.equals(other.linesToDisplayReason))
            return false;
        if (linesToDisplayRuleName == null) {
            if (other.linesToDisplayRuleName != null)
                return false;
        } else if (!linesToDisplayRuleName.equals(other.linesToDisplayRuleName))
            return false;
        if (noLinesToDisplayEachSideOfBalancedLine != other.noLinesToDisplayEachSideOfBalancedLine)
            return false;
        if (normalLineInverseGap != other.normalLineInverseGap)
            return false;
        if (rulesThatExecuted == null) {
            if (other.rulesThatExecuted != null)
                return false;
        } else if (!rulesThatExecuted.equals(other.rulesThatExecuted))
            return false;
        if (staticLineList == null) {
            if (other.staticLineList != null)
                return false;
        } else if (!staticLineList.equals(other.staticLineList))
            return false;
        return true;
    }


    @Override
    public String toString() {
        return JsonUtil.marshalJson(this);
        // ToStringHelper sh = MoreObjects.toStringHelper(this);
        // sh.add("linesToDisplayReason", linesToDisplayReason);
        // sh.add("linesToDisplayRuleName", linesToDisplayRuleName);
        // if (noLinesToDisplayEachSideOfBalancedLine != 0) {
        // sh.add("noLinesToDisplayEachSideOfBalancedLine", noLinesToDisplayEachSideOfBalancedLine);
        // }
        // if (displayLineSpacing != 0) {
        // sh.add("displayLineSpacing", displayLineSpacing);
        // }
        // if (normalLineInverseGap != 0) {
        // sh.add("normalLineInverseGap", normalLineInverseGap);
        // }
        // if (!derivedMarketSpecs.isEmpty()) {
        // sh.add("derivedMarketSpecs", derivedMarketSpecs);
        // }
        // if (!rulesThatExecuted.isEmpty()) {
        // sh.add("rulesThatExecuted", rulesThatExecuted);
        // }
        // return sh.omitNullValues().toString();
    }

    /**
     * returns as a string split over several lines for easier reading
     * 
     * @return
     */
    public String toMultiLineString() {
        return "MarketStatus [\n, linesToDisplayReason=" + linesToDisplayReason + ", linesToDisplayRuleName="
                        + linesToDisplayRuleName + ", noLinesToDisplayEachSideOfBalancedLine="
                        + noLinesToDisplayEachSideOfBalancedLine + ", displayLineSpacing=" + displayLineSpacing
                        + ", normalLineInverseGap=" + normalLineInverseGap + ",\n derivedMarketSpecs="
                        + derivedMarketSpecs + ",\n rulesThatExecuted=" + rulesThatExecuted + "]";

    }

    /**
     * if targetStatus is of higher priority than the current suspensionStatus then updates the suspensionStatus,
     * suspensionsStatusRuleName. If not no action is taken suspensionStatusReason properties.
     * 
     * @param ruleName
     * @param targetStatus
     * @param reason
     */


    public DerivedMarketsInfo copy() {
        DerivedMarketsInfo cc = new DerivedMarketsInfo();
        cc.linesToDisplayReason = this.linesToDisplayReason;
        cc.linesToDisplayRuleName = this.linesToDisplayRuleName;
        cc.noLinesToDisplayEachSideOfBalancedLine = this.noLinesToDisplayEachSideOfBalancedLine;
        cc.displayLineSpacing = this.displayLineSpacing;
        cc.normalLineInverseGap = this.normalLineInverseGap; // default value 1
                                                             // – can be 1, 2
        for (DerivedMarketSpec spec : this.derivedMarketSpecs)
            cc.derivedMarketSpecs.add(spec.copy());

        cc.staticLineList = this.staticLineList;
        // FIXME : need to do a clone
        cc.dataToGenerateStaticMarkets = this.dataToGenerateStaticMarkets;
        cc.rulesThatExecuted = this.rulesThatExecuted;
        return cc;
    }

}
