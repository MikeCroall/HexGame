public class BoardPrinter {

    /**
     * Colouring output mode uses ANSI escape codes, found here:
     * https://en.wikipedia.org/wiki/ANSI_escape_code
     */
    //TODO remove all colour output entirely for final (assignment) release
    private static final boolean colourOutput = true; //Colours letters and names, may not work in some environments

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

    //TODO print borders/markers, and optimise
    //Static to allow printing once game has been won
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
}
