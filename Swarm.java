/* Copyright (c) 2011 Bart Massey */

import java.lang.*;
import java.util.*;
import javax.swing.*;
import java.awt.*;

// http://download.oracle.com/javase/tutorial/uiswing/painting/step1.html


class Bug {
    static final double DT = 0.1;
    double x, y, t;
    double vx, vy, vt;
    double ax, ay, at;
    double r = 0.02;
    static final double V0 = 0.1;
    static Random prng = new Random();
    Playfield playfield;

    Bug(Playfield playfield) {
	this.playfield = playfield;
	x = prng.nextDouble();
	y = prng.nextDouble();
	t = 2 * Math.PI * prng.nextDouble();
	vx = prng.nextDouble() * V0;
	vy = prng.nextDouble() * V0;
	vt = prng.nextDouble() * V0 / (2 * Math.PI);
	ax = 0;
	ay = 0;
	at = 0;
    }

    public void paintComponent(Graphics g, int d) {
	g.setColor(Color.blue);
	int x0 = (int) Math.floor(x * d + 0.5);
	int y0 = (int) Math.floor(y * d + 0.5);
	int r0 = (int) Math.floor(r * d + 0.5);
	int x1 = x0 + (int) Math.floor(r0 * Math.cos(t) + 0.5);
	int y1 = y0 + (int) Math.floor(- r0 * Math.sin(t) + 0.5);
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
	return (dx * dx + dy * dy <= r * r);
    }

    public void step(Bug b) {
	double x0 = x;
	x += vx * DT;
	double y0 = y;
	y += vy * DT;
	double t0 = t;
	t += vt * DT;
	while (t < 0)
	    t += 2 * Math.PI;
	while (t >= 2 * Math.PI)
	    t -= 2 * Math.PI;
	if (playfield.collision(this)) {
	    x = x0; y = y0; t = t0;
	    vx = 0; vy = 0; vt = 0;
	    ax = 0; ay = 0; at = 0;
	    return;
	}
	vx += ax * DT;
	vy += ay * DT;
	vt += at * DT;
    }
}

class Playfield extends JPanel {
    static final long serialVersionUID = 0;
    Bug[] bugs;
    int d;
    // http://www.rgagnon.com/javadetails/java-0260.html
    public static final BasicStroke stroke = new BasicStroke(2.0f);

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
	if (b.wallCollision())
	    return true;
	for (int i = 0; i < bugs.length; i++)
	    if (bugs[i] != null && bugs[i] != b &&
		bugs[i].bugCollision(b))
		return true;
	return false;
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
        f.add(new Playfield(nbugs));
	f.pack();
	f.setVisible(true);
    }
}
