package com.internpe.onlineexam;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class LoginFrame extends JFrame {

    private final JTextField emailField = new JTextField();
    private final JPasswordField passwordField = new JPasswordField();

    public LoginFrame() {
        setTitle("Online Examination Portal");
        setSize(980, 620);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        JPanel background = new JPanel(new BorderLayout());
        background.setBackground(new Color(10, 27, 36));
        background.setBorder(BorderFactory.createEmptyBorder(14, 14, 14, 14));

        JPanel leftPanel = createLeftPanel();
        JPanel rightPanel = createRightPanel();

        background.add(leftPanel, BorderLayout.WEST);
        background.add(rightPanel, BorderLayout.CENTER);
        add(background);

    }

    private JPanel createLeftPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(13, 61, 69));
        panel.setBorder(BorderFactory.createEmptyBorder(40, 32, 40, 32));
        panel.setPreferredSize(new Dimension(430, 0));

        JPanel badge = new JPanel();
        badge.setBackground(new Color(249, 115, 22));
        badge.setBorder(BorderFactory.createEmptyBorder(10, 16, 10, 16));
        badge.setMaximumSize(new Dimension(120, 44));
        badge.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel badgeLabel = new JLabel("E-EXAM");
        badgeLabel.setForeground(Color.WHITE);
        badgeLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        badge.add(badgeLabel);

        JLabel title = new JLabel("Professional Learning Platform");
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(Color.WHITE);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel subtitle = new JLabel("Assess talent, track performance, and streamline online evaluation.");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        subtitle.setForeground(new Color(213, 239, 236));
        subtitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        subtitle.setMaximumSize(new Dimension(320, 80));

        JPanel statPanel = new JPanel(new GridLayout(1, 3, 12, 12));
        statPanel.setOpaque(false);
        statPanel.setMaximumSize(new Dimension(340, 90));
        statPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        statPanel.add(createStatCard("2500+", "Candidates"));
        statPanel.add(createStatCard("120", "Exams"));
        statPanel.add(createStatCard("96%", "Success"));

        JPanel infoPanel = new JPanel();
        infoPanel.setOpaque(false);
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        infoPanel.setBorder(BorderFactory.createEmptyBorder(25, 0, 0, 0));

        addInfoRow(infoPanel, "Secure authentication", "Protected student and admin access");
        addInfoRow(infoPanel, "Smart assessments", "Create, manage, and monitor exams");
        addInfoRow(infoPanel, "Instant analytics", "Track results and performance trends");

        panel.add(badge);
        panel.add(Box.createVerticalStrut(22));
        panel.add(title);
        panel.add(Box.createVerticalStrut(16));
        panel.add(subtitle);
        panel.add(Box.createVerticalStrut(24));
        panel.add(statPanel);
        panel.add(infoPanel);
        return panel;
    }

    private JPanel createStatCard(String value, String label) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(new Color(20, 84, 91));
        card.setBorder(BorderFactory.createEmptyBorder(14, 12, 14, 12));
        card.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel v = new JLabel(value);
        v.setFont(new Font("Segoe UI", Font.BOLD, 20));
        v.setForeground(Color.WHITE);

        JLabel l = new JLabel(label);
        l.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        l.setForeground(new Color(203, 232, 229));

        card.add(v);
        card.add(l);
        return card;
    }

    private void addInfoRow(Container container, String heading, String detail) {
        JLabel h = new JLabel("• " + heading);
        h.setFont(new Font("Segoe UI", Font.BOLD, 15));
        h.setForeground(Color.WHITE);

        JLabel d = new JLabel(detail);
        d.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        d.setForeground(new Color(200, 231, 228));

        container.add(h);
        container.add(Box.createVerticalStrut(6));
        container.add(d);
        container.add(Box.createVerticalStrut(16));
    }

    private JPanel createRightPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(242, 248, 248));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JPanel card = new JPanel(new GridBagLayout());
        card.setBackground(new Color(255, 255, 255));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(204, 226, 224), 1),
                BorderFactory.createEmptyBorder(26, 28, 26, 28)
        ));
        card.setPreferredSize(new Dimension(440, 420));
        card.setOpaque(true);

        JLabel title = new JLabel("Welcome Back");
        title.setFont(new Font("Segoe UI", Font.BOLD, 30));
        title.setForeground(new Color(16, 47, 56));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.WEST;
        card.add(title, gbc);

        JLabel subtitle = new JLabel("Sign in to continue your exam journey");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitle.setForeground(new Color(91, 119, 121));
        gbc.gridy = 1;
        card.add(subtitle, gbc);

        JLabel emailLabel = new JLabel("Email / Username");
        emailLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        emailLabel.setForeground(new Color(45, 78, 82));
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        card.add(emailLabel, gbc);

        emailField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        emailField.setPreferredSize(new Dimension(0, 42));
        emailField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(178, 211, 208)),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        card.add(emailField, gbc);

        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        passwordLabel.setForeground(new Color(45, 78, 82));
        gbc.gridy = 4;
        card.add(passwordLabel, gbc);

        passwordField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        passwordField.setPreferredSize(new Dimension(0, 42));
        passwordField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(178, 211, 208)),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        gbc.gridy = 5;
        card.add(passwordField, gbc);

        JButton loginBtn = createLoginButton();
        loginBtn.setPreferredSize(new Dimension(0, 46));
        gbc.gridy = 6;
        card.add(loginBtn, gbc);

        JButton registerBtn = createSecondaryButton("Create Account");
        gbc.gridy = 7;
        card.add(registerBtn, gbc);

        JButton adminBtn = createSecondaryButton("Admin Portal");
        gbc.gridy = 8;
        card.add(adminBtn, gbc);

        panel.add(card);
        getRootPane().setDefaultButton(loginBtn);
        registerBtn.addActionListener(e -> {
            new RegistrationFrame().setVisible(true);
            dispose();
        });
        adminBtn.addActionListener(e -> adminLogin());
        loginBtn.addActionListener(e -> studentLogin());
        return panel;
    }

    private JButton createLoginButton() {
        JButton button = new JButton("Login");
        button.setFont(new Font("Segoe UI", Font.BOLD, 15));
        button.setBackground(new Color(15, 139, 141));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createEmptyBorder(12, 15, 12, 15));
        button.setOpaque(true);
        button.setBorderPainted(false);
        return button;
    }

    private JButton createSecondaryButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setBackground(new Color(226, 243, 241));
        button.setForeground(new Color(20, 67, 72));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createEmptyBorder(11, 15, 11, 15));
        button.setBorderPainted(false);
        return button;
    }

    private void studentLogin() {
        String email = emailField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Enter email and password.");
            return;
        }

        String sql = "SELECT student_id,name,email FROM students WHERE email=? AND password=?";
        try (Connection c = DBConnection.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, email);
            ps.setString(2, password);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Session.studentId = rs.getInt("student_id");
                    Session.studentName = rs.getString("name");
                    Session.studentEmail = rs.getString("email");
                    new StudentDashboard().setVisible(true);
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Invalid student credentials.");
                }
            }
        } catch (SQLException ex) {
            showDbError(ex);
        }
    }

    private void adminLogin() {
        String username = JOptionPane.showInputDialog(this, "Admin username:");
        if (username == null) {
            return;
        }
        JPasswordField pf = new JPasswordField();
        int result = JOptionPane.showConfirmDialog(this, pf, "Admin password",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result != JOptionPane.OK_OPTION) {
            return;
        }

        String sql = "SELECT admin_id FROM admins WHERE username=? AND password=?";
        try (Connection c = DBConnection.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, username.trim());
            ps.setString(2, new String(pf.getPassword()));
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    new AdminDashboard().setVisible(true);
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Invalid admin credentials.");
                }
            }
        } catch (SQLException ex) {
            showDbError(ex);
        }
    }

    private void showDbError(SQLException ex) {
        JOptionPane.showMessageDialog(this,
                "Database error: " + ex.getMessage(),
                "Database Error", JOptionPane.ERROR_MESSAGE);
    }
}
