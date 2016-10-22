import java.util.Scanner;

public class Test {
	static Connect5 rawr = new Connect5(5, 5, 5, 1);

	public static void main(String[] args) {
		Scanner myScanner = new Scanner(System.in);
		boolean running = true;
		while (running) {
			System.out.println("Current player: " + rawr.getCurrentPlayer().playerNumber);
			System.out.print("1: Place token\n2: View maze\n3: Exit\n");
			int choice = Integer.parseInt(myScanner.nextLine());
			switch (choice) {
			case 1:
				System.out.println("Row:");
				int row = Integer.parseInt(myScanner.nextLine());
				System.out.println("Column:");
				int column = Integer.parseInt(myScanner.nextLine());
				if (rawr.getCurrentPlayer().move(row, column)) {
					System.out.println("Win!");
					for (int i = 0; i < 5; i++) {
						for (int j = 0; j < 3; j++) {
							System.out.print(rawr.getWinTiles()[i][j]);
						}
						System.out.println();
					}
				}
				break;
			case 2:
				for (int[][] z : rawr.getBoard()) {
					for (int[] y : z) {
						for (int x : y)
							System.out.print(x);
						System.out.println("");
					}
					System.out.println("");
				}
				break;
			case 3:
				running = false;
				break;
			}

		}
	}
}
