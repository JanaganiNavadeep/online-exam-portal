package com.internpe.onlineexam;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;

public class AdminDashboard extends JFrame {

    private final JComboBox<ExamItem> exams = new JComboBox<>();
    private final DefaultListModel<String> resultModel = new DefaultListModel<>();
    private final JList<String> results = new JList<>(resultModel);
    private final JLabel examCount = new JLabel("0");
    private final JLabel resultCount = new JLabel("0");

    public AdminDashboard() {
        setTitle("Admin Dashboard");
        setSize(980, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(new Color(242, 248, 248));

        JPanel header = new JPanel(new BorderLayout(12, 4));
        header.setBackground(new Color(13, 61, 69));
        header.setBorder(BorderFactory.createEmptyBorder(22, 28, 22, 28));

        JPanel identity = new JPanel();
        identity.setOpaque(false);
        identity.setLayout(new javax.swing.BoxLayout(identity, javax.swing.BoxLayout.Y_AXIS));

        JLabel title = new JLabel("Admin Console");
        title.setFont(new Font("Segoe UI", Font.BOLD, 26));
        title.setForeground(Color.WHITE);

        JLabel subtitle = new JLabel("Manage assessments, questions, and student performance");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        subtitle.setForeground(new Color(190, 226, 222));
        identity.add(title);
        identity.add(javax.swing.Box.createVerticalStrut(5));
        identity.add(subtitle);

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        actions.setOpaque(false);

        JButton addExam = stylePrimaryButton("Add Exam");
        JButton addQuestion = styleSecondaryButton("Add Question");
        JButton refresh = styleSecondaryButton("Refresh");
        JButton logout = styleSecondaryButton("Logout");

        actions.add(addExam);
        actions.add(addQuestion);
        actions.add(refresh);
        actions.add(logout);

        header.add(identity, BorderLayout.WEST);
        header.add(actions, BorderLayout.EAST);

        JPanel overview = new JPanel(new GridLayout(1, 2, 14, 0));
        overview.setBackground(new Color(242, 248, 248));
        overview.setBorder(BorderFactory.createEmptyBorder(16, 18, 2, 18));
        overview.add(createMetricCard("Published Exams", examCount, new Color(15, 139, 141)));
        overview.add(createMetricCard("Submitted Results", resultCount, new Color(249, 115, 22)));

        JPanel headerArea = new JPanel(new BorderLayout());
        headerArea.setBackground(new Color(242, 248, 248));
        headerArea.add(header, BorderLayout.NORTH);
        headerArea.add(overview, BorderLayout.CENTER);

        JPanel content = new JPanel(new BorderLayout(16, 16));
        content.setBorder(BorderFactory.createEmptyBorder(14, 18, 18, 18));
        content.setBackground(new Color(242, 248, 248));

        JPanel examPanel = new JPanel(new BorderLayout(10, 10));
        examPanel.setBackground(new Color(255, 255, 255));
        examPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(190, 220, 217)), "Exam Management"),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        exams.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        exams.setBackground(new Color(248, 252, 252));
        exams.setForeground(new Color(20, 67, 72));
        examPanel.add(exams, BorderLayout.CENTER);

        JButton deleteExam = styleDangerButton("Delete Selected Exam");
        JPanel deletePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        deletePanel.setOpaque(false);
        deletePanel.add(deleteExam);
        examPanel.add(deletePanel, BorderLayout.SOUTH);

        JPanel resultPanel = new JPanel(new BorderLayout(10, 10));
        resultPanel.setBackground(new Color(255, 255, 255));
        resultPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(190, 220, 217)), "All Student Results"),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        results.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        results.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        results.setBackground(new Color(248, 252, 252));
        results.setForeground(new Color(25, 58, 63));
        results.setSelectionBackground(new Color(193, 232, 225));
        results.setSelectionForeground(new Color(13, 61, 69));
        results.setBorder(BorderFactory.createEmptyBorder(6, 8, 6, 8));
        resultPanel.add(new JScrollPane(results), BorderLayout.CENTER);

        JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT, examPanel, resultPanel);
        split.setDividerLocation(180);
        split.setBorder(null);
        split.setBackground(new Color(242, 248, 248));

        content.add(split, BorderLayout.CENTER);
        root.add(headerArea, BorderLayout.NORTH);
        root.add(content, BorderLayout.CENTER);
        add(root);

        addExam.addActionListener(e -> addExam());
        addQuestion.addActionListener(e -> addQuestion());
        deleteExam.addActionListener(e -> deleteExam());
        refresh.addActionListener(e -> {
            loadExams();
            loadResults();
        });
        logout.addActionListener(e -> {
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
        return button;
    }

    private JButton styleDangerButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(new Color(224, 90, 76));
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createEmptyBorder(10, 16, 10, 16));
        button.setOpaque(true);
        return button;
    }

    private JPanel createMetricCard(String label, JLabel value, Color accent) {
        JPanel card = new JPanel(new BorderLayout(12, 0));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(190, 220, 217)),
                BorderFactory.createEmptyBorder(12, 16, 12, 16)
        ));

        JPanel accentBar = new JPanel();
        accentBar.setBackground(accent);
        accentBar.setPreferredSize(new java.awt.Dimension(6, 42));

        JPanel text = new JPanel();
        text.setOpaque(false);
        text.setLayout(new javax.swing.BoxLayout(text, javax.swing.BoxLayout.Y_AXIS));
        JLabel heading = new JLabel(label);
        heading.setFont(new Font("Segoe UI", Font.BOLD, 12));
        heading.setForeground(new Color(91, 119, 121));
        value.setFont(new Font("Segoe UI", Font.BOLD, 24));
        value.setForeground(new Color(16, 47, 56));
        text.add(heading);
        text.add(javax.swing.Box.createVerticalStrut(2));
        text.add(value);

        card.add(accentBar, BorderLayout.WEST);
        card.add(text, BorderLayout.CENTER);
        return card;
    }

    private void loadExams() {
        exams.removeAllItems();
        String sql = "SELECT exam_id,exam_name,duration_minutes,total_questions FROM exams ORDER BY exam_id";
        try (Connection c = DBConnection.getConnection(); PreparedStatement ps = c.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                exams.addItem(new ExamItem(rs.getInt(1), rs.getString(2), rs.getInt(3)));
            }
            examCount.setText(String.valueOf(exams.getItemCount()));
        } catch (SQLException ex) {
            error(ex);
        }
    }

    private void loadResults() {
        resultModel.clear();
        String sql = "SELECT s.name,e.exam_name,r.score,r.total_marks,r.percentage,r.submitted_at "
                + "FROM results r JOIN students s ON r.student_id=s.student_id "
                + "JOIN exams e ON r.exam_id=e.exam_id ORDER BY r.submitted_at DESC";
        try (Connection c = DBConnection.getConnection(); PreparedStatement ps = c.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                resultModel.addElement(String.format(
                        "%s | %s | %d/%d | %.2f%% | %s",
                        rs.getString(1), rs.getString(2), rs.getInt(3), rs.getInt(4),
                        rs.getDouble(5), rs.getTimestamp(6)));
            }
            resultCount.setText(String.valueOf(resultModel.size()));
        } catch (SQLException ex) {
            error(ex);
        }
    }

    private void addExam() {
        JTextField name = new JTextField();
        JTextField duration = new JTextField("15");
        JPanel p = new JPanel(new GridLayout(0, 2, 5, 5));
        p.add(new JLabel("Exam name:"));
        p.add(name);
        p.add(new JLabel("Duration (minutes):"));
        p.add(duration);

        int r = JOptionPane.showConfirmDialog(this, p, "Add Exam", JOptionPane.OK_CANCEL_OPTION);
        if (r != JOptionPane.OK_OPTION) {
            return;
        }

        try {
            int mins = Integer.parseInt(duration.getText().trim());
            if (name.getText().trim().isEmpty() || mins <= 0) {
                throw new NumberFormatException();
            }
            String sql = "INSERT INTO exams(exam_name,duration_minutes,total_questions) VALUES(?,?,0)";
            try (Connection c = DBConnection.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
                ps.setString(1, name.getText().trim());
                ps.setInt(2, mins);
                ps.executeUpdate();
            }
            loadExams();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Enter a valid positive duration.");
        } catch (SQLException ex) {
            error(ex);
        }
    }

    private void addQuestion() {
        ExamItem item = (ExamItem) exams.getSelectedItem();
        if (item == null) {
            JOptionPane.showMessageDialog(this, "Select an exam first.");
            return;
        }

        JTextArea q = new JTextArea(4, 35);
        JTextField a = new JTextField(), b = new JTextField(), c1 = new JTextField(), d = new JTextField();
        JComboBox<String> correct = new JComboBox<>(new String[]{"A", "B", "C", "D"});
        q.setLineWrap(true);
        q.setWrapStyleWord(true);
        q.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        q.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(178, 211, 208)),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        styleQuestionField(a);
        styleQuestionField(b);
        styleQuestionField(c1);
        styleQuestionField(d);

        JPanel p = new JPanel(new GridLayout(0, 2, 8, 8));
        p.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        p.setBackground(new Color(242, 248, 248));
        p.add(new JLabel("Question:"));
        p.add(new JScrollPane(q));
        p.add(new JLabel("Option A:"));
        p.add(a);
        p.add(new JLabel("Option B:"));
        p.add(b);
        p.add(new JLabel("Option C:"));
        p.add(c1);
        p.add(new JLabel("Option D:"));
        p.add(d);
        p.add(new JLabel("Correct answer:"));
        p.add(correct);

        int r = JOptionPane.showConfirmDialog(this, p, "Add Question", JOptionPane.OK_CANCEL_OPTION);
        if (r != JOptionPane.OK_OPTION) {
            return;
        }

        if (q.getText().trim().isEmpty() || a.getText().trim().isEmpty()
                || b.getText().trim().isEmpty() || c1.getText().trim().isEmpty()
                || d.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "All question fields are required.");
            return;
        }

        String sql = "INSERT INTO questions(exam_id,question_text,option_a,option_b,option_c,option_d,correct_answer) VALUES(?,?,?,?,?,?,?)";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, item.id);
            ps.setString(2, q.getText().trim());
            ps.setString(3, a.getText().trim());
            ps.setString(4, b.getText().trim());
            ps.setString(5, c1.getText().trim());
            ps.setString(6, d.getText().trim());
            ps.setString(7, (String) correct.getSelectedItem());
            ps.executeUpdate();

            try (PreparedStatement up = conn.prepareStatement(
                    "UPDATE exams SET total_questions=(SELECT COUNT(*) FROM questions WHERE exam_id=?) WHERE exam_id=?")) {
                up.setInt(1, item.id);
                up.setInt(2, item.id);
                up.executeUpdate();
            }
            loadExams();
            JOptionPane.showMessageDialog(this, "Question added.");
        } catch (SQLException ex) {
            error(ex);
        }
    }

    private void styleQuestionField(JTextField field) {
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(178, 211, 208)),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
    }

    private void deleteExam() {
        ExamItem item = (ExamItem) exams.getSelectedItem();
        if (item == null) {
            return;
        }
        int r = JOptionPane.showConfirmDialog(this,
                "Delete exam and its questions/results?", "Confirm",
                JOptionPane.YES_NO_OPTION);
        if (r != JOptionPane.YES_OPTION) {
            return;
        }
        try (Connection c = DBConnection.getConnection(); PreparedStatement ps = c.prepareStatement("DELETE FROM exams WHERE exam_id=?")) {
            ps.setInt(1, item.id);
            ps.executeUpdate();
            loadExams();
            loadResults();
        } catch (SQLException ex) {
            error(ex);
        }
    }

    private void error(SQLException ex) {
        JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
    }

    public static class ExamItem {

        final int id, duration;
        final String name;

        ExamItem(int id, String name, int duration) {
            this.id = id;
            this.name = name;
            this.duration = duration;
        }

        @Override
        public String toString() {
            return name + " | " + duration + " minutes";
        }
    }
}
