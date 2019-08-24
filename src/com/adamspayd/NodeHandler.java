package com.adamspayd;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Class to manage all 'nodes'
 * e.i. this is where the nodes are processed to find the best path
 */
public class NodeHandler {

    private int width;
    private int height;
    private int scale;

    private ArrayList<Node> nodes;
    private Node start;
    private Node stop;

    private ArrayList<Node> path;

    private ArrayList<Node> solution;

    public NodeHandler(int width, int height, int scale) {

        this.width = width;
        this.height= height;
        this.scale = scale;

        // Generate the nodes
        nodes = new ArrayList<>();
        if(!generateNodes(nodes)) {
            System.out.println("Failed to generate the nodes");
        }

        // Set the walls
        int[] indexes = {
                nodes.indexOf(new Node(125, 150, false)),
                nodes.indexOf(new Node(150, 150, false)),
                nodes.indexOf(new Node(175, 150, false)),
        };

        for(int idx : indexes) {
            nodes.get(idx).setIsBoundary(true);
        }

        // Create start and stop nodes
        start = new Node(0, 0, false);
        stop = new Node(nodes.get(nodes.size() - 1).getX(), nodes.get(nodes.size() - 1).getY(), false);

        path = new ArrayList<>();
        findNodeCosts(nodes);

        solution = new ArrayList<>();
        astar();
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
                this.path.add(node);
            }
        }

        return true;
    }

    public void astar() {
        ArrayList<Node> open_list = new ArrayList<>(path.size());
        ArrayList<Node> closed_list = new ArrayList<>();

        open_list.add(start);
        boolean end = false;
        while(!open_list.isEmpty() && !end) {
            // Follow this tutorial: https://www.geeksforgeeks.org/a-search-algorithm/

            Collections.sort(open_list);
            Node q = open_list.get(0);

            open_list.remove(open_list.indexOf(q));

            q.findNeighbors(path, this.scale);

            System.out.println("closed_list: " + closed_list);

            for (Node neighbor : q.getNeighbors()) {
                // Check to see if the goal has been reached
                if(neighbor.equals(stop)) {
                    // Stop the search
                    end = true;
                    closed_list.add(q);
                    break;
                }

                neighbor.setG((q.getG() + neighbor.diagonalDistance(neighbor.getX(), neighbor.getY(), q.getX(), q.getY())) / this.scale);
                neighbor.setH(neighbor.diagonalDistance(neighbor.getX(), neighbor.getY(), stop.getX(), stop.getY()) / this.scale);
                neighbor.setF(neighbor.getH(), neighbor.getG());

                // If there is a better node in `open_list`, skip this neighbor
                int idx = open_list.indexOf(neighbor);
                if(idx >= 0) {
                    // There is a match, check if the 'f' value is better (lower)
                    if(open_list.get(idx).getF() < neighbor.getF()) {
                        // Skip the current neighbor
                        continue;
                    }
                }

                // If there is a better node in `closed_list`, skip this neighbor
                idx =  closed_list.indexOf(neighbor);
                if(idx >= 0) {
                    // There is a match, check if the 'f' value is better (lower)
                    if(closed_list.get(idx).getF() < neighbor.getF()) {
                        // Skip the current neighbor
                        continue;
                    }
                }
                open_list.add(neighbor);
            }

            if(end == true) {
                break;
            }

            // Add q to the closed_list
            closed_list.add(q);
        }

        Main.drawGrid = true;
        solution = closed_list;
        System.out.println(closed_list);
    }

    /**
     * Renders the nodes in colors according to their type (start, stop, boundary)
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

        g.setColor(Color.lightGray);
        for(Node n : solution) {
            if(!n.equals(start)) {
                g.fillRect(n.getX(), n.getY(), this.scale, this.scale);
            }
        }

        g.setFont(new Font("arial", Font.PLAIN, 10));
        g.setColor(Color.gray);
        for(Node node : path) {
            g.drawString(Double.toString(Math.round(node.getF() * 10.0) / 10.0), (node.getX() + this.scale/7), (node.getY() + this.scale * 2/3));
        }
    }
}
