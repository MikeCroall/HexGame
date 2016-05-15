
/**
 * This is based on Claude Shannon's 'BirdCage' circuit that he created to beat the game Hex.
 * It's a basic AI, but it might work relatively well. This is one route to AI!
 * <p>
 * Created by ryan on 05/04/16.
 */
public class BirdCage {
    private Piece[][] board;
    private Integer[][] resistance;

    public BirdCage(Piece[][] board) {
        this.board = board;
        resistance = new Integer[board.length][board[0].length];


    }





    private boolean populateResistance(Piece p) {
        //this goes through all non-set cells, giving player p a positive score, and other player a negative score.
        //score bad -900 score:64182
        //score bad -800 score: 64500
        //score bad -700

        //now set score bad to -800.
        //try
        //score good 4000 score: 64511
        //score good 3000 score:

        final int goodCellValue = 5000;
        final int badCellValue = -800;
        for (int x = 0; x < board.length; x++) {
            for (int y = 0; y < board[0].length; y++) {
                if (board[x][y] == p) {
                    resistance[x][y] = goodCellValue;
                } else if (board[x][y] == Piece.UNSET) {
                    resistance[x][y] = 0; //zero value => voltage drop to be calculated for these empty cells.
                } else {
                    resistance[x][y] = badCellValue;
                }
            }
        }

        return true;
    }



    public Move getBestMove(Piece p) {
        populateResistance(p);

        //create a score for each possible move, and the aim is to find the highest move.
        Integer[][] scores = new Integer[board.length][board[0].length];
        int maxScore = 0;
        int maxX = -1;
        int maxY = -1;
        for (int x = 0; x < board.length; x++) {
            for (int y = 0; y < board[0].length; y++) {
                if (resistance[x][y] == 0) {
                    //if so, this means we need to calculate the score.
                    //do this by going through all the neighbours, averaging their weightings.
                    int score = 0;
                    for (int d1 = -1; d1 <= 1; d1++)
                        for (int d2 = -1; d2 <= 1; d2++) {
                            if ((d1 != d2) && (x + d1 >= 0) && (x + d1 < resistance.length) && (y + d2 >= 0) && (y + d2 < resistance[0].length)) {
                                score += resistance[x + d1][y + d2];
                            }
                        }

                    if (score > maxScore) {
                        maxScore = score;
                        maxX = x;
                        maxY = y;
                    }

                }
            }
        }
        Move m = new Move();
        try {

            m.setPosition(maxX, maxY);
        } catch (InvalidPositionException ex) {
            return null;
        }
        return m;


    }


}
