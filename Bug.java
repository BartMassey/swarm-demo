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

public class Bug {
    private double x, y, t;
    private double v, vt;
    private double a, at;
    private double r = 0.02;
    private static final double V0 = 0.2;
    private static Random prng = new Random();
    private Playfield playfield;
    private double dt;

    private double randV0() {
	return (2.0 * prng.nextDouble() - 1.0) * V0;
    }

    public Bug(Playfield playfield) {
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

