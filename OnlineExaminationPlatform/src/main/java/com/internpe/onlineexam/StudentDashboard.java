package com.internpe.onlineexam;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.Font;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;

public class StudentDashboard extends JFrame {

    private final JComboBox<ExamItem> exams = new JComboBox<>();
    private final DefaultListModel<String> resultModel = new DefaultListModel<>();
    private final JList<String> results = new JList<>(resultModel);

    public StudentDashboard() {
        setTitle("Student Dashboard - " + Session.studentName);
        setSize(820, 620);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(new Color(242, 248, 248));

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(13, 61, 69));
        header.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

        JLabel welcome = new JLabel("Welcome, " + Session.studentName);
        welcome.setFont(new Font("Segoe UI", Font.BOLD, 24));
        welcome.setForeground(Color.WHITE);

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        actions.setOpaque(false);

        JButton start = stylePrimaryButton("Start Exam");
        JButton refresh = styleSecondaryButton("Refresh");
        JButton logout = styleDangerButton("Logout");

        actions.add(start);
        actions.add(refresh);
        actions.add(logout);
        header.add(welcome, BorderLayout.WEST);
        header.add(actions, BorderLayout.EAST);

        JPanel content = new JPanel(new BorderLayout(16, 16));
        content.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        content.setBackground(new Color(242, 248, 248));

        JPanel examPanel = new JPanel(new BorderLayout(10, 10));
        examPanel.setBackground(new Color(255, 255, 255));
        examPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(190, 220, 217)), "Available Exams"),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        exams.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        exams.setBackground(new Color(248, 252, 252));
        exams.setForeground(new Color(20, 67, 72));
        examPanel.add(exams, BorderLayout.CENTER);

        JPanel resultPanel = new JPanel(new BorderLayout(10, 10));
        resultPanel.setBackground(new Color(255, 255, 255));
        resultPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(190, 220, 217)), "My Results"),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        results.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        results.setBackground(new Color(248, 252, 252));
        results.setForeground(new Color(25, 58, 63));
        results.setSelectionBackground(new Color(193, 232, 225));
        results.setSelectionForeground(new Color(13, 61, 69));
        results.setBorder(BorderFactory.createEmptyBorder(6, 8, 6, 8));
        resultPanel.add(new JScrollPane(results), BorderLayout.CENTER);

        JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT, examPanel, resultPanel);
        split.setDividerLocation(160);
        split.setBorder(null);
        split.setBackground(new Color(242, 248, 248));

        content.add(split, BorderLayout.CENTER);
        root.add(header, BorderLayout.NORTH);
        root.add(content, BorderLayout.CENTER);
        add(root);

        start.addActionListener(e -> startExam());
        refresh.addActionListener(e -> {
            loadExams();
            loadResults();
        });
        logout.addActionListener(e -> {
            Session.clear();
            new LoginFrame().setVisible(true);
            dispose();
        });

        loadExams();
        loadResults();
    }

    private JButton stylePrimaryButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(new Color(15, 139, 141));
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createEmptyBorder(10, 16, 10, 16));
        button.setOpaque(true);
        button.setBorderPainted(false);
        return button;
    }

    private JButton styleSecondaryButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(new Color(226, 243, 241));
        button.setForeground(new Color(20, 67, 72));
        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createEmptyBorder(10, 14, 10, 14));
        button.setOpaque(true);
        button.setBorderPainted(false);
        return button;
    }

    private JButton styleDangerButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(new Color(224, 90, 76));
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createEmptyBorder(10, 14, 10, 14));
        button.setOpaque(true);
        button.setBorderPainted(false);
        return button;
    }

    private void loadExams() {
        exams.removeAllItems();
        String sql = "SELECT exam_id,exam_name,duration_minutes,total_questions FROM exams ORDER BY exam_id";
        try (Connection c = DBConnection.getConnection(); PreparedStatement ps = c.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                exams.addItem(new ExamItem(
                        rs.getInt(1), rs.getString(2),
                        rs.getInt(3), rs.getInt(4)));
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage());
        }
    }

    private void loadResults() {
        resultModel.clear();
        String sql = "SELECT e.exam_name,r.score,r.total_marks,r.percentage,r.submitted_at "
                + "FROM results r JOIN exams e ON r.exam_id=e.exam_id "
                + "WHERE r.student_id=? ORDER BY r.submitted_at DESC";
        try (Connection c = DBConnection.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, Session.studentId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    resultModel.addElement(String.format(
                            "%s | Score: %d/%d | %.2f%% | %s",
                            rs.getString(1), rs.getInt(2), rs.getInt(3),
                            rs.getDouble(4), rs.getTimestamp(5)));
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage());
        }
    }

    private void startExam() {
        ExamItem item = (ExamItem) exams.getSelectedItem();
        if (item == null) {
            JOptionPane.showMessageDialog(this, "Select an exam.");
            return;
        }

        List<Question> qs = new ArrayList<>();
        String sql = "SELECT question_id,question_text,option_a,option_b,option_c,option_d,correct_answer "
                + "FROM questions WHERE exam_id=? ORDER BY question_id";
        try (Connection c = DBConnection.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, item.id);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    qs.add(new Question(
                            rs.getInt(1), rs.getString(2), rs.getString(3),
                            rs.getString(4), rs.getString(5), rs.getString(6),
                            rs.getString(7).charAt(0)));
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage());
            return;
        }

        if (qs.isEmpty()) {
            JOptionPane.showMessageDialog(this, "This exam has no questions.");
            return;
        }

        new ExamFrame(item, qs).setVisible(true);
        setVisible(false);
    }

    public static class ExamItem {

        final int id, duration, count;
        final String name;

        ExamItem(int id, String name, int duration, int count) {
            this.id = id;
            this.name = name;
            this.duration = duration;
            this.count = count;
        }

        @Override
        public String toString() {
            return name + " | " + count + " questions | " + duration + " minutes";
        }
    }
}
