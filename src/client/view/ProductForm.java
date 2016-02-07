package com.DimatiBart.sigma.client.view;

import com.DimatiBart.sigma.client.controller.Controller;
import com.DimatiBart.sigma.client.model.ClientModel;
import com.DimatiBart.sigma.resources.TableCellListener;
import com.DimatiBart.sigma.resources.dataPackages.ProductData;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * Created by Dimati_Bart on 09.12.15.
 */
public class ProductForm extends JPanel {
    private JButton backToMain;
    private JButton costsButton;
    private JTable table;
    private JScrollPane scrollPane;
    private JPanel panel;
    private JButton deleteButton;
    private DefaultTableModel userModel;
    private ClientModel model;
    private Controller controller;

    public ProductForm(ClientModel model) {
        Controller controller = new Controller(this, panel, model);
        this.model = model;
        this.controller = controller;
        backToMain.addActionListener(controller);
        costsButton.addActionListener(controller);
        costsButton.setActionCommand("editCosts");
        backToMain.setActionCommand("goToMain");
        List<ProductData> data = model.getMainProductData();
        userModel = (DefaultTableModel) table.getModel();
        for (int i = 0; i < data.size(); i++)
            userModel.addRow(new Object[]{data.get(i).getOperationId(), data.get(i).getName(), data.get(i).getPrice()});
        userModel.fireTableDataChanged();
        userModel = (DefaultTableModel) table.getModel();
        Action action = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                TableCellListener tcl = (TableCellListener) e.getSource();
                if (tcl.getNewValue().toString().equals("")) {
                    userModel.setValueAt(tcl.getOldValue().toString(), tcl.getRow(), tcl.getColumn());
                    JOptionPane.showMessageDialog(controller.getMainFrame(), "Нельзя изменить на пустое значение!");
                } else {
                    String id = userModel.getValueAt(tcl.getRow(), 0).toString();
                    model.productUpdate(id, tcl.getNewValue().toString());
                }
            }
        };
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteData();
            }
        });
        TableCellListener tcl = new TableCellListener(table, action);
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
        DefaultTableModel tableModel = new DefaultTableModel() {
            String[] names = {"id", "Название", "Стоимость"};

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
                if (cols == 0 || cols == 2)
                    return false;
                else return true;
            }
        };
        table = new JTable(tableModel);
        table.setFillsViewportHeight(true);
    }

    private void deleteData() {
        String prodId;
        if (userModel.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this.controller.getMainFrame(), "Нечего удалять");
            return;
        } else if (userModel.getRowCount() == 1)
            prodId = userModel.getValueAt(0, 0).toString();
        else prodId = userModel.getValueAt(table.getSelectedRow(), 0).toString();
        model.productDelete(prodId);
        int selectedRow = table.getSelectedRow();
        if (userModel.getRowCount() == 1)
            userModel.removeRow(0);
        userModel.removeRow(selectedRow);
        userModel.fireTableDataChanged();
    }

    public DefaultTableModel getUserModel() {
        return userModel;
    }

    public JTable getTable() {
        return table;
    }
}
