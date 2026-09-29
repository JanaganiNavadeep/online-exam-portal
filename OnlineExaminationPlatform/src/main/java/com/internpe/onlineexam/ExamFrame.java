package com.internpe.onlineexam;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.event.WindowEvent;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.Timer;

public class ExamFrame extends JFrame {

    private final StudentDashboard.ExamItem exam;
    private final List<Question> questions;
    private final char[] answers;
    private int index = 0;
    private int remainingSeconds;
    private final JLabel questionLabel = new JLabel();
    private final JLabel timerLabel = new JLabel();
    private final JLabel progressLabel = new JLabel();
    private final JLabel securityLabel = new JLabel("Secure mode active");
    private final JRadioButton a = new JRadioButton();
    private final JRadioButton b = new JRadioButton();
    private final JRadioButton c = new JRadioButton();
    private final JRadioButton d = new JRadioButton();
    private final ButtonGroup group = new ButtonGroup();
    private final JButton previous = new JButton("Previous");
    private final JButton next = new JButton("Next");
    private final JButton submit = new JButton("Submit Exam");
    private final GraphicsDevice graphicsDevice = GraphicsEnvironment
            .getLocalGraphicsEnvironment().getDefaultScreenDevice();
    private int focusLossCount;
    private boolean submitted;
    private boolean closingRequested;
    private Timer timer;

    private static final int MAX_FOCUS_LOSSES = 3;

    public ExamFrame(StudentDashboard.ExamItem exam, List<Question> questions) {
        this.exam = exam;
        this.questions = questions;
        this.answers = new char[questions.size()];
        java.util.Arrays.fill(answers, ' ');
        this.remainingSeconds = exam.duration * 60;

        setTitle("Exam - " + exam.name);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setUndecorated(true);
        setAlwaysOnTop(true);

        getContentPane().setBackground(new Color(242, 248, 248));

        questionLabel.setFont(new Font("SansSerif", Font.BOLD, 17));
        timerLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        progressLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        securityLabel.setFont(new Font("SansSerif", Font.BOLD, 13));
        securityLabel.setForeground(new Color(165, 245, 205));
        timerLabel.setForeground(Color.WHITE);
        progressLabel.setForeground(new Color(13, 104, 110));
        questionLabel.setForeground(new Color(25, 50, 58));
        a.setBackground(Color.WHITE);
        b.setBackground(Color.WHITE);
        c.setBackground(Color.WHITE);
        d.setBackground(Color.WHITE);

        JPanel top = new JPanel(new BorderLayout());
        top.setBackground(new Color(13, 61, 69));
        top.setBorder(BorderFactory.createEmptyBorder(16, 22, 16, 22));
        JLabel examTitle = new JLabel("EXAM  /  " + exam.name);
        examTitle.setFont(new Font("SansSerif", Font.BOLD, 18));
        examTitle.setForeground(Color.WHITE);
        JPanel topRight = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 0));
        topRight.setOpaque(false);
        topRight.add(securityLabel);
        topRight.add(timerLabel);
        top.add(examTitle, BorderLayout.WEST);
        top.add(topRight, BorderLayout.EAST);

        JPanel center = new JPanel();
        center.setBackground(new Color(255, 255, 255));
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
        center.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        center.add(progressLabel);
        center.add(Box.createVerticalStrut(12));
        center.add(questionLabel);
        center.add(Box.createVerticalStrut(18));

        group.add(a);
        group.add(b);
        group.add(c);
        group.add(d);
        center.add(a);
        center.add(b);
        center.add(c);
        center.add(d);

        JPanel bottom = new JPanel(new FlowLayout());
        bottom.setBackground(new Color(226, 243, 241));
        bottom.add(previous);
        bottom.add(next);
        bottom.add(submit);

        add(top, BorderLayout.NORTH);
        add(center, BorderLayout.CENTER);
        add(bottom, BorderLayout.SOUTH);

        previous.addActionListener(e -> {
            saveAnswer();
            if (index > 0) {
                index--;
                showQuestion();
            }
        });
        next.addActionListener(e -> {
            saveAnswer();
            if (index < questions.size() - 1) {
                index++;
                showQuestion();
            }
        });
        submit.addActionListener(e -> finish(false));

        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                closingRequested = true;
                int r = JOptionPane.showConfirmDialog(ExamFrame.this,
                        "Submit and exit the exam?", "Confirm",
                        JOptionPane.YES_NO_OPTION);
                if (r == JOptionPane.YES_OPTION) {
                    finish(false);
                } else {
                    closingRequested = false;
                }
            }

            @Override
            public void windowDeactivated(WindowEvent e) {
                if (!submitted && !closingRequested) {
                    handleFocusLoss();
                }
            }
        });

        enterSecureFullscreen();
        showQuestion();
        startTimer();
    }

    private void enterSecureFullscreen() {
        if (graphicsDevice.isFullScreenSupported()) {
            graphicsDevice.setFullScreenWindow(this);
        } else {
            setExtendedState(JFrame.MAXIMIZED_BOTH);
            setVisible(true);
        }
        requestFocusInWindow();
    }

    private void handleFocusLoss() {
        focusLossCount++;
        securityLabel.setText("Focus warning " + focusLossCount + "/" + MAX_FOCUS_LOSSES);
        securityLabel.setForeground(new Color(255, 205, 135));

        if (focusLossCount >= MAX_FOCUS_LOSSES) {
            JOptionPane.showMessageDialog(this,
                    "The exam was submitted after repeated focus changes.",
                    "Secure Exam", JOptionPane.WARNING_MESSAGE);
            finish(true);
            return;
        }

        JOptionPane.showMessageDialog(this,
                "Please stay on the exam window. This is warning "
                + focusLossCount + " of " + MAX_FOCUS_LOSSES + ".",
                "Secure Exam Warning", JOptionPane.WARNING_MESSAGE);
        toFront();
        requestFocusInWindow();
    }

    private void showQuestion() {
        Question q = questions.get(index);
        progressLabel.setText("Question " + (index + 1) + " of " + questions.size());
        questionLabel.setText("<html>Question " + (index + 1) + " of " + questions.size() + ": " + q.getText() + "</html>");
        a.setText("A. " + q.getA());
        b.setText("B. " + q.getB());
        c.setText("C. " + q.getC());
        d.setText("D. " + q.getD());
        group.clearSelection();

        switch (answers[index]) {
            case 'A' ->
                a.setSelected(true);
            case 'B' ->
                b.setSelected(true);
            case 'C' ->
                c.setSelected(true);
            case 'D' ->
                d.setSelected(true);
        }

        previous.setEnabled(index > 0);
        next.setEnabled(index < questions.size() - 1);
    }

    private void saveAnswer() {
        if (a.isSelected()) {
            answers[index] = 'A';
        } else if (b.isSelected()) {
            answers[index] = 'B';
        } else if (c.isSelected()) {
            answers[index] = 'C';
        } else if (d.isSelected()) {
            answers[index] = 'D';
        }
    }

    private void startTimer() {
        updateTimerLabel();
        timer = new Timer(1000, e -> {
            remainingSeconds--;
            updateTimerLabel();
            if (remainingSeconds <= 0) {
                timer.stop();
                JOptionPane.showMessageDialog(this, "Time is over. Exam will be submitted.");
                finish(true);
            }
        });
        timer.start();
    }

    private void updateTimerLabel() {
        timerLabel.setText(String.format("Time Remaining: %02d:%02d",
                remainingSeconds / 60, remainingSeconds % 60));
    }

    private void finish(boolean timeout) {
        if (submitted) {
            return;
        }
        submitted = true;
        saveAnswer();
        if (timer != null) {
            timer.stop();
        }

        int score = 0;
        for (int i = 0; i < questions.size(); i++) {
            if (answers[i] == questions.get(i).getCorrect()) {
                score++;
            }
        }

        double percentage = (score * 100.0) / questions.size();

        String sql = "INSERT INTO results(student_id,exam_id,score,total_marks,percentage) VALUES(?,?,?,?,?)";
        try (java.sql.Connection conn = DBConnection.getConnection(); java.sql.PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, Session.studentId);
            ps.setInt(2, exam.id);
            ps.setInt(3, score);
            ps.setInt(4, questions.size());
            ps.setDouble(5, percentage);
            ps.executeUpdate();
        } catch (java.sql.SQLException ex) {
            submitted = false;
            JOptionPane.showMessageDialog(this, "Could not save result: " + ex.getMessage());
            return;
        }

        JOptionPane.showMessageDialog(this,
                (timeout ? "Time expired.\n\n" : "")
                + String.format("Exam submitted!\nScore: %d/%d\nPercentage: %.2f%%",
                        score, questions.size(), percentage),
                "Result", JOptionPane.INFORMATION_MESSAGE);

        exitSecureFullscreen();
        new StudentDashboard().setVisible(true);
        dispose();
    }

    private void exitSecureFullscreen() {
        if (graphicsDevice.getFullScreenWindow() == this) {
            graphicsDevice.setFullScreenWindow(null);
        }
        setAlwaysOnTop(false);
    }
}
