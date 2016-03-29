import java.awt.*;
import java.util.ArrayList;

public class BoardManager {

    private static Piece wentFirst = Piece.RED; //red assumed, later figured out

    //TODO remove all colour output entirely for final (assignment) release
    private static final boolean colourOutput = true; //Colours do not work in some environments

    //TODO print borders/markers, and optimise
    public static void printBoard(Piece[][] grid){
        //Print top of hexs
        System.out.print("\n\n  ");
        for (int x = 0; x < grid[0].length; x++) {
            System.out.print(" / \\");
        }
        System.out.println();

        for (int y = 0; y < grid.length; y++) {
            //Start at the correct position
            String leftPadding = "";
            for (int spaces = 0; spaces < 2 * y; spaces++) {
                leftPadding += " ";
            }
            System.out.print(leftPadding + "  ");

            //Top of hex is present, print middle row
            for (int x = 0; x < grid[y].length; x++) {
                System.out.print("| " + getLetter(grid[y][x]) + " ");
            }
            System.out.print("|\n" + leftPadding);

            //Close off hex (is also top of next row, if exists)
            for (int x = 0; x < grid[y].length; x++) {
                System.out.print((x == 0 ? "  " : " /") + " \\");
            }
            System.out.println(" /" + (y < grid.length - 1 ? " \\" : ""));
        }
        System.out.println();
    }

    public static Piece winner(Piece[][] grid){
        if(hasWon(Piece.RED, grid)) { return Piece.RED; }
        else if(hasWon(Piece.BLUE, grid)) { return Piece.BLUE; }
        else { return Piece.UNSET; }
    }

    public static boolean isValidSpace(int x, int y, Piece[][] grid) {
        return (grid != null && x >= 0 && x < grid[0].length && y >= 0 && y < grid.length);
    }

    public static boolean isFreeSpace(int x, int y, Piece[][] grid) {
        return (grid != null && grid[y][x] == Piece.UNSET);
    }

    public static Piece whoseGo(Piece[][] grid) {
        int red = 0, blue = 0;
        for (Piece[] row : grid){
            for (Piece piece : row){
                if (piece == Piece.RED){ red++; }
                else if (piece == Piece.BLUE) { blue++; }
            }
        }
        if (red == blue){
            return wentFirst;
        }else if (red > blue){
            wentFirst = Piece.RED; //one more red than blue, red was first and blues turn
            return Piece.BLUE;
        }else{ //blue > red
            wentFirst = Piece.BLUE; //opposite to above
            return Piece.RED;
        }
    }

    private static String getLetter(Piece colour) {
        switch (colour) {
            case RED:
                if(colourOutput) { return "\u001B[31m" + "R" + "\u001B[0m"; }
                else { return "R"; }
            case BLUE:
                if (colourOutput) { return "\u001B[34m" + "B" + "\u001B[0m"; }
                else {return "B"; }
            case UNSET:
                return " ";
            default:
                return "?";
        }

    }

    private static boolean hasWon(Piece colour, Piece[][] grid) {
        //Red starts at top, finds bottom
        //Blue starts at left, finds right
        boolean topToBottom = (colour == Piece.RED);

        ArrayList<Point> closed = new ArrayList<Point>();
        ArrayList<Point> open = new ArrayList<Point>();

        if (topToBottom) {
            for (int x = 0; x < grid[0].length; x++) {
                if (grid[0][x] == colour) {
                    closed.add(new Point(x, 0));
                }
            }
        } else {
            for (int y = 0; y < grid.length; y++) {
                if (grid[y][0] == colour) {
                    closed.add(new Point(0, y));
                }
            }
        }

        for(Point p : closed) {
            open.addAll(neighboursOf(p, closed, grid));
        }

        boolean found = false;
        while (!open.isEmpty()) {
            if (foundOppositeEdge(topToBottom, open, grid)) {
                found = true; break;
            }
            closed.addAll(open);
            ArrayList<Point> openNeighbours = new ArrayList<Point>();
            for (Point p : open) {
                openNeighbours.addAll(neighboursOf(p, closed, grid));
            }
            open = new ArrayList<Point>(openNeighbours);
        }

        return found;
    }

    private static ArrayList<Point> neighboursOf(Point p, ArrayList<Point> closed, Piece[][] grid) {
        Piece colour = grid[p.y][p.x];
        ArrayList<Point> neighbours = new ArrayList<Point>();

        checkNeighbour(new Point(p.x - 1, p.y), neighbours, closed, colour, grid);      //left
        checkNeighbour(new Point(p.x + 1, p.y), neighbours, closed, colour, grid);      //right
        checkNeighbour(new Point(p.x, p.y - 1), neighbours, closed, colour, grid);      //upleft
        checkNeighbour(new Point(p.x + 1, p.y - 1), neighbours, closed, colour, grid);  //upright
        checkNeighbour(new Point(p.x - 1, p.y + 1), neighbours, closed, colour, grid);  //downleft
        checkNeighbour(new Point(p.x, p.y + 1), neighbours, closed, colour, grid);      //downright

        return neighbours;
    }

    private static void checkNeighbour(Point p, ArrayList<Point> neighbours, ArrayList<Point> closed, Piece colour, Piece[][] grid) {
        if (isValidSpace(p.x, p.y, grid) && grid[p.y][p.x] == colour && !closed.contains(p) && ! neighbours.contains(p)) {
            neighbours.add(p);
        }
    }

    private static boolean foundOppositeEdge(boolean topToBottom, ArrayList<Point> open, Piece[][] grid) {
        for (Point p : open) {
            if(topToBottom && p.y == grid.length - 1) {
                return true;
            }
            else if(!topToBottom && p.x == grid[0].length - 1) {
                return true;
            }
        }
        return false;
    }

}
