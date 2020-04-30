package collision;

import org.joml.Vector2f;

public class AABB {
    private Vector2f center, half_extent;
    private int range = 10;

    public AABB(Vector2f center, Vector2f half_extent) {
        this.center = center;
        this.half_extent = half_extent;
    }

    public Collision getCollision(AABB box2) {
        Vector2f distance = box2.center.sub(center, new Vector2f());

        distance.x = Math.abs(distance.x);
        distance.y = Math.abs(distance.y);

        distance.sub(half_extent.add(box2.half_extent, new Vector2f()));

        return new Collision(distance, distance.x < -0.5 &&  distance.y < -0.5);
    }

    public Vector2f correctPosition(AABB box2, Collision data) {
//        System.out.println((float)center.x + " BOX1 AND " + (float)center.y );
//        System.out.println((float)box2.center.x + " BOX2 AND " + (float)box2.center.y );
        Vector2f correctionDistance = box2.center.sub(center, new Vector2f());
//        System.out.println((float)correctionDistance.x + " COR AND " + (float)correctionDistance.y );

        if (data.distance.x > data.distance.y) {
            if (correctionDistance.x > 0) {
                center.add(data.distance.x, 0);
            }
            else {
                center.add(-data.distance.x, 0);
            }
        }
        else {
            if (correctionDistance.y > 0) {
                center.add(0, data.distance.y);
            }
            else {
                center.add(0, -data.distance.y);
            }
        }
        return correctionDistance;
    }



    public Vector2f getCenter() {
        return center;
    }

    public Vector2f getHalfExtent() {
        return half_extent;
    }
}