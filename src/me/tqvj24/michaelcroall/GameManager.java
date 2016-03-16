package me.tqvj24.michaelcroall;

public class GameManager implements GameManagerInterface {

    private PlayerInterface redPlayer, bluePlayer;
    private Board board;

    @Override
    public boolean specifyPlayer(PlayerInterface player, Piece colour) throws ColourAlreadySetException {
        if (colour == Piece.RED){
            if (redPlayer != null) {
                throw new ColourAlreadySetException();
            }
            redPlayer = player;
            return true;
        } else if(colour == Piece.BLUE){
            if (bluePlayer != null) {
                throw new ColourAlreadySetException();
            }
            bluePlayer = player;
            return true;
        }
        return false;
    }

    @Override
    public boolean boardSize(int sizeX, int sizeY) throws InvalidBoardSizeException, BoardAlreadySizedException {
        if (board != null) {
            throw new BoardAlreadySizedException();
        }
        if(sizeX < 1 || sizeY < 1) {
            throw new InvalidBoardSizeException();
        }
        board = new Board();
        board.setBoardSize(sizeX, sizeY);
        return true;
    }

    @Override
    public boolean playGame() {
        try {
            Piece winner = Piece.UNSET;
            boolean moveMade, conceded;
            while (winner == Piece.UNSET){

                moveMade = false;
                conceded = false;
                //Red moves first
                while(!moveMade){
                    try{
                        MoveInterface redMove = redPlayer.makeMove(board.getBoardView());
                        if(redMove.hasConceded()) {
                            conceded = true; moveMade = true;
                            System.out.println("Conceding to opponent acknowledged");
                        } else {
                            moveMade = board.placePiece(Piece.RED, redMove);
                            if(moveMade) {
                                System.out.println("Move completed");
                            }
                        }
                    } catch (PositionAlreadyTakenException e) {
                        System.out.println("That position is already taken!");
                    } catch (InvalidPositionException e) {
                        System.out.println("That is not a valid position!\n\nPlease try again!");
                    } catch (InvalidColourException e) {
                        System.out.println("Invalid colour given (likely internal error)");
                    }
                    //These catches are here, as they can be retried, and do not mean the game is currently unplayable
                }
                winner = conceded ? Piece.BLUE : board.gameWon();
                conceded = false;
                //Ensure Red hasn't won before letting Blue take a turn
                if(winner == Piece.UNSET) {
                    moveMade = false;
                    //Blue moves next
                    while (!moveMade) {
                        try {
                            MoveInterface blueMove = bluePlayer.makeMove(board.getBoardView());
                            if(blueMove.hasConceded()) {
                                conceded = true; moveMade = true;
                                System.out.println("Conceding to opponent acknowledged");
                            } else {
                                moveMade = board.placePiece(Piece.BLUE, blueMove);
                                if(moveMade){
                                    System.out.println("Move completed");
                                }
                            }
                        } catch (PositionAlreadyTakenException e) {
                            System.out.println("That position is already taken!");
                        } catch (InvalidPositionException e) {
                            System.out.println("That is not a valid position!");
                        } catch (InvalidColourException e) {
                            System.out.println("Invalid colour given (likely internal error)");
                        }
                        //These catches are here, as they can be retried, and do not mean the game is currently unplayable
                    }
                    winner = conceded ? Piece.RED : board.gameWon();
                }
            }

            HumanPlayer.printBoard(board.getBoardView());

            if (winner == Piece.RED) {
                redPlayer.finalGameState(GameState.WON);
                bluePlayer.finalGameState(GameState.LOST);
            } else {
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

    public static void main(String[] args){
        GameManager g = new GameManager();
        HumanPlayer r = new HumanPlayer();
        HumanPlayer b = new HumanPlayer();
        try {
            r.setColour(Piece.RED);
            b.setColour(Piece.BLUE);
            g.specifyPlayer(r, Piece.RED);
            g.specifyPlayer(b, Piece.BLUE);
            g.boardSize(5, 5);
        } catch (ColourAlreadySetException e) {
            System.out.println("That colour has already been set!");
        } catch (InvalidColourException e) {
            System.out.println("Invalid colour assigned to player!");
        } catch (BoardAlreadySizedException e) {
            System.out.println("Board has already been sized!");
        } catch (InvalidBoardSizeException e) {
            System.out.println("That's not a valid board size!");
        }
        g.playGame();
    }
}
