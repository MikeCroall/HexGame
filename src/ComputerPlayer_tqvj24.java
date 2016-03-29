import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class ComputerPlayer_tqvj24 implements PlayerInterface{

    private Piece colour;
    private Point nextChoice;

    public ComputerPlayer_tqvj24(){ colour = Piece.UNSET; }

    @Override
    public MoveInterface makeMove(Piece[][] boardView) throws NoValidMovesException {
        if (!hasValidMove(boardView)){ throw new NoValidMovesException(); }

        long startTime = System.currentTimeMillis();
        System.out.println("Computer player is thinking...");
        Point p = getNextMove(boardView);
        System.out.println("Computer decided in " + (System.currentTimeMillis() - startTime) + "ms");

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
        if (BoardManager_tqvj24.moreThanXEmpties(grid, 9)){
            //TODO Improve non-minimax to be not random
            int x = (int)grid[0].length / 2;
            int y = (int)grid.length / 2;
            Random r = new Random();
            while(!BoardManager_tqvj24.isFreeSpace(x, y, grid)){
                x = r.nextInt(grid[0].length);
                y = r.nextInt(grid.length);
            }
            return new Point(x,y);
        }else {
            Piece player = colour;
            nextChoice = new Point(-1, -1);
            int maxScore = grid.length * grid[0].length + 1;

            minimax(grid, 0, player, maxScore);

            return nextChoice;
        }
    }

    private int minimax(Piece[][] grid, int depth, Piece player, int maxScore) {
        /*
        //TODO note here about adapting my naughts and crosses minimax
         */
        if (BoardManager_tqvj24.winner(grid) != Piece.UNSET) {
            return score(grid, depth, player, maxScore);
        }

        ArrayList<Integer> scores = new ArrayList<Integer>();
        ArrayList<Point> moves = new ArrayList<Point>();

        ArrayList<Point> checked = new ArrayList<Point>();
        for (int y = 0; y < grid.length; y++) {
            for (int x = 0; x < grid[0].length; x++) {
                if (grid[y][x] != Piece.UNSET) {
                    //All placed pieces here
                    for (Point p : BoardManager_tqvj24.getEmptyNeighbours(x, y, grid)) {
                        //All empty neighbours of placed pieces
                        if (!checked.contains(p)) {
                            checked.add(p);
                            grid[p.y][p.x] = BoardManager_tqvj24.whoseGo(grid);
                            scores.add(minimax(grid, depth + 1, player, maxScore));
                            moves.add(p);
                            grid[p.y][p.x] = Piece.UNSET;
                        }
                    }
                }
            }
        }

        if (moves.isEmpty()){
            int x = (int)grid[0].length / 2;
            int y = (int)grid.length / 2;
            Random r = new Random();
            while(!BoardManager_tqvj24.isFreeSpace(x, y, grid)){
                x = r.nextInt(grid[0].length);
                y = r.nextInt(grid.length);
            }
            nextChoice = new Point(x,y);
            return 0;
        }

        int scoreIndex;
        if (BoardManager_tqvj24.whoseGo(grid) == player){
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
        Piece winner = BoardManager_tqvj24.winner(grid);
        if (player == winner){
            return maxScore - depth;
        }else if(winner != Piece.UNSET){ //opponent wins
            return depth - maxScore;
        }else{
            return 0;
        }
    }

}
