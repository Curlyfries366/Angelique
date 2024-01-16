#include<stdio.h>
//COSC2430-001
// Project 2-Fall 2023
// Angelique Pacheco
// 9/25/2023
// This program will determine the user's birthday and current day to get age, then get the maximum heartrate, and target heart rate. 

int main() {
	int bMonth,age,bDay,bYear,currentMonth,currentDay,currentYear,ageMonth,ageYear;
	float avgHeartmax, avgHeartmin, maxHeartRate;

	printf("Please enter you date of birth by month, day, and year: \n");
    
	scanf_s("%d\n", &bMonth);
	scanf_s("%d\n", &bDay);
	scanf_s("%d", &bYear);
    
	
	printf("Please enter the current date by month, day, and year: \n");

	scanf_s("%d\n", &currentMonth);
	scanf_s("%d\n", &currentDay);
	scanf_s("%d", &currentYear);
	
	//determines age number by the month and year
	ageMonth = currentMonth - bMonth; 
	ageYear = currentYear - bYear; 

	if (ageMonth >= 0) {
		if (ageYear > 0) {
			age = ageYear; //age if no needed subtraction
		}
	}
	else {
		ageYear = ageYear - 1;
		ageMonth = ageMonth + 12; //age if months is transfered
		age = ageYear;
	}

	maxHeartRate = (220 - age); //gets the maximum heart rate by 220 minus age in years
	avgHeartmin = maxHeartRate * .5;
	avgHeartmax = maxHeartRate * .85;

	printf("Date of Birth: %d/%d/%d\n", bMonth, bDay, bYear);
	printf("Age: %d\n", age);
	printf("Maximum Heart Rate: %.2f\n", maxHeartRate); //floating number instead of producing a whole number
	printf("Target Heart Rate Range: %.2f - %.2f\n", avgHeartmin, avgHeartmax);

	return 0;
}