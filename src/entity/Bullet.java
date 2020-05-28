package entity;

import collision.AABB;
import generate.World;
import org.joml.Vector2f;
import org.joml.Vector3f;
import render.FPS;
import render.Model;
import render.Shader;
import render.Texture;
import view.Camera;

public class Bullet {
    private Model model;
    private Texture texture;
    private Transform bullet_transform;
    private AABB bounding_box;
    private double angle;
    private int speed = 300;
    private double spawn_time;
    private Vector3f playerPosition;


    public Bullet(Vector2f mousePos, Vector3f playerPos) {
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
        this.texture = new Texture("bullet.png");

        bullet_transform = new Transform();
        bullet_transform.scale = new Vector3f(14, 10, 1);

        spawn_time = FPS.getTime();

        bullet_transform.pos.add(playerPos);
        angle = Math.atan2(mousePos.y, mousePos.x);

        playerPosition = playerPos;
        bounding_box = new AABB(new Vector2f(bullet_transform.pos.x, bullet_transform.pos.y), new Vector2f(1, 1));
    }

    public void updateBullet(float delta, World world) {
        bullet_transform.pos.add(new Vector3f(speed * (float) Math.cos(angle) * delta, speed * (float) Math.sin(angle) * delta, 0));

        bounding_box.getCenter().set(bullet_transform.pos.x / world.getScale(), bullet_transform.pos.y / world.getScale());
    }

    public void render(Shader shader, Camera camera) {
        shader.bind();
        shader.setUniform("sampler", 0);
        shader.setUniform("projection", (bullet_transform.getProjection(camera.getProjection())).rotateZ((float) angle));
        texture.bind(0);
        model.render();
    }

    public void unrender(Shader shader, Camera camera) {
        texture.delete();
    }

    public double getSpawn_time() {
        return spawn_time;
    }
    public AABB getBounding_box(){
        return bounding_box;
    }

}
