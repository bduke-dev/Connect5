import java.util.Vector;

/**
 * Created by brandon on 10/17/16.
 * @author Lincoln Patton, Brandon Duke
 */
public class AI implements Runnable {
	
	private int[][][] board;
	private MoveTree moves;
	
	public AI( int[][][] board){
		this.board = board;
	}
	
	public void start(){
		new Thread(this).start();
	}
	
	public void update( int[][][] board){
		this.board = board;
	}

    public void run(){
    	
    }
    
    public int[] getMove(){
    	int[] move = {0,0};
    	return move;
    }
    
    public int[] getMove(int time){
    	try {
			wait(time*1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return this.getMove();
    }
    
    private class MoveTree{
    	private int[] coords = new int[2];
    	private Vector<MoveTree> possibleMoves;
    	
    	
    	
    	public MoveTree getMove(int move){
    		return possibleMoves.elementAt(move);
    	}
    	
    	public void populate(){
    		for( int x = 0; x < board[0][0].length; x++)
    			for ( int y = 0; y < board[0].length; y++){
    				int z = 0;
    				while ( board[z][y][x] != 0) z++;
    				possibleMoves.add(new MoveTree());
    			}
    	}
    }
}