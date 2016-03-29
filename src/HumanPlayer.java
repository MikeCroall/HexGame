import java.util.Scanner;

public class HumanPlayer implements PlayerInterface {

    private Piece colour;

    public HumanPlayer(){
        colour = Piece.UNSET;
    }

    @Override
    public MoveInterface makeMove(Piece[][] boardView) throws NoValidMovesException {
        if (!hasValidMove(boardView)) {
            throw new NoValidMovesException();
        }

        Move move = new Move();
        boolean validPosition = false;

        BoardManager_tqvj24.printBoard(boardView);

        while(!validPosition) {
            validPosition = true;
            String choice = getValidInput();
            String[] items = choice.split(" ");

            if (items.length == 1) {
                move.setConceded();
            } else {
                //length is 2, items are int, getValidInput ensures this
                int x = Integer.parseInt(items[0]);
                int y = Integer.parseInt(items[1]);

                try {
                    move.setPosition(x, y);
                } catch (InvalidPositionException ex) {
                    validPosition = false;
                    System.out.println("That is not a valid position");
                }
            }
        }

        return move;
    }

    @Override
    public boolean setColour(Piece colour) throws InvalidColourException, ColourAlreadySetException {
        if (this.colour != Piece.UNSET) {
            {
                throw new ColourAlreadySetException();
            }
        }
        if(colour == Piece.UNSET) {
            throw new InvalidColourException();
        }
        this.colour = colour;
        return true;
    }

    @Override
    public boolean finalGameState(GameState state) {
        if (state == GameState.WON) {
            System.out.println("Congratulations " + getPlayerName() + ", you have won!");
        } else if (state == GameState.LOST) {
            System.out.println("Unfortunately " + getPlayerName() + ", you have lost.");
        }
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

    private String getValidInput(){
        Scanner scan = new Scanner(System.in);
        String input, output = "";
        boolean valid = false;

        while (!valid){
            valid = true;
            System.out.print("Please enter the move you wish to make, " + getPlayerName() + " player.\n\tEnter 'concede' to concede to your opponent\n\tEnter coordinates in the form 'x y' to make a move\nChoice: ");
            input = scan.nextLine();
            String items[]  = input.trim().toLowerCase().replace(",", " ").replace("'", "").split(" ");
            if (items.length == 1){
                //Probably conceding, double check
                if (items[0].equals("concede")) {
                    output = items[0];
                } else { valid = false; }
            } else if (items.length == 2){
                //Probably coordinates, double check
                int x, y;
                Scanner numCheck = new Scanner(items[0]);
                if (numCheck.hasNextInt()){
                    x = numCheck.nextInt();
                    numCheck = new Scanner(items[1]);
                    if (numCheck.hasNextInt()){
                        y = numCheck.nextInt();
                        output = "" + x + " " + y;
                    } else { valid = false; }
                } else {valid = false; }
            } else {valid = false; }

            if(!valid){
                System.out.println("Invalid input - please try again!");
            }
        }
        return output;
    }

}
