package com.DimatiBart.sigma.client.view;

import javax.swing.*;
import com.DimatiBart.sigma.client.controller.Controller;
import com.DimatiBart.sigma.client.model.ClientModel;

import java.awt.*;

/**
 * Created by Dimati_Bart on 01.12.15.
 */
public class RegisterForm extends JPanel {
    private JButton backButton;
    private JButton registerButton;
    private JTextField inEmail;
    private JPasswordField inPass2;
    private JPasswordField inPass1;
    private JPanel panel;

    public RegisterForm(ClientModel model) {
        //panel.setPreferredSize(new Dimension(500, 700));
        Controller controller = new Controller(this, panel, model);
        backButton.addActionListener(controller);
        registerButton.addActionListener(controller);
        backButton.setActionCommand("backtologin");
        registerButton.setActionCommand("registration");
    }

    public String getEmail() {
        return inEmail.getText();
    }

    public String getPass1() {
        return inPass1.getText();
    }

    public String getPass2() {
        return inPass2.getText();
    }

    public boolean isEmpty() {
        return getEmail().equals("") || getPass1().equals("") || getPass2().equals("");
    }

    public boolean isTheSame() {
        return getPass1().equals(getPass2());
    }

}
