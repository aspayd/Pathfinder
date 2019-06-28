package com.adamspayd;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;

/**
 * Pathfinder
 *
 * @author Adam
 * @since 6/21/2019
 */
public class Window extends Canvas {

    private JFrame frame;
    private Canvas canvas;
    private BufferStrategy bs;

    private int width, height;

    public Window(int width, int height, String title, Main main) {

        Dimension frameSize = new Dimension(width, height);

        frame = new JFrame(title);

        frame.setPreferredSize(frameSize);
        frame.setMinimumSize(frameSize);
        frame.setMaximumSize(frameSize);

        frame.setLayout(new BorderLayout());
        frame.setLocationRelativeTo(null);
        frame.add(main);
        frame.pack();
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
