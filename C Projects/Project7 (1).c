#include <stdio.h>
#include <string.h>

#define MAX_ITEMS 3
//COSC 2420
//Project7 - Fall 2023
//Angelique Pacheco
//12/6/23
//This program will be tracking inventory from the grocery store and creating a structure for the stores items current month,item names,id codes,price, etc.

struct GroceryItem {
    char month[20];
    char itemName[50];
    int idCode;
    int quantity;
    float price;
    float salesTotal;
};

//all parts of the item choices that are listed are set to 0
void initializeItems(struct GroceryItem items[]) {
    for (int i = 0; i < MAX_ITEMS; i++) {
        strcpy_s(items[i].month, sizeof(items[i].month), "Not Added");
        strcpy_s(items[i].itemName, sizeof(items[i].itemName), "Default Item");
        items[i].idCode = i + 1;
        items[i].quantity = 0;
        items[i].price = 0.0;
        items[i].salesTotal = 0.0;
    }
}

//this allows the user to add items no more than 3
void addItem(struct GroceryItem items[]) {
    int index;
    for (index = 0; index < MAX_ITEMS; index++) {
        if (items[index].quantity == 0) {
            break;
        }
    }

    if (index == MAX_ITEMS) {
        printf("Inventory is full. Cannot add more items.\n");
        return;
    }

    printf("Enter the current month: \n");
    getchar(); //read user input for the month entered
    fgets(items[index].month, sizeof(items[index].month), stdin);
    items[index].month[strcspn(items[index].month, "\n")] = '\0'; //Remove the trailing newline character
    
    printf("Enter the item name: \n");
    getchar(); //read user input for the month entered
    fgets(items[index].itemName, sizeof(items[index].itemName), stdin);
    items[index].itemName[strcspn(items[index].itemName, "\n")] = '\0'; // Remove the trailing newline character

    //I wrote the id code as for example: 101
    printf("Enter the identification code, ex. 101: ");
    scanf_s("%d", &items[index].idCode);

    printf("Enter the quantity: ");
    scanf_s("%d", &items[index].quantity);

    printf("Enter the price: ");
    scanf_s("%f", &items[index].price);

    items[index].salesTotal = 0.0;

    printf("Item added successfully.\n");
}

//gives the monthly report information
void monthlyReport(struct GroceryItem items[]) {
    char month[20];
    printf("Enter the current month: ");
    getchar();//reads user input for the month given
    fgets(month, sizeof(month), stdin);
    month[strcspn(month, "\n")] = '\0'; // Remove the trailing newline character

    printf("Monthly Report for %s:\n", month);
    for (int i = 0; i < MAX_ITEMS; i++) {
        printf("Item %d: Quantity in Stock = %d\n", items[i].idCode, items[i].quantity);
    }
}

//this give the daily report of the items totals
void dailyReport(struct GroceryItem items[]) {
    printf("Daily Report:\n");
    for (int i = 0; i < MAX_ITEMS; i++) {
        printf("Item %d: Sales Total = $%.2f\n", items[i].idCode, items[i].salesTotal);
    }
}
//updates the quantity of how many a certain item to be changed by readding the identification code
void updateQuantity(struct GroceryItem items[]) {
    int id, quantity;
    printf("Enter the identification code of the item, ex. 101: ");
    scanf_s("%d", &id);

    for (int i = 0; i < MAX_ITEMS; i++) {
        if (items[i].idCode == id) {
            printf("Enter the new quantity: ");
            scanf_s("%d", &quantity);

            items[i].quantity = quantity;
            printf("Quantity updated successfully.\n");
            return;
        }
    }
    //id number couldnt not be found that got typed wrong or doesn't exist
    printf("Item with identification code %d not found.\n", id);
}

//prompt the user to enter a id code and how much of a item was sold
void itemSale(struct GroceryItem items[]) {
    int id, quantity;
    printf("Enter the identification code of the item sold, ex 101: ");
    scanf_s("%d", &id);

    int isValidId = 0;
    for (int i = 0; i < MAX_ITEMS; i++) {
        if (items[i].idCode == id) {
            isValidId = 1;
            break;
        }
    }

    if (!isValidId) {
        printf("Item with identification code %d not found.\n", id);
        return;
    }

    for (int i = 0; i < MAX_ITEMS; i++) {
        if (items[i].idCode == id) {
            printf("Enter the quantity sold: ");
            scanf_s("%d", &quantity);

            if (quantity > items[i].quantity) {
                printf("Error: Not enough stock available.\n");
            }
            else {
                items[i].quantity -= quantity;
                items[i].salesTotal += quantity * items[i].price;
                printf("Sale recorded successfully.\n");
            }
            return;
        }
    }

    printf("Item with identification code %d not found.\n", id);
}

int main() {
    struct GroceryItem items[MAX_ITEMS];
    int choice;

    initializeItems(items);

    //loop for what is told to the user and switch statement for the choice they pick
    do {
        printf("\nMenu:\n");
        printf("0. Exit\n");
        printf("1. Add item\n");
        printf("2. Item Sale\n");
        printf("3. Update Quantity\n");
        printf("4. Daily Report\n");
        printf("5. Monthly Report\n");
        printf("Enter your choice: ");
        scanf_s("%d", &choice);

        switch (choice) {
        case 0:
            printf("Exiting program.\n");
            break;
        case 1:
            addItem(items);
            break;
        case 2:
            itemSale(items);
            break;
        case 3:
            updateQuantity(items);
            break;
        case 4:
            dailyReport(items);
            break;
        case 5:
            monthlyReport(items);
            break;
        default:
            printf("Please enter a valid option.\n");
        }
    } while (choice != 0);

    return 0;
}