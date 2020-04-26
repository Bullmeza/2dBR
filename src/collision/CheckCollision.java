package collision;

import org.joml.Vector2f;

public class CheckCollision {
    public Vector2f distance;
    public boolean isIntersecting;

    public CheckCollision(Vector2f distance, boolean intersects) {
        this.distance = distance;
        this.isIntersecting = intersects;
    }
}