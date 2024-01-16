#include<stdio.h>
//COSC 2420
//Project 1-Fall 2023
//Angelique Pacheco
//Sept. 13, 2023
//This program will have the user give a minimum and maximum temperature and then determine the average temperature.  


int main()
{
	int min,max,avg;
	printf("Enter a temperature:\n");
	
	scanf_s("%d", &max);
	scanf_s("%d", &min);

	avg = (max + min)/2;

	printf("%d", avg);
	return 0;
}