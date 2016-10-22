/**
 *
 * @author Lincoln Patton, Brandon Duke
 * @version 10/14/16
 */
public class Connect5 {
	public int[][][] board;
	public Player[] playerz;
	private int[][] winTiles;

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
	
	public int[][] getWinTiles(){
		return winTiles;
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
		int[][] tempWin;
		for (int xAxis : temp)
			for (int yAxis : temp)
				for (int zAxis : temp) {
					tempWin = new int[10][3];
					if (xAxis == 0 && yAxis == 0 && zAxis == 0);
					else {
						tempWin[0][0] = row;
						tempWin[0][1] = column;
						tempWin[0][2] = z;
						int count = 1;
						int x = row;
						int y = column;
						int zCheck = z;
						checkForward: try {
							while (board[zCheck + zAxis][y + yAxis][x + xAxis] == board[zCheck][y][x]) {
								tempWin[count][0] = x + xAxis;
								tempWin[count][1] = y + yAxis;
								tempWin[count][2] = zCheck + zAxis;
								count++;
								zCheck += zAxis;
								y += yAxis;
								x += xAxis;
							}
						} catch (ArrayIndexOutOfBoundsException e) {
							int ztemp = zCheck;
							int ytemp = y;
							int xtemp = x;
							if (zCheck + zAxis >= board.length)
								zCheck = -1;
							if (y + yAxis >= board[z].length)
								y = -1;
							if (x + xAxis >= board[z][column].length)
								x = -1;
							if (board[ztemp][ytemp][xtemp] == board[zCheck + zAxis][y + yAxis][x + xAxis]) {
								tempWin[count][0] = x + xAxis;
								tempWin[count][1] = y + yAxis;
								tempWin[count][2] = zCheck + zAxis;
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
								tempWin[count][0] = x - xAxis;
								tempWin[count][1] = y - yAxis;
								tempWin[count][2] = zCheck - zAxis;
								count++;
								zCheck -= zAxis;
								y -= yAxis;
								x -= xAxis;
							}
						} catch (ArrayIndexOutOfBoundsException e) {
							int ztemp = zCheck;
							int ytemp = y;
							int xtemp = x;
							if (zCheck - zAxis < 0)
								zCheck = board.length;
							if (y - yAxis < 0)
								y = board[z].length;
							if (x - xAxis < 0)
								x = board[z][column].length;
							if (board[ztemp][ytemp][xtemp] == board[zCheck - zAxis][y - yAxis][x - xAxis]) {
								tempWin[count][0] = x - xAxis;
								tempWin[count][1] = y - yAxis;
								tempWin[count][2] = zCheck - zAxis;
								count++;
								zCheck -= zAxis;
								y -= yAxis;
								x -= xAxis;
								break checkBackward;
							}
						}
						int value = (Math.pow(2, count);
						int moveX = tempWin[0][0];
						int moveY = tempWin[0][1];
						MoveTree.MoveTree(moveX, moveY, value);
						if (count < 5) {

						}
						else {
							winTiles = tempWin;
							return true;
						}
					}
				}
		return false;
	}
	private int currentPlayer = 0;

	public int turnOrder() {
		currentPlayer++;
		if (currentPlayer >= playerz.length)
			currentPlayer = 0;
		return currentPlayer;
	}

	public Player getCurrentPlayer() {
		return playerz[currentPlayer];
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
