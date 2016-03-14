package me.tqvj24.michaelcroall;

import java.awt.*;
import java.util.ArrayList;

public class Board implements BoardInterface {

    private Piece[][] grid;
    private Piece lastMove;

    public Board() {
        // grid is initialised in setBoardSize
        lastMove = Piece.UNSET;
    }

    //Interface methods
    @Override
    public boolean setBoardSize(int sizeX, int sizeY) throws InvalidBoardSizeException, BoardAlreadySizedException {
        if (grid != null)
            throw new BoardAlreadySizedException();
        if (sizeX < 2 || sizeY < 2)
            throw new InvalidBoardSizeException();

        grid = new Piece[sizeY][sizeX];
        for (int y = 0; y < sizeY; y++) {
            for (int x = 0; x < sizeX; x++) {
                grid[y][x] = Piece.UNSET;
            }
        }
        return true;
    }

    @Override
    public Piece[][] getBoardView() throws NoBoardDefinedException {
        if (grid == null)
            throw new NoBoardDefinedException();
        return grid;
    }

    @Override
    public boolean placePiece(Piece colour, MoveInterface move) throws PositionAlreadyTakenException, InvalidPositionException, InvalidColourException {
        if (colour == lastMove || colour == Piece.UNSET)
            throw new InvalidColourException();
        if (!isValidSpace(move.getXPosition(), move.getYPosition()))
            throw new InvalidPositionException();
        if (!isFreeSpace(move.getXPosition(), move.getYPosition()))
            throw new PositionAlreadyTakenException();

        grid[move.getYPosition()][move.getXPosition()] = colour;
        lastMove = colour;

        return true;
    }

    @Override
    public Piece gameWon() throws NoBoardDefinedException {
        if (grid == null)
            throw new NoBoardDefinedException();

        if(hasWon(Piece.RED))
            return Piece.RED;
        else if(hasWon(Piece.BLUE))
            return Piece.BLUE;
        else
            return Piece.UNSET;
    }

    //Non-interface methods
    private boolean isValidSpace(int x, int y) {
        return (grid != null && x >= 0 && x < grid[0].length && y >= 0 && y < grid.length);
    }

    private boolean isFreeSpace(int x, int y) {
        return (grid != null && grid[y][x] == Piece.UNSET);
    }

    private boolean hasWon(Piece colour) {
        //Red starts at top, finds bottom
        //Blue starts at left, finds right
        boolean topToBottom = (colour == Piece.RED);

        Point start;
        if (topToBottom)
            start = new Point((int) (grid[0].length / 2), 0);
        else
            start = new Point(0, (int) (grid.length / 2));

        ArrayList<Point> closed = new ArrayList<Point>();
        ArrayList<Point> open = new ArrayList<Point>();

        closed.add(start);
        open.addAll(neighboursOf(start, closed));

        boolean found = false;
        while (!open.isEmpty()) {
            if (foundOppositeEdge(topToBottom, open)) {
                found = true; break;
            }
            closed.addAll(open);
            ArrayList<Point> openNeighbours = new ArrayList<Point>();
            for (Point p : open) {
                openNeighbours.addAll(neighboursOf(p, closed));
            }
            open = new ArrayList<Point>(openNeighbours);
        }

        return found;
    }

    private ArrayList<Point> neighboursOf(Point p, ArrayList<Point> closed) {
        Piece colour = grid[p.y][p.x];
        ArrayList<Point> neighbours = new ArrayList<Point>();

        checkNeighbour(new Point(p.x - 1, p.y), neighbours, closed, colour);      //left
        checkNeighbour(new Point(p.x + 1, p.y), neighbours, closed, colour);      //right
        checkNeighbour(new Point(p.x, p.y - 1), neighbours, closed, colour);      //upleft
        checkNeighbour(new Point(p.x + 1, p.y - 1), neighbours, closed, colour);  //upright
        checkNeighbour(new Point(p.x - 1, p.y + 1), neighbours, closed, colour);  //downleft
        checkNeighbour(new Point(p.x, p.y + 1), neighbours, closed, colour);      //downright

        return neighbours;
    }

    private void checkNeighbour(Point p, ArrayList<Point> neighbours, ArrayList<Point> closed, Piece colour) {
        if (isValidSpace(p.x, p.y) && grid[p.y][p.x] == colour && !closed.contains(p))
            neighbours.add(p);
    }

    private boolean foundOppositeEdge(boolean topToBottom, ArrayList<Point> open) {
        for (Point p : open) {
            if(topToBottom && p.y == grid.length - 1)
                return true;
            else if(!topToBottom && p.x == grid[0].length - 1)
                return true;
        }
        return false;
    }

    //TODO put board borders into print method and optimise print method
    public void print() {
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

}
