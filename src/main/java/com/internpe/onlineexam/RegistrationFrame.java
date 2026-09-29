package com.internpe.onlineexam;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

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

public class RegistrationFrame extends JFrame {

    private final JTextField name = new JTextField();
    private final JTextField email = new JTextField();
    private final JPasswordField password = new JPasswordField();
    private final JPasswordField confirm = new JPasswordField();

    public RegistrationFrame() {
        setTitle("Student Registration");
        setSize(700, 560);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        JPanel background = new JPanel(new BorderLayout());
        background.setBackground(new Color(10, 27, 36));
        background.setBorder(BorderFactory.createEmptyBorder(14, 14, 14, 14));

        JPanel leftPanel = new JPanel();
        leftPanel.setBackground(new Color(13, 61, 69));
        leftPanel.setPreferredSize(new Dimension(255, 0));
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBorder(BorderFactory.createEmptyBorder(55, 28, 55, 28));

        JPanel badge = new JPanel();
        badge.setBackground(new Color(249, 115, 22));
        badge.setMaximumSize(new Dimension(120, 42));
        badge.setAlignmentX(Component.LEFT_ALIGNMENT);
        badge.setBorder(BorderFactory.createEmptyBorder(8, 14, 8, 14));

        JLabel portal = new JLabel("E-EXAM");
        portal.setForeground(Color.WHITE);
        portal.setFont(new Font("Segoe UI", Font.BOLD, 22));
        badge.add(portal);

        JLabel text = new JLabel("Create your student account");
        text.setForeground(new Color(229, 246, 243));
        text.setFont(new Font("Segoe UI", Font.BOLD, 20));
        text.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel desc = new JLabel("<html>Join the portal to schedule assessments, view results, and track your performance.</html>");
        desc.setForeground(new Color(202, 231, 228));
        desc.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        desc.setAlignmentX(Component.LEFT_ALIGNMENT);
        desc.setMaximumSize(new Dimension(180, 120));

        JPanel miniCard = new JPanel();
        miniCard.setBackground(new Color(20, 84, 91));
        miniCard.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        miniCard.setLayout(new BoxLayout(miniCard, BoxLayout.Y_AXIS));
        miniCard.setAlignmentX(Component.LEFT_ALIGNMENT);
        miniCard.setMaximumSize(new Dimension(180, 90));

        JLabel m1 = new JLabel("Live Assessments");
        m1.setForeground(Color.WHITE);
        m1.setFont(new Font("Segoe UI", Font.BOLD, 13));
        JLabel m2 = new JLabel("Ready for your next exam");
        m2.setForeground(new Color(203, 232, 229));
        m2.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        miniCard.add(m1);
        miniCard.add(Box.createVerticalStrut(6));
        miniCard.add(m2);

        leftPanel.add(badge);
        leftPanel.add(Box.createVerticalStrut(18));
        leftPanel.add(text);
        leftPanel.add(Box.createVerticalStrut(20));
        leftPanel.add(desc);
        leftPanel.add(Box.createVerticalStrut(24));
        leftPanel.add(miniCard);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(new Color(242, 248, 248));
        GridBagConstraints gc = new GridBagConstraints();
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.insets = new Insets(10, 10, 10, 10);

        JPanel card = new JPanel(new GridBagLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(204, 226, 224), 1),
                BorderFactory.createEmptyBorder(25, 25, 25, 25)
        ));
        card.setPreferredSize(new Dimension(380, 430));

        JLabel header = new JLabel("Register Student");
        header.setFont(new Font("Segoe UI", Font.BOLD, 28));
        header.setForeground(new Color(16, 47, 56));
        gc.gridx = 0;
        gc.gridy = 0;
        gc.gridwidth = 2;
        card.add(header, gc);

        addField(card, gc, 1, "Full Name", name);
        addField(card, gc, 2, "Email", email);
        addField(card, gc, 3, "Password", password);
        addField(card, gc, 4, "Confirm Password", confirm);

        JButton register = new JButton("Create Account");
        register.setBackground(new Color(15, 139, 141));
        register.setForeground(Color.WHITE);
        register.setFocusPainted(false);
        register.setFont(new Font("Segoe UI", Font.BOLD, 14));
        register.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        gc.gridx = 0;
        gc.gridy = 5;
        gc.gridwidth = 1;
        gc.weightx = 0.5;
        card.add(register, gc);

        JButton back = new JButton("Back to Login");
        back.setBackground(new Color(226, 243, 241));
        back.setForeground(new Color(20, 67, 72));
        back.setFocusPainted(false);
        back.setFont(new Font("Segoe UI", Font.BOLD, 14));
        back.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        gc.gridx = 1;
        card.add(back, gc);

        formPanel.add(card);
        background.add(leftPanel, BorderLayout.WEST);
        background.add(formPanel, BorderLayout.CENTER);
        add(background);

        register.addActionListener(e -> register());
        back.addActionListener(e -> {
            new LoginFrame().setVisible(true);
            dispose();
        });
    }

    private void addField(JPanel panel, GridBagConstraints gc, int row, String labelText, JTextField field) {
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Segoe UI", Font.BOLD, 12));
        label.setForeground(new Color(45, 78, 82));
        gc.gridx = 0;
        gc.gridy = row;
        gc.gridwidth = 1;
        gc.weightx = 0.2;
        panel.add(label, gc);

        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setPreferredSize(new Dimension(250, 40));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(178, 211, 208)),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        gc.gridx = 1;
        gc.weightx = 0.8;
        panel.add(field, gc);
    }

    private void register() {
        String n = name.getText().trim();
        String em = email.getText().trim();
        String pw = new String(password.getPassword());
        String cpw = new String(confirm.getPassword());

        if (n.isEmpty() || em.isEmpty() || pw.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required.");
            return;
        }
        if (!pw.equals(cpw)) {
            JOptionPane.showMessageDialog(this, "Passwords do not match.");
            return;
        }

        String sql = "INSERT INTO students(name,email,password) VALUES(?,?,?)";
        try (Connection c = DBConnection.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, n);
            ps.setString(2, em);
            ps.setString(3, pw);
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Registration successful.");
            new LoginFrame().setVisible(true);
            dispose();
        } catch (SQLIntegrityConstraintViolationException ex) {
            JOptionPane.showMessageDialog(this, "Email already registered.");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage());
        }
    }
}
