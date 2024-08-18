package shining.starj.lostSurvival.Effects;

import org.bukkit.Location;
import org.bukkit.util.Vector;

//RelativeLocation
public class RLocation {
    /*
     * 상대위치 구하기
     */
    public static Location getLocation(Location start, Vector dir, double size) {
        return getLocation(start, dir, size, size, size);
    }

    public static Location getLocation(Location start, Vector dir, double size, double up) {
        return getLocation(start, dir, size, up, size);
    }

    public static Location getLocation(Location start, Vector dir, double front, double up, double right) {
        dir = dir.clone().setY(0).normalize();
        //(x,y)
        // (y,-x)

        return start.clone().add(dir.getX() * front - dir.getZ() * right, up, dir.getZ() * front + dir.getX() * right);
    }

    public static Location getLocation(Location start, double front, double up, double right) {
        return getLocation(start, start.getDirection(), front, up, right);
    }

    /*
     * 계산된 벡터 구하기
     */
    public static Vector getRelativeVector(Location start, Vector dir, double size) {
        return getRelativeVector(start, dir, size, size, size);
    }

    public static Vector getRelativeVector(Location start, Vector dir, double dxz, double up) {
        return getRelativeVector(start, dir, dxz, up, dxz);
    }

    public static Vector getRelativeVector(Location start, Vector dir, double front, double up, double right) {
        dir = dir.clone().setY(0).normalize();
        return new Vector((dir.getX() - dir.getZ()) * front, up, (dir.getZ() - dir.getX()) * right);
    }

    public static Vector getRelativeVector(Location start, double front, double up, double right) {
        return getRelativeVector(start, start.getDirection(), front, up, right);
    }

    /*
     * 계산된 벡터 사용
     */
    public static Location getLocation(Location start, double size, Vector dir) {
        return getLocation(start, size, size, size, dir);
    }

    public static Location getLocation(Location start, double dxz, double up, Vector dir) {
        return getLocation(start, dxz, up, dxz, dir);
    }

    public static Location getLocation(Location start, double front, double up, double right, Vector dir) {
        return start.clone().add(dir.getX() * front, dir.getY() * up, dir.getZ() * right);
    }
}
