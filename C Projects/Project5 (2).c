#include<stdio.h>
#include<math.h>

//COSC2420-001
//Project5-Fall 2023
//Angelique Pacheco
//10/30/2023
//This program will print the results that calculates and print the parking charges for each 3 customers who parked in the garage yesterday.

float calculateCharges(float);

int main() {
    float car1, car2, car3;
    float totalCharges = 0;

    printf("Enter the hours parked for 3 cars:\n");
    scanf_s("%f %f %f", &car1, &car2, &car3); //user input from scanf 

    printf("Car     Hours  Charge \n");

    // Print the individual charges for each customer
    float charge1 = calculateCharges(car1);
    totalCharges += charge1 ;
    printf("1       %.1f    %.2f \n", car1, charge1);

    float charge2 = calculateCharges(car2);
    totalCharges += charge2;
    printf("2       %.1f    %.2f \n", car2, charge2);

    float charge3 = calculateCharges(car3);
    totalCharges += charge3;
    printf("3       %.1f   %.2f \n", car3, charge3);

    // gets the total charges for hours and charge
    printf("Total   %.1f   %.2f \n", car1 + car2 + car3, totalCharges);

    return 0;
}

float calculateCharges(float hours) {
    float minimumFee = 2.0;
    float additionalHourlyRate = 0.5;
    float maxCharge = 10.0;

    //determines the charges for each of the 3 customers
    if (hours <= 3.0) {
        return minimumFee;
    }
    else if (hours <= 24.0) {
        float over3Hours = hours - 3.0;
        float charge = minimumFee + ceil(over3Hours) * additionalHourlyRate;

        // Checks if the calculated charge exceeds the maximum charge
        if (charge > maxCharge) {
            charge = maxCharge;
        }
        return charge;
    }

    else {
        return maxCharge;
    }
}