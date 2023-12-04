package com.example;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.lwjgl.*;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;

import com.example.Planet.Sun;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.*;

public class Main {

    private long window;
    public Sun sun;

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
    
    public Vector2D translate = new Vector2D(640, 360);
    public Vector2D preMouse = null;
    public boolean drag = false;
    public boolean scal = false;
    public float scale = 1.0f;

    public void onMouse(int x, int y, int button, int action, int mods){
        if (button == GLFW_MOUSE_BUTTON_LEFT && action == GLFW_PRESS) {
            drag = true;
            preMouse = new Vector2D(x, y);
        }
        if (button == GLFW_MOUSE_BUTTON_LEFT && action == GLFW_RELEASE) {
            drag = false;
        }
        if (button == GLFW_MOUSE_BUTTON_RIGHT && action == GLFW_PRESS) {
            scal = true;
            preMouse = new Vector2D(x, y);
        }
        if (button == GLFW_MOUSE_BUTTON_RIGHT && action == GLFW_RELEASE) {
            scal = false;
        }
    }
    double[] mouse = new double[2];
    public void updateTranslate(){
        double[] x = new double[1];
        double[] y = new double[1];
        
        glfwGetCursorPos(window, x, y);
        y[0] = 720 - y[0];
        mouse[0] = x[0];
        mouse[1] = y[0];
        if (preMouse == null) {
            preMouse = new Vector2D(x[0], y[0]);
            return;
        }
        if (drag) {
            Vector2D change = (new Vector2D(x[0], y[0])).subtract(preMouse);
            translate = translate.add(change);
            preMouse = new Vector2D(x[0], y[0]);
        }
        if (scal) {
            scale += (y[0] - preMouse.getY()) / 100;
            preMouse = new Vector2D(x[0], y[0]);
        }
    }
    public void onDraw(){
        updateTranslate();
        glTranslated(translate.getX(), translate.getY(), 0);

        glScalef(scale, scale, 1);

        sun.update();
        sun.draw();

        glScalef(1f/scale, 1f/scale, 1);

        glTranslated(-translate.getX(), -translate.getY(), 0);
    }
    public void onInit(){
        sun = new Sun();
    }
    private void loop() {
        
        GL.createCapabilities();
        
        glViewport(0, 0, 1280, 720);
        glScalef(2.0f/ 1280, 2.0f / 720, 1.0f);
        glTranslatef(-640, -360, 0);
        glLineWidth(1f);
        glPointSize(1f);

        onInit();

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
