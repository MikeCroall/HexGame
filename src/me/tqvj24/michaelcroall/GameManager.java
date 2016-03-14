package me.tqvj24.michaelcroall;

public class GameManager implements GameManagerInterface {

    PlayerInterface redPlayer, bluePlayer;
    Board board;

    @Override
    public boolean specifyPlayer(PlayerInterface player, Piece colour) throws ColourAlreadySetException {
        if (colour == Piece.RED){
            if (redPlayer != null)
                throw new ColourAlreadySetException();
            redPlayer = player;
            return true;
        } else if(colour == Piece.BLUE){
            if (bluePlayer != null)
                throw new ColourAlreadySetException();
            bluePlayer = player;
            return true;
        }
        return false;
    }

    @Override
    public boolean boardSize(int sizeX, int sizeY) throws InvalidBoardSizeException, BoardAlreadySizedException {
        if (board != null)
            throw new BoardAlreadySizedException();
        if(sizeX < 1 || sizeY < 1)
            throw new InvalidBoardSizeException();
        board = new Board();
        board.setBoardSize(sizeX, sizeY);
        return true;
    }

    @Override
    public boolean playGame() {
        //TODO implement method
        return false;
    }
}
