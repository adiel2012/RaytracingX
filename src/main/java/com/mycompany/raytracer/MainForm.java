package com.mycompany.raytracer;

import javax.swing.*;
import java.awt.*;
import java.util.AbstractList;
import java.util.LinkedList;
import java.util.List;
// https://chatgpt.com/c/4372711f-43c8-4fd6-a5de-298ec65b661f
public class MainForm extends JFrame {
    private RenderPanel renderPanel;

    public MainForm() {
        initComponents();
    }

    private void initComponents() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Ray Tracing Demo");
        setSize(800, 600);

        // Create a RenderPanel for rendering the scene
        renderPanel = new RenderPanel();
        add(renderPanel, BorderLayout.CENTER);
    }

    public RenderPanel getRenderPanel() {
        return renderPanel;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainForm mainForm = new MainForm();
            mainForm.setVisible(true);
            
            double depth = -20;

            // Example camera position and look-at point
            Vector3D cameraPosition = new Vector3D(0, 0, 0);
            Vector3D lookAtPoint = new Vector3D(0, 0, -5);

            RayTracer rayTracer = new RayTracer();
            
            List<Shape> shapes = new LinkedList<>();
            
            // Sphere
            shapes.add(new Sphere(new Vector3D(0, 0, depth), 1, Color.RED));
            
            // Create and add a new Cone
            Vector3D coneBaseCenter = new Vector3D(2, 0, depth);
            Vector3D coneAxisDirection = new Vector3D(-1, 1, 0);
            double coneHeight = 2;
            double coneRadius = Math.PI / 6;
            Color coneColor = Color.BLUE;
            shapes.add(new Cone(coneBaseCenter, coneAxisDirection, coneHeight, coneRadius, coneColor));
            //Cube
            shapes.add(new Cube(new Vector3D(1, -1, depth), 2, Color.GREEN));
            // Cuboid
            shapes.add(new Cuboid(new Vector3D(-1, 1, depth), new Vector3D(0.2, 0.4, 1.0), Color.CYAN));
            
            // Render the scene with camera position and look-at point
            rayTracer.draw(shapes, mainForm.getRenderPanel(), cameraPosition, lookAtPoint);
        });
    }
}
