import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class RandomPlayer_tqvj24 implements PlayerInterface {

    private Piece colour;
    private Point nextChoice;
    private Piece[][] prevGrid;
    private GameState finalGameState;

    public RandomPlayer_tqvj24() {
        colour = Piece.UNSET;
        finalGameState = null;
    }

    @Override
    public MoveInterface makeMove(Piece[][] boardView) throws NoValidMovesException {
        if (colour == Piece.UNSET || !BoardManager_tqvj24.hasValidMove(boardView)) {
            throw new NoValidMovesException();
        }

        Point p = randomChoice(boardView);

        MoveInterface myMove = new Move();
        try {
            if (!myMove.setPosition(p.x, p.y)) {
                System.out.println("Error: Setting position of moved failed!");
            }
        } catch (InvalidPositionException e) {
            System.out.println("Computer player somehow chose an invalid move");
        }

        return myMove;
    }

    @Override
    public boolean setColour(Piece colour) throws InvalidColourException, ColourAlreadySetException {
        if (colour == Piece.UNSET) {
            throw new InvalidColourException();
        }
        if (this.colour != Piece.UNSET) {
            throw new ColourAlreadySetException();
        }
        try {
            this.colour = colour;
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @Override
    public boolean finalGameState(GameState state) {
        try {
            finalGameState = state;
            System.out.println("Computer player tqvj24 " + getPlayerName() + (state == GameState.WON ? " won!" : " lost."));
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    //Non-interface methods
    public GameState getFinalGameState(){
        return finalGameState;
    }

    private String getPlayerName() {
        return colour.name().toLowerCase();
    }

    private Point randomChoice(Piece[][] grid) {
        ArrayList<Point> empties = BoardManager_tqvj24.getFreeSpaces(grid);
        Random r = new Random();
        return empties.get(r.nextInt(empties.size()));
    }

}
