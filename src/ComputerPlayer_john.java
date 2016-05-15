import java.util.Collections;
import java.util.ArrayList;
import java.util.HashMap;

public class ComputerPlayer_john implements PlayerInterface {
    private Piece colour;
    private Piece enemy;

    public ComputerPlayer_john() {
        colour = Piece.UNSET;
    }


    /**
     * Searches for a move to make in order of effectiveness.
     * i.e. if a constructive move exists, make that move, else search for a destructive move....
     *
     * @param boardView the current state of the board
     * @return a Move object representing the desired place to place a piece
     * @throws NoValidMovesException Indicates that no valid moves are possible - e.g. all cells on the
     *                               board are already occupied by a piece.
     */
    public MoveInterface makeMove(Piece[][] boardView) throws NoValidMovesException {
        // First checks that a valid move exists.
        if (findAnyMove(boardView) == null) {
            throw new NoValidMovesException();
        }
        // Estimates the direction the enemy is 'heading' in
        int attackAngle = findAttackAngle(boardView);
        // Then checks for different types of moves in order of effectiveness
        MoveInterface move = firstMove(boardView);
        if (move == null) {
            move = findConstructiveMove(boardView, true);
            if (move == null) {
                move = findGoodMove(boardView, attackAngle, true);
                if (move == null) {
                    move = findDestructiveMove(boardView, true);
                    if (move == null) {
                        move = findConstructiveMove(boardView, false);
                        if (move == null) {
                            move = findDestructiveMove(boardView, false);
                            if (move == null) {
                                move = findGoodMove(boardView, attackAngle, false);
                                // If all else fails, find any old move.
                                if (move == null) {
                                    move = findAnyMove(boardView);
                                }
                            }
                        }
                    }
                }
            }
        }
        return move;
    }

    /**
     * Creates a move at a specific x and y co-ordinate, mostly to prevent code duplication
     *
     * @param x,y the co-ordinates that the move will be made
     * @return a Move object representing the desired place to place a piece
     */
    private MoveInterface createMove(int x, int y) {
        MoveInterface move = new Move();
        try {
            move.setPosition(x, y);
        }
        // Never actually happens
        catch (InvalidPositionException e) {
        }
        return move;
    }

    /**
     * Creates a shuffled list of the possible x or y values on the board
     * Used to iterate through all co-ordinates in a random way. Makes the bot less predictable.
     *
     * @param boardView the current state of the board
     *                  wantX      true if we want a list of x values, false for y
     * @return a shuffled list of the possible values for the co-ordinate requested
     */
    private ArrayList<Integer> shuffleCoords(Piece[][] boardView, boolean wantX) {
        ArrayList<Integer> shuffledCoords = new ArrayList<Integer>();
        // If we want x, add all x values to arraylist
        if (wantX) {
            for (int x = 0; x < boardView.length; x++) {
                shuffledCoords.add(x);
            }
        }
        // Otherwise add all y values
        else {
            for (int y = 0; y < boardView[0].length; y++) {
                shuffledCoords.add(y);
            }
        }
        // Shuffle the list
        Collections.shuffle(shuffledCoords);
        return shuffledCoords;
    }

    /**
     * If we have the first move, place a piece in the center of the board.
     *
     * @param boardView the current state of the board
     * @return the move if one is made, otherwise null.
     */
    private MoveInterface firstMove(Piece[][] boardView) {
        // If any piece is on the board, do nothing
        for (int x = 0; x < boardView.length; x++) {
            for (int y = 0; y < boardView[0].length; y++) {
                if (checkCell(boardView, x, y, enemy) || checkCell(boardView, x, y, colour)) {
                    return null;
                }
            }
        }
        // Otherwise place a piece in the middle.
        int x = boardView.length / 2 - 1;
        int y = boardView[0].length / 2;
        // Adjust for odddly sized board
        if (boardView.length % 2 == 1) {
            x++;
        }
        return createMove(x, y);
    }

    /**
     * Calculates the attack angle i.e. the assumed direction that the enemy is constructing their path from
     * This allows for a goodMove to prioritise impeding the enemy's progress.
     *
     * @param boardView the current state of the board
     * @return int        the appropriate offset for goodMove to prioritise being in front of the line.
     */
    private int findAttackAngle(Piece[][] boardView) {
        // Detect which half of the board the enemy's move was placed and return the approprite offset to place a piece towards the other side
        int attackAngle = 0;
        int sizeX = boardView.length;
        int sizeY = boardView[0].length;
        int a = 0;
        for (int x = 0; x < sizeX; x++) {
            for (int y = 0; y < sizeY; y++) {
                if (boardView[x][y] == enemy) {
                    if (colour == Piece.RED) {
                        if (x <= sizeX / 2 - 1) {
                            a++;
                        } else {
                            a--;
                        }
                        /*

                        "For a brummy, he's not half bad - Ryan Collins 3.30pm 03/05/2016

                         */
                    } else {
                        if (y <= sizeY / 2 - 1) {
                            a++;
                        } else {
                            a--;
                        }
                    }
                }
            }
        }
        if (a >= 0) {
            attackAngle = 1;
        } else {
            attackAngle = -1;
        }
        return attackAngle;
    }


    /**
     * Finds a move that breaks a potential connection for the enemy.
     *
     * @param boardView the current state of the board
     *                  isCrucial   true if we are only impeding crucial moves
     * @return MoveInterface   The move that was found. Null if no moves available.
     */
    private MoveInterface findDestructiveMove(Piece[][] boardView, boolean isCrucial) {
        // Pretend you are the enemy and find a constructive move
        Piece temp = colour;
        colour = enemy;
        enemy = temp;
        MoveInterface move = findConstructiveMove(boardView, isCrucial);
        enemy = colour;
        colour = temp;
        return move;
    }


    /**
     * Finds a move that connects two unconnected pieces
     *
     * @param boardView the current state of the board
     *                  isCrucial   true if we are looking for a move that must be made to connect 2 pieces
     * @return MoveInterface   The move that was found. Null if no moves available.
     */
    private MoveInterface findConstructiveMove(Piece[][] boardView, boolean isCrucial) {
        // Allows for positions to be checked in a random order. Makes us less predicatable.
        ArrayList<Integer> xValues = shuffleCoords(boardView, true);
        ArrayList<Integer> yValues = shuffleCoords(boardView, false);

        for (int x : xValues) {
            for (int y : yValues) {
                if (isConstructive(boardView, x, y, isCrucial)) {
                    return createMove(x, y);
                }
            }
            Collections.shuffle(yValues);
        }
        return null;
    }

    /**
     * Determines if placing a piece in a certain position would connect two unconnected pieces.
     *
     * @param boardView the current state of the board
     *                  x,y         the position being evaluated
     *                  isCrucial   true if we are checking for a move that MUST be made to connect 2 pieces
     * @return boolean    true if the move is constructive
     */
    private boolean isConstructive(Piece[][] boardView, int x, int y, boolean isCrucial) {
        // Check that a piece can actually be placed there
        if (boardView[x][y] != Piece.UNSET) {
            return false;
        }
        // Checking if move would connect a piece to our edge of the board.
        if (colour == Piece.RED) {
            // Check top of board
            if (y == 0) {
                // Check top right corner
                if (
                        x == boardView.length - 1 &&
                                checkCell(boardView, x, y + 1, colour)
                        ) {
                    return true;
                }

                return edgeCheck(boardView,
                        new int[]{x - 1, x - 1, x, x + 1},
                        new int[]{y + 1, y, y + 1, y},
                        isCrucial);
            }
            // Check bottom of board
            else if (y == boardView[0].length - 1) {
                // Check bottom left corner
                if (
                        x == 0 &&
                                checkCell(boardView, x, y - 1, colour)
                        ) {
                    return true;
                }
                return edgeCheck(boardView,
                        new int[]{x, x - 1, x + 1, x + 1},
                        new int[]{y - 1, y, y - 1, y},
                        isCrucial);
            }
        } else if (colour == Piece.BLUE) {
            // Check left of board
            if (x == 0) {
                // Check bottom left corner
                if (
                        y == boardView[0].length - 1 &&
                                checkCell(boardView, x + 1, y, colour)
                        ) {
                    return true;
                }
                return edgeCheck(boardView,
                        new int[]{x + 1, x, x + 1, x},
                        new int[]{y, y + 1, y - 1, y - 1},
                        isCrucial);
            }
            // Check right of board
            else if (x == boardView.length - 1) {
                // Check top right corner
                if (
                        y == 0 &&
                                checkCell(boardView, x - 1, y, colour)
                        ) {
                    return true;
                }
                return edgeCheck(boardView,
                        new int[]{x - 1, x, x - 1, x},
                        new int[]{y, y - 1, y + 1, y + 1},
                        isCrucial);
            }
        }
        // Checks if 2 pieces on opposite side of this position are ours
        // i.e. this move is the only move that could connect them
        if (
                (checkCell(boardView, x - 1, y, colour) && checkCell(boardView, x + 1, y, colour)) ||
                        (checkCell(boardView, x, y - 1, colour) && checkCell(boardView, x, y + 1, colour)) ||
                        (checkCell(boardView, x - 1, y + 1, colour) && checkCell(boardView, x + 1, y - 1, colour))
                ) {
            return true;
        }
        // Checks if 2 neighbouring pieces are ours and the other piece that could connect them is not ours.
        else if (
                (isConnectable(boardView, x, y - 1, x - 1, y, x + 1, y - 1, isCrucial)) ||
                        (isConnectable(boardView, x + 1, y - 1, x, y - 1, x + 1, y, isCrucial)) ||
                        (isConnectable(boardView, x + 1, y, x + 1, y - 1, x, y + 1, isCrucial)) ||
                        (isConnectable(boardView, x, y + 1, x + 1, y, x - 1, y + 1, isCrucial)) ||
                        (isConnectable(boardView, x - 1, y + 1, x - 1, y, x, y + 1, isCrucial)) ||
                        (isConnectable(boardView, x - 1, y, x, y - 1, x - 1, y + 1, isCrucial))
                ) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * A special case of isConnectable where the position being considered is on the edge of the board
     *
     * @param boardView the current state of the board
     *                  x,y         the positions to check. Changes depending on the edge being checked
     *                  isCrucial   true if we are checking for a move that must be made to connect the edge
     * @return boolean    true if the move is constructive
     */
    private boolean edgeCheck(Piece[][] boardView, int[] x, int[] y, boolean isCrucial) {
        boolean isConstructive = false;
        // Checks for crucial edge moves
        if (
                (checkCell(boardView, x[0], y[0], colour) && checkCell(boardView, x[1], y[1], enemy)) ||
                        (checkCell(boardView, x[2], y[2], colour) && checkCell(boardView, x[3], y[3], enemy))
                ) {
            isConstructive = true;
        }
        // if not crucial, look for moves that could be made to connect to edge
        else if (
                !isCrucial &&
                        ((checkCell(boardView, x[0], y[0], colour) && checkCell(boardView, x[1], y[1], Piece.UNSET)) ||
                                (checkCell(boardView, x[2], y[2], colour) && checkCell(boardView, x[3], y[3], Piece.UNSET)))
                ) {
            isConstructive = true;
        }
        return isConstructive;
    }

    /**
     * Determines if two pieces, left and right, could be connected but are not.
     *
     * @param x,y,xLeft,yLeft,xRight,yRight co-ordinates for the left and right piece and the other piece that could connect them
     *                                      boardView   the current state of the board
     *                                      isCrucial   true if we are checking if there is only one available piece to connect left and right
     * @return boolean     true if the pieces can be connected and haven't been and if it is crucial, that only one piece can connect them
     */
    private boolean isConnectable(Piece[][] boardView, int x, int y, int xLeft, int yLeft, int xRight, int yRight, boolean isCrucial) {
        // true if the pieces can be connected and haven't been and if it is crucial, that only one piece can connect them
        boolean isConnectable = false;
        if (
                checkCell(boardView, xLeft, yLeft, colour) &&
                        checkCell(boardView, xRight, yRight, colour) &&
                        (checkCell(boardView, x, y, enemy) || !isCrucial && checkCell(boardView, x, y, Piece.UNSET))
                ) {
            isConnectable = true;
        }
        return isConnectable;
    }


    /**
     * Finds a move that is directly infront or behind an enemy piece
     *
     * @param boardView the current state of the board
     *                  attackAngle the offset required to place a piece in front of the enemy
     *                  isOptimal   true if we are looking for a move in front.
     * @return move       the move to be made or null if not found.
     */
    private MoveInterface findGoodMove(Piece[][] boardView, int attackAngle, boolean isOptimal) {
        MoveInterface move = null;
        if (colour == Piece.RED) {
            // Find a move that is infront of an enemy piece
            move = findBlockingMove(boardView, attackAngle, 0);
            // Otherwise find one that is behind.
            if (move == null && !isOptimal) {
                move = findBlockingMove(boardView, -attackAngle, 0);
            }
        } else if (colour == Piece.BLUE) {
            move = findBlockingMove(boardView, 0, attackAngle);
            if (move == null && !isOptimal) {
                move = findBlockingMove(boardView, 0, -attackAngle);
            }
        }
        return move;
    }

    /**
     * Finds a move that places a piece next to an enemy piece in a relative position defined by xOffset,yOffset
     *
     * @param boardView the current state of the board
     *                  xOffset     the relative x position to the enemy piece. i.e 1 indicated we are searching for a space to the right of the enemy
     *                  xOffset     the relative y position to the enemy piece. i.e 1 indicated we are searching for a space above the enemy
     * @return MoveInterface   The move that was found. Null if no moves available.
     */
    private MoveInterface findBlockingMove(Piece[][] boardView, int xOffset, int yOffset) {
        // Allows for positions to be checked in a random order. Makes us less predicatable.
        ArrayList<Integer> xValues = shuffleCoords(boardView, true);
        ArrayList<Integer> yValues = shuffleCoords(boardView, false);

        for (int x : xValues) {
            for (int y : yValues) {
                // If our position is within bounds and p is an enemy.
                if (checkCell(boardView, x + xOffset, y + yOffset, Piece.UNSET) && checkCell(boardView, x, y, enemy)) {
                    return createMove(x + xOffset, y + yOffset);
                }
            }
            Collections.shuffle(yValues);
        }
        return null;
    }

    /**
     * Finds a random move if one is available
     *
     * @param boardView the current state of the board
     * @return MoveInterface   The move that was found. Null if no moves available.
     */
    private MoveInterface findAnyMove(Piece[][] boardView) {
        // Allows for positions to be checked in a random order. Makes us less predicatable.
        ArrayList<Integer> xValues = shuffleCoords(boardView, true);
        ArrayList<Integer> yValues = shuffleCoords(boardView, false);

        for (int x : xValues) {
            for (int y : yValues) {
                if (boardView[x][y] == Piece.UNSET) {
                    return createMove(x, y);
                }
            }
            Collections.shuffle(yValues);
        }
        return null;
    }

    /**
     * Checks if the piece at x,y is within bounds and a certain piece p
     *
     * @param boardView the current state of the board
     *                  x,y         the co-ordinates of the piece we are checking
     *                  p           the type of piece that we are checking for
     * @return true if x,y within bounds and the piece found is of type p
     */
    private boolean checkCell(Piece[][] boardView, int x, int y, Piece p) {
        return (x >= 0 && y >= 0 &&
                x < boardView.length &&
                y < boardView[0].length &&
                boardView[x][y] == p);
    }


    /**
     * Set the colour that this player will be
     *
     * @param colour A Piece (RED/BLUE) that this player will be
     * @return true indicating that the method succeeded
     * @throws InvalidColourException    A colour other than RED/BLUE was provided
     * @throws ColourAlreadySetException The colour has already been set for this player.
     */
    public boolean setColour(Piece colour)
            throws InvalidColourException, ColourAlreadySetException {
        if (colour != Piece.RED && colour != Piece.BLUE) {
            throw new InvalidColourException();
        } else if (this.colour != Piece.UNSET) {
            throw new ColourAlreadySetException();
        } else {
            this.colour = colour;
            if (colour == Piece.RED) {
                enemy = Piece.BLUE;
            } else {
                enemy = Piece.RED;
            }
            return true;
        }
    }

    /**
     * Informs the player of the final game state. Player has Won, lost.
     *
     * @param state either WON or LOST
     * @return true indicating method has compleated successfully.
     */
    public boolean finalGameState(GameState state) {
        if (state == GameState.WON) {
            //System.out.println(colour + " won");
        } else if (state == GameState.LOST) {
            //System.out.println(colour + " lost");
        }
        return true;
    }
}
