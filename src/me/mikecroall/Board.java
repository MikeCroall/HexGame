package me.mikecroall;

import java.awt.*;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class Board {

    private Colour[][] grid;

    private enum Colour {
        BLANK(' '),
        RED('R'),
        BLUE('B'),
        OFFLIMITS('#');

        private char letter;

        Colour(char let){  //is already private by default
            letter = let;
        }

        public String getLetter(){
            return "" + letter;
        }

    }

    public Board(int width, int height){
        if (width < 2 || height < 2) {
            System.out.println("Invalid grid size given to Board");
        }else {
            grid = new Colour[width + 2][height + 2];
            for (int y = 0; y < height + 2; y++) {
                for (int x = 0; x < width + 2; x++) {
                    if ((x == 0 && y == 0) || (x == 0 && y == height + 1) || (x == width + 1 && y == 0) || (x == width + 1 && y == height + 1))
                        grid[y][x] = Colour.OFFLIMITS;
                    else if (x == 0 || x == width + 1)
                        grid[y][x] = Colour.BLUE;
                    else if (y == 0 || y == height + 1)
                        grid[y][x] = Colour.RED;
                    else
                        grid[y][x] = Colour.BLANK;
                }
            }
        }
    }

    public void print(){
        //Print top of hexs
        System.out.print("\n\n  ");
        for (int x = 0; x < grid[0].length; x++) {
            System.out.print(" / \\");
        }
        System.out.println();

        for (int y = 0; y < grid.length ; y++) {
            //Start at the correct position
            String leftPadding = "";
            for (int spaces = 0; spaces < 2*y; spaces++)
                leftPadding += " ";
            System.out.print(leftPadding + "  ");

            //Top of hex is present, print middle row
            for (int x = 0; x < grid[y].length ; x++)
                System.out.print("| " + grid[y][x].getLetter() + " ");
            System.out.print("|\n" + leftPadding);

            //Close off hex (and top of next row, if exists)
            for (int x = 0; x < grid[y].length; x++)
                System.out.print((x == 0 ? "  " : " /") + " \\");
            System.out.println(" /" + (y < grid.length - 1 ? " \\" : ""));
        }
        System.out.println();
    }

    public boolean makeMove(int x, int y, boolean isRedsTurn){
        Colour targetCol = isRedsTurn ? Colour.RED : Colour.BLUE;
        if (isFreeSpace(x, y)) {
            grid[y + 1][x + 1] = targetCol;
            System.out.println("Space (" + x + ", " + y + ") set to " + targetCol.name().toLowerCase());
            return true;
        }
        return false;
    }

    //For taking turns
    public boolean isValidSpace(int x, int y){
        if (y < 0 || y > grid.length - 3 || x < 0 || x > grid[0].length - 3){
            System.out.println("Invalid coordinates: (" + x + ", " + y + ")");
            return false;
        }
        return true;
    }

    //Includes boundaries as valid
    public boolean isValidExtraSpace(int x, int y){
        return !(y < 0 || y >= grid.length || x < 0 || x >= grid[0].length);
    }

    public boolean isFreeSpace(int x, int y){
        if (isValidSpace(x, y))
            if (getColourAt(x, y) == Colour.BLANK)
                return true;
            else
                System.out.println("Space (" + x + ", " + y + ") is not available");
        return false;
    }

    public Colour getColourAt(int x, int y){
        return grid[y + 1][x + 1];
    }

    public boolean hasWon(boolean checkRed){
        Colour col = checkRed ? Colour.RED : Colour.BLUE;
        //Red starts at top, finds bottom
        //Blue starts at left, finds right

        Point start;
        if(checkRed)
            start = new Point((int)(grid[0].length / 2), 0);
        else
            start = new Point(0, (int)(grid.length / 2));

        ArrayList<Point> discarded = new ArrayList<Point>();
        ArrayList<Point> toSearch = new ArrayList<Point>();

        discarded.add(start);
        toSearch.addAll(findNeighbours(start, discarded));

        boolean found = false;
        while(!(found || toSearch.isEmpty())){
            found = foundOppositeEdge(checkRed, toSearch);
            if (!found){
                discarded.addAll(toSearch);
                ArrayList<Point> newToSearch = new ArrayList<Point>();
                for(Point p : toSearch){
                    newToSearch.addAll(findNeighbours(p, discarded));
                }
                toSearch = new ArrayList<Point>(newToSearch);
            }
        }
        return found;
    }

    private ArrayList<Point> findNeighbours(Point p, ArrayList<Point> ignore) {

        //TODO optimise this method and the methods it calls

        Colour col = grid[p.y][p.x];
        ArrayList<Point> neighbours = new ArrayList<Point>();

        handleNeighbour(new Point(p.x - 1, p.y), neighbours, ignore, col);      //left
        handleNeighbour(new Point(p.x + 1, p.y), neighbours, ignore, col);      //right
        handleNeighbour(new Point(p.x, p.y - 1), neighbours, ignore, col);      //upleft
        handleNeighbour(new Point(p.x + 1, p.y - 1), neighbours, ignore, col);  //upright
        handleNeighbour(new Point(p.x - 1, p.y + 1), neighbours, ignore, col);  //downleft
        handleNeighbour(new Point(p.x, p.y + 1), neighbours, ignore, col);      //downright

        return neighbours;
    }

    private void handleNeighbour(Point p, ArrayList<Point> neighbours, ArrayList<Point> ignore, Colour col) {
        if (isValidExtraSpace(p.x, p.y)) {
            if (grid[p.y][p.x].equals(col)) {
                if (!(ignore.contains(p) || neighbours.contains(p))) {
                    neighbours.add(p);
                }
            }
        }
    }

    private boolean foundOppositeEdge(boolean bottomEdge, ArrayList<Point> toCheck) {
        for (Point p : toCheck){
            if (bottomEdge) {
                if (p.y == grid.length - 1) {
                    return true;
                }
            } else {
                if (p.x == grid[0].length - 1) {
                    return true;
                }
            }
        }
        return false;
    }

    public static void main(String[] args){
        System.out.println("Beginning testing...");
        Board b = new Board(5,5);
        b.print();
        b.makeMove(4, 3, true);
        b.makeMove(-1, 3, false);
        b.makeMove(3, -1, true);
        b.makeMove(15, 3, false);
        b.makeMove(3, 15, true);
        b.makeMove(5, 4, false);
        b.makeMove(4, 5, true);
        b.makeMove(0, 2, false);
        b.makeMove(2, 0, true);
        b.makeMove(4, 3, false);
        b.print();

        b.makeMove(4 ,0, true);
        b.makeMove(3, 1, true);
        b.makeMove(2,2,true);
        b.makeMove(2,3,true);
        b.makeMove(4,4,true);
        b.makeMove(3, 3, false);
        b.print();

        //Win checks
        if (b.hasWon(true))
            System.out.println("Red has won");
        else if (b.hasWon(false))
            System.out.println("Blue has won");
        else
            System.out.println("No one has won yet...");
    }
}
