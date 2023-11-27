import java.awt.AWTEvent;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class BombSquare extends GameSquare {
    private GameBoard board; // Object reference to the GameBoard this square is part of.

    /*
     * True if this squre contains a bomb. False otherwise. True if the square has a
     * flag True if square is clicked
     */
    private boolean hasBomb, clicked, hasFlag, lost;

    private BombSquare check;

    private boolean hasNum = false;
    private boolean visited = false;

    private JFrame frame;

    /*
     * Array that allows for the traversal around a square
     */
    private int[] aroundx = { 0, 1, 1, 1, 0, -1, -1, -1 };
    private int[] aroundy = { 1, 1, 0, -1, -1, -1, 0, 1 };

    /*
     * BombSquare constructor
     */
    public BombSquare(int x, int y, GameBoard board) {
        super(x, y, "images/blank.png");

        this.board = board;
        this.hasBomb = Math.random() < 0.15;

    }

    /*
     * Key listener activates when left mouse is clicked
     */
    @Override
    public void leftClicked() {
        int x = getXLocation(); // Get x and y locations of the current square
        int y = getYLocation();

        if (this.clicked) {         //  If the square has been clicked, return so the user cant click it again
            return;
        }
        this.clicked = true;

        //  If the user clicke a bomb print the loss message and close program
        if (hasBomb) {
            this.setImage("images/bomb.png");
            JOptionPane.showMessageDialog(frame, "You've Exploded!! Better luck next time :(");
            System.exit(0);
        }

        //  if the user didn't click a bomb the go the the recursive function
        if (!hasBomb) {

            reveal(x, y);
        }
    }

    /*
    *   Key listener activates when left mouse is clicked
    */
    @Override
    public void rightClicked() {

        //  Places a flag on a square if it has no flag, removes the flag if it does
        if (hasFlag == true) {
            this.setImage("images/blank.png");
            hasFlag = false;
            return;
        }
        if (hasFlag == false) {
            this.setImage("images/flag.png");
            hasFlag = true;
            return;
        }
    }
    /*
    *  Checks several base cases to see if the square can be set to 0 and then be traversed
    */

    public void reveal(int x, int y) {
        BombSquare square;
       
        square = (BombSquare) board.getSquareAt(x, y);

        //  If square is out of board bounds return
        if (square == null) {
            return;
        }
        
        //  If the square has already been visited return
        if (square.visited == true) {
            return;
        }

        square.visited = true;

        //  If the square has a bomb return
        if (square.hasBomb) {
            return;
        }

        //  If the square has a number on it return
        if (square.hasNum) {
            //System.out.println("num");
            return;
        }
        //  Set the clicked square to 0 if it passes all base cases
        square.setImage("images/0.png");

        //  Jump to bombcheck method if it passes all base cases
        square.numBombs(x, y);

    }

    /*
    *   numBombs method traverses around the 8 squares of a current square to see for bombs
    */
    public void numBombs(int x, int y) {

        // This loop enables us to do a hasBomb check on all 8 surrounding squares
        int count = 0;
        for (int l = 0; l < 8; l++) {

            //  If surrounding square is out of bounds and if current square is out of bounds return
            if (board.getSquareAt(x + aroundx[l], y + aroundy[l]) == null) {
                if (board.getSquareAt(x, y) == null) {
                    return;
                }
            } 
            //  Else, if a surrounding square has a bomb to the current counter
            else {
                check = (BombSquare) board.getSquareAt(x + aroundx[l], y + aroundy[l]);

                if (check.hasBomb) {
                    count++;
                }
            }
        }

        //  If counter is more than 0 then hasNum is true and set the image to the amount of bombs around the square
        if (count > 0)
            this.hasNum = true;
        if (count == 5) {
            this.setImage("images/5.png");
        }
        if (count == 4) {
            this.setImage("images/4.png");
        }
        if (count == 3) {
            this.setImage("images/3.png");
        }
        if (count == 2) {
            this.setImage("images/2.png");
        }
        if (count == 1) {
            this.setImage("images/1.png");
        }

        //  If the square doesn't have a number on it then recurse around the functino again with new coordinates
        if(!this.hasNum) {
            reveal(x + 1, y + 1);
            reveal(x + 1, y);
            reveal(x + 1, y - 1);
            reveal(x, y - 1);
            reveal(x - 1, y - 1);
            reveal(x - 1, y);
            reveal(x - 1, y + 1);
            reveal(x, y + 1);
        }
    }

}
