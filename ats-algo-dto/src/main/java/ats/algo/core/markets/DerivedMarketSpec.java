package ats.algo.core.markets;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import ats.core.util.json.JsonUtil;

/**
 * immutable class to hold the properties needed to derive one or more markets
 * 
 * @author Geoff
 *
 */
public class DerivedMarketSpec implements Serializable {

    public enum DerivedMarketType {
        NOT_DERIVED_MARKET,
        RANGE_TOTAL,
        RANGE_HCAP,
        LINES_STATIC,
        LINES_NEAREST_EVENS,
        LINES_REGULAR,
        LINES_EUROPEAN,
        LINES_ASIAN
    }

    public enum DerivedMarketGapBetweenLines {
        FULL_LINE,
        HALF_LINE,
        QUARTER_LINE
    }

    private static final long serialVersionUID = 1L;
    private DerivedMarketType derivedMarketType;
    private DerivedMarketSpecApplicability applicability;
    private MarketCategory marketCategory;
    private String derivedMarketAtsType;
    private Map<String, String> derivedMarketNames;
    private int nRanges;
    private int rangeWidth;
    private int totalMarketRangeStart;
    private int lineSpacing;
    private int nMkts;
    private double minProbBoundary;
    private double maxProbBoundary;
    private DerivedMarketGapBetweenLines derivedMarketGapBetweenLines;
    private boolean innerRangesShorter;
    // FIXME: TO BE ADDED IN JASONCREATORS
    private double[] fixedLines; // Jin added fixedLines option


    private DerivedMarketSpec() {}

    @JsonCreator
    public DerivedMarketSpec(@JsonProperty("derivedMarketType") DerivedMarketType derivedMarketType,
                    @JsonProperty("applicability") DerivedMarketSpecApplicability applicability,
                    @JsonProperty("marketCategory") MarketCategory marketCategory,
                    @JsonProperty("derivedMarketAtsType") String derivedMarketAtsType,
                    @JsonProperty("derivedMarketNames") Map<String, String> derivedMarketNames,
                    @JsonProperty("nRanges") int nRanges, @JsonProperty("rangeWidth") int rangeWidth,
                    @JsonProperty("totalMarketRangeStart") int totalMarketRangeStart,
                    @JsonProperty("lineSpacing") int lineSpacing, @JsonProperty("nMkts") int nMkts,
                    @JsonProperty("derivedMarketGapBetweenLines") DerivedMarketGapBetweenLines derivedMarketGapBetweenLines,
                    @JsonProperty("innerRangesShorter") boolean innerRangesShorter,
                    @JsonProperty("fixedLines") double[] fixedLines,
                    @JsonProperty("minProbBoundary") int minProbBoundary,
                    @JsonProperty("maxProbBoundary") int maxProbBoundary) {
        super();
        this.derivedMarketType = derivedMarketType;
        this.applicability = applicability;
        this.marketCategory = marketCategory;
        this.derivedMarketAtsType = derivedMarketAtsType;
        this.derivedMarketNames = derivedMarketNames;
        this.nRanges = nRanges;
        this.rangeWidth = rangeWidth;
        this.totalMarketRangeStart = totalMarketRangeStart;
        this.lineSpacing = lineSpacing;
        this.nMkts = nMkts;
        this.derivedMarketGapBetweenLines = derivedMarketGapBetweenLines;
        this.innerRangesShorter = innerRangesShorter;
        this.fixedLines = fixedLines;
        this.minProbBoundary = minProbBoundary;
        this.maxProbBoundary = maxProbBoundary;
    }

    /**
     * generates a DerivedMarketSpec object suitable for a RANGE_HCAP mkt
     * 
     * @param derivedMarketAtsType the ATS type code
     * @param derivedMarketName
     * @param applicability whether applies pre match in play or both
     * @param inPlayOnly
     * @param nRanges the number of selections to generate each side of the "Draw". e.g. if nRanges = 3 then 7
     *        selections will be generated selection
     * @param rangeWidth the width of each range. e.g. if rangeWidth = 10 and innerRangesShorter = false then ranges
     *        will be 1 to 10, -1 to -10, 11 to 20 etc
     * @param innerRangesShorter if true the first range each side of 0 is 1 shorter than specified by rangeWidth, so
     *        e.g. 1 to 9, -1 to -9, 10 to 19 etc
     */
    public static DerivedMarketSpec getDerivedMarketSpecForDynamicRangeHcap(String derivedMarketAtsType,
                    String derivedMarketName, DerivedMarketSpecApplicability applicability, int nRanges, int rangeWidth,
                    boolean innerRangesShorter) {
        return getDerivedMarketSpecForDynamicRangeHcap(derivedMarketAtsType, initDerivedMarketNames(derivedMarketName),
                        applicability, nRanges, rangeWidth, innerRangesShorter);
    }

    /**
     * generates a DerivedMarketSpec object suitable for a RANGE_HCAP mkt
     * 
     * @param derivedMarketAtsType the ATS type code
     * @param derivedMarketNames Map of <sequenceId, derivedMarketName> for use where the name of the derivedMarket
     *        depends on the sequenceID associated with the parent market. For example sequenceIds "S1" "S2" might be
     *        associated with market names of the form "First half points..." and "Second half points..."
     * @param applicability whether applies pre match in play or both
     * @param inPlayOnly
     * @param nRanges the number of selections to generate each side of the "Draw". e.g. if nRanges = 3 then 7
     *        selections will be generated selection
     * @param rangeWidth the width of each range. e.g. if rangeWidth = 10 and innerRangesShorter = false then ranges
     *        will be 1 to 10, -1 to -10, 11 to 20 etc
     * @param innerRangesShorter if true the first range each side of 0 is 1 shorter than specified by rangeWidth, so
     *        e.g. 1 to 9, -1 to -9, 10 to 19 etc
     */
    public static DerivedMarketSpec getDerivedMarketSpecForDynamicRangeHcap(String derivedMarketAtsType,
                    Map<String, String> derivedMarketNames, DerivedMarketSpecApplicability applicability, int nRanges,
                    int rangeWidth, boolean innerRangesShorter) {
        DerivedMarketSpec spec = new DerivedMarketSpec();
        spec.marketCategory = MarketCategory.GENERAL;
        spec.derivedMarketType = DerivedMarketType.RANGE_HCAP;
        spec.derivedMarketAtsType = derivedMarketAtsType;
        spec.derivedMarketNames = derivedMarketNames;
        spec.nRanges = nRanges;
        spec.rangeWidth = rangeWidth;
        spec.totalMarketRangeStart = 0;
        spec.applicability = applicability;
        spec.innerRangesShorter = innerRangesShorter;
        return spec;
    }

    /*
     * generates the map of sequenceId, market name for the case where there is only one supplied market name
     */
    private static Map<String, String> initDerivedMarketNames(String derivedMarketName) {
        Map<String, String> derivedMarketNames = new HashMap<String, String>(1);
        derivedMarketNames.put("M", derivedMarketName);
        return derivedMarketNames;
    }

    /**
     * generates a DerivedMarketSpec object suitable for a RANGE_TOTAL mkt
     * 
     * @param derivedMarketAtsType the ATS type code
     * @param derivedMarketName
     * @param applicability whether applies pre match in play or both
     * @param nRanges the number of selections to generate excluding the "below" and"above" selections. e.g. if nRanges
     *        = 4 then 6 selections will be created.
     * @param rangeWidth the width of each selection range
     * @param totalMarketRangeStart the id of the line corresponding to the "below" selection
     * @return
     */
    public static DerivedMarketSpec getDerivedMarketSpecForDynamicRangeTotal(String derivedMarketAtsType,
                    String derivedMarketName, DerivedMarketSpecApplicability applicability, int nRanges, int rangeWidth,
                    int totalMarketRangeStart) {
        return getDerivedMarketSpecForDynamicRangeTotal(derivedMarketAtsType, initDerivedMarketNames(derivedMarketName),
                        applicability, nRanges, rangeWidth, totalMarketRangeStart);
    }

    /**
     * generates a DerivedMarketSpec object suitable for a RANGE_TOTAL mkt
     * 
     * @param derivedMarketAtsType the ATS type code
     * @param derivedMarketNames Map of <sequenceId, derivedMarketName> for use where the name of the derivedMarket
     *        depends on the sequenceID associated with the parent market. For example sequenceIds "S1" "S2" might be
     *        associated with market names of the form "First half points..." and "Second half points..."
     * @param applicability whether applies pre match in play or both
     * @param nRanges the number of selections to generate excluding the "below" and"above" selections. e.g. if nRanges
     *        = 4 then 6 selections will be created.
     * @param rangeWidth the width of each selection range
     * @param totalMarketRangeStart the id of the line corresponding to the "below" selection
     * @return
     */
    public static DerivedMarketSpec getDerivedMarketSpecForDynamicRangeTotal(String derivedMarketAtsType,
                    Map<String, String> derivedMarketNames, DerivedMarketSpecApplicability applicability, int nRanges,
                    int rangeWidth, int totalMarketRangeStart) {
        DerivedMarketSpec spec = new DerivedMarketSpec();
        spec.marketCategory = MarketCategory.GENERAL;
        spec.derivedMarketType = DerivedMarketType.RANGE_TOTAL;
        spec.derivedMarketAtsType = derivedMarketAtsType;
        spec.derivedMarketNames = derivedMarketNames;
        spec.nRanges = nRanges;
        spec.rangeWidth = rangeWidth;
        spec.totalMarketRangeStart = totalMarketRangeStart;
        spec.applicability = applicability;
        return spec;
    }


    /**
     * Generates a set of lineMarkets where the central lines are balanced around a static baseLine. The baseLine is set
     * pre-match via a trading rule.
     * 
     * @param derivedMarketAtsType the ATS type code
     * @param derivedMarketName
     * @param applicability whether applies pre match in play or both
     * @param nMktsEachSide the number of markets to generate each side of the baseLine. e.g. if nMktsEachSide = 3 then
     *        7 selections will be generated
     * @param lineSpacing the gap between successive lines.
     * @return
     */
    public static DerivedMarketSpec getDerivedMarketSpecForStaticLines(String derivedMarketAtsType,
                    String derivedMarketName, DerivedMarketSpecApplicability applicability, int nMktsEachSide,
                    int lineSpacing) {
        return getDerivedMarketSpecForStaticLines(derivedMarketAtsType, initDerivedMarketNames(derivedMarketName),
                        applicability, nMktsEachSide, lineSpacing);
    }

    /**
     * Generates a set of lineMarkets where the central lines are balanced around a static baseLine. The baseLine is set
     * pre-match via a trading rule.
     * 
     * @param derivedMarketAtsType the ATS type code
     * @param derivedMarketNames Map of <sequenceId, derivedMarketName> for use where the name of the derivedMarket
     *        depends on the sequenceID associated with the parent market. For example sequenceIds "S1" "S2" might be
     *        associated with market names of the form "First half points..." and "Second half points..."
     * @param applicability whether applies pre match in play or both
     * @param nMktsEachSide the number of markets to generate each side of the baseLine. e.g. if nMktsEachSide = 3 then
     *        7 selections will be generated
     * @param lineSpacing the gap between successive lines.
     * @return
     */
    public static DerivedMarketSpec getDerivedMarketSpecForStaticLines(String derivedMarketAtsType,
                    Map<String, String> derivedMarketNames, DerivedMarketSpecApplicability applicability,
                    int nMktsEachSide, int lineSpacing) {
        DerivedMarketSpec spec = new DerivedMarketSpec();
        spec.derivedMarketType = DerivedMarketType.LINES_STATIC;
        spec.derivedMarketAtsType = derivedMarketAtsType;
        spec.derivedMarketNames = derivedMarketNames;
        spec.nMkts = nMktsEachSide;
        spec.lineSpacing = lineSpacing;
        spec.applicability = applicability;
        spec.derivedMarketGapBetweenLines = DerivedMarketGapBetweenLines.FULL_LINE;
        spec.minProbBoundary = 0;
        spec.maxProbBoundary = 0;
        return spec;
    }


    /**
     * Generates a set of regular (also called 2-way) Markets where the central lines are balanced around the balanced
     * line. The derived markets will have the same ATS type code and name as the original. Lines will be generated at
     * intervals of lineSpacing
     * 
     * @param applicability whether applies pre match in play or both
     * @param nMktsEachSide the number of markets to generate each side of the baseLine. e.g. if nMktsEachSide = 3 then
     *        7 selections will be generated
     * @param lineSpacing the gap between successive lines. e.g. if lineSpacing = 2 then lines will be generated at
     *        19.5, 21.5, 23.5...
     * @return
     */
    public static DerivedMarketSpec getDerivedMarketSpecForRegularLines(DerivedMarketSpecApplicability applicability,
                    int nMktsEachSide, int lineSpacing) {
        return getDerivedMarketSpecForRegularLines(applicability, nMktsEachSide, lineSpacing,
                        DerivedMarketGapBetweenLines.FULL_LINE);

    }


    public static DerivedMarketSpec getDerivedMarketSpecForFixedLines(DerivedMarketSpecApplicability applicability,
                    double[] fixedLines) {
        DerivedMarketSpec spec = new DerivedMarketSpec();
        spec.derivedMarketType = DerivedMarketType.LINES_REGULAR;
        spec.nMkts = 0;
        spec.lineSpacing = 0;
        spec.applicability = applicability;
        spec.derivedMarketGapBetweenLines = DerivedMarketGapBetweenLines.FULL_LINE;
        spec.fixedLines = fixedLines;
        spec.minProbBoundary = 0;
        spec.maxProbBoundary = 0;
        return spec;

    }

    public static DerivedMarketSpec getDerivedMarketSpecForFixedLines(DerivedMarketSpecApplicability applicability,
                    double[] fixedLines, double minProbBoundary, double maxProbBoundary) {
        DerivedMarketSpec spec = new DerivedMarketSpec();
        spec.derivedMarketType = DerivedMarketType.LINES_REGULAR;
        spec.nMkts = 0;
        spec.lineSpacing = 0;
        spec.applicability = applicability;
        spec.derivedMarketGapBetweenLines = DerivedMarketGapBetweenLines.FULL_LINE;
        spec.fixedLines = fixedLines;
        spec.minProbBoundary = minProbBoundary;
        spec.maxProbBoundary = maxProbBoundary;
        return spec;

    }


    /**
     * Generates a set of regular (also called 2-way) Markets where the central lines are balanced around the balanced
     * line. The derived markets will have the same ATS type code and name as the original. Lines will be generated at
     * intervals specified of lineSpacing*gapBetweenLines. So e.g. if lineSpacing = 1 and gapBetweenLines= HALF_LINE
     * then lines might be 19.5, 20.0 20.5, ...
     * 
     * @param applicability whether applies pre match in play or both
     * @param nMktsEachSide the number of markets to generate each side of the baseLine. e.g. if nMktsEachSide = 3 then
     *        7 selections will be generated
     * @param lineSpacing the gap between successive lines.
     * @param gapBetweenLines must be either FULL_LINE or HALF_LINE. QUARTR_LINE is not valid for regular line markets
     * @return
     */
    public static DerivedMarketSpec getDerivedMarketSpecForRegularLines(DerivedMarketSpecApplicability applicability,
                    int nMktsEachSide, int lineSpacing, DerivedMarketGapBetweenLines gapBetweenLines) {
        if (gapBetweenLines == DerivedMarketGapBetweenLines.QUARTER_LINE)
            throw new IllegalArgumentException("Quarter line spacing not allowed for regular markets");
        DerivedMarketSpec spec = new DerivedMarketSpec();
        spec.derivedMarketType = DerivedMarketType.LINES_REGULAR;
        spec.nMkts = nMktsEachSide;
        spec.lineSpacing = lineSpacing;
        spec.applicability = applicability;
        spec.derivedMarketGapBetweenLines = gapBetweenLines;
        spec.minProbBoundary = 0;
        spec.maxProbBoundary = 0;
        return spec;
    }



    /**
     * Generates a set of European style lineMarkets where the central lines are balanced around the balanced line. The
     * line spacing is specified in increments of 1, so e.g. if balanced line is 60, nMktsEachSide = 3 and lineSpacing =
     * 2 then the following markets will be generated:
     * 
     * {54, 56, 58, 60, 62, 64, 66}
     * 
     * @param derivedMarketAtsType the ATS type code
     * @param derivedMarketName - not used
     * @param applicability whether applies pre match in play or both
     * @param nMktsEachSide the number of markets to generate each side of the baseLine. e.g. if nMktsEachSide = 3 then
     *        7 selections will be generated
     * @param lineSpacing the gap between successive lines.
     * @return
     */
    public static DerivedMarketSpec getDerivedMarketSpecForEuropeanLines(String derivedMarketAtsType,
                    String derivedMarketName, DerivedMarketSpecApplicability applicability, int nMktsEachSide,
                    int lineSpacing) {
        DerivedMarketSpec spec = new DerivedMarketSpec();
        spec.derivedMarketType = DerivedMarketType.LINES_EUROPEAN;
        spec.nMkts = nMktsEachSide;
        spec.lineSpacing = lineSpacing;
        spec.applicability = applicability;
        spec.minProbBoundary = 0;
        spec.maxProbBoundary = 0;
        return spec;
    }



    /**
     * Generates a set of Asian style lineMarkets where the central lines are balanced around the balanced line. The
     * derived markets will have the same ATS type code and name as the original
     * 
     * @param derivedMarketAtsType the ATS type code
     * @param derivedMarketName - not used
     * @param applicability whether applies pre match in play or both
     * @param nMktsEachSide the number of markets to generate each side of the baseLine. e.g. if nRanges = 3 then 7
     *        selections will be generated
     * @param lineSpacing the gap between successive lines.
     * @return
     */
    public static DerivedMarketSpec getDerivedMarketSpecForAsianLines(String derivedMarketAtsType,
                    String derivedMarketName, DerivedMarketSpecApplicability applicability, int nMktsEachSide,
                    int lineSpacing, DerivedMarketGapBetweenLines gapBetweenLines) {
        DerivedMarketSpec spec = new DerivedMarketSpec();
        spec.derivedMarketType = DerivedMarketType.LINES_ASIAN;
        spec.nMkts = nMktsEachSide;
        spec.lineSpacing = lineSpacing;
        spec.applicability = applicability;
        spec.minProbBoundary = 0;
        spec.maxProbBoundary = 0;
        return spec;
    }

    /**
     * Generates a set of Asian style lineMarkets where the central lines are balanced around the balanced line. The
     * derived markets will have the same ATS type code and name as the original
     * 
     * @param derivedMarketAtsType the ATS type code
     * @param derivedMarketName - not used
     * @param applicability whether applies pre match in play or both
     * @param nMktsEachSide the number of markets to generate each side of the baseLine. e.g. if nRanges = 3 then 7
     *        selections will be generated
     * @param lineSpacing the gap between successive lines.
     * @param minProbBoundary the min prob that this market can have.
     * @param maxProbBoundary the max prob that this market can have.
     * @return
     */
    public static DerivedMarketSpec getDerivedMarketSpecForAsianLines(String derivedMarketAtsType,
                    String derivedMarketName, DerivedMarketSpecApplicability applicability, int nMktsEachSide,
                    int lineSpacing, DerivedMarketGapBetweenLines gapBetweenLines, double minProbBoundary,
                    double maxProbBoundary) {
        DerivedMarketSpec spec = new DerivedMarketSpec();
        spec.derivedMarketType = DerivedMarketType.LINES_ASIAN;
        spec.nMkts = nMktsEachSide;
        spec.lineSpacing = lineSpacing;
        spec.applicability = applicability;
        spec.minProbBoundary = minProbBoundary;
        spec.maxProbBoundary = maxProbBoundary;
        return spec;
    }


    /**
     * Generates a set of lineMarkets which are closest to probability of 0.5
     * 
     * @param derivedMarketAtsType the ATS type code
     * @param derivedMarketName
     * @param applicability whether applies pre match in play or both
     * @param nMkts the total number of markets to generate
     * @return
     */
    public static DerivedMarketSpec getDerivedMarketSpecForLinesNearestEvens(String derivedMarketAtsType,
                    String derivedMarketName, DerivedMarketSpecApplicability applicability, int nMkts) {
        return getDerivedMarketSpecForLinesNearestEvens(derivedMarketAtsType, initDerivedMarketNames(derivedMarketName),
                        applicability, nMkts);

    }

    /**
     * Generates a set of lineMarkets which are closest to probability of 0.5
     * 
     * @param derivedMarketAtsType the ATS type code
     * @param derivedMarketNames Map of <sequenceId, derivedMarketName> for use where the name of the derivedMarket
     *        depends on the sequenceID associated with the parent market. For example sequenceIds "S1" "S2" might be
     *        associated with market names of the form "First half points..." and "Second half points..."
     * @param applicability whether applies pre match in play or both
     * @param nMkts the total number of markets to generate
     * @return
     */
    public static DerivedMarketSpec getDerivedMarketSpecForLinesNearestEvens(String derivedMarketAtsType,
                    Map<String, String> derivedMarketNames, DerivedMarketSpecApplicability applicability, int nMkts) {
        DerivedMarketSpec spec = new DerivedMarketSpec();
        spec.derivedMarketType = DerivedMarketType.LINES_NEAREST_EVENS;
        spec.derivedMarketAtsType = derivedMarketAtsType;
        spec.derivedMarketNames = derivedMarketNames;
        spec.nMkts = nMkts;
        spec.applicability = applicability;
        spec.minProbBoundary = 0;
        spec.maxProbBoundary = 0;
        return spec;

    }

    public MarketCategory getMarketCategory() {
        return marketCategory;
    }

    public int getnRanges() {
        return nRanges;
    }

    public int getRangeWidth() {
        return rangeWidth;
    }

    public String getDerivedMarketAtsType() {
        return derivedMarketAtsType;
    }

    /**
     * gets the map of <sequenceId>, <marketName>
     * 
     * @return
     */
    public Map<String, String> getDerivedMarketNames() {
        return derivedMarketNames;
    }

    public int getTotalMarketRangeStart() {
        return totalMarketRangeStart;
    }

    public DerivedMarketType getDerivedMarketType() {
        return derivedMarketType;
    }

    public int getLineSpacing() {
        return lineSpacing;
    }

    public int getnMkts() {
        return nMkts;
    }

    public DerivedMarketGapBetweenLines getDerivedMarketGapBetweenLines() {
        return derivedMarketGapBetweenLines;
    }

    public DerivedMarketSpecApplicability getApplicability() {
        return applicability;
    }

    public double[] getFixedLines() {
        return fixedLines;
    }

    public void setFixedLines(double[] fixedLines) {
        this.fixedLines = fixedLines;
    }

    public boolean isInnerRangesShorter() {
        return innerRangesShorter;
    }

    public void setInnerRangesShorter(boolean innerRangesShorter) {
        this.innerRangesShorter = innerRangesShorter;
    }

    public double getMinProbBoundary() {
        return minProbBoundary;
    }

    public void setMinProbBoundary(double minProbBoundary) {
        this.minProbBoundary = minProbBoundary;
    }

    public double getMaxProbBoundary() {
        return maxProbBoundary;
    }

    public void setMaxProbBoundary(double maxProbBoundary) {
        this.maxProbBoundary = maxProbBoundary;
    }

    /**
     * Returns the derived market name associated with the specified sequenceId. For backwards compatibility reasons if
     * this is null then returns the one associated with sequenceId "M"
     * 
     * @param sequenceId
     * @return
     */
    public String getDerivedMarketName(String sequenceId) {
        String s = this.derivedMarketNames.get(sequenceId);
        if (s == null)
            s = this.derivedMarketNames.get("M");
        return s;
    }

    public DerivedMarketSpec copy() {
        switch (this.derivedMarketType) {
            case RANGE_HCAP:
                return DerivedMarketSpec.getDerivedMarketSpecForDynamicRangeHcap(this.derivedMarketAtsType,
                                this.derivedMarketNames, this.applicability, this.nRanges, this.rangeWidth,
                                this.innerRangesShorter);

            case RANGE_TOTAL:
                return DerivedMarketSpec.getDerivedMarketSpecForDynamicRangeTotal(this.derivedMarketAtsType,
                                this.derivedMarketNames, this.applicability, this.nRanges, this.rangeWidth,
                                this.totalMarketRangeStart);

            case LINES_NEAREST_EVENS:
                return getDerivedMarketSpecForLinesNearestEvens(this.derivedMarketAtsType, this.derivedMarketNames,
                                this.applicability, this.nMkts);

            case LINES_STATIC:
                return getDerivedMarketSpecForStaticLines(this.derivedMarketAtsType, this.derivedMarketNames,
                                this.applicability, this.nMkts, this.lineSpacing);

            case LINES_REGULAR:
                return getDerivedMarketSpecForRegularLines(this.applicability, this.nMkts, this.lineSpacing);
            case LINES_ASIAN:
                return getDerivedMarketSpecForAsianLines("", "", this.applicability, this.nMkts, this.lineSpacing,
                                DerivedMarketGapBetweenLines.QUARTER_LINE);
            case NOT_DERIVED_MARKET:
                return getDerivedMarketSpecForRegularLines(this.applicability, this.nMkts, this.lineSpacing);
            default:
                throw new IllegalArgumentException(
                                String.format("DerivedMarketType not supported %s", this.derivedMarketType));
        }
    }


    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((applicability == null) ? 0 : applicability.hashCode());
        result = prime * result + ((derivedMarketAtsType == null) ? 0 : derivedMarketAtsType.hashCode());
        result = prime * result
                        + ((derivedMarketGapBetweenLines == null) ? 0 : derivedMarketGapBetweenLines.hashCode());
        result = prime * result + ((derivedMarketNames == null) ? 0 : derivedMarketNames.hashCode());
        result = prime * result + ((derivedMarketType == null) ? 0 : derivedMarketType.hashCode());
        result = prime * result + Arrays.hashCode(fixedLines);
        result = prime * result + (innerRangesShorter ? 1231 : 1237);
        result = prime * result + lineSpacing;
        result = prime * result + ((marketCategory == null) ? 0 : marketCategory.hashCode());
        long temp;
        temp = Double.doubleToLongBits(maxProbBoundary);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(minProbBoundary);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        result = prime * result + nMkts;
        result = prime * result + nRanges;
        result = prime * result + rangeWidth;
        result = prime * result + totalMarketRangeStart;
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
        DerivedMarketSpec other = (DerivedMarketSpec) obj;
        if (applicability != other.applicability)
            return false;
        if (derivedMarketAtsType == null) {
            if (other.derivedMarketAtsType != null)
                return false;
        } else if (!derivedMarketAtsType.equals(other.derivedMarketAtsType))
            return false;
        if (derivedMarketGapBetweenLines != other.derivedMarketGapBetweenLines)
            return false;
        if (derivedMarketNames == null) {
            if (other.derivedMarketNames != null)
                return false;
        } else if (!derivedMarketNames.equals(other.derivedMarketNames))
            return false;
        if (derivedMarketType != other.derivedMarketType)
            return false;
        if (!Arrays.equals(fixedLines, other.fixedLines))
            return false;
        if (innerRangesShorter != other.innerRangesShorter)
            return false;
        if (lineSpacing != other.lineSpacing)
            return false;
        if (marketCategory != other.marketCategory)
            return false;
        if (Double.doubleToLongBits(maxProbBoundary) != Double.doubleToLongBits(other.maxProbBoundary))
            return false;
        if (Double.doubleToLongBits(minProbBoundary) != Double.doubleToLongBits(other.minProbBoundary))
            return false;
        if (nMkts != other.nMkts)
            return false;
        if (nRanges != other.nRanges)
            return false;
        if (rangeWidth != other.rangeWidth)
            return false;
        if (totalMarketRangeStart != other.totalMarketRangeStart)
            return false;
        return true;
    }

    @Override
    public String toString() {
        return JsonUtil.marshalJson(this);

        // return "DerivedMarketSpec [derivedMarketType=" + derivedMarketType + ", applicability=" + applicability
        // + ", marketCategory=" + marketCategory + ", derivedMarketAtsType=" + derivedMarketAtsType
        // + ", derivedMarketNames=" + derivedMarketNames + ", nRanges=" + nRanges + ", rangeWidth="
        // + rangeWidth + ", totalMarketRangeStart=" + totalMarketRangeStart + ", lineSpacing="
        // + lineSpacing + ", nMkts=" + nMkts + ", derivedMarketGapBetweenLines="
        // + derivedMarketGapBetweenLines + ", innerRangesShorter=" + innerRangesShorter + "]";
    }

}
