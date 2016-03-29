import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;

public class ComputerPlayer_tqvj24 implements PlayerInterface{

    private Piece colour;
    private Point nextChoice;

    public ComputerPlayer_tqvj24(){ colour = Piece.UNSET; }

    @Override
    public MoveInterface makeMove(Piece[][] boardView) throws NoValidMovesException {
        if (!hasValidMove(boardView)){ throw new NoValidMovesException(); }

        System.out.println("Computer player is thinking...");

        //TODO actively choose when to use minimax and when it'll be too slow

        /*
        //RANDOM CHOOSING
        Random r = new Random();
        int x = r.nextInt(boardView[0].length), y = r.nextInt(boardView.length);
        //For now, random moves
        while(boardView[y][x] != Piece.UNSET){
            x = r.nextInt(boardView[0].length);
            y = r.nextInt(boardView.length);
        }

        MoveInterface myMove = new Move();
        try {
            myMove.setPosition(x, y);
        } catch (InvalidPositionException e) {
            System.out.println("Computer player somehow chose an invalid move");
        }
        */

        Point p = getNextMove(boardView);
        MoveInterface myMove = new Move();
        try {
            myMove.setPosition(p.x, p.y);
        } catch (InvalidPositionException e) {
            System.out.println("Computer player somehow chose an invalid move");
        }

        return myMove;
    }

    @Override
    public boolean setColour(Piece colour) throws InvalidColourException, ColourAlreadySetException {
        if (colour == Piece.UNSET){ throw new InvalidColourException(); }
        if (this.colour != Piece.UNSET){ throw new ColourAlreadySetException(); }
        this.colour = colour;
        return true;
    }

    @Override
    public boolean finalGameState(GameState state) {
        System.out.println("Computer player " + getPlayerName() + (state == GameState.WON ? " won!" : " lost."));
        return true;
    }

    //Non-interface methods
    private String getPlayerName(){
        return colour.name().toLowerCase();
    }

    private boolean hasValidMove(Piece[][] grid){
        for (Piece[] row : grid) {
            for (Piece piece : row) {
                if (piece == Piece.UNSET) {
                    return true;
                }
            }
        }
        return false;
    }

    //Below - methods for minimax solving
    private Point getNextMove(Piece[][] grid){
        Piece player = colour;
        nextChoice = new Point(-1 ,-1);
        int maxScore = grid.length * grid[0].length + 1;

        minimax(grid, 0, player, maxScore);

        return nextChoice;
    }

    private int minimax(Piece[][] grid, int depth, Piece player, int maxScore) {
        /*
        //TODO note here about adapting my naughts and crosses minimax
         */
        if (BoardManager.winner(grid) != Piece.UNSET) {
            return score(grid, depth, player, maxScore);
        }

        ArrayList<Integer> scores = new ArrayList<Integer>();
        ArrayList<Point> moves = new ArrayList<Point>();

        //TODO modify to only look next to placed pieces
        for (int y = 0; y < grid.length; y++) {
            for (int x = 0; x < grid[0].length; x++) {
                if (grid[y][x] == Piece.UNSET){
                    //All available moves are in here
                    grid[y][x] = BoardManager.whoseGo(grid);
                    scores.add(minimax(grid, depth + 1, player, maxScore));
                    moves.add(new Point(x, y));
                    grid[y][x] = Piece.UNSET;
                }
            }
        }

        int scoreIndex;
        if (BoardManager.whoseGo(grid) == player){
            //If the next turn is ours
            scoreIndex = scores.indexOf(Collections.max(scores)); //we want max score
        }else{
            //If the next turn is not ours
            scoreIndex = scores.indexOf(Collections.min(scores)); //opponent wants min score
        }
        nextChoice = moves.get(scoreIndex);
        return scores.get(scoreIndex);
    }

    private int score(Piece[][] grid, int depth, Piece player, int maxScore) {
        Piece winner = BoardManager.winner(grid);
        if (player == winner){
            return maxScore - depth;
        }else if(winner != Piece.UNSET){ //opponent wins
            return depth - maxScore;
        }else{
            return 0;
        }
    }

}
