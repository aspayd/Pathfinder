package com.adamspayd;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Pathfinder
 *
 * @author Adam
 * @since 6/26/2019
 */
public class WallHandler {

    private int width;
    private int height;
    private int scale;

    private List<Wall> walls;
    private List<Wall> boundaries;
    private Wall start;
    private Wall stop;

    private List<Wall> path;

    public WallHandler(int width, int height, int scale) {

        this.width = width;
        this.height= height;
        this.scale = scale;

        boundaries = new ArrayList<>();
        boundaries.add(new Wall(100, 150, true));
        boundaries.add(new Wall(125, 150, true));
        boundaries.add(new Wall(150, 150, true));
        boundaries.add(new Wall(175, 150, true));
        boundaries.add(new Wall(175, 125, true));

        // Generate the walls
        walls = new ArrayList<>();
        if(!generateWalls(walls)) {
            System.out.println("Failed to generate the walls");
        }


        start = new Wall(0, 0, false);
        stop = new Wall(walls.get(walls.size() - 1).getX(), walls.get(walls.size() - 1).getY(), false);

        path = new ArrayList<>();
        findPath(walls);
    }

    /**
     * Generate a list of walls for each cell on the grid
     *
     * @param walls             The list to add the walls into
     * @return                  Whether or not the walls were added
     */
    public boolean generateWalls(List<Wall> walls) {

        if(!walls.isEmpty()) {
            System.out.println("Please provide an empty list before generating walls");
            return false;
        }

        for(int y = 0; y < this.height - (this.scale); y++) {
            for(int x = 0; x < this.width - (this.scale); x++) {

                if(y % (this.scale) == 0 && x % (this.scale) == 0) {
                    walls.add(new Wall(x, y, false));
                }
            }
        }

        for(Wall wall : walls) {
            for(Wall boundary : boundaries) {
                if(wall.getX() == boundary.getX() && wall.getY() == boundary.getY()) {
                    wall.setIsboundary(true);
                }
            }
        }

        return true;
    }

    /**
     * Finds the best path between the start and stop points based on the weighted values of each wall cell
     *
     * @param walls
     * @return
     */
    public boolean findPath(List<Wall> walls) {

        if(walls.isEmpty()) {
            System.out.println("No grid was provided.");
            return false;
        }

        for(Wall wall : this.walls) {
            if(!wall.getIsboundary()) {
                this.path.add(new Wall(wall.getX(), wall.getY(), wall.getIsboundary()));
            }
        }

        return true;
    }

    public double findHeuristic(Wall wall) {
        return -1.0;
    }

    public double findCost(Wall wall) {
        return -1.0;
    }

    /**
     * Renders the walls in colors according to their type (start, stop, boundary)
     *
     */
    public void render(Graphics g) {

        for(Wall wall : walls) {

            Color color = (wall.getIsboundary()) ? Color.black : Color.white;

            if(wall.getX() == start.getX() && wall.getY() == start.getY()) {
                color = Color.green;
            } else if(wall.getX() == stop.getX() && wall.getY() == stop.getY()) {
                color = Color.red;
            }

            g.setColor(color);
            g.fillRect(wall.x, wall.y, this.scale, this.scale);

            g.setColor(Color.gray);
            g.drawRect(wall.x, wall.y, this.scale, this.scale);
        }
    }

    private class Wall {

        private int x;
        private int y;
        private boolean isboundary;

        public Wall(int x, int y, boolean isboundary) {
            this.x = x;
            this.y = y;
            this.isboundary = isboundary;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public boolean getIsboundary() {
            return isboundary;
        }

        public void setIsboundary(boolean isboundary) {
            this.isboundary = isboundary;
        }
    }
}
