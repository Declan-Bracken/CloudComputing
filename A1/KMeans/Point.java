package KMeans;

public class Point {
    private double x;
    private double y;

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    // Getters and Setters
    public double getX() { return x; }
    public double getY() { return y; }
    public void setX(double x) { this.x = x; }
    public void setY(double y) { this.y = y; }

    // Utility method to calculate distance to another point
    public double distance(Point other) {
        return Math.sqrt(Math.pow(this.x - other.x, 2) + Math.pow(this.y - other.y, 2));
    }

    // Method to parse a Point from a String
    public static Point parse(String str) {
        // Split the string by either a comma or a space.
        String[] parts = str.split("[,\\s]+");
        return new Point(Double.parseDouble(parts[0]), Double.parseDouble(parts[1]));
    }

    @Override
    public String toString() {
        return x + "," + y;
    }
}
