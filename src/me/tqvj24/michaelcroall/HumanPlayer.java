package me.tqvj24.michaelcroall;

import java.util.Scanner;

public class HumanPlayer implements PlayerInterface {

    Piece colour;

    public HumanPlayer(){
        colour = Piece.UNSET;
    }

    @Override
    public MoveInterface makeMove(Piece[][] boardView) throws NoValidMovesException {
        if (!hasValidMove(boardView))
            throw new NoValidMovesException();

        Move move = new Move();
        boolean validPosition = false;

        printBoard(boardView);

        while(!validPosition) {
            validPosition = true;
            String choice = getValidInput();
            String[] items = choice.split(" ");

            if (items.length == 1) {
                move.setConceded();
            } else {
                //length is 2, getValidInput ensures this
                int x = Integer.parseInt(items[0]);
                int y = Integer.parseInt(items[1]);

                try {
                    move.setPosition(x, y);
                } catch (InvalidPositionException ex) {
                    validPosition = false;
                    System.out.println("That's not a valid position");
                }
            }
        }

        return move;
    }

    @Override
    public boolean setColour(Piece colour) throws InvalidColourException, ColourAlreadySetException {
        if (this.colour != Piece.UNSET)
            throw new ColourAlreadySetException();
        if(colour == Piece.UNSET)
            throw new InvalidColourException();
        this.colour = colour;
        return true;
    }

    @Override
    public boolean finalGameState(GameState state) {
        if (state == GameState.WON)
            System.out.println("Congratulations, you have won!");
        else if (state == GameState.LOST)
            System.out.println("Unfortunately, you have lost.");
        return true;
    }

    //Non-interface methods
    //TODO print borders/markers, and optimise
    private void printBoard(Piece[][] grid){
        //Print top of hexs
        System.out.print("\n\n  ");
        for (int x = 0; x < grid[0].length; x++) {
            System.out.print(" / \\");
        }
        System.out.println();

        for (int y = 0; y < grid.length; y++) {
            //Start at the correct position
            String leftPadding = "";
            for (int spaces = 0; spaces < 2 * y; spaces++)
                leftPadding += " ";
            System.out.print(leftPadding + "  ");

            //Top of hex is present, print middle row
            for (int x = 0; x < grid[y].length; x++)
                System.out.print("| " + getLetter(grid[y][x]) + " ");
            System.out.print("|\n" + leftPadding);

            //Close off hex (and top of next row, if exists)
            for (int x = 0; x < grid[y].length; x++)
                System.out.print((x == 0 ? "  " : " /") + " \\");
            System.out.println(" /" + (y < grid.length - 1 ? " \\" : ""));
        }
        System.out.println();
    }

    private String getLetter(Piece colour){
        switch(colour){
            case RED:
                return "R";
            case BLUE:
                return "B";
            case UNSET:
                return " ";
            default:
                return "?";
        }
    }

    private boolean hasValidMove(Piece[][] grid){
        for (Piece[] row : grid)
            for (Piece piece : row)
                if (piece == Piece.UNSET)
                    return true;
        return false;
    }

    private String getValidInput(){
        Scanner scan = new Scanner(System.in);
        String input = "", output = "";
        boolean valid = false;

        while (!valid){
            valid = true;
            System.out.println("Please enter the move you wish to make.\n\tEnter 'concede' to concede to your opponent\n\tEnter coordinates in the form 'x y' to make a move\nChoice: ");
            input = scan.nextLine();
            String items[]  = input.trim().toLowerCase().replace(",", " ").replace("'", "").split(" ");
            if (items.length == 1){
                //Probably conceding, double check
                if (items[0].equals("concede"))
                    output = items[0];
                else { valid = false; }
            }else if (items.length == 2){
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
        }
        return output;
    }

}
