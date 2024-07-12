/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.raytracer;

/**
 *
 * @author adiel
 */
import java.awt.Color;

public interface Shape {
    boolean intersect(Ray ray, double[] t);
    Color getColor();
}