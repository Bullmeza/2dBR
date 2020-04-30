package entity;

import collision.AABB;
import collision.Collision;
import generate.Tile;
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
import static org.lwjgl.glfw.GLFW.GLFW_KEY_A;

public class Player {
    private Model model;
    private Animation texture;
    private Transform transform;
    private AABB bounding_box;
    private double angle;
    private int RADIUS = 20;
    private boolean changedPos;

    private int speed = 200;

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
        changedPos = true;
        bounding_box = new AABB(new Vector2f(transform.pos.x, transform.pos.y), new Vector2f(1, 1));
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


        camera.setPosition(transform.pos.mul(-1, new Vector3f()));
        bounding_box.getCenter().set(transform.pos.x / world.getScale(), transform.pos.y / world.getScale());


        for (int i = -RADIUS; i <= RADIUS; i++) {
            for (int j = -RADIUS; j <= RADIUS; j++) {
                int x = Math.round(transform.pos.x / world.getScale()) + i;
                int y = Math.round(transform.pos.y/world.getScale()) + j;
                AABB tile = world.getTileCollisionBox(x, y);
//                System.out.println(x + " " + y);

                if (tile != null) {
                    while (bounding_box.getCollision(tile).isIntersecting) {
                        System.out.println(x + " " + y);
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
    }
}
