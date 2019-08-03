package com.adamspayd;

/**
 * Pathfinder
 *
 * @author Adam
 * @since 8/1/2019
 */
public class Node implements Comparable<Node> {

    @Override
    public int compareTo(Node n) {
        if(this.getX() == n.getX() && this.getY() == n.getY()) {
            return 0;
        }
        return -1;
    }

    private int x;
    private int y;
    private double f;
    private boolean isBoundary;

    public Node(int x, int y, boolean isboundary) {
        this.x = x;
        this.y = y;
        this.isBoundary = isboundary;
    }

    private double distance(int x1, int y1, int x2, int y2) {
        return Math.sqrt(Math.pow((x2 - x1), 2) + Math.pow((y2 - y1), 2));
    }

    public double heuristic(int stopX, int stopY) {
        // Diagonal distance formula (h = max( abs(node.x - goal.x), abs(node.y - goal.y) )
        return Math.max(Math.abs(this.x - stopX), Math.abs(this.y - stopY));
    }

    public double cost(int startX, int startY) {
        return distance(x, y, startX, startY);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean getIsBoundary() {
        return isBoundary;
    }

    public void setIsBoundary(boolean boundary) {
        this.isBoundary = boundary;
    }

    public double getF() {
        return f;
    }

    public void setF(double heuristic, double cost) {
        this.f = heuristic + cost;
    }
}
