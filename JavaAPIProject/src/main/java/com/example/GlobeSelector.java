package com.example;

import gov.nasa.worldwind.BasicModel;
import gov.nasa.worldwind.awt.WorldWindowGLCanvas;
import gov.nasa.worldwind.event.SelectEvent;
import gov.nasa.worldwind.event.SelectListener;
import gov.nasa.worldwind.geom.Position;

import javax.swing.*;
import java.awt.*;

public class GlobeSelector extends JFrame {
    public GlobeSelector() {
        setTitle("Clickable Globe");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        WorldWindowGLCanvas wwd = new WorldWindowGLCanvas();
        wwd.setModel(new BasicModel());

        wwd.addSelectListener(new SelectListener() {
            public void selected(SelectEvent event) {
                if (event.getEventAction().equals(SelectEvent.LEFT_CLICK)) {
                    Position pos = event.getTopObject() != null ? event.getTopPickedObject().getPosition() : event.getPickPoint() != null ? wwd.getView().computePositionFromScreenPoint(event.getPickPoint().getX(), event.getPickPoint().getY()) : null;
                    if (pos != null) {
                        double lat = pos.getLatitude().degrees;
                        double lon = pos.getLongitude().degrees;
                        JOptionPane.showMessageDialog(null, "Latitude: " + lat + "\nLongitude: " + lon);
                    }
                }
            }
        });

        getContentPane().add(wwd, BorderLayout.CENTER);
        setVisible(true);
    }

    public static void main(String[] args) {
        new GlobeSelector();
    }
}

