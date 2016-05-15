import java.awt.*;
import java.lang.Math;
import java.util.ArrayList;

public class ComputerPlayer_tom_oli implements PlayerInterface {
    private Piece playerColour;
    private GameState state;

    public ComputerPlayer_tom_oli() {
        state = GameState.INCOMPLETE;
    }

    public MoveInterface makeMove(Piece[][] boardView) {
        try {
            boolean full = true;
            for (int i = 0; i < boardView.length; i++) {
                for (int j = 0; j < boardView[0].length; j++) {
                    if (boardView[i][j] == Piece.UNSET) {
                        full = false;
                    }
                }
            }
            if (full) {
                throw new NoValidMovesException();
            }
        } catch (NoValidMovesException e) {
            System.out.println(e);
            System.out.println("Game board is full, game over");
        }
        MoveInterface move = new Move();
        int x = (int) (Math.random() * boardView.length);
        int y = (int) (Math.random() * boardView[0].length);
        boolean moved = false;
        while (!moved) {
            if (boardView[x][y] != Piece.UNSET) {
                ArrayList<Point> freeSpaces = BoardManager_mike.getFreeSpaces(boardView);
                Point p = freeSpaces.get((int)(Math.random() * freeSpaces.size()));
                x = p.x;
                y = p.y;
            } else {
                moved = true;
            }
        }
        try {
            if (x < 0 || y < 0 || x > boardView.length - 1 || y > boardView[0].length - 1) {
                throw new InvalidPositionException();
            }
            move.setPosition(x, y);
        } catch (InvalidPositionException e) {
            System.out.println(e);
        }
        return move;
    }

    public boolean setColour(Piece colour) {
        try {
            if (colour != Piece.RED && colour != Piece.BLUE) {
                throw new InvalidColourException();
            }
            if (playerColour != null) {
                throw new ColourAlreadySetException();
            }
            playerColour = colour;
        } catch (InvalidColourException e) {
            System.out.println(e);
        } catch (ColourAlreadySetException e) {
            System.out.println(e);
        }
        return true;
    }

    public GameState getGameState() {
        return state;
    }

    public boolean finalGameState(GameState state) {
        this.state = state;
        if (state == GameState.WON) {
            //System.out.println("Congratulations " + playerColour.name() + " player, you've won!");
        } else if (state == GameState.LOST) {
            //System.out.println("Commiserations " + playerColour.name() + " player, you lost.");
        } else {
            //System.out.println("An error has occured");
            return false;
        }
        return true;
    }
}

