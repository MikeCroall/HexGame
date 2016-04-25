public class GameManager implements GameManagerInterface {

    private PlayerInterface redPlayer, bluePlayer;
    private Board board;

    @Override
    public boolean specifyPlayer(PlayerInterface player, Piece colour) throws InvalidColourException, ColourAlreadySetException {
        if (colour == Piece.UNSET){
            throw new InvalidColourException();
        }
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
        System.out.println("Error: Failed to specify player");
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
        try {
            board = new Board();
            if(!board.setBoardSize(sizeX, sizeY)){
                System.out.println("Error: Failed to set board size");
                return false;
            }
        }catch(Exception e){
            System.out.println("Error: Failed to set board size");
            return false;
        }
        return true;
    }

    @Override
    public boolean playGame() {
        try {
            Piece winner = Piece.UNSET;
            while (winner == Piece.UNSET){
                //Red move first
                winner = takeTurn(redPlayer, Piece.RED);
                //Ensure Red hasn't won before letting Blue take a turn
                if(winner == Piece.UNSET) {
                    winner = takeTurn(bluePlayer, Piece.BLUE);
                }
            }
            //Game finished, show the final board state
            BoardManager_tqvj24.printBoard(board.getBoardView());

            if (winner == Piece.RED) {
                if(!redPlayer.finalGameState(GameState.WON)){ System.out.println("Error: Failed to send red player their final game state"); }
                if(!bluePlayer.finalGameState(GameState.LOST)) { System.out.println("Error: Failed to send blue player their final game state"); }
            } else {
                if(!redPlayer.finalGameState(GameState.LOST)) { System.out.println("Error: Failed to send red player their final game state"); }
                if(!bluePlayer.finalGameState(GameState.WON)) { System.out.println("Error: Failed to send Blue player their final game state"); }
            }

        } catch(NoBoardDefinedException noBoardEx){
            System.out.println("No board has been defined!");
            return false;
        } catch(NoValidMovesException noMovesEx){
            System.out.println("There are no valid moves to make!");
            return false;
        } catch(Exception e){
            System.out.println("Game ended unexpectedly");
            return false;
        }
        return true;
    }

    //Non-interface methods
    private Piece takeTurn(PlayerInterface player, Piece colour) throws NoBoardDefinedException, NoValidMovesException{
        boolean moveMade = false;
        boolean conceded = false;
        while(!(moveMade || conceded)){
            try{
                MoveInterface move = player.makeMove(board.getBoardView());
                if(move.hasConceded()) {
                    conceded = true;
                    System.out.println("Conceding to opponent acknowledged");
                } else {
                    moveMade = board.placePiece(colour, move);
                    if(moveMade) {
                        System.out.println("Move made successfully");
                    }else{
                        System.out.println("Error: Piece failed to place");
                    }
                }
            } catch (PositionAlreadyTakenException e) {
                System.out.println("That position is already taken!\n\nPlease try again!");
            } catch (InvalidPositionException e) {
                System.out.println("That is not a valid position!\n\nPlease try again!");
            } catch (InvalidColourException e) {
                System.out.println("Invalid colour given to place");
            }
            //These catches are here, as they can be retried, and do not mean the game is currently unplayable
        }
        if(conceded) {
            if (colour == Piece.RED) { return Piece.BLUE; }
            else { return Piece.RED; }
        } else { return board.gameWon(); }
    }

}
