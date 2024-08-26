import java.util.Scanner;

public class TicTacToe {
	private static char[][] board = new char[3][3];

	// Initializes the board with default symbols
	public static void initializeBoard() {
		for (int row = 0; row < 3; row++) {
			for (int col = 0; col < 3; col++) {
				board[row][col] = '-';
			}
		}
	}

	// Displays the board
	public static void displayBoard() {
		for (int row = 0; row < 3; row++) {
			System.out.print((row + 1) + "|");
			for (int col = 0; col < 3; col++) {
				System.out.print(board[row][col] + " |");
			}
			System.out.println();
		}
	}

	// Handles player's move input
	public static void getMove(char symbol) {
		Scanner input = new Scanner(System.in);
		int row, col;
		while (true) {
			System.out.println(symbol + "'s turn. Enter row # and column # with spaces (1-3):");
			row = input.nextInt() - 1;
			col = input.nextInt() - 1;

			if (isValidMove(row, col)) {
				board[row][col] = symbol;
				break;
			} else {
				System.out.println("Invalid move. Try again.");
			}
		}
		displayBoard();
	}

	// Validates if the move is within the bounds and not overriding
	public static boolean isValidMove(int row, int col) {
		return row >= 0 && row < 3 && col >= 0 && col < 3 && board[row][col] == '-';
	}

	// Checks if the current player has won or if it's a draw
	public static boolean isGameOver(char symbol) {
		if (checkWinner(symbol) || checkDraw()) {
			return true;
		}
		return false;
	}

	// Checks for a winner
	public static boolean checkWinner(char symbol) {
		for (int i = 0; i < 3; i++) {
			if ((board[i][0] == symbol && board[i][1] == symbol && board[i][2] == symbol) || // rows
				(board[0][i] == symbol && board[1][i] == symbol && board[2][i] == symbol)) { // columns
				System.out.println(symbol + " wins!");
				return true;
			}
		}
		// diagonals
		if ((board[0][0] == symbol && board[1][1] == symbol && board[2][2] == symbol) || 
			(board[0][2] == symbol && board[1][1] == symbol && board[2][0] == symbol)) {
			System.out.println(symbol + " wins!");
			return true;
		}
		return false;
	}

	// Checks for a draw
	public static boolean checkDraw() {
		for (int row = 0; row < 3; row++) {
			for (int col = 0; col < 3; col++) {
				if (board[row][col] == '-') {
					return false;
				}
			}
		}
		System.out.println("It's a draw!");
		return true;
	}

	// Starts a new game
	public static void startGame() {
		char currentPlayer = 'X';
		initializeBoard();
		displayBoard();
		while (!isGameOver(currentPlayer)) {
			getMove(currentPlayer);

			//switches the players move to make
			if (currentPlayer == 'X') {
			    currentPlayer = 'O';
			} else {
			    currentPlayer = 'X';
			}
		}
	}

	public static void main(String[] args) {
		Scanner input = new Scanner(System.in);
		String response;
		do {
			startGame();
			System.out.println("Play again? (y/n):");
			response = input.nextLine();
		} while (response.equalsIgnoreCase("y"));
		input.close();
	}
}
