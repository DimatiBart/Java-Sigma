package com.DimatiBart.sigma.client.view;

import javax.swing.*;
import com.DimatiBart.sigma.client.controller.Controller;
import com.DimatiBart.sigma.client.model.ClientModel;

import java.awt.*;

/**
 * Created by Dimati_Bart on 01.12.15.
 */
public class LoginForm extends JPanel {
    private JButton loginButton;
    private JPasswordField inPass;
    private JTextField inEmail;
    private JButton registerButton;

    public String getInPass() {
        return inPass.getText();
    }

    public String getInEmail() {
        return inEmail.getText();
    }

    public boolean isEmpty() {
        return getInEmail().equals("") || getInPass().equals("");
    }

    public JPanel panel;

    public LoginForm(ClientModel model) {
        Controller controller = new Controller(this, panel, model);
        loginButton.addActionListener(controller);
        registerButton.addActionListener(controller);
        loginButton.setActionCommand("login");
        registerButton.setActionCommand("login-register");
    }

}
