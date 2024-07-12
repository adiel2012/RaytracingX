/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.raytracer;


import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class RenderPanel extends JPanel {
    private BufferedImage canvas;

    public RenderPanel() {
        canvas = new BufferedImage(800, 600, BufferedImage.TYPE_INT_RGB);
    }

    public void setCanvas(BufferedImage canvas) {
        this.canvas = canvas;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (canvas != null) {
            g.drawImage(canvas, 0, 0, null);
        }
    }
}

