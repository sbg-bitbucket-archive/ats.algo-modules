package ats.algo.sport.basketball;

import ats.algo.core.baseclasses.MatchFormat;
import ats.algo.core.baseclasses.MatchParams;
import ats.algo.core.baseclasses.AlgoMatchState;
import ats.algo.core.common.TeamId;
import ats.algo.genericsupportfunctions.RandomNoGenerator;
import ats.algo.montecarloframework.MonteCarloMatch;
import ats.algo.sport.basketball.BasketballMatchIncident.BasketballMatchIncidentType;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class BasketballMatch extends MonteCarloMatch {

    /**
     * @author Jin
     * 
     */
    private static final int timeSliceSecs = 5;// 2
    private BasketballMatchState simMatchState;
    private BasketballMatchFacts matchFacts;
    private BasketballMatchIncident matchIncident;
    private boolean isFourtyMinutiesMatch;
    private boolean isTwoHalvesFormat;
    @SuppressWarnings("unused")
    private int normalQuarterIterNo;
    @SuppressWarnings("unused")
    private int overTimeIterNo;
    @SuppressWarnings("unused")
    private double[][] coef_;
    @SuppressWarnings("unused")
    private double[] intercept_;

    public BasketballMatch(BasketballMatchFormat matchFormat, BasketballMatchState matchState,
                    BasketballMatchParams matchParams) {

        super((MatchFormat) matchFormat, (AlgoMatchState) matchState, (MatchParams) matchParams);
        monteCarloMarkets = new BasketballMatchMarketsFactory(matchState);
        this.simMatchState = (BasketballMatchState) matchState.copy();
        /*
         * create the container for holding match facts just once rather than every time playMatch is executed -
         * improves performance
         */
        this.matchFacts = new BasketballMatchFacts();
        this.matchIncident = new BasketballMatchIncident();
        this.isFourtyMinutiesMatch = simMatchState.isFourtyMinutiesMatch();
        this.isTwoHalvesFormat = simMatchState.isTwoHalvesFormat();

        this.normalQuarterIterNo = currentPeriodIterationNO(BasketballMatchPeriod.PREMATCH, isFourtyMinutiesMatch,
                        isTwoHalvesFormat);
        this.overTimeIterNo = currentPeriodIterationNO(BasketballMatchPeriod.IN_EXTRA_TIME, isFourtyMinutiesMatch,
                        isTwoHalvesFormat);
        JSONObject myJson = getModelData();
        this.coef_ = getModelCoefFromJson(myJson);
        this.intercept_ = getModelInterceptFromJson(myJson);

    }

    @Override
    public MonteCarloMatch clone() {
        BasketballMatch cc = new BasketballMatch((BasketballMatchFormat) matchFormat, (BasketballMatchState) matchState,
                        (BasketballMatchParams) matchParams);
        return cc;
    }

    @Override
    public void resetStatistics() {
        monteCarloMarkets = new BasketballMatchMarketsFactory((BasketballMatchState) matchState);
    }

    @SuppressWarnings("unused")
    private BasketballMatchFormat getBasketballMatchFormat() {
        return (BasketballMatchFormat) matchFormat;
    }

    @Override
    public void playMatch() {
        BasketballMatchState startingMatchState = (BasketballMatchState) matchState;
        int startingPeriodNo = startingMatchState.getPeriodNo();
        simMatchState.setEqualTo(matchState);
        String[] freeThrowStatusActural = simMatchState.getFreeThrowStatus();
        boolean atFreeThrowRealMatch = !(freeThrowStatusActural)[0].equals("UNKNOWN");

        if (simMatchState.getMatchPeriod() == BasketballMatchPeriod.PREMATCH) {
            if (simMatchState.isTwoHalvesFormat()) {
                simMatchState.setMatchPeriod(BasketballMatchPeriod.IN_FIRST_HALF);
            } else {
                simMatchState.setMatchPeriod(BasketballMatchPeriod.IN_FIRST_QUARTER);
            }
        }
        BasketballMatchParams basketballMatchParams = (BasketballMatchParams) matchParams;


        double[] timeDistribution = new double[1];
        if (isFourtyMinutiesMatch) {
            timeDistribution = getEUROPointsDistn();
        } else {
            timeDistribution = getNBAPointsDistn();
        }


        double scoreToTal = basketballMatchParams.getPace().getMean();
        double scoreDiff = basketballMatchParams.getAdv().getMean();
        double simHomeScoreRate = (scoreToTal + scoreDiff) / 2;
        double simAwayScoreRate = (scoreToTal - scoreDiff) / 2;

        // double adjParam = basketballMatchParams.getAdjRate().getMean();

        matchFacts.reset(((BasketballMatchState) matchState).getCurrentPeriodGoalsA(),
                        ((BasketballMatchState) matchState).getCurrentPeriodGoalsB(),
                        ((BasketballMatchState) matchState).getPeriodNo());


        do {
            @SuppressWarnings("unused")
            BasketballMatchPeriod simMatchPeriod = simMatchState.getMatchPeriod();
            int simMatchPeriodNo = simMatchState.getPeriodNo();

            String[] freeThrowStatusSimulation = {"UNKNOWN", "0", "0"};
            if (atFreeThrowRealMatch)
                freeThrowStatusSimulation = simMatchState.getFreeThrowStatus();

            boolean freeShootIncidentOccurred = false;

            int sliceIdx = simMatchState.getElapsedTimeSecs() / timeSliceSecs;
            if (sliceIdx > 579) {
                sliceIdx = 579;
            } else if (isFourtyMinutiesMatch && sliceIdx > 479) {
                sliceIdx = 479;
            }
            if (sliceIdx > timeDistribution.length - 1) {
                sliceIdx = timeDistribution.length - 1;
            }
            double timeBasedFactor = timeDistribution[sliceIdx];

            // injure time
            double homeScoreRate = (simHomeScoreRate) * timeBasedFactor;
            double awayScoreRate = (simAwayScoreRate) * timeBasedFactor;
            double rh = RandomNoGenerator.nextDouble();



            // get the scoring play point distns
            // //double[] transStateDistn = getPointScoreDistribution(homeScoreRate, awayScoreRate);
            // double qtr1 = 0.0, qtr2 = 0.0, qtr3 = 0.0, qtr4 = 0.0;
            // int thisSimQuarter = simMatchState.getPeriodNo();
            // switch(thisSimQuarter)
            // {
            // case 1: qtr1 = 1.0;
            // break;
            // case 2: qtr2 = 1.0;
            // break;
            // case 3: qtr3 = 1.0;
            // break;
            // case 4: qtr4 = 1.0;
            // break;
            // }
            // double[] params = new double[]
            // {
            // simMatchState.getElapsedTimeSecs(),
            // simMatchState.getPointsA() - simMatchState.getPointsB(),
            // ((BasketballMatchParams) matchParams).getPace().getMean(),
            // ((BasketballMatchParams) matchParams).getAdv().getMean(),
            // qtr1,
            // qtr2,
            // qtr3,
            // qtr4
            //
            // };
            //
            // double[] transStateDistn = getModelProbabilities(coef_, intercept_, params);

            if (simMatchState.getPointsA() > simMatchState.getPointsB()) {
                homeScoreRate *= (1.0 + ((BasketballMatchParams) matchParams).getALoseBoost().getMean());
                awayScoreRate *= (1.0 + ((BasketballMatchParams) matchParams).getBLoseBoost().getMean());
            }
            double[] transStateDistn = getPointScoreDistribution(homeScoreRate, awayScoreRate);

            /*
             * Ball Position Info
             */
            // if (simMatchState.getBallPosition().getBallHoldingTeam() != TeamId.UNKNOWN) {
            // if (simMatchState.getBallPosition().getBallHoldingTeam() == TeamId.A) {
            // awayScoreRate = 0;
            // } else {
            // homeScoreRate = 0;
            // }
            // resetBallPosition = true;
            // }

            if ((freeThrowStatusSimulation)[0].equals("UNKNOWN")) {
                boolean incidentOccurred = false;
                // if (rh < homeScoreRate) {
                // matchIncident.setIncidentSubType(BasketballMatchIncidentType.POINT_SCORED);
                // matchIncident.setTeamId(TeamId.A);
                // incidentOccurred = true;
                //
                // if (simMatchPeriodNo == startingPeriodNo)
                // matchFacts.addScoreCurrentPeriod(TeamId.A);
                //
                // } else if (rh < (awayScoreRate + homeScoreRate)) {
                // matchIncident.setIncidentSubType(BasketballMatchIncidentType.POINT_SCORED);
                // matchIncident.setTeamId(TeamId.B);
                // incidentOccurred = true;
                //
                // if (simMatchPeriodNo == startingPeriodNo)
                // matchFacts.addScoreCurrentPeriod(TeamId.B);
                // }
                // if (incidentOccurred) {
                // matchIncident.setElapsedTime(simMatchState.getElapsedTimeSecs());
                // simMatchState.updateStateForIncident(matchIncident, false);
                // }

                // nothing happened
                if (rh < transStateDistn[0]) {

                }
                // a team points moves
                else if (rh < transStateDistn[1]) {
                    matchIncident.setIncidentSubType(BasketballMatchIncidentType.TWO_POINTS_SCORED);
                    matchIncident.setTeamId(TeamId.A);
                    incidentOccurred = true;

                    if (simMatchPeriodNo == startingPeriodNo)
                        matchFacts.addScoreCurrentPeriod(TeamId.A, 2);
                } else if (rh < transStateDistn[2]) {
                    matchIncident.setIncidentSubType(BasketballMatchIncidentType.THREE_POINTS_SCORED);
                    matchIncident.setTeamId(TeamId.A);
                    incidentOccurred = true;

                    if (simMatchPeriodNo == startingPeriodNo)
                        matchFacts.addScoreCurrentPeriod(TeamId.A, 3);
                } else if (rh < transStateDistn[3]) {
                    matchIncident.setIncidentSubType(BasketballMatchIncidentType.POINT_SCORED);
                    matchIncident.setTeamId(TeamId.A);
                    incidentOccurred = true;

                    if (simMatchPeriodNo == startingPeriodNo)
                        matchFacts.addScoreCurrentPeriod(TeamId.A, 1);
                }

                // b team points moves
                else if (rh < transStateDistn[4]) {
                    matchIncident.setIncidentSubType(BasketballMatchIncidentType.TWO_POINTS_SCORED);
                    matchIncident.setTeamId(TeamId.B);
                    incidentOccurred = true;

                    if (simMatchPeriodNo == startingPeriodNo)
                        matchFacts.addScoreCurrentPeriod(TeamId.B, 2);
                } else if (rh < transStateDistn[5]) {
                    matchIncident.setIncidentSubType(BasketballMatchIncidentType.THREE_POINTS_SCORED);
                    matchIncident.setTeamId(TeamId.B);
                    incidentOccurred = true;

                    if (simMatchPeriodNo == startingPeriodNo)
                        matchFacts.addScoreCurrentPeriod(TeamId.B, 3);
                } else if (rh < transStateDistn[6]) {
                    matchIncident.setIncidentSubType(BasketballMatchIncidentType.POINT_SCORED);
                    matchIncident.setTeamId(TeamId.B);
                    incidentOccurred = true;

                    if (simMatchPeriodNo == startingPeriodNo)
                        matchFacts.addScoreCurrentPeriod(TeamId.B, 1);
                }
                if (incidentOccurred) {
                    matchIncident.setElapsedTime(simMatchState.getElapsedTimeSecs());
                    simMatchState.updateStateForIncident(matchIncident, false);
                }


            } else {
                freeShootIncidentOccurred = true;
                homeScoreRate = timeBasedFactor;
                awayScoreRate = timeBasedFactor;
                matchIncident.setElapsedTime(simMatchState.getElapsedTimeSecs());
                if (freeThrowStatusSimulation[0].equals("H")) {
                    matchIncident.setTeamId(TeamId.A);
                    if (rh < homeScoreRate) {
                        matchIncident.setIncidentSubType(BasketballMatchIncidentType.FIRST_FREETHROW_SCORED);
                        if (simMatchPeriodNo == startingPeriodNo)
                            matchFacts.addScoreCurrentPeriod(TeamId.A);
                    } else {
                        matchIncident.setIncidentSubType(BasketballMatchIncidentType.FIRST_FREETHROW_MISSED);
                    }

                } else if (freeThrowStatusSimulation[0].equals("A")) {
                    matchIncident.setTeamId(TeamId.B);
                    if (rh < awayScoreRate) {
                        matchIncident.setIncidentSubType(BasketballMatchIncidentType.FIRST_FREETHROW_SCORED);
                        if (simMatchPeriodNo == startingPeriodNo)
                            matchFacts.addScoreCurrentPeriod(TeamId.B);
                    } else {
                        matchIncident.setIncidentSubType(BasketballMatchIncidentType.FIRST_FREETHROW_MISSED);
                    }
                } else {
                    throw new IllegalArgumentException("Should not go here, Freethrow simulation in Basketball");
                }
                simMatchState.updateStateForIncident(matchIncident, false);
            }
            if (!freeShootIncidentOccurred) {
                // if (resetBallPosition) {
                // simMatchState.getBallPosition().resetBallPosition();
                // }
                simMatchState.incrementSimulationElapsedTime(timeSliceSecs);
            }
        } while (simMatchState.getMatchPeriod() != BasketballMatchPeriod.MATCH_COMPLETED);

        //
        // int totalSlice = 0;
        // int maxPoints = 200;
        // if(this.isFourtyMinutiesMatch) {
        // totalSlice = 40 * (60 / timeSliceSecs);
        // }
        // else {
        // totalSlice = 48 * (60 / timeSliceSecs);
        // }
        // double[][][] probMatrix = new double[totalSlice][][];
        //
        // for(int i = 0; i < totalSlice; i++) {
        // probMatrix[i] = new double[maxPoints][];
        // for(int j = 0; j < maxPoints; j++) {
        // probMatrix[i][j] = new double[maxPoints];
        // }
        // }
        //
        //
        // int elapsedSecs = ((BasketballMatchState) matchState).getElapsedTimeSecs();
        // int currentSlice = elapsedSecs / timeSliceSecs;
        // int currentAPoints = ((BasketballMatchState) matchState).getPointsA();
        // int currentBPoints = ((BasketballMatchState) matchState).getPointsB();
        //
        // probMatrix[currentSlice][currentAPoints][currentBPoints] = 1.0;
        //// double scoreToTal = basketballMatchParams.getPace().getMean();
        //// double scoreDiff = basketballMatchParams.getAdv().getMean();
        //// double simHomeScoreRate = (scoreToTal + scoreDiff) / 2;
        //// double simAwayScoreRate = (scoreToTal - scoreDiff) /
        ////
        // double[] distribution = getNBAPointsDistn();
        // double alb = ((BasketballMatchParams) matchParams).getALoseBoost().getMean();
        // double blb = ((BasketballMatchParams) matchParams).getBLoseBoost().getMean();
        //
        // for(int t = currentSlice + 1; t < totalSlice; t++) {
        // double aExpectancy = distribution[t] * simHomeScoreRate;
        // double bExpectancy = distribution[t] * simAwayScoreRate;
        //
        // //double[] transDistn = getPointScoreDistribution(aExpectancy, bExpectancy);
        //
        //
        // for(int ap = currentAPoints; ap < maxPoints - 4; ap++)
        // {
        // for(int bp = currentBPoints; bp < maxPoints - 4; bp++)
        // {
        // if(ap < bp) {
        // aExpectancy *= (1.0 + alb);
        // }
        // else if(bp < ap) {
        // bExpectancy *= (1.0 + blb);
        // }
        //
        // double[] transDistn = getPointScoreDistribution(aExpectancy, bExpectancy);
        //
        // probMatrix[t][ap + 2][bp] += probMatrix[t - 1][ap][bp] * transDistn[1];
        // probMatrix[t][ap + 3][bp] += probMatrix[t - 1][ap][bp] * transDistn[2];
        // probMatrix[t][ap + 1][bp] += probMatrix[t - 1][ap][bp] * transDistn[3];
        //
        // probMatrix[t][ap][bp + 2] += probMatrix[t - 1][ap][bp] * transDistn[4];
        // probMatrix[t][ap][bp + 3] += probMatrix[t - 1][ap][bp] * transDistn[5];
        // probMatrix[t][ap][bp + 1] += probMatrix[t - 1][ap][bp] * transDistn[6];
        //
        // probMatrix[t][ap][bp] += probMatrix[t - 1][ap][bp] * transDistn[0];
        // }
        // }
        //


        ((BasketballMatchMarketsFactory) monteCarloMarkets).updateStats(simMatchState, matchFacts);
    }

    // @SuppressWarnings("static-access")
    // @Override
    // public void playMatch() {
    // BasketballMatchState startingMatchState = (BasketballMatchState) matchState;
    // int startingPeriodNo = startingMatchState.getPeriodNo();
    // simMatchState.setEqualTo(matchState);
    // String[] freeThrowStatusActural = simMatchState.getFreeThrowStatus();
    // boolean atFreeThrowRealMatch = !(freeThrowStatusActural)[0].equals("UNKNOWN");
    //
    // if (simMatchState.getMatchPeriod() == BasketballMatchPeriod.PREMATCH) {
    // if (simMatchState.isTwoHalvesFormat()) {
    // simMatchState.setMatchPeriod(BasketballMatchPeriod.IN_FIRST_HALF);
    // } else {
    // simMatchState.setMatchPeriod(BasketballMatchPeriod.IN_FIRST_QUARTER);
    // }
    // }
    // BasketballMatchParams basketballMatchParams = (BasketballMatchParams) matchParams;
    //
    // double scoreToTal = basketballMatchParams.getPace().getMean();
    // double scoreDiff = basketballMatchParams.getAdv().getMean();
    // //double simHomeScoreRate = (scoreToTal + scoreDiff) / 2;
    // //double simAwayScoreRate = (scoreToTal - scoreDiff) / 2;
    //
    // //double adjParam = basketballMatchParams.getAdjRate().getMean();
    //
    // matchFacts.reset(((BasketballMatchState) matchState).getCurrentPeriodGoalsA(),
    // ((BasketballMatchState) matchState).getCurrentPeriodGoalsB(),
    // ((BasketballMatchState) matchState).getPeriodNo());
    // /*
    // * Expected points difference for the current period
    // */
    // int totalSlice = 0;
    // int maxPoints = 200;
    // if(this.isFourtyMinutiesMatch) {
    // totalSlice = 40 * (60 / timeSliceSecs);
    // }
    // else {
    // totalSlice = 48 * (60 / timeSliceSecs);
    // }
    // double[][][] probMatrix = new double[totalSlice][][];
    //
    // for(int i = 0; i < totalSlice; i++) {
    // probMatrix[i] = new double[maxPoints][];
    // for(int j = 0; j < maxPoints; j++) {
    // probMatrix[i][j] = new double[maxPoints];
    // }
    // }
    //
    //
    // int elapsedSecs = ((BasketballMatchState) matchState).getElapsedTimeSecs();
    // int currentSlice = elapsedSecs / timeSliceSecs;
    // int currentAPoints = ((BasketballMatchState) matchState).getPointsA();
    // int currentBPoints = ((BasketballMatchState) matchState).getPointsB();
    //
    // probMatrix[currentSlice][currentAPoints][currentBPoints] = 1.0;
    //
    // for(int t = currentSlice; t < totalSlice; t++) {
    // for(int ap = currentAPoints; ap < maxPoints; ap++) {
    // for(int bp = currentBPoints; bp < maxPoints; bp++) {
    //
    // }
    // }
    //
    // }
    //
    // simMatchState.setMatchPeriod(BasketballMatchPeriod.MATCH_COMPLETED);
    //
    //
    // ((BasketballMatchMarketsFactory) monteCarloMarkets).updateStats(simMatchState, matchFacts);
    // }

    private int currentPeriodIterationNO(BasketballMatchPeriod matchPeriod, boolean fourtyMinutiesMatch,
                    boolean twoHalvesFormat) {
        int iterationNO;
        if (!matchPeriod.equals(BasketballMatchPeriod.IN_EXTRA_TIME))
            if (fourtyMinutiesMatch) {
                if (twoHalvesFormat)
                    iterationNO = 1200 / timeSliceSecs;
                iterationNO = 600 / timeSliceSecs;
            } else {
                iterationNO = 1440 / timeSliceSecs;
                iterationNO = 720 / timeSliceSecs;
            }
        else {
            iterationNO = 300 / timeSliceSecs;
        }

        return iterationNO;
    }

    private double[] getNBAPointsDistn() {
        return new double[] {5.63713110614041E-06, 7.55375568222816E-05, 0.000660671765639657, 0.00200005411645862,
                0.00157952413594054, 0.000962821992928783, 0.00136305830146475, 0.0016821199220723, 0.00167986506962984,
                0.00154006421819756, 0.00149045746446353, 0.001646042282993, 0.0016099646439137, 0.00164378743055054,
                0.00173398152824879, 0.0015738870048344, 0.001710305577603, 0.0016900119056209, 0.00157050472617072,
                0.00180839165884984, 0.00158516126704668, 0.00181515621617721, 0.00168324734829353, 0.00170466844649686,
                0.00171368785626669, 0.00181515621617721, 0.00172270726603651, 0.00172947182336388, 0.00176329461000072,
                0.00182755790461072, 0.00187716465834476, 0.00166633595497511, 0.00180613680640739, 0.00172383469225774,
                0.00182304819972581, 0.00175878490511581, 0.00185574356014142, 0.00184446929792914, 0.00175991233133704,
                0.00187152752723862, 0.00175314777400967, 0.00186701782235371, 0.00174863806912476, 0.00184221444548669,
                0.00176442203622195, 0.00183544988815932, 0.00173961865935493, 0.00173285410202756, 0.00185799841258388,
                0.00172721697092142, 0.00184785157659283, 0.00186363554369002, 0.00182304819972581, 0.00186589039613248,
                0.0017824608557616, 0.0017824608557616, 0.00178358828198283, 0.00185236128147774, 0.0018027545277437,
                0.0018185384948409, 0.00176780431488563, 0.00176667688866441, 0.00188392921567213, 0.00175314777400967,
                0.00186138069124756, 0.00179598997041634, 0.00185010642903528, 0.00177231401977055, 0.00185010642903528,
                0.00186476296991125, 0.00171594270870914, 0.00187378237968107, 0.00173961865935493, 0.00180500938018616,
                0.00172045241359405, 0.00178471570820406, 0.00185574356014142, 0.00172496211847897, 0.00173059924958511,
                0.00183206760949563, 0.00172157983981528, 0.00174074608557616, 0.00178584313442528, 0.00174525579046107,
                0.00177344144599177, 0.00172608954470019, 0.00181402878995599, 0.0018106465112923, 0.00171481528248791,
                0.00177682372465546, 0.00177569629843423, 0.00172834439714265, 0.00177682372465546, 0.00174863806912476,
                0.00178358828198283, 0.00174300093801862, 0.0018185384948409, 0.00189858575654809, 0.00182643047838949,
                0.00168775705317844, 0.00190760516631792, 0.00184108701926546, 0.00172608954470019, 0.00174300093801862,
                0.00177231401977055, 0.00175878490511581, 0.00181966592106213, 0.00184108701926546, 0.00174751064290353,
                0.00184446929792914, 0.00163025831589581, 0.0017181975611516, 0.00174300093801862, 0.00180951908507107,
                0.00175089292156721, 0.00168550220073598, 0.00166520852875388, 0.00179937224908002, 0.00163364059455949,
                0.00169452161050581, 0.00158741611948914, 0.00185574356014142, 0.00173172667580634, 0.00172608954470019,
                0.00169452161050581, 0.00180613680640739, 0.0017384912331337, 0.00183206760949563, 0.00183094018327441,
                0.00173172667580634, 0.00176329461000072, 0.00182530305216827, 0.00211505159102388, 0.00185348870769897,
                0.00200230896890108, 0.00197299588714914, 0.00197863301825529, 0.00242622122808283, 0.00211505159102388,
                0.0013529114654737, 0.00101242874666282, 0.00098198823868966, 0.00139349880943791, 0.00335522043437477,
                0.00144761526805686, 8.34295403708781E-05, 0.00123002200735984, 0.00211730644346634,
                0.00157839670971932, 0.00116237643408615, 0.0013089418428458, 0.00169226675806335, 0.00163364059455949,
                0.00161560177501984, 0.00161560177501984, 0.00159192582437405, 0.00170579587271809, 0.00164716970921423,
                0.00163702287322318, 0.00174863806912476, 0.00164942456165669, 0.00159981780792265, 0.00155810303773721,
                0.00172496211847897, 0.00157614185727686, 0.00189971318276932, 0.00160658236525002, 0.00168099249585107,
                0.00173059924958511, 0.00173623638069125, 0.00172608954470019, 0.0016900119056209, 0.001774568872213,
                0.00167310051230248, 0.00178471570820406, 0.0018027545277437, 0.0016381502994444, 0.00167648279096616,
                0.00175878490511581, 0.00175540262645213, 0.00173059924958511, 0.00177005916732809, 0.00174863806912476,
                0.00157163215239195, 0.00159869038170142, 0.00168437477451476, 0.00167197308608125, 0.00166633595497511,
                0.00168662962695721, 0.00169564903672704, 0.00174300093801862, 0.00171143300382423, 0.00166520852875388,
                0.00180388195396493, 0.00185912583880511, 0.00160770979147125, 0.00174187351179739, 0.00174187351179739,
                0.00166182625009019, 0.00168324734829353, 0.00181290136373476, 0.00167873764340862, 0.00176442203622195,
                0.00176780431488563, 0.00167873764340862, 0.00171707013493037, 0.00178922541308897, 0.00179260769175265,
                0.00186701782235371, 0.00186701782235371, 0.00169564903672704, 0.00171368785626669, 0.00168775705317844,
                0.0017824608557616, 0.00179824482285879, 0.00186927267479616, 0.00166069882386897, 0.00178922541308897,
                0.00177569629843423, 0.00174412836423984, 0.00161560177501984, 0.00173510895447002, 0.00182192077350458,
                0.00165280684032037, 0.00171707013493037, 0.00171368785626669, 0.00178809798686774, 0.00162800346345335,
                0.00179486254419511, 0.00186250811746879, 0.00183544988815932, 0.0018027545277437, 0.00183319503571686,
                0.00179260769175265, 0.00166633595497511, 0.00179598997041634, 0.00177569629843423, 0.00186927267479616,
                0.00183432246193809, 0.00189971318276932, 0.00187716465834476, 0.00182755790461072, 0.00189633090410564,
                0.0016742279385237, 0.00177795115087669, 0.00189745833032686, 0.00194368280539721, 0.0018185384948409,
                0.00183206760949563, 0.00189407605166318, 0.00184221444548669, 0.00182530305216827, 0.00184559672415037,
                0.00182643047838949, 0.00187152752723862, 0.00177907857709791, 0.00187829208456599, 0.00189182119922072,
                0.00169452161050581, 0.00178584313442528, 0.00185236128147774, 0.00174863806912476, 0.00181741106861967,
                0.00178020600331914, 0.00185912583880511, 0.00177005916732809, 0.0018185384948409, 0.00185236128147774,
                0.00186589039613248, 0.00196397647737932, 0.00189294862544195, 0.00200005411645862, 0.00186814524857493,
                0.00182981275705318, 0.00201696550977704, 0.00186927267479616, 0.00230333176996897, 0.00192789883830002,
                0.00216578577097915, 0.00210152247636915, 0.00220186341005845, 0.00266636301320442, 0.00231347860596003,
                0.001324725809943, 0.000951547730716502, 0.00106090807417563, 0.0016099646439137, 0.00357506854751425,
                0.00154006421819756, 9.24489501407028E-05, 0.00117139584385598, 0.00207671909950213,
                0.00157501443105563, 0.00106541777906054, 0.00123340428602352, 0.00167197308608125, 0.00152878995598528,
                0.00159530810303774, 0.00146903636626019, 0.00158741611948914, 0.00164829713543546, 0.00166633595497511,
                0.00154231907064002, 0.00172721697092142, 0.00160770979147125, 0.00168888447939967, 0.00167986506962984,
                0.00162462118478967, 0.00172496211847897, 0.00177907857709791, 0.00166971823363879, 0.00168888447939967,
                0.00164153257810809, 0.00175540262645213, 0.00171143300382423, 0.00160658236525002, 0.00173510895447002,
                0.00179260769175265, 0.00185236128147774, 0.00169452161050581, 0.00180726423262862, 0.00165844397142651,
                0.00168662962695721, 0.00175765747889458, 0.00171368785626669, 0.00176216718377949, 0.001710305577603,
                0.00186701782235371, 0.00183770474060178, 0.00158065156216177, 0.00166295367631142, 0.00172608954470019,
                0.00178809798686774, 0.00179598997041634, 0.00179937224908002, 0.0018106465112923, 0.00170466844649686,
                0.001774568872213, 0.00163589544700195, 0.00174751064290353, 0.0017903528393102, 0.00174976549534598,
                0.00174074608557616, 0.00169903131539072, 0.00174976549534598, 0.00182643047838949, 0.00166295367631142,
                0.00175202034778844, 0.0016821199220723, 0.00170692329893932, 0.001774568872213, 0.00183657731438055,
                0.00175540262645213, 0.00161672920124107, 0.00173285410202756, 0.00176554946244318, 0.00172270726603651,
                0.00169790388916949, 0.00177569629843423, 0.00173623638069125, 0.00173285410202756, 0.00167535536474493,
                0.00170015874161195, 0.00171932498737283, 0.00174187351179739, 0.00167873764340862, 0.00177682372465546,
                0.00169452161050581, 0.0016257486110109, 0.00167986506962984, 0.00172947182336388, 0.00173623638069125,
                0.0017384912331337, 0.00178358828198283, 0.00175991233133704, 0.00172947182336388, 0.00170354102027563,
                0.00173285410202756, 0.00173172667580634, 0.00176329461000072, 0.00171932498737283, 0.00185687098636265,
                0.00169677646294826, 0.0018027545277437, 0.00169903131539072, 0.00178584313442528, 0.00177795115087669,
                0.00185574356014142, 0.00178697056064651, 0.00173059924958511, 0.00178922541308897, 0.00181290136373476,
                0.00183544988815932, 0.0018106465112923, 0.00170692329893932, 0.00179824482285879, 0.00177569629843423,
                0.00176216718377949, 0.00169677646294826, 0.00160545493902879, 0.0017181975611516, 0.0017463832166823,
                0.00168437477451476, 0.00191549714986651, 0.00178697056064651, 0.00176780431488563, 0.00159079839815282,
                0.00167535536474493, 0.00169339418428458, 0.00168888447939967, 0.00170917815138177, 0.00178358828198283,
                0.00165167941409914, 0.001774568872213, 0.00174976549534598, 0.00179598997041634, 0.00179486254419511,
                0.00179937224908002, 0.00175314777400967, 0.00172608954470019, 0.00213309041056353, 0.00184334187170792,
                0.00201245580489213, 0.00185010642903528, 0.00204627859152897, 0.00245778916227722, 0.0021759326069702,
                0.0014453604156144, 0.00101693845154773, 0.00103610469730861, 0.00148031062847247, 0.00328644743487986,
                0.00142281189118984, 0.000104850638574212, 0.00119732664694422, 0.00192226170719388,
                0.00151526084133054, 0.00124129626957212, 0.0015254076773216, 0.00173059924958511, 0.00156712244750704,
                0.00144987012049931, 0.00157952413594054, 0.00151526084133054, 0.00165957139764774, 0.00156937729994949,
                0.00165957139764774, 0.00169226675806335, 0.00168775705317844, 0.00164491485677177, 0.00158065156216177,
                0.00156937729994949, 0.00168775705317844, 0.00161672920124107, 0.00165280684032037, 0.001646042282993,
                0.001646042282993, 0.00164153257810809, 0.00173398152824879, 0.00160320008658633, 0.00163702287322318,
                0.00158741611948914, 0.00164829713543546, 0.00163364059455949, 0.00173285410202756, 0.0016178566274623,
                0.00174187351179739, 0.00157726928349809, 0.00170692329893932, 0.00161560177501984, 0.00163251316833826,
                0.00172721697092142, 0.00156486759506458, 0.00153442708709142, 0.00159981780792265, 0.00159643552925897,
                0.00165506169276283, 0.00164491485677177, 0.00152315282487914, 0.00155697561151598, 0.00161560177501984,
                0.00164378743055054, 0.00151864311999423, 0.00166971823363879, 0.00158741611948914, 0.00158741611948914,
                0.00169113933184212, 0.00174074608557616, 0.00161672920124107, 0.00170241359405441, 0.00162462118478967,
                0.00167310051230248, 0.00186363554369002, 0.00163702287322318, 0.00164491485677177, 0.00172947182336388,
                0.0016178566274623, 0.00159643552925897, 0.00173398152824879, 0.00162687603723212, 0.00163138574211704,
                0.00175765747889458, 0.00175765747889458, 0.00174300093801862, 0.00159981780792265, 0.00186250811746879,
                0.0015896709719316, 0.0016900119056209, 0.00157614185727686, 0.0017384912331337, 0.0015096237102244,
                0.00159079839815282, 0.00169452161050581, 0.00159756295548019, 0.00171256043004546, 0.00165167941409914,
                0.00168550220073598, 0.00170466844649686, 0.00158290641460423, 0.00169452161050581, 0.00174863806912476,
                0.00172947182336388, 0.00172383469225774, 0.00167310051230248, 0.00169452161050581, 0.00164829713543546,
                0.00168888447939967, 0.001646042282993, 0.00172383469225774, 0.00171143300382423, 0.00171481528248791,
                0.00161109207013493, 0.00170241359405441, 0.00178697056064651, 0.00169790388916949, 0.0016178566274623,
                0.0018467241503716, 0.00166520852875388, 0.00167986506962984, 0.00173961865935493, 0.00175540262645213,
                0.00174525579046107, 0.00177231401977055, 0.00166295367631142, 0.00179937224908002, 0.00166408110253265,
                0.00153780936575511, 0.00172157983981528, 0.00164491485677177, 0.00169339418428458, 0.00160432751280756,
                0.00157614185727686, 0.00192902626452125, 0.00172157983981528, 0.00176893174110686, 0.00192338913341511,
                0.00176442203622195, 0.00189971318276932, 0.00188956634677827, 0.00188054693700844, 0.00187603723212353,
                0.00179598997041634, 0.00193804567429107, 0.00183770474060178, 0.00207333682083844, 0.00205191572263511,
                0.00214098239411213, 0.00206318998484739, 0.00225597986867739, 0.0025051410635688, 0.00253445414532073,
                0.00272273432426582, 0.00306547189551916, 0.00317821451764197, 0.00310831409192582, 0.00278587019265459,
                0.00069336712605527};
    }

    private double[] getEUROPointsDistn() {
        return new double[] {0.001306987173, 0.002003999437, 0.002457021738, 0.002223733089, 0.002011133647,
                0.002227300193, 0.002177360727, 0.002038957064, 0.002085329425, 0.002041810747, 0.002023975224,
                0.002041097327, 0.002048944957, 0.00216451915, 0.002104591791, 0.002040383906, 0.00210387837,
                0.002143829943, 0.00212670784, 0.002104591791, 0.002148823889, 0.002095317318, 0.002127421261,
                0.002185921779, 0.002206610986, 0.002130988366, 0.002131701787, 0.002147397048, 0.002238001508,
                0.002098171002, 0.002188062041, 0.002207324407, 0.002173080202, 0.002220879405, 0.002166659413,
                0.002282233606, 0.002130274945, 0.002158811783, 0.002182354674, 0.002260830978, 0.002160952045,
                0.002211604933, 0.002258690715, 0.002145970206, 0.002140262838, 0.002130988366, 0.002175933885,
                0.002168799676, 0.002127421261, 0.002195909672, 0.002230867298, 0.002197336514, 0.002151677573,
                0.002158098362, 0.002170939939, 0.002206610986, 0.002206610986, 0.002168799676, 0.00217165336,
                0.002173080202, 0.00223942835, 0.002206610986, 0.002125994419, 0.002237288087, 0.002123140735,
                0.002230153877, 0.002148823889, 0.002153817836, 0.002247989401, 0.002225159931, 0.002210178091,
                0.002310057023, 0.002086042846, 0.002287227553, 0.002261544399, 0.002182354674, 0.00220233046,
                0.002189488883, 0.002250843085, 0.002195909672, 0.002201617039, 0.002185208358, 0.00216451915,
                0.002250129664, 0.002212318354, 0.002250129664, 0.002233007561, 0.002258690715, 0.002228013614,
                0.002234434403, 0.002200903618, 0.002203043881, 0.002279379923, 0.002203043881, 0.002168086255,
                0.002231580719, 0.002161665466, 0.00220946467, 0.002166659413, 0.002252269927, 0.002242282033,
                0.002191629146, 0.002199476777, 0.002272245713, 0.002117433368, 0.002235861245, 0.002225159931,
                0.002206610986, 0.002308630181, 0.002120287052, 0.002155244678, 0.002252269927, 0.002213031775,
                0.00208889653, 0.002009706805, 0.001881291035, 0.001735753162, 0.002083902583, 0.003449390274,
                0.001994011544, 0.0009502767, 0.001499610828, 0.001531714771, 0.001361207165, 0.001498897407,
                0.001718631059, 0.001702935798, 0.001740033687, 0.001722198164, 0.001741460529, 0.001774991314,
                0.001850613934, 0.00187772393, 0.001886998402, 0.001894132612, 0.001856321301, 0.001959767339,
                0.001884858139, 0.001895559454, 0.001951919708, 0.00191553524, 0.001972608916, 0.001931943922,
                0.002008993384, 0.001919102345, 0.002021834961, 0.001984023651, 0.001925523133, 0.001936224447,
                0.001994724965, 0.001940504973, 0.001871303141, 0.001977602862, 0.00196832839, 0.001929803659,
                0.00194549892, 0.002019694698, 0.001975462599, 0.001999718912, 0.001982596809, 0.002064640218,
                0.001981169967, 0.002012560489, 0.001942645236, 0.001964047864, 0.001974749179, 0.002001859174,
                0.002057506008, 0.002041810747, 0.001952633129, 0.001966188127, 0.002007566542, 0.002032536275,
                0.002000432333, 0.001996865228, 0.002067493902, 0.002036816801, 0.002026115487, 0.001985450493,
                0.002058219429, 0.001966188127, 0.002044664431, 0.002032536275, 0.00204395101, 0.001987590756,
                0.002068207322, 0.002065353639, 0.002053225483, 0.001974749179, 0.002137409154, 0.002113866263,
                0.002144543364, 0.002088183109, 0.002068207322, 0.002047518115, 0.002147397048, 0.002121713893,
                0.002038243643, 0.002161665466, 0.002117433368, 0.00205893285, 0.002139549417, 0.002210178091,
                0.00215667152, 0.002139549417, 0.00214168968, 0.002174507043, 0.002147397048, 0.002130274945,
                0.002123854156, 0.002242995454, 0.002139549417, 0.002198049935, 0.002148823889, 0.002212318354,
                0.002250129664, 0.002140262838, 0.00213455547, 0.002151677573, 0.002225873352, 0.002152390994,
                0.002147397048, 0.002245135717, 0.002177360727, 0.00219448283, 0.002205897565, 0.002212318354,
                0.002236574666, 0.002208751249, 0.002288654395, 0.002218025721, 0.002292934921, 0.002197336514,
                0.002215885458, 0.002128134682, 0.001898413137, 0.001874870246, 0.002287940974, 0.003520018948,
                0.00279018932, 0.000680603582, 0.001187845875, 0.00128201744, 0.001247059813, 0.001346225325,
                0.001634447387, 0.001638727913, 0.00163658765, 0.00166655133, 0.001692234484, 0.001738606845,
                0.00185489446, 0.001882004456, 0.001778558418, 0.001801387889, 0.001894132612, 0.001951206287,
                0.001872729983, 0.001914821819, 0.001911968135, 0.001910541293, 0.001954773392, 0.001901980242,
                0.001936937868, 0.001965474706, 0.002041097327, 0.001978316283, 0.002026115487, 0.001967614969,
                0.001996151807, 0.001956200234, 0.001961907601, 0.001993298123, 0.002001859174, 0.002039670485,
                0.001950492866, 0.00196048076, 0.00204395101, 0.00196048076, 0.002028255749, 0.002054652325,
                0.001918388924, 0.002038957064, 0.002069634164, 0.00204395101, 0.002026828908, 0.002041810747,
                0.002163805729, 0.002034676538, 0.002133128629, 0.002111726, 0.002034676538, 0.002050371799,
                0.002083902583, 0.002103164949, 0.001979029704, 0.002021834961, 0.002059646271, 0.002082475741,
                0.002083902583, 0.002035389959, 0.002034676538, 0.002046091273, 0.002056792587, 0.002074628111,
                0.002101738107, 0.002073201269, 0.002133128629, 0.002101738107, 0.002021834961, 0.002074628111,
                0.002106018633, 0.002180214411, 0.002106732054, 0.002108872316, 0.002104591791, 0.002170226518,
                0.002140262838, 0.002138835996, 0.00213455547, 0.002189488883, 0.002195909672, 0.002090323372,
                0.002178074148, 0.002168799676, 0.002265824925, 0.002210178091, 0.002116719947, 0.002183781516,
                0.002218025721, 0.002148110468, 0.002198049935, 0.002169513097, 0.002147397048, 0.002098171002,
                0.002245849138, 0.002193055988, 0.002168086255, 0.002130988366, 0.002183068095, 0.002135268891,
                0.002170226518, 0.002150250731, 0.002173080202, 0.002230153877, 0.002150964152, 0.002174507043,
                0.002320044916, 0.002099597844, 0.002165945992, 0.002119573631, 0.002174507043, 0.002257977294,
                0.002158811783, 0.002005426279, 0.001879864193, 0.001889852086, 0.002253696768, 0.003459378168,
                0.002031109433, 0.000866806449, 0.001456092151, 0.001464653202, 0.00134265822, 0.001441823732,
                0.001635160808, 0.001677966065, 0.00165941712, 0.001654423174, 0.001713637112, 0.001677966065,
                0.001827784464, 0.001829924726, 0.001723625005, 0.001820650254, 0.001799961047, 0.001794253679,
                0.001840626041, 0.001814229466, 0.001951919708, 0.001879864193, 0.001849187092, 0.001901266821,
                0.001966188127, 0.001908401031, 0.001981883388, 0.001932657343, 0.001939791552, 0.001845619987,
                0.00192338287, 0.001929090238, 0.001887711823, 0.001910541293, 0.001954773392, 0.001914108398,
                0.001862028669, 0.001980456546, 0.001997578649, 0.001939078131, 0.001992584702, 0.001971895495,
                0.001876297088, 0.001926949975, 0.002031822854, 0.001943358657, 0.001949066024, 0.002011133647,
                0.001989017597, 0.001990444439, 0.00193051708, 0.001961194181, 0.001981883388, 0.001886284981,
                0.002001145754, 0.001956913655, 0.001936224447, 0.001974749179, 0.001955486813, 0.001999718912,
                0.00204395101, 0.0020061397, 0.0020061397, 0.002001145754, 0.002039670485, 0.002029682591,
                0.002016127593, 0.001984737072, 0.001999005491, 0.002035389959, 0.002021834961, 0.002011847068,
                0.002086042846, 0.002011133647, 0.002053225483, 0.00210387837, 0.002078195216, 0.002118146789,
                0.00206606706, 0.002101738107, 0.002101738107, 0.002173793623, 0.00206606706, 0.00210387837,
                0.002045377852, 0.002157384941, 0.002128848103, 0.002190915725, 0.002087469688, 0.002052512062,
                0.002218025721, 0.002205184144, 0.002168086255, 0.002125280998, 0.002258690715, 0.002125994419,
                0.002261544399, 0.002358569648, 0.002340020703, 0.002256550452, 0.002362136752, 0.002326465705,
                0.002377832013, 0.002335740177, 0.002441326477, 0.002394240695, 0.002548339619, 0.002564748301,
                0.00269958486, 0.002726694856, 0.002706005648, 0.002865098519, 0.003071990594, 0.003091252959,
                0.003249632409, 0.003602775778, 0.003600635515, 0.003910973626, 0.004071493339, 0.004505253275};
    }

    private double[] getPointScoreDistribution(double pointExpectancyA, double pointExpectancyB) {
        if (!isFourtyMinutiesMatch) {
            double oneToTwoPointRatio = 0.478;
            double threeToTwoPointRatio = 0.224; // one pointers are 2.13x more likely than three pointers

            double[] myScoreDistn = new double[7];


            // (x * 2.0) + (oneToTwoPointRatio * x * 1.0) + (threeToTwoPointRatio * x * 3.0) = pointExpectancy
            // for this example (oneToTwoPointRatio = 0.478; threeToTwoPointRatio = 0.224)
            // 2x + 0.478x + 0.672x = y = 3.15x
            // x = y/3.15

            // 2-pt move home team
            myScoreDistn[1] = pointExpectancyA / (2.0 + 1.0 * oneToTwoPointRatio + 3.0 * threeToTwoPointRatio);
            myScoreDistn[2] = myScoreDistn[1] * threeToTwoPointRatio; // 3-pt move A team
            myScoreDistn[3] = myScoreDistn[1] * oneToTwoPointRatio; // 1-pt move A team

            myScoreDistn[4] = pointExpectancyB / (2.0 + 1.0 * oneToTwoPointRatio + 3.0 * threeToTwoPointRatio);
            myScoreDistn[5] = myScoreDistn[4] * threeToTwoPointRatio; // 3-pt move A team
            myScoreDistn[6] = myScoreDistn[4] * oneToTwoPointRatio; // 1-pt move A team

            double sum = 0.0;
            for (int i = 1; i < 7; i++) {
                sum += myScoreDistn[i];
            }

            // nothing happens, no points for either tean
            myScoreDistn[0] = 1.0 - sum;

            // return cumualtive array
            for (int i = 1; i < 7; i++) {
                myScoreDistn[i] += myScoreDistn[i - 1];
            }

            return myScoreDistn;
        } else {
            // below ratios are for european basketball
            double oneToTwoPointRatio = 0.293;
            double threeToTwoPointRatio = 0.245; // one pointers are 2.13x more likely than three pointers

            double[] myScoreDistn = new double[7];


            // (x * 2.0) + (oneToTwoPointRatio * x * 1.0) + (threeToTwoPointRatio * x * 3.0) = pointExpectancy
            // for this example (oneToTwoPointRatio = 0.478; threeToTwoPointRatio = 0.224)
            // 2x + 0.478x + 0.672x = y = 3.15x
            // x = y/3.15

            // 2-pt move home team
            myScoreDistn[1] = pointExpectancyA / (2.0 + 1.0 * oneToTwoPointRatio + 3.0 * threeToTwoPointRatio);
            myScoreDistn[2] = myScoreDistn[1] * threeToTwoPointRatio; // 3-pt move A team
            myScoreDistn[3] = myScoreDistn[1] * oneToTwoPointRatio; // 1-pt move A team

            myScoreDistn[4] = pointExpectancyB / (2.0 + 1.0 * oneToTwoPointRatio + 3.0 * threeToTwoPointRatio);
            myScoreDistn[5] = myScoreDistn[4] * threeToTwoPointRatio; // 3-pt move A team
            myScoreDistn[6] = myScoreDistn[4] * oneToTwoPointRatio; // 1-pt move A team

            double sum = 0.0;
            for (int i = 1; i < 7; i++) {
                sum += myScoreDistn[i];
            }

            // nothing happens, no points for either tean
            myScoreDistn[0] = 1.0 - sum;

            // return cumualtive array
            for (int i = 1; i < 7; i++) {
                myScoreDistn[i] += myScoreDistn[i - 1];
            }

            return myScoreDistn;
        }
    }

    private double getLinearRegressionOutput(double[] coefficients, double[] X, double intercept) {

        // loop through beta coefficients and input params to return the total z-value
        double z = 0.0;
        for (int i = 0; i < X.length; i++) {
            z += coefficients[i] * X[i];
        }

        z += intercept; // add the intercept term
        return z;
    }

    @SuppressWarnings("unused")
    private double getSigmoidFunctionForLogisticRegression(double z) {
        double y = 0.0;
        // bound the z output between 0.0 and 1.0 (probability)
        y = 1.0 / (1.0 + Math.exp(-1.0 * z));
        return y;
    }

    private double getSoftmaxFunctionForLogisticRegression(double z) {
        return Math.exp(z);
    }

    private double[] getMulticlassProbabilitiesSoftmax(double[] Z) {
        double[] probabilities = new double[Z.length]; // we need one entry for every class
        double sumExponential = 0.0;
        double maxZ = Double.NEGATIVE_INFINITY;
        double[] ZTransform = new double[Z.length];
        double[] ZExponential = new double[Z.length];

        for (int j = 0; j < Z.length; j++) {
            if (Z[j] > maxZ) {
                maxZ = Z[j];
            }
        }
        for (int j = 0; j < Z.length; j++) {
            ZTransform[j] = Z[j] - maxZ; // transform for large exponents
            ZExponential[j] = getSoftmaxFunctionForLogisticRegression(ZTransform[j]);
            sumExponential += ZExponential[j]; // Math.exp(Z[j]) is the sum of the exponents of the linear models
        }
        for (int j = 0; j < Z.length; j++) {
            probabilities[j] = ZExponential[j] / sumExponential; // log-odds ratio (softmax)
        }
        return probabilities;
    }

    // public double[][] getData(){
    // JSONParser parser = new JSONParser();
    // Object obj = parser.parse(new
    // FileReader("C:\\Users\\rmohseni\\algo-modules\\ats-algo-sport-basketball\\src\\main\\java\\ats\\algo\\sport\\basketball\\mymodel.json"));
    // JSONObject jsonObject = (JSONObject)obj;
    // return new double[2][];
    // }

    // private double[][] getModelCoefficients(){
    //
    // //beta_0 and beta_1
    // double[][] fittedCoefficients = new double[7][]; //jagged array
    // for (int i = 0; i < 7; i++) {
    // fittedCoefficients[i] = new double[2]; //7 classes
    // }
    //
    // //beta_0 is RunningTimeSeconds
    // fittedCoefficients[0][0] = 4.234057127496601e-05;
    // fittedCoefficients[1][0] = 0.000192124213452451;
    // fittedCoefficients[2][0] = 0.00038116838215629465;
    // fittedCoefficients[3][0] = 0.00031704027789640656;
    // fittedCoefficients[4][0] = -0.0004693837186546948;
    // fittedCoefficients[5][0] = -0.0004898655224324573;
    // fittedCoefficients[6][0] = 2.6575787498498095e-05;
    //
    // //beta_1 is scoreDifferencePoints
    // fittedCoefficients[0][1] = 0.4585204873217349;
    // fittedCoefficients[1][1] = 5.728306932734946;
    // fittedCoefficients[2][1] = 10.908208393390193;
    // fittedCoefficients[3][1] = 3.7851499915129283;
    // fittedCoefficients[4][1] = -5.928769034816403;
    // fittedCoefficients[5][1] = -11.508367734919753;
    // fittedCoefficients[6][1] = -3.443049024463399;
    //
    // return fittedCoefficients;
    // }
    //
    // private double[] getModelIntercepts(){
    // double[] intercepts = new double[7];
    // intercepts[0] = 8.89585295219355;
    // intercepts[1] = 3.1942428042475868;
    // intercepts[2] = -10.797509507927503;
    // intercepts[3] = 3.793236145221383;
    // intercepts[4] = 2.8576921591175997;
    // intercepts[5] = -11.908203628952705;
    // intercepts[6] = 3.964689076486598;
    //
    // return intercepts;
    // }
    //
    // public double[] getModelProbabilites(double RunningTimeSecs, double score_difference){
    //
    // double[][] coefficients = getModelCoefficients();
    // double[] intercepts = getModelIntercepts();
    // double[] X = new double[] { RunningTimeSecs, score_difference };
    //
    // double[] Z = new double[7];
    // for (int i = 0; i < 7; i++) {
    // Z[i] = getLinearRegressionOutput(coefficients[i], X, intercepts[i]); //7 classes
    // };
    //
    // double[] probabilities = getMulticlassProbabilitiesSoftmax(Z);
    //
    // return probabilities;
    // }

    public JSONObject getModelData() {
        try {

            JSONParser parser = new JSONParser();
            // String json = "{\"penalty\": \"l2\", \"dual\": false, \"tol\": 0.001, \"C\": 1.0, \"fit_intercept\":
            // true, \"intercept_scaling\": 1, \"class_weight\": null, \"random_state\": null, \"solver\": \"lbfgs\",
            // \"max_iter\": 10000, \"multi_class\": \"multinomial\", \"verbose\": 2, \"warm_start\": false, \"n_jobs\":
            // 1, \"classes_\": [0, 1, 2, 3, 4, 5, 6], \"coef_\": [[-3.196922464932251e-05, 0.0009064905577233172,
            // 0.012903798313633174, -0.00020365400605566085], [-9.010943743448825e-05, -0.007849005953586492,
            // 0.0044987664148971014, -0.006231565586016675], [-1.9363105945784945e-05, -0.008520520163772966,
            // -0.002360503225411071, -0.015718904053850128], [0.0001401262542114733, -0.010255055030897669,
            // -0.008446099055189565, -0.005185797367431357], [-9.962343612375383e-05, 0.009262697704022589,
            // 0.004499284160784848, 0.007819024027830723], [-3.063326741247957e-05, 0.00796546204059889,
            // -0.002597624397437346, 0.01615813632706447], [0.00013157221735299955, 0.008489930845906395,
            // -0.008497622211232342, 0.0033627606584691137]], \"intercept_\": [0.004157583806710309,
            // -0.0009359332361440898, -0.000861466288651853, -0.0003272169409220791, -0.0008822877458196752,
            // -0.0008558893878875866, -0.00029479020728267227], \"n_iter_\": [711]}";
            String json = "{\"penalty\": \"l2\", \"dual\": false, \"tol\": 0.001, \"C\": 1.0, \"fit_intercept\": true, \"intercept_scaling\": 1, \"class_weight\": null, \"random_state\": null, \"solver\": \"lbfgs\", \"max_iter\": 5000, \"multi_class\": \"multinomial\", \"verbose\": 2, \"warm_start\": false, \"n_jobs\": 1, \"classes_\": [0, 1, 2, 3, 4, 5, 6], \"coef_\": [[-3.19611744903468e-05, 0.0007666259212093149, 0.012916184877685371, -0.0005332079588485946, 0.001260830357156609, -0.0005662683541624999, 0.0009833798309224003, 0.002634852775163228], [-9.146507453346201e-05, -0.007479354416241473, 0.004507931786431682, -0.0065200387161716755, 0.000597535978156612, -0.00031209510427639196, -0.0006478705121635696, -0.000627564811748676], [-1.997817403164431e-05, -0.008760546217532885, -0.0023512759176794527, -0.01682594125433728, -0.000551315029406535, -5.499173153675866e-05, -0.0002789533970943639, -5.987671565073817e-06], [0.00013709588760916903, -0.01006812775496832, -0.008445370389633848, -0.004930489580721544, -0.00019266216853743576, 0.000656416617158071, -0.0003459831680287538, -0.00045583809010810866], [-0.00010123205917197164, 0.00917506250813771, 0.00450701041074328, 0.007963084575706278, -0.00012420826495696745, -0.00022016060524424833, 0.0004562410196468542, -0.0010202310783062032], [-2.9394862443309318e-05, 0.008179572810276954, -0.002641486554418634, 0.017032716497236144, -0.0006728446976123733, -0.00018587911583623667, -3.3979250316722666e-05, 1.5668271207944452e-05], [0.0001369354570605398, 0.008186767149123779, -0.008492994213147418, 0.0038138764371286743, -0.00031733617480034226, 0.0006829782938978868, -0.00013283452296562, -0.0005408993946435175]], \"intercept_\": [0.00431279648152246, -0.0009899948493578715, -0.0008912482158948383, -0.0003380669868982424, -0.000908359293824197, -0.0008770351727349728, -0.00030809196281225313], \"n_iter_\": [790]}";
            Object obj = parser.parse(json);
            JSONObject jsonObj = (JSONObject) obj;
            @SuppressWarnings("unused")
            JSONArray coefficients = (JSONArray) (jsonObj.get("coef_"));
            @SuppressWarnings("unused")
            JSONArray intercepts = (JSONArray) (jsonObj.get("intercept_"));
            // getModelProbabilitiesFromJson(jsonObj, 100, 0, 207.5, -5.5);
            // String s = "helloWorld";
            return jsonObj;
        } catch (ParseException e) {
            return new JSONObject();
        }

    }

    // public double[] getModelProbabilitiesFromJson(JSONObject jsonObj,
    // double RunningTimeSecs,
    // double score_difference,
    // double OVUN_LINE,
    // double HCAP_LINE)
    // {
    //
    // JSONArray coefficients = (JSONArray)(jsonObj.get("coef_"));
    // JSONArray intercepts = (JSONArray)(jsonObj.get("intercept_"));
    //
    // double[][] coef_ = getModelCoefFromJson(jsonObj);
    // double[] intercepts_ = getModelInterceptFromJson(jsonObj);
    //
    // double[] X = new double[] { RunningTimeSecs, score_difference, OVUN_LINE, HCAP_LINE };
    //
    // double[] Z = new double[7];
    // for (int i = 0; i < 7; i++) {
    // Z[i] = getLinearRegressionOutput(coef_[i], X, intercepts_[i]); //7 classes
    // };
    //
    // double[] probabilities = getMulticlassProbabilitiesSoftmax(Z);
    // return probabilities;
    // }

    public double[] getModelProbabilities(double[][] coef_, double[] intercepts_, double[] params) {
        double[] X = params;
        int nClasses = intercepts_.length;
        double[] Z = new double[nClasses];
        for (int i = 0; i < nClasses; i++) {
            Z[i] = getLinearRegressionOutput(coef_[i], X, intercepts_[i]); // 7 classes
        } ;

        double[] probabilities = getMulticlassProbabilitiesSoftmax(Z);

        for (int i = 1; i < nClasses; i++) {
            probabilities[i] += probabilities[i - 1];
        } ;
        return probabilities;
    }


    public double[][] getModelCoefFromJson(JSONObject jsonObj) {

        // get the model coefficients and params from the JSONObject
        JSONArray coefficients = (JSONArray) (jsonObj.get("coef_"));

        // generic use of class and params size
        int nClasses = coefficients.size();
        int nParams = ((JSONArray) coefficients.get(0)).size();
        double[][] coef_ = new double[nClasses][];
        for (int i = 0; i < nClasses; i++) {
            coef_[i] = new double[nParams]; // 4 classes
        }

        for (int i = 0; i < nClasses; i++) {
            for (int j = 0; j < nParams; j++) {
                coef_[i][j] = (double) ((JSONArray) coefficients.get(i)).get(j);
            }
        }

        return coef_;
    }

    public double[] getModelInterceptFromJson(JSONObject jsonObj) {
        JSONArray intercepts = (JSONArray) (jsonObj.get("intercept_"));
        int nClasses = intercepts.size();
        double[] intercepts_ = new double[nClasses];
        for (int i = 0; i < nClasses; i++) {
            intercepts_[i] = (double) (intercepts.get(i));
        }
        return intercepts_;
    }

    @Override
    public void consolidateStats(MonteCarloMatch match) {
        this.monteCarloMarkets.consolidate(((BasketballMatch) match).monteCarloMarkets);
    }

}
