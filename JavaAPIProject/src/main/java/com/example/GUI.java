package com.example;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;
import java.awt.BorderLayout;
import java.awt.GridLayout;
public class GUI {
    public GUI(){
        JFrame frame = new JFrame();
        JButton button = new JButton("Click to start");
        
        
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createEmptyBorder(100, 100, 100, 100));
        panel.setLayout(new GridLayout(0,1));
        panel.add(button);
        
        
        frame.add(panel, BorderLayout.CENTER);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("RESTAURANT RECOMMENDER");
        frame.pack();
        frame.setVisible(true);

    }; 
    public static void main(String[] args) {
        new GUI();
    }
}
