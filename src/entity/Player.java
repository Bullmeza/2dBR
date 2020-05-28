package entity;

import collision.AABB;
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
    private int RADIUS = 30;
    private int speed = 200;
    private ArrayList<Bullet> bullets;
    private FPS timer;
    private double last_shot = 0;
    private double last_reload = 0;
    private int shots = 0;


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
            if(shots == 20){
                last_shot += 2.5;
                shots = 0;
                texture = new Animation(20, (int) (20/2.5), "reload/survivor-reload_rifle");
            }
            if(FPS.getTime()-last_shot > 0.25) {
                if(shots == 0){
                    texture = new Animation(20, 20, "move/survivor-move_rifle");
                }
                bullets.add(new Bullet(mousePos, transform.pos));
                last_shot = FPS.getTime();
                shots++;
            }
        }
        for(int i = 0; i < bullets.size(); i++){
                bullets.get(i).updateBullet(delta, world);
        }
        System.out.println(bullets.size());




        camera.setPosition(transform.pos.mul(-1, new Vector3f()));
        bounding_box.getCenter().set(transform.pos.x / world.getScale(), transform.pos.y / world.getScale());



        for (int i = 0; i <= RADIUS; i++) {
            for (int j = -RADIUS; j <= RADIUS; j++) {
                AABB tile = world.getTileCollisionBox(i, j);
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
//        for (int i = -5; i <= RADIUS; i++) {
//            for (int j = 5; j >= -RADIUS; j--) {
//
//                AABB tile = world.getTileCollisionBox(i, j);
//
//                if (tile != null) {
//                    System.out.println(i + " " + j);
//                    while (bounding_box.getCollision(tile).isIntersecting) {
//                        Vector2f fix = bounding_box.correctPosition(tile, bounding_box.getCollision(tile));
//                        transform.pos.x -= fix.x;
//                        transform.pos.y -= fix.y;
//                        bounding_box.getCenter().set(transform.pos.x / world.getScale(), transform.pos.y / world.getScale());
//                    }
//                }
//
//            }
//        }


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
            bullets.get(i).render(shader, camera);
        }
    }

    public void move(Vector2f newPos, World world) {
        transform.pos = (new Vector3f(newPos.x * world.getScale(), newPos.y * world.getScale(), 1));
    }
}
