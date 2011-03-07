/*
 * Copyright (c) 2011 Bart Massey
 * [This program is licensed under the "MIT License"]
 * Please see the file COPYING in the source
 * distribution of this software for license terms.
 */

// Animated "swarm of bugs" for AI swarm demo.

import java.lang.*;
import java.util.*;
import javax.swing.*;
import java.awt.*;

public class Swarm {
    public static void main(String[] args) {
	if (args.length != 1) {
	    System.err.println ("usage: Swarm <nbugs>");
	    System.exit(1);
	}
	final int nbugs = Integer.parseInt(args[0]);
	SwingUtilities.invokeLater(new Runnable() {
		public void run() {
		    createAndShowGUI(nbugs);
		}
	    });
    }

    private static void createAndShowGUI(int nbugs) {
	JFrame f = new JFrame("Swarm Demo");
	f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	double dt = 0.01;
	int ddt = 20;
	if (nbugs > 20) {
	    dt = 0.1;
	    ddt = 2;
	}
	Playfield p = new Playfield(nbugs, dt, ddt);
        f.add(p);
	f.pack();
	f.setVisible(true);
	p.start();
    }
}
