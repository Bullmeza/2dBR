import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL;
import render.Model;
import render.Shader;
import render.Texture;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;


public class Main {


    static long window;
    static int width = 640;
    static int height = 480;


    public Main() {
        if (!glfwInit()) {
            System.err.println("Failed 2 create");
            System.exit(1);
        }


        window = glfwCreateWindow(width, height, "Game", 0, 0);
        glfwShowWindow(window);
        glfwMakeContextCurrent(window);
        glfwSetWindowAttrib(window, GLFW_RESIZABLE, GL_FALSE);
        GL.createCapabilities();

        Camera camera = new Camera(width,height);
        glEnable(GL_TEXTURE_2D);

        float[] vertices = new float[]{
                -0.5f, 0.5f, 0,
                0.5f, 0.5f, 0,
                0.5f, -0.5f, 0,
                -0.5f, -0.5f, 0
        };

        float[] texture = new float[]{
                0, 0,
                1, 0,
                1, 1,
                0, 1
        };

        int[] indices = new int[]{
                0, 1, 2,
                2, 3, 0
        };

        Model model = new Model(vertices, texture, indices);
        Shader shader = new Shader("shader");

        Texture grass = new Texture("./res/grass.png");


        Matrix4f scale = new Matrix4f()
                .translate(new Vector3f(0,0,0))
                .scale(64);

        Matrix4f target = new Matrix4f();



        double frame_cap = 1.0 / 60.0;
        double frame_time = 0;
        int frames = 0;
        double time = FPS.getTime();
        double unprocessed = 0;

        while (!glfwWindowShouldClose(window)) {
           target = scale;

            boolean can_render = false;
            double time_2 = FPS.getTime();
            double passed = time_2 - time;
            unprocessed += passed;
            frame_time += passed;

            time = time_2;

            while (unprocessed >= frame_cap) {
                unprocessed -= frame_cap;
                can_render = true;

                glfwPollEvents();
                if (glfwGetKey(window, GLFW_KEY_ESCAPE) == 1) {
                    glfwSetWindowShouldClose(window, true);
                }
                if (glfwGetKey(window, GLFW_KEY_W) == 1) {
                    camera.setPosition(new Vector3f(0,100,0));
                }
                if (glfwGetKey(window, GLFW_KEY_S) == 1) {
                    camera.setPosition(new Vector3f(0,-100,0));
                }
                if (glfwGetKey(window, GLFW_KEY_D) == 1) {
                    camera.setPosition(new Vector3f(100,0,0));
                }
                if (glfwGetKey(window, GLFW_KEY_A) == 1) {
                    camera.setPosition(new Vector3f(-100,0,0));
                }

                if (glfwGetMouseButton(window, 0) == 1) {
                }

                if (glfwGetMouseButton(window, 1) == 1) {

                }
                if(frame_time >= 1.0){
                    frame_time = 0;
                    System.out.println("FPS:" + frames);
                    frames = 0;
                }
            }
            if(can_render){
                glClear(GL_COLOR_BUFFER_BIT);


                shader.bind();
                shader.setUniform("sampler",0);
                shader.setUniform("projection", camera.getProjection().mul(target));
                model.render();
                grass.bind(1);

                glEnd();
                glfwSwapBuffers(window);

                frames++;
            }
        }
        glfwTerminate();
    }

    public static void main(String[] args) {
        new Main();
    }
}