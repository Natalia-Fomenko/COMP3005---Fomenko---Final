# COMP3005 Final Project
Natalia Fomenko, 100909652

The program is written in IntelliJ (Java). 
The user is prompted to enter user type (member, trainer, or admin).
Then the user is prompted to choose one of the options below:
2.1 Member Functions
	User Registration 
	Profile Management 
	Health History 
	Dashboard 
	Session Registration (PT/Group)
2.2 Trainer Functions
	Set Availability 
	Schedule View 
	Member Lookup
2.3 Administrative Staff Functions
	Class Management 
	Billing & Payment
The process repeats until the user enters zero (0).

## Building and Running
First, setup the initial data in pgAdmin 4: 
1. Open pgAdmin 4 and leave it running 
2. In pgAdmin 4, create a new database called Final.
3. In Final, open the query tool, open and run DDL 
4. In Final, open the query tool, open and run DML

Next, perform these steps in Intellij (Java): 
1. In IntelliJ, create a new Maven project called Final.
2. Copy Maven (https://jdbc.postgresql.org/download/), 
   and insert it as dependency into your pom.xml file
3. In the java folder, create a new file called Final.java 
4. Copy all the code from Final.java provided on GitHub 
   and paste it into your Final.java 
5. Run Final.java (green button at the top).

Link to video: https://youtu.be/1XgYQs3gqBE

## Credits
- Developed individually by Natalia Fomenko (Student ID 100909652).




