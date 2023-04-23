package sk.stuba.fei.uim.oop.gui;

import sk.stuba.fei.uim.oop.controls.GameLogic;

import javax.swing.*;
import java.awt.*;

public class Game {



        public Game() {
            JFrame frame = new JFrame("Water Pipes");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 890);
            frame.getContentPane().setBackground(Color.GRAY);
            frame.setResizable(false);
            frame.setFocusable(true);
            frame.requestFocusInWindow();

            JButton buttonRestart = new JButton("RESTART");
            JButton buttonCheck = new JButton("CHECK");

            GameLogic logic = new GameLogic(frame, buttonRestart, buttonCheck);

            buttonRestart.addActionListener(logic);
            buttonRestart.setFocusable(false);
            buttonCheck.addActionListener(logic);
            buttonCheck.setFocusable(false);

            JPanel sideMenu = new JPanel();
            sideMenu.setBackground(Color.LIGHT_GRAY);

            JSlider slider = new JSlider(JSlider.HORIZONTAL, 8, 12, 8);
            slider.setMinorTickSpacing(2);
            slider.setMajorTickSpacing(2);
            slider.setSnapToTicks(true);
            slider.setPaintTicks(true);
            slider.setPaintLabels(true);
            slider.addChangeListener(logic);

            sideMenu.setLayout(new GridLayout(2,2 ));
            sideMenu.add(logic.getLabel());
            sideMenu.add(buttonRestart);
            sideMenu.add(buttonCheck);
            sideMenu.add(logic.getBoardSizeLabel());
            sideMenu.add(slider);
            frame.add(sideMenu, BorderLayout.PAGE_START);

            frame.addKeyListener(logic);

            frame.setVisible(true);
        }
    }

