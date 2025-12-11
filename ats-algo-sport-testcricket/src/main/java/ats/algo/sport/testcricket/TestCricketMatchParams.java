package ats.algo.sport.testcricket;

import ats.algo.genericsupportfunctions.Gaussian;
import ats.algo.core.MarketGroup;
import ats.algo.core.baseclasses.MatchFormat;
import ats.algo.core.baseclasses.MatchParam;
import ats.algo.core.baseclasses.MatchParamType;
import ats.algo.core.baseclasses.AlgoMatchParams;

/**
 * 
 * @author Robert
 *
 */
public class TestCricketMatchParams extends AlgoMatchParams {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private MatchParam inningoneTeamARuns;
    private MatchParam inningoneTeamBRuns;
    private MatchParam inningtwoTeamARuns;
    private MatchParam inningtwoTeamBRuns;
    private MatchParam teamAPlayer1;
    private MatchParam teamAPlayer2;
    private MatchParam teamAPlayer3;
    private MatchParam teamAPlayer4;
    private MatchParam teamAPlayer5;
    private MatchParam teamAPlayer6;
    private MatchParam teamAPlayer7;
    private MatchParam teamAPlayer8;
    private MatchParam teamAPlayer9;
    private MatchParam teamAPlayer10;
    private MatchParam teamAPlayer11;
    private MatchParam teamBPlayer1;
    private MatchParam teamBPlayer2;
    private MatchParam teamBPlayer3;
    private MatchParam teamBPlayer4;
    private MatchParam teamBPlayer5;
    private MatchParam teamBPlayer6;
    private MatchParam teamBPlayer7;
    private MatchParam teamBPlayer8;
    private MatchParam teamBPlayer9;
    private MatchParam teamBPlayer10;
    private MatchParam teamBPlayer11;

    public TestCricketMatchParams() {
        super();
        setDefaultParams();
    }

    /*
     * getters and setters come in two flavours - setting and returning the matchParam or the underlying gaussian
     */

    void setInningoneTeamARuns(Gaussian skill) {
        this.inningoneTeamARuns.setGaussian(skill);
    }

    Gaussian getTeamAPlayer1() {
        return teamAPlayer1.getGaussian();
    }

    void setTeamAPlayer1(Gaussian skill) {
        this.teamAPlayer1.setGaussian(skill);
    }

    Gaussian getTeamAPlayer2() {
        return teamAPlayer2.getGaussian();
    }

    void setTeamAPlayer2(Gaussian skill) {
        this.teamAPlayer2.setGaussian(skill);
    }

    Gaussian getTeamAPlayer3() {
        return teamAPlayer3.getGaussian();
    }

    void setTeamAPlayer3(Gaussian skill) {
        this.teamAPlayer3.setGaussian(skill);
    }

    Gaussian getTeamAPlayer4() {
        return teamAPlayer4.getGaussian();
    }

    void setTeamAPlayer4(Gaussian skill) {
        this.teamAPlayer4.setGaussian(skill);
    }

    Gaussian getTeamAPlayer5() {
        return teamAPlayer5.getGaussian();
    }

    void setTeamAPlayer5(Gaussian skill) {
        this.teamAPlayer5.setGaussian(skill);
    }

    Gaussian getTeamAPlayer6() {
        return teamAPlayer6.getGaussian();
    }

    void setTeamAPlayer6(Gaussian skill) {
        this.teamAPlayer6.setGaussian(skill);
    }

    Gaussian getTeamAPlayer7() {
        return teamAPlayer7.getGaussian();
    }

    void setTeamAPlayer7(Gaussian skill) {
        this.teamAPlayer7.setGaussian(skill);
    }

    Gaussian getTeamAPlayer8() {
        return teamAPlayer8.getGaussian();
    }

    void setTeamAPlayer8(Gaussian skill) {
        this.teamAPlayer8.setGaussian(skill);
    }

    Gaussian getTeamAPlayer9() {
        return teamAPlayer9.getGaussian();
    }

    void setTeamAPlayer9(Gaussian skill) {
        this.teamAPlayer9.setGaussian(skill);
    }

    Gaussian getTeamAPlayer10() {
        return teamAPlayer10.getGaussian();
    }

    void setTeamAPlayer10(Gaussian skill) {
        this.teamAPlayer10.setGaussian(skill);
    }

    Gaussian getTeamAPlayer11() {
        return teamAPlayer11.getGaussian();
    }

    void setTeamAPlayer11(Gaussian skill) {
        this.teamAPlayer11.setGaussian(skill);
    }

    Gaussian getTeamBPlayer1() {
        return teamBPlayer1.getGaussian();
    }

    void setTeamBPlayer1(Gaussian skill) {
        this.teamBPlayer1.setGaussian(skill);
    }

    Gaussian getTeamBPlayer2() {
        return teamBPlayer2.getGaussian();
    }

    void setTeamBPlayer2(Gaussian skill) {
        this.teamBPlayer2.setGaussian(skill);
    }

    Gaussian getTeamBPlayer3() {
        return teamBPlayer3.getGaussian();
    }

    void setTeamBPlayer3(Gaussian skill) {
        this.teamBPlayer3.setGaussian(skill);
    }

    Gaussian getTeamBPlayer4() {
        return teamBPlayer4.getGaussian();
    }

    void setTeamBPlayer4(Gaussian skill) {
        this.teamBPlayer4.setGaussian(skill);
    }

    Gaussian getTeamBPlayer5() {
        return teamBPlayer5.getGaussian();
    }

    void setTeamBPlayer5(Gaussian skill) {
        this.teamBPlayer5.setGaussian(skill);
    }

    Gaussian getTeamBPlayer6() {
        return teamBPlayer6.getGaussian();
    }

    void setTeamBPlayer6(Gaussian skill) {
        this.teamBPlayer6.setGaussian(skill);
    }

    Gaussian getTeamBPlayer7() {
        return teamBPlayer7.getGaussian();
    }

    void setTeamBPlayer7(Gaussian skill) {
        this.teamBPlayer7.setGaussian(skill);
    }

    Gaussian getTeamBPlayer8() {
        return teamBPlayer8.getGaussian();
    }

    void setTeamBPlayer8(Gaussian skill) {
        this.teamBPlayer8.setGaussian(skill);
    }

    Gaussian getTeamBPlayer9() {
        return teamBPlayer9.getGaussian();
    }

    void setTeamBPlayer9(Gaussian skill) {
        this.teamBPlayer9.setGaussian(skill);
    }

    Gaussian getTeamBPlayer10() {
        return teamBPlayer10.getGaussian();
    }

    void setTeamBPlayer10(Gaussian skill) {
        this.teamBPlayer10.setGaussian(skill);
    }

    Gaussian getTeamBPlayer11() {
        return teamBPlayer11.getGaussian();
    }

    void setTeamBPlayer11(Gaussian skill) {
        this.teamBPlayer11.setGaussian(skill);
    }

    void setInningoneTeamBRuns(Gaussian skill) {
        this.inningoneTeamBRuns.setGaussian(skill);
    }

    void setInningtwoTeamARuns(Gaussian skill) {
        this.inningtwoTeamARuns.setGaussian(skill);
    }

    void setInningtwoTeamBRuns(Gaussian skill) {
        this.inningtwoTeamBRuns.setGaussian(skill);
    }

    Gaussian getInningoneTeamARuns() {
        return inningoneTeamARuns.getGaussian();
    }

    Gaussian getInningoneTeamBRuns() {
        return inningoneTeamBRuns.getGaussian();
    }

    Gaussian getInningtwoTeamARuns() {
        return inningtwoTeamARuns.getGaussian();
    }

    Gaussian getInningtwoTeamBRuns() {
        return inningtwoTeamBRuns.getGaussian();
    }

    @Override
    public void setDefaultParams(MatchFormat matchFormat) {
        setDefaultParams(); // defaults don't depend on match format for now
    }

    private void setDefaultParams() {

        teamAPlayer1 = new MatchParam(MatchParamType.A, MarketGroup.INDIVIDUAL, 60, 1, 0, 200, false);
        teamAPlayer2 = new MatchParam(MatchParamType.A, MarketGroup.INDIVIDUAL, 45, 1, 0, 200, false);
        teamAPlayer3 = new MatchParam(MatchParamType.A, MarketGroup.INDIVIDUAL, 45, 1, 0, 200, false);
        teamAPlayer4 = new MatchParam(MatchParamType.A, MarketGroup.INDIVIDUAL, 31, 1, 0, 200, false);
        teamAPlayer5 = new MatchParam(MatchParamType.A, MarketGroup.INDIVIDUAL, 31, 1, 0, 200, false);
        teamAPlayer6 = new MatchParam(MatchParamType.A, MarketGroup.INDIVIDUAL, 31, 1, 0, 200, false);
        teamAPlayer7 = new MatchParam(MatchParamType.A, MarketGroup.INDIVIDUAL, 31, 1, 0, 200, false);
        teamAPlayer8 = new MatchParam(MatchParamType.A, MarketGroup.INDIVIDUAL, 14, 1, 0, 200, false);
        teamAPlayer9 = new MatchParam(MatchParamType.A, MarketGroup.INDIVIDUAL, 14, 1, 0, 200, false);
        teamAPlayer10 = new MatchParam(MatchParamType.A, MarketGroup.INDIVIDUAL, 14, 1, 0, 200, false);
        teamAPlayer11 = new MatchParam(MatchParamType.A, MarketGroup.INDIVIDUAL, 4, 1, 0, 200, false);
        teamBPlayer1 = new MatchParam(MatchParamType.B, MarketGroup.INDIVIDUAL, 44.33, 1, 0, 200, false);
        teamBPlayer2 = new MatchParam(MatchParamType.B, MarketGroup.INDIVIDUAL, 44.33, 1, 0, 200, false);
        teamBPlayer3 = new MatchParam(MatchParamType.B, MarketGroup.INDIVIDUAL, 44.33, 1, 0, 200, false);
        teamBPlayer4 = new MatchParam(MatchParamType.B, MarketGroup.INDIVIDUAL, 30.33, 1, 0, 200, false);
        teamBPlayer5 = new MatchParam(MatchParamType.B, MarketGroup.INDIVIDUAL, 30.33, 1, 0, 200, false);
        teamBPlayer6 = new MatchParam(MatchParamType.B, MarketGroup.INDIVIDUAL, 30.33, 1, 0, 200, false);
        teamBPlayer7 = new MatchParam(MatchParamType.B, MarketGroup.INDIVIDUAL, 30.33, 1, 0, 200, false);
        teamBPlayer8 = new MatchParam(MatchParamType.B, MarketGroup.INDIVIDUAL, 14.33, 1, 0, 200, false);
        teamBPlayer9 = new MatchParam(MatchParamType.B, MarketGroup.INDIVIDUAL, 14.33, 1, 0, 200, false);
        teamBPlayer10 = new MatchParam(MatchParamType.B, MarketGroup.INDIVIDUAL, 14.33, 1, 0, 200, false);
        teamBPlayer11 = new MatchParam(MatchParamType.B, MarketGroup.INDIVIDUAL, 4.33, 1, 0, 200, false);
        double totalsA1 = teamAPlayer1.getGaussian().getMean() + teamAPlayer2.getGaussian().getMean()
                        + teamAPlayer3.getGaussian().getMean() + teamAPlayer4.getGaussian().getMean()
                        + teamAPlayer5.getGaussian().getMean() + teamAPlayer6.getGaussian().getMean()
                        + teamAPlayer7.getGaussian().getMean() + teamAPlayer8.getGaussian().getMean()
                        + teamAPlayer9.getGaussian().getMean() + teamAPlayer10.getGaussian().getMean()
                        + teamAPlayer11.getGaussian().getMean();
        double totalsB1 = teamBPlayer1.getGaussian().getMean() + teamBPlayer2.getGaussian().getMean()
                        + teamBPlayer3.getGaussian().getMean() + teamBPlayer4.getGaussian().getMean()
                        + teamBPlayer5.getGaussian().getMean() + teamBPlayer6.getGaussian().getMean()
                        + teamBPlayer7.getGaussian().getMean() + teamBPlayer8.getGaussian().getMean()
                        + teamBPlayer9.getGaussian().getMean() + teamBPlayer10.getGaussian().getMean()
                        + teamAPlayer11.getGaussian().getMean();
        inningoneTeamARuns = new MatchParam(MatchParamType.A, MarketGroup.NOT_SPECIFIED, totalsA1, 1, 0, 700, false);
        inningoneTeamBRuns = new MatchParam(MatchParamType.B, MarketGroup.NOT_SPECIFIED, totalsB1, 1, 0, 700, false);
        inningtwoTeamARuns = new MatchParam(MatchParamType.A, MarketGroup.NOT_SPECIFIED, totalsA1, 1, 0, 700, false);
        inningtwoTeamBRuns = new MatchParam(MatchParamType.B, MarketGroup.NOT_SPECIFIED, totalsB1, 1, 0, 700, false);
        updateParamMap();

    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((inningoneTeamARuns == null) ? 0 : inningoneTeamARuns.hashCode());
        result = prime * result + ((inningoneTeamBRuns == null) ? 0 : inningoneTeamBRuns.hashCode());
        result = prime * result + ((inningtwoTeamARuns == null) ? 0 : inningtwoTeamARuns.hashCode());
        result = prime * result + ((inningtwoTeamBRuns == null) ? 0 : inningtwoTeamBRuns.hashCode());
        result = prime * result + ((teamAPlayer1 == null) ? 0 : teamAPlayer1.hashCode());
        result = prime * result + ((teamAPlayer10 == null) ? 0 : teamAPlayer10.hashCode());
        result = prime * result + ((teamAPlayer11 == null) ? 0 : teamAPlayer11.hashCode());
        result = prime * result + ((teamAPlayer2 == null) ? 0 : teamAPlayer2.hashCode());
        result = prime * result + ((teamAPlayer3 == null) ? 0 : teamAPlayer3.hashCode());
        result = prime * result + ((teamAPlayer4 == null) ? 0 : teamAPlayer4.hashCode());
        result = prime * result + ((teamAPlayer5 == null) ? 0 : teamAPlayer5.hashCode());
        result = prime * result + ((teamAPlayer6 == null) ? 0 : teamAPlayer6.hashCode());
        result = prime * result + ((teamAPlayer7 == null) ? 0 : teamAPlayer7.hashCode());
        result = prime * result + ((teamAPlayer8 == null) ? 0 : teamAPlayer8.hashCode());
        result = prime * result + ((teamAPlayer9 == null) ? 0 : teamAPlayer9.hashCode());
        result = prime * result + ((teamBPlayer1 == null) ? 0 : teamBPlayer1.hashCode());
        result = prime * result + ((teamBPlayer10 == null) ? 0 : teamBPlayer10.hashCode());
        result = prime * result + ((teamBPlayer11 == null) ? 0 : teamBPlayer11.hashCode());
        result = prime * result + ((teamBPlayer2 == null) ? 0 : teamBPlayer2.hashCode());
        result = prime * result + ((teamBPlayer3 == null) ? 0 : teamBPlayer3.hashCode());
        result = prime * result + ((teamBPlayer4 == null) ? 0 : teamBPlayer4.hashCode());
        result = prime * result + ((teamBPlayer5 == null) ? 0 : teamBPlayer5.hashCode());
        result = prime * result + ((teamBPlayer6 == null) ? 0 : teamBPlayer6.hashCode());
        result = prime * result + ((teamBPlayer7 == null) ? 0 : teamBPlayer7.hashCode());
        result = prime * result + ((teamBPlayer8 == null) ? 0 : teamBPlayer8.hashCode());
        result = prime * result + ((teamBPlayer9 == null) ? 0 : teamBPlayer9.hashCode());
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
        TestCricketMatchParams other = (TestCricketMatchParams) obj;
        if (inningoneTeamARuns == null) {
            if (other.inningoneTeamARuns != null)
                return false;
        } else if (!inningoneTeamARuns.equals(other.inningoneTeamARuns))
            return false;
        if (inningoneTeamBRuns == null) {
            if (other.inningoneTeamBRuns != null)
                return false;
        } else if (!inningoneTeamBRuns.equals(other.inningoneTeamBRuns))
            return false;
        if (inningtwoTeamARuns == null) {
            if (other.inningtwoTeamARuns != null)
                return false;
        } else if (!inningtwoTeamARuns.equals(other.inningtwoTeamARuns))
            return false;
        if (inningtwoTeamBRuns == null) {
            if (other.inningtwoTeamBRuns != null)
                return false;
        } else if (!inningtwoTeamBRuns.equals(other.inningtwoTeamBRuns))
            return false;
        if (teamAPlayer1 == null) {
            if (other.teamAPlayer1 != null)
                return false;
        } else if (!teamAPlayer1.equals(other.teamAPlayer1))
            return false;
        if (teamAPlayer10 == null) {
            if (other.teamAPlayer10 != null)
                return false;
        } else if (!teamAPlayer10.equals(other.teamAPlayer10))
            return false;
        if (teamAPlayer11 == null) {
            if (other.teamAPlayer11 != null)
                return false;
        } else if (!teamAPlayer11.equals(other.teamAPlayer11))
            return false;
        if (teamAPlayer2 == null) {
            if (other.teamAPlayer2 != null)
                return false;
        } else if (!teamAPlayer2.equals(other.teamAPlayer2))
            return false;
        if (teamAPlayer3 == null) {
            if (other.teamAPlayer3 != null)
                return false;
        } else if (!teamAPlayer3.equals(other.teamAPlayer3))
            return false;
        if (teamAPlayer4 == null) {
            if (other.teamAPlayer4 != null)
                return false;
        } else if (!teamAPlayer4.equals(other.teamAPlayer4))
            return false;
        if (teamAPlayer5 == null) {
            if (other.teamAPlayer5 != null)
                return false;
        } else if (!teamAPlayer5.equals(other.teamAPlayer5))
            return false;
        if (teamAPlayer6 == null) {
            if (other.teamAPlayer6 != null)
                return false;
        } else if (!teamAPlayer6.equals(other.teamAPlayer6))
            return false;
        if (teamAPlayer7 == null) {
            if (other.teamAPlayer7 != null)
                return false;
        } else if (!teamAPlayer7.equals(other.teamAPlayer7))
            return false;
        if (teamAPlayer8 == null) {
            if (other.teamAPlayer8 != null)
                return false;
        } else if (!teamAPlayer8.equals(other.teamAPlayer8))
            return false;
        if (teamAPlayer9 == null) {
            if (other.teamAPlayer9 != null)
                return false;
        } else if (!teamAPlayer9.equals(other.teamAPlayer9))
            return false;
        if (teamBPlayer1 == null) {
            if (other.teamBPlayer1 != null)
                return false;
        } else if (!teamBPlayer1.equals(other.teamBPlayer1))
            return false;
        if (teamBPlayer10 == null) {
            if (other.teamBPlayer10 != null)
                return false;
        } else if (!teamBPlayer10.equals(other.teamBPlayer10))
            return false;
        if (teamBPlayer11 == null) {
            if (other.teamBPlayer11 != null)
                return false;
        } else if (!teamBPlayer11.equals(other.teamBPlayer11))
            return false;
        if (teamBPlayer2 == null) {
            if (other.teamBPlayer2 != null)
                return false;
        } else if (!teamBPlayer2.equals(other.teamBPlayer2))
            return false;
        if (teamBPlayer3 == null) {
            if (other.teamBPlayer3 != null)
                return false;
        } else if (!teamBPlayer3.equals(other.teamBPlayer3))
            return false;
        if (teamBPlayer4 == null) {
            if (other.teamBPlayer4 != null)
                return false;
        } else if (!teamBPlayer4.equals(other.teamBPlayer4))
            return false;
        if (teamBPlayer5 == null) {
            if (other.teamBPlayer5 != null)
                return false;
        } else if (!teamBPlayer5.equals(other.teamBPlayer5))
            return false;
        if (teamBPlayer6 == null) {
            if (other.teamBPlayer6 != null)
                return false;
        } else if (!teamBPlayer6.equals(other.teamBPlayer6))
            return false;
        if (teamBPlayer7 == null) {
            if (other.teamBPlayer7 != null)
                return false;
        } else if (!teamBPlayer7.equals(other.teamBPlayer7))
            return false;
        if (teamBPlayer8 == null) {
            if (other.teamBPlayer8 != null)
                return false;
        } else if (!teamBPlayer8.equals(other.teamBPlayer8))
            return false;
        if (teamBPlayer9 == null) {
            if (other.teamBPlayer9 != null)
                return false;
        } else if (!teamBPlayer9.equals(other.teamBPlayer9))
            return false;
        return true;
    }

    protected void updateParamMap() {
        super.updateParamMap();
        super.paramMap.put("inningoneTeamARuns", inningoneTeamARuns);
        super.paramMap.put("inningoneTeamBRuns", inningoneTeamBRuns);
        super.paramMap.put("inningtwoTeamARuns", inningtwoTeamARuns);
        super.paramMap.put("inningtwoTeamBRuns", inningtwoTeamBRuns);
        super.paramMap.put("teamAPlayer1", teamAPlayer1);
        super.paramMap.put("teamAPlayer2", teamAPlayer2);
        super.paramMap.put("teamAPlayer3", teamAPlayer3);
        super.paramMap.put("teamAPlayer4", teamAPlayer4);
        super.paramMap.put("teamAPlayer5", teamAPlayer5);
        super.paramMap.put("teamAPlayer6", teamAPlayer6);
        super.paramMap.put("teamAPlayer7", teamAPlayer7);
        super.paramMap.put("teamAPlayer8", teamAPlayer8);
        super.paramMap.put("teamAPlayer9", teamAPlayer9);
        super.paramMap.put("teamAPlayer10", teamAPlayer10);
        super.paramMap.put("teamAPlayer11", teamAPlayer11);
        super.paramMap.put("teamBPlayer1", teamBPlayer1);
        super.paramMap.put("teamBPlayer2", teamBPlayer2);
        super.paramMap.put("teamBPlayer3", teamBPlayer3);
        super.paramMap.put("teamBPlayer4", teamBPlayer4);
        super.paramMap.put("teamBPlayer5", teamBPlayer5);
        super.paramMap.put("teamBPlayer6", teamBPlayer6);
        super.paramMap.put("teamBPlayer7", teamBPlayer7);
        super.paramMap.put("teamBPlayer8", teamBPlayer8);
        super.paramMap.put("teamBPlayer9", teamBPlayer9);
        super.paramMap.put("teamBPlayer10", teamBPlayer10);
        super.paramMap.put("teamBPlayer11", teamBPlayer11);

    }

    @Override
    public String toString() {
        return "TestCricketMatchParams [inningoneTeamARuns=" + inningoneTeamARuns + ", inningoneTeamBRuns="
                        + inningoneTeamBRuns + ", inningtwoTeamARuns=" + inningtwoTeamARuns + ", inningtwoTeamBRuns="
                        + inningtwoTeamBRuns + ", teamAPlayer1=" + teamAPlayer1 + ", teamAPlayer2=" + teamAPlayer2
                        + ", teamAPlayer3=" + teamAPlayer3 + ", teamAPlayer4=" + teamAPlayer4 + ", teamAPlayer5="
                        + teamAPlayer5 + ", teamAPlayer6=" + teamAPlayer6 + ", teamAPlayer7=" + teamAPlayer7
                        + ", teamAPlayer8=" + teamAPlayer8 + ", teamAPlayer9=" + teamAPlayer9 + ", teamAPlayer10="
                        + teamAPlayer10 + ", teamAPlayer11=" + teamAPlayer11 + ", teamBPlayer1=" + teamBPlayer1
                        + ", teamBPlayer2=" + teamBPlayer2 + ", teamBPlayer3=" + teamBPlayer3 + ", teamBPlayer4="
                        + teamBPlayer4 + ", teamBPlayer5=" + teamBPlayer5 + ", teamBPlayer6=" + teamBPlayer6
                        + ", teamBPlayer7=" + teamBPlayer7 + ", teamBPlayer8=" + teamBPlayer8 + ", teamBPlayer9="
                        + teamBPlayer9 + ", teamBPlayer10=" + teamBPlayer10 + ", teamBPlayer11=" + teamBPlayer11 + "]";
    }

}
