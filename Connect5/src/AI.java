import java.util.Vector;

/**
 * 
 * @author Lincoln Patton, Brandon Duke
 * @version 10/14/16
 */
public class AI implements Runnable {

	private int[][][] board;
	private MoveTree moves = new MoveTree();
	Thread aiThread = new Thread(this);

	public AI(int[][][] board) {
		this.board = board; // Get the board.
	}

	public void start() {
		aiThread.start(); // Start the AI.
	}

	public void update(int[][][] board) {
		aiThread.interrupt(); // Stop the AI...
		this.board = board;   // and update the board.
	}

	public void run() {
		/*
		 * Basically everything the AI has to do initializes here.
		 */
		try {
			moves.populateAll();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public int[] getMove() {
		/*
		 * Get the best move from the MoveTree.
		 */
		MoveTree best = null;
		for (MoveTree move: moves.possibleMoves){
			if (best == null) best = move;
			else if ( move.value > best.value) best = move;
		}
		return best.coords;
	}

	public int[] getMove(int time) {
		/*
		 * Wait for awhile before getting the move.
		 */
		try {
			wait(time * 1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return this.getMove();
	}

	private class MoveTree {
		/*
		 * Well, it's a tree of possible moves.
		 */
		private int[] coords = new int[2];
		private int value = 0;
		private Vector<MoveTree> possibleMoves;

		public MoveTree getMove(int move) {
			return possibleMoves.elementAt(move);
		}
		
		public MoveTree(){
			/*
			 * Initialize a MoveTree without coordinates.
			 * Primarily used for the root of the tree.
			 */
		}
		
		public MoveTree(int x, int y){
			/*
			 * Initialize a MoveTree with coordinates.
			 * Use this one unless you know what you're doing!
			 */
			coords[0] = x;
			coords[1] = y;
		}

		public boolean populate() throws InterruptedException {
			possibleMoves = new Vector<MoveTree>(); // Empty the possible moves to make sure there are no repeats.
			for (int x = 0; x < board[0][0].length; x++)
				for (int y = 0; y < board[0].length; y++) {
					int z = 0;
					try {
						while (board[z][y][x] != 0) // Try to find the vertical zone to place the piece in so we can make sure the move is valid.
							z++;
						if (Thread.interrupted()) {
							InterruptedException e = new InterruptedException();
							throw e;
						}
						// Add code to filter results here.
						possibleMoves.add(new MoveTree(x, y));
					} catch (ArrayIndexOutOfBoundsException e) {

					}
				}
			if (possibleMoves.isEmpty())
				return false;
			else
				return true;
		}

		public void populateAll() throws InterruptedException {
			if (populate())
				for (MoveTree move : possibleMoves) {
					move.populateAll();
				}
		}
	}
}