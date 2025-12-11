package ats.algo.loadtester;

import java.io.IOException;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import ats.algo.algomanager.AlgoManager;
import ats.algo.algomanager.AlgoManagerConfiguration;
import ats.algo.algomanager.JmsAlgoManagerConfiguration;
import ats.algo.algomanager.SharedMemoryEnvironmentAlgoManagerConfiguration;
import ats.algo.core.common.SupportedSportType;
import ats.algo.genericsupportfunctions.ConsoleInput;
import ats.algo.springbridge.SpringContextBridge;
import ats.core.util.log.Level;
import ats.core.util.log.LogUtil;

public class LoadTesterMain {

    static AlgoManagerConfiguration algoManagerConfiguration;

    public static void main(String[] args) {
        initSpring();


        int loggingLevel = ConsoleInput.readInt("Choose logging level: 1=warnings/errors only or 2=all", 1, false);
        if (loggingLevel == 1)
            LogUtil.initConsoleLogging(Level.WARN);
        else
            LogUtil.initConsoleLogging(Level.TRACE);

        int memoryOption = ConsoleInput.readInt("Choose algo config : 1=jms or 2=shared memory", 1, false);
        if (memoryOption == 2) {
            int nCalculators = ConsoleInput.readInt("Enter number of of Algo calculators to start up", 1, false);
            int nParamFinders = ConsoleInput.readInt("Enter number of Param finders to start up", 1, false);
            System.out.printf("Initialising shared memory...\n");
            algoManagerConfiguration = new SharedMemoryEnvironmentAlgoManagerConfiguration(nCalculators, nParamFinders);
        } else {
            String brokerHost = ConsoleInput.readString("Enter AMQ broker host", "enamalgobench02");
            algoManagerConfiguration = new JmsAlgoManagerConfiguration("tcp://" + brokerHost + ":61616", false);
            System.out.printf("Initialising jms...\n");
        }

        runLoadTest();

        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.exit(0);
    }

    private static void initSpring() {
        String componentScanPackage = System.getProperty("algomgr.springBridgeComponentScanPackage");
        System.out.println("algomgr.springBridgeComponentScanPackage = " + componentScanPackage);
        if (componentScanPackage != null) {
            System.out.println("initialising spring context...");
            String[] packages = componentScanPackage.split(",");
            AnnotationConfigApplicationContext springApplicationContext =
                            new AnnotationConfigApplicationContext(packages);
            SpringContextBridge.newApplicationContext(springApplicationContext);
        }
    }

    private static void runLoadTest() {
        String s = "-----Algo Manager load and soak testing suite v4-----\n";
        s += "Tests available:\n";
        s += "1 - Multiple tennis matches:  The specified number of matches are concurrently started and then played through\n";
        s += "     to completion by generating point won match incidents with 80% probability of player A winning a point\n.";
        s += "     Incidents for a given match are issued sequentially: as soon as results from one incident are received\n";
        s += "     the next is issued.\n";
        s += "2 - Multiple football matches.  The specified number of matches are concurrently started.\n";
        s += "     For each match 50 calcs are triggered by issuing setMatchParams method calls to AlgoManager.\n";
        s += "     Param changes for a given match are issued sequentially: as soon as results from one change are received\n";
        s += "     the next is issued.\n";
        s += "3 - Concurrent events tennis.  A single match is started and then 48 match incidents immediately fired at AlgoManager\n";
        s += "     Each incident is pointWon by playerA who therefore wins 6-0 6-0.  AlgoManage has to queue up the incidents and\n";
        s += "     process them sequentially\n";
        s += "4 - Concurrent events football.  A single match is started and then 48 match incidents immeidately fired at AlgoManager\n";
        s += "     Each incident is goalScored.  Teah scoring is assigned at random equally.  As with test 3 this exercises AlgoManager\n";
        s += "     queuing logic\n";
        s += "5 - Param finding football.  Issues n sequential param finds to each of m matches, where n and m are keyed in when prompted.\n";
        s += "     Each param find for a given match is issued as soon as the previous one finishes\n";
        s += "6 - Saturday afternoon simulator.  Simulates the load from n football matches all in the first half with random incidents\n";
        s += "      and param finds thrown in. Incidents generated at average rate of one every 30 seconds.  Param finds every four minutes\n";
        s += "7 - Fatal error test.  Causes an exception to be generated in AlgoCalculator and then in AlgoParamfinder and verifies recovery\n";
        s += "8 - Multiple volleyball matches: Same as 1  for Volleyball\n";
        s += "9 - Basic load test for any sport\n";
        System.out.print(s);
        String inputReqString =
                        "Tests available:\n  1=Multiple tennis matches, 2=Multiple football matches 3=Concurrent events tennis,\n";
        inputReqString += "  4=Concurrent events football, 5=Param finding football, 6=Saturday afternoon simulator,\n";
        inputReqString += "  7=Fatal error test, 8=Multiple volleyball matches, 9 = Any sport load test\nEnter option:";
        int testNo = ConsoleInput.readInt(inputReqString, 1, false);
        AlgoManager algoManager;
        AlgoManagerLoadTester algoManagerLoadTester = null;
        FootballSaturdayLoadSimulator footballSaturdayLoadSimulator = null;
        GenericSportLoadTester genericSportLoadTester = null;
        if (testNo == 6) {
            footballSaturdayLoadSimulator = new FootballSaturdayLoadSimulator();
            algoManager = new AlgoManager(algoManagerConfiguration, footballSaturdayLoadSimulator);
            algoManager.onlyPublishMarketsFollowingParamChange(false);
            footballSaturdayLoadSimulator.setAlgoManager(algoManager);
        } else if (testNo == 9) {
            genericSportLoadTester = new GenericSportLoadTester();
            algoManager = new AlgoManager(algoManagerConfiguration, genericSportLoadTester);
            algoManager.onlyPublishMarketsFollowingParamChange(false);
            genericSportLoadTester.setAlgoManager(algoManager);
        } else {
            algoManagerLoadTester = new AlgoManagerLoadTester();
            algoManager = new AlgoManager(algoManagerConfiguration, algoManagerLoadTester);
            algoManager.onlyPublishMarketsFollowingParamChange(false);
            algoManagerLoadTester.setAlgoManager(algoManager);
        }
        switch (testNo) {
            case 1:
            default:
                int noMatches = ConsoleInput.readInt("Enter number of matches to play", 1, false);
                algoManagerLoadTester.executeRandomTennisMatchesLoadTest(noMatches);
                break;
            case 2:
                int noMatches2 = ConsoleInput.readInt("Enter number of matches to play", 1, false);
                algoManagerLoadTester.executeFootballCalcsTest(noMatches2);
                break;
            case 3:
                algoManagerLoadTester.executeConcurrentEventsTestTennis();
                break;
            case 4:
                algoManagerLoadTester.executeConcurrentEventsTestFootball();
                break;
            case 5:
                int noMatches3 = ConsoleInput.readInt("Enter number of matches to play", 1, false);
                int noParamFinds = ConsoleInput.readInt("Enter number of paramFinds per match", 10, false);
                algoManagerLoadTester.executeFootballParamFindTest(noMatches3, noParamFinds);
                break;
            case 6:
                int nMatches = ConsoleInput.readInt("Enter number of matches to play", 6, false);
                int meanGoalScoreTime = 30;
                int meanParamFindTime = ConsoleInput.readInt("Enter average frequency of param finds/match in seconds",
                                300, false);
                footballSaturdayLoadSimulator.executeSaturdayLoadTest(nMatches, meanGoalScoreTime, meanParamFindTime);
                break;
            case 7:
                algoManagerLoadTester.executeFatalErrorTest();
                break;
            case 8:
                int noMatches4 = ConsoleInput.readInt("Enter number of matches to play", 1, false);
                algoManagerLoadTester.executeRandomVolleyballMatchesLoadTest(noMatches4);
                break;
            case 9:
                boolean gotSport = false;
                SupportedSportType sportType = null;
                do {
                    String sport = ConsoleInput.readString("Enter sport name: ", "TENNIS").toUpperCase();
                    try {
                        sportType = SupportedSportType.valueOf(sport);
                    } catch (IllegalArgumentException e) {
                        /*
                         * do nothing
                         */
                    }
                    gotSport = sportType != null;
                    if (!gotSport) {
                        System.out.print("Invalid sportname:  " + sport + "\nValid names:");
                        SupportedSportType[] sList = SupportedSportType.values();
                        for (SupportedSportType st : sList)
                            System.out.print(st + ", ");
                        System.out.println();
                    }

                } while (!gotSport);
                genericSportLoadTester.executeLoadTest(sportType);



        }
        sleep(1000);

        algoManager.close();
        System.out.print(algoManager.getStatistics().toString());
        System.out.println("** Finished - press any key to exit **");

    }



    private static void sleep(int mSecs) {
        try {
            Thread.sleep(mSecs);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }



}
