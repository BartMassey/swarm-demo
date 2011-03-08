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
    double a;
    double dt = Playfield.DT;
    AI ai;
    double AMIN, AMAX, VTMIN, VTMAX, CDRAG;

    public Agent(Playfield playfield) {
	super(playfield);
    }

    abstract public void thump();

    public void step() {
	double x0 = x, y0 = y, t0 = t;
	x += v * Math.cos(t) * dt;
	y += - v * Math.sin(t) * dt;
	t += vt * dt;
	while (t < - Math.PI)
	    t += 2 * Math.PI;
	while (t >= Math.PI)
	    t -= 2 * Math.PI;
       	if (playfield.collision(this)) {
	    x = x0; y = y0; t = t0;
	    return;
	}
	v += a * dt;
	v -= v * r * r * CDRAG * dt;
    }

    static double clamp(double v, double lo, double hi) {
	if (v < lo)
	    return lo;
	if (v > hi)
	    return hi;
	return v;
    }

    public void control(Agent[] agents, Thing[] things) {
	MeView meV = new MeView();
	meV.t = t;
	meV.vt = vt;
	meV.x = x;
	meV.y = y;
	meV.r = r;
	meV.a = a;
	meV.AMIN = AMIN;
	meV.AMAX = AMAX;
	meV.VTMIN = VTMIN;
	meV.VTMAX = VTMAX;
	meV.CDRAG = CDRAG;
	AgentView[] aVs = new AgentView[agents.length];
	for (int i = 0; i < aVs.length; i++) {
	    aVs[i] = new AgentView();
	    aVs[i].c = agents[i].getClass();
	    aVs[i].r = agents[i].r;
	    aVs[i].t = agents[i].t;
	    aVs[i].v = agents[i].v;
	    aVs[i].x = agents[i].x;
	    aVs[i].y = agents[i].y;
	}
	if (things != null)
	    throw new Error("internal error: no things yet");
	ai.control(meV, aVs, null);
	a = clamp (meV.a, AMIN, AMAX);
	vt = clamp (meV.vt, VTMIN, VTMAX);
    }
}
