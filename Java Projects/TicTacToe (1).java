import java.util.Scanner;
//Angelique Pacheco
//COSC 2430 
//Project #3
//3/6/23
// This program will allowing two people using the same keyboard to play a game of TicTacToe. private char[][] board;

public class TicTacToe {
	// Use a 3 X 3 (two-dimensional) array for the game board.
	private static char[][] board = new char[3][3];

	// writes the default board without any symbols
	public static void defaultBoard() {
		for (int row = 0; row < 3; row++) {
			for (int col = 0; col < 3; col++) {
				board[row][col] = '-';
			}
		}
	}

	// displays the board
	public static void writeBoard() {
		System.out.println("R/C|1| 2| 3|");
		for (int row = 0; row < 3; row++) {
			System.out.print(row + 1 + "|");
			for (int col = 0; col < 3; col++) {
				System.out.print(board[row][col] + " |");
			}
			System.out.println(" ");
		}
	}

	// Get Player Move
	public static char getMove(char symbol) {
		Scanner Input = new Scanner(System.in);
		int row, col;

		while (true) {
			// Prompt X's or O's turns
			if (symbol == 'X') {
				System.out.println("X's turn.");
				System.out.println("Where do you want your " + symbol +" placed?");
				System.out.println("Please enter the row number and column number separated by a space.");
			} else {
				System.out.println("O's turn.");
				System.out.println("Where do you want your " + symbol +" placed?");
				System.out.println("Please enter the row number and column number separated by a space.");
			}

			// set row and column move and subtract it by 1
			row = Input.nextInt() - 1;
			col = Input.nextInt() - 1;

			// executes when move is valid
			if (row >= 0 && row < 3 && col >= 0 && col < 3) {
				System.out.println("You have entered row #" + (row+1) + " and column #" + (col+1));
				System.out.println("Thank you for your selection.");
				System.out.println(" ");
				
				// invalid if move is overriding
				if (board[row][col] != '-') {
					System.out.println("That cell is already taken.");
					System.out.println("Please make another selection");
					continue;
				}
				break;
			}

		}

		// sets the symbol to the corresponding square
		board[row][col] = symbol;
		writeBoard();

		return symbol;
	}

	// check winner
	public static boolean winner(char symbol) {

		// checks column
		for (int col = 0; col < 3; col++) {
			if (board[0][col] == symbol && board[1][col] == symbol && board[2][col] == symbol) {
				playerWin(symbol);
				return true;
			}
		}

		// checks row
		for (int row = 0; row < 3; row++) {
			if (board[row][0] == symbol && board[row][1] == symbol && board[row][2] == symbol) {
				playerWin(symbol);
				return true;
			}
		}

		// diagonal checks
		if (board[0][0] == symbol && board[1][1] == symbol && board[2][2] == symbol
				|| board[2][0] == symbol && board[1][1] == symbol && board[0][2] == symbol) {
			playerWin(symbol);
			return true;
		}

		// These lines execute only if there is no winner (Draw).
		boolean draw = true;
		for (int row = 0; row < 3; row++) {
			for (int col = 0; col < 3; col++) {
				if (board[row][col] == '-') {
					draw = false;
					break;
				}
			}
		}
		if (draw) {
			System.out.println("Draw!");
			return true;
		}

		return false;
	}

	// executes when a player won
	public static void playerWin(char symbol) {
		if (symbol == 'X') {
			System.out.println("X is the winner!!!");
		} else {
			System.out.println("O is the winner!!!");
		}
	}

	// starts a new game
	public static void newGame() {

		boolean gameover = false;
		char symbol = 'X';
		defaultBoard();
		writeBoard();

		// loop till game is over
		while (!gameover) {
			symbol = getMove(symbol);
			gameover = winner(symbol);

			if (symbol == 'X')
				symbol = 'O';

			else
				symbol = 'X';

		}
	}

	public static void main(String[] args) {
		Scanner input = new Scanner(System.in);
		String response;
		boolean stop = false;

		// loops if user wish to keep going
		while (!stop) {
			newGame();

			while (true) {
				System.out.println("Would you like to keep playing? y/n");
				response = input.nextLine();

				if (response.equalsIgnoreCase("y"))
					break;

				if (response.equalsIgnoreCase("n")) {
					stop = true;
					break;
				}

			}
		}
	}
}