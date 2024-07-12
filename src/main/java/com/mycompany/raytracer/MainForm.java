package com.mycompany.raytracer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.LinkedList;
import java.util.List;

public class MainForm extends JFrame {
    private RenderPanel renderPanel;
    private Vector3D cameraPosition;
    private Vector3D lookAtPoint;

    public MainForm() {
        initComponents();
        cameraPosition = new Vector3D(0, 0, 0);
        lookAtPoint = new Vector3D(0, 0, -5);

        // Add a ComponentListener to ensure the RenderPanel is properly sized before rendering
        renderPanel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                renderScene();
            }

            @Override
            public void componentResized(ComponentEvent e) {
                renderScene();
            }
        });
    }

    private void initComponents() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Ray Tracing Demo");
        setSize(800, 600);

        // Create a RenderPanel for rendering the scene
        renderPanel = new RenderPanel();
        add(renderPanel, BorderLayout.CENTER);

        // Add keyboard and mouse listeners for camera navigation
        addKeyListener(new CameraKeyListener());
        renderPanel.addMouseListener(new CameraMouseListener());
        renderPanel.addMouseMotionListener(new CameraMouseListener());
    }

    public RenderPanel getRenderPanel() {
        return renderPanel;
    }

    public void renderScene() {
        if (renderPanel.getWidth() <= 0 || renderPanel.getHeight() <= 0) {
            return;
        }

        RayTracer rayTracer = new RayTracer();
        List<Shape> shapes = new LinkedList<>();

        double depth = -20;

        // Sphere
        shapes.add(new Sphere(new Vector3D(0, 0, depth), 1, Color.RED));

        // Create and add a new Cone
        Vector3D coneBaseCenter = new Vector3D(2, 0, depth);
        Vector3D coneAxisDirection = new Vector3D(-1, 1, 0);
        double coneHeight = 2;
        double coneRadius = Math.PI / 6;
        Color coneColor = Color.BLUE;
        shapes.add(new Cone(coneBaseCenter, coneAxisDirection, coneHeight, coneRadius, coneColor));

        // Cube
        shapes.add(new Cube(new Vector3D(1, -1, depth), 2, Color.GREEN));

        // Cuboid
        shapes.add(new Cuboid(new Vector3D(-1, 1, depth), new Vector3D(0.2, 0.4, 1.0), Color.CYAN));

        // Render the scene with camera position and look-at point
        rayTracer.draw(shapes, renderPanel, cameraPosition, lookAtPoint);
        renderPanel.repaint();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainForm mainForm = new MainForm();
            mainForm.setVisible(true);
        });
    }

    private class CameraKeyListener extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            double moveSpeed = 0.5;

            switch (e.getKeyCode()) {
                case KeyEvent.VK_W:
                    cameraPosition = cameraPosition.add(new Vector3D(0, 0, -moveSpeed));
                    break;
                case KeyEvent.VK_S:
                    cameraPosition = cameraPosition.add(new Vector3D(0, 0, moveSpeed));
                    break;
                case KeyEvent.VK_A:
                    cameraPosition = cameraPosition.add(new Vector3D(-moveSpeed, 0, 0));
                    break;
                case KeyEvent.VK_D:
                    cameraPosition = cameraPosition.add(new Vector3D(moveSpeed, 0, 0));
                    break;
                case KeyEvent.VK_UP:
                    lookAtPoint = lookAtPoint.add(new Vector3D(0, -moveSpeed, 0));
                    break;
                case KeyEvent.VK_DOWN:
                    lookAtPoint = lookAtPoint.add(new Vector3D(0, moveSpeed, 0));
                    break;
                case KeyEvent.VK_LEFT:
                    lookAtPoint = lookAtPoint.add(new Vector3D(-moveSpeed, 0, 0));
                    break;
                case KeyEvent.VK_RIGHT:
                    lookAtPoint = lookAtPoint.add(new Vector3D(moveSpeed, 0, 0));
                    break;
            }

            renderScene();
        }
    }

    private class CameraMouseListener extends MouseAdapter {
        private Point lastMousePosition;

        @Override
        public void mousePressed(MouseEvent e) {
            lastMousePosition = e.getPoint();
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            if (lastMousePosition == null) {
                lastMousePosition = e.getPoint();
                return;
            }

            int deltaX = e.getX() - lastMousePosition.x;
            int deltaY = e.getY() - lastMousePosition.y;

            double rotateSpeed = 0.01;
            lookAtPoint = lookAtPoint.add(new Vector3D(-deltaX * rotateSpeed, deltaY * rotateSpeed, 0));

            lastMousePosition = e.getPoint();
            renderScene();
        }
    }
}
