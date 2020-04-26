package generate;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import view.Camera;
import render.Shader;

public class World {
    private byte[] tiles;
    private int width;
    private int height;
    private int scale;

    private Matrix4f world;

    public World(){
        width=50;
        height=width;
        scale=64;

        tiles = new byte[width*height];

        world = new Matrix4f().setTranslation(new Vector3f(0));
        world.scale(scale);
    }
    public void render(TileRender render, Shader shader, Camera cam){
        for(int i = 0; i < height; i++){
            for(int j = 0; j <width; j++){
                render.renderTile(tiles[j + i * width], j, -i,shader, world,cam);
            }
        }
    }

    public void setTile(Tile tile, int x, int y){
        tiles[x + y * width] = tile.getId();
    }

    public int getWidth(){
        return width;
    }
    public int getHeight(){
        return height;
    }

    public Tile getTile(int x, int y) {
        try {
            return Tile.tiles[tiles[x + y * width]];
        }
        catch (ArrayIndexOutOfBoundsException e) {
            return null;
        }
    }

    public float getScale() {
        return scale;
    }
}
