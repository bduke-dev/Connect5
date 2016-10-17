import java.util.Scanner;

public class Test {
	static Connect5 rawr = new Connect5(5, 5, 5, 2);

	public static void main(String[] args) {
		Scanner myScanner = new Scanner(System.in);
		boolean running = true;
		while (running) {
			System.out.println("Current player: " + rawr.getCurrentPlayer());
			System.out.print("1: Place token\n2: View maze\n3: Exit\n");
			int choice = Integer.parseInt(myScanner.nextLine());
			switch(choice){
			case 1:
				System.out.println("Row:");
				int row = Integer.parseInt(myScanner.nextLine());
				System.out.println("Column:");
				int column = Integer.parseInt(myScanner.nextLine());
				if(rawr.playerz[rawr.getCurrentPlayer()-1].move(row, column)) System.out.println("Win!");
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
