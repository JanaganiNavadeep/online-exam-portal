package com.internpe.onlineexam;

public class Question {
    private final int id;
    private final String text;
    private final String a;
    private final String b;
    private final String c;
    private final String d;
    private final char correct;

    public Question(int id, String text, String a, String b, String c, String d, char correct) {
        this.id = id;
        this.text = text;
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
        this.correct = Character.toUpperCase(correct);
    }

    public int getId() { return id; }
    public String getText() { return text; }
    public String getA() { return a; }
    public String getB() { return b; }
    public String getC() { return c; }
    public String getD() { return d; }
    public char getCorrect() { return correct; }
}
