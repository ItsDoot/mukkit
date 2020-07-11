package pw.dotdash.mukkit.util;

public final class MathUtil {
    
    public static double clamp(double n, double min, double max) {
        if (n < min) {
            return min;
        } else if (n > max) {
            return max;
        } else {
            return n;
        }
    }

    public static float clamp(float n, float min, float max) {
        if (n < min) {
            return min;
        } else if (n > max) {
            return max;
        } else {
            return n;
        }
    }

    public static int clamp(int n, int min, int max) {
        if (n < min) {
            return min;
        } else if (n > max) {
            return max;
        } else {
            return n;
        }
    }

    public static long clamp(long n, long min, long max) {
        if (n < min) {
            return min;
        } else if (n > max) {
            return max;
        } else {
            return n;
        }
    }

    private MathUtil() {
    }
}