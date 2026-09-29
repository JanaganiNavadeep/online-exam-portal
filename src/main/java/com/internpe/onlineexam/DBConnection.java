package com.internpe.onlineexam;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Locale;
import java.util.Properties;

public final class DBConnection {

    private static final String DEFAULT_URL = "jdbc:sqlite:online_exam.db";
    private static final String DEFAULT_USER = "";
    private static final String DEFAULT_PASSWORD = "";

    private static final String URL = loadProperty("db.url", DEFAULT_URL);
    private static final String USER = loadProperty("db.username", DEFAULT_USER);
    private static final String PASSWORD = loadProperty("db.password", DEFAULT_PASSWORD);
    private static final Object MIGRATION_LOCK = new Object();
    private static volatile boolean additionalQuestionsEnsured;

    private DBConnection() {
    }

    private static String loadProperty(String key, String defaultValue) {
        String envKey = "ONLINE_EXAM_" + key.toUpperCase().replace('.', '_');
        String envValue = System.getenv(envKey);
        if (envValue != null && !envValue.isBlank()) {
            return envValue;
        }

        try (InputStream input = DBConnection.class.getClassLoader().getResourceAsStream("app.properties")) {
            if (input != null) {
                Properties properties = new Properties();
                properties.load(input);
                String propertyValue = properties.getProperty(key);
                if (propertyValue != null && !propertyValue.isBlank()) {
                    return propertyValue;
                }
            }
        } catch (IOException ignored) {
            // Fall back to the default value below.
        }

        return defaultValue;
    }

    static String getBaseUrl(String jdbcUrl) {
        if (jdbcUrl == null || jdbcUrl.isBlank()) {
            return "jdbc:sqlite";
        }
        return jdbcUrl;
    }

    static boolean isDatabaseMissing(SQLException ex) {
        if (ex == null) {
            return false;
        }

        String message = ex.getMessage();
        if (message == null) {
            return false;
        }

        String lowered = message.toLowerCase(Locale.ROOT);
        return lowered.contains("no such table")
                || lowered.contains("unable to open database file")
                || lowered.contains("unknown database");
    }

    private static Connection openConnection() throws SQLException {
        Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
        try (Statement statement = connection.createStatement()) {
            statement.execute("PRAGMA busy_timeout = 5000");
            statement.execute("PRAGMA foreign_keys = ON");
        }
        return connection;
    }

    private static Connection openConnectionWithMigration() throws SQLException {
        Connection connection = openConnection();
        try {
            ensureAdditionalQuestions(connection);
            return connection;
        } catch (SQLException ex) {
            try {
                connection.close();
            } catch (SQLException closeException) {
                ex.addSuppressed(closeException);
            }
            throw ex;
        }
    }

    private static void ensureSchema() throws SQLException {
        try (Connection connection = openConnection(); Statement statement = connection.createStatement()) {
            statement.execute("CREATE TABLE IF NOT EXISTS admins (admin_id INTEGER PRIMARY KEY AUTOINCREMENT, username TEXT NOT NULL UNIQUE, password TEXT NOT NULL);");
            statement.execute("CREATE TABLE IF NOT EXISTS students (student_id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT NOT NULL, email TEXT NOT NULL UNIQUE, password TEXT NOT NULL);");
            statement.execute("CREATE TABLE IF NOT EXISTS exams (exam_id INTEGER PRIMARY KEY AUTOINCREMENT, exam_name TEXT NOT NULL, duration_minutes INTEGER NOT NULL, total_questions INTEGER NOT NULL DEFAULT 0);");
            statement.execute("CREATE TABLE IF NOT EXISTS questions (question_id INTEGER PRIMARY KEY AUTOINCREMENT, exam_id INTEGER NOT NULL, question_text TEXT NOT NULL, option_a TEXT NOT NULL, option_b TEXT NOT NULL, option_c TEXT NOT NULL, option_d TEXT NOT NULL, correct_answer TEXT NOT NULL, FOREIGN KEY (exam_id) REFERENCES exams(exam_id) ON DELETE CASCADE);");
            statement.execute("CREATE TABLE IF NOT EXISTS results (result_id INTEGER PRIMARY KEY AUTOINCREMENT, student_id INTEGER NOT NULL, exam_id INTEGER NOT NULL, score INTEGER NOT NULL, total_marks INTEGER NOT NULL, percentage REAL NOT NULL, submitted_at TEXT DEFAULT CURRENT_TIMESTAMP, FOREIGN KEY (student_id) REFERENCES students(student_id) ON DELETE CASCADE, FOREIGN KEY (exam_id) REFERENCES exams(exam_id) ON DELETE CASCADE);");
            statement.execute("INSERT OR IGNORE INTO admins (username, password) VALUES ('admin', 'admin123');");
            statement.execute("INSERT OR IGNORE INTO exams (exam_name, duration_minutes, total_questions) VALUES ('Java Programming', 15, 15);");
            statement.execute("INSERT OR IGNORE INTO questions (exam_id, question_text, option_a, option_b, option_c, option_d, correct_answer) SELECT exam_id, 'Which keyword is used to inherit a class in Java?', 'implements', 'extends', 'inherits', 'super', 'B' FROM exams WHERE exam_name = 'Java Programming';");
            statement.execute("INSERT OR IGNORE INTO questions (exam_id, question_text, option_a, option_b, option_c, option_d, correct_answer) SELECT exam_id, 'Which method is the entry point of a Java application?', 'start()', 'run()', 'main()', 'init()', 'C' FROM exams WHERE exam_name = 'Java Programming';");
            statement.execute("INSERT OR IGNORE INTO questions (exam_id, question_text, option_a, option_b, option_c, option_d, correct_answer) SELECT exam_id, 'Which collection does not allow duplicate elements?', 'List', 'Set', 'Map', 'ArrayList', 'B' FROM exams WHERE exam_name = 'Java Programming';");
            statement.execute("INSERT OR IGNORE INTO questions (exam_id, question_text, option_a, option_b, option_c, option_d, correct_answer) SELECT exam_id, 'Which keyword prevents a class from being inherited?', 'static', 'const', 'final', 'private', 'C' FROM exams WHERE exam_name = 'Java Programming';");
            statement.execute("INSERT OR IGNORE INTO questions (exam_id, question_text, option_a, option_b, option_c, option_d, correct_answer) SELECT exam_id, 'Which data type stores true or false?', 'boolean', 'bool', 'logical', 'bit', 'A' FROM exams WHERE exam_name = 'Java Programming';");
            statement.execute("INSERT OR IGNORE INTO questions (exam_id, question_text, option_a, option_b, option_c, option_d, correct_answer) SELECT exam_id, 'Which symbol is used to end a Java statement?', ':', '.', ';', ',', 'C' FROM exams WHERE exam_name = 'Java Programming';");
            statement.execute("INSERT OR IGNORE INTO questions (exam_id, question_text, option_a, option_b, option_c, option_d, correct_answer) SELECT exam_id, 'Which concept allows the same method name with different parameters?', 'Inheritance', 'Overloading', 'Encapsulation', 'Abstraction', 'B' FROM exams WHERE exam_name = 'Java Programming';");
            statement.execute("INSERT OR IGNORE INTO questions (exam_id, question_text, option_a, option_b, option_c, option_d, correct_answer) SELECT exam_id, 'Which package contains Scanner?', 'java.io', 'java.net', 'java.util', 'java.sql', 'C' FROM exams WHERE exam_name = 'Java Programming';");
            statement.execute("INSERT OR IGNORE INTO questions (exam_id, question_text, option_a, option_b, option_c, option_d, correct_answer) SELECT exam_id, 'Which keyword creates an object?', 'new', 'create', 'object', 'make', 'A' FROM exams WHERE exam_name = 'Java Programming';");
            statement.execute("INSERT OR IGNORE INTO questions (exam_id, question_text, option_a, option_b, option_c, option_d, correct_answer) SELECT exam_id, 'Which interface is commonly used to run a task in a new thread?', 'Runnable', 'Serializable', 'Cloneable', 'Comparable', 'A' FROM exams WHERE exam_name = 'Java Programming';");
            statement.execute("INSERT INTO questions (exam_id, question_text, option_a, option_b, option_c, option_d, correct_answer) SELECT exam_id, 'Which access modifier makes a member visible only inside its class?', 'public', 'protected', 'private', 'default', 'C' FROM exams WHERE exam_name = 'Java Programming' AND NOT EXISTS (SELECT 1 FROM questions WHERE question_text = 'Which access modifier makes a member visible only inside its class?');");
            statement.execute("INSERT INTO questions (exam_id, question_text, option_a, option_b, option_c, option_d, correct_answer) SELECT exam_id, 'Which collection stores key-value pairs?', 'Set', 'Map', 'Queue', 'List', 'B' FROM exams WHERE exam_name = 'Java Programming' AND NOT EXISTS (SELECT 1 FROM questions WHERE question_text = 'Which collection stores key-value pairs?');");
            statement.execute("INSERT INTO questions (exam_id, question_text, option_a, option_b, option_c, option_d, correct_answer) SELECT exam_id, 'Which keyword is used to handle an exception?', 'throw', 'throws', 'try', 'catch', 'D' FROM exams WHERE exam_name = 'Java Programming' AND NOT EXISTS (SELECT 1 FROM questions WHERE question_text = 'Which keyword is used to handle an exception?');");
            statement.execute("INSERT INTO questions (exam_id, question_text, option_a, option_b, option_c, option_d, correct_answer) SELECT exam_id, 'Which class is the parent of all Java classes?', 'Class', 'Object', 'Parent', 'Main', 'B' FROM exams WHERE exam_name = 'Java Programming' AND NOT EXISTS (SELECT 1 FROM questions WHERE question_text = 'Which class is the parent of all Java classes?');");
            statement.execute("INSERT INTO questions (exam_id, question_text, option_a, option_b, option_c, option_d, correct_answer) SELECT exam_id, 'Which keyword refers to the current object?', 'self', 'current', 'this', 'super', 'C' FROM exams WHERE exam_name = 'Java Programming' AND NOT EXISTS (SELECT 1 FROM questions WHERE question_text = 'Which keyword refers to the current object?');");
            statement.execute("UPDATE exams SET total_questions = (SELECT COUNT(*) FROM questions WHERE questions.exam_id = exams.exam_id) WHERE exam_name = 'Java Programming';");
        }
    }

    private static void ensureAdditionalQuestions(Connection connection) throws SQLException {
        if (additionalQuestionsEnsured) {
            return;
        }

        synchronized (MIGRATION_LOCK) {
            if (additionalQuestionsEnsured) {
                return;
            }

            String[] statements = {
                "INSERT INTO questions (exam_id, question_text, option_a, option_b, option_c, option_d, correct_answer) SELECT exam_id, 'Which access modifier makes a member visible only inside its class?', 'public', 'protected', 'private', 'default', 'C' FROM exams WHERE exam_name = 'Java Programming' AND NOT EXISTS (SELECT 1 FROM questions WHERE question_text = 'Which access modifier makes a member visible only inside its class?')",
                "INSERT INTO questions (exam_id, question_text, option_a, option_b, option_c, option_d, correct_answer) SELECT exam_id, 'Which collection stores key-value pairs?', 'Set', 'Map', 'Queue', 'List', 'B' FROM exams WHERE exam_name = 'Java Programming' AND NOT EXISTS (SELECT 1 FROM questions WHERE question_text = 'Which collection stores key-value pairs?')",
                "INSERT INTO questions (exam_id, question_text, option_a, option_b, option_c, option_d, correct_answer) SELECT exam_id, 'Which keyword is used to handle an exception?', 'throw', 'throws', 'try', 'catch', 'D' FROM exams WHERE exam_name = 'Java Programming' AND NOT EXISTS (SELECT 1 FROM questions WHERE question_text = 'Which keyword is used to handle an exception?')",
                "INSERT INTO questions (exam_id, question_text, option_a, option_b, option_c, option_d, correct_answer) SELECT exam_id, 'Which class is the parent of all Java classes?', 'Class', 'Object', 'Parent', 'Main', 'B' FROM exams WHERE exam_name = 'Java Programming' AND NOT EXISTS (SELECT 1 FROM questions WHERE question_text = 'Which class is the parent of all Java classes?')",
                "INSERT INTO questions (exam_id, question_text, option_a, option_b, option_c, option_d, correct_answer) SELECT exam_id, 'Which keyword refers to the current object?', 'self', 'current', 'this', 'super', 'C' FROM exams WHERE exam_name = 'Java Programming' AND NOT EXISTS (SELECT 1 FROM questions WHERE question_text = 'Which keyword refers to the current object?')",
                "UPDATE exams SET total_questions = (SELECT COUNT(*) FROM questions WHERE questions.exam_id = exams.exam_id) WHERE exam_name = 'Java Programming'"
            };

            try (Statement statement = connection.createStatement()) {
                for (String sql : statements) {
                    statement.execute(sql);
                }
            }
            additionalQuestionsEnsured = true;
        }
    }

    public static Connection getConnection() throws SQLException {
        Path dbPath = Path.of("online_exam.db");
        if (Files.notExists(dbPath) && !URL.startsWith("jdbc:sqlite::memory:")) {
            ensureSchema();
            return openConnectionWithMigration();
        }

        try {
            return openConnectionWithMigration();
        } catch (SQLException ex) {
            if (isDatabaseMissing(ex)) {
                ensureSchema();
                return openConnectionWithMigration();
            }
            throw ex;
        }
    }
}
