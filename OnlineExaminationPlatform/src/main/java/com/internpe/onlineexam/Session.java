package com.internpe.onlineexam;

public final class Session {
    public static int studentId;
    public static String studentName;
    public static String studentEmail;

    private Session() {}

    public static void clear() {
        studentId = 0;
        studentName = null;
        studentEmail = null;
    }
}
