package entity;

import generate.World;
import org.joml.Vector3f;
import render.Animation;
import render.Model;
import render.Shader;
import render.Texture;
import view.Camera;
import view.Window;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_A;

public class Player {
    private Model model;
    private Animation texture;
    private Transform transform;
    private int speed = 200;

    public Player(){
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
        this.texture = new Animation(20,20,"move/survivor-move_rifle");

        transform = new Transform();
        transform.scale = new Vector3f(64,64,1);
    }
    public void update(float delta, Window window, Camera camera, World world){
        if (window.getInput().isKeyDown(GLFW_KEY_W)) {
            transform.pos.add(new Vector3f(0,speed*delta,0));
        }
        if (window.getInput().isKeyDown(GLFW_KEY_S)) {
            transform.pos.add(new Vector3f(0,-speed*delta,0));
        }
        if (window.getInput().isKeyDown(GLFW_KEY_D)) {
            transform.pos.add(new Vector3f(speed*delta,0,0));
        }
        if (window.getInput().isKeyDown(GLFW_KEY_A)) {
            transform.pos.add(new Vector3f(-speed*delta,0,0));
        }
        camera.setPosition(transform.pos.mul(-1, new Vector3f()));


    }
    public void render(Shader shader, Camera camera){

        shader.bind();
        shader.setUniform("sampler", 0);
        shader.setUniform("projection", transform.getProjection(camera.getProjection()));
        texture.bind(0);
        model.render();

    }

}
