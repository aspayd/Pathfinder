package com.adamspayd;

import java.awt.*;
import java.util.List;

/**
 * Class to manage all 'nodes'
 * e.i. this is where the nodes are processed to find the best path
 */
public class NodeHandler {

    private int width;
    private int height;
    private int scale;

    private List<Node> nodes;
    private List<Node> boundaries;
    private Node start;
    private Node stop;

    private List<Node> path;

    public NodeHandler(int width, int height, int scale) {

        this.width = width;
        this.height= height;
        this.scale = scale;

        // @todo: add a method to do this in the `NodeList` class
        boundaries = new NodeList<>();
        boundaries.add(new Node(100, 150, true));
        boundaries.add(new Node(125, 150, true));
        boundaries.add(new Node(150, 150, true));
        boundaries.add(new Node(175, 150, true));
        boundaries.add(new Node(175, 125, true));

        // Generate the nodes
        nodes = new NodeList<>();
        if(!generateNodes(nodes)) {
            System.out.println("Failed to generate the nodes");
        }

        start = new Node(0, 0, false);
        stop = new Node(nodes.get(nodes.size() - 1).getX(), nodes.get(nodes.size() - 1).getY(), false);

        path = new NodeList<>();
        findNodeCosts(nodes);
    }

    /**
     * Generate a list of nodes for each cell on the grid
     * If the node is matched with a set boundary, it will be marked as a boundary
     *
     * @param nodes             The list to add the nodes into
     * @return                  Whether or not the nodes were added
     */
    public boolean generateNodes(List<Node> nodes) {

        if(!nodes.isEmpty()) {
            System.out.println("Please provide an empty list before generating nodes");
            return false;
        }

        for(int y = 0; y < this.height - (this.scale); y++) {
            for(int x = 0; x < this.width - (this.scale); x++) {

                if(y % (this.scale) == 0 && x % (this.scale) == 0) {
                    nodes.add(new Node(x, y, false));
                }
            }
        }

        for(Node node : nodes) {
            for(Node boundary : boundaries) {
                if(node.getX() == boundary.getX() && node.getY() == boundary.getY()) {
                    node.setIsBoundary(true);
                }
            }
        }

        return true;
    }

    /**
     * Finds the best path between the start and stop points based on the weighted values of each node cell
     *
     * @param nodes
     * @return
     */
    public boolean findNodeCosts(List<Node> nodes) {

        if(nodes.isEmpty()) {
            System.out.println("No nodes were provided.");
            return false;
        }

        for(Node node : this.nodes) {
            if(!node.getIsBoundary()) {
                node.setF(node.heuristic(stop.getX(), stop.getY()) / this.scale, node.cost(start.getX(), start.getY()) / this.scale);
                this.path.add(node);
            }
        }

        return true;
    }

    public void tick() {
        NodeList<Node> open_list = new NodeList<>();
        NodeList<Node> closed_list = new NodeList<>();

        while(!open_list.isEmpty()) {
            // Follow this tutorial: https://www.geeksforgeeks.org/a-search-algorithm/
        }
    }

    /**
     * Renders the nodes in colors according to their type (start, stop, boundary)
     *
     */
    public void render(Graphics g) {
        for(Node node : nodes) {

            Color color = (node.getIsBoundary()) ? Color.black : Color.white;

            if(node.getX() == start.getX() && node.getY() == start.getY()) {
                color = Color.green;
            } else if(node.getX() == stop.getX() && node.getY() == stop.getY()) {
                color = Color.red;
            }

            g.setColor(color);
            g.fillRect(node.getX(), node.getY(), this.scale, this.scale);

            g.setColor(Color.gray);
            g.drawRect(node.getX(), node.getY(), this.scale, this.scale);
        }

        g.setFont(new Font("arial", Font.PLAIN, 10));
        for(Node node : path) {
            g.drawString(Double.toString(Math.round(node.getF() * 10.0) / 10.0), (node.getX() + this.scale/7), (node.getY() + this.scale * 2/3));
        }
    }
}
