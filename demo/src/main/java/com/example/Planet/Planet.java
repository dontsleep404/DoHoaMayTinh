package com.example.Planet;

import static org.lwjgl.opengl.GL11.*;

import java.awt.Color;
import java.util.ArrayList;


public class Planet {
    public float speed;
    public float angle;
    public float radius;
    public float distance_from_parent;
    public Color color;
    public ArrayList<Planet> children;

    public Planet(float speed, float angle, float radius, Color color, float distance_from_parent) {
        this.speed = speed;
        this.angle = angle;
        this.radius = radius;
        this.color = color;
        this.distance_from_parent = distance_from_parent;
        children = new ArrayList<Planet>();
    }
    public void update() {
        angle += speed;
        for (Planet child : children) {
            child.update();
        }
    }

    public void draw() {
        drawPlanet();
    }

    public void drawPlanet() {
        // translate by angle and distance
        float translateX = distance_from_parent * (float) Math.cos(angle * Math.PI / 180);
        float translateY = distance_from_parent * (float) Math.sin(angle * Math.PI / 180);
        glTranslatef(translateX, translateY, 0);
        glColor3f(color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f);
        glBegin(GL_POLYGON);
        for (int i = 0; i < 360; i++) {
            glVertex2f(radius * (float) Math.cos(i * Math.PI / 180),radius * (float) Math.sin(i * Math.PI / 180));
        }
        glEnd();

        for (Planet child : children) {
            drawOrbit(child);
            child.drawPlanet();
        }

        glTranslatef(-translateX, -translateY, 0);
    }
    public void drawOrbit(Planet child) {
        glColor3f(0.5f, 0.5f, 0.5f);
        glBegin(GL_LINE_LOOP);
        for (int i = 0; i < 360; i++) {
            glVertex2f(child.distance_from_parent * (float) Math.cos(i * Math.PI / 180),child.distance_from_parent * (float) Math.sin(i * Math.PI / 180));
        }
        glEnd();
    }
}
