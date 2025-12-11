package ats.algo.genericsupportfunctions;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public final class ConsoleInput {

    /**
     * Reads a String from the console
     * 
     * @param prompt
     * @param defaultInput the default value if no input entered
     * @return
     * @throws IOException
     */
    public static String readString(String prompt, String defaultInput) {
        String opStr = defaultInput;
        System.out.printf("%s (default %s): ", prompt, defaultInput);
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        String str = null;
        try {
            str = in.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (str.length() > 0)
            opStr = str;
        return opStr;
    }

    /**
     * read a boolean yes = true, no = false;
     * 
     * @param prompt
     * @param defaultB
     * @return
     */
    public static boolean readBoolean(String prompt, boolean defaultB) {
        String defaultInput;
        String otherInput;
        if (defaultB) {
            defaultInput = "yes";
            otherInput = "no";
        } else {
            defaultInput = "no";
            otherInput = "yes";
        }
        String str = readString(prompt, defaultInput);
        boolean result = defaultB;
        if (str.toLowerCase().equals(otherInput))
            result = !defaultB;
        return result;
    }

    /**
     * Reads integer
     * 
     * @param prompt
     * @param defaultInput
     * @param oddNoReqd true if must be odd
     * @return
     * @throws IOException
     */
    public static int readInt(String prompt, int defaultInput, boolean oddNoReqd) {
        boolean inputOk;
        int opInt = defaultInput;
        do {
            inputOk = true;
            System.out.printf("%s (default %s): ", prompt, defaultInput);
            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
            String str = null;
            try {
                str = in.readLine();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            if (str.length() > 0) {
                try {
                    int no = Integer.parseInt(str);
                    if (oddNoReqd) {
                        inputOk = 2 * (opInt / 2) != opInt;
                        if (!inputOk)
                            System.out.println("***** Error - Odd number required.");
                        else
                            opInt = no;
                    } else
                        opInt = no;
                } catch (NumberFormatException e) {
                    System.out.println("***** Error - Invalid number.");
                    inputOk = false;
                }
            }
        } while (!inputOk);
        return opInt;
    }

    /**
     * reads a double from the console
     * 
     * @param prompt
     * @param default Double default if no input entered
     * @param min lower limit
     * @param max upper limit
     * @return the read double
     */
    public static double readDouble(String prompt, double defaultDouble, double min, double max) {
        double opDouble = defaultDouble;
        boolean inputOk;
        do {
            inputOk = true;
            System.out.printf("%s between %.3f and %.3f (default %.3f): ", prompt, min, max, defaultDouble);
            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
            String s = null;
            try {
                s = in.readLine();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            if (s.length() > 0)
                try {
                    double no = Double.parseDouble(s);
                    inputOk = (no >= min && no <= max);
                    if (!inputOk)
                        System.out.println("***** Error - Number entered is outside allowed range");
                    else
                        opDouble = no;
                } catch (NumberFormatException e) {
                    inputOk = false;
                    System.out.println("***** Error - not a valid number");
                }
        } while (!inputOk);
        return opDouble;
    }

    /**
     * reads either two or three prices in format P1, P2 [, P3]
     * 
     * @param prompt
     * @param inputReqd
     * @return
     * @throws IOException
     */
    public static Odds readPrices(String prompt, boolean inputReqd) {
        boolean inputOk;
        Odds prices = new Odds(0, 0);
        do {
            inputOk = false;
            System.out.printf(prompt);
            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
            String s = null;
            try {
                s = in.readLine();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            if (s.length() == 0 && !inputReqd)
                return null;
            try {
                String[] subStrings = s.split(",");
                int n = subStrings.length;
                double[] numbers = new double[n];
                for (int i = 0; i < n; i++) {
                    numbers[i] = Double.parseDouble(subStrings[i]);
                }

                switch (n) {
                    case 2:
                        prices = new Odds(numbers[0], numbers[1]);
                        inputOk = prices.IsValid;
                        break;
                    case 3:
                        prices = new Odds(numbers[0], numbers[1], numbers[2]);
                        inputOk = prices.IsValid;
                        break;
                    default:
                        inputOk = false;
                        break;
                }
            } catch (NumberFormatException e) {
                inputOk = false;
            }
            if (!inputOk)
                System.out.println(
                                "***** Error - not valid prices.  Can't interpret numbers. Should be something like \"1.8, 2.0\"");

        } while (!inputOk);
        return prices;
    }

    /**
     * reads an over/under type market. Expected format x.5, Pover, Punder
     * 
     * @param prompt
     * @param inputReqd
     * @return
     */
    public static Odds readOverUnderMarket(String prompt, boolean inputReqd) {
        boolean inputOk;
        Odds prices = new Odds(0, 0);
        do {
            inputOk = false;
            System.out.printf(prompt);
            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
            String s = null;
            try {
                s = in.readLine();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            if (s.length() == 0 && !inputReqd)
                return null;
            try {
                String[] subStrings = s.split(",");
                int n = subStrings.length;
                double[] numbers = new double[n];
                for (int i = 0; i < n; i++) {
                    numbers[i] = Double.parseDouble(subStrings[i]);
                }

                switch (n) {
                    case 3:
                        int lineIdentifier = (int) Math.round(numbers[0] - 0.5);
                        prices = new Odds(lineIdentifier, numbers[1], numbers[2]);
                        inputOk = prices.IsValid;
                        break;
                    default:
                        inputOk = false;
                        System.out.println("***** Error - not valid prices");
                        break;
                }
            } catch (NumberFormatException e) {
                inputOk = false;
                System.out.println("***** Error - not valid prices");
            }

        } while (!inputOk);
        return prices;
    }

    /**
     * reads handicap market. Expected format x.5, , PriceA (x.5), PriceB (-x.5)
     * 
     * @param prompt
     * @param inputReqd
     * @return
     */
    public static Odds readHandicapMarket(String prompt, boolean inputReqd) {
        boolean inputOk;
        Odds prices = new Odds(0, 0);
        do {
            inputOk = false;
            System.out.printf(prompt);
            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
            String s = null;
            try {
                s = in.readLine();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            if (s.length() == 0 && !inputReqd)
                return null;
            try {
                String[] subStrings = s.split(",");
                int n = subStrings.length;
                double[] numbers = new double[n];
                for (int i = 0; i < n; i++) {
                    numbers[i] = Double.parseDouble(subStrings[i]);
                }

                switch (n) {
                    case 3:
                        int lineIdentifier = -(int) Math.round(numbers[0] + 0.5);
                        prices = new Odds(lineIdentifier, numbers[1], numbers[2]);
                        inputOk = prices.IsValid;
                        break;
                    default:
                        inputOk = false;
                        System.out.println("***** Error - not valid prices");
                        break;
                }
            } catch (NumberFormatException e) {
                inputOk = false;
                System.out.println("***** Error - not valid prices");
            }

        } while (!inputOk);
        return prices;
    }



}
