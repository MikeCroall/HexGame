package me.tqvj24.michaelcroall;

public class Move implements MoveInterface {

    private int x, y;
    private boolean conceded;

    public Move(){
        conceded = false;
    }

    @Override
    public boolean setPosition(int x, int y) throws InvalidPositionException {
        //TODO check valid coordinates

        this.x = x;
        this.y = y;

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
    public boolean setConceeded() {
        conceded = true;
        return true;
    }

    @Override
    public boolean hasConceeded() {
        return conceded;
    }

    //TODO ensure concede is correctly implemented (email sent, awaiting reply)
}
