package generate;

//Robert Muresan
//2020/05/31
//2D Battle Royale
//Play with friends on a server/localhost

import collision.AABB;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import view.Camera;
import render.Shader;


public class World {
    private byte[] tiles;
    private AABB[] collision_boxes;

    private int width;
    private int height;
    private int scale;
    private int viewX;
    private int viewY;

    private Matrix4f world;

    public World() {
        width =10;
        height = width;
        scale = 64;

        viewX = 11;
        viewY= viewX;

        tiles = new byte[width * height];
        collision_boxes = new AABB[width * height];

        world = new Matrix4f().setTranslation(new Vector3f(0));
        world.scale(scale);
    }

    public void render(TileRender render, Shader shader, Camera cam) {

        int posX = (int) cam.getPosition().x / (scale * 2);
        int posY = (int) cam.getPosition().y / (scale * 2);

        for (int i = 0; i < viewX+3; i++) {
            for (int j = 0; j < viewY+2; j++) {
                Tile t = getTile(i - posX - (viewX / 2) + 1, j + posY - (viewY / 2));
                if (t != null) render.renderTile(t, i - posX - (viewX / 2) + 1, -j - posY + (viewY / 2), shader, world, cam);
            }
        }
    }

    public void setTile(Tile tile, int x, int y) {
        tiles[x + y * width] = tile.getId();
        if (tile.isSolid()) {
            collision_boxes[x + y * width] = new AABB(new Vector2f(x * 2, -y * 2), new Vector2f(1, 1));
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

    public AABB getTileCollisionBox(int x, int y) {
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
