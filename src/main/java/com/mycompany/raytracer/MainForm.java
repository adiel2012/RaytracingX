package com.mycompany.raytracer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class MainForm extends JFrame {
    private RenderPanel renderPanel;
    private Vector3D cameraPosition;
    private Vector3D lookAtPoint;
    private List<Shape> shapes;
    private List<Light> lights;
    private RayTracer rayTracer;

    public MainForm() {
        initComponents();
        initScene();
    }

    private void initComponents() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Ray Tracing Demo");
        setSize(800, 600);

        renderPanel = new RenderPanel();
        add(renderPanel, BorderLayout.CENTER);

        JPanel controlPanel = new JPanel();
        JButton renderButton = new JButton("Render");
        renderButton.addActionListener(e -> renderScene());
        controlPanel.add(renderButton);

        add(controlPanel, BorderLayout.SOUTH);

        addKeyListener(new CameraKeyListener());
        renderPanel.addMouseListener(new CameraMouseListener());
        renderPanel.addMouseMotionListener(new CameraMouseListener());
        renderPanel.setFocusable(true);
        renderPanel.requestFocusInWindow();

        renderPanel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                renderScene();
            }
        });
    }

    private void initScene() {
        cameraPosition = new Vector3D(0, 0, 5);
        lookAtPoint = new Vector3D(0, 0, 0);

        shapes = new ArrayList<>();
        lights = new ArrayList<>();

        // Add shapes
        shapes.add(new Sphere(new Vector3D(0, 0, -1), 1, Color.RED));
        shapes.add(new Cone(new Vector3D(2, -1, -2), new Vector3D(0, 1, 0), 2, 1, Color.BLUE));
        shapes.add(new Cube(new Vector3D(-2, 0, -2), 1.5, Color.GREEN));
        shapes.add(new Cuboid(new Vector3D(0, -1, -2), new Vector3D(0.5, 1, 0.5), Color.CYAN));
        shapes.add(new Cylinder(new Vector3D(-2, 1, -3), 0.5, 2, Color.MAGENTA));
        shapes.add(new Plane(new Vector3D(0, 1, 0), 2, Color.GRAY));
        //shapes.add(new Pyramid(new Vector3D(2, 0, -4), 2, 2, 2, Color.YELLOW));

        // Add lights
        lights.add(new Light(new Vector3D(5, 5, 5), Color.WHITE, 1.0));
        lights.add(new Light(new Vector3D(-5, 5, 5), Color.WHITE, 0.5));

        rayTracer = new RayTracer(lights);
    }

    public void renderScene() {
        if (renderPanel.getWidth() <= 0 || renderPanel.getHeight() <= 0) {
            return;
        }

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
            Vector3D moveDirection = new Vector3D(0, 0, 0);

            switch (e.getKeyCode()) {
                case KeyEvent.VK_W: moveDirection = lookAtPoint.subtract(cameraPosition).normalize(); break;
                case KeyEvent.VK_S: moveDirection = cameraPosition.subtract(lookAtPoint).normalize(); break;
                case KeyEvent.VK_A:
                    Vector3D right = lookAtPoint.subtract(cameraPosition).cross(new Vector3D(0, 1, 0)).normalize();
                    moveDirection = right.scale(-1);
                    break;
                case KeyEvent.VK_D:
                    right = lookAtPoint.subtract(cameraPosition).cross(new Vector3D(0, 1, 0)).normalize();
                    moveDirection = right;
                    break;
            }

            cameraPosition = cameraPosition.add(moveDirection.scale(moveSpeed));
            lookAtPoint = lookAtPoint.add(moveDirection.scale(moveSpeed));

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
            Vector3D cameraToLookAt = lookAtPoint.subtract(cameraPosition);
            
            cameraToLookAt = cameraToLookAt.rotate(new Vector3D(0, 1, 0), -deltaX * rotateSpeed);
            
            Vector3D right = cameraToLookAt.cross(new Vector3D(0, 1, 0)).normalize();
            cameraToLookAt = cameraToLookAt.rotate(right, -deltaY * rotateSpeed);

            lookAtPoint = cameraPosition.add(cameraToLookAt);

            lastMousePosition = e.getPoint();
            renderScene();
        }
    }
}