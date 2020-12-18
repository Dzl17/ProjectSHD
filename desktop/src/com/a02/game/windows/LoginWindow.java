package com.a02.game.windows;

import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class LoginWindow extends JFrame{
    public LoginWindow() {
        //1.- Ajustes de ventana
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());   //UI Look&Feel más moderno y limpio
        } catch (Exception e) {
            e.printStackTrace();
        }

        setTitle("Login");
        setSize(340,210);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setVisible(true);
        setResizable(false);
        setIconImage(Toolkit.getDefaultToolkit().createImage("core/assets/boredlion.png"));
        this.setLocationRelativeTo(null);
        setLayout(new GridLayout(5,1));

        //2.- Creación de elementos
        JPanel userLabelPanel = new JPanel(); //Panel para label de usuario
        JPanel userTFPanel = new JPanel(); //Panel para TextField de usuario
        JPanel pwdLabelPanel = new JPanel();
        JPanel pwdTFPanel = new JPanel(); //Panel para TextField de password
        JPanel buttonsPanel = new JPanel(); //Panel para los botones

        userLabelPanel.setLayout(new FlowLayout());
        userTFPanel.setLayout(new BorderLayout());
        userTFPanel.setBorder(BorderFactory.createEmptyBorder(1,20,1,20));
        pwdLabelPanel.setLayout(new FlowLayout());
        pwdTFPanel.setLayout(new BorderLayout());
        pwdTFPanel.setBorder(BorderFactory.createEmptyBorder(1,20,1,20));
        buttonsPanel.setLayout(new FlowLayout());

        JLabel userLabel = new JLabel("User:");
        JLabel pwdLabel = new JLabel("Password:");
        JTextField userJTF = new JTextField();
        JPasswordField pwdJTF = new JPasswordField();
        JButton cancelButton = new JButton("Cancel");
        JButton loginButton = new JButton("Login");
        JButton registerButton = new JButton("Register");

        //3.- Interacción
        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //checkUser()
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        new SettingsWindow();
                    }
                });
                dispose();
            }
        });

        registerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        new RegisterWindow();
                    }
                });
            }
        });

        //4.- Introducción de componentes
        userLabelPanel.add(userLabel);
        userTFPanel.add(userJTF);
        pwdLabelPanel.add(pwdLabel);
        pwdTFPanel.add(pwdJTF);
        buttonsPanel.add(cancelButton);
        buttonsPanel.add(loginButton);
        buttonsPanel.add(registerButton);

        add(userLabelPanel);
        add(userTFPanel);
        add(pwdLabelPanel);
        add(pwdTFPanel);
        add(buttonsPanel);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new LoginWindow();
            }
        });
    }
}
