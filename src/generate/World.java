package generate;

import collision.AABBCollision;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import view.Camera;
import render.Shader;

import java.util.Collections;

public class World {
    private byte[] tiles;
    private AABBCollision[] collision_boxes;

    private int width;
    private int height;
    private int scale;

    private Matrix4f world;

    public World() {
        width = 50;
        height = width;
        scale = 64;

        tiles = new byte[width * height];
        collision_boxes = new AABBCollision[width * height];

        world = new Matrix4f().setTranslation(new Vector3f(0));
        world.scale(scale);
    }

    public void render(TileRender render, Shader shader, Camera cam) {
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                render.renderTile(tiles[j + i * width], j, -i, shader, world, cam);
            }
        }
    }

    public void setTile(Tile tile, int x, int y) {
        tiles[x + y * width] = tile.getId();
        if (tile.isSolid()) {
            collision_boxes[x + y * width] = new AABBCollision(new Vector2f(x * 2, -y * 2), new Vector2f(1, 1));
        } else {
            collision_boxes[x + y * width] = null;
        }
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Tile getTile(int x, int y) {
        try {
            return Tile.tiles[tiles[x + y * width]];
        } catch (ArrayIndexOutOfBoundsException e) {
            return null;
        }
    }

    public AABBCollision getTileCollisionBox(int x, int y) {
        try {
            return collision_boxes[x + y * width];
        } catch (ArrayIndexOutOfBoundsException e) {
            return null;
        }
    }

    public float getScale() {
        return scale;
    }
}
