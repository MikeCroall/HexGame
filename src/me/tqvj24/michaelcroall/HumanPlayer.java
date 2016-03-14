package me.tqvj24.michaelcroall;

public class HumanPlayer implements PlayerInterface {

    //TODO implement class

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
}
