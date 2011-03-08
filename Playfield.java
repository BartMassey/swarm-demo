/*
 * Copyright (c) 2011 Bart Massey
 * [This program is licensed under the "MIT License"]
 * Please see the file COPYING in the source
 * distribution of this software for license terms.
 */

// http://download.oracle.com/javase/tutorial/uiswing/painting/step1.html

import java.lang.*;
import java.util.*;
import javax.swing.*;
import java.awt.*;

public class Playfield extends JPanel implements Runnable {
    static final long serialVersionUID = 0;
    private Agent[] agents;
    private int d;
    private Thread t = null;
    private boolean threadSuspended = true;
    private static Random prng = new Random();
    // http://www.rgagnon.com/javadetails/java-0260.html
    public static BasicStroke stroke;
    public static double DT;   // Physics time increment
    public static int DDT;   // Multiple for display time

    private static void shuffle(Agent[] agents) {
	for (int i = 0; i < agents.length - 1; i++) {
	    int j = prng.nextInt(agents.length - i) + i;
	    Agent tmp = agents[j];
	    agents[j] = agents[i];
	    agents[i] = tmp;
	}
    }

    public Playfield(int nagents, double dt, int ddt) {
	DT = dt;
	DDT = ddt;
	agents = new Agent[nagents];
	for (int i = 0; i < nagents; i++)
	    do
		agents[i] = new Bug(this);
	    while(collision(agents[i]));
    }

    public Dimension getPreferredSize() {
	return new Dimension(250,250);
    }

    public boolean collision(Agent b) {
	boolean result = false;

	if (b.wallCollision()) {
	    result = true;
	    b.thump();
	}
	for (int i = 0; i < agents.length; i++) {
	    if (agents[i] != null && agents[i].id != b.id &&
		agents[i].thingCollision(b)) {
		result = true;
		b.thump();
		agents[i].thump();
	    }
	}
	return result;
    }

    public void paintComponent(Graphics g) {
	super.paintComponent(g);

	Dimension ds = getSize();
	d = Math.min(ds.width, ds.height) - 5;
	float strokeWidth = (float) d / 200.0f;
	stroke = new BasicStroke(strokeWidth);
	Graphics2D g2d = (Graphics2D) g;
	g2d.setStroke(stroke);
	g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
			    RenderingHints.VALUE_ANTIALIAS_ON);
	if (d <= 0)
	    return;
	g.clearRect(0, 0, ds.width, ds.height);
	g.translate((ds.width - d) / 2, (ds.height - d) / 2);
	g.setColor(Color.black);
	g.drawRect(0, 0, d, d);
	for (int i = 0; i < agents.length; i++)
	    agents[i].paintComponent(g, d);
    }

    // http://profs.etsmtl.ca/mmcguffin/learn/java/06-threads/
    public void run() {
	while(true) {
	    long then = System.currentTimeMillis();
	    for (int j = 0; j < DDT; j++) {
		for (int i = 0; i < agents.length; i++)
		    agents[i].control(agents, null);
		shuffle(agents);
		for (int i = 0; i < agents.length; i++)
		    agents[i].step();
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
	}
    }

    public void start() {
	if (t == null) {
	    t = new Thread(this);
	    threadSuspended = false;
	    t.start();
	} else {
	    if (threadSuspended) {
		threadSuspended = false;
		synchronized(this) {
		    t.notify();
		}
	    }
	}
    }

    public void stop() {
	threadSuspended = true;
    }
}
