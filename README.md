# Java-Based Online Examination Platform

A Java Swing + SQLite desktop application suitable for a Java programming internship project.

## Features
- Student registration and login
- Admin login
- Exam selection
- Timed MCQ examination
- Automatic submission when time expires
- Automatic scoring
- Result storage
- Student result history
- Admin question management
- Admin result viewing

## Requirements
- JDK 17+
- Maven 3.8+
- SQLite JDBC driver (managed automatically by Maven)

## Setup
1. Run the app once; it will create the SQLite database automatically.
2. If needed, you can also run `database.sql` manually to initialize the schema.
3. Run:
   `mvn clean compile exec:java`
4. Default admin:
   - Username: admin
   - Password: admin123

The sqlite schema inserts a sample Java Programming exam and questions.

## Important
This is an educational project. For production use, passwords should be hashed, configuration should use environment variables/secrets, and authentication should be hardened.
