import java.awt.*;
import java.util.Scanner;

public class HumanPlayer implements PlayerInterface {

    private Piece colour;
    private GameState currentGameState;

    public HumanPlayer() {
        colour = Piece.UNSET;
        currentGameState = GameState.INCOMPLETE;
    }

    @Override
    public MoveInterface makeMove(Piece[][] boardView) throws NoValidMovesException {
        if (colour == Piece.UNSET || currentGameState != GameState.INCOMPLETE|| !BoardManager_tqvj24.hasValidMove(boardView)) {
            throw new NoValidMovesException();
        }

        MoveInterface move = new Move();
        boolean validPosition = false;

        BoardManager_tqvj24.printBoard(boardView);

        while (!validPosition) {
            validPosition = true;
            Point choice = getValidInput();

            if (choice == null) {
                if (!move.setConceded()) {
                    System.out.println("Error: Failed to set conceded");
                    validPosition = false;
                }
            } else {
                try {
                    move.setPosition(choice.x, choice.y);
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
            throw new ColourAlreadySetException();
        }
        if (colour == Piece.UNSET) {
            throw new InvalidColourException();
        }
        try {
            this.colour = colour;
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @Override
    public boolean finalGameState(GameState state) {
        try {
            currentGameState = state;
            if (state == GameState.WON) {
                System.out.println("Congratulations " + getPlayerName() + ", you have won!");
            } else if (state == GameState.LOST) {
                System.out.println("Unfortunately " + getPlayerName() + ", you have lost.");
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    //Non-interface methods
    public GameState getCurrentGameState(){
        return currentGameState;
    }

    private String getPlayerName() {
        return colour.name().toLowerCase();
    }

    private Point getValidInput() {
        Scanner scan = new Scanner(System.in);
        String input;
        Point output = null;
        boolean valid = false;

        while (!valid) {
            valid = true;
            System.out.print("Please enter the move you wish to make, " + getPlayerName() + " player.\n\tEnter 'concede' to concede to your opponent\n\tEnter coordinates in the form 'x y' to make a move\nChoice: ");
            input = scan.nextLine();
            String items[] = input.toLowerCase().replace(",", " ").replace("'", "").replace("(", "").replace(")", "").trim().split(" ");
            if (items.length == 1) { //Probably conceding, double check
                if (items[0].equals("concede")) {
                    output = null;
                } else {
                    valid = false;
                }
            } else if (items.length == 2) { //Probably coordinates, double check
                int x, y;
                Scanner numCheck = new Scanner(items[0]);
                if (numCheck.hasNextInt()) {
                    x = numCheck.nextInt();
                    numCheck = new Scanner(items[1]);
                    if (numCheck.hasNextInt()) {
                        y = numCheck.nextInt();
                        output = new Point(x, y);
                    } else {
                        valid = false;
                    }
                } else {
                    valid = false;
                }
            } else {
                valid = false;
            }

            if (!valid) {
                System.out.println("Invalid input - please try again!");
            }
        }
        return output;
    }

}
