package ats.algo.sport.football.tradingrules;

public class FootballTradingList {
    public static final String ALL_MARKET = "All_MARKET";
    public static final String[] ALL_CORNER_SUSPEND_MARKET =
                    new String[] {"C:OU", "C:OU1", "C:SPRD1", "C:OU:A", "C:OU:B", "C:SPRD", "C:AXB", "C:AXB1", "C:NC"};
    public static final String[] ALL_BOOKING_SUSPEND_MARKET =
                    new String[] {"BC:OU", "BO:AXB", "BO:OU", "BO:SPRD", "BY:OU:A", "BY:OU:B", "BO:RED"};
    public static final String[] FIRST_HALF_SUSPEND_MARKET =
                    new String[] {"P:OU", "P:SPRD", "P:AXB", "C:OU1", "C:SPRD1", "C:AXB1"};
    public static final String[] FIVE_MIN_SUSPEND_MARKET = new String[] {"FT:5MR", "FT:5MG"};
    public static final String[] TEN_MIN_SUSPEND_MARKET = new String[] {"FT:10MR"};
    public static final String[] FIFTENN_MIN_SUSPEND_MARKET = new String[] {"FT:15MR"};
    public static final String[] BEFORE_FULL_TIME_OPEN_MARKET =
                    new String[] {"FT:AB", "FT:AXB", "FT:OU", "C:AXB", "C:OU"};
    public static final String[] FULL_TIME_SUSPEND_MARKET = new String[] {ALL_MARKET};
    public static final String[] PREMATCH_OPEN_MARKET = new String[] {ALL_MARKET};
    public static final String[] INPLAY_OPEN_MARKET = new String[] {ALL_MARKET};
    public static final String[] GOAL_SUSPEND_MARKET = new String[] {ALL_MARKET};
    public static final String[] NORMAL_TIME_PENALTY_SUSPEND_MARKET = new String[] {ALL_MARKET};
    public static final String[] RED_CARD_SUSPEND_MARKET = new String[] {ALL_MARKET};
    public static final String[] EXTRA_TIME_OPEN_MARKET = new String[] {"FT:AB"};
    // No extra time market available for testing only
    public static final String[] HALF_TIME_EXTRA_TIME_SUSPEND_MARKET = new String[] {ALL_MARKET};
    public static final String[] FULL_TIME_EXTRA_TIME_SUSPEND_MARKET = new String[] {ALL_MARKET};
    public static final String[] PENALTY_OPEN_MARKET = new String[] {"FT:AHCP"};
    // No PENLATY market available for testing only
}
