import generate.Tile;
import generate.TileRender;
import generate.World;
import multiplayer.MazeReq;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL;
import render.*;

import java.io.IOException;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;


public class Main {

    long window;
    int width = 640;
    int height = 480;
    int speed = 3;



    public Main(){
        if (!glfwInit()) {
            System.err.println("Failed 2 create");
            System.exit(1);
        }


        window = glfwCreateWindow(width, height, "Game", 0, 0);
        glfwShowWindow(window);
        glfwMakeContextCurrent(window);
        glfwSetWindowAttrib(window, GLFW_RESIZABLE, GL_FALSE);
        GL.createCapabilities();
        glEnable(GL_TEXTURE_2D);


        Camera camera = new Camera(width,height);
        TileRender tiles = new TileRender();
        Shader shader = new Shader("shader");
        World world = new World();
        MazeReq mazeReq = new MazeReq();




        double frame_cap = 1.0 /60.0;
        double frame_time = 0;
        int frames = 0;
        double time = FPS.getTime();
        double unprocessed = 0;
        boolean maze_loaded = false;

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
                if (glfwGetKey(window, GLFW_KEY_W) == 1) {
                    camera.getPosition().sub(new Vector3f(0,speed,0));
                }
                if (glfwGetKey(window, GLFW_KEY_S) == 1) {
                    camera.getPosition().sub(new Vector3f(0,-speed,0));
                }
                if (glfwGetKey(window, GLFW_KEY_D) == 1) {
                    camera.getPosition().sub(new Vector3f(speed,0,0));
                }
                if (glfwGetKey(window, GLFW_KEY_A) == 1) {
                    camera.getPosition().sub(new Vector3f(-speed,0,0));
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

                world.render(tiles,shader,camera);


                if(!maze_loaded){
                    int[][] maze = mazeReq.get();

                    for(int x = 0; x < maze.length; x++){
                        for(int y = 0; y < maze[0].length; y++){
                            if(maze[x][y] == 1){
                                world.setTile(Tile.wall,x,y);
                            }
                        }
                    }
                    maze_loaded = true;
                }


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