package generate;

//Robert Muresan
//2020/05/31
//2D Battle Royale
//Play with friends on a server/localhost

public class Tile {
    public static Tile tiles[] = new Tile[16];
    public static final Tile grass = new Tile((byte) 0, "grass");
    public static final Tile wall = new Tile((byte) 1, "wall").setSolid();
    public static final Tile rock = new Tile((byte) 2, "rock");

    private byte id;
    private String texture;
    private boolean solid;

    public Tile(byte id, String texture) {
        this.id = id;
        this.texture = texture;
        this.solid = false;
        if (tiles[id] != null) {
            throw new IllegalStateException("Tiles at [" + id + "] is already being used!");
        }
        tiles[id] = this;
    }


    public byte getId() {
        return id;
    }

    public String getTexture() {
        return texture;
    }

    public Tile setSolid() {
        this.solid = true;
        return this;
    }

    public boolean isSolid() {
        return solid;
    }
}