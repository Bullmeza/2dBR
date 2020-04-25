package generate;

public class Tile {
    public static Tile tiles[] = new Tile[16];
    public static final Tile grass = new Tile((byte)0, "grass");
    public static final Tile wall = new Tile((byte)1, "wall");

    private byte id;
    private String texture;

    public Tile(byte id, String texture) {
        this.id = id;
        this.texture = texture;
        if (tiles[id] != null) {throw new IllegalStateException("Tiles at [" + id + "] is already being used!");}
        tiles[id] = this;
    }


    public byte getId() {
        return id;
    }

    public String getTexture() {
        return texture;
    }
}