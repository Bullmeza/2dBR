package entity;

import collision.AABBCollision;
import collision.CheckCollision;
import generate.World;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import render.Animation;
import render.Model;
import render.Shader;
import view.Camera;
import view.Window;

import java.util.Vector;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_A;

public class Player {
    private Model model;
    private Animation texture;
    private Transform transform;
    private AABBCollision collision_box;
    private double angle = 0;

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

        collision_box = new AABBCollision(new Vector2f(transform.pos.x, transform.pos.y), new Vector2f(1, 1));
    }

    public void update(float delta, Window window, Camera camera, World world) {


        Vector2f mousePos = window.getInput().getMousePosition();
        double anglePos = Math.atan2(mousePos.y, mousePos.x);
        angle = anglePos;


        if (window.getInput().isKeyDown(GLFW_KEY_W)) {
            transform.pos.add(new Vector3f(speed * (float) Math.cos(angle) * delta, speed * (float) Math.sin(angle) * delta, 0));

        }
        if (window.getInput().isKeyDown(GLFW_KEY_S)) {
            transform.pos.add(new Vector3f(-speed * (float) Math.cos(angle) * delta, -speed * (float) Math.sin(angle) * delta, 0));

        } if (window.getInput().isKeyDown(GLFW_KEY_A)) {

            if(angle < 0){
                transform.pos.add(new Vector3f(-speed * (float) Math.cos(angle + 1.5708 ) * delta, -speed * (float) Math.sin(angle + 1.5708) * delta, 0));
            }
            else{
                transform.pos.add(new Vector3f(speed * (float) Math.cos(angle + 1.5708 ) * delta, speed * (float) Math.sin(angle + 1.5708) * delta, 0));
            }

        } if (window.getInput().isKeyDown(GLFW_KEY_D)) {
            if(angle < 0){
                transform.pos.add(new Vector3f(speed * (float) Math.cos(angle + 1.5708 ) * delta, speed * (float) Math.sin(angle + 1.5708) * delta, 0));
            }
            else{
                transform.pos.add(new Vector3f(-speed * (float) Math.cos(angle + 1.5708 ) * delta, -speed * (float) Math.sin(angle + 1.5708) * delta, 0));
            }

        }


        collision_box.getCenter().set(transform.pos.x, transform.pos.y);

        AABBCollision[] boxes = new AABBCollision[25];

        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                boxes[i + j * 5] = world.getTileCollisionBox((int) (((transform.pos.x / 2) + 0.5f) - (5 / 2) + i), (int) (((-transform.pos.y / 2) + 0.5f) - (5 / 2) + j));
            }
        }
        AABBCollision box = null;
        for (int i = 0; i < boxes.length; i++) {
            if (boxes[i] != null) {
                if (box == null) {
                    box = boxes[i];

                    Vector2f length1 = box.getCenter().sub(transform.pos.x, transform.pos.y, new Vector2f());
                    Vector2f length2 = boxes[i].getCenter().sub(transform.pos.x, transform.pos.y, new Vector2f());

                    if (length1.lengthSquared() > length2.lengthSquared()) {
                        box = boxes[i];
                    }
                }
            }
        }

        if (box != null) {
            CheckCollision data = collision_box.getCollision(box);
            if (data.isIntersecting) {
                collision_box.correctPosition(box, data);
                transform.pos.set(collision_box.getCenter(), 0);
            }
        }
        camera.setPosition(transform.pos.mul(-1, new Vector3f()));
    }

    public void render(Shader shader, Camera camera) {
        shader.bind();
        shader.setUniform("sampler", 0);
        shader.setUniform("projection", (transform.getProjection(camera.getProjection())).rotateZ((float) angle));
        texture.bind(0);
        model.render();
    }
}
