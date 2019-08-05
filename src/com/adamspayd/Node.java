package com.adamspayd;

import java.util.ArrayList;
import java.util.Comparator;

/**
 * Pathfinder
 *
 * @author Adam
 * @since 8/1/2019
 */
public class Node implements Comparable, Comparator<Node> {

    @Override
    public int compareTo(Object o) {
        if(this.equals(o)) {
            return 0;
        }

        Node n = (Node) o;

//        return this.getF() < n.getF() ? -1 : 1;
        return Double.compare(this.getF(), n.getF());
    }

    @Override
    public int compare(Node o1, Node o2) {
        if(o1.getF() == o2.getF()) {
            return 0;
        }

        return o1.getF() < o2.getF() ? -1 : 1;
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) {
            return true;
        }

        if(!(o instanceof Node)) {
            return false;
        }

        Node n = (Node) o;

        return (
                this.getX() == n.getX() &&
                this.getY() == n.getY() &&
                this.getIsBoundary() == n.getIsBoundary()
        );
    }

    private int x;
    private int y;

    private double f;
    private double h;
    private double g;

    private boolean isBoundary;

    private ArrayList<Node> neighbors;
    private Node parent;

    public Node getParent() {
        return parent;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public Node(int x, int y, boolean isboundary) {
        this.x = x;
        this.y = y;
        this.isBoundary = isboundary;

        neighbors = new ArrayList<>();
    }

    public double diagonalDistance(int x1, int y1, int x2, int y2) {
        return Math.max(Math.abs(x2 - x1), Math.abs(y2 - y1));
    }

    public double distance(int x1, int y1, int x2, int y2) {
        return Math.sqrt(Math.pow((x2 - x1), 2) + Math.pow((y2 - y1), 2));
    }

    public void findNeighbors(ArrayList<Node> nodes, int scale) {
        int[] indexes = {
                nodes.indexOf(new Node(x - scale, y - scale, false)),
                nodes.indexOf(new Node(x - scale, y, false)),
                nodes.indexOf(new Node(x - scale, y + scale, false)),
                nodes.indexOf(new Node(x, y - scale, false)),
                nodes.indexOf(new Node(x, y + scale, false)),
                nodes.indexOf(new Node(x + scale, y - scale, false)),
                nodes.indexOf(new Node(x + scale, y, false)),
                nodes.indexOf(new Node(x + scale, y + scale, false))
        };

        for (int idx : indexes) {
            if(idx >= 0) {
                if(nodes.get(idx).getX() == 0 && nodes.get(idx).getY() == 0) {
                    continue;
                }
                neighbors.add(nodes.get(idx));
            }
        }
    }

    public ArrayList<Node> getNeighbors() {
        return neighbors;
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

    public void setF(double heuristic, double g) {
        this.f = heuristic + g;
    }

    public double getH() {
        return h;
    }

    public void setH(double h) {
        this.h = h;
    }

    public double getG() {
        return g;
    }

    public void setG(double g) {
        this.g = g;
    }
}
