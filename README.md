  Renewable Energy Project
This project is a web-based application built to manage renewable energy data. It interacts with a PHPMyAdmin database using PHP files and allows users to view, insert, update, and delete data in real-time. The application provides an intuitive user interface to manage data for energy sources, reports, operators, and monitors.

Table of Contents
Introduction
Installation
Usage
Features


Introduction
This project is designed to be a user-friendly solution for managing data related to renewable energy sources, reports, operators, and monitors. It is connected to a MySQL database, with a web-based interface built using PHP and CSS. The GUI allows users to perform CRUD (Create, Read, Update, Delete) operations in real-time through a web interface that interacts with the database tables.

Main Tables:
Energy Source: Stores data about different types of energy sources.
Report: Stores renewable energy reports.
Operator: Manages operators responsible for the energy systems.
Monitor: Stores data related to monitoring the energy systems.

**Installation**
Prerequisites
XAMPP: The project runs locally on XAMPP (Apache + MySQL).
PHP: The backend is built using PHP.
PHPMyAdmin: A database is used to store and retrieve data.
CSS: For frontend styling.


Move Project Files to XAMPP's htdocs Folder: Move the project files into the htdocs directory of your XAMPP installation. This is the root directory where you can place your web project files.

Example path:

makefile
Copy code
C:\xampp\htdocs\DBproject\

Set Up the Database:

Open phpMyAdmin by navigating to http://localhost/phpmyadmin/ in your browser.
Create a new database called project.
Manually create the required tables (energysource, report, operator, monitor).

Configure Database Connection:

Update the database connection details in the PHP files .
Database connection code:

<?php
$servername = "localhost";
$username = "root"; // Default XAMPP MySQL username
$password = "";     // Default XAMPP MySQL password
$dbname = "renewable_energy_db";

// Create connection
$conn = new mysqli($servername, $username, $password, $dbname);

// Check connection
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}
?>

Start XAMPP: Ensure Apache and MySQL are running on XAMPP, then navigate to http://localhost/DBProject/ in your browser.

Usage
Navigating the Web Interface:

Upon loading the application, you'll be presented with the user interface (UI) to interact with the data.
The interface contains textboxes and buttons for inserting, updating, and deleting records.
Operations in the GUI:

Insert: Fill in the textboxes for the corresponding table (Energy Source, Report, Operator, Monitor) and click the "Insert" button to add data to the database.
Update: Modify existing entries except for ID's in the textboxes and click the "Update" button to save changes to the database.
Delete: Select a record and click the "Delete" button to remove the entry from the database.
Database Updates: All changes made in the UI (inserts, updates, deletes) will reflect in the MySQL database in real time.

Example Usage
To insert a new energy source:

1. Go to the "Energy Source" section in the UI.
2. Enter the energy source details in the provided textboxes.
3. Click the "Insert" button to add it to the database.
4. The newly added entry will be displayed in the table.

Features
Real-time Data Management: Insert, update, and delete data directly from the GUI to the MySQL database.
User-Friendly Interface: Simple and intuitive user interface designed for easy interaction.
Database Interaction: Connects directly to MySQL via PHP for seamless CRUD operations.
Energy Source Management: Allows users to manage renewable energy sources efficiently.
Operator and Monitoring Management: Manage operators and monitoring data related to energy systems.


