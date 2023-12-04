package com.example.Planet;

import java.awt.Color;

public class Sun extends Planet {

    public static final float earthSpeed = 0.01f;
    public static final float earthRadius = 20f;
    public static final float earthDis = 100f;

    public Sun() {
        super(0, 0, 50, Color.YELLOW, 0 * earthDis);
        children.add(Mercury());
        children.add(Venus());
        children.add(Earth());
        children.add(Mars());
        children.add(Jupiter());
        children.add(Saturn());
        children.add(Uranus());
        children.add(Neptune());
    }

    public Planet Mercury() {
        float mercuryOrbitRadius = earthDis;
        return new Planet(365f / 88f * earthSpeed, 0, 20, Color.RED, mercuryOrbitRadius);
    }

    public Planet Venus() {
        float venusOrbitRadius = 2f * earthDis;
        return new Planet(365f / 225f * earthSpeed, 0, 20, Color.ORANGE, venusOrbitRadius);
    }

    public Planet Earth() {
        float earthOrbitRadius = 3f * earthDis;
        Planet earth = new Planet(earthSpeed, 0, 20, Color.BLUE, earthOrbitRadius);
        Planet moon = Moon();
        earth.children.add(moon);
        return earth;
    }
    public Planet Moon() {
        return new Planet(365f * earthSpeed, 0, 10, Color.WHITE, 40);
    }

    public Planet Mars() {
        float marsOrbitRadius = 4f * earthDis;
        return new Planet(365f / 687f * earthSpeed, 0, 20, Color.RED, marsOrbitRadius);
    }

    public Planet Jupiter() {
        float jupiterOrbitRadius = 5f * earthDis;
        return new Planet(1f / 11.86f * earthSpeed, 0, 20, Color.ORANGE, jupiterOrbitRadius);
    }

    public Planet Saturn() {
        float saturnOrbitRadius = 6f * earthDis;
        return new Planet(1f / 29.46f * earthSpeed, 0, 20, Color.YELLOW, saturnOrbitRadius);
    }

    public Planet Uranus() {
        float uranusOrbitRadius = 7f * earthDis;
        return new Planet(1f / 84f * earthSpeed, 0, 20, Color.CYAN, uranusOrbitRadius);
    }

    public Planet Neptune() {
        float neptuneOrbitRadius = 8f * earthDis;
        return new Planet(1f / 165f * earthSpeed, 0, 20, Color.BLUE, neptuneOrbitRadius);
    }
}
