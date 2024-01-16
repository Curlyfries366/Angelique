#include<stdio.h>
//COSC 2420
//Project 1-Fall 2023
//Angelique Pacheco
//Sept. 13, 2023
//This program will display an integer that will add 5, double it, subtract 7, and display the final outcome. 


int main()
{
	int num1, sum;
	printf("Enter a number:\n");
	scanf_s("%d", &num1);

	sum = num1 + 5 * 2 - 7;

	printf("%d", sum);
	return 0;
}