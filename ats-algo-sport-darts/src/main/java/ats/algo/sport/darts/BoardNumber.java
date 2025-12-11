package ats.algo.sport.darts;

/**
 * Basic properties of a dart board
 * 
 * @author Geoff
 *
 */
public class BoardNumber {
    public int no;
    public int noToLeft;
    public int noToRight;
    public Colour colour;

    public BoardNumber(int no, int noToLeft, int noToRight, Colour colour) {
        this.no = no;
        this.noToLeft = noToLeft;
        this.noToRight = noToRight;
        this.colour = colour;
    }
}
