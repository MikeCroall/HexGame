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
            while (winner == Piece.UNSET){
                //Red move first
                winner = takeTurn(redPlayer, Piece.RED);
                //Ensure Red hasn't won before letting Blue take a turn
                if(winner == Piece.UNSET) {
                    winner = takeTurn(bluePlayer, Piece.BLUE);
                }
            }
            //Game finished, show the final board state
            BoardManager.printBoard(board.getBoardView());

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
        return conceded ? Piece.BLUE : board.gameWon();
    }


    //BELOW is for testing purposes only


    private static void playHumanVsHuman(int boardWidth, int boardHeight){
        GameManager g = new GameManager();
        HumanPlayer r = new HumanPlayer();
        HumanPlayer b = new HumanPlayer();
        try {
            r.setColour(Piece.RED);
            b.setColour(Piece.BLUE);
            g.specifyPlayer(r, Piece.RED);
            g.specifyPlayer(b, Piece.BLUE);
            g.boardSize(boardWidth, boardHeight);
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

    private static void playHumanVsComputer(int boardWidth, int boardHeight, boolean humanAsRed){
        GameManager g = new GameManager();
        HumanPlayer h = new HumanPlayer();
        ComputerPlayer_tqvj24 c = new ComputerPlayer_tqvj24();
        try {
            if(humanAsRed) {
                h.setColour(Piece.RED);
                c.setColour(Piece.BLUE);
                g.specifyPlayer(h, Piece.RED);
                g.specifyPlayer(c, Piece.BLUE);
            }else{
                h.setColour(Piece.BLUE);
                c.setColour(Piece.RED);
                g.specifyPlayer(h, Piece.BLUE);
                g.specifyPlayer(c, Piece.RED);
            }
            g.boardSize(boardWidth, boardHeight);
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

    private static void playComputerVsComputer(int boardWidth, int boardHeight){
        GameManager g = new GameManager();
        ComputerPlayer_tqvj24 r = new ComputerPlayer_tqvj24();
        ComputerPlayer_tqvj24 b = new ComputerPlayer_tqvj24();
        try {
            r.setColour(Piece.RED);
            b.setColour(Piece.BLUE);
            g.specifyPlayer(r, Piece.RED);
            g.specifyPlayer(b, Piece.BLUE);
            g.boardSize(boardWidth, boardHeight);
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

    public static void main(String[] args){
        //playHumanVsHuman(5,5);
        playHumanVsComputer(4,3, true);
        //playComputerVsComputer(3,3);
    }
}
