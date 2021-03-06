package view;

//Robert Muresan
//2020/05/31
//2D Battle Royale
//Play with friends on a server/localhost

import static org.lwjgl.glfw.GLFW.*;

import org.joml.Vector2f;

public class Input {
    private long window;


    private static Vector2f mousePos = new Vector2f();
    private static double[] x = new double[1], y = new double[1];
    private static int[] winWidth = new int[1], winHeight = new int[1];

    public Input(long window) {
        this.window = window;
    }

    public boolean isKeyDown(int key) {
        return glfwGetKey(window, key) == 1;
    }

    public boolean isMouseButtonDown(int button) {
        return glfwGetMouseButton(window, button) == 1;
    }

    public Vector2f getMousePosition() {
        glfwGetCursorPos(window, x, y);

        glfwGetWindowSize(window, winWidth, winHeight);

        mousePos.set((float) x[0] - (winWidth[0] / 2.0f), -((float) y[0] - (winHeight[0] / 2.0f)));

        return mousePos;
    }

}