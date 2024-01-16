#include<stdio.h>
#include<math.h>
//COSC2420-001
//Project 3-Fall 2023
//Angelique Pacheco
//10/4/2023
//This program will be calculating the cost of phone calls, input the time and length by the user, and output the discount rate,cost,time, and length for the call.

int main() {
    float length, hour, cost = .49, discountRate;

    printf("Enter the time of call began in hours (ex. 24): ");
    scanf_s("%f", &hour);
    printf("Enter the length of the call in minutes followed by seconds(ex. 2.7): ");
    scanf_s("%f", &length);


    // rounds up the length of the call like 2.7 to 3 
    int additionalMinutes = ceil(length - 1);

    //  formula for the cost for additional minutes
    cost += additionalMinutes * 0.37;

    // this determines the discount rate based on the time of day
    if ((hour >= 16 && hour < 22) || (hour >= 0 && hour < 7)) {
        if (hour >= 16 && hour < 22) {
            discountRate = 0.35; // 35% evening discount
        }
        else {
            discountRate = 0.65; // 65% evening discount
        }
        cost *= (1 - discountRate); //applies the 65 or 35 discount
    }
    else {
        discountRate = 0; // no discount made
    }

    // gives the user hour,length,cost and discount rate
    printf("Time of call: %.2f \n", hour);
    printf("Length of call: %.0f minutes\n", length);
    printf("Cost: $%.2f\n", cost);
    printf("Discount applied: %.0f percent\n", discountRate * 100);

    return 0;
}