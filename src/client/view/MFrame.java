package com.DimatiBart.sigma.client.view;

/**
 * Created by Dimati_Bart on 01.12.15.
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class MFrame extends JFrame {
    private Component currentContent;

    public MFrame(Component inContent) {
        Container content = getContentPane();
        content.setLayout(new BorderLayout());
        setTitle("Sigma");
        setContent(inContent);
        setSize(inContent.getPreferredSize());
        setResizable(false);
        //setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    public void setContent(Component inComponent) {
        Container contentPane = getContentPane();
        if (currentContent != null) {
            contentPane.remove(currentContent);
        }
        contentPane.add(inComponent, BorderLayout.CENTER);
        currentContent = inComponent;
        contentPane.doLayout();
        repaint();
    }
}