//Robert Muresan
//2020/05/31
//2D Battle Royale
//Play with friends on a server/localhost


import entity.Bullet;
import entity.Player;
import generate.Tile;
import generate.TileRender;
import generate.World;
import multiplayer.MazeReq;
import org.joml.Random;
import org.joml.Vector2f;
import org.lwjgl.opengl.GL;
import render.*;
import view.Camera;
import view.Window;
import render.FPS;


import java.util.ArrayList;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;


public class Main {
    public static ArrayList<Player> players = new ArrayList<Player>();


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

        int id = Integer.parseInt(mazeReq.createMap(world.getWidth()).split(":")[1]);
        System.out.println(id);
        player.setPlayer_id(id);

        String[] players_info;
        String[] bullets_info;


        Random rand = new Random();

        int randX = (rand.nextInt(world.getWidth() - 2) + 1) * 2;
        int randY = (rand.nextInt(world.getHeight() - 2) + 1) * 2;


        player.move(new Vector2f(randX, -randY), world);


        double frame_cap = 1.0 / 35.0;
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


                player.update((float) frame_cap, window, camera, world);

                players_info = mazeReq.playerWrite(id, player.getPlayerPosition().x, player.getPlayerPosition().y, player.getAngle());
                for (int i = 0; i < players_info.length / 4; i++) {
                    int id_current = Integer.parseInt(players_info[i * 4].split(":")[1]);
                    float x = Float.parseFloat(players_info[i * 4 + 1].split(":")[1]);
                    float y = Float.parseFloat(players_info[i * 4 + 2].split(":")[1]);
                    double angle = Double.parseDouble(players_info[i * 4 + 3].split(":")[1]);

                    int count = 0;

                    for (int j = 0; j < players.size(); j++) {
                        if (id_current == players.get(j).getPlayer_id()) {
                            players.get(j).move(new Vector2f(x / world.getScale(), y / world.getScale()), world, angle);
                            continue;
                        }
                        count++;
                    }
                    if (count == players.size()) {
                        if (id_current != id) {
                            players.add(new Player());
                            players.get(players.size() - 1).move(new Vector2f(x / world.getScale(), y / world.getScale()), world, angle);
                            players.get(players.size() - 1).setPlayer_id(id_current);
                            System.out.println(new Vector2f(x, y));
                        }
                    }
                }
                bullets_info = mazeReq.bulletRead(id);
                if (bullets_info != null) {
                    for (int i = 0; i < bullets_info.length / 4; i++) {
                        int player_shot = Integer.parseInt(bullets_info[i * 4].split(":")[1]);
                        float x = Float.parseFloat(bullets_info[i * 4 + 1].split(":")[1]);
                        float y = Float.parseFloat(bullets_info[i * 4 + 2].split(":")[1]);
                        double angle = Double.parseDouble(bullets_info[i * 4 + 3].split(":")[1]);


                        if(player_shot != id){
                            player.addBullet((new Bullet(player_shot, x, y, angle)));

                        }

                    }
                }




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
//                System.out.println(players.size());
                for (int i = 0; i < players.size(); i++) {
                    players.get(i).render(shader, camera);
                }

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