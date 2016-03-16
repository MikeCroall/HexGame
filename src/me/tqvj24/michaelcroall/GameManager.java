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
        try {
            Piece winner = Piece.UNSET;
            boolean moveMade;
            while (winner == Piece.UNSET){

                moveMade = false;
                //Red moves first
                while(!moveMade){
                    try{
                        moveMade = board.placePiece(Piece.RED, redPlayer.makeMove(board.getBoardView()));
                    } catch (PositionAlreadyTakenException e) {
                        System.out.println("That position is already taken!");
                    } catch (InvalidPositionException e) {
                        System.out.println("That is not a valid position!");
                    } catch (InvalidColourException e) {
                        System.out.println("Invalid colour given (likely internal error)");
                    }
                    //These catches are here, as they can be retried, and do not mean the game is currently unplayable
                }
                winner = board.gameWon();
                //Ensure Red hasn't won before letting Blue take a turn
                if(winner == Piece.UNSET) {
                    moveMade = false;
                    //Blue moves next
                    while (!moveMade) {
                        try {
                            moveMade = board.placePiece(Piece.BLUE, bluePlayer.makeMove(board.getBoardView()));
                        } catch (PositionAlreadyTakenException e) {
                            System.out.println("That position is already taken!");
                        } catch (InvalidPositionException e) {
                            System.out.println("That is not a valid position!");
                        } catch (InvalidColourException e) {
                            System.out.println("Invalid colour given (likely internal error)");
                        }
                        //These catches are here, as they can be retried, and do not mean the game is currently unplayable
                    }
                    winner = board.gameWon();
                }
            }
            if (winner == Piece.RED){
                redPlayer.finalGameState(GameState.WON);
                bluePlayer.finalGameState(GameState.LOST);
            }else{
                redPlayer.finalGameState(GameState.LOST);
                bluePlayer.finalGameState(GameState.WON);
            }
        } catch(NoBoardDefinedException noBoardEx){
            System.out.println("No board has been defined!");
        } catch(NoValidMovesException noMovesEx){
            System.out.println("There are no valid moves to make!");
        }

        return true; // TODO find out what this return is meant to mean
    }
}
