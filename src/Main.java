import collision.AABB;
import entity.Bullet;
import entity.Player;
import generate.Tile;
import generate.TileRender;
import generate.World;
import multiplayer.MazeReq;
import org.joml.Random;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL;
import render.*;
import view.Camera;
import view.Window;
import render.FPS;


import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;


public class Main {


    public Main() {


        if (!glfwInit()) {
            System.err.println("Failed 2 create");
            System.exit(1);
        }


        Window window = new Window();
        window.createWindow("8-Bit BR");
        GL.createCapabilities();
        glEnable(GL_TEXTURE_2D);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);


        Camera camera = new Camera(window.getWidth(), window.getHeight());
        TileRender tiles = new TileRender();
        Shader shader = new Shader("shader");
        World world = new World();
        MazeReq mazeReq = new MazeReq();
        Player player = new Player();

        String id = mazeReq.createMap(world.getWidth());
        System.out.println(id);

        Random rand = new Random();

        int randX = rand.nextInt(world.getWidth() - 2) + 1;
        int randY = rand.nextInt(world.getHeight() - 2) + 1;


        player.move(new Vector2f(randX, -randY), world);




        double frame_cap = 1.0 / 60.0;
        double frame_time = 0;
        int frames = 0;
        double time = FPS.getTime();
        double unprocessed = 0;
        boolean maze_loaded = false;

        while (!window.shouldClose()) {


            boolean can_render = false;
            double time_2 = FPS.getTime();
            double passed = time_2 - time;
            unprocessed += passed;
            frame_time += passed;
            time = time_2;


            while (unprocessed >= frame_cap) {
                unprocessed -= frame_cap;
                can_render = true;


                if (window.getInput().isKeyDown(GLFW_KEY_ESCAPE)) {
                    window.close();
                }


                //LEFT CLICK
                if (window.getInput().isMouseButtonDown(0)) {
                }

                //RIGHT CLICK
                if (window.getInput().isMouseButtonDown(1)) {
                }


                player.update((float) frame_cap, window, camera, world);
                //CHECK KEYS
                glfwPollEvents();

                if (frame_time >= 1.0) {
                    frame_time = 0;
//                    System.out.println("fps:" + frames);
                    frames = 0;
                }
            }
            if (can_render) {


                glClear(GL_COLOR_BUFFER_BIT);

                world.render(tiles, shader, camera);
                if (!maze_loaded) {



                    int[][] maze = mazeReq.getMap();

                    for (int x = 0; x < maze.length; x++) {
                        for (int y = 0; y < maze[0].length; y++) {
                            if (maze[x][y] == 1) {
                                world.setTile(Tile.wall, x, y);
                            }
                        }
                    }
                    maze_loaded = true;
                }



                player.render(shader, camera);



                glEnd();
                window.swapBuffers();
                frames++;
            }
        }
        glfwTerminate();
    }

    public static void main(String[] args) {
        new Main();
    }
}