/**
 *
 * @author Lincoln Patton, Brandon Duke
 * @version 10/14/16
 */
public class Connect5 {
	public int[][][] board;
	public Player[] playerz;

	public Connect5(int x, int y, int z, int playerCount) {
		board = new int[z][y][x];
		for (int[][] a : board)
			for (int[] b : a)
				for (int c : b)
					c = 0;
		playerz = new Player[playerCount];
		for (int i = 0; i < playerCount; i++) {
			playerz[i] = new Player(i + 1);
		}
	}

	public int[][][] getBoard() {
		return board;
	}

	private boolean checkWin(int row, int column) {
		Integer z = null;
		for (int i = board.length - 1; i >= 0; i--) {
			if (board[i][column][row] != 0) {
				z = i;
				break;
			}
		}
		if (z == null)
			return false;
		int[] temp = { 0, 1 };
		for (int xAxis : temp)
			for (int yAxis : temp)
				zFun: for (int zAxis : temp) {
					if (xAxis == 0 && yAxis == 0 && zAxis == 0);
					else {
						int count = 1;
						int x = row;
						int y = column;
						int zCheck = z;
						checkForward: try {
							while (board[zCheck + zAxis][y + yAxis][x + xAxis] == board[zCheck][y][x]) {
								count++;
								zCheck += zAxis;
								y += yAxis;
								x += xAxis;
							}
						} catch (ArrayIndexOutOfBoundsException e) {
							int ztemp = zCheck;
							int ytemp = y;
							int xtemp = x;
							if (zCheck + 1 >= board.length)
								zCheck = -1;
							if (y + 1 >= board[z].length)
								y = -1;
							if (x + 1 >= board[z][column].length)
								x = -1;
							if (board[ztemp][ytemp][xtemp] == board[zCheck + 1][y + 1][x + 1]) {
								count++;
								zCheck += zAxis;
								y += yAxis;
								x += xAxis;
								break checkForward;
							}
						}
						x = row;
						y = column;
						zCheck = z;
						checkBackward: try {
							while (board[zCheck - zAxis][y - yAxis][x - xAxis] == board[zCheck][y][x]) {
								count++;
								zCheck -= zAxis;
								y -= yAxis;
								x -= xAxis;
							}
						} catch (ArrayIndexOutOfBoundsException e) {
							int ztemp = zCheck;
							int ytemp = y;
							int xtemp = x;
							if (zCheck - 1 < 0)
								zCheck = board.length;
							if (y - 1 < 0)
								y = board[z].length;
							if (x - 1 < 0)
								x = board[z][column].length;
							if (board[ztemp][ytemp][xtemp] == board[zCheck - 1][y - 1][x - 1]) {
								count++;
								zCheck -= zAxis;
								y -= yAxis;
								x -= xAxis;
								break checkBackward;
							}
						}
						if (count >= 5)
							return true;
					}
				}
		return false;
	}
	private int currentPlayer = 1;

	public int turnOrder() {
		currentPlayer++;
		if (currentPlayer > playerz.length)
			currentPlayer = 1;
		return currentPlayer;
	}

	public int getCurrentPlayer() {
		return currentPlayer;
	}

	public class Player {
		int playerNumber;

		public Player(int number) {
			playerNumber = number;
		}

		public boolean move(int row, int column) {
			for (int[][] z : board)
				if (z[column][row] == 0) {
					z[column][row] = playerNumber;
					turnOrder();
					return checkWin(row, column);
				}
			throw new InvalidMoveException();
		}
	}
}
