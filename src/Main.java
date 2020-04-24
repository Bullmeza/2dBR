import org.lwjgl.opengl.GL;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;


public class Main {


    static long window;


    public Main() {
        if (!glfwInit()) {
            System.err.println("Failed 2 create");
            System.exit(1);
        }


        window = glfwCreateWindow(500, 500, "Game", 0, 0);
        glfwShowWindow(window);
        glfwMakeContextCurrent(window);
        GL.createCapabilities();
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
        Texture grass = new Texture("./res/grass.png");


        double frame_cap = 1.0 / 60.0;

        double frame_time = 0;
        int frames = 0;
        double time = FPS.getTime();
        double unprocessed = 0;


        while (!glfwWindowShouldClose(window)) {
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

                grass.bind();
                model.render();

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