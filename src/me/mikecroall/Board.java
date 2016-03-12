package me.mikecroall;

public class Board {

    private colour[][] grid;

    private enum colour{
        BLANK(' '),
        RED('R'),
        BLUE('B'),
        OFFLIMITS('#');

        private char letter;

        colour(char let){  //is already private by default
            letter = let;
        }

        public String getLetter(){
            return "" + letter;
        }

    }

    public Board(int width, int height){
        grid = new colour[width + 2][height + 2];
        for (int y = 0; y < height + 2; y++) {
            for (int x = 0; x < width + 2; x++) {
                if ((x == 0 && y == 0) || (x == 0 && y == height + 1) || (x == width + 1 && y == 0) || (x == width + 1 && y == height + 1))
                    grid[y][x] = colour.OFFLIMITS;
                else if (x == 0 || x == width + 1)
                    grid[y][x] = colour.RED;
                else if (y == 0 || y == height + 1)
                    grid[y][x] = colour.BLUE;
                else
                    grid[y][x] = colour.BLANK;
            }
        }
    }

    public void print(){
        System.out.print("\n\n  ");
        for (int x = 0; x < grid[0].length; x++) {
            System.out.print(" / \\");
        }
        System.out.println();
        String leftPadding;
        for (int y = 0; y < grid.length ; y++) {
            //Start at the correct position
            leftPadding = "";
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

    public void setColour(int x, int y, colour targetCol){
        if (isFreeSpace(x, y)) {
            grid[y + 1][x + 1] = targetCol;
            System.out.println("Space (" + x + ", " + y + ") set to " + targetCol.name().toLowerCase());
        }
    }

    public boolean validSpace(int x, int y){
        if (y < 0 || y > grid.length - 3 || x < 0 || x > grid[0].length - 3){
            System.out.println("Invalid coordinates: (" + x + ", " + y + ")");
            return false;
        }
        return true;
    }

    public boolean isFreeSpace(int x, int y){
        if (validSpace(x, y))
            if (grid[y + 1][x + 1] == colour.BLANK)
                return true;
            else
                System.out.println("Space (" + x + ", " + y + ") is not available");
        return false;
    }

    public static void main(String[] args){
        System.out.println("Beginning testing...");
        Board b = new Board(5,5);
        b.print();
        b.setColour(4, 3, colour.RED);
        b.setColour(-1, 3, colour.BLUE);
        b.setColour(3, -1, colour.RED);
        b.setColour(15, 3, colour.BLUE);
        b.setColour(3, 15, colour.RED);
        b.setColour(5, 4, colour.BLUE);
        b.setColour(4, 5, colour.RED);
        b.setColour(0, 2, colour.BLUE);
        b.setColour(2, 0, colour.RED);
        b.setColour(4, 3, colour.BLUE);
        b.print();
    }
}
