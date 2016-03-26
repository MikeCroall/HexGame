import java.util.Random;

public class ComputerPlayer_tqvj24 implements PlayerInterface{

    private Piece colour;

    public ComputerPlayer_tqvj24(){ colour = Piece.UNSET; }

    @Override
    public MoveInterface makeMove(Piece[][] boardView) throws NoValidMovesException {
        if (!hasValidMove(boardView)){ throw new NoValidMovesException(); }

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


        //Piece currentPlayer = colour;


        return myMove;
        //TODO improve makeMove method
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

}
