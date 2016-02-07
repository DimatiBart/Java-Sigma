package com.DimatiBart.sigma.client.controller;

import com.DimatiBart.sigma.client.model.ClientModel;
import com.DimatiBart.sigma.client.view.LoginForm;
import com.DimatiBart.sigma.client.view.MFrame;
import com.DimatiBart.sigma.client.view.RegisterForm;
import com.DimatiBart.sigma.client.view.*;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Dimati_Bart on 01.12.15.
 */
public class Controller implements ActionListener {
    private ClientModel model;
    private Component frame;
    private Component panel;
    private static MFrame mainFrame;
    @Override
    public void actionPerformed(ActionEvent event) {
        String command = event.getActionCommand();
        switch (command) {
            case "login":
                auth();
                break;
            case "login-register":
                RegisterForm regForm = new RegisterForm(model);
                break;
            case "registration":
                reg();
                break;
            case "backtologin":
               goToLogin();
                break;
            case "goToAdmin":
                goToAdmin();
                break;
            case "goToMain":
                goToMain();
                break;
            case "addNewData":
                beginNew();
                break;
            case "showEditStart":
                editProd();
                break;
            case "editCosts":
                openEditWindow();
                break;
            case "showCharts":
                showCharts();
                break;
        }
    }

    public Controller(Component View, Component Panel, ClientModel model) {
        if (this.mainFrame != null) {
            this.mainFrame.setVisible(false);
        }
        this.mainFrame = new MFrame(Panel);
        this.mainFrame.setVisible(true);
        this.frame = View;
        this.panel = Panel;
        this.model = model;
        this.mainFrame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                if (JOptionPane.showConfirmDialog(mainFrame,
                        "Вы действительно хотите завершить работу?", "ORLY?",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION){
                    model.closeConnection();
                    System.exit(0);
                }
            }
        });
    }
    public void goToMain () {
        UserMainForm MainForm = new UserMainForm(model);
    }

    void goToLogin() {
        LoginForm loginForm = new LoginForm(model);
    }

    void goToAdmin () {
        AdminForm adminForm = new AdminForm(model);
    }

    public void auth () {
        if (((LoginForm)this.frame).isEmpty()) {
            JOptionPane.showMessageDialog(this.mainFrame, "Заполните все поля");
            return;
        }
        if (!model.authentication(((LoginForm)this.frame).getInEmail(), ((LoginForm)this.frame).getInPass())) {
            JOptionPane.showMessageDialog(this.mainFrame, "Ошибка авторизации, проверьте данные");
            return;
        }
        JOptionPane.showMessageDialog(this.mainFrame, "Маладэц");
       goToMain();
    }

    public void reg() {
        if (((RegisterForm)this.frame).isEmpty()) {
            JOptionPane.showMessageDialog(this.mainFrame, "Заполните все поля");
            return;
        }
        if (!((RegisterForm)this.frame).isTheSame()) {
            JOptionPane.showMessageDialog(this.mainFrame, "Ошибка. Пароли не совпадают");
            return;
        }
        if (model.registration(((RegisterForm)this.frame).getEmail(), ((RegisterForm)this.frame).getPass1())) {
            JOptionPane.showMessageDialog(this.mainFrame, "Регистрация прошла успешно");
            goToLogin();
        }
        else {
            JOptionPane.showMessageDialog(this.mainFrame, "Ошибка регистрации.");
            return;
        }
    }
    public MFrame getMainFrame () {
        return this.mainFrame;
    }

    public void beginNew () {
        String newName = JOptionPane.showInputDialog("Введите наименование вашей продукции");
        if (newName == null)
            return;
        if (newName.length() < 1) {
            JOptionPane.showMessageDialog(this.mainFrame, "Название обязательно к заполнению!");
            return;
        }
        model.addProduct(newName);
        goToAdd(newName);
    }

    public void goToAdd (String newName) {
        NewDataForm newDataForm = new NewDataForm(model, newName);
    }

    private void editProd () {
        ProductForm productForm = new ProductForm(model);
    }

    private void openEditWindow() {
        ProductForm form = (ProductForm)this.frame;
        if (form.getTable().getRowCount() == 0) {
            JOptionPane.showMessageDialog(this.mainFrame, "Нечего выбирать");
            return;
        }
        String operationId = form.getUserModel().getValueAt(form.getTable().getSelectedRow(), 0).toString();
        String name = form.getUserModel().getValueAt(form.getTable().getSelectedRow(), 1).toString();
        model.productIdExchange(operationId);
        goToAdd(name);
    }

    private void showCharts() {
        ChartForm chartForm = new ChartForm(model);
    }
}
