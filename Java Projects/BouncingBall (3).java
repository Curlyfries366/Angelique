import java.util.Scanner;

//Angelique Pacheco 
//COSC 2430 
//Project #1
//2/5/23
//This program will be simulate a bouncing ball from its height at each second the time passes on a simulated clock

public class BouncingBall {

	public static void main(String[] args) {

		Scanner velocity = new Scanner(System.in);

		System.out.print("Enter the initial velocity of the ball: ");
		double Velocity = velocity.nextInt();
		double height = 0;
		int bounce = 0;

		// for loop will act as the timer
		for (int t = 0; t >= 0; t++) {

			System.out.println("Time: " + t + " Height: " + height + " ft.");

			// if loop makes the program simulate the bouncing ball
			if (t >= 0) {
				height = height + Velocity;
				Velocity = Velocity - 32.0;
			}
			// if loop shows how many bounces have happened
			if (height <= 0) {
				height = height * -0.5;
				Velocity = Velocity * -0.5;
				System.out.println("Bounce!");
				bounce++;
			}
			// this stops the bounce at 5
			if (bounce == 5) {
				break;
			}
		}
		System.out.println("Press any key to continue...");
	}
}
