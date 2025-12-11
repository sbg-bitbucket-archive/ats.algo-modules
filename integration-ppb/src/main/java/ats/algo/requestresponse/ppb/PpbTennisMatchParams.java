package ats.algo.requestresponse.ppb;

import java.io.Serializable;
import java.util.LinkedHashMap;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import ats.algo.core.MarketGroup;
import ats.algo.core.baseclasses.GenericMatchParams;
import ats.algo.core.baseclasses.MatchParam;
import ats.algo.core.baseclasses.MatchParamType;
import ats.algo.core.enumMatchParam.EnumMatchParam;
import ats.algo.core.tradercontrol.TraderControlMatchParam;
import ats.algo.sport.tennis.TennisMatchFormat;
import ats.algo.sport.tennis.TennisMatchFormat.TournamentLevel;

public class PpbTennisMatchParams implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private double teamAPreMatchLine;
    private double teamBPreMatchLine;
    private int teamAPlayerRank;
    private int teamBPlayerRank;
    private String teamAPlayerId;
    private String teamBPlayerId;
    private boolean usePlayerParameters;
    private int serveQuality;
    private String traderConfidence;
    private String surfaceSpeed;
    private int aceQualityA;
    private int aceQualityB;
    private int doubleFaultQualityA;
    private int doubleFaultQualityB;
    private int biasPlayerA;
    private String teamAPlayerName;
    private String teamBPlayerName;

    @JsonCreator
    public PpbTennisMatchParams(@JsonProperty("teamAPreMatchLine") double teamAPreMatchLine,
                    @JsonProperty("teamBPreMatchLine") double teamBPreMatchLine,
                    @JsonProperty("teamAPlayerRank") int teamAPlayerRank,
                    @JsonProperty("teamBPlayerRank") int teamBPlayerRank,
                    @JsonProperty("teamAPlayerId") String teamAPlayerId,
                    @JsonProperty("teamBPlayerId") String teamBPlayerId,
                    @JsonProperty("teamAPlayerName") String teamAPlayerName,
                    @JsonProperty("teamBPlayerName") String teamBPlayerName,
                    @JsonProperty("teamAPlayer1Sex") String teamAPlayer1Sex,
                    @JsonProperty("teamAPlayer2Sex") String teamAPlayer2Sex,
                    @JsonProperty("teamBPlayer1Sex") String teamBPlayer1Sex,
                    @JsonProperty("teamBPlayer2Sex") String teamBPlayer2Sex,
                    @JsonProperty("usePlayerParameters") boolean usePlayerParameters,
                    @JsonProperty("serveQuality") int serveQuality,
                    @JsonProperty("traderConfidence") String traderConfidence,
                    @JsonProperty("surfaceSpeed") String surfaceSpeed, @JsonProperty("aceQualityA") int aceQualityA,
                    @JsonProperty("aceQualityB") int aceQualityB,
                    @JsonProperty("doubleFaultQualityA") int doubleFaultQualityA,
                    @JsonProperty("doubleFaultQualityB") int doubleFaultQualityB,
                    @JsonProperty("biasPlayerA") int biasPlayerA) {
        super();
        this.teamAPreMatchLine = teamAPreMatchLine;
        this.teamBPreMatchLine = teamBPreMatchLine;
        this.teamAPlayerRank = teamAPlayerRank;
        this.teamBPlayerRank = teamBPlayerRank;
        this.teamAPlayerId = teamAPlayerId;
        this.teamBPlayerId = teamBPlayerId;
        this.teamAPlayerName = teamAPlayerName;
        this.teamBPlayerName = teamBPlayerName;
        this.usePlayerParameters = usePlayerParameters;
        this.serveQuality = serveQuality;
        this.traderConfidence = traderConfidence;
        this.surfaceSpeed = surfaceSpeed;
        this.aceQualityA = aceQualityA;
        this.aceQualityB = aceQualityB;
        this.doubleFaultQualityA = doubleFaultQualityA;
        this.doubleFaultQualityB = doubleFaultQualityB;
        this.biasPlayerA = biasPlayerA;
    }

    public static PpbTennisMatchParams generate(GenericMatchParams matchParams, TennisMatchFormat matchFormat) {
        LinkedHashMap<String, MatchParam> map = matchParams.getParamMap();
        Builder builder = new Builder();
        double prob = map.get("teamAPreMatchLine").getGaussian().getMean();
        builder.teamAPreMatchLine(prob);
        builder.teamBPreMatchLine(1 - prob);
        builder.teamAPlayerId(((EnumMatchParam) map.get("teamAPlayerId")).getValue());
        builder.teamBPlayerId(((EnumMatchParam) map.get("teamBPlayerId")).getValue());
        builder.teamAPlayerName(((EnumMatchParam) map.get("teamAPlayerName")).getValue());
        builder.teamBPlayerName(((EnumMatchParam) map.get("teamBPlayerName")).getValue());
        builder.teamAPlayerRank((int) map.get("teamAPlayerRank").getGaussian().getMean());
        builder.teamBPlayerRank((int) map.get("teamBPlayerRank").getGaussian().getMean());

        if (matchFormat.isDoublesMatch()) {
            builder.usePlayerParameters(false);
        } else if (((EnumMatchParam) map.get("usePlayerParameters")).getValue().equals("true")) {
            builder.usePlayerParameters(true);
        } else {
            builder.usePlayerParameters(false);
        }

        builder.serveQuality((int) map.get("serveQuality").getGaussian().getMean());
        builder.traderConfidence(((EnumMatchParam) map.get("traderConfidence")).getValue());
        builder.traderConfidence(((EnumMatchParam) map.get("surfaceSpeed")).getValue());
        builder.aceQualityA((int) map.get("aceQualityA").getGaussian().getMean());
        builder.aceQualityB((int) map.get("aceQualityB").getGaussian().getMean());
        builder.doubleFaultQualityA((int) map.get("doubleFaultQualityA").getGaussian().getMean());
        builder.doubleFaultQualityB((int) map.get("doubleFaultQualityB").getGaussian().getMean());
        builder.biasPlayerA((int) map.get("biasPlayerA").getGaussian().getMean());
        return builder.build();

    }

    public double getTeamAPreMatchLine() {
        return teamAPreMatchLine;
    }

    public double getTeamBPreMatchLine() {
        return teamBPreMatchLine;
    }

    public int getTeamAPlayerRank() {
        return teamAPlayerRank;
    }

    public int getTeamBPlayerRank() {
        return teamBPlayerRank;
    }

    public String getTeamAPlayerName() {
        return teamAPlayerName;
    }

    public String getTeamBPlayerName() {
        return teamBPlayerName;
    }

    public boolean isUsePlayerParameters() {
        return usePlayerParameters;
    }

    public int getServeQuality() {
        return serveQuality;
    }

    public String getTraderConfidence() {
        return traderConfidence;
    }

    public String getSurfaceSpeed() {
        return surfaceSpeed;
    }

    public int getAceQualityA() {
        return aceQualityA;
    }
    
    public int getAceQualityB() {
        return aceQualityB;
    }

    public int getDoubleFaultQualityA() {
        return doubleFaultQualityA;
    }

    public int getDoubleFaultQualityB() {
        return doubleFaultQualityB;
    }

    public int getBiasPlayerA() {
        return biasPlayerA;
    }

    public String getTeamAPlayerId() {
        return teamAPlayerId;
    }

    public String getTeamBPlayerId() {
        return teamBPlayerId;
    }


    public static class Builder {
        private double teamAPreMatchLine;
        private double teamBPreMatchLine;
        private int teamAPlayerRank;
        private int teamBPlayerRank;
        private String teamAPlayerId;
        private String teamBPlayerId;
        private String teamAPlayerName;
        private String teamBPlayerName;
        private boolean usePlayerParameters;
        private int serveQuality;
        private String traderConfidence;
        private String surfaceSpeed;
        private int aceQualityA;
        private int aceQualityB;
        private int doubleFaultQualityA;
        private int doubleFaultQualityB;
        private int biasPlayerA;

        public Builder teamAPreMatchLine(double val) {
            teamAPreMatchLine = val;
            return this;
        }

        public Builder teamBPreMatchLine(double val) {
            teamBPreMatchLine = val;
            return this;
        }

        public Builder teamAPlayerRank(int val) {
            teamAPlayerRank = val;
            return this;
        }

        public Builder teamBPlayerRank(int val) {
            teamBPlayerRank = val;
            return this;
        }


        public Builder teamAPlayerId(String val) {
            teamAPlayerId = val;
            return this;
        }

        public Builder teamBPlayerId(String val) {
            teamBPlayerId = val;
            return this;
        }

        public Builder teamAPlayerName(String val) {
            teamAPlayerName = val;
            return this;
        }

        public Builder teamBPlayerName(String val) {
            teamBPlayerName = val;
            return this;
        }

        public Builder usePlayerParameters(boolean val) {
            usePlayerParameters = val;
            return this;
        }

        public Builder serveQuality(int val) {
            serveQuality = val;
            return this;
        }

        public Builder traderConfidence(String val) {
            traderConfidence = val;
            return this;
        }

        public Builder surfaceSpeed(String val) {
            surfaceSpeed = val;
            return this;
        }

        public Builder aceQualityA(int val) {
            aceQualityA = val;
            return this;
        }
        
        public Builder aceQualityB(int val) {
            aceQualityB = val;
            return this;
        }

        public Builder doubleFaultQualityA(int val) {
            doubleFaultQualityA = val;
            return this;
        }

        public Builder doubleFaultQualityB(int val) {
            doubleFaultQualityB = val;
            return this;
        }

        public Builder biasPlayerA(int val) {
            biasPlayerA = val;
            return this;
        }

        public PpbTennisMatchParams build() {
            return new PpbTennisMatchParams(this);
        }
    }

    private PpbTennisMatchParams(Builder builder) {
        teamAPreMatchLine = builder.teamAPreMatchLine;
        teamBPreMatchLine = builder.teamBPreMatchLine;
        teamAPlayerRank = builder.teamAPlayerRank;
        teamBPlayerRank = builder.teamBPlayerRank;
        teamAPlayerId = builder.teamAPlayerId;
        teamBPlayerId = builder.teamBPlayerId;
        // teamAPlayer1Sex = builder.teamAPlayer1Sex;
        // teamAPlayer2Sex = builder.teamAPlayer2Sex;
        // teamBPlayer1Sex = builder.teamBPlayer1Sex;
        // teamBPlayer2Sex = builder.teamBPlayer2Sex;
        teamAPlayerName = builder.teamAPlayerName;
        teamBPlayerName = builder.teamBPlayerName;
        usePlayerParameters = builder.usePlayerParameters;
        serveQuality = builder.serveQuality;
        traderConfidence = builder.traderConfidence;
        surfaceSpeed = builder.surfaceSpeed;
        aceQualityA = builder.aceQualityA;
        aceQualityB = builder.aceQualityB;
        doubleFaultQualityA = builder.doubleFaultQualityA;
        doubleFaultQualityB = builder.doubleFaultQualityB;
        biasPlayerA = builder.biasPlayerA;
    }

    /**
     * creates the PPB specific match params in AlgoFramework format
     * 
     * @return
     */
    public static GenericMatchParams generatePpbGenericMatchParams(GenericMatchParams defaultMatchParams,
                    TennisMatchFormat matchFormat) {
        GenericMatchParams genericMatchParams = new GenericMatchParams();
        genericMatchParams.setEventId(defaultMatchParams.getEventId());
        genericMatchParams.setOriginatingClassName(GenericMatchParams.class.getCanonicalName());
        LinkedHashMap<String, MatchParam> paramMap = genericMatchParams.getParamMap();
        paramMap.put("teamAPreMatchLine", generateDoubleMatchParam(MatchParamType.A, "Probability team A wins match",
                        0.5, 0.05, 0.0, 1.0, true));
        paramMap.put("teamAPlayerRank",
                        generateIntegerMatchParam("Team A player rank", matchFormat.getTeamAPlayerRank(), 0, 10000));
        paramMap.put("teamBPlayerRank",
                        generateIntegerMatchParam("Team B player rank", matchFormat.getTeamBPlayerRank(), 0, 10000));
        paramMap.put("teamAPlayerId", generateEnumMatchParam("Team A player Id", matchFormat.getTeamAPlayerId(),
                        new String[] {matchFormat.getTeamAPlayerId()}));
        paramMap.put("teamBPlayerId", generateEnumMatchParam("Team B player Id", matchFormat.getTeamBPlayerId(),
                        new String[] {matchFormat.getTeamBPlayerId()}));
        paramMap.put("teamAPlayerName", generateEnumMatchParam("Team A player Name", matchFormat.getTeamAPlayerName(),
                        new String[] {matchFormat.getTeamAPlayerName()}));
        paramMap.put("teamBPlayerName", generateEnumMatchParam("Team B player Name", matchFormat.getTeamBPlayerName(),
                        new String[] {matchFormat.getTeamBPlayerName()}));
        String usePlayerParameters = "true";
        if (matchFormat.isDoublesMatch()) {
            usePlayerParameters = "false";
        } else if (matchFormat.getTournamentLevel().equals(TournamentLevel.ITF)
                        || matchFormat.getTournamentLevel().equals(TournamentLevel.ITF_QUALIFIER)) {
            usePlayerParameters = "false";
        } else {
            usePlayerParameters = "true";
        }
        paramMap.put("usePlayerParameters", generateEnumMatchParam("Use player parameters", usePlayerParameters,
                        new String[] {"true", "false"}));
        paramMap.put("serveQuality", generateIntegerMatchParam("Serve quality", 0, -30, 30));
        paramMap.put("traderConfidence", generateEnumMatchParam("Trader confidence", "high",
                        new String[] {"extremely_low", "very_low", "low", "medium", "high", "very_high"}));
        paramMap.put("surfaceSpeed",
                        generateEnumMatchParam("Surface speed", "medium", new String[] {"slow", "medium", "fast"}));
        paramMap.put("aceQualityA", generateIntegerMatchParam("Ace quality A", 0, -30, 30));
        paramMap.put("aceQualityB", generateIntegerMatchParam("Ace quality B", 0, -30, 30));
        paramMap.put("doubleFaultQualityA", generateIntegerMatchParam("Double fault quality A", 0, -30, 30));
        paramMap.put("doubleFaultQualityB", generateIntegerMatchParam("Double fault quality B", 0, -30, 30));
        paramMap.put("biasPlayerA", generateIntegerMatchParam("Bias Player A", 0, -30, 30));
        return genericMatchParams;
    }

    private static MatchParam generateDoubleMatchParam(MatchParamType type, String description, double defaultValue,
                    double stdDevn, double min, double max, boolean displayAsPercentage) {
        MatchParam matchParam = new MatchParam();
        matchParam.setMatchParameterType(type);
        matchParam.setMarketGroup(MarketGroup.NOT_SPECIFIED);
        matchParam.setDescription(description);
        matchParam.setMinAllowedParamValue(min);
        matchParam.setMaxAllowedParamValue(max);
        matchParam.getGaussian().setProperties(defaultValue, stdDevn, 0.0);
        return matchParam;
    }

    private static MatchParam generateIntegerMatchParam(String description, int defaultValue, int min, int max) {
        MatchParam matchParam = new TraderControlMatchParam(defaultValue, min, max);
        matchParam.setDescription(description);
        return matchParam;
    }

    private static MatchParam generateEnumMatchParam(String description, String defaultValue, String[] values) {
        MatchParam matchParam = new EnumMatchParam(description, defaultValue, values);
        return matchParam;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + aceQualityA;
        result = prime * result + aceQualityB;
        result = prime * result + biasPlayerA;
        result = prime * result + doubleFaultQualityA;
        result = prime * result + doubleFaultQualityB;
        result = prime * result + serveQuality;
        result = prime * result + ((surfaceSpeed == null) ? 0 : surfaceSpeed.hashCode());
        result = prime * result + ((teamAPlayerId == null) ? 0 : teamAPlayerId.hashCode());
        result = prime * result + teamAPlayerRank;
        long temp;
        temp = Double.doubleToLongBits(teamAPreMatchLine);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        result = prime * result + ((teamBPlayerId == null) ? 0 : teamBPlayerId.hashCode());
        result = prime * result + teamBPlayerRank;
        temp = Double.doubleToLongBits(teamBPreMatchLine);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        result = prime * result + ((traderConfidence == null) ? 0 : traderConfidence.hashCode());
        result = prime * result + (usePlayerParameters ? 1231 : 1237);
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
        PpbTennisMatchParams other = (PpbTennisMatchParams) obj;
        if (aceQualityA != other.aceQualityA)
            return false;
        if (aceQualityB != other.aceQualityB)
            return false;
        if (biasPlayerA != other.biasPlayerA)
            return false;
        if (doubleFaultQualityA != other.doubleFaultQualityA)
            return false;
        if (doubleFaultQualityB != other.doubleFaultQualityB)
            return false;
        if (serveQuality != other.serveQuality)
            return false;
        if (surfaceSpeed == null) {
            if (other.surfaceSpeed != null)
                return false;
        } else if (!surfaceSpeed.equals(other.surfaceSpeed))
            return false;
        if (teamAPlayerId == null) {
            if (other.teamAPlayerId != null)
                return false;
        } else if (!teamAPlayerId.equals(other.teamAPlayerId))
            return false;
        if (teamAPlayerRank != other.teamAPlayerRank)
            return false;
        if (Double.doubleToLongBits(teamAPreMatchLine) != Double.doubleToLongBits(other.teamAPreMatchLine))
            return false;
        if (teamBPlayerId == null) {
            if (other.teamBPlayerId != null)
                return false;
        } else if (!teamBPlayerId.equals(other.teamBPlayerId))
            return false;
        if (teamBPlayerRank != other.teamBPlayerRank)
            return false;
        if (Double.doubleToLongBits(teamBPreMatchLine) != Double.doubleToLongBits(other.teamBPreMatchLine))
            return false;
        if (traderConfidence == null) {
            if (other.traderConfidence != null)
                return false;
        } else if (!traderConfidence.equals(other.traderConfidence))
            return false;
        if (usePlayerParameters != other.usePlayerParameters)
            return false;
        return true;
    }

}
