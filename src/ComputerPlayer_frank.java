import java.util.Random;
import java.awt.Point;
import java.util.ArrayList;

public class ComputerPlayer_frank implements PlayerInterface
{
    private Piece playerColour;
    private GameState state;
    private Point cell;
    private ArrayList<Point> cells;
    public ComputerPlayer_frank()
    {
        state = GameState.INCOMPLETE;
        playerColour = Piece.UNSET;
        cell = new Point();   //cell considered
        cells = new ArrayList<Point>(); //list of cells to consider
    }
    

    public MoveInterface makeMove(Piece[][] boardView) throws NoValidMovesException
    {
        try{
            int middleX = (int) boardView.length/2;
            int middleY = (int) boardView[0].length/2;
            int moveX = -1;
            int moveY = -1;
            
            MoveInterface move = new Move();
            if(!state.equals(GameState.INCOMPLETE)){ 
                throw new NoValidMovesException();
            }
            //AI for BLUE
            if(playerColour == Piece.BLUE){
                Random r = new Random();
                boolean moveReady = false;
                while(!moveReady){
                    moveX = r.nextInt(boardView.length);
                    moveY = r.nextInt(boardView[0].length);
                    if(boardView[moveX][moveY].equals(Piece.UNSET)){
                        moveReady = true;
                    }
                    move.setPosition(moveX,moveY);  
                    System.out.println("CPU plays " + move.getXPosition() +  "," + move.getYPosition());
                    return move;
                }
            }
            
            
            //AI for RED
            if(cells.isEmpty()){
                //if middle is empty, start from middle, otherwise pick a random point
                // since RED always goes first, the first element of cells is the middle
                //however this could be used for a blue AI that works like red
                if(boardView[middleX][middleY] == Piece.UNSET){
                    cells.add(new Point(middleX,middleY));
                }
                else{
                    Random r = new Random();
                    boolean moveReady = false;
                    while(!moveReady){
                        moveX = r.nextInt(boardView.length);
                        moveY = r.nextInt(boardView[0].length);
                        if(boardView[moveX][moveY].equals(Piece.UNSET)){
                            cells.add(new Point(moveX,moveY));
                            moveReady = true;
                        }
                    }                    
                }
            }
            //choosing move
            if(playerColour == Piece.RED){
                forLoop: //go through cells picked
                for(Point cell : cells){
                    if(boardView[middleX][middleY] == Piece.UNSET){
                        moveX = middleX;
                        moveY = middleY;
                        break forLoop;
                    }
                    else if((cell.y>0 && cell.x<boardView.length-1) && (boardView[cell.x][cell.y-1] == Piece.UNSET && boardView[cell.x+1][cell.y-1] != Piece.UNSET)){
                        moveX = cell.x;
                        moveY = cell.y-1; 
                        break forLoop;
                    }
                    else if((cell.y>0 && cell.x<boardView.length-1) && (boardView[cell.x][cell.y-1] != Piece.UNSET && boardView[cell.x+1][cell.y-1] == Piece.UNSET)){
                        moveX = cell.x+1;
                        moveY = cell.y-1;
                        break forLoop;
                    }
                    else if((cell.y<boardView[0].length-1 && cell.x>0) && (boardView[cell.x-1][cell.y+1] != Piece.UNSET && boardView[cell.x][cell.y+1] == Piece.UNSET)){
                        moveX = cell.x;
                        moveY = cell.y+1;
                        break forLoop;
                    }
                    
                    else if((cell.y<boardView[0].length-1 && cell.x>0) && (boardView[cell.x-1][cell.y+1] == Piece.UNSET && boardView[cell.x][cell.y+1] != Piece.UNSET)){
                        moveX = cell.x-1;
                        moveY = cell.y+1;
                        break forLoop;
                    }
                    else if((boardView[cell.x][cell.y-1] == Piece.UNSET && boardView[cell.x+1][cell.y-1] == Piece.UNSET && (cell.x>1 && cell.y>1 && cell.x<boardView.length-1 && cell.y<boardView[0].length-2)) && boardView[cell.x+1][cell.y-2] == Piece.UNSET ){   //place above
                        moveX = cell.x+1;
                        moveY = cell.y-2;
                        cells.add(new Point(moveX,moveY));   
                        break forLoop;
                    }                        
                    else if((boardView[cell.x-1][cell.y+1] == Piece.UNSET && boardView[cell.x][cell.y+1] == Piece.UNSET && (cell.x>1 && cell.y>1 && cell.x<boardView.length-1 && cell.y<boardView[0].length-2)) && boardView[cell.x-1][cell.x+2] == Piece.UNSET){   //place below
                        moveX = cell.x-1;
                        moveY = cell.y+2;
                        cells.add(new Point(moveX,moveY));  
                        break forLoop;
                    }                    
                }
            }
            
            if(moveX<0 | moveX>boardView.length-1 | moveY<0 | moveY>boardView[0].length-1){
                Random r = new Random();
                boolean moveReady = false;
                while(!moveReady){
                    moveX = r.nextInt(boardView.length);
                    moveY = r.nextInt(boardView[0].length);
                    if(boardView[moveX][moveY].equals(Piece.UNSET)){
                        moveReady = true;
                    }
                }
            }
            
            move.setPosition(moveX,moveY);  
            //System.out.println("CPU plays " + move.getXPosition() +  "," + move.getYPosition());
            return move;
        }
        catch(InvalidPositionException e){
            System.out.println(e);
            e.printStackTrace();
        }
        
        return null; // shouldn't be reachable
    }
    
    public boolean setColour(Piece colour) throws InvalidColourException, ColourAlreadySetException
    {
        if(colour != Piece.RED && colour != Piece.BLUE){
            throw new InvalidColourException();
        }
        if(!playerColour.equals(Piece.UNSET)){
            throw new ColourAlreadySetException();
        }
        playerColour = colour;
        return true;
    }
    
    public boolean finalGameState(GameState state)
    {
        return true;
    }
    
}