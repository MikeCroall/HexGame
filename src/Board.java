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

        try {
            grid = new Piece[sizeX][sizeY];

            for (int x = 0; x < sizeX; x++) {
                for (int y = 0; y < sizeY; y++) {
                    grid[x][y] = Piece.UNSET;
                }
            }
        }catch(Exception e){
            return false; // so that the boolean return actually has meaning
        }
        return true;
    }

    @Override
    public Piece[][] getBoardView() throws NoBoardDefinedException {
        if (grid == null) { throw new NoBoardDefinedException(); }
        return BoardManager_tqvj24.actualCopy(grid);
    }

    @Override
    public boolean placePiece(Piece colour, MoveInterface move) throws PositionAlreadyTakenException, InvalidPositionException, InvalidColourException, NoBoardDefinedException {
        if (colour == lastMove || colour == Piece.UNSET)
        { throw new InvalidColourException(); }
        if (grid == null)
        { throw new NoBoardDefinedException(); }
        if (!BoardManager_tqvj24.isValidSpace(move.getXPosition(), move.getYPosition(), grid))
        { throw new InvalidPositionException(); }
        if (!BoardManager_tqvj24.isFreeSpace(move.getXPosition(), move.getYPosition(), grid))
        { throw new PositionAlreadyTakenException(); }

        try {
            grid[move.getXPosition()][move.getYPosition()] = colour;
            lastMove = colour;
        }catch(Exception e){
            return false;
        }
        return true;
    }

    @Override
    public Piece gameWon() throws NoBoardDefinedException {
        if (grid == null) {
            throw new NoBoardDefinedException();
        }

        return BoardManager_tqvj24.winner(grid);
    }

}
