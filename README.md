# Online Examination Platform

A professional Java Swing desktop application for conducting timed multiple-choice examinations. The application uses SQLite, so it runs locally without requiring a separate database server.

## Features

### Students
- Create a student account
- Log in securely with email and password
- Browse available examinations
- Take timed multiple-choice exams
- Navigate between questions
- Submit exams manually or automatically when time expires
- View scores and previous results
- Secure fullscreen exam mode with focus-loss warnings

### Administrators
- Log in through the admin portal
- Create examinations and set durations
- Add questions with four answer options
- Select the correct answer
- Delete examinations and related records
- View submitted student results
- Refresh live exam and result metrics

## Technology Stack

- Java 17+
- Java Swing
- Maven
- SQLite
- JDBC
- JUnit 5

## Project Structure

```text
src/main/java/com/internpe/onlineexam/
  Main.java              Application entry point
  LoginFrame.java        Student and admin login
  RegistrationFrame.java Student registration
  StudentDashboard.java  Student exam and result dashboard
  AdminDashboard.java    Exam and question management
  ExamFrame.java         Timed fullscreen examination window
  DBConnection.java      SQLite connection and schema setup
  Question.java          Question model
  Session.java           Current student session

database.sql             SQLite schema and sample data
pom.xml                  Maven build configuration
```

## Requirements

- JDK 17 or newer
- Maven 3.8 or newer
- Windows, macOS, or Linux with a graphical desktop environment

SQLite JDBC is downloaded automatically by Maven.

## Run Locally

Open a terminal in the project directory:

```powershell
cd F:\OnlineExaminationPlatform
mvn clean compile exec:java
```

The application opens the login window. On first launch, the file `online_exam.db` is created automatically and the schema/sample exam is initialized.

## Build a Runnable JAR

```powershell
mvn clean package -DskipTests
```

The executable shaded JAR should be generated in `target` with the name:

```text
online-examination-platform-1.0.0-all.jar
```

Run it with:

```powershell
java -jar target\online-examination-platform-1.0.0-all.jar
```

## Default Admin Account

```text
Username: admin
Password: admin123
```

Change the default password before using the application in a real environment.

## Database Configuration

The default database is:

```text
jdbc:sqlite:online_exam.db
```

Configuration is stored in `src/main/resources/app.properties` and can be overridden with environment variables:

```text
ONLINE_EXAM_DB_URL
ONLINE_EXAM_DB_USERNAME
ONLINE_EXAM_DB_PASSWORD
```

The SQLite database file is local to the directory from which the application is started.

## Testing

Run the test suite with:

```powershell
mvn test
```

## Troubleshooting

### Maven is not recognized

Install Maven and add its `bin` directory to the Windows PATH. For example:

```text
D:\apache-maven-3.9.11\bin
```

Then close and reopen PowerShell and verify:

```powershell
mvn -version
```

### Database is locked

Close any other running copy of the application and make sure `online_exam.db` is not open in another database tool. The application configures a SQLite busy timeout to handle short-lived locks.

### The application does not open

This is a desktop Swing application and requires a graphical desktop session. It cannot run as a browser website or as a Render Web Service without being converted to a web application.

## Security Note

This project is intended for learning and demonstration. Before production use:

- Hash passwords instead of storing plain text passwords.
- Add role-based authorization and stronger session handling.
- Validate and sanitize all user input.
- Store secrets outside source code.
- Use a server-side database for multi-user deployment.

## License

This project is provided for educational use.
