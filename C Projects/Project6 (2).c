#include <stdio.h>
#define SIZE 10

//COSC 2420
//Project 6 -Fall 2023
//Angelique Pacheco
//Nov 15, 2023
//This program will assign seats for the user either in the first class or economy by asking the user which seat they want on the airplane and tells user that next flight is in 3 hrs if both classes are full.

    int main() {
        int seats[SIZE] = { 0 };
        int seatNumber, choice, alternateChoice;

        while (1) {
            printf("Please type 1 for 'first class', 2 for 'economy', and 0 to exit\n");
            scanf_s("%d", &choice);

            if (choice == 0) {
                printf("Next flight leaves in 3 hours.\n");
                break;
            }

            //Checks if both sections are full
            int firstClassFull = 1;
            int economyFull = 1;

            for (int i = 0; i < SIZE; i++) {
                if (i < 5 && seats[i] == 0) {
                    firstClassFull = 0;
                }
                else if (i >= 5 && seats[i] == 0) {
                    economyFull = 0;
                }
                
            }



            //User input of choosing first class or economy
            if (choice == 1 || choice == 2) {
                if (choice == 1) {

                    //first class section
                    for (seatNumber = 0; seatNumber < 5; seatNumber++) {
                        if (seats[seatNumber] == 0) {
                            seats[seatNumber] = 1; //assigns the seat
                            break;
                        }
                    }
                    if (seatNumber == 5) {
                        if (!economyFull) {
                            printf("First class is full. Would you like to be placed in the economy section? (1 for Yes, 0 for No)\n");
                            scanf_s("%d", &alternateChoice);
                            if (alternateChoice == 1) {
                                choice = 2; //switch to the economy section
                                seatNumber = 5; //start from the first economy seat
                            }
                        }
                        
                    }
                }
                else {
                    // Economy section
                    for (seatNumber = 0; seatNumber < 10; seatNumber++) {
                        if (seats[seatNumber] == 0) {
                            seats[seatNumber] = 1; //Assigns the seat
                            break;
                        }
                    }
                    if (seatNumber == 10) {
                        if (!firstClassFull) {
                            printf("Economy is full. Would you like to be placed in the first-class section? (1 for Yes, 0 for No)\n");
                            scanf_s("%d", &alternateChoice);
                            if (alternateChoice == 1) {
                                choice = 1; //Switch to first-class section
                                seatNumber = 0; //Start from the first first-class seat
                            }
                            
                        }
                   
                    }
                }

                //Both sections are full
                if (firstClassFull && economyFull) {
                    printf("Both first-class and economy are full, next flight leaves in 3 hours. \n");
                    break;
                }

                printf("Your seat number is %d.\n", seatNumber + 1);
                if (choice == 1) {
                    printf("First Class\n");
                }
                else {
                    printf("Economy\n");
                }
            }
        }

        return 0;
    }