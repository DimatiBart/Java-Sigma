package com.DimatiBart.sigma.client.view;

import com.DimatiBart.sigma.client.controller.Controller;
import com.DimatiBart.sigma.client.model.ClientModel;
import com.DimatiBart.sigma.resources.dataPackages.ProductData;
import javafx.scene.chart.BarChart;
import org.knowm.xchart.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.List;

/**
 * Created by Dimati_Bart on 09.12.15.
 */
public class ChartForm extends JPanel{
    private JPanel panel;
    private JPanel panelChart;
    private JButton backToMain;
    private JButton saveChart;
    private ClientModel model;
    public ChartForm (ClientModel model) {
        this.model = model;
        Controller controller = new Controller(this, panel, model);
        backToMain.addActionListener(controller);
        backToMain.setActionCommand("goToMain");
        List<ProductData> data = model.getMainProductData();
        if (data.size() == 0) {
            JOptionPane.showMessageDialog(controller.getMainFrame(), "Отсутствуют записи");
            backToMain.doClick();
            return;
        }
        Chart chart = new ChartBuilder().chartType(StyleManager.ChartType.Line).width(200).height(400).title("График изменения стоимости товаров").xAxisTitle("Товар").yAxisTitle("Стоимость").build();
        chart.getStyleManager().setYAxisDecimalPattern("\u20BD #,###.##");
        double[] yData = new double[data.size()];
        double[] xData = new double[data.size()];

        for (int i = 0; i < data.size(); i++) {
            xData[i] = data.get(i).getPrice();
            yData[i] = i;
        }
        chart.addSeries("\u20BD", yData, xData);
        JPanel pnlChart = new XChartPanel(chart);
        panelChart.setLayout(new java.awt.BorderLayout());
        panelChart.add(pnlChart);
        panelChart.validate();
        saveChart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    BitmapEncoder.saveBitmap(chart, "./chart", BitmapEncoder.BitmapFormat.PNG);
                } catch (IOException except) {
                    System.err.println("Ошибка при сохранении графика");
                }
            }
        });
    }
}
