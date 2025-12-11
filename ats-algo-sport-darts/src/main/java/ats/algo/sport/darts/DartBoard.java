package ats.algo.sport.darts;



public final class DartBoard {
    static Colour test = Colour.GREEN;
    static Colour innerBullColour = Colour.GREEN;

    static BoardNumber[] boardNumber = {new BoardNumber(0, 0, 0, Colour.NOCOLOUR),
            new BoardNumber(1, 20, 18, Colour.GREEN), new BoardNumber(2, 15, 17, Colour.RED),
            new BoardNumber(3, 17, 19, Colour.RED), new BoardNumber(4, 18, 13, Colour.GREEN),
            new BoardNumber(5, 12, 20, Colour.GREEN), new BoardNumber(6, 13, 10, Colour.GREEN),
            new BoardNumber(7, 19, 16, Colour.RED), new BoardNumber(8, 16, 11, Colour.RED),
            new BoardNumber(9, 14, 12, Colour.GREEN), new BoardNumber(10, 6, 15, Colour.RED),
            new BoardNumber(11, 8, 14, Colour.GREEN), new BoardNumber(12, 9, 5, Colour.RED),
            new BoardNumber(13, 4, 6, Colour.RED), new BoardNumber(14, 11, 9, Colour.RED),
            new BoardNumber(15, 10, 2, Colour.GREEN), new BoardNumber(16, 7, 8, Colour.GREEN),
            new BoardNumber(17, 2, 3, Colour.GREEN), new BoardNumber(18, 1, 4, Colour.RED),
            new BoardNumber(19, 3, 7, Colour.GREEN), new BoardNumber(20, 5, 1, Colour.RED)};



    /**
     * returns the number on the dart board to the left of specified no
     * 
     * @param no
     * @return
     */
    public static int leftNo(int no) {
        return boardNumber[no].noToLeft;
    }

    /**
     * returns the number on the dart board to the right of the specified no
     * 
     * @param no
     * @return
     */
    public static int rightNo(int no) {
        return boardNumber[no].noToRight;
    }

    /**
     * returns the colour of this no on the dartboard
     * 
     * @param no
     * @return
     */
    public static Colour colourOfNumber(int no) {
        if (no == 25)
            return innerBullColour;
        else
            return boardNumber[no].colour;
    }
}
