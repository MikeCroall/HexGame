import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by ryan on 16/03/16.
 */
public class ComputerPlayer_ryan implements PlayerInterface {


    /*
    Makrs for:
    only going for unoccupied cells
    marked using the tournament.
     */
    private Piece colour;
    private GameState currentState;
    public ComputerPlayer_ryan() {
        colour = Piece.UNSET;
    }


    private boolean isBoardFull(Piece[][] boardView) {
        //TODO: optimise with while
        boolean occupied = true;
        for (Piece[] pieces : boardView)
            for (Piece piece : pieces) {
                if (piece == null || piece == Piece.UNSET)
                    occupied = false;
            }

        return occupied; //make it not use NOT, as this is unclean.
    }

    public String toString() {
        if (colour == Piece.RED) {
            return "Red (Computer)";
        } else if (colour == Piece.BLUE) {
            return "Blue (Computer)";
        }
        return "Undefined";
    }

    private List<MoveInterface> getValidMoves(Piece[][] boardView) {
        List<MoveInterface> moves = new ArrayList<MoveInterface>();
        for (int x = 0; x < boardView.length; x++)
            for (int y = 0; y < boardView[0].length; y++) {
                if (boardView[x][y] == Piece.UNSET || boardView[x][y] == null) {
                    MoveInterface m = new Move();
                    try {
                        m.setPosition(x, y); //create a move with the current position if the position is free
                    } catch (Exception ex) {
                        ex.printStackTrace(); //this shouldn't happen.
                    }
                    moves.add(m);
                }
            }

        return moves;
    }


    public MoveInterface makeMove(Piece[][] boardView) throws NoValidMovesException {

        if (isBoardFull(boardView)) //== true
            throw new NoValidMovesException();

        //we want to create a list of valid moves, so that we can store them
        List<MoveInterface> validMoves = getValidMoves(boardView);

        BirdCage b = new BirdCage(boardView);
        Move bestMove = b.getBestMove(colour);
        if (bestMove == null) {
            //this means that the best move is not found.
            //we therefore select a random move from the valid moves available.
            Random randGen = new Random();
            return validMoves.get(randGen.nextInt(validMoves.size()));

        }
        return bestMove;

    }

    /**
     * Set the colour that this player will be
     *
     * @param colour A Piece (RED/BLUE) that this player will be
     * @return true indicating that the method succeeded
     * @throws InvalidColourException    A colour other than RED/BLUE was provided
     * @throws ColourAlreadySetException The colour has already been set for this player.
     */
    public boolean setColour(Piece colour) throws InvalidColourException, ColourAlreadySetException {

        if (this.colour != Piece.UNSET)
            throw new ColourAlreadySetException();

        if (colour != Piece.BLUE && colour != Piece.RED)
            throw new InvalidColourException();

        this.colour = colour;

        return true;
    }

    /**
     * Informs the player of the final game state. Player has Won, lost.
     *
     * @param state either WON or LOST
     * @return true indicating method has compleated successfully.
     */
    public GameState getCurrentState() {
        return currentState;
    }
    public boolean finalGameState(GameState state) {

        currentState = state;
        //if incomplete, just ignore and don't output anything.
        switch (state) {
            case WON:
                //System.out.println(this.toString() + " - you won! :D");

                break;
            case LOST:
                //System.out.println(this.toString() + " - you lost :( Sorry about that...");
                break;
        }
        return true;
    }


}
