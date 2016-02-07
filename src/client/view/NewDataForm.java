package com.DimatiBart.sigma.client.view;

import com.DimatiBart.sigma.client.controller.Controller;
import com.DimatiBart.sigma.client.model.ClientModel;
import com.DimatiBart.sigma.resources.TableCellListener;
import com.DimatiBart.sigma.resources.dataPackages.ProductData;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * Created by Dimati_Bart on 06.12.15.
 */
public class NewDataForm extends JPanel {
    private JPanel panel;
    private JButton backToMain;
    private JButton addButton;
    private JTable dataTable;
    private JTextField inputName;
    private JTextField inputAmount;
    private JTextField inputPrice;
    private JTextField inputK;
    private JCheckBox checkBoxRecurrent;
    private JLabel label;
    private JTextField resultSum;
    private JButton deleteButton;
    private JButton saveFileButton;
    private TableColumn idColumn;
    private DefaultTableModel userModel;
    private ClientModel model;
    private Controller controller;
    private String name;

    public NewDataForm(ClientModel model, String name) {
        Controller controller = new Controller(this, panel, model);
        this.name = name;
        this.model = model;
        this.controller = controller;
        backToMain.addActionListener(controller);
        backToMain.setActionCommand("goToMain");
        label.setText(label.getText() + " \"" + name + "\"");
        List<ProductData> data = model.getProductData();
        userModel = (DefaultTableModel) dataTable.getModel();
        for (int i = 0; i < data.size(); i++)
            userModel.addRow(new Object[]{data.get(i).getID(), data.get(i).getName(), data.get(i).getK(), data.get(i).getPrice(), data.get(i).getAmount(), data.get(i).isRecurrent()});
        idColumn = dataTable.getColumnModel().getColumn(0);
        dataTable.removeColumn(idColumn);
        addListeners();
        fillInProdPrice();
        Action action = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                TableCellListener tcl = (TableCellListener) e.getSource();
                if (tcl.getNewValue().toString().equals("")) {
                    userModel.setValueAt(tcl.getOldValue().toString(), tcl.getRow(), tcl.getColumn());
                    JOptionPane.showMessageDialog(controller.getMainFrame(), "Нельзя изменить на пустое значение!");
                } else {
                    String id = userModel.getValueAt(tcl.getRow(), 0).toString();
                    String field = userModel.getColumnName(tcl.getColumn());
                    String newValue = tcl.getNewValue().toString();
                    switch (field) {
                        case "Стоимость":
                            field = "price";
                            newValue = "'"+newValue+"'";
                            break;
                        case "Название":
                            field = "cost_name";
                            newValue = "'"+newValue+"'";
                            break;
                        case "К":
                            field = "K";
                            newValue = "'"+newValue+"'";
                            break;
                        case "Кол-во":
                            field = "amount";
                            newValue = "'"+newValue+"'";
                            break;
                        case "Возвратные?":
                            field = "type";
                            break;
                    }
                    model.costsUpdate(id, field, newValue);
                    fillInProdPrice();
                }
            }
        };

        TableCellListener tcl = new TableCellListener(dataTable, action);
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
        DefaultTableModel tableModel = new DefaultTableModel() {
            String[] names = {"id", "Название", "К", "Стоимость", "Кол-во", "Возвратные?"};

            @Override
            public int getColumnCount() {
                return names.length;
            }

            @Override
            public String getColumnName(int index) {
                return names[index];
            }

            @Override
            public boolean isCellEditable(int row, int cols) {
                if (cols == 0)
                    return false;
                else return true;
            }
        };
        dataTable = new JTable(tableModel);
        dataTable.setFillsViewportHeight(true);
    }

    private void addListeners() {
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                pressedAddButton();
            }
        });
        inputAmount.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                intCheck(e);
            }

            @Override
            public void keyPressed(KeyEvent e) {

            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });
        inputPrice.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                floatCheck(e);
            }

            @Override
            public void keyPressed(KeyEvent e) {

            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });
        inputK.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                floatCheck(e);
            }

            @Override
            public void keyPressed(KeyEvent e) {

            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String costId;
                if (userModel.getRowCount() == 0) {
                    JOptionPane.showMessageDialog(controller.getMainFrame(), "Нечего удалять");
                    return;
                } else if (userModel.getRowCount() == 1)
                    costId = userModel.getValueAt(0, 0).toString();
                else costId = userModel.getValueAt(dataTable.getSelectedRow(), 0).toString();
                model.deleteCost(costId);
                fillInProdPrice();
                int selectedRow = dataTable.getSelectedRow();
                if (userModel.getRowCount() == 1)
                    userModel.removeRow(0);
                userModel.removeRow(selectedRow);
            }
        });
        saveFileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if (userModel.getRowCount() == 0) {
                        JOptionPane.showMessageDialog(controller.getMainFrame(), "Нечего сохранять");
                        return;
                    }
                    FileWriter writer = new FileWriter(name + ".txt");
                    writer.write("Товар \""+ name +"\"\n");
                    writer.write ("Полная себестоимость: "+ resultSum.getText()+"\n");
                    writer.write("Затраты: \n\n");
                    for(int i=0;i<userModel.getRowCount();i++)
                    {
                        writer.write("Наименование: "+ dataTable.getValueAt(i, 0)+ "\n");
                        writer.write("Количество: "+ dataTable.getValueAt(i, 2)+ "\n");
                        writer.write("Стоимость: "+ dataTable.getValueAt(i, 1)+ "\n");
                        writer.write("К: " + dataTable.getValueAt(i, 3) + "\n");
                        if (Boolean.parseBoolean(dataTable.getValueAt(i, 4).toString()))
                            writer.write("Возвратные: да \n\n");
                        else writer.write("Возвратные: нет \n\n");
                    }
                    JOptionPane.showMessageDialog(controller.getMainFrame(), "Сохранение произошло успешно");
                    writer.close();
                } catch (IOException except) {
                    System.err.println("Ошибка при сохранении файла");
                    System.err.println(except.getMessage());
                }
            }
        });
    }

    private void floatCheck(KeyEvent evt) {
        // TODO add your handling code here:
        char key = evt.getKeyChar();
        if (!((key >= '0' && key <= '9') || key == '.'))
            evt.setKeyChar((char) 0);
    }

    private void intCheck(KeyEvent evt) {
        // TODO add your handling code here:
        char key = evt.getKeyChar();
        if (!(key >= '0' && key <= '9'))
            evt.setKeyChar((char) 0);
    }

    private void pressedAddButton() {
        String newID = model.addCost(inputName.getText(), inputK.getText(), inputPrice.getText(), inputAmount.getText(), checkBoxRecurrent.isSelected());
        JOptionPane.showMessageDialog(controller.getMainFrame(), "Данные успешно добавленны");
        userModel.addRow(new Object[]{newID, inputName.getText(), inputK.getText(), inputPrice.getText(), inputAmount.getText(), checkBoxRecurrent.isSelected()});
        clearFields();
        fillInProdPrice();
        userModel.fireTableDataChanged();
    }


    private void clearFields() {
        inputName.setText("");
        inputAmount.setText("");
        inputPrice.setText("");
        inputK.setText("");
        checkBoxRecurrent.setSelected(false);
    }

    private void fillInProdPrice() {
        resultSum.setText(model.getProductPrice().toString());
    }

}
