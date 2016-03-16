package me.tqvj24.michaelcroall;

//TODO implement class
public class ComputerPlayer_tqvj24 implements PlayerInterface{
    @Override
    public MoveInterface makeMove(Piece[][] boardView) throws NoValidMovesException {
        return null;
    }

    @Override
    public boolean setColour(Piece colour) throws InvalidColourException, ColourAlreadySetException {
        return false;
    }

    @Override
    public boolean finalGameState(GameState state) {
        return false;
    }

    //Non-interface methods

}
