import java.util.Vector;

/**
 * Created by brandon on 10/17/16.
 * 
 * @author Lincoln Patton, Brandon Duke
 */
public class AI implements Runnable {

	private int[][][] board;
	private MoveTree moves = new MoveTree(0);
	Thread aiThread = new Thread(this);
	int playerCount;

	public AI(int[][][] board, int playerCount) {
		this.board = board; // Get the board.
		this.playerCount = playerCount;
	}

	public void start() {
		aiThread.start(); // Start the AI.
	}

	public void update(int[][][] board) {
		aiThread.interrupt(); // Stop the AI...
		int[][][] tempBoard = this.board;
		this.board = board;
		while( tempBoard != board){
			for ( MoveTree move : moves.possibleMoves){
				if (board[move.coords[2]][move.coords[1]][move.coords[0]] == move.player){
					moves = move;
					break;
				}
			}
		}
	}

	public void run() {
		/*
		 * Basically everything the AI has to do initializes here.
		 */
		Vector<Thread> threads;
		while (true) {
			threads = new Vector<Thread>();
			try {
				moves.populate();
				for (MoveTree move : moves.possibleMoves) {
					threads.addElement(new Thread(new Runnable() {
						public void run() {
							try {
								move.populateAll();
							} catch (InterruptedException e) {
							}
						}
					}));
				}
			} catch (InterruptedException e) {
				for (Thread thread : threads) {
					thread.interrupt();
				}
				try {
					synchronized(aiThread){
						aiThread.wait(100);
					}
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			for ( Thread thread: threads)
				try {
					thread.join();
				} catch (InterruptedException e) {
				}
		}
	}

	public Integer[] getMove() {
		/*
		 * Get the best move from the MoveTree.
		 */
		aiThread.interrupt();
		MoveTree best = null;
		for (MoveTree move : moves.possibleMoves) {
			if ( move.getHeuristic() > 16/board.length){
				best = move;
				break;
			}
			if (best == null)
				best = move;
			else if (move.getHeuristic() > best.getHeuristic())
				best = move;
		}
		return best.coords;
	}

	public Integer[] getMove(int time) {
		/*
		 * Wait for awhile before getting the move.
		 */
		try {
			synchronized(aiThread) {
				aiThread.wait(time * 1000);
			}
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
		private Integer[] coords = new Integer[3];
		private Vector<MoveTree> possibleMoves;
		private Double heuristicValue = null;
		int player;

		public MoveTree getMove(int move) {
			return possibleMoves.elementAt(move);
		}

		public MoveTree(int player) {
			/*
			 * Initialize a MoveTree without coordinates. Primarily used for the
			 * root of the tree.
			 */
			for ( Integer x: coords) 
				x = null;
			this.player = player;
		}

		public MoveTree(int player, int x, int y, int z) {
			/*
			 * Initialize a MoveTree with coordinates. Use this one unless you
			 * know what you're doing!
			 */
			this.player = player;
			coords[0] = x;
			coords[1] = y;
			coords[2] = z;
		}

		public double getHeuristic() {
			if (heuristicValue == null) {
				Double heuristic = null;
				if (possibleMoves != null) {
					for (MoveTree move : possibleMoves) {
						if (heuristic == null || heuristic < move.getHeuristic())
							heuristic = move.getHeuristic();
					}
				} else {
					int row = coords[0];
					int column = coords[1];
					int height = coords[2];
					Integer z = null;
					for (int i = board.length - 1; i >= 0; i--) {
						if (board[i][column][row] != 0) {
							z = i;
							break;
						}
					}
					int[] temp = {0, 1};
					for (int xAxis : temp)
						for (int yAxis : temp)
							for (int zAxis : temp) {

								if (xAxis == 0 && yAxis == 0 && zAxis == 0) ;
								else {
									int count = 1;
									int x = row;
									int y = column;
									z = height;
									checkForward:
									try {
										while (board[z + zAxis][y + yAxis][x + xAxis] == board[z][y][x]) {
											count++;
											z += zAxis;
											y += yAxis;
											x += xAxis;
										}
									} catch (ArrayIndexOutOfBoundsException e) {
										int ztemp = z;
										int ytemp = y;
										int xtemp = x;
										if (z + zAxis >= board.length)
											z = -1;
										if (y + yAxis >= board[0].length)
											y = -1;
										if (x + xAxis >= board[0][0].length)
											x = -1;
										if (board[ztemp][ytemp][xtemp] == board[z + zAxis][y + yAxis][x + xAxis]) {
											count++;
											z += zAxis;
											y += yAxis;
											x += xAxis;
											break checkForward;
										}
									}
									x = row;
									y = column;
									z = height;
									checkBackward:
									try {
										while (board[z - zAxis][y - yAxis][x - xAxis] == board[z][y][x]) {
											count++;
											z -= zAxis;
											y -= yAxis;
											x -= xAxis;
										}
									} catch (ArrayIndexOutOfBoundsException e) {
										int ztemp = z;
										int ytemp = y;
										int xtemp = x;
										if (z - zAxis < 0)
											z = board.length;
										if (y - yAxis < 0)
											y = board[0].length;
										if (x - xAxis < 0)
											x = board[0][0].length;
										if (board[ztemp][ytemp][xtemp] == board[z - zAxis][y - yAxis][x - xAxis]) {
											count++;
											z -= zAxis;
											y -= yAxis;
											x -= xAxis;
											break checkBackward;
										}
									}
									heuristic = Math.pow(2, count);
								}
							}
				}
				heuristicValue = heuristic;
				return heuristic;
			} else
				return heuristicValue;
		}

		public void prune() {
			aiThread.interrupt();
			Double bestHeuristic = null;
			for (MoveTree move : possibleMoves) {
				double moveHeuristic = move.getHeuristic();
				if (bestHeuristic == null || bestHeuristic > moveHeuristic) {
					possibleMoves.removeElement(move);
				} else if (bestHeuristic < moveHeuristic)
					bestHeuristic = moveHeuristic;
			}
		}

		public boolean populate() throws InterruptedException {
			heuristicValue = null;
			possibleMoves = new Vector<MoveTree>(); // Empty the possible moves
													// to make sure there are no
													// repeats.
			int tempPlayer = player + 1;
			if (tempPlayer > playerCount)
				tempPlayer = 1;

			for (int x = 0; x < board[0][0].length; x++)
				for (int y = 0; y < board[0].length; y++) {
					int z = 0;
					try {
						while (board[z][y][x] != 0) // Try to find the vertical
													// zone to place the piece
													// in so we can make sure
													// the move is valid.
							z++;
						if (Thread.interrupted()) {
							InterruptedException e = new InterruptedException();
							throw e;
						}
						MoveTree newMove = new MoveTree(tempPlayer, x, y, z); // Filter
																				// possible
																				// moves
						if ( (board.length <= 6 && board[0].length <= 6 && board[0][0].length <= 6)
								|| board.length < 5 || board[0].length < 5 || board[0][0].length < 5)
							possibleMoves.add(newMove);
						else
						try {
							if (z != 0 || board[0][y + 1][x] != 0 || board[0][y][x + 1] != 0
									|| board[0][y + 1][x + 1] != 0 || board[0][y - 1][x] != 0 || board[0][y][x - 1] != 0
									|| board[0][y - 1][x - 1] != 0 || board[0][x - 1][y + 1] != 0
									|| board[0][x + 1][y - 1] != 0)
								possibleMoves.add(newMove);
						} catch (ArrayIndexOutOfBoundsException e) {

						}
					} catch (ArrayIndexOutOfBoundsException e) {

					}
				}
			if (possibleMoves.isEmpty()){
				if ( coords[0] == null){
					possibleMoves.addElement(new MoveTree(tempPlayer,2,2,0));
					return true;
				}else
					for ( int y = 0; y < board[0].length; y++)
						for ( int x = 0; x < board[0][y].length; x++){
							if (board[0][y][x] != 0){
								int zz = 0;
								for( int z = 0; z < board.length; z++)
									if ( board[z][y][x] == 0) zz = z;
								if (zz != 0){
									possibleMoves.add(new MoveTree(tempPlayer, x, y , zz));
									return true;
								}
							}
						}
				return false;
			}
			else
				return true;
		}

		public void populateAll() throws InterruptedException {
			if (populate())
				for (MoveTree move : possibleMoves) {
					move.populateAll();
					move.prune();
				}
		}
	}
}