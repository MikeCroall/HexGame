public class Main_tqvj24 {

    private static void playHumanVsHuman(int boardWidth, int boardHeight){
        GameManager g = new GameManager();
        HumanPlayer r = new HumanPlayer();
        HumanPlayer b = new HumanPlayer();
        boolean ready = false;
        try {
            r.setColour(Piece.RED);
            b.setColour(Piece.BLUE);
            g.specifyPlayer(r, Piece.RED);
            g.specifyPlayer(b, Piece.BLUE);
            g.boardSize(boardWidth, boardHeight);
            ready = true;
        } catch (ColourAlreadySetException e) {
            System.out.println("That colour has already been set!");
        } catch (InvalidColourException e) {
            System.out.println("Invalid colour assigned to player!");
        } catch (BoardAlreadySizedException e) {
            System.out.println("Board has already been sized!");
        } catch (InvalidBoardSizeException e) {
            System.out.println("That's not a valid board size!");
        }
        if(ready) {
            g.playGame();
        }
    }

    private static void playHumanVsComputer(int boardWidth, int boardHeight, boolean humanAsRed){
        GameManager g = new GameManager();
        HumanPlayer h = new HumanPlayer();
        ComputerPlayer_tqvj24 c = new ComputerPlayer_tqvj24();
        boolean ready = false;
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
            ready = true;
        } catch (ColourAlreadySetException e) {
            System.out.println("That colour has already been set!");
        } catch (InvalidColourException e) {
            System.out.println("Invalid colour assigned to player!");
        } catch (BoardAlreadySizedException e) {
            System.out.println("Board has already been sized!");
        } catch (InvalidBoardSizeException e) {
            System.out.println("That's not a valid board size!");
        }
        if(ready) {
            g.playGame();
        }
    }

    private static void playComputerVsComputer(int boardWidth, int boardHeight){
        GameManager g = new GameManager();
        ComputerPlayer_tqvj24 r = new ComputerPlayer_tqvj24();
        ComputerPlayer_tqvj24 b = new ComputerPlayer_tqvj24();
        boolean ready = false;
        try {
            r.setColour(Piece.RED);
            b.setColour(Piece.BLUE);
            g.specifyPlayer(r, Piece.RED);
            g.specifyPlayer(b, Piece.BLUE);
            g.boardSize(boardWidth, boardHeight);
            ready = true;
        } catch (ColourAlreadySetException e) {
            System.out.println("That colour has already been set!");
        } catch (InvalidColourException e) {
            System.out.println("Invalid colour assigned to player!");
        } catch (BoardAlreadySizedException e) {
            System.out.println("Board has already been sized!");
        } catch (InvalidBoardSizeException e) {
            System.out.println("That's not a valid board size!");
        }
        if(ready) {
            g.playGame();
        }
    }

    public static void main(String[] args){
        //playHumanVsHuman(5,5);
        //playHumanVsComputer(5,5, false);
        playComputerVsComputer(11,11);
    }
}
