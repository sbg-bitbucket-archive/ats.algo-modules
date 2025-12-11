package ats.algo.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;

import com.google.common.collect.Lists;

/**
 * Defines all the possible game periods across all sports
 * 
 * @author
 *
 */
public enum GamePeriod {
    PREMATCH("PM"),
    PRE_START("PRE_START"),
    NORMAL_TIME_START("INPLAY"),
    PERIOD1("1P"),
    FIRST_SET("1SET"),
    FIRST_QUARTER("1Q"),
    END_OF_FIRST_QUARTER("1QE"),
    FIRST_INNINGS("1IT"),
    FIRST_INNINGS_BOTTOM("1IB"),
    FIRST_HALF("1H"),
    FIRST_HALF_EXTRA_TIME("1HX"),
    BREAK("B"),
    PAUSE("PAUSE"),
    INTERRUPTED("INTERRUPTED"),
    SECOND_QUARTER("2Q"),
    END_OF_SECOND_QUARTER("2QE"),
    HALF_TIME("HT"),
    HALF_TIME_EXTRA_TIME("HTX"),
    PERIOD2("2P"),
    SECOND_HALF("2H"),
    SECOND_SET("2SET"),
    SECOND_INNINGS("2IT"),
    SECOND_INNINGS_BOTTOM("2IB"),
    SECOND_HALF_EXTRA_TIME("2HX"),
    PERIOD3("3P"),
    THIRD_QUARTER("3Q"),
    END_OF_THIRD_QUARTER("3QE"),
    THIRD_SET("3SET"),
    THIRD_INNINGS("3IT"),
    THIRD_INNINGS_BOTTOM("3IB"),
    FOURTH_QUARTER("4Q"),
    FOURTH_SET("4SET"),
    FOURTH_INNINGS("4IT"),
    FOURTH_INNINGS_BOTTOM("4IB"),
    END_OF_FOURTH_QUARTER("4QE"),
    FIFTH_SET("5SET"),
    FIFTH_INNINGS("5IT"),
    FIFTH_INNINGS_BOTTOM("5IB"),
    SESSION_BREAK("BREAK"),
    STUMPS("STUMPS"),
    AFTER_SUPER_OVER("AFTERSO"),
    FRAME1("1FRM"),
    FRAME2("2FRM"),
    FRAME3("3FRM"),
    FRAME4("4FRM"),
    FRAME5("5FRM"),
    FRAME6("6FRM"),
    FRAME7("7FRM"),
    FRAME8("8FRM"),
    FRAME9("9FRM"),
    FRAME10("10FRM"),
    FRAME11("11FRM"),
    FRAME12("12FRM"),
    FRAME13("13FRM"),
    FRAME14("14FRM"),
    FRAME15("15FRM"),
    FRAME16("16FRM"),
    FRAME17("17FRM"),
    FRAME18("18FRM"),
    FRAME19("19FRM"),
    FRAME20("20FRM"),
    FRAME21("21FRM"),
    FRAME22("22FRM"),
    FRAME23("23FRM"),
    FRAME24("24FRM"),
    FRAME25("25FRM"),
    FRAME26("26FRM"),
    FRAME27("27FRM"),
    FRAME28("28FRM"),
    FRAME29("29FRM"),
    FRAME30("30FRM"),
    FRAME31("31FRM"),
    FRAME32("32FRM"),
    FRAME33("33FRM"),
    FRAME34("34FRM"),
    FRAME35("35FRM"),
    GAME1("G1"),
    GAME2("G2"),
    GAME3("G3"),
    GAME4("G4"),
    GAME5("G5"),
    GAME6("G6"),
    GAME7("G7"),
    SIXTH_INNINGS("6IT"),
    SIXTH_INNINGS_BOTTOM("6IB"),
    SEVENTH_INNINGS("7IT"),
    SEVENTH_INNINGS_BOTTOM("7IB"),
    EIGHTH_INNINGS("8IT"),
    EIGHTH_INNINGS_BOTTOM("8IB"),
    NINETH_INNINGS("9IT"),
    NINETH_INNINGS_BOTTOM("9IB"),
    NORMAL_TIME_END("NTE"),
    BKB_FIRST_HALF("B1H"),
    BKB_END_OF_FIRST_HALF("B1HE"),
    BKB_SECOND_HALF("B2H"),
    BKB_END_OF_SECOND_HALF("B2HE"),
    OVERTIME("OT"),
    END_OF_OVERTIME("OTE"),
    GOING_TO_EXTRA_TIME("XS"),
    SUPER_OVER_HOME("SOH"),
    SUPER_OVER_AWAY("SOA"),
    EXTRA_TIME_INNINGS("EIT"),
    EXTRA_TIME_INNINGS_BOTTOM("EIB"),
    EXTRA_TIME_FIRST_HALF("1HX"),
    EXTRA_TIME_HALF_TIME("1HTX"),
    EXTRA_TIME_SECOND_HALF("2HX"),
    EXTRA_TIME_ENDED("XE"),
    PENALTIES("PEN"),
    PENALTY_SHOOTING("PS"),
    POSTMATCH("END");

    private String mnemonic;

    private GamePeriod(String mnemonic) {
        this.mnemonic = mnemonic;
    }

    public static GamePeriod half(int value) {
        if (value == 1) {
            return FIRST_HALF;
        } else if (value == 2) {
            return SECOND_HALF;
        }
        throw new IllegalArgumentException("half should be 1 or 2, not " + value);
    }

    public static GamePeriod baseBallInningsFromMnemonic(String mnemonic) {
        for (GamePeriod gp : getBaseballPeriods()) {
            if (gp.getMnemonic().equals(mnemonic)) {
                return gp;
            }
        }
        throw new IllegalArgumentException("No recognised baseball period for mnemonic " + mnemonic);
    }

    public static GamePeriod innings(int value) {
        if (value == 1) {
            return FIRST_INNINGS;
        } else if (value == 2) {
            return SECOND_INNINGS;
        } else if (value == 3) {
            return THIRD_INNINGS;
        } else if (value == 4) {
            return FOURTH_INNINGS;
        } else if (value == 5) {
            return FIFTH_INNINGS;
        } else if (value == 6) {
            return SIXTH_INNINGS;
        } else if (value == 7) {
            return SEVENTH_INNINGS;
        } else if (value == 8) {
            return EIGHTH_INNINGS;
        } else if (value == 9) {
            return NINETH_INNINGS;
        } else if (value > 9) {
            return EXTRA_TIME_INNINGS;
        }
        throw new IllegalArgumentException("innings should be 1..9, not " + value);
    }

    public static GamePeriod quarter(int value) {
        if (value == 1) {
            return FIRST_QUARTER;
        } else if (value == 2) {
            return SECOND_QUARTER;
        } else if (value == 3) {
            return THIRD_QUARTER;
        } else if (value == 4) {
            return FOURTH_QUARTER;
        }
        throw new IllegalArgumentException("quarter should be 1,2,3 or 4, not " + value);
    }

    public static GamePeriod set(int value) {
        if (value == 1) {
            return FIRST_SET;
        } else if (value == 2) {
            return SECOND_SET;
        } else if (value == 3) {
            return THIRD_SET;
        } else if (value == 4) {
            return FOURTH_SET;
        } else if (value == 5) {
            return FIFTH_SET;
        }
        throw new IllegalArgumentException("set should be 1,2,3,4 or 5 not " + value);
    }

    public static GamePeriod frame(int value) {
        ArrayList<GamePeriod> frames = Lists.newArrayList(getSnookerFrames());
        if (value > frames.size() || value == 0) {
            throw new IllegalArgumentException("frame should be 1..35 not " + value);
        }
        return frames.get(value - 1);

    }


    public static GamePeriod game(int value) {
        ArrayList<GamePeriod> games = Lists.newArrayList(getTabletennisGames());
        if (value > games.size() || value == 0) {
            throw new IllegalArgumentException("game should be 1..7 not " + value);
        }
        return games.get(value - 1);

    }

    public static boolean isBaseball(GamePeriod period) {
        return getBaseballPeriods().contains(period);
    }

    public static EnumSet<GamePeriod> getBaseballPeriods() {
        return EnumSet.of(FIRST_INNINGS, FIRST_INNINGS_BOTTOM, BREAK, SECOND_INNINGS, SECOND_INNINGS_BOTTOM,
                        THIRD_INNINGS, THIRD_INNINGS_BOTTOM, FOURTH_INNINGS, FOURTH_INNINGS_BOTTOM, FIFTH_INNINGS,
                        FIFTH_INNINGS_BOTTOM, SIXTH_INNINGS, SIXTH_INNINGS_BOTTOM, SEVENTH_INNINGS,
                        SEVENTH_INNINGS_BOTTOM, EIGHTH_INNINGS, EIGHTH_INNINGS_BOTTOM, NINETH_INNINGS,
                        NINETH_INNINGS_BOTTOM, EXTRA_TIME_INNINGS, EXTRA_TIME_INNINGS_BOTTOM);
    }

    public static EnumSet<GamePeriod> getTabletennisPeriods() {
        return EnumSet.of(GAME1, GAME2, GAME3, GAME4, GAME5, GAME6, GAME7);
    }

    public static boolean isTabletennis(GamePeriod period) {
        return getTabletennisPeriods().contains(period);
    }


    public static EnumSet<GamePeriod> getBasketballPeriods() {
        return EnumSet.of(FIRST_QUARTER, END_OF_FIRST_QUARTER, SECOND_QUARTER, END_OF_SECOND_QUARTER, THIRD_QUARTER,
                        END_OF_THIRD_QUARTER, FOURTH_QUARTER, END_OF_FOURTH_QUARTER, BKB_FIRST_HALF,
                        BKB_END_OF_FIRST_HALF, BKB_SECOND_HALF, BKB_END_OF_SECOND_HALF, OVERTIME, INTERRUPTED, PAUSE);
    }

    public boolean isEndOfBasketballPeriod() {
        return EnumSet.of(BKB_END_OF_FIRST_HALF, BKB_END_OF_SECOND_HALF, END_OF_FIRST_QUARTER, END_OF_SECOND_QUARTER,
                        END_OF_THIRD_QUARTER, END_OF_FOURTH_QUARTER, END_OF_OVERTIME).contains(this);
    }

    public static boolean isBasketball(GamePeriod period) {
        return getBasketballPeriods().contains(period);
    }

    public static EnumSet<GamePeriod> getIceHockeyPeriods() {
        return EnumSet.of(PERIOD1, PERIOD2, PERIOD3, PAUSE);
    }

    public static boolean isIceHockey(GamePeriod period) {
        return getIceHockeyPeriods().contains(period);
    }

    public static EnumSet<GamePeriod> getAmericanFootballPeriods() {
        return EnumSet.of(FIRST_QUARTER, SECOND_QUARTER, THIRD_QUARTER, FOURTH_QUARTER, OVERTIME, INTERRUPTED, PAUSE);
    }

    public static boolean isAmericanFootball(GamePeriod period) {
        return getAmericanFootballPeriods().contains(period);
    }

    public static EnumSet<GamePeriod> getTennisPeriods() {
        return EnumSet.of(FIRST_SET, SECOND_SET, THIRD_SET, FOURTH_SET, FIFTH_SET);
    }

    public static EnumSet<GamePeriod> getSnookerFrames() {
        return EnumSet.range(FRAME1, FRAME35);
    }

    public static EnumSet<GamePeriod> getTabletennisGames() {
        return EnumSet.range(GAME1, GAME7);
    }

    public boolean isFirstPeriod() {
        return this == FIRST_HALF || this == FIRST_SET || this == FIRST_QUARTER || this == FIRST_INNINGS
                        || this == PERIOD1 || this == FRAME1 || this == GAME1;
    }

    public boolean isInplayFootballPeriod() {
        return this == FIRST_HALF || this == SECOND_HALF || this == EXTRA_TIME_FIRST_HALF
                        || this == EXTRA_TIME_SECOND_HALF || this == PENALTY_SHOOTING;
    }

    public boolean isInplayFootballNormalTime() {
        return this == FIRST_HALF || this == SECOND_HALF;
    }

    public boolean isFirstHalfEnd() {
        return this == HALF_TIME;
    }

    public boolean isSecondHalfEnd() {
        return this == NORMAL_TIME_END;
    }

    public boolean isPostMatch() {
        return this == POSTMATCH;
    }

    /**
     * Return quarter number from 1 to 4. For another game period return 0.
     * 
     * @return 1 to 4 or zero for non quarter period.
     */
    public int getBasketballQuarterNumber() {
        switch (this) {
            case FIRST_QUARTER:
            case END_OF_FIRST_QUARTER:
                return 1;
            case SECOND_QUARTER:
            case END_OF_SECOND_QUARTER:
                return 2;
            case THIRD_QUARTER:
            case END_OF_THIRD_QUARTER:
                return 3;
            case FOURTH_QUARTER:
            case END_OF_FOURTH_QUARTER:
                return 4;
            default:
                return 0;
        }
    }

    /**
     * Return quarter number from 1 to 2. For another game period return 0.
     * 
     * @return 1 to 2 or zero for non half period.
     */
    public int getBasketballHalfNumber() {
        switch (this) {
            case BKB_FIRST_HALF:
            case BKB_END_OF_FIRST_HALF:
                return 1;
            case BKB_SECOND_HALF:
            case BKB_END_OF_SECOND_HALF:
                return 2;
            default:
                return 0;
        }
    }

    public int getSetNumber() {
        if (this == FIRST_SET) {
            return 1;
        } else if (this == SECOND_SET) {
            return 2;
        } else if (this == THIRD_SET) {
            return 3;
        } else if (this == FOURTH_SET) {
            return 4;
        } else if (this == FIFTH_SET) {
            return 5;
        }
        return 0;
    }

    public boolean isSnooker() {
        return getSnookerFrames().contains(this);
    }

    public int getFrameNumber() {
        int index = Lists.newArrayList(getSnookerFrames()).indexOf(this);
        return index > -1 ? index + 1 : 1;
    }

    public int getGameNumber() {
        int index = Lists.newArrayList(getTabletennisGames()).indexOf(this);
        return index > -1 ? index + 1 : 1;
    }

    public boolean isTennis() {
        return getTennisPeriods().contains(this);
    }

    public EnumSet<GamePeriod> getSoccerPeriods() {
        return EnumSet.of(FIRST_HALF, HALF_TIME, SECOND_HALF, EXTRA_TIME_FIRST_HALF, EXTRA_TIME_HALF_TIME,
                        EXTRA_TIME_SECOND_HALF, EXTRA_TIME_ENDED, PENALTY_SHOOTING);
    }

    public boolean isSoccer() {
        return getSoccerPeriods().contains(this);
    }

    public boolean isRestPeriod() {
        return this == HALF_TIME || this == NORMAL_TIME_END || this == EXTRA_TIME_HALF_TIME || this == EXTRA_TIME_ENDED
                        || this == SESSION_BREAK || this == BREAK || this == PAUSE || this == INTERRUPTED;
    }

    public boolean is(String period) {
        return name().equals(period) || mnemonic.equals(period);
    }

    public boolean isPlayPeriod() {
        return this != PREMATCH && this != PRE_START && !isRestPeriod() && !isPaused();
    }

    public boolean isPaused() {
        return this == SESSION_BREAK || this == BREAK || this == PAUSE || this == INTERRUPTED
                        || this == END_OF_FIRST_QUARTER || this == END_OF_SECOND_QUARTER || this == END_OF_THIRD_QUARTER
                        || this == END_OF_FOURTH_QUARTER;
    }

    public boolean isExtraTime() {
        return this == GOING_TO_EXTRA_TIME || this == EXTRA_TIME_FIRST_HALF || this == EXTRA_TIME_HALF_TIME
                        || this == EXTRA_TIME_SECOND_HALF || this == EXTRA_TIME_INNINGS
                        || this == EXTRA_TIME_INNINGS_BOTTOM;
    }

    public boolean isAfter(GamePeriod other) {
        return ordinal() > other.ordinal();
    }

    public boolean isOrAfter(GamePeriod other) {
        return ordinal() >= other.ordinal();
    }

    public boolean isWithin(GamePeriod since, GamePeriod to) {
        return isOrAfter(since) && isOrBefore(to);
    }

    public boolean isBefore(GamePeriod other) {
        return ordinal() < other.ordinal();
    }

    public boolean isOrBefore(GamePeriod other) {
        return ordinal() <= other.ordinal();
    }

    public String toText() {
        return toString().replace("_", " ").toLowerCase().replace("pre", "pre ").replace("post", "post ");
    }

    public static GamePeriod findGamePeriod(String status) {
        GamePeriod[] enums = values();
        for (GamePeriod period : enums) {
            if (period.toString().equalsIgnoreCase(status) || period.getMnemonic().equalsIgnoreCase(status)) {
                return period;
            }
        }
        return null;
    }

    public boolean forcePeriodStart() {
        return !Arrays.asList(END_OF_FIRST_QUARTER, END_OF_SECOND_QUARTER, END_OF_THIRD_QUARTER, END_OF_FOURTH_QUARTER)
                        .contains(this);
    }

    public String getMnemonic() {
        return mnemonic;
    }
}
