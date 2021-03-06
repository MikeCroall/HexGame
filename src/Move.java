public class Move implements MoveInterface {

    private int x, y;
    private boolean conceded;

    public Move() {
        conceded = false;
    }

    @Override
    public boolean setPosition(int x, int y) throws InvalidPositionException {
        if (x < 0 || y < 0) {
            throw new InvalidPositionException();
        }
        try {
            this.x = x;
            this.y = y;
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @Override
    public int getXPosition() {
        return x;
    }

    @Override
    public int getYPosition() {
        return y;
    }

    @Override
    public boolean setConceded() {
        try {
            conceded = true;
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @Override
    public boolean hasConceded() {
        return conceded;
    }

}
