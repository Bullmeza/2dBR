package generate;

//Robert Muresan
//2020/05/31
//2D Battle Royale
//Play with friends on a server/localhost

import java.util.HashMap;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import view.Camera;
import render.Model;
import render.Shader;
import render.Texture;

import static org.lwjgl.opengl.GL11.*;


public class TileRender {
    private HashMap<String, Texture> tileTextures;
    private Model model;


    public TileRender() {
        tileTextures = new HashMap<String, Texture>();

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

        for (int i = 0; i < Tile.tiles.length; i++) {
            if(Tile.tiles[i] != null){
                if (!tileTextures.containsKey(Tile.tiles[i].getTexture())) {
                    String tex = Tile.tiles[i].getTexture();
                    tileTextures.put(tex, new Texture(tex + ".png"));
                }
            }
        }
    }

    public void renderTile(Tile tile, int x, int y, Shader shader, Matrix4f world, Camera cam) {
        shader.bind();
        if (tileTextures.containsKey(tile.getTexture())) tileTextures.get(tile.getTexture()).bind(0);

        Matrix4f tile_pos = new Matrix4f().translate(new Vector3f(x * 2, y * 2, 0));
        Matrix4f target = new Matrix4f();

        cam.getProjection().mul(world, target);
        target.mul(tile_pos);

        shader.setUniform("sampler", 0);
        shader.setUniform("projection", target);

        model.render();
    }

}