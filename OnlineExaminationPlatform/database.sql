CREATE TABLE IF NOT EXISTS admins (
    admin_id INTEGER PRIMARY KEY AUTOINCREMENT,
    username TEXT NOT NULL UNIQUE,
    password TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS students (
    student_id INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT NOT NULL,
    email TEXT NOT NULL UNIQUE,
    password TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS exams (
    exam_id INTEGER PRIMARY KEY AUTOINCREMENT,
    exam_name TEXT NOT NULL,
    duration_minutes INTEGER NOT NULL,
    total_questions INTEGER NOT NULL DEFAULT 0
);

CREATE TABLE IF NOT EXISTS questions (
    question_id INTEGER PRIMARY KEY AUTOINCREMENT,
    exam_id INTEGER NOT NULL,
    question_text TEXT NOT NULL,
    option_a TEXT NOT NULL,
    option_b TEXT NOT NULL,
    option_c TEXT NOT NULL,
    option_d TEXT NOT NULL,
    correct_answer TEXT NOT NULL,
    FOREIGN KEY (exam_id) REFERENCES exams(exam_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS results (
    result_id INTEGER PRIMARY KEY AUTOINCREMENT,
    student_id INTEGER NOT NULL,
    exam_id INTEGER NOT NULL,
    score INTEGER NOT NULL,
    total_marks INTEGER NOT NULL,
    percentage REAL NOT NULL,
    submitted_at TEXT DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (student_id) REFERENCES students(student_id) ON DELETE CASCADE,
    FOREIGN KEY (exam_id) REFERENCES exams(exam_id) ON DELETE CASCADE
);

INSERT OR IGNORE INTO admins (username, password) VALUES ('admin', 'admin123');

INSERT OR IGNORE INTO exams (exam_name, duration_minutes, total_questions)
VALUES ('Java Programming', 15, 15);

INSERT OR IGNORE INTO questions (exam_id, question_text, option_a, option_b, option_c, option_d, correct_answer)
SELECT exam_id, 'Which keyword is used to inherit a class in Java?', 'implements', 'extends', 'inherits', 'super', 'B'
FROM exams WHERE exam_name = 'Java Programming';

INSERT OR IGNORE INTO questions (exam_id, question_text, option_a, option_b, option_c, option_d, correct_answer)
SELECT exam_id, 'Which method is the entry point of a Java application?', 'start()', 'run()', 'main()', 'init()', 'C'
FROM exams WHERE exam_name = 'Java Programming';

INSERT OR IGNORE INTO questions (exam_id, question_text, option_a, option_b, option_c, option_d, correct_answer)
SELECT exam_id, 'Which collection does not allow duplicate elements?', 'List', 'Set', 'Map', 'ArrayList', 'B'
FROM exams WHERE exam_name = 'Java Programming';

INSERT OR IGNORE INTO questions (exam_id, question_text, option_a, option_b, option_c, option_d, correct_answer)
SELECT exam_id, 'Which keyword prevents a class from being inherited?', 'static', 'const', 'final', 'private', 'C'
FROM exams WHERE exam_name = 'Java Programming';

INSERT OR IGNORE INTO questions (exam_id, question_text, option_a, option_b, option_c, option_d, correct_answer)
SELECT exam_id, 'Which data type stores true or false?', 'boolean', 'bool', 'logical', 'bit', 'A'
FROM exams WHERE exam_name = 'Java Programming';

INSERT OR IGNORE INTO questions (exam_id, question_text, option_a, option_b, option_c, option_d, correct_answer)
SELECT exam_id, 'Which symbol is used to end a Java statement?', ':', '.', ';', ',', 'C'
FROM exams WHERE exam_name = 'Java Programming';

INSERT OR IGNORE INTO questions (exam_id, question_text, option_a, option_b, option_c, option_d, correct_answer)
SELECT exam_id, 'Which concept allows the same method name with different parameters?', 'Inheritance', 'Overloading', 'Encapsulation', 'Abstraction', 'B'
FROM exams WHERE exam_name = 'Java Programming';

INSERT OR IGNORE INTO questions (exam_id, question_text, option_a, option_b, option_c, option_d, correct_answer)
SELECT exam_id, 'Which package contains Scanner?', 'java.io', 'java.net', 'java.util', 'java.sql', 'C'
FROM exams WHERE exam_name = 'Java Programming';

INSERT OR IGNORE INTO questions (exam_id, question_text, option_a, option_b, option_c, option_d, correct_answer)
SELECT exam_id, 'Which keyword creates an object?', 'new', 'create', 'object', 'make', 'A'
FROM exams WHERE exam_name = 'Java Programming';

INSERT OR IGNORE INTO questions (exam_id, question_text, option_a, option_b, option_c, option_d, correct_answer)
SELECT exam_id, 'Which interface is commonly used to run a task in a new thread?', 'Runnable', 'Serializable', 'Cloneable', 'Comparable', 'A'
FROM exams WHERE exam_name = 'Java Programming';

INSERT OR IGNORE INTO questions (exam_id, question_text, option_a, option_b, option_c, option_d, correct_answer)
SELECT exam_id, 'Which access modifier makes a member visible only inside its class?', 'public', 'protected', 'private', 'default', 'C'
FROM exams WHERE exam_name = 'Java Programming';

INSERT OR IGNORE INTO questions (exam_id, question_text, option_a, option_b, option_c, option_d, correct_answer)
SELECT exam_id, 'Which collection stores key-value pairs?', 'Set', 'Map', 'Queue', 'List', 'B'
FROM exams WHERE exam_name = 'Java Programming';

INSERT OR IGNORE INTO questions (exam_id, question_text, option_a, option_b, option_c, option_d, correct_answer)
SELECT exam_id, 'Which keyword is used to handle an exception?', 'throw', 'throws', 'try', 'catch', 'D'
FROM exams WHERE exam_name = 'Java Programming';

INSERT OR IGNORE INTO questions (exam_id, question_text, option_a, option_b, option_c, option_d, correct_answer)
SELECT exam_id, 'Which class is the parent of all Java classes?', 'Class', 'Object', 'Parent', 'Main', 'B'
FROM exams WHERE exam_name = 'Java Programming';

INSERT OR IGNORE INTO questions (exam_id, question_text, option_a, option_b, option_c, option_d, correct_answer)
SELECT exam_id, 'Which keyword refers to the current object?', 'self', 'current', 'this', 'super', 'C'
FROM exams WHERE exam_name = 'Java Programming';
