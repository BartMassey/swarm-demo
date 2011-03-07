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

// http://download.oracle.com/javase/tutorial/uiswing/painting/step1.html


class Bug {
    double x, y, t;
    double v, vt;
    double a, at;
    double r = 0.02;
    static final double V0 = 0.1;
    static Random prng = new Random();
    Playfield playfield;
    double dt;

    double randV0() {
	return (2.0 * prng.nextDouble() - 1.0) * V0;
    }

    Bug(Playfield playfield) {
	this.playfield = playfield;
	dt = Playfield.DT;
	x = prng.nextDouble();
	y = prng.nextDouble();
	t = 2 * Math.PI * prng.nextDouble();
	v = prng.nextDouble() * V0;
	vt = prng.nextDouble() * V0 * (2 * Math.PI);
	a = 0;
	at = 0;
    }

    public void paintComponent(Graphics g, int d) {
	g.setColor(Color.blue);
	int x0 = (int) Math.floor(x * d + 0.5);
	int y0 = (int) Math.floor(y * d + 0.5);
	int r0 = (int) Math.floor(r * d + 0.5);
	int x1 = x0 - (int) Math.floor(1.3 * r0 * Math.cos(t) + 0.5);
	int y1 = y0 - (int) Math.floor(- 1.3 * r0 * Math.sin(t) + 0.5);
	g.drawOval(x0 - r0, y0 - r0, 2 * r0, 2 * r0);
	g.setColor(Color.red);
	g.drawLine(x0, y0, x1, y1);
    }

    public boolean wallCollision() {
	return (x - r <= 0.0 || x + r >= 1.0 ||
		y - r <= 0.0 || y + r >= 1.0);
    }

    public boolean bugCollision(Bug b) {
	double dx = b.x - x;
	double dy = b.y - y;
	return (dx * dx + dy * dy <= 4 * r * r);
    }

    public void thump() {
	v = 0; vt = 0;
	a = 0; at = 0;
    }

    public void step() {
	double x0 = x, y0 = y, t0 = t;
	x += v * Math.cos(t) * dt;
	y += - v * Math.sin(t) * dt;
	t += vt * dt;
	while (t < 0)
	    t += 2 * Math.PI;
	while (t >= 2 * Math.PI)
	    t -= 2 * Math.PI;
	if (playfield.collision(this)) {
	    x = x0; y = y0; t = t0;
	    return;
	}
	v += a * dt;
	vt += at * dt;
    }
}

class Playfield extends JPanel implements Runnable {
    static final long serialVersionUID = 0;
    Bug[] bugs;
    int d;
    Thread t = null;
    boolean threadSuspended = true;
    // http://www.rgagnon.com/javadetails/java-0260.html
    public static final BasicStroke stroke = new BasicStroke(2.0f);
    public static final double DT = 0.01;   // Physics time increment
    public static final double DDT = 10;   // Multiple for display time

    public Playfield(int nbugs) {
	bugs = new Bug[nbugs];
	for (int i = 0; i < nbugs; i++)
	    do
		bugs[i] = new Bug(this);
	    while(collision(bugs[i]));
    }

    public Dimension getPreferredSize() {
	return new Dimension(250,250);
    }

    public boolean collision(Bug b) {
	boolean result = false;

	if (b.wallCollision()) {
	    result = true;
	    b.thump();
	}
	for (int i = 0; i < bugs.length; i++) {
	    if (bugs[i] != null && bugs[i] != b &&
		bugs[i].bugCollision(b)) {
		result = true;
		b.thump();
		bugs[i].thump();
	    }
	}
	return result;
    }

    public void paintComponent(Graphics g) {
	super.paintComponent(g);

	Graphics2D g2d = (Graphics2D) g;
	g2d.setStroke(stroke);
	g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
			    RenderingHints.VALUE_ANTIALIAS_ON);
	Dimension ds = getSize();
	d = Math.min(ds.width, ds.height) - 5;
	if (d <= 0)
	    return;
	g.clearRect(0, 0, ds.width, ds.height);
	g.translate((ds.width - d) / 2, (ds.height - d) / 2);
	g.setColor(Color.black);
	g.drawRect(0, 0, d, d);
	// Draw Text
	for (int i = 0; i < bugs.length; i++)
	    bugs[i].paintComponent(g, d);
    }

    // http://profs.etsmtl.ca/mmcguffin/learn/java/06-threads/
    public void run() {
	while(true) {
	    long then = System.currentTimeMillis();
	    for (int j = 0; j < DDT; j++)
		for (int i = 0; i < bugs.length; i++)
		    bugs[i].step();
	    if (threadSuspended) {
		synchronized(this) {
		    while (threadSuspended) {
			try {
			    t.wait();
			} catch(InterruptedException e) {
			    System.exit(1);
			}
		    }
		}
	    }
	    repaint();
	    long interval = (long) Math.floor(1000 * DT * DDT);
	    long now = System.currentTimeMillis();
	    interval -= now - then;
	    if (interval < 0) {
		System.err.println("time overrun");
	    } else {
		try {
		    t.sleep(interval);
		} catch(InterruptedException e) {
		    System.exit(1);
		}
	    }
	}
    }

    public void start() {
	if ( t == null ) {
	    t = new Thread( this );
	    threadSuspended = false;
	    t.start();
	}
	else {
	    if ( threadSuspended ) {
		threadSuspended = false;
		synchronized( this ) {
		    notify();
		}
	    }
	}
    }

    public void stop() {
	threadSuspended = true;
    }
}

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
	Playfield p = new Playfield(nbugs);
        f.add(p);
	f.pack();
	f.setVisible(true);
	p.start();
    }
}
