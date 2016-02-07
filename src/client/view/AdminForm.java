package com.DimatiBart.sigma.client.view;

import com.DimatiBart.sigma.client.controller.Controller;
import com.DimatiBart.sigma.client.model.ClientModel;
import com.DimatiBart.sigma.resources.TableCellListener;
import com.DimatiBart.sigma.resources.dataPackages.UserData;
import com.sun.tools.javac.comp.Flow;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dimati_Bart on 03.12.15.
 */
public class AdminForm extends JPanel {
    private JTable userTable;
    private JButton backToMain;
    private JPanel panel;

    public AdminForm(ClientModel model) {
        //panel = new JPanel();
        //panel.setLayout(new GridLayout(2, 1));
        Controller controller = new Controller(this, panel, model);
        //panel.add(userTable.getTableHeader(), BorderLayout.PAGE_START);
        backToMain.setActionCommand("goToMain");
        backToMain.addActionListener(controller);
        //panel.add(new JScrollPane(userTable), FlowLayout.LEFT);
        //panel.add(new JScrollPane(backToMain));
        DefaultTableModel userModel = (DefaultTableModel) userTable.getModel();
        List<UserData> data = model.getUsers();
        for (int i = 0; i < data.size(); i++)
            userModel.addRow(new Object[]{data.get(i).getId(), data.get(i).getEmail(), data.get(i).getType()});
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
                    if (!field.equals("type"))
                        newValue = "'" + newValue + "'";
                    model.adminUpdate(id, field, newValue);
                }
            }
        };

        TableCellListener tcl = new TableCellListener(userTable, action);
    }

    public JTable getTable() {
        return userTable;
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
        DefaultTableModel tableModel = new DefaultTableModel() {
            String[] names = {"id", "email", "type"};

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
        userTable = new JTable(tableModel);
    }

}