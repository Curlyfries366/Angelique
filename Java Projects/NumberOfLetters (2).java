import java.util.Scanner;

//Angelique Pacheco
//COSC 2430
//Project#
//2/19/23
// This program will be able to read the text entered by the user ending with a period and will continue the loop depending if the user says 'yes' and stops the program at 'no'.

public class NumberOfLetters {
	public static void main(String[] args) {
		Scanner input = new Scanner(System.in);
		String period = ".";
		boolean repeat = true; // only repeats the while loop if the user says yes

		//while loop will repeat if the condition is true when the user says yes
		while (repeat) {
			System.out.println("Please enter a line of text that ends with a period: ");
			period = input.nextLine();

			if ("." == (period)) {
				break;
			}
			
			count(period); // goes to method count

			System.out.println("Would you like to try another text? (yes/no)");
			Scanner YesOrNo = new Scanner(System.in);
			String answer = YesOrNo.nextLine();

			if ("No".equalsIgnoreCase(answer)) { // stops the program from user input is no and is not case sensitive
				repeat = false;
				break;
			}

			else if ("Yes".equalsIgnoreCase(answer)) { // repeats the loop when user says yes
				repeat = true;
			}
		}
	}

	private static void count(String letter) {
		
		//the integer will be from the index of 26
		int[] array = new int[26];
		letter = letter.toUpperCase(); //this will make the letters upper case

		// scan and increment count of character frequency
		for (int i = 0; i < letter.length(); ++i) {
			if (letter.charAt(i) < 65 || letter.charAt(i) > 90) {
				continue;
				
			} else
				++array[(char) (letter.charAt(i)) - 65];

		}
			
		// prints out the letter and counts how many characters are in the text
		for (int i = 0; i <= 25; i++) {
			if (array[i] == 0)
				continue;
			
			System.out.println("Letter: " + (char) ('A' + i) + " freq. " + array[i]);

		}
	}
}