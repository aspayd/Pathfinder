package com.adamspayd;

import java.awt.*;
import java.awt.image.BufferStrategy;

/**
 * Pathfinder
 *
 * @author Adam
 * @since 6/21/2019
 */
public class Main extends Canvas implements Runnable {

    private Thread thread;

    private NodeHandler nh;

    private Window window;

    private int winWidth = 500, winHeight = 500;
    private int width, height;

    private int gridScale = 25;
    private boolean drawGrid;

    private boolean running = false;
    private String title = "A* Pathfinder";
    private String version = "v1.0";

    public Main() {

        window = new Window(winWidth, winHeight, title + " " + version, this);

        width = window.getWidth();
        height = window.getHeight();

        nh = new NodeHandler(width, height, gridScale);

        start();
    }

    public synchronized void start() {
        thread = new Thread(this);
        thread.start();

        running = true;
        drawGrid = true;
    }

    public synchronized void stop() {
        try {
            thread.join();
            running = false;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        // Variable timestep loop function
        boolean render = false;
        double timeInitial = 0;
        double timeFinal = System.nanoTime() / 1e9;
        double delta = 0;
        double unprocessedTime = 0;
        final double UPDATE_CAP = 1.0/120.0;

        double frameTime = 0;
        int frames = 0;
        int fps = 0;

        while(running) {

            render = false;

            timeInitial = System.nanoTime()  / 1e9;
            delta = timeInitial - timeFinal;
            timeFinal = timeInitial;

            // Add to delta time in separate variables
            unprocessedTime += delta;
            frameTime += delta;

            // Render the window by making up for the time unprocessed
            while(unprocessedTime >= UPDATE_CAP) {
                unprocessedTime -= UPDATE_CAP;
                render = true;

                if(frameTime >= 1.0) {
                    frameTime = 0;
                    fps = frames;
                    frames = 0;
                    System.out.println("FPS: " +  fps);
                }
            }

            if(render) {

                render();

                // @todo main logic (tick)

                frames++;

            } else {
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void render() {
        BufferStrategy bs = this.getBufferStrategy();

        if(bs == null) {
            this.createBufferStrategy(2);
            return;
        }

        Graphics g = bs.getDrawGraphics();
        g.setColor(Color.black);

        if(drawGrid) {

            nh.render(g);

            drawGrid = false;
        }

        bs.show();
    }

    public static void main(String[] args) {
        new Main();
    }
}