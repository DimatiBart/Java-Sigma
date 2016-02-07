package com.DimatiBart.sigma.client.view;

import com.DimatiBart.sigma.client.controller.Controller;
import com.DimatiBart.sigma.client.model.ClientModel;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Dimati_Bart on 02.12.15.
 */
public class UserMainForm extends JPanel {
    private JButton addButton;
    private JButton showEditButton; // button for show/edit
    private JButton deleteButton;
    private JButton adminButton;
    private JButton exitButton;
    private JPanel panel;
    private JButton showCharts;

    public UserMainForm(ClientModel model) {
        Controller controller = new Controller(this, panel, model);
        if (!model.getType())
            adminButton.setVisible(false);
        else {
            showCharts.setVisible(false);
            addButton.setVisible(false);
            showEditButton.setVisible(false);
        }
        addButton.addActionListener(controller);
        showEditButton.addActionListener(controller);
        adminButton.addActionListener(controller);
        exitButton.addActionListener(controller);
        showCharts.addActionListener(controller);
        addButton.setActionCommand("addNewData");
        showEditButton.setActionCommand("showEditStart");
        adminButton.setActionCommand("goToAdmin");
        exitButton.setActionCommand("backtologin");
        showCharts.setActionCommand("showCharts");

    }

}
