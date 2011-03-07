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

abstract public class Agent extends Motile {
    double t;
    double v, vt;
    double a, at;
    double dt = Playfield.DT;

    public Agent(Playfield playfield) {
	super(playfield);
    }

    abstract public void thump();

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
