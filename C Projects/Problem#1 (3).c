#include<stdio.h>
//COSC 2420
//Project 1-Fall 2023
//Angelique Pacheco
//Sept. 13, 2023
//This program will display two numbers from the user and displaying the sum,difference,product,and quotient. 


int main()
{
	int num1, num2, sum, dif, mul, div; //sum is addition, dif is subtraction, mul is multiplication, and div is for division

	printf("Enter two numbers:\n");
	scanf_s("%d", &num1);
	scanf_s("%d", &num2);

	sum = num1 + num2;
	dif = num1 - num2;
	mul = num1 * num2;
	div = num1 / num2;

	printf("%d + %d = %d\n", num1, num2, sum);
	printf("%d - %d = %d\n", num1, num2, dif);
	printf("%d * %d = %d\n", num1, num2, mul);
	printf("%d / %d = %d\n", num1, num2, div);

	return 0;
}