#include<stdio.h>

//COSC2430-001
// Project 4-Fall 2023
// Angelique Pacheco
// 10/16/2023
// This program will be creating a diamond made of asterisks that will be determined be the user entering a odd number between 1-19.

int main() {
    int odd, i, j;
    printf("Enter an odd number for the diamond size (1-19): \n");
    scanf_s("%d", &odd);


    //determines if the value is odd than it will contine loop, if not true exists loop
    if (odd < 1 || odd > 19 || odd % 2 == 0) {
        printf("This is not a odd number!\n");
        return 0;
    }
    
        //top of diamond
        for (i = 1; i <= odd; i += 2) {
           
            //prints the spaces
            for (j = 1; j <= (odd - i) / 2; j++) {
                printf(" ");
            }
           
            for (j = 1; j <= i; j++) {
                printf("*");
            }
            printf("\n");
        }

        //bottom of diamond
        for (i = odd - 2; i >= 1; i -= 2) {

            for (j = 1; j <= (odd - i) / 2; j++) {
                printf(" ");
            }
            for (j = 1; j <= i; j++) {
                printf("*");
            }
            printf("\n");
        }
        return 0;
}
