package com.example;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.lwjgl.*;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.*;

import java.util.ArrayList;

public class Main {

    private long window;

    public void run() {
        System.out.println("Hello LWJGL " + Version.getVersion() + "!");

        init();
        loop();

        glfwFreeCallbacks(window);
        glfwDestroyWindow(window);

        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    private void init() {
        GLFWErrorCallback.createPrint(System.err).set();

        if (!glfwInit())
            throw new IllegalStateException("Unable to initialize GLFW");

        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE);
        window = glfwCreateWindow(1280, 720, "Hello World!", NULL, NULL);
        // center
        GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        glfwSetWindowPos(window, (vidmode.width() - 1280) / 2, (vidmode.height() - 720) / 2);
        
        if (window == NULL)
            throw new RuntimeException("Failed to create the GLFW window");

        glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
            if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE)
                glfwSetWindowShouldClose(window, true);
        });

        // mouse click
        glfwSetMouseButtonCallback(window, (window, button, action, mods) -> {
            // if (button == GLFW_MOUSE_BUTTON_LEFT && action == GLFW_RELEASE) {
                double[] x = new double[1];
                double[] y = new double[1];
                glfwGetCursorPos(window, x, y);
                y[0] = 720 - y[0];
                onMouse((int)x[0], (int)y[0], button, action, mods);
            // }
        });

        glfwMakeContextCurrent(window);
        glfwSwapInterval(1);

        glfwShowWindow(window);
    }
    
    
    public ArrayList<Vector2D> LineArr = new ArrayList<Vector2D>();
    public ArrayList<Vector2D> CircleArr = new ArrayList<Vector2D>();
    public ArrayList<Vector2D> EllipseArr = new ArrayList<Vector2D>();
    enum MODE{
        LINE,
        CIRCLE,
        ELLIPSE,
    }
    public MODE mode = MODE.LINE;

    public void onMouse(int x, int y, int button, int action, int mods){
        if (button == GLFW_MOUSE_BUTTON_MIDDLE && action == GLFW_RELEASE) {
            if (mode == MODE.LINE) mode = MODE.CIRCLE;
            else if (mode == MODE.CIRCLE) mode = MODE.ELLIPSE;
            else if (mode == MODE.ELLIPSE) mode = MODE.LINE;
            LineArr.clear();
            CircleArr.clear();
            EllipseArr.clear();
        }
        if (button == GLFW_MOUSE_BUTTON_LEFT) {
            if (mode == MODE.LINE) LineArr.add(new Vector2D(x, y));
            else if (mode == MODE.CIRCLE) CircleArr.add(new Vector2D(x, y));
            else if (mode == MODE.ELLIPSE) EllipseArr.add(new Vector2D(x, y));
        }
        if (button == GLFW_MOUSE_BUTTON_RIGHT && action == GLFW_RELEASE) {
            LineArr.clear();
            CircleArr.clear();
            EllipseArr.clear();
        }
    }
    public void lineBresenham(Vector2D v1, Vector2D v2){
        int x1 = (int)v1.getX();
        int y1 = (int)v1.getY();
        int x2 = (int)v2.getX();
        int y2 = (int)v2.getY();
        glVertex2i(x1, y1);
        int dx = Math.abs(x2 - x1);
        int dy = Math.abs(y2 - y1);
        int p = 2 * dy - dx;
        int x = x1;
        int y = y1;
        int x_unit = 1;
        int y_unit = 1;
        if (x1 > x2) x_unit = -1;
        if (y1 > y2) y_unit = -1;
        //
        boolean swapxy = false;
        if (dy > dx) {
            swapxy = true;
            int temp;
            temp = dx; dx = dy; dy = temp;
            temp = x; x = y; y = temp;
            temp = x1; x1 = y1; y1 = temp;
            temp = x2; x2 = y2; y2 = temp;
            temp = x_unit; x_unit = y_unit; y_unit = temp;
        }
        // đưa về duy nhất 1 trường hợp 
        glBegin(GL_POINTS);
        while (x != x2){
            if (swapxy) glVertex2i(y, x);
            else glVertex2i(x, y);
            x += x_unit;
            if (p < 0){
                p += 2 * dy;
            } else {
                p += 2 * dy - 2 * dx;
                y += y_unit;
            }
        }
        glEnd();
    }
    public void lineMidpoint(Vector2D v1, Vector2D v2){
        int x1 = (int)v1.getX();
        int y1 = (int)v1.getY();
        int x2 = (int)v2.getX();
        int y2 = (int)v2.getY();
        glVertex2i(x1, y1);
        int dx = Math.abs(x2 - x1);
        int dy = Math.abs(y2 - y1);
        int p = dx - 2 * dy;
        int x = x1;
        int y = y1;
        int x_unit = 1;
        int y_unit = 1;
        if (x1 > x2) x_unit = -1;
        if (y1 > y2) y_unit = -1;
        //
        boolean swapxy = false;
        if (dy > dx) {
            swapxy = true;
            int temp;
            temp = dx; dx = dy; dy = temp;
            temp = x; x = y; y = temp;
            temp = x1; x1 = y1; y1 = temp;
            temp = x2; x2 = y2; y2 = temp;
            temp = x_unit; x_unit = y_unit; y_unit = temp;
        }
        // đưa về duy nhất 1 trường hợp 
        glBegin(GL_POINTS);
        while (x != x2){
            if (swapxy) glVertex2i(y, x);
            else glVertex2i(x, y);
            x += x_unit;
            if (p < 0){
                p += 2 * dy;
            } else {
                p += 2 * dy - 2 * dx;
                y += y_unit;
            }
        }
        glEnd();
    }
    public void circleBres(Vector2D v1, Vector2D v2){
        int xc = (int)v1.getX();
        int yc = (int)v1.getY();
        int r = (int) Vector2D.distance(v1, v2);
        int x = 0;
        int y = r;
        int p = 3 - 2 * r;
        while (x <= y){
            point8(xc, yc, x, y);
            x++;
            if (p < 0){
                p += 4 * x + 6;
            } else {
                p += 4 * (x - y) + 10;
                y--;
            }
        }
    }
    public void circleMidpoint(Vector2D v1, Vector2D v2){
        int xc = (int)v1.getX();
        int yc = (int)v1.getY();
        int r = (int) Vector2D.distance(v1, v2);
        int x = 0;
        int y = r;
        int p = 5 / 4 - r;
        while (x <= y){
            point8(xc, yc, x, y);
            x++;
            if (p < 0){
                p += 2 * x + 3;
            } else {
                p += 2 * (x - y) + 5;
                y--;
            }
        }
    }
    public void ellipseBres(Vector2D v1, Vector2D v2){
        int xc = (int)v1.getX();
        int yc = (int)v1.getY();
        double a = Math.abs(v2.getX() - v1.getX());
        double b = Math.abs(v2.getY() - v1.getY());
        double a2 = a * a;
        double b2 = b * b;
        double x = 0;
        double y = b;
        double p = b2 - a2 * b + 0.25 * a2;
        while (b2 * x < a2 * y){
            point4(xc, yc, (int)x, (int)y);
            x++;
            if (p < 0){
                p += b2 * (2 * x + 3);
            } else {
                p += b2 * (2 * x + 3) + a2 * (2 - 2 * y);
                y--;
            }
        }
        p = b2 * (x + 0.5) * (x + 0.5) + a2 * (y - 1) * (y - 1) - a2 * b2;
        while (y >= 0){
            point4(xc, yc, (int)x, (int)y);
            y--;
            if (p > 0){
                p += a2 * (3 - 2 * y);
            } else {
                p += b2 * (2 * x + 2) + a2 * (3 - 2 * y);
                x++;
            }
        }

        
    }
    public void ellipseMidpoint(Vector2D v1, Vector2D v2){
        int xc = (int)v1.getX();
        int yc = (int)v1.getY();
        double a = Math.abs(v2.getX() - v1.getX());
        double b = Math.abs(v2.getY() - v1.getY());
        double a2 = a * a;
        double b2 = b * b;
        double x = 0;
        double y = b;
        double p = b2 - a2 * b + 0.25 * a2;
        while (b2 * x < a2 * y){
            point4(xc, yc, (int)x, (int)y);
            x++;
            if (p < 0){
                p += b2 * (2 * x + 3);
            } else {
                p += b2 * (2 * x + 3) + a2 * (2 - 2 * y);
                y--;
            }
        }
        p = b2 * (x + 0.5) * (x + 0.5) + a2 * (y - 1) * (y - 1) - a2 * b2;
        while (y >= 0){
            point4(xc, yc, (int)x, (int)y);
            y--;
            if (p > 0){
                p += a2 * (3 - 2 * y);
            } else {
                p += b2 * (2 * x + 2) + a2 * (3 - 2 * y);
                x++;
            }
        }

        
    }
    public void point4(int xc, int yc, int x, int y){
        glBegin(GL_POINTS);
        glVertex2i(xc + x, yc + y);
        glVertex2i(xc - x, yc + y);
        glVertex2i(xc + x, yc - y);
        glVertex2i(xc - x, yc - y);
        glEnd();
    }
    public void point8(int xc, int yc, int x, int y){
        glBegin(GL_POINTS);
        glVertex2i(xc + x, yc + y);
        glVertex2i(xc - x, yc + y);
        glVertex2i(xc + x, yc - y);
        glVertex2i(xc - x, yc - y);
        glVertex2i(xc + y, yc + x);
        glVertex2i(xc - y, yc + x);
        glVertex2i(xc + y, yc - x);
        glVertex2i(xc - y, yc - x);
        glEnd();
    }
    public void onDraw(){
        double[] x = new double[1];
        double[] y = new double[1];
        glfwGetCursorPos(window, x, y);
        y[0] = 720 - y[0];
        Vector2D mouse = new Vector2D(x[0], y[0]);

        if (LineArr.size() > 0){
            for (int i = 0; i < LineArr.size() - 1; i += 2){
                lineMidpoint(LineArr.get(i), LineArr.get(i + 1));
            }
            if (LineArr.size() % 2 == 1){
                lineMidpoint(LineArr.get(LineArr.size() - 1), mouse);
            }
        }
        if (CircleArr.size() > 0){
            for (int i = 0; i < CircleArr.size() - 1; i += 2){
                circleBres(CircleArr.get(i), CircleArr.get(i + 1));
            }
            if (CircleArr.size() % 2 == 1){
                circleBres(CircleArr.get(CircleArr.size() - 1), mouse);
            }
        }
        if (EllipseArr.size() > 0){
            for (int i = 0; i < EllipseArr.size() - 1; i += 2){
                ellipseBres(EllipseArr.get(i), EllipseArr.get(i + 1));
            }
            if (EllipseArr.size() % 2 == 1){
                ellipseBres(EllipseArr.get(EllipseArr.size() - 1), mouse);
            }
        }
    }
    private void loop() {
        
        GL.createCapabilities();

        glViewport(0, 0, 1280, 720);
        glScalef(2.0f/ 1280, 2.0f / 720, 1.0f);
        glTranslatef(-640, -360, 0);
        glLineWidth(1f);
        glPointSize(1f);

        while (!glfwWindowShouldClose(window)) {
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
            onDraw();
            glfwSwapBuffers(window);
            glfwPollEvents();
        }
    }
    

    public static void main(String[] args) {
        new Main().run();
    }
}
