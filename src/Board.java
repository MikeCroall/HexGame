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
        if (grid != null) { throw new BoardAlreadySizedException(); }
        if (sizeX < 2 || sizeY < 2) { throw new InvalidBoardSizeException(); }

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
        if (grid == null) { throw new NoBoardDefinedException(); }
        return grid;
    }

    @Override
    public boolean placePiece(Piece colour, MoveInterface move) throws PositionAlreadyTakenException, InvalidPositionException, InvalidColourException {
        if (colour == lastMove || colour == Piece.UNSET)
        { throw new InvalidColourException(); }
        if (!BoardManager.isValidSpace(move.getXPosition(), move.getYPosition(), grid))
        { throw new InvalidPositionException(); }
        if (!isFreeSpace(move.getXPosition(), move.getYPosition()))
        { throw new PositionAlreadyTakenException(); }

        grid[move.getYPosition()][move.getXPosition()] = colour;
        lastMove = colour;

        return true;
    }

    @Override
    public Piece gameWon() throws NoBoardDefinedException {
        if (grid == null) {
            throw new NoBoardDefinedException();
        }

        return BoardManager.winner(grid);
    }

    //Non-interface methods

    private boolean isFreeSpace(int x, int y) {
        return (grid != null && grid[y][x] == Piece.UNSET);
    }

}
