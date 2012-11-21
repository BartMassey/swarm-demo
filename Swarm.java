/*
 * Copyright (c) 2011 Bart Massey
 * [This program is licensed under the "MIT License"]
 * Please see the file COPYING in the source
 * distribution of this software for license terms.
 */

import java.lang.*;
import java.util.*;
import javax.swing.*;
import java.awt.*;

/** Animated "swarm of bugs" for AI swarm demo,
 *  utilizing an extensible Java framework built
 *  for these kinds of simulations. */
public class Swarm {
    /** Run the demo. */
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

    /** Set up the top-level window in which the GUI will run. */
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
