import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class ComputerPlayer_tqvj24 implements PlayerInterface {

    private Piece colour;
    private Point nextChoice;
    private Piece[][] prevGrid;

    public ComputerPlayer_tqvj24() {
        colour = Piece.UNSET;
    }

    @Override
    public MoveInterface makeMove(Piece[][] boardView) throws NoValidMovesException {
        if (colour == Piece.UNSET || !BoardManager_tqvj24.hasValidMove(boardView)) {
            throw new NoValidMovesException();
        }

        System.out.println("Computer player tqvj24 is thinking...");
        Point p = chooseNextMove(boardView);

        MoveInterface myMove = new Move();
        try {
            if (!myMove.setPosition(p.x, p.y)) {
                System.out.println("Error: Setting position of moved failed!");
            }
        } catch (InvalidPositionException e) {
            System.out.println("Computer player somehow chose an invalid move");
        }

        return myMove;
    }

    @Override
    public boolean setColour(Piece colour) throws InvalidColourException, ColourAlreadySetException {
        if (colour == Piece.UNSET) {
            throw new InvalidColourException();
        }
        if (this.colour != Piece.UNSET) {
            throw new ColourAlreadySetException();
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
            System.out.println("Computer player tqvj24 " + getPlayerName() + (state == GameState.WON ? " won!" : " lost."));
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    //Non-interface methods
    private String getPlayerName() {
        return colour.name().toLowerCase();
    }

    //Below - methods for minimax solving
    private Point chooseNextMove(Piece[][] grid) {
        if (BoardManager_tqvj24.moreThanXEmpties(grid, 9)) {
            Piece target = colour == Piece.RED ? Piece.BLUE : Piece.RED;
            nextChoice = new Point(-1, -1);

            if (prevGrid == null) {
                nextChoice = randomChoice(grid);
            } else {
                Point lastMove = lastMove(prevGrid, grid, target);
                if (lastMove.x == -1) {
                    surroundExisting(grid, target);
                } else {
                    surroundLastMove(grid, target, lastMove);
                }
            }
            prevGrid = BoardManager_tqvj24.actualCopy(grid);
            return nextChoice;
        } else {
            nextChoice = new Point(-1, -1);
            int maxScore = grid.length * grid[0].length + 1;

            minimax(grid, 0, colour, maxScore); //sets nextChoice

            return nextChoice;
        }
    }

    private void surroundLastMove(Piece[][] grid, Piece target, Point lastMove) {

        ArrayList<Point> choices = BoardManager_tqvj24.getEmptyNeighbours(lastMove.x, lastMove.y, grid);

        if (choices.size() == 0) {
            surroundExisting(grid, target);
        } else {
            choices = directionFilter(choices, lastMove, target);
            Random r = new Random();
            nextChoice = choices.get(r.nextInt(choices.size()));
        }
    }

    private ArrayList<Point> directionFilter(ArrayList<Point> choices, Point lastMove, Piece target) {
        ArrayList<Point> result = new ArrayList<Point>();
        /*
        if target is red, block top to bottom (topleft to bottomright when rotated)
            -choose:
                all that are different y

        if target is blue, block left to right (bottomleft to topright when rotated)
            -choose:
                all that are different x
        TODO favour the direction towards the nearest edge
         */
        for (Point p : choices) {
            if (target == Piece.RED) {
                if (p.y != lastMove.y) {
                    result.add(p);
                }
            } else {
                if (p.x != lastMove.x) {
                    result.add(p);
                }
            }
        }
        if (result.isEmpty()) {
            return choices;
        }
        return result;
    }

    private void surroundExisting(Piece[][] grid, Piece target) {
        ArrayList<Point> choices = new ArrayList<Point>();

        for (int x = 0; x < grid.length; x++) {
            for (int y = 0; y < grid[0].length; y++) {
                if (grid[x][y] == target) {
                    choices.addAll(BoardManager_tqvj24.getEmptyNeighbours(x, y, grid));
                }
            }
        }

        if (choices.size() == 0) {
            nextChoice = randomChoice(grid);
        } else {
            Random r = new Random();
            nextChoice = choices.get(r.nextInt(choices.size()));
        }
    }

    private int minimax(Piece[][] grid, int depth, Piece player, int maxScore) {
        /*
        This minimax algorithm was written by adapting a previous C# project of mine, using minimax to play
        naughts and crosses. That original project was written with help of a blog post at:
        http://neverstopbuilding.com/minimax

        The algorithm in my original naughts and crosses minimax project was written by myself,
        after reading the blog post's explanation and python code to understand the algorithm.

        This minimax algorithm was then written by looking at my previous C# algorithm and re-writing it
        for the java and HexGame environment.
         */
        if (BoardManager_tqvj24.winner(grid) != Piece.UNSET) {
            return score(grid, depth, player, maxScore);
        }

        ArrayList<Integer> scores = new ArrayList<Integer>();
        ArrayList<Point> moves = new ArrayList<Point>();

        for (int x = 0; x < grid.length; x++) {
            for (int y = 0; y < grid[0].length; y++) {
                if (grid[x][y] != Piece.UNSET) {
                    //All placed pieces here
                    for (Point p : BoardManager_tqvj24.getEmptyNeighbours(x, y, grid)) {
                        //All empty neighbours of placed pieces
                        if (!moves.contains(p)) {
                            grid[p.x][p.y] = BoardManager_tqvj24.whoseGo(grid);
                            scores.add(minimax(grid, depth + 1, player, maxScore));
                            moves.add(p);
                            grid[p.x][p.y] = Piece.UNSET;
                        }
                    }
                }
            }
        }

        if (moves.isEmpty()) { //essentially only ever when computer player is playing an EMPTY grid
            int x = (int) grid.length / 2;
            int y = (int) grid[0].length / 2;
            Random r = new Random();
            while (!BoardManager_tqvj24.isFreeSpace(x, y, grid)) {
                x = r.nextInt(grid.length);
                y = r.nextInt(grid[0].length);
            }
            nextChoice = new Point(x, y);
            return 0; //value forgotten anyway in this case
        }

        int scoreIndex;
        if (BoardManager_tqvj24.whoseGo(grid) == player) {
            //If the next turn is ours
            scoreIndex = scores.indexOf(Collections.max(scores)); //we want max score
        } else {
            //If the next turn is not ours
            scoreIndex = scores.indexOf(Collections.min(scores)); //opponent wants min score
        }
        nextChoice = moves.get(scoreIndex);
        return scores.get(scoreIndex);
    }

    private int score(Piece[][] grid, int depth, Piece player, int maxScore) {
        Piece winner = BoardManager_tqvj24.winner(grid);
        if (player == winner) {
            return maxScore - depth;
        } else if (winner != Piece.UNSET) { //opponent wins
            return depth - maxScore;
        } else {
            return 0;
        }
    }

    private Point lastMove(Piece[][] lastGrid, Piece[][] grid, Piece target) {
        for (int x = 0; x < grid.length; x++) {
            for (int y = 0; y < grid[0].length; y++) {
                if (lastGrid[x][y] != grid[x][y] && grid[x][y] == target) {
                    return new Point(x, y);
                }
            }
        }
        System.out.println("Last move not found");
        return new Point(-1, -1);
    }

    private Point randomChoice(Piece[][] grid) {
        int x = (int) grid.length / 2;
        int y = (int) grid[0].length / 2;
        Random r = new Random();
        while (!BoardManager_tqvj24.isFreeSpace(x, y, grid)) {
            x = r.nextInt(grid.length);
            y = r.nextInt(grid[0].length);
        }
        return new Point(x, y);
    }

}
