package view;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.glColor3f;


public class Window {
    private long window;

    private int width, height;
    private Input input;

    public Window() {
        setSize(1000, 1000);
    }

    public void createWindow(String title) {
        window = glfwCreateWindow(width, height, title, 0, 0);

        if (window == 0) throw new IllegalStateException("Failed to create window!");

        glfwShowWindow(window);
        glfwSetWindowAttrib(window, GLFW_RESIZABLE, GL_FALSE);
        glfwMakeContextCurrent(window);




        input = new Input(window);

    }

    public boolean shouldClose() {
        return glfwWindowShouldClose(window);
    }

    public void swapBuffers() {
        glfwSwapBuffers(window);
    }

    public void setSize(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public long getWindow() {
        return window;
    }

    public Input getInput() {
        return input;
    }

    public void close() {
        glfwSetWindowShouldClose(window, true);
    }


}