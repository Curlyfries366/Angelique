//Angelique Pacheco
//COSC 2430 
//Project #4
//3/20/23
//This program will having a left and right arrows drawn that will be composed of the tail and arrowhead, both are drawn using keyboard characters.
import java.util.*;

public class ArrowDemo {
	//display either Left arrow or Right arrow
	//offset = 0

	public static void main(String[] args) {
		int tail, width;
		int x = 0; //to stop the program
		while (x != 3)
		{
			//opening display
			System.out.println("Select left or right arrow");
			System.out.println("1. Left");
			System.out.println("2. Right");
			System.out.println("3. End program");
			System.out.println("Please select 1, 2, or 3:");
			
			//declare objects
			Scanner sc = new Scanner(System.in);
			RightArrow r = new RightArrow(0, 0, 0); //object for left arrow
			LeftArrow l = new LeftArrow(0, 0, 0); //object for right arrow
			
			x = sc.nextInt(); //choice
			if (x == 1) //left arrow
			{
				System.out.print("Enter length of left arrow:");
				tail = sc.nextInt();
				System.out.print("Enter width of left arrow:");
				width = sc.nextInt();
				if (width % 2 == 0) // if width is even
					System.out.println("Width must be a odd number!!");
				else {
					l.set(tail, width); //setting tail and width
					l.drawHere(); //draw tail and width
					System.out.println();
				}
			} 
			else if (x == 2) //right arrow
			{
				System.out.print("Enter length of right arrow:");
				tail = sc.nextInt();
				System.out.print("Enter width of right arrow:");
				width = sc.nextInt();
				if (width % 2 == 0) //if width is even
					System.out.println("Width must be a odd number!!");
				else 
				{
					r.set(tail, width); //sets tail and width
					r.drawHere(); //draw tail and width
					System.out.println();
				}
			} 
			else if (x == 3) //ends the program
			{
				break;
			} 
			else //executes when the user puts the wrong number choice 
			{
				System.out.println("Wrong choice!!!");
			}
		}
	}
}
//class for right arrow
class RightArrow extends ShapeBase {
	private int tail;
	private int width;

	RightArrow()
	{
		super();
		tail = 0;
		width = 0;
	}

	RightArrow(int theOffset, int tailSize, int theWidth) {
		super(theOffset);
		tail = tailSize;
		width = theWidth;
	}

	void set(int newHeight, int newWidth) {
		tail = newHeight;
		width = newWidth;
	}

	//draws the shape at the current line
	public void drawHere()
	{
		topArrowHead();
		ArrowTail();
		bottomArrowHead();
	}

	void topArrowHead() {
		//draws the upper half part
		skipSpaces(tail);
		System.out.println("*");
		for (int i = 1; i < width / 2; i++)
		{
			skipSpaces(tail);
			System.out.print("*");
			skipSpaces(2 * i - 1);
			System.out.println("*");
		}
	}

	void ArrowTail() {
		//draws the middle part including tail
		for (int i = 0; i < tail; i++)
			System.out.print("*");
		System.out.print("*");
		skipSpaces(width / 2 + 2);
		System.out.println("*");
	}

	void bottomArrowHead() {
		//draws the lower half part
		for (int i = width / 2 - 1; i > 0; i--) 
		{
			skipSpaces(tail);
			System.out.print("*");
			skipSpaces(2 * i - 1);
			System.out.println("*");
		}
		skipSpaces(tail);
		System.out.print("*");
	}

	private static void skipSpaces(int number) 
	{
		for (int count = 0; count < number; count++)
			System.out.print(" ");
	}
}
//class for the Left arrow 
class LeftArrow extends ShapeBase {
	private int tail;
	private int width;

	LeftArrow()
	{
		super();
		tail = 0;
		width = 0;
	}

	LeftArrow(int theOffset, int tailSize, int theWidth) 
	{
		super(theOffset);
		tail = tailSize;
		width = theWidth;
	}

	void set(int newHeight, int newWidth)
	{
		tail = newHeight;
		width = newWidth;
	}

	//draws the shape at the current line.
	public void drawHere() 
	{
		topArrowHead();
		ArrowTail();
		bottomArrowHead();
	}

	void topArrowHead() {
		//draws the upper half part
		skipSpaces(width/2+3);
		System.out.println("*");
		for (int i = 1; i<width/2; i++) 
		{
			skipSpaces(width/2+3-2 * i);
			System.out.print("*");
			skipSpaces(2 * i - 1);
			System.out.println("*");
		}
	}

	public void ArrowTail() 
	{
		//draws the middle part including tail
		System.out.print("*");
		skipSpaces(width/2 + 2);
		System.out.print("*");
		for (int i = 0; i < tail; i++)
			 System.out.print("*");
		System.out.println();
	}

	public void bottomArrowHead()
	{
		//draws the lower half part
		for (int i = 0; i< width/2 - 1; i++) {

			skipSpaces(2 * i + 2);
			System.out.print("*");
			skipSpaces(width / 2 - 2 * i);
			System.out.println("*");
		}
		skipSpaces(width / 2 + 3);
		System.out.print("*");
	}

	private static void skipSpaces(int number) 
	{
		for (int count = 0; count < number; count++)
			System.out.print(" ");
	}
}

//abstract base class for drawing simple shapes on the screen using keyboard characters
abstract class ShapeBase implements ShapeInterface {
	private int offset;

	ShapeBase() 
	{
		offset = 0;
	}

	ShapeBase(int theOffset) 
	{
		offset = theOffset;
	}

	public abstract void drawHere();

	public void drawAt(int lineNumber) 
	{
		for (int count = 0; count < lineNumber; count++)
			System.out.println();
	}

	public void setOffset(int newOffset)
	{
		offset = newOffset;
	}

	public int getOffset()
	{
		return offset;
	}
}

interface ShapeInterface {

	//sets the offset for the drawing
	public void setOffset(int newOffset);

	//returns the offset for the drawing
	public int getOffset();

	//draws the shape at lineNumber lines down from the current line
	public void drawAt(int lineNumber);

	//draws the shape at the current line
	public void drawHere();
}