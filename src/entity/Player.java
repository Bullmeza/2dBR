package entity;

//Robert Muresan
//2020/05/31
//2D Battle Royale
//Play with friends on a server/localhost

import collision.AABB;
import multiplayer.MazeReq;
import render.FPS;
import generate.World;
import org.joml.Vector2f;
import org.joml.Vector3f;
import render.Animation;
import render.Model;
import render.Shader;
import view.Camera;
import view.Window;

import java.util.ArrayList;

import static org.lwjgl.glfw.GLFW.*;

public class Player {
    private Model model;
    private Animation texture;
    private Transform transform;
    private AABB bounding_box;
    private double angle;
    private int RADIUS = 3;
    private int speed = 200;
    private ArrayList<Bullet> bullets;
    private FPS timer;
    private double last_shot = 0;
    private int shots = 0;
    private int player_id;
    private int health = 5;


    public Player() {
        float[] vertices = new float[]{
                -1f, 1f, 0,
                1f, 1f, 0,
                1f, -1f, 0,
                -1f, -1f, 0
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
        model = new Model(vertices, texture, indices);
        this.texture = new Animation(20, 20, "move/survivor-move_rifle");

        transform = new Transform();
        transform.scale = new Vector3f(64, 64, 1);
        angle = 0;
        timer = new FPS();

        bounding_box = new AABB(new Vector2f(transform.pos.x, transform.pos.y), new Vector2f(1, 1));

        bullets = new ArrayList<Bullet>();
    }

    public void update(float delta, Window window, Camera camera, World world) {
        Vector2f mousePos = window.getInput().getMousePosition();
        double anglePos = Math.atan2(mousePos.y, mousePos.x);
        angle = anglePos;
        if (window.getInput().isKeyDown(GLFW_KEY_W) && window.getInput().isKeyDown(GLFW_KEY_A)) {
            transform.pos.add(new Vector3f(speed * (float) Math.cos(angle + Math.toRadians(45)) * delta, speed * (float) Math.sin(angle + Math.toRadians(45)) * delta, 0));
        } else if (window.getInput().isKeyDown(GLFW_KEY_W) && window.getInput().isKeyDown(GLFW_KEY_D)) {
            transform.pos.add(new Vector3f(-speed * (float) Math.cos(angle + Math.toRadians(45)) * delta, speed * (float) Math.sin(angle + Math.toRadians(45)) * delta, 0));
        } else if (window.getInput().isKeyDown(GLFW_KEY_S) && window.getInput().isKeyDown(GLFW_KEY_A)) {
            transform.pos.add(new Vector3f(speed * (float) Math.cos(angle + Math.toRadians(45)) * delta, -speed * (float) Math.sin(angle + Math.toRadians(45)) * delta, 0));
        } else if (window.getInput().isKeyDown(GLFW_KEY_S) && window.getInput().isKeyDown(GLFW_KEY_D)) {
            transform.pos.add(new Vector3f(-speed * (float) Math.cos(angle + Math.toRadians(45)) * delta, -speed * (float) Math.sin(angle + Math.toRadians(45)) * delta, 0));
        } else if (window.getInput().isKeyDown(GLFW_KEY_W)) {
            transform.pos.add(new Vector3f(speed * (float) Math.cos(angle) * delta, speed * (float) Math.sin(angle) * delta, 0));
        } else if (window.getInput().isKeyDown(GLFW_KEY_S)) {
            transform.pos.add(new Vector3f(-speed * (float) Math.cos(angle) * delta, -speed * (float) Math.sin(angle) * delta, 0));
        } else if (window.getInput().isKeyDown(GLFW_KEY_A)) {
            transform.pos.add(new Vector3f(speed * (float) Math.cos(angle + Math.toRadians(90)) * delta, speed * (float) Math.sin(angle + Math.toRadians(90)) * delta, 0));
        } else if (window.getInput().isKeyDown(GLFW_KEY_D)) {
            transform.pos.add(new Vector3f(-speed * (float) Math.cos(angle + Math.toRadians(90)) * delta, -speed * (float) Math.sin(angle + Math.toRadians(90)) * delta, 0));
        }
        if (window.getInput().isMouseButtonDown(0)) {
            if(shots == 15){
                last_shot += 2.5;
                shots = 0;
                texture = new Animation(20, (int) (15/2.5), "reload/survivor-reload_rifle");
            }
            if(FPS.getTime()-last_shot > 0.33) {
                if(shots == 0){
                    texture = new Animation(20, 20, "move/survivor-move_rifle");
                }

                bullets.add(new Bullet(mousePos, transform.pos, player_id));
                MazeReq.bulletWrite(player_id, transform.pos.x, transform.pos.y, angle);

                last_shot = FPS.getTime();
                shots++;
            }
        }
        for(int i = 0; i < bullets.size(); i++){
            Bullet current = bullets.get(i);


            if(current.getBounding_box().getCollision(bounding_box).isIntersecting && current.getPlayer_shot_id() != player_id){
                health--;
                System.out.println(health);
                if(health == 0){
                    System.out.println("DEAD");
                    System.exit(1);
                }
                current.setHit_wall();
            }
                current.updateBullet(delta, world);

                int[] x_pos = current.getTilePosX(world);
                int[] y_pos = current.getTilePosY(world);


                for(int x = 0; x < 2; x++){
                    for(int y = 0; y < 2; y++){
//                        System.out.println(x_pos[x] + " " + y_pos[y]);
                        AABB tile = world.getTileCollisionBox(x_pos[x], y_pos[y]);
                        if(tile != null){
                            if(current.getBounding_box().getCollision(tile).isIntersecting){
                                current.setHit_wall();
                            }
                        }
                    }
                }
        }


        camera.setPosition(transform.pos.mul(-1, new Vector3f()));
        bounding_box.getCenter().set(transform.pos.x / world.getScale(), transform.pos.y / world.getScale());



        for (int i = -RADIUS; i <= RADIUS; i++) {
            for (int j = -RADIUS; j <= RADIUS; j++) {
                AABB tile = world.getTileCollisionBox((int) (i+Math.abs(transform.pos.x/world.getScale()/2)), (int) (j+Math.abs(transform.pos.y/world.getScale()/2)));
             if (tile != null) {
                    while (bounding_box.getCollision(tile).isIntersecting) {
                        Vector2f fix = bounding_box.correctPosition(tile, bounding_box.getCollision(tile));
                        transform.pos.x -= fix.x;
                        transform.pos.y -= fix.y;
                        bounding_box.getCenter().set(transform.pos.x / world.getScale(), transform.pos.y / world.getScale());
                    }
                }

            }
        }
    }

    public void render(Shader shader, Camera camera) {
        shader.bind();
        shader.setUniform("sampler", 0);
        shader.setUniform("projection", (transform.getProjection(camera.getProjection())).rotateZ((float) angle));
        texture.bind(0);
        model.render();
        for(int i = 0; i < bullets.size(); i++){
            if(FPS.getTime()-bullets.get(i).getSpawn_time() > 4){
                bullets.get(i).unrender(shader,camera);
                bullets.remove(i);
                continue;
            }
            else if(bullets.get(i).getHit_wall()){
                bullets.get(i).unrender(shader,camera);
                bullets.remove(i);
                continue;
            }
            bullets.get(i).render(shader, camera);
        }
    }
    public void unrender(Shader shader, Camera camera) {
        texture.delete();
    }

    public void move(Vector2f newPos, World world) {
        transform.pos = (new Vector3f(newPos.x * world.getScale(), newPos.y * world.getScale(), 1));
    }
    public void move(Vector2f newPos, World world, double new_angle) {
        transform.pos = (new Vector3f(newPos.x * world.getScale(), newPos.y * world.getScale(), 1));
        angle = new_angle;
    }
    public void setPlayer_id(int new_id){
        player_id = new_id;
    }
    public int getPlayer_id(){
        return player_id;
    }

    public Vector2f getPlayerPosition(){
        return new Vector2f(transform.pos.x, transform.pos.y);
    }
    public double getAngle(){
        return angle;
    }

    public void addBullet(Bullet bullet){
        bullets.add(bullet);
    }
}
