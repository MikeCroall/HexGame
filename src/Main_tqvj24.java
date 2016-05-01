public class Main_tqvj24 {

    private static void playHumanVsHuman(int boardWidth, int boardHeight) {
        GameManager g = new GameManager();
        HumanPlayer r = new HumanPlayer();
        HumanPlayer b = new HumanPlayer();
        boolean ready = false;
        try {
            if (g.specifyPlayer(r, Piece.RED)) {
                if (g.specifyPlayer(b, Piece.BLUE)) {
                    if (g.boardSize(boardWidth, boardHeight)) {
                        ready = true;
                    }
                }
            }
        } catch (ColourAlreadySetException e) {
            System.out.println("That colour has already been set!");
        } catch (InvalidColourException e) {
            System.out.println("Invalid colour assigned to player!");
        } catch (BoardAlreadySizedException e) {
            System.out.println("Board has already been sized!");
        } catch (InvalidBoardSizeException e) {
            System.out.println("That's not a valid board size!");
        }
        if (ready) {
            g.playGame();
        }
    }

    private static void playHumanVsComputer(int boardWidth, int boardHeight, boolean humanAsRed) {
        GameManager g = new GameManager();
        HumanPlayer h = new HumanPlayer();
        ComputerPlayer_tqvj24 c = new ComputerPlayer_tqvj24();
        boolean ready = false;
        try {
            if (humanAsRed) {
                if (g.specifyPlayer(h, Piece.RED)) {
                    if (g.specifyPlayer(c, Piece.BLUE)) {
                        ready = true;
                    }
                }
            } else {
                if (g.specifyPlayer(h, Piece.BLUE)) {
                    if (g.specifyPlayer(c, Piece.RED)) {
                        ready = true;
                    }
                }
            }
            if (!g.boardSize(boardWidth, boardHeight)) {
                ready = false;
            }
        } catch (ColourAlreadySetException e) {
            System.out.println("That colour has already been set!");
        } catch (InvalidColourException e) {
            System.out.println("Invalid colour assigned to player!");
        } catch (BoardAlreadySizedException e) {
            System.out.println("Board has already been sized!");
        } catch (InvalidBoardSizeException e) {
            System.out.println("That's not a valid board size!");
        }
        if (ready) {
            g.playGame();
        }
    }

    private static Piece playComputerVsComputer(int boardWidth, int boardHeight) {
        GameManager g = new GameManager();
        RandomPlayer_tqvj24 r = new RandomPlayer_tqvj24();
        ComputerPlayer_tqvj24 b = new ComputerPlayer_tqvj24();
        boolean ready = false;
        try {
            if (g.specifyPlayer(r, Piece.RED)) {
                if (g.specifyPlayer(b, Piece.BLUE)) {
                    if (g.boardSize(boardWidth, boardHeight)) {
                        ready = true;
                    }
                }
            }
        } catch (ColourAlreadySetException e) {
            System.out.println("That colour has already been set!");
        } catch (InvalidColourException e) {
            System.out.println("Invalid colour assigned to player!");
        } catch (BoardAlreadySizedException e) {
            System.out.println("Board has already been sized!");
        } catch (InvalidBoardSizeException e) {
            System.out.println("That's not a valid board size!");
        }
        if (ready) {
            g.playGame();
            if (r.getFinalGameState() == GameState.WON){
                return Piece.RED;
            }else if(b.getFinalGameState() == GameState.WON){
                return Piece.BLUE;
            }else{
                return Piece.UNSET;
            }
        }
        return null;
    }

    private static void playMultiple(int games){
        int redWin = 0, blueWin = 0, noWin = 0;
        for (int game = 1; game < games + 1; game++) {
            Piece winner = playComputerVsComputer(12, 12);
            if (winner == Piece.RED) {
                redWin++;
            }else if(winner == Piece.BLUE){
                blueWin++;
            }else{
                noWin++;
            }
            System.out.println("Game " + game + " completed");
        }
        System.out.println("\nRed win: " + redWin + "\nBlue win: " + blueWin + "\nNo win: " + noWin);
    }

    public static void main(String[] args) {
        //playHumanVsHuman(5,5);
        //playHumanVsComputer(5,5, false);
        //playComputerVsComputer(15, 15);
        playMultiple(1000);
    }
}
